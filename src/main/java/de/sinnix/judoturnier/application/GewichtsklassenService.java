package de.sinnix.judoturnier.application;

import de.sinnix.judoturnier.adapter.secondary.GewichtsklassenRepository;
import de.sinnix.judoturnier.adapter.secondary.WettkaempferRepository;
import de.sinnix.judoturnier.config.GewichtsklassenConfiguration;
import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Farbe;
import de.sinnix.judoturnier.model.Geschlecht;
import de.sinnix.judoturnier.model.Gewichtsklasse;
import de.sinnix.judoturnier.model.GewichtsklassenGruppe;
import de.sinnix.judoturnier.model.RandoriGruppenName;
import de.sinnix.judoturnier.model.TurnierTyp;
import de.sinnix.judoturnier.model.Wettkaempfer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GewichtsklassenService {

	private static final Logger logger = LogManager.getLogger(GewichtsklassenService.class);

	@Autowired
	private GewichtsklassenRepository gewichtsklassenRepository;
	@Autowired
	private WettkaempferService       wettkaempferService;
	@Autowired
	private EinstellungenService      einstellungenService;
	@Autowired
	private WettkaempferRepository    wettkaempferRepository;


	public List<GewichtsklassenGruppe> lade() {
		logger.info("lade alle GewichtsklassenGruppen");
		return gewichtsklassenRepository.findAll().stream()
			.sorted((a, b) -> {
				if (a.altersKlasse().getReihenfolge() != b.altersKlasse().getReihenfolge()) {
					return Integer.compare(a.altersKlasse().getReihenfolge(), b.altersKlasse().getReihenfolge()); // nach Alter sortieren
				} else {
					return Double.compare(a.maxGewicht(), b.maxGewicht()); // wenn Alter gleich, dann aufsteigend nach Gewicht sortieren
				}
			})
			.collect(Collectors.toList());
	}

	public List<GewichtsklassenGruppe> teileInGewichtsklassen(List<Wettkaempfer> wettkaempferListe) {
		logger.info("Erstelle Gewichtsklassen...");

		var einstellungen = einstellungenService.ladeEinstellungen();

		// erstelle die GewichtsklassenGruppen
		List<GewichtsklassenGruppe> result = new ArrayList<>();

		if (einstellungen.turnierTyp() == TurnierTyp.RANDORI) {
			logger.info("Randori-Turnier");
			logger.debug("Bei einem Randori-Turnier wird nicht nach geschlecht unterschieden, es wird daher keine Einteilung vorgenommen");
		    var randoriGruppenGroesse = einstellungen.randoriGruppengroesse().anzahl();
			var wettkaempferNachAlter = gruppiereNachAlterklasse(wettkaempferListe);

			// erstelle random Namen für die Gruppen
			var gruppenNamen = RandoriGruppenName.alleGruppenNamen();

			for (List<Wettkaempfer> wks : wettkaempferNachAlter) {
				int numGroups = (wks.size() + randoriGruppenGroesse - 1) / randoriGruppenGroesse;
				List<RandoriGruppenName> aktuelleGruppenNamen = gruppenNamen.subList(0, numGroups + 1); // we need one more name for an empty group
				gruppenNamen = gruppenNamen.subList(numGroups, gruppenNamen.size());
				result.addAll(erstelleGewichtsklassenGruppenRandori(wks, aktuelleGruppenNamen, randoriGruppenGroesse));
			}
		} else {
			logger.info("Normales-Turnier");
			List<List<Wettkaempfer>> gruppenNachGeschlecht = gruppiereNachGeschlecht(wettkaempferListe);
			List<List<List<Wettkaempfer>>> gruppenNachGeschlechtUndAlter = new ArrayList<>();
			var variablerGewichtsTeil = einstellungen.variablerGewichtsteil().variablerTeil();

			for (List<Wettkaempfer> g : gruppenNachGeschlecht) {
				List<List<Wettkaempfer>> gruppenNachAlter = gruppiereNachAlterklasse(g);
				gruppenNachGeschlechtUndAlter.add(gruppenNachAlter);
			}

			result = gruppenNachGeschlechtUndAlter.stream()
				.flatMap(gs -> gs.stream().flatMap(g -> erstelleGewichtsklassenGruppen(g, variablerGewichtsTeil).stream()))
				.collect(Collectors.toList());
		}

		return result;
	}

	public void loescheAlles() {
		logger.info("loesche alle GewichtsklassenGruppen...");
		gewichtsklassenRepository.deleteAll();
	}

	public void speichere(List<GewichtsklassenGruppe> gwks) {
		logger.info("speichere Liste der GewichtsklassenGruppe");
		gewichtsklassenRepository.saveAll(gwks);
	}

	public void loescheAltersklasse(Altersklasse altersklasse) {
		logger.info("loesche Altersklasse {}", altersklasse);
		gewichtsklassenRepository.deleteAllByAltersklasse(altersklasse);
	}

	public void aktualisiere(Map<Integer, List<Integer>> gruppenTeilnehmer) {
		logger.info("aktualisiere GewichtsklassenGruppe {}", gruppenTeilnehmer);
		var gewichtsklassenGruppen = gewichtsklassenRepository.findAll();
		var wettkaempfer = wettkaempferService.alleKaempfer();

		List<GewichtsklassenGruppe> updatedGewichtsklassenGruppen = new ArrayList<>();
		for (var gewichtsklassenGruppe : gewichtsklassenGruppen) {
			var teilnehmerIds = gruppenTeilnehmer.get(gewichtsklassenGruppe.id());

			// benötigt für die leere Gruppe
			if (teilnehmerIds == null || teilnehmerIds.isEmpty()) {
				updatedGewichtsklassenGruppen.add(gewichtsklassenGruppe);
				continue;
			}

			var wettkaempferListe = getWettkeampferListe(teilnehmerIds, wettkaempfer);
			var updatedMinGewicht = wettkaempferListe.stream().map(Wettkaempfer::gewicht).min(Double::compareTo).orElse(0d);
			var updatedMaxGewicht = wettkaempferListe.stream().map(Wettkaempfer::gewicht).max(Double::compareTo).orElse(200d);
			GewichtsklassenGruppe updatedGewichtsklassenGruppe = new GewichtsklassenGruppe(
				gewichtsklassenGruppe.id(),
				gewichtsklassenGruppe.altersKlasse(),
				gewichtsklassenGruppe.gruppenGeschlecht(),
				wettkaempferListe,
				gewichtsklassenGruppe.name(),
				updatedMinGewicht,
				updatedMaxGewicht
			);
			updatedGewichtsklassenGruppen.add(updatedGewichtsklassenGruppe);
		}

		gewichtsklassenRepository.saveAll(updatedGewichtsklassenGruppen);
	}

	private List<Wettkaempfer> getWettkeampferListe(List<Integer> teilnehmerIds, List<Wettkaempfer> wettkaempferList) {
		if (teilnehmerIds == null || teilnehmerIds.isEmpty()) return new ArrayList<>();

		List<Wettkaempfer> newWettkaempferList = new ArrayList<>();
		for (var id : teilnehmerIds) {
			for (var w : wettkaempferList) {
				if (id == w.id()) newWettkaempferList.add(w);
			}
		}
		return newWettkaempferList;
	}

	private List<List<Wettkaempfer>> gruppiereNachAlterklasse(List<Wettkaempfer> kaempferListe) {
		// Gruppiert die Wettkämpfer nach Altersklasse
		return kaempferListe.stream()
			.collect(Collectors.groupingBy(Wettkaempfer::altersklasse))
			.values().stream().collect(Collectors.toList());
	}

	private List<List<Wettkaempfer>> gruppiereNachGeschlecht(List<Wettkaempfer> kaempferListe) {
		// Gruppiert die Wettkämpfer nach Geschlecht
		return kaempferListe.stream()
			.collect(Collectors.groupingBy(Wettkaempfer::geschlecht))
			.values().stream().collect(Collectors.toList());
	}

	private List<GewichtsklassenGruppe> erstelleGewichtsklassenGruppen(List<Wettkaempfer> kaempferListe, Double variablerGewichtsTeil) {
		return kaempferListe.stream()
			.collect(Collectors.groupingBy(wettkaempfer -> gewichtsKlasse(wettkaempfer, variablerGewichtsTeil)))
			.entrySet().stream()
			.filter(entry -> !entry.getValue().isEmpty())
			.map(entry -> {
				List<Wettkaempfer> gruppe = entry.getValue();
				double minGewicht = gruppe.stream().mapToDouble(Wettkaempfer::gewicht).min().orElse(0);
				double maxGewicht = gruppe.stream().mapToDouble(Wettkaempfer::gewicht).max().orElse(200);
				Geschlecht geschlecht = gruppe.get(0).geschlecht();
				Altersklasse altersKlasse = gruppe.get(0).altersklasse();
				return new GewichtsklassenGruppe(null, altersKlasse, Optional.ofNullable(geschlecht), gruppe, Optional.empty(), minGewicht, maxGewicht);
			}).collect(Collectors.toList());
	}

	private List<GewichtsklassenGruppe> erstelleGewichtsklassenGruppenRandori(List<Wettkaempfer> kaempferListe, List<RandoriGruppenName> gruppenNamen, Integer gruppengroesse) {
		List<GewichtsklassenGruppe> gewichtsklassenGruppen = new ArrayList<>();
		int anzahlRandoriGruppen = (kaempferListe.size() + gruppengroesse - 1) / gruppengroesse;
		logger.info("erstelle " + anzahlRandoriGruppen + " Gruppen...");

		List<List<Wettkaempfer>> wettkaempferGruppen = randoriKlassen(kaempferListe, gruppengroesse);

		for (int current = 0; current < anzahlRandoriGruppen; current++) {
			List<Wettkaempfer> gruppe = wettkaempferGruppen.get(current);
			Altersklasse altersKlasse = gruppe.get(0).altersklasse();
			RandoriGruppenName randoriGruppe = gruppenNamen.get(current);
			double maxGewicht = gruppe.stream().mapToDouble(Wettkaempfer::gewicht).max().orElse(0);
			double minGewicht = gruppe.stream().mapToDouble(Wettkaempfer::gewicht).min().orElse(0);
			gewichtsklassenGruppen.add(new GewichtsklassenGruppe(null, altersKlasse, Optional.empty(), gruppe, Optional.of(randoriGruppe), minGewicht, maxGewicht));
		}

		RandoriGruppenName randoriGruppe = gruppenNamen.get(anzahlRandoriGruppen);
		gewichtsklassenGruppen.add(new GewichtsklassenGruppe(null, wettkaempferGruppen.get(0).get(0).altersklasse(), Optional.empty(), new ArrayList<>(), Optional.of(randoriGruppe), 0d, 100d));

		return gewichtsklassenGruppen;
	}

	private Gewichtsklasse gewichtsKlasse(Wettkaempfer wettkaempfer, Double variablerGewichtsTeil) {
		if (wettkaempfer.gewicht() == 0) {
			return new Gewichtsklasse("Ohne", 0d);
		}
		List<Gewichtsklasse> gewichtsklassen = GewichtsklassenConfiguration.getGewichtsklasse(wettkaempfer.geschlecht(), wettkaempfer.altersklasse());
		return gewichtsklassen.stream()
			.filter(gk -> wettkaempfer.gewicht() <= gk.gewicht() + variablerGewichtsTeil)
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("Unbekannte Gewichtsklasse: " + wettkaempfer.gewicht()));
	}

	private List<List<Wettkaempfer>> randoriKlassen(List<Wettkaempfer> wettkaempfer, int gruppenGroesse) {
		wettkaempfer.sort(Comparator.comparingDouble(Wettkaempfer::gewicht));

		List<List<Wettkaempfer>> gruppen = new ArrayList<>();
		List<Wettkaempfer> aktuelleGruppe = new ArrayList<>();

		for (Wettkaempfer kaempfer : wettkaempfer) {
			Farbe farbe = getFarbe(aktuelleGruppe.size());
			Wettkaempfer farbigerKaempfer = new Wettkaempfer(kaempfer.id(), kaempfer.name(), kaempfer.geschlecht(), kaempfer.altersklasse(), kaempfer.verein(), kaempfer.gewicht(), Optional.of(farbe), kaempfer.checked(), kaempfer.printed());
			if (aktuelleGruppe.size() < gruppenGroesse) {
				aktuelleGruppe.add(farbigerKaempfer);
			} else {
				gruppen.add(aktuelleGruppe);
				aktuelleGruppe = new ArrayList<>();
				aktuelleGruppe.add(farbigerKaempfer);
			}
			logger.trace("Name {}, Farbe {}", farbigerKaempfer.name(), farbigerKaempfer.farbe());
			wettkaempferRepository.save(farbigerKaempfer);
		}

		if (!aktuelleGruppe.isEmpty()) {
			gruppen.add(aktuelleGruppe);
		}

		return gruppen;
	}

	private Farbe getFarbe(int index) {
		Farbe[] farben = Farbe.values();
		return farben[index];
	}
}
