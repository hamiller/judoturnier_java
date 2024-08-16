package de.sinnix.judoturnier.adapter.secondary;

import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Begegnung;
import de.sinnix.judoturnier.model.Matte;
import de.sinnix.judoturnier.model.Runde;
import de.sinnix.judoturnier.model.Turnier;
import de.sinnix.judoturnier.model.Wertung;
import de.sinnix.judoturnier.model.WettkampfGruppe;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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
			.sorted(Comparator.comparing(Begegnung::getGesamtRunde))
			.toList();

		// Map, um die Runden zu sammeln
		Map<UUID, List<Begegnung>> rundenMap = new LinkedHashMap<>();

		// Iteriere über die Begegnungen, um sie nach Runde zu gruppieren
		for (Begegnung begegnung : begegnungenList) {
			UUID rundeId = begegnung.getRundeId();
			rundenMap.computeIfAbsent(rundeId, k -> new ArrayList<>()).add(begegnung);
		}

		Map<Integer, Matte> matteMap = new HashMap<>();

		// Iteriere über die RundenMap, um Matte-Objekte zu erstellen
		for (Map.Entry<UUID, List<Begegnung>> entry : rundenMap.entrySet()) {
			UUID rundeId = entry.getKey();
			List<Begegnung> begegnungenInRunde = entry.getValue();

			var ersteBegegnung = begegnungenInRunde.get(0);
			Integer mattenRunde = ersteBegegnung.getMattenRunde();
			Integer gruppenRunde = ersteBegegnung.getGruppenRunde();
			Integer rundeGesamt = ersteBegegnung.getGesamtRunde();
			Integer matteId = ersteBegegnung.getMatteId();
			Altersklasse altersklasse = ersteBegegnung.getWettkaempfer1().isPresent() ? ersteBegegnung.getWettkaempfer1().get().altersklasse(): Altersklasse.PAUSE;
			WettkampfGruppe gruppe = ersteBegegnung.getWettkampfGruppe();

			// Erstelle eine neue Runde
			Runde runde = new Runde(rundeId, mattenRunde, gruppenRunde, rundeGesamt, matteId, altersklasse, gruppe, begegnungenInRunde);

			// Füge die Runde zur Matte hinzu
			Matte matte = matteMap.computeIfAbsent(matteId, k -> new Matte(matteId, new ArrayList<>()));
			matte.runden().add(runde); // Annahme, dass die Liste modifizierbar ist
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
			Integer wettkamfpGruppeID = runde.gruppe().id();
			Optional<WettkampfGruppeJpa> optionalWettkampfGruppeJpa = wettkampfGruppeJpaRepository.findById(wettkamfpGruppeID);
			if (optionalWettkampfGruppeJpa.isEmpty()) {
				logger.info("Erstelle neue Wettkampfgruppe {} {}", runde.gruppe().name(), wettkamfpGruppeID);
				WettkampfGruppeJpa wettkampfGruppeJpa = wettkampfGruppeConverter.convertFromWettkampfGruppe(runde.gruppe());
				wettkampfGruppeJpa = wettkampfGruppeJpaRepository.save(wettkampfGruppeJpa);

				optionalWettkampfGruppeJpa = Optional.of(wettkampfGruppeJpa);
			}

			var wettkampfGruppe = wettkampfGruppeConverter.convertToWettkampfGruppe(optionalWettkampfGruppeJpa.get());
			var rundeId = runde.rundeId();

			for (Begegnung begegnung : runde.begegnungen()) {
				Begegnung newBegegnung = new Begegnung();
				newBegegnung.setRundeId(rundeId);
				newBegegnung.setMatteId(matte.id());
				newBegegnung.setGruppenRunde(runde.gruppenRunde());
				newBegegnung.setMattenRunde(runde.mattenRunde());
				newBegegnung.setGesamtRunde(runde.rundeGesamt());
				newBegegnung.setWettkaempfer1(begegnung.getWettkaempfer1());
				newBegegnung.setWettkaempfer2(begegnung.getWettkaempfer2());
				newBegegnung.setWettkampfGruppe(wettkampfGruppe);
				newBegegnung.setTurnierUUID(begegnung.getTurnierUUID());
				newBegegnung.setWertungen(List.of());
				newBegegnung.setBegegnungId(begegnung.getBegegnungId());

				BegegnungJpa begegnungJpa = begegnungConverter.convertFromBegegnung(newBegegnung);
				rundeId = UUID.fromString(begegnungJpa.getRundeUUID());
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
