package de.sinnix.judoturnier.application.algorithm;

import de.sinnix.judoturnier.model.Begegnung;
import de.sinnix.judoturnier.model.Wettkaempfer;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class DKOSystem {


	public static final int GEWINNER_RUNDE = 1;
	public static final int VERLIERER_RUNDE = 2;
	public static final int FREILOS = -1;


	// ------------------------------------------------------------------
	// Initialisierungshilfen
	// ------------------------------------------------------------------

	private static List<Integer> leer() {
		return Arrays.asList(-1, -1);
	}

	// Markiere Spieler, die hinzugefügt werden müssen um das Modell zu vervollständigen, als Freilose
	private static List<Pair<Optional<Wettkaempfer>, Optional<Wettkaempfer>>> freilosMarkierung1(List<Wettkaempfer> wettkaempferListe, int gesamtAnzahlSpieler) {
		List<Pair<Optional<Wettkaempfer>, Optional<Wettkaempfer>>> result = new ArrayList<>();
		for (int i = 0; i < gesamtAnzahlSpieler; i = i+2) {
			Optional<Wettkaempfer> wk1 = getElement(wettkaempferListe, i);
			Optional<Wettkaempfer> wk2 = getElement(wettkaempferListe, i +1);
			var paarung = new ImmutablePair(wk1, wk2);
			result.add(paarung);
		}
		return result;
	}

	private static Optional<Wettkaempfer> getElement(List<Wettkaempfer> wettkaempferListe, int i) {
		if (i >= wettkaempferListe.size()) return Optional.empty();
		return Optional.ofNullable(wettkaempferListe.get(i));
	}

	// Abkürzung, um eine Spiel-ID zu erstellen, da KO-Turniere sehr spezifisch bezüglich der Positionen sind
	private static Begegnung.BegegnungId erstelleID(Begegnung.RundenTyp rundenTyp, int runde, int akuellePaarungsId) {
		return new Begegnung.BegegnungId(rundenTyp, runde, akuellePaarungsId);
	}

	// Hilfsfunktionen zur Initialisierung von KO-Turnieren
	private static int berechnePlatz(int aktuellerPlatz, int gesamtRunden) {
		int aktuelleRunde = (int) Math.floor(Math.log(aktuellerPlatz) / Math.log(2));
		int rest = aktuellerPlatz - (int) Math.pow(2, aktuelleRunde);
		if (rest == 0) {
			return (int) Math.pow(2, gesamtRunden - aktuelleRunde);
		}
		String umgekehrteBinaerzahl = new StringBuilder(Integer.toBinaryString(aktuellerPlatz - 2 * rest)).reverse().toString();
		return (Integer.parseInt(umgekehrteBinaerzahl, 2) << gesamtRunden - umgekehrteBinaerzahl.length()) + (int) Math.pow(2, gesamtRunden - aktuelleRunde - 1);
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
		System.out.println(erweiterteListe);

		// Erstelle die Paarungen
		for (int i = 0; i < anzahlPaarungenRunde1 * 2; i += 2) {
			Optional<Wettkaempfer> wettkaempfer1 = Optional.ofNullable(erweiterteListe.get(i));
			Optional<Wettkaempfer> wettkaempfer2 = Optional.ofNullable(erweiterteListe.get(i + 1));
			paarungen.add(new ImmutablePair<>(wettkaempfer1, wettkaempfer2));
		}

		return paarungen;
	}

	// Erstelle ALLE Spiele für ein KO-Turnier
	public static List<Begegnung> erstelleBegegnungen(List<Wettkaempfer> teilnehmer, int gesamtAnzahlSpieler, int gesamtRunden, int letzteRunde, boolean bronzeFinale) {
		List<Begegnung> spieleListe = new ArrayList<>();

		System.out.println("gesamtAnzahlSpieler: " + gesamtAnzahlSpieler);
		System.out.println("gesamtRunden: " + gesamtRunden);
		System.out.println("letzteRunde:" + letzteRunde);

		List<Wettkaempfer> gemischteTeilnehmer =  mischeTeilnehmer(teilnehmer);

		// in der ersten Runde müssen eventuell Freilose erstellt werden, sodass im weiteren Turnierverlauf ein ausgeglichener Baum entsteht
		List<Pair<Optional<Wettkaempfer>, Optional<Wettkaempfer>>> paarungen = freilosMarkierung(gemischteTeilnehmer, gesamtRunden);

		// erste Gewinner-Runde zur Initialisierung der Spieler
		for (int akuellePaarungsId = 1; akuellePaarungsId <= Math.pow(2, gesamtRunden - 1); akuellePaarungsId++) {
			var begegnung = new Begegnung();
			begegnung.setTurnierUUID(UUID.randomUUID());
			begegnung.setBegegnungId(erstelleID(Begegnung.RundenTyp.GEWINNERRUNDE, 1, akuellePaarungsId));
			begegnung.setWettkaempfer1(paarungen.get(akuellePaarungsId-1).getLeft());
			begegnung.setWettkaempfer2(paarungen.get(akuellePaarungsId-1).getRight());
			spieleListe.add(begegnung);
		}

		// leere Gewinner-Runden
		for (int aktuelleRunde = 2; aktuelleRunde <= gesamtRunden; aktuelleRunde++) {
			for (int akuellePaarungsId = 1; akuellePaarungsId <= Math.pow(2, gesamtRunden - aktuelleRunde); akuellePaarungsId++) {
				spieleListe.add(leereBegegnung(erstelleID(Begegnung.RundenTyp.GEWINNERRUNDE, aktuelleRunde, akuellePaarungsId)));
			}
		}

		// leere Verlierer-Runden
		if (letzteRunde >= VERLIERER_RUNDE) {
			for (int aktuelleRunde = 1; aktuelleRunde <= 2 * gesamtRunden - 2; aktuelleRunde++) {
				// Anzahl der Spiele halbiert sich in jeder ungeraden Runde im Verlierer-Turnierbaum
				for (int akuellePaarungsId = 1; akuellePaarungsId <= Math.pow(2, gesamtRunden - 1 - Math.floorDiv(aktuelleRunde + 1, 2)); akuellePaarungsId++) {
					spieleListe.add(leereBegegnung(erstelleID(Begegnung.RundenTyp.TROSTRUNDE, aktuelleRunde, akuellePaarungsId)));
				}
			}
			spieleListe.add(leereBegegnung(erstelleID(Begegnung.RundenTyp.TROSTRUNDE, 2 * gesamtRunden - 1, 1))); // Großes Finale Spiel 1
		}
		if (bronzeFinale) {
			// Bronze-Finale, wenn letzteRunde === GEWINNER_RUNDE, sonst Großes Finale Spiel 2
			spieleListe.add(leereBegegnung(erstelleID(Begegnung.RundenTyp.TROSTRUNDE, letzteRunde == VERLIERER_RUNDE ? 2 * gesamtRunden : 1, 1)));
		}
		return spieleListe;
	}

	private static List<Wettkaempfer> mischeTeilnehmer(List<Wettkaempfer> teilnehmer) {
		// TODO
		return teilnehmer;
	}

	private static Begegnung leereBegegnung(Begegnung.BegegnungId begegnungId) {
		var begegnung = new Begegnung();
		begegnung.setTurnierUUID(UUID.randomUUID());
		begegnung.setBegegnungId(begegnungId);
		// leer
		begegnung.setWettkaempfer1(Optional.empty());
		begegnung.setWettkaempfer2(Optional.empty());
		return begegnung;
	}

	// ------------------------------------------------------------------
	// Fortschrittshilfen - instance context
	// ------------------------------------------------------------------

	// Finde das Spiel und die Position, zu der ein Gewinner "nach rechts" im aktuellen Rundentyp (Gewinner/Verliere) verschoben werden soll
	private Begegnung.BegegnungId verschiebeNachRechts(Begegnung.BegegnungId id, int gesamtRunden, int letzteRunde, boolean istLang, boolean downMix) {
		Begegnung.RundenTyp bracket = id.rundenTyp;
		int runde = id.runde;
		int aktuellePaarungsId = id.akuellePaarung;

		// Fälle, in denen der Fortschritt für Gewinner endet
		boolean istFinaleEinfach = (letzteRunde == GEWINNER_RUNDE && runde == gesamtRunden);
		boolean istFinaleDoppelt = (letzteRunde == VERLIERER_RUNDE && bracket == Begegnung.RundenTyp.TROSTRUNDE && runde == 2 * gesamtRunden);
		boolean istBronze = (letzteRunde == GEWINNER_RUNDE && bracket == Begegnung.RundenTyp.TROSTRUNDE);
		boolean istKurzesVRFinale = (bracket == Begegnung.RundenTyp.TROSTRUNDE && runde == 2 * gesamtRunden - 1 && !istLang);

		if (istFinaleEinfach || istFinaleDoppelt || istBronze || istKurzesVRFinale) {
			return null;
		}

		// Sonderfall: Gewinner der Gewinnerrunde verschiebt sich ins VR-GF S1
		if (letzteRunde >= VERLIERER_RUNDE && bracket == Begegnung.RundenTyp.GEWINNERRUNDE && runde == gesamtRunden) {
			return erstelleID(Begegnung.RundenTyp.TROSTRUNDE, 2 * gesamtRunden - 1, 1);
		}

		// für Verliererrunde-Positionierung
		int halbesSpiel = (bracket == Begegnung.RundenTyp.TROSTRUNDE && runde % 2 == 1) ? aktuellePaarungsId : (aktuellePaarungsId + 1) / 2;

		int position;
		if (bracket == Begegnung.RundenTyp.GEWINNERRUNDE) {
			position = (aktuellePaarungsId + 1) % 2; // normale GR-Progression
		}
		// VR-Progression
		else if (runde >= 2 * gesamtRunden - 2) {
			position = (runde + 1) % 2; // VR-Finalgewinner -> unten & GF(1) Außenseitergewinner -> oben
		} else if (runde == 1) {
			// außer downMix, LBR1-Gewinner bewegen sich invers zur normalen Progression
			position = downMix ? 1 : aktuellePaarungsId % 2;
		} else {
			// Gewinner aus VR immer unten in ungeraden Runden, sonst normale Progression
			position = runde % 2 == 1 ? 1 : (aktuellePaarungsId + 1) % 2;
		}

		// normale Progression
		return erstelleID(bracket, runde + 1, halbesSpiel);
	}

	// Finde das Spiel und die Position, zu der ein Verlierer "nach unten" im aktuellen Bracket verschoben werden soll
	private Begegnung.BegegnungId verschiebeWettkeampferInTrostrunde(Begegnung.BegegnungId id, int gesamtRunden, int letzteRunde, boolean istLang, boolean downMix) {
		Begegnung.RundenTyp bracket = id.rundenTyp;
		int runde = id.runde;
		int spiel = id.akuellePaarung;

		// K.-o.-Runden / spezielle Finals
		if (bracket.getValue() >= letzteRunde) { // größer als Fall ist für BF in langem Einzel-K.-o.-Turnier
			if (bracket == Begegnung.RundenTyp.GEWINNERRUNDE && istLang && runde == gesamtRunden - 1) {
				// wenn Bronze-Finale, verschiebe den Verlierer zu "LBR1" an der Spiegelposition von WBGF
				return erstelleID(Begegnung.RundenTyp.TROSTRUNDE, 1, 1);
			}
			if (bracket == Begegnung.RundenTyp.TROSTRUNDE && runde == 2 * gesamtRunden - 1 && istLang) {
				// wenn Doppel-Finale, dann verschiebe den Verlierer nach unten
				return erstelleID(Begegnung.RundenTyp.TROSTRUNDE, 2 * gesamtRunden, 1);
			}
			// sonst immer KO'd bei Verlierer >= letzte Runde
			return null;
		}

		// WBR1 wird immer in LBR1 gespeist, als ob es WBR2 wäre
		if (runde == 1) {
			return erstelleID(Begegnung.RundenTyp.TROSTRUNDE, 1, (spiel + 1) / 2);
		}

		if (downMix) {
			// immer oben einsetzen bei Downmixing
			return erstelleID(Begegnung.RundenTyp.TROSTRUNDE, (runde - 1) * 2, mischeVRSpiele(gesamtRunden, runde, spiel));
		}

		// normale VR-Abstiege: oben für (runde > 2) und (runde <= 2 wenn ungerades spiel) zur Anpassung an die Bracket-Bewegung
		int position = (runde > 2 || spiel % 2 == 1) ? 0 : 1;
		return erstelleID(Begegnung.RundenTyp.TROSTRUNDE, (runde - 1) * 2, spiel);
	}

	// gegeben eine Richtung (eines der oben genannten), verschiebe einen 'Aufsteiger' an diesen Ort
	public void setzeSieger(Begegnung.BegegnungId fortschritt, Wettkaempfer sieger, List<Begegnung> spieleListe) {
		if (fortschritt != null) {
			Begegnung zielBegegnung = findeBegegnung(fortschritt, spieleListe);
			if (zielBegegnung == null) {
				throw new IllegalArgumentException("Turnier fehlerhaft: " + fortschritt + " nicht gefunden!");
			}

			int position = fortschritt.akuellePaarung;
			if (zielBegegnung.getWettkaempfer1().isEmpty()) {
				zielBegegnung.setWettkaempfer1(Optional.of(sieger));
			}
			else if (zielBegegnung.getWettkaempfer2().isEmpty()) {
				zielBegegnung.setWettkaempfer2(Optional.of(sieger));
			}
			else {
				throw new IllegalArgumentException("Begegnung fehlerhaft: " + fortschritt + " hat bereits zwei Kämpfer!");
			}

//			if (zielBegegnung.wettkaempfer.get((position + 1) % 2) == FREILOS) {
//				zielBegegnung.ergebnis = position == 0 ? Arrays.asList(1, 0) : Arrays.asList(0, 1); // setze WO-Ergebnismarker
//				zielBegegnung.getWertungen().add(new Wertung(null, ))
//			}
		}
	}

	// Hilfsfunktion, um Matches mit Freilosen korrekt zu bewerten
//	public void bewerteFreilos(Begegnung.BegegnungId fortschritt, Begegnung begegnung) {
//		int index = begegnung.wettkaempfer.indexOf(FREILOS);
//		if (index >= 0) {
//			// setze die Ergebnisse manuell, um die `_verify` Freilos-Wertungseinschränkung zu vermeiden
//			begegnung.ergebnis = index == 0 ? Arrays.asList(0, 1) : Arrays.asList(1, 0);
//			setzeSieger(fortschritt, begegnung.wettkaempfer.get(index == 0 ? 1 : 0), Arrays.asList(begegnung));
//		}
//	}

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
	private Begegnung findeBegegnung(Begegnung.BegegnungId id, List<Begegnung> begegnungList) {
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

