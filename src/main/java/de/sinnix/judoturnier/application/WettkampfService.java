package de.sinnix.judoturnier.application;

import de.sinnix.judoturnier.adapter.secondary.TurnierRepository;
import de.sinnix.judoturnier.application.algorithm.Algorithmus;
import de.sinnix.judoturnier.application.algorithm.DoppelKOSystem;
import de.sinnix.judoturnier.application.algorithm.JederGegenJeden;
import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Begegnung;
import de.sinnix.judoturnier.model.BegegnungenJeRunde;
import de.sinnix.judoturnier.model.Einstellungen;
import de.sinnix.judoturnier.model.GewichtsklassenGruppe;
import de.sinnix.judoturnier.model.Matte;
import de.sinnix.judoturnier.model.Runde;
import de.sinnix.judoturnier.model.SeparateAlterklassen;
import de.sinnix.judoturnier.model.TurnierTyp;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Transactional
@Service
public class WettkampfService {
	private static final Logger                 logger = LogManager.getLogger(WettkampfService.class);
	@Autowired
	private              Helpers                helpers;
	@Autowired
	private              TurnierRepository      turnierRepository;
	@Lazy
	@Autowired
	private              EinstellungenService   einstellungenService;
	@Lazy
	@Autowired
	private              GewichtsklassenService gewichtsklassenService;

	private volatile     Integer                totaleRundenAnzahl;
	private volatile     Map<Integer, Integer>  rundenAnzahlMatte;


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
		// check gruppe auf vorhandene Daten
		checkGruppenSindValide(gwks);
		List<WettkampfGruppe> wettkampfGruppen = new ArrayList<>();
		for (GewichtsklassenGruppe gwk : gwks) {
			Algorithmus algorithmus = einstellungen.turnierTyp() == TurnierTyp.RANDORI ?
				new JederGegenJeden() :
				new DoppelKOSystem();

			// erstelle einzelne Gruppen, falls Gruppengröße beschränkt ist
			var maxGruppenGroesse = einstellungen.gruppengroessen().altersklasseGruppengroesse().get(gwk.altersKlasse());
			logger.debug("Maximale Gruppengröße in Altersklasse {}: {}",  gwk.altersKlasse(), maxGruppenGroesse);
			List<List<Wettkaempfer>> wettkaempferGruppen = splitArrayToChunkSize(gwk.teilnehmer(), maxGruppenGroesse);
			for (List<Wettkaempfer> wettkaempferList : wettkaempferGruppen) {
				var splittedGwkg = new GewichtsklassenGruppe(gwk.id(), gwk.altersKlasse(), gwk.gruppenGeschlecht(), wettkaempferList, gwk.name(), gwk.minGewicht(), gwk.maxGewicht(), gwk.turnierUUID());
				WettkampfGruppe wettkampfGruppe = algorithmus.erstelleWettkampfGruppe(splittedGwkg);
				wettkampfGruppen.add(wettkampfGruppe);
			}
		}
		logger.info("{}", wettkampfGruppen);
		loggWettkampfgruppen(wettkampfGruppen);

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

	private List<List<Wettkaempfer>> splitArrayToChunkSize(List<Wettkaempfer> arr, int chunkSize) {
		List<List<Wettkaempfer>> result = new ArrayList<>();
		for (int i = 0; i < arr.size(); i += chunkSize) {
			result.add(arr.subList(i, Math.min(i + chunkSize, arr.size())));
		}
		return result;
	}
}
