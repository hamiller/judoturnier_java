package de.sinnix.judoturnier.adapter.secondary;

import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Begegnung;
import de.sinnix.judoturnier.model.GruppenRunde;
import de.sinnix.judoturnier.model.Matte;
import de.sinnix.judoturnier.model.Runde;
import de.sinnix.judoturnier.model.Turnier;
import de.sinnix.judoturnier.model.Wertung;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class TurnierRepository {

	private static final Logger logger = LogManager.getLogger(TurnierRepository.class);

	@Autowired
	private WertungJpaRepository wertungJpaRepository;
	@Autowired
	private BegegnungJpaRepository begegnungJpaRepository;
	@Autowired
	private WettkampfGruppeJpaRepository wettkampfGruppeJpaRepository;
	@Autowired
	private WertungConverter wertungConverter;
	@Autowired
	private WettkaempferConverter wettkaempferConverter;
	@Autowired
	private BegegnungConverter begegnungConverter;
	@Autowired
	private WettkampfGruppeConverter wettkampfGruppeConverter;
	@Autowired
	private TurnierJpaRepository turnierJpaRepository;
	@Autowired
	private TurnierConverter     turnierConverter;

	public Begegnung ladeBegegnung(Integer begegnungId) {
		BegegnungJpa begegnungJpa = begegnungJpaRepository.findById(begegnungId).orElseThrow();
		List<WettkampfGruppeJpa> wettkampfGruppeJpaList = wettkampfGruppeJpaRepository.findAll();

		return begegnungConverter.convertToBegegnung(begegnungJpa, wettkampfGruppeJpaList);
	}

	public void speichereWertung(Wertung wertung) {
		WertungJpa jpa = wertungConverter.convertFromWertung(wertung);
		wertungJpaRepository.save(jpa);
	}

	public Map<Integer, Matte> ladeMatten(UUID turnierUUID) {
		List<BegegnungJpa> begegnungenJpaList = begegnungJpaRepository.findAllByTurnierUUID(turnierUUID.toString());
		List<WettkampfGruppeJpa> wettkampfGruppeJpaList = wettkampfGruppeJpaRepository.findAllByTurnierUUID(turnierUUID.toString());

		var begegnungenList = begegnungenJpaList.stream()
			.map(jpa -> begegnungConverter.convertToBegegnung(jpa, wettkampfGruppeJpaList))
			.toList();

		Map<Integer, Matte> matteMap = new HashMap<>();
		for (Begegnung b : begegnungenList) {
			Integer matteId = b.getMatteId();
			Runde r = new Runde(
				b.getBegegnungId(),
				b.getMattenRunde(),
				b.getGruppenRunde(),
				b.getGesamtRunde(),
				matteId,
				b.getWettkaempfer1() == null ? Altersklasse.PAUSE : b.getWettkaempfer1().altersklasse(), // wir haben eine PAUSE
				b.getWettkampfGruppe(),
				List.of(b));
			if (!matteMap.containsKey(matteId)) {
				List<Runde> rundeList = new ArrayList<>();
				matteMap.put(matteId, new Matte(matteId, rundeList));
			}
			matteMap.get(matteId).runden().add(r);
		}

		return matteMap;
	}

	public void speichereMatten(List<Matte> mattenList) {
		logger.info("Speichere alle Matten");
		for (Matte matte : mattenList) {
			speichereMatte(matte);
		}
	}

	@Transactional
	public void speichereMatte(Matte matte) {
		List<BegegnungJpa> begegnungJpaList = new ArrayList<>();
		for (Runde runde : matte.runden()) {
			Integer wettkampfGruppeId;
			Optional<WettkampfGruppeJpa> optionalWettkampfGruppeJpa = wettkampfGruppeJpaRepository.findById(runde.gruppe().id());

			if (optionalWettkampfGruppeJpa.isPresent()) {
				wettkampfGruppeId = optionalWettkampfGruppeJpa.get().getId();
			} else {
				wettkampfGruppeId = wettkampfGruppeJpaRepository.save(wettkampfGruppeConverter.convertFromWettkampfGruppe(runde.gruppe()))
					.getId();
			}

			for (Begegnung begegnung : runde.begegnungen()) {
				BegegnungJpa begegnungJpa = new BegegnungJpa();
				begegnungJpa.setMatteId(matte.id());
				begegnungJpa.setGruppenRunde(runde.gruppenRunde());
				begegnungJpa.setMattenRunde(runde.mattenRunde());
				begegnungJpa.setGesamtRunde(runde.rundeGesamt());
				begegnungJpa.setWettkaempfer1(wettkaempferConverter.convertFromWettkaempfer(begegnung.getWettkaempfer1()));
				begegnungJpa.setWettkaempfer2(wettkaempferConverter.convertFromWettkaempfer(begegnung.getWettkaempfer2()));
				begegnungJpa.setWettkampfGruppeId(wettkampfGruppeId);
				begegnungJpa.setTurnierUUID(begegnung.getTurnierUUID().toString());

				begegnungJpaList.add(begegnungJpa);
			}
		}

		logger.info("Speichere Begegnungen-Liste (Größe: {}) für Matte {}", begegnungJpaList.size(), matte.id());
		begegnungJpaRepository.saveAll(begegnungJpaList);
	}

	@Transactional
	public void loescheAlleMatten(UUID turnierUUID) {
		logger.info("Lösche alle Matten für Turnier {}", turnierUUID);
		try {
			begegnungJpaRepository.deleteAllByTurnierUUID(turnierUUID.toString());
			wettkampfGruppeJpaRepository.deleteAllByTurnierUUID(turnierUUID.toString());
		}catch (Exception e) {
			logger.error("Fehler", e);
		}
	}

	@Transactional
	public void loescheWettkaempfeMitAltersklasse(Altersklasse altersklasse, UUID turnierUUID) {
		logger.info("loesche Wettkaempfe mit Altersklasse {}", altersklasse);
		begegnungJpaRepository.findAllByTurnierUUID(turnierUUID.toString()).stream()
			.filter(begegnungJpa -> begegnungJpa.getWettkaempfer1().getAltersklasse().equals(altersklasse.name()))
			.forEach(begegnungJpa -> begegnungJpaRepository.deleteById(begegnungJpa.getId()));
	}

	public void speichereBegegnung(Begegnung begegnung) {
		begegnungJpaRepository.save(begegnungConverter.convertFromBegegnung(begegnung));
	}

	public List<Turnier> ladeAlleTurniere() {
		return turnierJpaRepository.findAll().stream().map(jpa -> turnierConverter.convertToTurnier(jpa)).toList();
	}
}
