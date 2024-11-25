package de.sinnix.judoturnier.application;

import de.sinnix.judoturnier.adapter.secondary.BenutzerRepository;
import de.sinnix.judoturnier.adapter.secondary.TurnierRepository;
import de.sinnix.judoturnier.application.algorithm.Algorithmus;
import de.sinnix.judoturnier.application.algorithm.DoppelKOSystem;
import de.sinnix.judoturnier.application.algorithm.JederGegenJeden;
import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Begegnung;
import de.sinnix.judoturnier.model.BegegnungenJeRunde;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Transactional
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
	private TurnierRepository      turnierRepository;
	@Autowired
	private BenutzerRepository     benutzerRepository;
	@Autowired
	private Helpers                helpers;
	@Autowired
	private WettkaempferService    wettkaempferService;

	private volatile Integer totaleRundenAnzahl;
	private volatile Map<Integer, Integer> rundenAnzahlMatte;

	public List<Turnier> ladeAlleTurniere() {
		logger.info("ladeTurniere");
		return turnierRepository.ladeAlleTurniere();
	}

	public List<Turnier> ladeTurniere(List<UUID> turnierUUIDs) {
		logger.info("lade alle Turniere {}", turnierUUIDs);
		return turnierUUIDs.stream()
			.map(uuid -> turnierRepository.ladeTurnier(uuid))
			.toList();
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
			Turnier neuesTurnier = new Turnier(UUID.randomUUID(), name, ort, parsedDate);
			Turnier turnier = turnierRepository.speichereTurnier(neuesTurnier);



			einstellungenService.speichereDefaultEinstellungen(turnier.uuid());

			return turnier;
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

		// reset global counters
		totaleRundenAnzahl = 1;
		rundenAnzahlMatte = new HashMap<>();
		IntStream.rangeClosed(1, einstellungen.mattenAnzahl().anzahl()).forEach(i -> rundenAnzahlMatte.put(i, 1));

		if (einstellungen.separateAlterklassen().equals(SeparateAlterklassen.GETRENNT)) {
			logger.info("Trenne nach Altersklassen...");

			// Gruppieren nach Altersklasse
			Map<Altersklasse, List<GewichtsklassenGruppe>> groupedByAltersklasse = gwks.stream()
				.collect(Collectors.groupingBy(GewichtsklassenGruppe::altersKlasse));

			groupedByAltersklasse.values().forEach(groupedGwks -> erstelleMatten(groupedGwks, einstellungen));
			return;
		}

		if (einstellungen.separateAlterklassen().equals(SeparateAlterklassen.ZUSAMMEN)) {
			logger.info("Alle Altersklassen zusammen...");
			erstelleMatten(gwks, einstellungen);
			return;
		}

		logger.error("Wettkampfreihenfolge {} nicht implementiert!", einstellungen.separateAlterklassen());
	}

	private void erstelleMatten(List<GewichtsklassenGruppe> gwks, Einstellungen einstellungen) {
		Algorithmus algorithmus = einstellungen.turnierTyp() == TurnierTyp.RANDORI ?
			new JederGegenJeden() :
			new DoppelKOSystem();

		// check gruppe auf vorhandene Daten
		checkGruppenSindValide(gwks);

		List<WettkampfGruppe> wettkampfGruppen = erstelleWettkampfgruppen(gwks, algorithmus, einstellungen.gruppengroesse().anzahl());

		//loggWettkampfgruppen(wettkampfGruppen);

		List<Matte> matten = erstelleGruppenReihenfolge(wettkampfGruppen, einstellungen.mattenAnzahl().anzahl(), einstellungen.wettkampfReihenfolge());

		turnierRepository.speichereMatten(matten);
	}

	private static void loggWettkampfgruppen(List<WettkampfGruppe> wettkampfGruppen) {
		for (WettkampfGruppe wettkampfGruppe : wettkampfGruppen) {
			int r = 1;
			for (BegegnungenJeRunde begegnungenJeRunde : wettkampfGruppe.alleRundenBegegnungen()) {
				logger.debug("WettkampfGruppe {} Runde {}", wettkampfGruppe.name(), r);
				for (Begegnung begegnung : begegnungenJeRunde.begegnungenJeRunde()) {
					logger.debug("{} - Wettkaempfer1: {}, Wettkaempfer2: {}", begegnung.getBegegnungId(), begegnung.getWettkaempfer1().map(Wettkaempfer::name), begegnung.getWettkaempfer2().map(Wettkaempfer::name));
				}
				r++;
			}
		}
	}

	public Begegnung ladeBegegnung(UUID begegnungId) {
		logger.info("lade Begegnung {}", begegnungId);
		return turnierRepository.ladeBegegnung(begegnungId);
	}

	public List<Begegnung> ladeAlleBegegnungen(UUID turnierId) {
		logger.info("lade alle Begegnungen {}", turnierId);
		return turnierRepository.ladeAlleBegegnungen(turnierId);
	}

	public void speichereRandoriWertung(UUID begegnungId, int kampfgeist1, int technik1, int stil1, int vielfalt1, int kampfgeist2, int technik2, int stil2, int vielfalt2, UUID bewerterUUID) {
		logger.info("speichereRandoriWertung: {}", begegnungId);
		Begegnung begegnung = ladeBegegnung(begegnungId);

		Benutzer benutzer = benutzerRepository.findBenutzer(bewerterUUID).orElseThrow(() -> new RuntimeException("Nutzer " + bewerterUUID + " konnte nicht gefunden werden"));
		var existierendeWertung = wertungVonBewerter(begegnung.getWertungen(), benutzer);
		if (existierendeWertung.isPresent()) {
			logger.debug("Aktualisiere existierende Wertung");
			var wertung = existierendeWertung.get();
			wertung.setKampfgeistWettkaempfer1(kampfgeist1);
			wertung.setTechnikWettkaempfer1(technik1);
			wertung.setKampfstilWettkaempfer1(stil1);
			wertung.setVielfaltWettkaempfer1(vielfalt1);
			wertung.setKampfgeistWettkaempfer2(kampfgeist2);
			wertung.setTechnikWettkaempfer2(technik2);
			wertung.setKampfstilWettkaempfer2(stil2);
			wertung.setVielfaltWettkaempfer2(vielfalt2);

			turnierRepository.speichereBegegnung(begegnung);
			return;
		}

		logger.debug("Erstelle neue Wertung");
		UUID wertungId = UUID.randomUUID();
		Wertung wertungNeu = new Wertung(wertungId, null, null, null, null, null, null,
			kampfgeist1, technik1, stil1, vielfalt1,
			kampfgeist2, technik2, stil2, vielfalt2,
			benutzer
		);
		begegnung.getWertungen().add(wertungNeu);
		turnierRepository.speichereBegegnung(begegnung);
	}

	public void speichereTurnierWertung(UUID begegnungId, int scoreWeiss, int scoreBlau, int penaltiesWeiss, int penaltiesBlau, String fightTime, UUID siegerUUID, UUID bewerterUUID) {
		logger.info("Begegnung: {}, Sieger: {}, Kampfzeit: {}s", begegnungId, siegerUUID, fightTime);
		Begegnung begegnung = ladeBegegnung(begegnungId);

		Benutzer benutzer = benutzerRepository.findBenutzer(bewerterUUID).orElseThrow(() -> new RuntimeException("Nutzer " + bewerterUUID + " konnte nicht gefunden werden"));
		Wettkaempfer wettkaempfer = wettkaempferService.ladeKaempfer(siegerUUID).orElseThrow();
		Duration dauer = parseDuration(fightTime);

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
		return wertungen.stream().filter(w -> w.getBewerter().uuid().equals(benutzer.uuid())).findFirst();
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
			var gruppen = wettkampfGruppenJeMatten.get(m);
			List<Runde> runden = new ArrayList<>();
			Integer matteId = m + 1;
			Sortierer sortierer = new Sortierer(totaleRundenAnzahl, rundenAnzahlMatte.get(matteId));
			Pair<Integer, List<Runde>> result = null;
			switch (reihenfolge) {
				case WettkampfReihenfolge.ABWECHSELND:
					result = sortierer.erstelleReihenfolgeMitAbwechselndenGruppen(gruppen, matteId);
					totaleRundenAnzahl = result.getLeft();
					runden = result.getRight();
					matten.add(new Matte(matteId, runden));
					rundenAnzahlMatte.put(matteId, rundenAnzahlMatte.get(matteId) + runden.size());
					break;
				case WettkampfReihenfolge.ALLE:
					result = sortierer.erstelleReihenfolgeMitAllenGruppenJeDurchgang(gruppen, matteId);
					totaleRundenAnzahl = result.getLeft();
					runden = result.getRight();
					matten.add(new Matte(matteId, runden));
					rundenAnzahlMatte.put(matteId, rundenAnzahlMatte.get(matteId) + runden.size());
					break;
			}

			logger.debug("Aktuelle totaleRundenAnzahl {}, {}", totaleRundenAnzahl, rundenAnzahlMatte);
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

	private static Duration parseDuration(String input) {
		// Regex für das Parsen von "mm:ss.SS"
		Pattern pattern = Pattern.compile("(\\d{2}):(\\d{2})\\.(\\d{2})");
		Matcher matcher = pattern.matcher(input);

		if (matcher.matches()) {
			int minutes = Integer.parseInt(matcher.group(1));
			int seconds = Integer.parseInt(matcher.group(2));
			int millis = Integer.parseInt(matcher.group(3)) * 10; // 36 bedeutet 360 Millisekunden

			long totalMillis = (long) minutes * 60 * 1000 + seconds * 1000 + millis;
			return Duration.ofMillis(totalMillis);
		} else {
			throw new IllegalArgumentException("Invalid duration format: " + input);
		}
	}

	public Turnier ladeTurnier(UUID turnierid) {
		return turnierRepository.ladeTurnier(turnierid);
	}

	public Metadaten ladeMetadaten(UUID id, UUID turnierUUID) {
		logger.info("Lade Metadaten für Begegnung {}", id);
		var matten = turnierRepository.ladeMatten(turnierUUID);

		var aktuelleRunde = matten.values().stream()
			.flatMap(matte -> matte.runden().stream())
			.filter(runde -> runde.begegnungen().stream()
				.anyMatch(begegnung -> begegnung.getId().equals(id)))
			.findFirst();

		if (aktuelleRunde.isEmpty()) {
			throw new IllegalArgumentException("Es konnten keine Daten zu dieser Begegnung gefunden werden.");
		}

		var alleRundeBegegnungIds = aktuelleRunde.get().begegnungen().stream().map(Begegnung::getId).toList();

		UUID vorgaenger = null;
		UUID nachfolger = null;
		int index = alleRundeBegegnungIds.indexOf(id);
		if (index > 0) {
			vorgaenger = alleRundeBegegnungIds.get(index - 1);
		}
		if (index < alleRundeBegegnungIds.size() - 1) {
			nachfolger = alleRundeBegegnungIds.get(index + 1);
		}

		return new Metadaten(alleRundeBegegnungIds, Optional.ofNullable(vorgaenger), Optional.ofNullable(nachfolger), aktuelleRunde.get().rundeId());
	}

	public List<Begegnung> ladeMattenRunde(UUID turnierUUID, Integer matte, Integer mattenrunde) {
		logger.info("Lade Matten Runde");

		return turnierRepository.ladeAlleBegegnungen(turnierUUID).stream()
			.filter(b -> b.getMatteId().equals(matte))
			.filter(b -> b.getMattenRunde().equals(mattenrunde))
			.toList();
	}
}
