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
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Stack;
import java.util.UUID;
import java.util.stream.Collectors;

@Transactional
@Service
public class GewichtsklassenService {

	private static final Logger logger = LogManager.getLogger(GewichtsklassenService.class);

	private static final Double DEFAULT_MIN_GEWICHT = 0.0;
	private static final Double DEFAULT_MAX_GEWICHT = 200.0;

	@Lazy
	@Autowired
	private WettkaempferService       wettkaempferService;
	@Lazy
	@Autowired
	private EinstellungenService      einstellungenService;
	@Lazy
	@Autowired
	private TurnierService            turnierService;
	@Autowired
	private WettkaempferRepository    wettkaempferRepository;
	@Autowired
	private GewichtsklassenRepository gewichtsklassenRepository;


	public List<GewichtsklassenGruppe> ladeGewichtsklassenGruppen(UUID turnierUUID) {
		logger.info("lade alle GewichtsklassenGruppen");
		return gewichtsklassenRepository.findAll(turnierUUID).stream()
			.sorted((a, b) -> {
				if (a.altersKlasse().getReihenfolge() != b.altersKlasse().getReihenfolge()) {
					return Integer.compare(a.altersKlasse().getReihenfolge(), b.altersKlasse().getReihenfolge()); // nach Alter sortieren
				} else {
					return Double.compare(a.maxGewicht(), b.maxGewicht()); // wenn Alter gleich, dann aufsteigend nach Gewicht sortieren
				}
			})
			.collect(Collectors.toList());
	}

	public List<GewichtsklassenGruppe> ladeGewichtsklassenGruppe(Altersklasse altersklasse, UUID turnierUUID) {
		return ladeGewichtsklassenGruppen(turnierUUID).stream()
			.filter(g -> g.altersKlasse().equals(altersklasse))
			.sorted(Comparator.comparingDouble(GewichtsklassenGruppe::maxGewicht))
			.peek(g -> logger.debug("Gruppengröße {}", g.teilnehmer().size()))
			.collect(Collectors.toList());
	}

