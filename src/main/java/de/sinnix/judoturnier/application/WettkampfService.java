package de.sinnix.judoturnier.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


import de.sinnix.judoturnier.adapter.secondary.BenutzerRepository;
import de.sinnix.judoturnier.adapter.secondary.TurnierRepository;
import de.sinnix.judoturnier.adapter.secondary.WertungRepository;
import de.sinnix.judoturnier.application.algorithm.Algorithmus;
import de.sinnix.judoturnier.application.algorithm.BesterAusDrei;
import de.sinnix.judoturnier.application.algorithm.DoppelKOSystem;
import de.sinnix.judoturnier.application.algorithm.DoppelKOSystemMitDoppelterTrostrunde;
import de.sinnix.judoturnier.application.algorithm.DoppelKOSystemVorgepoolt;
import de.sinnix.judoturnier.application.algorithm.JederGegenJeden;
import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Begegnung;
import de.sinnix.judoturnier.model.Benutzer;
import de.sinnix.judoturnier.model.Einstellungen;
import de.sinnix.judoturnier.model.GewichtsklassenGruppe;
import de.sinnix.judoturnier.model.Matte;
import de.sinnix.judoturnier.model.Runde;
import de.sinnix.judoturnier.model.SeparateAlterklassen;
import de.sinnix.judoturnier.model.TurnierTyp;
import de.sinnix.judoturnier.model.Wertung;
import de.sinnix.judoturnier.model.Wettkaempfer;
import de.sinnix.judoturnier.model.WettkampfGruppe;
import de.sinnix.judoturnier.model.WettkampfGruppeMitBegegnungen;
import de.sinnix.judoturnier.model.WettkampfReihenfolge;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Transactional
@Service
public class WettkampfService {
	private static final Logger                 logger = LogManager.getLogger(WettkampfService.class);
	@Autowired
	private              Helpers                helpers;
	@Autowired
	private              TurnierRepository      turnierRepository;
	@Autowired
	private              WertungRepository      wertungRepository;
	@Lazy
	@Autowired
	private              EinstellungenService   einstellungenService;
	@Lazy
	@Autowired
	private              GewichtsklassenService gewichtsklassenService;

	private volatile Integer               totaleRundenAnzahl;
	private volatile Map<Integer, Integer> rundenAnzahlMatte;
	@Autowired
	private          BenutzerRepository    benutzerRepository;


	public Begegnung ladeBegegnung(UUID begegnungId) {
		logger.info("lade Begegnung {}", begegnungId);
		return turnierRepository.ladeBegegnung(begegnungId);
	}

	public List<Begegnung> ladeAlleBegegnungen(UUID turnierId) {
		logger.info("lade alle Begegnungen {}", turnierId);
		return turnierRepository.ladeAlleBegegnungen(turnierId);
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
		logger.info("Erstelle Matten für {} Gruppen", gwks.size());

		// check gruppe auf vorhandene Daten
		checkGruppenSindValide(gwks);
		List<WettkampfGruppeMitBegegnungen> wettkampfGruppeMitBegegnungenList = new ArrayList<>();
		for (GewichtsklassenGruppe gwk : gwks) {
			Algorithmus algorithmus = getAlgorithmus(einstellungen, gwk);

			logger.info("Nutze Algorithmus {} für Gruppe {}({})", algorithmus.getClass().getSimpleName(), gwk.id(), gwk.name());

			// erstelle einzelne Gruppen, falls Gruppengröße beschränkt ist
			var maxGruppenGroesse = einstellungen.gruppengroessen().altersklasseGruppengroesse().get(gwk.altersKlasse());
			logger.debug("Maximale Gruppengröße in Altersklasse {}: {}", gwk.altersKlasse(), maxGruppenGroesse);
			List<List<Wettkaempfer>> wettkaempferGruppen = splitArrayToChunkSize(gwk.teilnehmer(), maxGruppenGroesse);
			for (List<Wettkaempfer> wettkaempferList : wettkaempferGruppen) {
				var splittedGwkg = new GewichtsklassenGruppe(gwk.id(), gwk.altersKlasse(), gwk.gruppenGeschlecht(), wettkaempferList, gwk.name(), gwk.minGewicht(), gwk.maxGewicht(), gwk.turnierUUID());
				WettkampfGruppeMitBegegnungen wettkampfGruppeMitBegegnungen = algorithmus.erstelleWettkampfGruppe(splittedGwkg);
				wettkampfGruppeMitBegegnungenList.add(wettkampfGruppeMitBegegnungen);
			}
		}
		logger.trace("erstellte Liste der WettkampfGruppeMitBegegnungen: {}", wettkampfGruppeMitBegegnungenList);

		// Speichern der Gruppen
		List<WettkampfGruppe> wettkampfGruppen = wettkampfGruppeMitBegegnungenList.stream()
			.map(WettkampfGruppeMitBegegnungen::gruppe)
			.toList();
		List<WettkampfGruppe> persistierteWettkampfGruppen = turnierRepository.speichereGruppen(wettkampfGruppen);
		// IDs zurück in wettkampfGruppeMitBegegnungenList propagieren
		List<WettkampfGruppeMitBegegnungen> aktualisierteWettkampfGruppenMitBegegnungenList = IntStream.range(0, wettkampfGruppeMitBegegnungenList.size())
			.mapToObj(i -> new WettkampfGruppeMitBegegnungen(
				persistierteWettkampfGruppen.get(i),
				wettkampfGruppeMitBegegnungenList.get(i).alleRundenBegegnungen()
			))
			.toList();

		// Erstellen aller Begegnungen
		List<Matte> matten = erstelleGruppenReihenfolge(aktualisierteWettkampfGruppenMitBegegnungenList, einstellungen.mattenAnzahl().anzahl(), einstellungen.wettkampfReihenfolge());

		// Speichern der Begegnungen
		turnierRepository.speichereMatten(matten);

		if (einstellungen.turnierTyp() != TurnierTyp.RANDORI) {
			// Freilose in der ersten Runde prüfen und aktualisieren
			checkAndUpdateFreilose(einstellungen.turnierUUID());
		}
	}

