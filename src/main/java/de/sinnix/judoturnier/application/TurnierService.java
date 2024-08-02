package de.sinnix.judoturnier.application;

import de.sinnix.judoturnier.adapter.secondary.BenutzerRepository;
import de.sinnix.judoturnier.adapter.secondary.TurnierConverter;
import de.sinnix.judoturnier.adapter.secondary.TurnierJpa;
import de.sinnix.judoturnier.adapter.secondary.TurnierJpaRepository;
import de.sinnix.judoturnier.adapter.secondary.TurnierRepository;
import de.sinnix.judoturnier.application.algorithm.Algorithmus;
import de.sinnix.judoturnier.application.algorithm.DoppelKOSystem;
import de.sinnix.judoturnier.application.algorithm.JederGegenJeden;
import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Begegnung;
import de.sinnix.judoturnier.model.Benutzer;
import de.sinnix.judoturnier.model.Einstellungen;
import de.sinnix.judoturnier.model.GewichtsklassenGruppe;
import de.sinnix.judoturnier.model.Matte;
import de.sinnix.judoturnier.model.Metadaten;
import de.sinnix.judoturnier.model.Runde;
import de.sinnix.judoturnier.model.SeparateAlterklassen;
import de.sinnix.judoturnier.model.Turnier;
import de.sinnix.judoturnier.model.TurnierTyp;
import de.sinnix.judoturnier.model.Wertung;
import de.sinnix.judoturnier.model.Wettkaempfer;
import de.sinnix.judoturnier.model.WettkampfGruppe;
import de.sinnix.judoturnier.model.WettkampfReihenfolge;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TurnierService {
	private static final Logger           logger     = LogManager.getLogger(TurnierService.class);
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	@Lazy
	@Autowired
	private EinstellungenService   einstellungenService;
	@Lazy
	@Autowired
	private GewichtsklassenService gewichtsklassenService;
	@Autowired
	private TurnierConverter       turnierConverter;
	@Autowired
	private TurnierRepository      turnierRepository;
	@Autowired
	private TurnierJpaRepository turnierJpaRepository;
	@Autowired
	private BenutzerRepository   benutzerRepository;
	@Autowired
	private Helpers              helpers;
	@Autowired
	private WettkaempferService    wettkaempferService;

	private volatile Integer totaleRundenAnzahl;

	public List<Turnier> ladeTurniere() {
		logger.info("ladeTurniere");
		return turnierRepository.ladeAlleTurniere();
	}

	public List<Matte> ladeWettkampfreihenfolge(UUID turnierUUID) {
		logger.info("ladeWettkampfreihenfolge");
		return turnierRepository.ladeMatten(turnierUUID).values().stream().toList();
	}

	public void loescheWettkampfreihenfolge(UUID turnierUUID) {
		logger.info("loescheWettkampfreihenfolge");
		turnierRepository.loescheAlleMatten(turnierUUID);
	}

	public Turnier erstelleTurnier(String name, String ort, String datum) {
		logger.info("erstelle neues Turnier");
		try {
			// Parsen des Strings in ein java.util.Date
			Date parsedDate = dateFormat.parse(datum);
			System.out.println("Parsed Date: " + parsedDate);
			Turnier neuesTurnier = new Turnier(UUID.randomUUID(), name, ort, parsedDate);

			TurnierJpa jpa = turnierJpaRepository.save(turnierConverter.convertFromTurnier(neuesTurnier));
			return turnierConverter.convertToTurnier(jpa);
		} catch (ParseException e) {
			logger.warn("Datum konnte nicht geparsed werden: {}", datum, e);
			throw new IllegalArgumentException("Datum");
		}
	}

	public void erstelleWettkampfreihenfolge(UUID turnierUUID) {
		logger.info("erstelle Wettkampfreihenfolge für alle Altersklassen");
		erstelleWettkampfreihenfolgeAltersklasse(Optional.empty(), turnierUUID);
	}

	public void loescheWettkampfreihenfolgeAltersklasse(Altersklasse altersklasse, UUID turnierUUID) {
		logger.info("lösche WettkampfreihenfolgeAltersklasse für {}", altersklasse);
		turnierRepository.loescheWettkaempfeMitAltersklasse(altersklasse, turnierUUID);
	}

	public void erstelleWettkampfreihenfolgeAltersklasse(Optional<Altersklasse> altersklasse, UUID turnierUUID) {
		logger.info("erstelle Wettkampfreihenfolge für Altersklasse {}", altersklasse);

		Einstellungen einstellungen = einstellungenService.ladeEinstellungen(turnierUUID);
		List<GewichtsklassenGruppe> gwks = altersklasse.isPresent() ? gewichtsklassenService.ladeGewichtsklassenGruppe(altersklasse.get(), turnierUUID) : gewichtsklassenService.ladeGewichtsklassenGruppen(turnierUUID);
		if (einstellungen.separateAlterklassen().equals(SeparateAlterklassen.GETRENNT)) {
			logger.info("Trenne nach Altersklassen...");

			// Gruppieren nach Altersklasse
			Map<Altersklasse, List<GewichtsklassenGruppe>> groupedByAltersklasse = gwks.stream()
				.collect(Collectors.groupingBy(GewichtsklassenGruppe::altersKlasse));

			// reset der Gesamtzahl aller Runden
			totaleRundenAnzahl = 1;
			groupedByAltersklasse.values().stream().forEach(groupedGwks -> erstelleMatten(groupedGwks, einstellungen));
			return;
		}

		if (einstellungen.separateAlterklassen().equals(SeparateAlterklassen.ZUSAMMEN)) {
			logger.info("Alle Altersklassen zusammen...");
			totaleRundenAnzahl = 1;
			erstelleMatten(gwks, einstellungen);
			return;
		}

		logger.warn("Wettkampfreihenfolge {} nicht implementiert!", einstellungen.separateAlterklassen());
	}

	private void erstelleMatten(List<GewichtsklassenGruppe> gwks, Einstellungen einstellungen) {
		Algorithmus algorithmus = einstellungen.turnierTyp() == TurnierTyp.RANDORI ?
			new JederGegenJeden() :
			new DoppelKOSystem();

		// check gruppe auf vorhandene Daten
		checkGruppenSindValide(gwks);

		List<WettkampfGruppe> wettkampfGruppen = erstelleWettkampfgruppen(gwks, algorithmus, einstellungen.gruppengroesse().anzahl());
		List<Matte> matten = erstelleGruppenReihenfolge(wettkampfGruppen, einstellungen.mattenAnzahl().anzahl(), einstellungen.wettkampfReihenfolge());

		turnierRepository.speichereMatten(matten);
	}

	public Begegnung ladeBegegnung(Integer begegnungId) {
		logger.info("lade Begegnung {}", begegnungId);
		return turnierRepository.ladeBegegnung(begegnungId);
	}

	@Transactional
	public void speichereRandoriWertung(String begegnungId, int kampfgeist1, int technik1, int stil1, int fairness1, int kampfgeist2, int technik2, int stil2, int fairness2, String bewerterUUID) {
		logger.info("speichereRandoriWertung: {}", begegnungId);
		Begegnung begegnung = ladeBegegnung(Integer.parseInt(begegnungId));

		Benutzer benutzer = benutzerRepository.findById(bewerterUUID);
		var existierendeWertung = wertungVonBewerter(begegnung.getWertungen(), benutzer);
		if (existierendeWertung.isPresent()) {
			logger.debug("Aktualisiere existierende Wertung");
			var wertung = existierendeWertung.get();
			wertung.setKampfgeistWettkaempfer1(kampfgeist1);
			wertung.setTechnikWettkaempfer1(technik1);
			wertung.setKampfstilWettkaempfer1(stil1);
			wertung.setFairnessWettkaempfer1(fairness1);
			wertung.setKampfgeistWettkaempfer2(kampfgeist2);
			wertung.setTechnikWettkaempfer2(technik2);
			wertung.setKampfstilWettkaempfer2(stil2);
			wertung.setFairnessWettkaempfer2(fairness2);

			turnierRepository.speichereBegegnung(begegnung);
			return;
		}

		logger.debug("Erstelle neue Wertung");
		UUID wertungId = UUID.randomUUID();
		Wertung wertungNeu = new Wertung(wertungId, null, null, null, null, null, null,
			kampfgeist1, technik1, stil1, fairness1,
			kampfgeist2, technik2, stil2, fairness2,
			benutzer
		);
		begegnung.getWertungen().add(wertungNeu);
		turnierRepository.speichereBegegnung(begegnung);
	}

	@Transactional
	public void speichereTurnierWertung(String begegnungId, int scoreWeiss, int scoreBlau, int penaltiesWeiss, int penaltiesBlau, int fightTime, int sieger, String bewerterUUID) {
		logger.info("Begegnung: {}, Sieger: {}, Kampfzeit: {}s", begegnungId, sieger, fightTime);
		Begegnung begegnung = ladeBegegnung(Integer.parseInt(begegnungId));

		Benutzer benutzer = benutzerRepository.findById(bewerterUUID);
		Wettkaempfer wettkaempfer = wettkaempferService.ladeKaempfer(sieger).orElseThrow();
		Duration dauer = Duration.of(fightTime, ChronoUnit.SECONDS);

		var existierendeWertung = wertungVonBewerter(begegnung.getWertungen(), benutzer);
		if (existierendeWertung.isPresent()) {
			logger.debug("Aktualisiere existierende Wertung");
			var wertung = existierendeWertung.get();
			wertung.setSieger(wettkaempfer);
			wertung.setPunkteWettkaempferWeiss(scoreWeiss);
			wertung.setStrafenWettkaempferWeiss(penaltiesWeiss);
			wertung.setPunkteWettkaempferRot(scoreBlau);
			wertung.setStrafenWettkaempferRot(penaltiesBlau);
			wertung.setZeit(dauer);

			turnierRepository.speichereBegegnung(begegnung);
			return;
		}

		logger.debug("Erstelle neue Wertung");
		UUID wertungId = UUID.randomUUID();
		Wertung wertungNeu = new Wertung(wertungId,
			wettkaempfer,
			dauer,
			scoreWeiss,
			penaltiesWeiss,
			scoreBlau,
			penaltiesBlau,
			null, null, null, null, null, null, null, null,
			benutzer
		);
		begegnung.getWertungen().add(wertungNeu);
		turnierRepository.speichereBegegnung(begegnung);
	}

	private Optional<Wertung> wertungVonBewerter(List<Wertung> wertungen, Benutzer benutzer) {
		return wertungen.stream().filter(w -> w.getBewerter().equals(benutzer)).findFirst();
	}

	private List<WettkampfGruppe> erstelleWettkampfgruppen(List<GewichtsklassenGruppe> gewichtsklassenGruppen, Algorithmus algorithmus, Integer maxGruppenGroesse) {
		logger.debug("erstelle Wettkampfgruppen aus den Gewichtsklassengruppen");
		// erstelle alle Begegnungen in jeder Gruppe
		List<WettkampfGruppe> wettkampfGruppen = new ArrayList<>();
		for (int i = 0; i < gewichtsklassenGruppen.size(); i++) {
			var gruppe = gewichtsklassenGruppen.get(i);
			var wkg = algorithmus.erstelleWettkampfGruppen(i, gruppe, maxGruppenGroesse);
			wettkampfGruppen.addAll(wkg);
		}
		logger.debug("Anzahl erstellter Wettkampfgruppen: {}", wettkampfGruppen.size());
		return wettkampfGruppen;
	}

	private List<Matte> erstelleGruppenReihenfolge(List<WettkampfGruppe> wettkampfGruppen, Integer anzahlMatten, WettkampfReihenfolge reihenfolge) {
		logger.debug("erstelle Reihenfolge der Wettkämpfe aus den Wettkampfgruppen: {}, {}", wettkampfGruppen.size(), reihenfolge);
		List<Matte> matten = new ArrayList<>();

		// Ausplitten der Begegnungen auf die Matten
		List<List<WettkampfGruppe>> wettkampfGruppenJeMatten = helpers.splitArrayToParts(wettkampfGruppen, anzahlMatten);

		for (int m = 0; m < anzahlMatten; m++) {
			Sortierer sortierer = new Sortierer(totaleRundenAnzahl);
			var gruppen = wettkampfGruppenJeMatten.get(m);
			List<Runde> runden = new ArrayList<>();
			Integer matteId = m + 1;
			Pair<Integer, List<Runde>> result = null;
			switch (reihenfolge) {
				case WettkampfReihenfolge.ABWECHSELND:
					result = sortierer.erstelleReihenfolgeMitAbwechselndenGruppen(gruppen, matteId);
					totaleRundenAnzahl = result.getLeft();
					runden = result.getRight();
					matten.add(new Matte(matteId, runden));
					break;
				case WettkampfReihenfolge.ALLE:
					result = sortierer.erstelleReihenfolgeMitAllenGruppenJeDurchgang(gruppen, matteId);
					totaleRundenAnzahl = result.getLeft();
					runden = result.getRight();
					matten.add(new Matte(matteId, runden));
					break;
			}

			logger.debug("Aktuelle totaleRundenAnzahl {}", totaleRundenAnzahl);
		}
		logger.trace("Matten {}", matten);
		return matten;
	}



	private void checkGruppenSindValide(List<GewichtsklassenGruppe> gruppen) throws Error {
		try {
			for (var gruppe : gruppen) {
				if (gruppe.altersKlasse() == null) throw new Error("GewichtsklassenGruppe " + gruppe.id() + " hat keine Altersklasse.");
				if (gruppe.gruppenGeschlecht() == null) throw new Error("GewichtsklassenGruppe " + gruppe.id() + " hat kein Geschlecht.");
				for (var teilnehmer : gruppe.teilnehmer()) {
					if (teilnehmer.altersklasse() == null) throw new Error("Teilnehmer " + teilnehmer.id() + " hat keine Altersklasse.");
					if (teilnehmer.geschlecht() == null) throw new Error("Teilnehmer " + teilnehmer.id() + " hat kein Geschlecht.");
					if (teilnehmer.gewicht() == null) throw new Error("Teilnehmer " + teilnehmer.id() + " hat kein Gewicht.");
				}
			}
		} catch (Error e) {
			logger.error(e);
			throw e;
		}
	}

	public Turnier ladeTurnier(String turnierid) {
		return turnierJpaRepository.findById(turnierid).map(t -> turnierConverter.convertToTurnier(t)).orElseThrow();
	}

	public Metadaten ladeMetadaten(Integer begegnungId, UUID turnierUUID) {
		logger.info("Lade Metadaten für Begegnung {}", begegnungId);
		var matten = turnierRepository.ladeMatten(turnierUUID);;

		var aktuelleRunde = matten.values().stream()
			.flatMap(matte -> matte.runden().stream())
			.filter(runde -> runde.begegnungen().stream()
				.anyMatch(begegnung -> begegnung.getBegegnungId().equals(begegnungId)))
			.findFirst();

		if (aktuelleRunde.isEmpty()) {
			throw new IllegalArgumentException("Es konnten keine Daten zu dieser Begegnung gefunden werden.");
		}

		var alleRundeBegegnungIds = aktuelleRunde.get().begegnungen().stream().map(Begegnung::getBegegnungId).collect(Collectors.toUnmodifiableList());

		Integer vorgaenger = null;
		Integer nachfolger = null;
		int index = alleRundeBegegnungIds.indexOf(begegnungId);
		if (index > 0) {
			vorgaenger = alleRundeBegegnungIds.get(index - 1);
		}
		if (index < alleRundeBegegnungIds.size() - 1) {
			nachfolger = alleRundeBegegnungIds.get(index + 1);
		}

		return new Metadaten(alleRundeBegegnungIds, Optional.ofNullable(vorgaenger), Optional.ofNullable(nachfolger), aktuelleRunde.get().rundeId());
	}
}
