package de.sinnix.judoturnier.application.algorithm;

import de.sinnix.judoturnier.model.Begegnung;
import de.sinnix.judoturnier.model.GewichtsklassenGruppe;
import de.sinnix.judoturnier.model.Wettkaempfer;
import de.sinnix.judoturnier.model.WettkampfGruppe;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class DoppelKOSystem implements Algorithmus {

	private static final Logger logger          = LogManager.getLogger(DoppelKOSystem.class);
	public static final  int    VERLIERER_RUNDE = 2;


	@Override
	public List<WettkampfGruppe> erstelleWettkampfGruppen(Integer gruppenid, GewichtsklassenGruppe gewichtsklassenGruppe, Integer maxGruppenGroesse) {
		List<WettkampfGruppe> result = new ArrayList<>();

		// Teilnehmer der Gruppe
		List<Wettkaempfer> teilnehmer = gewichtsklassenGruppe.teilnehmer();
		int teilnehmerAnzahl = teilnehmer.size();

		// Alle Begegnungen in der Gruppe generieren
		int gesamtRunden = (int) Math.ceil(Math.log(teilnehmerAnzahl) / Math.log(2));
		boolean bronzeFinale = true;
		int letzteRunde = 1;

		List<Begegnung> begegnungen = erstelleBegegnungen(teilnehmer, teilnehmerAnzahl, gesamtRunden, letzteRunde, bronzeFinale);


		String id = ((gruppenid + 1) * 10) + Integer.toString(1); // ids erstellen und konkatenieren
//		WettkampfGruppe wettkampfGruppe = new WettkampfGruppe(
//			Integer.parseInt(id),
//			gewichtsklassenGruppe.name().orElseGet(() -> RandoriGruppenName.Ameise).name(),
//			"(" + gewichtsklassenGruppe.minGewicht() + "-" + gewichtsklassenGruppe.maxGewicht() + " " + gewichtsklassenGruppe.altersKlasse() + ")",
//			begegnungen,
//			gewichtsklassenGruppe.turnierUUID()
//		);
//		result.add(wettkampfGruppe);


		// Ausgabe des Turnierbaums
		for (Begegnung begegnung : begegnungen) {
			logger.info("{} - Wettkaempfer1: {}, Wettkaempfer2: {}, Ergebnis: {}", begegnung.getBegegnungId(), begegnung.getWettkaempfer1().map(Wettkaempfer::name), begegnung.getWettkaempfer2().map(Wettkaempfer::name), begegnung.getWertungen());
		}
		return result;

	}

	// Erstelle ALLE Spiele für ein KO-Turnier
	public List<Begegnung> erstelleBegegnungen(List<Wettkaempfer> teilnehmer, int gesamtAnzahlSpieler, int gesamtRunden, int letzteRunde, boolean bronzeFinale) {
		List<Begegnung> spieleListe = new ArrayList<>();

		logger.debug("gesamtAnzahlSpieler: {}", gesamtAnzahlSpieler);
		logger.debug("gesamtRunden: {}", gesamtRunden);
		logger.debug("letzteRunde: {}", letzteRunde);

		List<Wettkaempfer> gemischteTeilnehmer = mischeTeilnehmer(teilnehmer);

		// in der ersten Runde müssen eventuell Freilose erstellt werden, sodass im weiteren Turnierverlauf ein ausgeglichener Baum entsteht
		List<Pair<Optional<Wettkaempfer>, Optional<Wettkaempfer>>> paarungen = freilosMarkierung(gemischteTeilnehmer, gesamtRunden);

		// erste Gewinner-Runde zur Initialisierung der Spieler
		for (int akuellePaarungsId = 1; akuellePaarungsId <= Math.pow(2, gesamtRunden - 1); akuellePaarungsId++) {
			var begegnung = new Begegnung();
			begegnung.setTurnierUUID(UUID.randomUUID());
			begegnung.setBegegnungId(erstelleID(Begegnung.RundenTyp.GEWINNER_RUNDE, 1, akuellePaarungsId));
			begegnung.setWettkaempfer1(paarungen.get(akuellePaarungsId - 1).getLeft());
			begegnung.setWettkaempfer2(paarungen.get(akuellePaarungsId - 1).getRight());
			spieleListe.add(begegnung);
		}

		// leere Gewinner-Runden
		for (int aktuelleRunde = 2; aktuelleRunde <= gesamtRunden; aktuelleRunde++) {
			for (int akuellePaarungsId = 1; akuellePaarungsId <= Math.pow(2, gesamtRunden - aktuelleRunde); akuellePaarungsId++) {
				spieleListe.add(leereBegegnung(erstelleID(Begegnung.RundenTyp.GEWINNER_RUNDE, aktuelleRunde, akuellePaarungsId)));
			}
		}

		// leere Verlierer-Runden
		if (letzteRunde >= VERLIERER_RUNDE) {
			for (int aktuelleRunde = 1; aktuelleRunde <= 2 * gesamtRunden - 2; aktuelleRunde++) {
				// Anzahl der Spiele halbiert sich in jeder ungeraden Runde im Verlierer-Turnierbaum
				for (int akuellePaarungsId = 1; akuellePaarungsId <= Math.pow(2, gesamtRunden - 1 - Math.floorDiv(aktuelleRunde + 1, 2)); akuellePaarungsId++) {
					spieleListe.add(leereBegegnung(erstelleID(Begegnung.RundenTyp.VERLIERER_RUNDE, aktuelleRunde, akuellePaarungsId)));
				}
			}
			spieleListe.add(leereBegegnung(erstelleID(Begegnung.RundenTyp.VERLIERER_RUNDE, 2 * gesamtRunden - 1, 1))); // Großes Finale Spiel 1
		}
		if (bronzeFinale) {
			// Bronze-Finale, wenn letzteRunde === GEWINNER_RUNDE, sonst Großes Finale Spiel 2
			spieleListe.add(leereBegegnung(erstelleID(Begegnung.RundenTyp.VERLIERER_RUNDE, letzteRunde == VERLIERER_RUNDE ? 2 * gesamtRunden : 1, 1)));
		}
		return spieleListe;
	}

	private static List<Wettkaempfer> mischeTeilnehmer(List<Wettkaempfer> teilnehmer) {
		// TODO
		return teilnehmer;
	}

	/**
	 * Erstellt Begegnungen mit Freiloses, d.h. einzelne Kämpfer haben keinen Gegner. Die ist erforderlich um einen ausgeglichenen Baum zu erhalten:
	 * Das Finale ist eine Begegnung, das Halbfinale zwei Begegnungen, Vierltelfinale vier Begegnungen usw.
	 */
	private static List<Pair<Optional<Wettkaempfer>, Optional<Wettkaempfer>>> freilosMarkierung(List<Wettkaempfer> wettkaempfer, int gesamtRunden) {
		List<Pair<Optional<Wettkaempfer>, Optional<Wettkaempfer>>> paarungen = new ArrayList<>();

		// Berechne die Anzahl der Paarungen in der ersten Runde
		// für einen ausgeglichenen Baum benötigen wir immer ein Vielfaches von 2^x
		int anzahlPaarungenRunde1 = (int) Math.pow(2, gesamtRunden - 1);

		List<Wettkaempfer> erweiterteListe = new ArrayList<>(wettkaempfer);

		// wir starten erst mit dem zweiten Eintrag, der erste Kämpfer ist nie ein Freilos
		int insertPosition = 1;
		// Füge null an jeder zweiten Stelle ein, bis die Ziel-Länge erreicht ist
		while (erweiterteListe.size() < anzahlPaarungenRunde1 * 2) {
			erweiterteListe.add(insertPosition, null);
			insertPosition += 2;
		}
		logger.debug("erweiterteListe: {}", erweiterteListe);

		// Erstelle die Paarungen
		for (int i = 0; i < anzahlPaarungenRunde1 * 2; i += 2) {
			Optional<Wettkaempfer> wettkaempfer1 = Optional.ofNullable(erweiterteListe.get(i));
			Optional<Wettkaempfer> wettkaempfer2 = Optional.ofNullable(erweiterteListe.get(i + 1));
			paarungen.add(new ImmutablePair<>(wettkaempfer1, wettkaempfer2));
		}

		return paarungen;
	}

	// Abkürzung, um eine Spiel-ID zu erstellen, da KO-Turniere sehr spezifisch bezüglich der Positionen sind
	private Begegnung.BegegnungId erstelleID(Begegnung.RundenTyp rundenTyp, int runde, int akuellePaarungsId) {
		return new Begegnung.BegegnungId(rundenTyp, runde, akuellePaarungsId);
	}

	private Begegnung leereBegegnung(Begegnung.BegegnungId begegnungId) {
		var begegnung = new Begegnung();
		begegnung.setTurnierUUID(UUID.randomUUID());
		begegnung.setBegegnungId(begegnungId);
		// leer
		begegnung.setWettkaempfer1(Optional.empty());
		begegnung.setWettkaempfer2(Optional.empty());
		return begegnung;
	}

}