	public List<GewichtsklassenGruppe> teileInGewichtsklassen(List<Wettkaempfer> wettkaempferListe, UUID turnierUUID) {
		logger.info("Erstelle Gewichtsklassen...");

		var einstellungen = einstellungenService.ladeEinstellungen(turnierUUID);

		// erstelle die GewichtsklassenGruppen
		List<GewichtsklassenGruppe> result = new ArrayList<>();

		if (einstellungen.turnierTyp() == TurnierTyp.RANDORI) {
			logger.info("Randori-Turnier");
			logger.debug("Bei einem Randori-Turnier wird nicht nach geschlecht unterschieden, es wird daher keine Einteilung vorgenommen");
			var wettkaempferNachAlter = gruppiereNachAlterklasse(wettkaempferListe);

			// erstelle random Namen für die Gruppen
			var gruppenNamen = RandoriGruppenName.alleGruppenNamen();
			Stack<RandoriGruppenName> aktuelleGruppenNamen = new Stack<>();
			aktuelleGruppenNamen.addAll(gruppenNamen);
			// entferne schon genutzte Namen
			gewichtsklassenRepository.findAll(turnierUUID).stream().forEach(gwg -> gwg.name().map(n -> aktuelleGruppenNamen.removeElement(n)));

			for (List<Wettkaempfer> wks : wettkaempferNachAlter) {
				var gruppenGroesse = einstellungen.gruppengroessen().altersklasseGruppengroesse().get(wks.get(0).altersklasse());
				int numGroups = (wks.size() + gruppenGroesse - 1) / gruppenGroesse;
				gruppenNamen = gruppenNamen.subList(numGroups, gruppenNamen.size());
				result.addAll(erstelleGewichtsklassenGruppenRandori(wks, aktuelleGruppenNamen, gruppenGroesse, turnierUUID));
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
				.flatMap(gs -> gs.stream().flatMap(g -> {
					var gruppenGroesse = einstellungen.gruppengroessen().altersklasseGruppengroesse().get(g.get(0).altersklasse());
					return erstelleGewichtsklassenGruppen(g, variablerGewichtsTeil, gruppenGroesse, turnierUUID).stream();
				}))
				.collect(Collectors.toList());
		}

		return result;
	}

	public void loescheAlles(UUID turnierId) {
		logger.info("loesche alle GewichtsklassenGruppen für Turnier {}", turnierId);
		gewichtsklassenRepository.deleteAll(turnierId);
		turnierService.loescheWettkampfreihenfolge(turnierId);
	}

	public void speichere(List<GewichtsklassenGruppe> gwks) {
		logger.info("speichere Liste der GewichtsklassenGruppe");
		gewichtsklassenRepository.saveAll(gwks);
	}

	public void loescheAltersklasse(UUID turnierUUID, Altersklasse altersklasse) {
		logger.info("loesche Altersklasse {}", altersklasse);
		gewichtsklassenRepository.deleteAllByAltersklasse(turnierUUID, altersklasse);
	}

	public void aktualisiere(Map<UUID, List<UUID>> gruppenTeilnehmer, UUID turnierUUID) {
		logger.info("aktualisiere GewichtsklassenGruppen {}", gruppenTeilnehmer);
		var gewichtsklassenGruppen = gewichtsklassenRepository.findAll(turnierUUID);
		var wettkaempfer = wettkaempferService.alleKaempfer(turnierUUID);

		List<GewichtsklassenGruppe> updatedGewichtsklassenGruppen = new ArrayList<>();
		for (var gewichtsklassenGruppe : gewichtsklassenGruppen) {
			var teilnehmerIds = gruppenTeilnehmer.get(gewichtsklassenGruppe.id());

			// benötigt für die leere Gruppe
			if (teilnehmerIds == null || teilnehmerIds.isEmpty()) {
				GewichtsklassenGruppe updatedGewichtsklassenGruppe = new GewichtsklassenGruppe(
					gewichtsklassenGruppe.id(),
					gewichtsklassenGruppe.altersKlasse(),
					gewichtsklassenGruppe.gruppenGeschlecht(),
					List.of(),
					gewichtsklassenGruppe.name(),
					DEFAULT_MIN_GEWICHT,
					DEFAULT_MAX_GEWICHT,
					turnierUUID
				);

				updatedGewichtsklassenGruppen.add(updatedGewichtsklassenGruppe);
				continue;
			}

			var wettkaempferListe = getWettkeampferListe(teilnehmerIds, wettkaempfer);
			var updatedMinGewicht = wettkaempferListe.stream().map(Wettkaempfer::gewicht).min(Double::compareTo).orElse(DEFAULT_MIN_GEWICHT);
			var updatedMaxGewicht = wettkaempferListe.stream().map(Wettkaempfer::gewicht).max(Double::compareTo).orElse(DEFAULT_MAX_GEWICHT);
			GewichtsklassenGruppe updatedGewichtsklassenGruppe = new GewichtsklassenGruppe(
				gewichtsklassenGruppe.id(),
				gewichtsklassenGruppe.altersKlasse(),
				gewichtsklassenGruppe.gruppenGeschlecht(),
				wettkaempferListe,
				gewichtsklassenGruppe.name(),
				updatedMinGewicht,
				updatedMaxGewicht,
				turnierUUID
			);
			updatedGewichtsklassenGruppen.add(updatedGewichtsklassenGruppe);
		}

		gewichtsklassenRepository.saveAll(updatedGewichtsklassenGruppen);
	}

	private List<Wettkaempfer> getWettkeampferListe(List<UUID> teilnehmerIds, List<Wettkaempfer> wettkaempferList) {
		if (teilnehmerIds == null || teilnehmerIds.isEmpty()) return new ArrayList<>();

		List<Wettkaempfer> newWettkaempferList = new ArrayList<>();
		for (UUID id : teilnehmerIds) {
			for (Wettkaempfer w : wettkaempferList) {
				if (id.equals(w.id())) newWettkaempferList.add(w);
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

	private List<List<Wettkaempfer>> gruppiereNachGewichtsklasse(List<Wettkaempfer> kaempferListe, Double variablerGewichtsTeil) {
		// Gruppiert die Wettkämpfer nach Gewichtsklassen
		return kaempferListe.stream()
			.collect(Collectors.groupingBy(wettkaempfer -> gewichtsKlasse(wettkaempfer, variablerGewichtsTeil)))
			.values().stream().collect(Collectors.toList());
	}

	private List<GewichtsklassenGruppe> erstelleGewichtsklassenGruppen(List<Wettkaempfer> kaempferListe, Double variablerGewichtsTeil, Integer gruppenGroesse, UUID turnierUUID) {
		logger.debug("erstelle GewichtsklassenGruppe-Liste für {} {}", kaempferListe.get(0).altersklasse(), variablerGewichtsTeil);

		List<GewichtsklassenGruppe> gewichtsklassenGruppen = new ArrayList<>();
		if (kaempferListe.get(0) != null && !GewichtsklassenConfiguration.hasGewichtsklasse(kaempferListe.get(0).geschlecht(), kaempferListe.get(0).altersklasse())) {
			// die Altersklassen U9, U11 haben keine strenge Einteilung nach Gewichtsklassen, sondern werden gewichtsnah zusammen gestellt
			logger.info("Altersklassen {} {} wird noch nicht automatisch gewichtsnah zusammengestellt!", kaempferListe.get(0).geschlecht(), kaempferListe.get(0).altersklasse());

			var turnierGruppen = turnierGruppen(kaempferListe, gruppenGroesse);
			for (int current = 0; current < turnierGruppen.size(); current++) {
				List<Wettkaempfer> gruppe = turnierGruppen.get(current);
				Altersklasse altersKlasse = gruppe.get(0).altersklasse();
				double maxGewicht = gruppe.stream().mapToDouble(Wettkaempfer::gewicht).max().orElse(0);
				double minGewicht = gruppe.stream().mapToDouble(Wettkaempfer::gewicht).min().orElse(0);
				gewichtsklassenGruppen.add(new GewichtsklassenGruppe(null, altersKlasse, Optional.of(kaempferListe.get(0).geschlecht()), gruppe, Optional.empty(), minGewicht, maxGewicht, turnierUUID));
			}

			// füge leere Gewichtsklassengruppe hinzu um Wettkaempfer besser zuordnen zu können
			gewichtsklassenGruppen.add(new GewichtsklassenGruppe(null, kaempferListe.get(0).altersklasse(), Optional.of(kaempferListe.get(0).geschlecht()), new ArrayList<>(), Optional.empty(), DEFAULT_MIN_GEWICHT, DEFAULT_MAX_GEWICHT, turnierUUID));
			return gewichtsklassenGruppen;
		}


		var wettkaempferNachGewichtsklasse = gruppiereNachGewichtsklasse(kaempferListe, variablerGewichtsTeil);

		for (List<Wettkaempfer> wks : wettkaempferNachGewichtsklasse) {
			double minGewicht = wks.stream().mapToDouble(Wettkaempfer::gewicht).min().orElse(0);
			double maxGewicht = wks.stream().mapToDouble(Wettkaempfer::gewicht).max().orElse(200);
			Geschlecht geschlecht = wks.get(0).geschlecht();
			Altersklasse altersKlasse = wks.get(0).altersklasse();
			gewichtsklassenGruppen.add(new GewichtsklassenGruppe(null, altersKlasse, Optional.ofNullable(geschlecht), wks, Optional.empty(), minGewicht, maxGewicht, turnierUUID));
		}

		// füge leere Gewichtsklassengruppe hinzu um Wettkaempfer besser zuordnen zu können
		gewichtsklassenGruppen.add(new GewichtsklassenGruppe(null, kaempferListe.get(0).altersklasse(), Optional.of(kaempferListe.get(0).geschlecht()), new ArrayList<>(), Optional.empty(), DEFAULT_MIN_GEWICHT, DEFAULT_MAX_GEWICHT, turnierUUID));

		return gewichtsklassenGruppen;
	}

	private List<GewichtsklassenGruppe> erstelleGewichtsklassenGruppenRandori(List<Wettkaempfer> kaempferListe, Stack<RandoriGruppenName> gruppenNamen, Integer gruppengroesse, UUID turnierUUID) {
		List<GewichtsklassenGruppe> gewichtsklassenGruppen = new ArrayList<>();
		int anzahlRandoriGruppen = (kaempferListe.size() + gruppengroesse - 1) / gruppengroesse;
		logger.info("erstelle " + anzahlRandoriGruppen + " Gruppen...");

		List<List<Wettkaempfer>> wettkaempferGruppen = randoriGruppen(kaempferListe, gruppengroesse, turnierUUID);

		for (int current = 0; current < anzahlRandoriGruppen; current++) {
			List<Wettkaempfer> gruppe = wettkaempferGruppen.get(current);
			Altersklasse altersKlasse = gruppe.get(0).altersklasse();
			RandoriGruppenName randoriGruppe = gruppenNamen.pop();
			double maxGewicht = gruppe.stream().mapToDouble(Wettkaempfer::gewicht).max().orElse(0);
			double minGewicht = gruppe.stream().mapToDouble(Wettkaempfer::gewicht).min().orElse(0);
			gewichtsklassenGruppen.add(new GewichtsklassenGruppe(null, altersKlasse, Optional.empty(), gruppe, Optional.of(randoriGruppe), minGewicht, maxGewicht, turnierUUID));
		}

		RandoriGruppenName randoriGruppe = gruppenNamen.pop();
		// füge leere Gewichtsklassengruppe hinzu um Wettkaempfer besser zuordnen zu können
		gewichtsklassenGruppen.add(new GewichtsklassenGruppe(null, wettkaempferGruppen.get(0).get(0).altersklasse(), Optional.empty(), new ArrayList<>(), Optional.of(randoriGruppe), DEFAULT_MIN_GEWICHT, DEFAULT_MAX_GEWICHT, turnierUUID));

		return gewichtsklassenGruppen;
	}

	private Gewichtsklasse gewichtsKlasse(Wettkaempfer wettkaempfer, Double variablerGewichtsTeil) {
		if (wettkaempfer.gewicht() == 0) {
			return new Gewichtsklasse("Ohne", 0d);
		}
		List<Gewichtsklasse> gewichtsklassen = GewichtsklassenConfiguration.getGewichtsklassen(wettkaempfer.geschlecht(), wettkaempfer.altersklasse());
		return gewichtsklassen.stream()
			.filter(gk -> wettkaempfer.gewicht() <= gk.gewicht() + variablerGewichtsTeil)
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("Unbekannte Gewichtsklasse: " + wettkaempfer.gewicht()));
	}

	private List<List<Wettkaempfer>> randoriGruppen(List<Wettkaempfer> wettkaempfer, int gruppenGroesse, UUID turnierUUID) {
		wettkaempfer.sort(Comparator.comparingDouble(Wettkaempfer::gewicht));

		List<List<Wettkaempfer>> gruppen = new ArrayList<>();
		List<Wettkaempfer> aktuelleGruppe = new ArrayList<>();

		for (Wettkaempfer kaempfer : wettkaempfer) {
			Farbe farbe = getFarbe(aktuelleGruppe.size());
			Wettkaempfer farbigerKaempfer = new Wettkaempfer(kaempfer.id(), kaempfer.name(), kaempfer.geschlecht(), kaempfer.altersklasse(), kaempfer.verein(), kaempfer.gewicht(), Optional.of(farbe), kaempfer.checked(), kaempfer.printed(), turnierUUID);
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

	private List<List<Wettkaempfer>> turnierGruppen(List<Wettkaempfer> wettkaempfer, int gruppenGroesse) {
		wettkaempfer.sort(Comparator.comparingDouble(Wettkaempfer::gewicht));

		List<List<Wettkaempfer>> gruppen = new ArrayList<>();
		List<Wettkaempfer> aktuelleGruppe = new ArrayList<>();

		for (Wettkaempfer kaempfer : wettkaempfer) {
			if (aktuelleGruppe.size() < gruppenGroesse) {
				aktuelleGruppe.add(kaempfer);
			} else {
				gruppen.add(aktuelleGruppe);
				aktuelleGruppe = new ArrayList<>();
				aktuelleGruppe.add(kaempfer);
			}
		}

		if (!aktuelleGruppe.isEmpty()) {
			gruppen.add(aktuelleGruppe);
		}

		return gruppen;
	}
}