	private void checkAndUpdateFreilose(UUID turnierUUID) {
		List<Begegnung> alleFreilose = turnierRepository.ladeAlleBegegnungen(turnierUUID).stream()
			.filter(begegnung -> begegnung.getGruppenRunde() == 1) // Nur die erste Runde;
			.filter(begegnung -> begegnung.getBegegnungId().getRundenTyp() == Begegnung.RundenTyp.GEWINNERRUNDE) // Freilose können nur in der Gewinnerunde sein
			.filter(begegnung -> begegnung.getWettkaempfer2().isEmpty() ^ begegnung.getWettkaempfer1().isEmpty()) // XOR: nur Paarungen mit einem fehlenden Wettkämpfer
			.collect(Collectors.toList());

		Benutzer dummyKampfrichter = benutzerRepository.findBenutzerByUsername(Benutzer.ANONYMOUS_KAMPFRICHTER).orElseThrow();
		for (Begegnung begegnung : alleFreilose) {
			logger.debug("Freilos in Begegnung {} gefunden", begegnung.getId());

			// Freilos-Wertung aktualisieren
			var freilos = begegnung.getWettkaempfer1().or(() -> begegnung.getWettkaempfer2()).orElseThrow(() -> new IllegalArgumentException("Freilos nicht gefunden"));
			var freilosWertung = new Wertung(
				UUID.randomUUID(),
				freilos,
				Duration.ZERO,
				0, 0, 0, 0,
				null, null, null, null,
				null, null, null, null,
				dummyKampfrichter
			);
			logger.debug("Freilos in Begegnung {} aktualisiert: {}", begegnung.getId(), freilos);
			wertungRepository.speichereWertungInBegegnung(freilosWertung, begegnung.getId());

			// Freilos in nächste Runde übernehmen
			var wettkampfGruppe = begegnung.getWettkampfGruppe();
			var alleWettkampfgruppeRunden = turnierRepository.ladeWettkampfgruppeRunden(wettkampfGruppe.id(), begegnung.getTurnierUUID());
			Pair<Optional<Begegnung>, Optional<Begegnung>> nachfolger = Sortierer.nachfolgeBegegnungen(begegnung.getBegegnungId(), wettkampfGruppe, alleWettkampfgruppeRunden);
			Optional<Begegnung> nextGewinnerBegegnungOptional = nachfolger.getLeft();
			Begegnung nextGewinnerRunde = nextGewinnerBegegnungOptional.get();
			if (nextGewinnerRunde.getWettkaempfer1().isEmpty()) {
				nextGewinnerRunde.setWettkaempfer1(Optional.of(freilos));
			} else if (nextGewinnerRunde.getWettkaempfer2().isEmpty()) {
				nextGewinnerRunde.setWettkaempfer2(Optional.of(freilos));
			}

			turnierRepository.speichereBegegnung(nextGewinnerRunde);
		}
	}

	/**
	 * Ermittelt den passenden Algorithmus für die Gewichtsklassen-Gruppe basierend auf den Einstellungen.
	 *
	 * bei 2 Teilnehmern: „Best of Three"
	 * bis 5 Teilnehmer: Jeder gegen Jeden
	 * 6 - 8 Teilnehmer: Vorgepooltes KO-System
	 * ab 9 Teilnehmer: Doppel-KO-System (bis U21), KO-System mit doppelter Trostrund (Frauen/Männer)
	 *
	 */
	private static Algorithmus getAlgorithmus(Einstellungen einstellungen, GewichtsklassenGruppe gwk) {
		var turnierTyp = einstellungen.turnierTyp();

		if (turnierTyp == TurnierTyp.RANDORI) {
			return new JederGegenJeden();
		}

		if (gwk.teilnehmer().size() <= 2) {
			return new BesterAusDrei();
		}

		if (gwk.teilnehmer().size() <= 5) {
			return new JederGegenJeden();
		}

		if (gwk.teilnehmer().size() >= 6 && gwk.teilnehmer().size() <= 8) {
			return new DoppelKOSystemVorgepoolt();
		}

		if (gwk.altersKlasse() == Altersklasse.FRAUEN || gwk.altersKlasse() == Altersklasse.MAENNER) {
			return new DoppelKOSystemMitDoppelterTrostrunde();
		}

		return new DoppelKOSystem();
	}

	private List<Matte> erstelleGruppenReihenfolge(List<WettkampfGruppeMitBegegnungen> wettkampfGruppen, Integer anzahlMatten, WettkampfReihenfolge reihenfolge) {
		logger.debug("erstelle Reihenfolge der Wettkämpfe aus den Wettkampfgruppen: {}, {}", wettkampfGruppen.size(), reihenfolge);
		List<Matte> matten = new ArrayList<>();

		// Ausplitten der Begegnungen auf die Matten
		List<List<WettkampfGruppeMitBegegnungen>> wettkampfGruppenJeMatten = helpers.splitArrayToParts(wettkampfGruppen, anzahlMatten);

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

	private List<List<Wettkaempfer>> splitArrayToChunkSize(List<Wettkaempfer> arr, int chunkSize) {
		List<List<Wettkaempfer>> result = new ArrayList<>();
		for (int i = 0; i < arr.size(); i += chunkSize) {
			result.add(arr.subList(i, Math.min(i + chunkSize, arr.size())));
		}
		return result;
	}
}
