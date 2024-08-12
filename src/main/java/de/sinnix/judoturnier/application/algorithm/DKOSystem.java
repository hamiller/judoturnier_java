package de.sinnix.judoturnier.application.algorithm;

import de.sinnix.judoturnier.model.Begegnung;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DKOSystem {


	public static final int GEWINNER_RUNDE = 1;
	public static final int VERLIERER_RUNDE = 2;
	public static final int FREILOS = -1;

	// Id-Klasse - damit jede Id eine automatische String-Repräsentation hat
	public static class Id {
		public int bracket;
		public int runde;
		public int spiel;

		public Id(int bracket, int runde, int spiel) {
			this.bracket = bracket;
			this.runde = runde;
			this.spiel = spiel;
		}

		@Override
		public String toString() {
			return (this.bracket == GEWINNER_RUNDE ? "GR" : "VR") + " R" + this.runde + " S" + this.spiel;
		}
	}

	// ------------------------------------------------------------------
	// Initialisierungshilfen
	// ------------------------------------------------------------------

	private static List<Integer> leer() {
		return Arrays.asList(-1, -1);
	}

	// Markiere Spieler, die hinzugefügt werden mussten, um das Modell zu vervollständigen, als Freilose
	private static List<Integer> freilosMarkierung(List<Integer> spielerListe, int gesamtAnzahlSpieler) {
		return spielerListe.stream()
			.map(spieler -> (spieler > gesamtAnzahlSpieler) ? FREILOS : spieler)
			.collect(Collectors.toList());
	}

	// Abkürzung, um eine Spiel-ID zu erstellen, da KO-Turniere sehr spezifisch bezüglich der Positionen sind
	private static Id erstelleID(int bracket, int runde, int spiel) {
		return new Id(bracket, runde, spiel);
	}

	// Hilfsfunktionen zur Initialisierung von KO-Turnieren
	private static int berechneGeradenPlatz(int aktuellerPlatz, int gesamtRunden) {
		int aktuelleRunde = (int) Math.floor(Math.log(aktuellerPlatz) / Math.log(2));
		int rest = aktuellerPlatz - (int) Math.pow(2, aktuelleRunde);
		if (rest == 0) {
			return (int) Math.pow(2, gesamtRunden - aktuelleRunde);
		}
		String umgekehrteBinaerzahl = new StringBuilder(Integer.toBinaryString(aktuellerPlatz - 2 * rest)).reverse().toString();
		return (Integer.parseInt(umgekehrteBinaerzahl, 2) << gesamtRunden - umgekehrteBinaerzahl.length()) + (int) Math.pow(2, gesamtRunden - aktuelleRunde - 1);
	}

	// Hole die initialen Spieler für Spiel i in einem p-KO-Turnier
	private static List<Integer> setzungen(int spielNummer, int gesamtRunden) {
		int geraderPlatz = berechneGeradenPlatz(spielNummer, gesamtRunden);
		return Arrays.asList((int) Math.pow(2, gesamtRunden) + 1 - geraderPlatz, geraderPlatz);
	}

	// Erstelle ALLE Spiele für ein KO-Turnier
	public static List<Begegnung> eliminierung(int gesamtAnzahlSpieler, int gesamtRunden, int letzteRunde, boolean istLang) {
		List<Begegnung> spieleListe = new ArrayList<>();

		// erste GR-Runde zur Initialisierung der Spieler
		for (int i = 1; i <= Math.pow(2, gesamtRunden - 1); i++) {
			spieleListe.add(new Begegnung(erstelleID(GEWINNER_RUNDE, 1, i), freilosMarkierung(setzungen(i, gesamtRunden), gesamtAnzahlSpieler)));
		}

		// leere GR-Runden
		for (int aktuelleRunde = 2; aktuelleRunde <= gesamtRunden; aktuelleRunde++) {
			for (int aktuellesSpiel = 1; aktuellesSpiel <= Math.pow(2, gesamtRunden - aktuelleRunde); aktuellesSpiel++) {
				spieleListe.add(new Begegnung(erstelleID(GEWINNER_RUNDE, aktuelleRunde, aktuellesSpiel), leer()));
			}
		}

		// leere VR-Runden
		if (letzteRunde >= VERLIERER_RUNDE) {
			for (int aktuelleRunde = 1; aktuelleRunde <= 2 * gesamtRunden - 2; aktuelleRunde++) {
				// Anzahl der Spiele halbiert sich in jeder ungeraden Runde im Verliererbracket
				for (int aktuellesSpiel = 1; aktuellesSpiel <= Math.pow(2, gesamtRunden - 1 - Math.floorDiv(aktuelleRunde + 1, 2)); aktuellesSpiel++) {
					spieleListe.add(new Begegnung(erstelleID(VERLIERER_RUNDE, aktuelleRunde, aktuellesSpiel), leer()));
				}
			}
			spieleListe.add(new Begegnung(erstelleID(VERLIERER_RUNDE, 2 * gesamtRunden - 1, 1), leer())); // Großes Finale Spiel 1
		}
		if (istLang) {
			// Bronze-Finale, wenn letzteRunde === GEWINNER_RUNDE, sonst Großes Finale Spiel 2
			spieleListe.add(new Begegnung(erstelleID(VERLIERER_RUNDE, letzteRunde == VERLIERER_RUNDE ? 2 * gesamtRunden : 1, 1), leer()));
		}
		return spieleListe;
	}

	// ------------------------------------------------------------------
	// Fortschrittshilfen - instance context
	// ------------------------------------------------------------------

	// Finde das Spiel und die Position, zu der ein Gewinner "nach rechts" im aktuellen Bracket verschoben werden soll
	private Id verschiebeNachRechts(Id id, int gesamtRunden, int letzteRunde, boolean istLang, boolean downMix) {
		int bracket = id.bracket;
		int runde = id.runde;
		int spiel = id.spiel;

		// Fälle, in denen der Fortschritt für Gewinner endet
		boolean istFinaleEinfach = (letzteRunde == GEWINNER_RUNDE && runde == gesamtRunden);
		boolean istFinaleDoppelt = (letzteRunde == VERLIERER_RUNDE && bracket == VERLIERER_RUNDE && runde == 2 * gesamtRunden);
		boolean istBronze = (letzteRunde == GEWINNER_RUNDE && bracket == VERLIERER_RUNDE);
		boolean istKurzesVRFinale = (bracket == VERLIERER_RUNDE && runde == 2 * gesamtRunden - 1 && !istLang);

		if (istFinaleEinfach || istFinaleDoppelt || istBronze || istKurzesVRFinale) {
			return null;
		}

		// Sonderfall: Gewinner der GR verschiebt sich ins VR-GF S1
		if (letzteRunde >= VERLIERER_RUNDE && bracket == GEWINNER_RUNDE && runde == gesamtRunden) {
			return erstelleID(VERLIERER_RUNDE, 2 * gesamtRunden - 1, 1);
		}

		// für VR-Positionierung
		int halbesSpiel = (bracket == VERLIERER_RUNDE && runde % 2 == 1) ? spiel : (spiel + 1) / 2;

		int position;
		if (bracket == GEWINNER_RUNDE) {
			position = (spiel + 1) % 2; // normale GR-Progression
		}
		// VR-Progression
		else if (runde >= 2 * gesamtRunden - 2) {
			position = (runde + 1) % 2; // VR-Finalgewinner -> unten & GF(1) Außenseitergewinner -> oben
		} else if (runde == 1) {
			// außer downMix, LBR1-Gewinner bewegen sich invers zur normalen Progression
			position = downMix ? 1 : spiel % 2;
		} else {
			// Gewinner aus VR immer unten in ungeraden Runden, sonst normale Progression
			position = runde % 2 == 1 ? 1 : (spiel + 1) % 2;
		}

		// normale Progression
		return erstelleID(bracket, runde + 1, halbesSpiel);
	}

	// Finde das Spiel und die Position, zu der ein Verlierer "nach unten" im aktuellen Bracket verschoben werden soll
	private Id verschiebeNachUnten(Id id, int gesamtRunden, int letzteRunde, boolean istLang, boolean downMix) {
		int bracket = id.bracket;
		int runde = id.runde;
		int spiel = id.spiel;

		// K.-o.-Runden / spezielle Finals
		if (bracket >= letzteRunde) { // größer als Fall ist für BF in langem Einzel-K.-o.-Turnier
			if (bracket == GEWINNER_RUNDE && istLang && runde == gesamtRunden - 1) {
				// wenn Bronze-Finale, verschiebe den Verlierer zu "LBR1" an der Spiegelposition von WBGF
				return erstelleID(VERLIERER_RUNDE, 1, 1);
			}
			if (bracket == VERLIERER_RUNDE && runde == 2 * gesamtRunden - 1 && istLang) {
				// wenn Doppel-Finale, dann verschiebe den Verlierer nach unten
				return erstelleID(VERLIERER_RUNDE, 2 * gesamtRunden, 1);
			}
			// sonst immer KO'd bei Verlierer >= letzte Runde
			return null;
		}

		// WBR1 wird immer in LBR1 gespeist, als ob es WBR2 wäre
		if (runde == 1) {
			return erstelleID(VERLIERER_RUNDE, 1, (spiel + 1) / 2);
		}

		if (downMix) {
			// immer oben einsetzen bei Downmixing
			return erstelleID(VERLIERER_RUNDE, (runde - 1) * 2, mischeVRSpiele(gesamtRunden, runde, spiel));
		}

		// normale VR-Abstiege: oben für (runde > 2) und (runde <= 2 wenn ungerades spiel) zur Anpassung an die Bracket-Bewegung
		int position = (runde > 2 || spiel % 2 == 1) ? 0 : 1;
		return erstelleID(VERLIERER_RUNDE, (runde - 1) * 2, spiel);
	}

	// gegeben eine Richtung (eines der oben genannten), verschiebe einen 'Aufsteiger' an diesen Ort
	public void spieleinsatz(Id fortschritt, int aufsteiger, List<Begegnung> spieleListe) {
		if (fortschritt != null) {
			Begegnung zielBegegnung = findeBegegnung(fortschritt, spieleListe);
			if (zielBegegnung == null) {
				throw new IllegalArgumentException("Turnier fehlerhaft: " + fortschritt + " nicht gefunden!");
			}

			int position = fortschritt.spiel;
			zielBegegnung.wettkaempfer.set(position, aufsteiger);
			if (zielBegegnung.wettkaempfer.get((position + 1) % 2) == FREILOS) {
				zielBegegnung.ergebnis = position == 0 ? Arrays.asList(1, 0) : Arrays.asList(0, 1); // setze WO-Ergebnismarker
			}
		}
	}

	// Hilfsfunktion, um Matches mit Freilosen korrekt zu bewerten
	private void bewerteFreilos(Id fortschritt, Begegnung begegnung) {
		int index = begegnung.wettkaempfer.indexOf(FREILOS);
		if (index >= 0) {
			// setze die Ergebnisse manuell, um die `_verify` Freilos-Wertungseinschränkung zu vermeiden
			begegnung.ergebnis = index == 0 ? Arrays.asList(0, 1) : Arrays.asList(1, 0);
			spieleinsatz(fortschritt, begegnung.wettkaempfer.get(index == 0 ? 1 : 0), Arrays.asList(begegnung));
		}
	}

	// Hilfsklasse für Spiel
//	public static class Begegnung {
//		public Id            id;
//		public List<Integer> wettkaempfer;
//		public List<Integer> ergebnis;
//
//		public Begegnung(Id id, List<Integer> wettkaempfer) {
//			this.id = id;
//			this.wettkaempfer = new ArrayList<>(wettkaempfer);
//			this.ergebnis = null;
//		}
//	}

	// Finde Spiel in der Liste
	private Begegnung findeBegegnung(Id id, List<Begegnung> begegnungList) {
		for (Begegnung begegnung : begegnungList) {
			if (begegnung.getBegegnungId().equals(id)) {
				return begegnung;
			}
		}
		return null;
	}

	// Hilfsfunktion zur Vermischung der VR-Spiele
	private int mischeVRSpiele(int gesamtRunden, int aktuelleRunde, int aktuellesSpiel) {
		int anzahlSpiele = (int) Math.pow(2, gesamtRunden - aktuelleRunde);
		int mittelpunkt = (int) Math.floor(Math.pow(2, gesamtRunden - aktuelleRunde - 1)); // Mittelpunkt 0 in den Finals

		boolean umgekehrt = (aktuelleRunde / 2) % 2 == 1;
		boolean partitioniert = ((aktuelleRunde + 1) / 2) % 2 == 0;

		if (partitioniert) {
			if (umgekehrt) {
				return (aktuellesSpiel > mittelpunkt) ? anzahlSpiele - aktuellesSpiel + mittelpunkt + 1 : mittelpunkt - aktuellesSpiel + 1;
			}
			return (aktuellesSpiel > mittelpunkt) ? aktuellesSpiel - mittelpunkt : aktuellesSpiel + mittelpunkt;
		}
		return umgekehrt ? anzahlSpiele - aktuellesSpiel + 1 : aktuellesSpiel;
	}

}

