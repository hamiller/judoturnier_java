package de.sinnix.judoturnier.adapter.secondary;

import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Begegnung;
import de.sinnix.judoturnier.model.Matte;
import de.sinnix.judoturnier.model.Runde;
import de.sinnix.judoturnier.model.Turnier;
import de.sinnix.judoturnier.model.Wertung;
import de.sinnix.judoturnier.model.WettkampfGruppe;
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
	private WertungJpaRepository         wertungJpaRepository;
	@Autowired
	private BegegnungJpaRepository       begegnungJpaRepository;
	@Autowired
	private WettkampfGruppeJpaRepository wettkampfGruppeJpaRepository;
	@Autowired
	private WertungConverter             wertungConverter;
	@Autowired
	private BegegnungConverter           begegnungConverter;
	@Autowired
	private WettkampfGruppeConverter     wettkampfGruppeConverter;
	@Autowired
	private TurnierJpaRepository         turnierJpaRepository;
	@Autowired
	private TurnierConverter             turnierConverter;

	public Begegnung ladeBegegnung(UUID begegnungId) {
		BegegnungJpa begegnungJpa = begegnungJpaRepository.findById(begegnungId.toString()).orElseThrow();
		List<WettkampfGruppeJpa> wettkampfGruppeJpaList = wettkampfGruppeJpaRepository.findAll();

		return begegnungConverter.convertToBegegnung(begegnungJpa, wettkampfGruppeJpaList);
	}

	public List<Begegnung> ladeAlleBegegnungen(UUID turnierId) {
		List<BegegnungJpa> begegnungenJpa = begegnungJpaRepository.findAllByTurnierUUID(turnierId.toString());
		return begegnungenJpa.stream().map(begegnungJpa -> begegnungConverter.convertToBegegnung(begegnungJpa, List.of())).toList();
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
			.peek(b -> logger.info("found {}", b))
			.sorted(Comparator
				.comparing(Begegnung::getGesamtBegegnung)
				.thenComparing(begegnung -> begegnung.getBegegnungId().rundenTyp)
				.thenComparing(begegnung -> begegnung.getBegegnungId().akuellePaarung)
			)
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
			Integer rundeGesamt = ersteBegegnung.getGesamtBegegnung();
			Integer matteId = ersteBegegnung.getMatteId();
			Altersklasse altersklasse = ersteBegegnung.getWettkaempfer1().isPresent() ? ersteBegegnung.getWettkaempfer1().get().altersklasse() : Altersklasse.PAUSE;
			WettkampfGruppe gruppe = ersteBegegnung.getWettkampfGruppe();

			// Erstelle eine neue Runde
			Runde runde = new Runde(rundeId, mattenRunde, gruppenRunde, rundeGesamt, matteId, altersklasse, gruppe, begegnungenInRunde);

			// Füge die Runde zur Matte hinzu
			Matte matte = matteMap.computeIfAbsent(matteId, k -> new Matte(matteId, new ArrayList<>()));
			matte.runden().add(runde); // Annahme, dass die Liste modifizierbar ist
		}

		return matteMap;
	}

	public List<Runde> ladeWettkampfgruppeRunden(UUID wettkampfgruppeUUID, UUID turnierUUID) {
		List<BegegnungJpa> begegnungenJpaList = begegnungJpaRepository.findAllByTurnierUUID(turnierUUID.toString());
		Optional<WettkampfGruppeJpa> wettkampfGruppeJpa = wettkampfGruppeJpaRepository.findByUuidAndTurnierUUID(wettkampfgruppeUUID.toString(), turnierUUID.toString());

		var begegnungenList = begegnungenJpaList.stream()
			.filter(jpa -> jpa.getWettkampfGruppeId().equals(wettkampfgruppeUUID.toString()))
			.map(jpa -> begegnungConverter.convertToBegegnung(jpa, wettkampfGruppeJpa))
			.sorted(Comparator
				.comparing(Begegnung::getGesamtBegegnung)
				.thenComparing(begegnung -> begegnung.getBegegnungId().rundenTyp)
				.thenComparing(begegnung -> begegnung.getBegegnungId().akuellePaarung)
			)
			.toList();

		// Map, um die Runden zu sammeln
		Map<UUID, List<Begegnung>> rundenMap = new LinkedHashMap<>();

		// Iteriere über die Begegnungen, um sie nach Runde zu gruppieren
		for (Begegnung begegnung : begegnungenList) {
			UUID rundeId = begegnung.getRundeId();
			rundenMap.computeIfAbsent(rundeId, k -> new ArrayList<>()).add(begegnung);
		}

		List<Runde> rundenList = new ArrayList<>();

		// Iteriere über die RundenMap, um Matte-Objekte zu erstellen
		for (Map.Entry<UUID, List<Begegnung>> entry : rundenMap.entrySet()) {
			UUID rundeId = entry.getKey();
			List<Begegnung> begegnungenInRunde = entry.getValue();

			var ersteBegegnung = begegnungenInRunde.get(0);
			Integer mattenRunde = ersteBegegnung.getMattenRunde();
			Integer gruppenRunde = ersteBegegnung.getGruppenRunde();
			Integer rundeGesamt = ersteBegegnung.getGesamtBegegnung();
			Integer matteId = ersteBegegnung.getMatteId();
			Altersklasse altersklasse = ersteBegegnung.getWettkaempfer1().isPresent() ? ersteBegegnung.getWettkaempfer1().get().altersklasse() : Altersklasse.PAUSE;
			WettkampfGruppe gruppe = ersteBegegnung.getWettkampfGruppe();

			// Erstelle eine neue Runde
			rundenList.add(new Runde(rundeId, mattenRunde, gruppenRunde, rundeGesamt, matteId, altersklasse, gruppe, begegnungenInRunde));
		}

		return rundenList;
	}

	public void speichereMatten(List<Matte> mattenList) {
		logger.info("Speichere alle Matten");
		for (Matte matte : mattenList) {
			speichereMatte(matte);
		}
	}

	public void speichereMatte(Matte matte) {
		List<BegegnungJpa> begegnungJpaList = new ArrayList<>();
		for (Runde runde : matte.runden()) {
			logger.trace("Speichere Runde {} für Matte {}", runde, runde.matteId());

			UUID wettkamfpGruppeID = runde.gruppe().id();
			Optional<WettkampfGruppeJpa> optionalWettkampfGruppeJpa = wettkampfGruppeJpaRepository.findById(wettkamfpGruppeID.toString());
			if (optionalWettkampfGruppeJpa.isEmpty()) {
				logger.trace("Erstelle neue Wettkampfgruppe {} {}", runde.gruppe().name(), wettkamfpGruppeID);
				WettkampfGruppeJpa wettkampfGruppeJpa = wettkampfGruppeConverter.convertFromWettkampfGruppe(runde.gruppe());
				wettkampfGruppeJpa = wettkampfGruppeJpaRepository.save(wettkampfGruppeJpa);

				optionalWettkampfGruppeJpa = Optional.of(wettkampfGruppeJpa);
			} else {
				logger.trace("Es existiert schon eine Wettkampfgruppe {}", wettkamfpGruppeID);
			}

			var wettkampfGruppe = wettkampfGruppeConverter.convertToWettkampfGruppe(optionalWettkampfGruppeJpa.get());
			var rundeId = runde.rundeId();

			for (Begegnung begegnung : runde.begegnungen()) {
				Begegnung newBegegnung = new Begegnung();
				newBegegnung.setRundeId(rundeId);
				newBegegnung.setMatteId(matte.id());
				newBegegnung.setGruppenRunde(runde.gruppenRunde());
				newBegegnung.setMattenRunde(runde.mattenRunde());
				newBegegnung.setGesamtBegegnung(runde.rundeGesamt());
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

	public void loescheAlleMatten(UUID turnierUUID) {
		logger.info("Lösche alle Matten für Turnier {}", turnierUUID);
		try {
			begegnungJpaRepository.deleteAllByTurnierUUID(turnierUUID.toString());
			wettkampfGruppeJpaRepository.deleteAllByTurnierUUID(turnierUUID.toString());
		} catch (Exception e) {
			logger.error("Fehler", e);
		}
	}

	public void loescheWettkaempfeMitAltersklasse(Altersklasse altersklasse, UUID turnierUUID) {
		logger.info("loesche Wettkaempfe mit Altersklasse {}", altersklasse);
		begegnungJpaRepository.findAllByTurnierUUID(turnierUUID.toString()).stream()
			.filter(begegnungJpa -> begegnungJpa.getWettkaempfer1().getAltersklasse().equals(altersklasse.name()))
			.forEach(begegnungJpa -> begegnungJpaRepository.deleteById(begegnungJpa.getUuid()));
	}

	public void speichereBegegnung(Begegnung begegnung) {
		begegnungJpaRepository.save(begegnungConverter.convertFromBegegnung(begegnung));
	}

	public List<Turnier> ladeAlleTurniere() {
		return turnierJpaRepository.findAll().stream().map(jpa -> turnierConverter.convertToTurnier(jpa)).toList();
	}

	public Turnier speichereTurnier(Turnier neuesTurnier) {
		TurnierJpa jpa = turnierJpaRepository.save(turnierConverter.convertFromTurnier(neuesTurnier));
		return turnierConverter.convertToTurnier(jpa);
	}

	public Turnier ladeTurnier(UUID turnierid) {
		return turnierJpaRepository
			.findById(turnierid.toString())
			.map(t -> turnierConverter.convertToTurnier(t))
			.orElseThrow();
	}

	public Optional<Begegnung> findeBegegnung(Begegnung.RundenTyp rundenTyp, int runde, int paarung, UUID wettkampfGruppeUUID, UUID turnierUUID) {
		List<WettkampfGruppeJpa> wettkampfGruppeJpaList = wettkampfGruppeJpaRepository.findAll();
		List<BegegnungJpa> begegnungJpas = begegnungJpaRepository.findAllByTurnierUUIDAndWettkampfGruppeId(turnierUUID.toString(), wettkampfGruppeUUID.toString());
		return begegnungJpas.stream()
			.filter(b -> b.getRundenTyp().equals(rundenTyp.getValue()))
			.filter(b -> b.getRunde().equals(runde))
			.filter(b -> b.getPaarung().equals(paarung))
			.map(b -> begegnungConverter.convertToBegegnung(b, wettkampfGruppeJpaList))
			.findFirst();
	}
}
