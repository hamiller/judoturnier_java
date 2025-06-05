package de.sinnix.judoturnier.adapter.secondary;

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
	@Autowired
	private WettkaempferJpaRepository    wettkaempferJpaRepository;


	public Begegnung ladeBegegnung(UUID begegnungId) {
		BegegnungJpa begegnungJpa = begegnungJpaRepository.findById(begegnungId).orElseThrow();
		List<WettkampfGruppeJpa> wettkampfGruppeJpaList = wettkampfGruppeJpaRepository.findAll();

		return begegnungConverter.convertToBegegnung(begegnungJpa, wettkampfGruppeJpaList);
	}

	public List<Begegnung> ladeAlleBegegnungen(UUID turnierId) {
		List<BegegnungJpa> begegnungenJpa = begegnungJpaRepository.findAllByTurnierUUID(turnierId);
		List<WettkampfGruppeJpa> wettkampfGruppeJpaList = wettkampfGruppeJpaRepository.findAll();
		return begegnungenJpa.stream().map(begegnungJpa -> begegnungConverter.convertToBegegnung(begegnungJpa, wettkampfGruppeJpaList)).toList();
	}

	public void speichereWertung(Wertung wertung) {
		WertungJpa jpa = wertungConverter.convertFromWertung(wertung);
		wertungJpaRepository.save(jpa);
	}

	public Map<Integer, Matte> ladeMatten(UUID turnierUUID) {
		List<BegegnungJpa> begegnungenJpaList = begegnungJpaRepository.findAllByTurnierUUID(turnierUUID);
		List<WettkampfGruppeJpa> wettkampfGruppeJpaList = wettkampfGruppeJpaRepository.findAllByTurnierUUID(turnierUUID);

		var begegnungenList = begegnungenJpaList.stream()
			.map(jpa -> begegnungConverter.convertToBegegnung(jpa, wettkampfGruppeJpaList))
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
		List<BegegnungJpa> begegnungenJpaList = begegnungJpaRepository.findAllByTurnierUUID(turnierUUID);
		Optional<WettkampfGruppeJpa> wettkampfGruppeJpa = wettkampfGruppeJpaRepository.findByIdAndTurnierUUID(wettkampfgruppeUUID, turnierUUID);

		var begegnungenList = begegnungenJpaList.stream()
			.filter(jpa -> jpa.getWettkampfGruppeId().equals(wettkampfgruppeUUID))
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

	@Transactional
	public void speichereMatten(List<Matte> mattenList) {
		logger.info("Speichere alle Matten");
		for (Matte matte : mattenList) {
			speichereMatte(matte);
		}
	}

	@Transactional
	public void speichereMatte(Matte matte) {
		logger.info("Speichere Begegnungen für Matte {}", matte.id());

		List<BegegnungJpa> begegnungJpaList = new ArrayList<>();
		for (Runde runde : matte.runden()) {
			logger.debug("Speichere Runde {} für Matte {}", runde, runde.matteId());

			WettkampfGruppeJpa wkgJpa = wettkampfGruppeJpaRepository.findById(runde.gruppe().id())
				.orElseThrow(() -> {
					logger.error("WettkampfGruppe mit ID {} für Matte {} nicht gefunden", runde.gruppe().id(), matte.id());
					return new IllegalArgumentException("WettkampfGruppe nicht gefunden: " + runde.gruppe().id());
				});
			UUID rundeId = runde.rundeId() != null ? runde.rundeId() : UUID.randomUUID();

			for (Begegnung begegnung : runde.begegnungen()) {
				WettkaempferJpa wkJpa1 = begegnung.getWettkaempfer1().isPresent()
					? wettkaempferJpaRepository.findById(begegnung.getWettkaempfer1().get().id()).orElseGet(() -> null)
					: null;
				WettkaempferJpa wkJpa2 = begegnung.getWettkaempfer2().isPresent()
					? wettkaempferJpaRepository.findById(begegnung.getWettkaempfer2().get().id()).orElseGet(() -> null)
					: null;

				BegegnungJpa jpa = new BegegnungJpa();
				jpa.setRundeUUID(rundeId.toString());
				jpa.setRunde(begegnung.getBegegnungId().getRunde());
				jpa.setRundenTyp(begegnung.getBegegnungId().getRundenTyp().getValue());
				jpa.setPaarung(begegnung.getBegegnungId().getAkuellePaarung());
				jpa.setMatteId(matte.id());
				jpa.setMattenRunde(runde.mattenRunde());
				jpa.setGruppenRunde(runde.gruppenRunde());
				jpa.setGesamtBegegnung(runde.rundeGesamt());
				jpa.setWettkaempfer1(wkJpa1);
				jpa.setWettkaempfer2(wkJpa2);
				jpa.setWettkampfGruppeId(wkgJpa.getId());
				jpa.setWertungen(List.of());
				jpa.setTurnierUUID(begegnung.getTurnierUUID());

				begegnungJpaList.add(jpa);
			}
		}

		logger.info("Speichere Begegnungen-Liste (Größe: {}) für Matte {}", begegnungJpaList.size(), matte.id());
		begegnungJpaRepository.saveAll(begegnungJpaList);
	}

	public void loescheAlleMatten(UUID turnierUUID) {
		logger.info("Lösche alle Matten für Turnier {}", turnierUUID);
		try {
			begegnungJpaRepository.deleteAllByTurnierUUID(turnierUUID);
			wettkampfGruppeJpaRepository.deleteAllByTurnierUUID(turnierUUID);
		} catch (Exception e) {
			logger.error("Fehler", e);
		}
	}

	public void loescheWettkaempfeMitAltersklasse(Altersklasse altersklasse, UUID turnierUUID) {
		logger.info("loesche Wettkaempfe mit Altersklasse {}", altersklasse);
		begegnungJpaRepository.findAllByTurnierUUID(turnierUUID).stream()
			.filter(begegnungJpa -> begegnungJpa.getWettkaempfer1().getAltersklasse().equals(altersklasse.name()))
			.forEach(begegnungJpa -> begegnungJpaRepository.deleteById(begegnungJpa.getId()));
	}

	@Transactional
	public void speichereBegegnung(Begegnung begegnung) {
		Optional<BegegnungJpa> optionalJpa = begegnungJpaRepository.findById(begegnung.getId());
		WettkaempferJpa wk1 = begegnung.getWettkaempfer1().isPresent()
			? wettkaempferJpaRepository.findById(begegnung.getWettkaempfer1().get().id()).orElseThrow()
			: null;
		WettkaempferJpa wk2 = begegnung.getWettkaempfer2().isPresent()
			? wettkaempferJpaRepository.findById(begegnung.getWettkaempfer2().get().id()).orElseThrow()
			: null;
		if (optionalJpa.isPresent()) {
			logger.debug("Begegnung existiert bereits, aktualisiere {}", begegnung);
			BegegnungJpa existingJpa = optionalJpa.get();
			existingJpa.updateFrom(begegnungConverter.convertFromBegegnung(begegnung), wk1, wk2);
			begegnungJpaRepository.save(existingJpa);
			return;
		}

		logger.debug("Begegnung wird neu angelegt {}", begegnung);
		BegegnungJpa jpa = begegnungConverter.convertFromBegegnung(begegnung);
		jpa.setWettkaempfer1(wk1);
		jpa.setWettkaempfer2(wk2);
		begegnungJpaRepository.save(jpa);
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
			.findById(turnierid)
			.map(t -> turnierConverter.convertToTurnier(t))
			.orElseThrow();
	}

	public Optional<Begegnung> findeBegegnung(Begegnung.RundenTyp rundenTyp, int runde, int paarung, UUID wettkampfGruppeUUID, UUID turnierUUID) {
		List<WettkampfGruppeJpa> wettkampfGruppeJpaList = wettkampfGruppeJpaRepository.findAll();
		List<BegegnungJpa> begegnungJpas = begegnungJpaRepository.findAllByTurnierUUIDAndWettkampfGruppeId(turnierUUID, wettkampfGruppeUUID);
		return begegnungJpas.stream()
			.filter(b -> b.getRundenTyp().equals(rundenTyp.getValue()))
			.filter(b -> b.getRunde().equals(runde))
			.filter(b -> b.getPaarung().equals(paarung))
			.map(b -> begegnungConverter.convertToBegegnung(b, wettkampfGruppeJpaList))
			.findFirst();
	}

	@Transactional
	public List<WettkampfGruppe> speichereGruppen(List<WettkampfGruppe> wettkampfGruppen) {
		logger.info("Speichere alle Wettkampfgruppen");

		List<WettkampfGruppe> persistierteGruppen = new ArrayList<>();
		for (WettkampfGruppe wettkampfGruppe : wettkampfGruppen) {
			if (wettkampfGruppe.id() != null) {
				WettkampfGruppeJpa wettkampfGruppeJpaOptional = wettkampfGruppeJpaRepository.findByIdAndTurnierUUID(wettkampfGruppe.id(), wettkampfGruppe.turnierUUID())
					.orElseThrow(() -> new IllegalArgumentException("Wir habe eine Wettkampfgruppe mit einer ID erhalten, diese aber nicht gefunden..."));
				logger.debug("Wettkampfgruppe existiert bereits, aktualisiere: {}", wettkampfGruppe);
				WettkampfGruppeJpa jpa = wettkampfGruppeJpaOptional;
				jpa.updateFrom(wettkampfGruppe);
				wettkampfGruppeJpaRepository.save(jpa);
				continue;
			}

			logger.debug("Wettkampfgruppe wird neu angelegt: {}", wettkampfGruppe);
			WettkampfGruppeJpa jpa = wettkampfGruppeConverter.convertFromWettkampfGruppe(wettkampfGruppe);
			jpa = wettkampfGruppeJpaRepository.saveAndFlush(jpa);
			persistierteGruppen.add(wettkampfGruppeConverter.convertToWettkampfGruppe(jpa));
		}
		// diese haben die erstellte ID aus der Datenbank
		return persistierteGruppen;
	}
}
