package de.sinnix.judoturnier.application;

import de.sinnix.judoturnier.adapter.secondary.TurnierRepository;
import de.sinnix.judoturnier.application.algorithm.Algorithmus;
import de.sinnix.judoturnier.application.algorithm.JederGegenJeden;
import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Einstellungen;
import de.sinnix.judoturnier.model.GewichtsklassenGruppe;
import de.sinnix.judoturnier.model.Matte;
import de.sinnix.judoturnier.model.Runde;
import de.sinnix.judoturnier.model.TurnierTyp;
import de.sinnix.judoturnier.model.Wertung;
import de.sinnix.judoturnier.model.WettkampfGruppe;
import de.sinnix.judoturnier.model.WettkampfReihenfolge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TurnierService {
	private static final Logger logger = LogManager.getLogger(TurnierService.class);

	@Autowired
	private TurnierRepository turnierRepository;
	@Autowired
	private EinstellungenService einstellungenService;
	@Autowired
	private GewichtsklassenService gewichtsklassenService;
	@Autowired
	private Sortierer sortierer;

	public List<Matte> ladeWettkampfreihenfolge() {
		logger.info("ladeWettkampfreihenfolge");
		return turnierRepository.ladeMatten().values().stream().toList();
	}

	public void loescheWettkampfreihenfolge() {
		logger.info("loescheWettkampfreihenfolge");
		turnierRepository.loescheAlleMatten();
	}

	public void erstelleWettkampfreihenfolge() {
		logger.info("erstelle Wettkampfreihenfolge für alle Altersklassen");
		erstelleWettkampfreihenfolgeAltersklasse(Optional.empty());
	}

	public void loescheWettkampfreihenfolgeAltersklasse(Altersklasse altersklasse) {
		logger.info("lösche WettkampfreihenfolgeAltersklasse für {}", altersklasse);
		turnierRepository.loescheWettkaempfeMitAltersklasse(altersklasse);
	}

	public void erstelleWettkampfreihenfolgeAltersklasse(Optional<Altersklasse> altersklasse) {
		logger.info("erstelle Wettkampfreihenfolge für Altersklasse {}", altersklasse);

		Einstellungen einstellungen = einstellungenService.ladeEinstellungen();
		List<GewichtsklassenGruppe> gwks = altersklasse.isPresent() ? gewichtsklassenService.ladeGewichtsklassenGruppe(altersklasse.get()) : gewichtsklassenService.ladeGewichtsklassenGruppen();
		if (einstellungen.turnierTyp() == TurnierTyp.RANDORI) {
			// check gruppe auf vorhandene Daten
			checkGruppenSindValide(gwks);

			Algorithmus algorithmus = new JederGegenJeden();
			List<WettkampfGruppe> wettkampfGruppen = erstelleWettkampfgruppen(gwks, algorithmus, einstellungen.mattenAnzahl().anzahl());
			List<Matte> matten = erstelleGruppenReihenfolgeRandori(wettkampfGruppen, einstellungen.mattenAnzahl().anzahl(), einstellungen.wettkampfReihenfolge());
		}
		else {
			logger.error("Turniermodus noch nicht implementiert!");
		}
	}

	public Optional<Wertung> ladeWertungFuerWettkampf(String wertungId) {
		logger.info("lade Wertung für Wettkampf {}", wertungId);
		return turnierRepository.ladeWertung(wertungId);
	}

	public void speichereWertung(Wertung wertung) {
		logger.info("speichere Wertung {}", wertung);
		turnierRepository.speichereWertung(wertung);
	}

	private List<WettkampfGruppe> erstelleWettkampfgruppen(List<GewichtsklassenGruppe> gewichtsklassenGruppen, Algorithmus algorithmus, Integer anzahlMatten) {
		logger.debug("erstelle Wettkampfgruppen aus den Gewichtsklassengruppen");
		// erstelle alle Begegnungen in jeder Gruppe
    	List<WettkampfGruppe> wettkampfGruppen = new ArrayList<>();
		for (int i = 0; i < gewichtsklassenGruppen.size(); i++) {
      		var gruppe = gewichtsklassenGruppen.get(i);
      		var wkg = algorithmus.erstelleWettkampfGruppen(i, gruppe, anzahlMatten);
			wettkampfGruppen.addAll(wkg);
		}
		return wettkampfGruppen;
	}

	private List<Matte> erstelleGruppenReihenfolgeRandori(List<WettkampfGruppe> wettkampfGruppen, Integer anzahlMatten, WettkampfReihenfolge reihenfolge) {
		logger.debug("erstelle Reihenfolge der Wettkämpfe aus den Wettkmapfgruppen: " + reihenfolge);
    	List<Matte> matten = new ArrayList<>();

		// Ausplitten der Begegnungen auf die Matten
		List<List<WettkampfGruppe>> wettkampfGruppenJeMatten = this.splitArray(wettkampfGruppen, anzahlMatten);


		for (int m = 0; m < anzahlMatten; m++) {
      		var gruppen = wettkampfGruppenJeMatten.get(m);

			// TODO
			// sortiere die Gruppen, sodass die Gruppen mit wenigen Kämpfen ganz hinten sind, aber die Altersklassen zusammen bleiben
			// gruppen.sort((gs1, gs2) => if (gs2.alleGruppenBegegnungen[0][0].wettkaempfer1.altersklasse ) gs2.alleGruppenBegegnungen.length - gs1.alleGruppenBegegnungen.length);
			// this.logWettkampfGruppen(gruppen)
			List<Runde> runden = new ArrayList<>();
			Integer matteId = m+1;
			switch (reihenfolge) {
				case WettkampfReihenfolge.ABWECHSELND:
					runden = sortierer.erstelleReihenfolgeMitAbwechselndenGruppen(gruppen);
					matten.add(new Matte(matteId, runden, null));
					break;
				case WettkampfReihenfolge.ALLE:
					runden = sortierer.erstelleReihenfolgeMitAllenGruppenJeDurchgang(gruppen);
					matten.add(new Matte(matteId, runden, null));
					break;
			}
		}

		int erwartet = wettkampfGruppenJeMatten.stream()
			.mapToInt(wgm -> wgm.stream()
				.mapToInt(gr -> gr.alleGruppenBegegnungen().stream()
					.mapToInt(br -> br.size())
					.sum())
				.sum())
			.sum();

		// Berechnung der "summe"
		int summe = matten.stream()
			.mapToInt(m -> m.runden().stream()
				.mapToInt(r -> r.begegnungen().size())
				.sum())
			.sum();
		logger.debug("erwartet {}, summe {}, mattenanzahl {}", erwartet, summe, anzahlMatten);
		return matten;
	}

	private List<List<WettkampfGruppe>> splitArray(List<WettkampfGruppe> arr, Integer numberOfParts) {
    	List<List<WettkampfGruppe>> parts = new ArrayList<>();
    	Integer partLength = (int) Math.ceil((double) arr.size() / numberOfParts);

		for (int i = 0; i < numberOfParts; i++) {
		 	Integer startIndex = i * partLength;
			Integer endIndex = startIndex + partLength;
			parts.add(arr.subList(startIndex, endIndex));
		}

		return parts;
	}

	private void checkGruppenSindValide(List<GewichtsklassenGruppe> gruppen) {
		for (var gruppe : gruppen) {
			if (gruppe.altersKlasse() == null) throw new Error("GewichtsklassenGruppe " + gruppe.id() + " hat keine Altersklasse.");
			if (gruppe.gruppenGeschlecht() == null) throw new Error("GewichtsklassenGruppe " + gruppe.id() + " hat kein Geschlecht.");
			for (var teilnehmer : gruppe.teilnehmer()) {
				if (teilnehmer.altersklasse() == null) throw new Error("Teilnehmer " + teilnehmer.id() + " hat keine Altersklasse.");
				if (teilnehmer.geschlecht() == null) throw new Error("Teilnehmer " + teilnehmer.id() + " hat kein Geschlecht.");
				if (teilnehmer.gewicht() == null) throw new Error("Teilnehmer " + teilnehmer.id() + " hat kein Gewicht.");
			}
		}
	}
}
