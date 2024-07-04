package de.sinnix.judoturnier.application;

import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Begegnung;
import de.sinnix.judoturnier.model.Runde;
import de.sinnix.judoturnier.model.Wettkaempfer;
import de.sinnix.judoturnier.model.WettkampfGruppe;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class Sortierer {

	private static final Logger logger = LogManager.getLogger(Sortierer.class);

	private static final int PAUSEN             = 2;
	private static final int DEFAULT_MAX_RUNDEN = 100;

	/**
	 * Es werd die erste Runde aller Gruppen gekämpft, danach die zweite Runde aller Gruppen usw.
	 */
	public List<Runde> erstelleReihenfolgeMitAllenGruppenJeDurchgang(List<WettkampfGruppe> gruppen) {
		logger.info("erstelleReihenfolgeMitAllenGruppenJeDurchgang...");
		List<Runde> runden = new ArrayList<>();
		int maxRundenNummer = DEFAULT_MAX_RUNDEN; // TODO: mach das konfigurierbar?
		for (int rundenNummer = 0; rundenNummer < maxRundenNummer; rundenNummer++) {
			for (int gruppenNr = 0; gruppenNr < gruppen.size(); gruppenNr++) {
				WettkampfGruppe gruppe = gruppen.get(gruppenNr);
				Runde runde = kopiereRunde(rundenNummer, gruppe);
				if (runde != null) {
					// die Gruppe hat eine entsprechende Runde
					runden.add(runde);
				}
			}
		}
		return runden;
	}

	private Runde kopiereRunde(int rundenNummer, WettkampfGruppe wettkampfGruppe) {
		if (wettkampfGruppe.alleGruppenBegegnungen().size() <= rundenNummer) return null;

		int mattenRunde = rundenNummer + 1;
		Altersklasse altersKlasse = wettkampfGruppe.alleGruppenBegegnungen().get(rundenNummer).get(0).getWettkaempfer1().altersklasse();
		return new Runde(rundenNummer, mattenRunde, 0, null, null, altersKlasse, wettkampfGruppe, wettkampfGruppe.alleGruppenBegegnungen().get(rundenNummer));
	}

	/**
	 * Es werden immer zwei Gruppen abwechselnd kämpfen, bis in diesen Gruppen alle Runden gekämpft sind, danach kommen die nächsten beiden Gruppen an die Reihe.
	 * Bei ungerader Anzahl wechseln sich die letzten drei Gruppen ab, davor gilt weiterhin, dass immer zwei Gruppen abwechselnd an der Reihe sind.
	 */
	public List<Runde> erstelleReihenfolgeMitAbwechselndenGruppen(List<WettkampfGruppe> gruppen) {
		logger.info("erstelleReihenfolgeMitAbwechselndenGruppen...");
		int rundenNummer = 0;
		List<Runde> runden = new ArrayList<>();
		// gerade Anzahl an Gruppen -> 2 Gruppen je Matte
		if (gruppen.size() % 2 == 0) {
			logger.info("Berechne gerade Anzahl an Gruppen (" + gruppen.size() + ")");
			var result = gruppiereAbwechselndPaare(gruppen, rundenNummer);
			rundenNummer = result.getLeft();
			runden.addAll(result.getRight());
		}
		// ungerade Anzahl an Gruppen -> 2 Gruppen je Matte und einmal 3 Gruppen je Matte
		else {
			logger.info("Berechne ungerade Anzahl an Gruppen");
			if (gruppen.size() > 1) {
				logger.info("Wir haben mehr als 1 Gruppe, also splitten wir");
				// behandle die letzten 3 Gruppen separat und gruppiere zuerst die anderen Gruppen
				List<WettkampfGruppe> letztenDreiGruppen = gruppen.subList(gruppen.size() - 3, gruppen.size());
				List<WettkampfGruppe> andereGruppen = gruppen.subList(0, gruppen.size() - 3);
				var result = gruppiereAbwechselndPaare(andereGruppen, rundenNummer);
				rundenNummer = result.getLeft();
				runden.addAll(result.getRight());

				// jetzt die letzten drei Gruppen
				result = gruppiereAbwechselndTrios(letztenDreiGruppen, rundenNummer);
				rundenNummer = result.getLeft();
				runden.addAll(result.getRight());
			} else {
				logger.info("Es existiert nur eine Gruppe, daher fügen wir diese komplett hinzu");
				WettkampfGruppe gruppeZuletzt = gruppen.get(gruppen.size() - 1);
				int gruppeRunde = 0;
				for (List<Begegnung> begegnungen : gruppeZuletzt.alleGruppenBegegnungen()) {
					Altersklasse altersKlasseZuletzt = begegnungen.get(0).getWettkaempfer1().altersklasse();
					gruppeRunde++;
					int mattenRunde = rundenNummer + 1;
					Runde rundeZuletzt = new Runde(rundenNummer, mattenRunde, gruppeRunde, null, null, altersKlasseZuletzt, gruppeZuletzt, begegnungen);
					runden.add(rundeZuletzt);
					rundenNummer++;
				}
			}
		}
		return runden;
	}

	private Pair<Integer, List<Runde>> gruppiereAbwechselndPaare(List<WettkampfGruppe> gruppen, int rundenNummer) {
		List<Runde> runden = new ArrayList<>();
		int resultRundenNummer = rundenNummer;
		for (int gruppenNr = 0; gruppenNr < gruppen.size(); gruppenNr += 2) {
			WettkampfGruppe gruppe1 = gruppen.get(gruppenNr);
			WettkampfGruppe gruppe2 = gruppen.get(gruppenNr + 1);
			Altersklasse altersKlasse1 = gruppe1.alleGruppenBegegnungen().get(0).get(0).getWettkaempfer1().altersklasse();
			Altersklasse altersKlasse2 = gruppe2.alleGruppenBegegnungen().get(0).get(0).getWettkaempfer1().altersklasse();

			int gruppe1Runde = 0;
			int gruppe2Runde = 0;
			// Abwechselnd die Begegnungen der gruppe1 und gruppe2 nehmen und der Matte hinzufügen
			int maxAnzahlBegegnungen = Math.max(gruppe1.alleGruppenBegegnungen().size(), gruppe2.alleGruppenBegegnungen().size());
			for (int r = 0; r < maxAnzahlBegegnungen; r++) {
				if (gruppe1.alleGruppenBegegnungen().size() > r) {
					gruppe1Runde++;
					int mattenRunde = resultRundenNummer + 1;
					Runde runde1 = new Runde(resultRundenNummer, mattenRunde, gruppe1Runde, null, null, altersKlasse1, gruppe1, gruppe1.alleGruppenBegegnungen().get(r));
					runden.add(runde1);
					resultRundenNummer++;
				} else {
					// Gruppe 1 hat keine Teilnehmer mehr, wir fügen daher einen Dummy (Pause) ein
					logger.info("Gruppe 1 (von 2) ist leer, füge Dummy ein");
					gruppe1Runde++;
					int mattenRunde = resultRundenNummer + 1;
					runden.add(dummyRunde(resultRundenNummer, mattenRunde, gruppe1Runde, altersKlasse1, gruppe1));
					resultRundenNummer++;
				}

				if (gruppe2.alleGruppenBegegnungen().size() > r) {
					gruppe2Runde++;
					int mattenRunde = resultRundenNummer + 1;
					Runde runde2 = new Runde(resultRundenNummer, mattenRunde, gruppe2Runde, null, null, altersKlasse2, gruppe2, gruppe2.alleGruppenBegegnungen().get(r));
					runden.add(runde2);
					resultRundenNummer++;
				} else {
					// Gruppe 2 hat keine Teilnehmer mehr, wir fügen daher einen Dummy (Pause) ein - es sei denn, dass dies die letzte Runde wäre, dann ist eine Pause am Ende unnötig
					if (r == maxAnzahlBegegnungen - 1) {
						logger.info("Gruppe 2 (von 2) ist leer, aber wir sind fertig und stoppen hier");
						break;
					}
					logger.info("Gruppe 2 (von 2) ist leer, füge Dummy ein");
					gruppe2Runde++;
					int rundenName = resultRundenNummer + 1;
					runden.add(dummyRunde(resultRundenNummer, rundenName, gruppe2Runde, altersKlasse2, gruppe2));
					resultRundenNummer++;
				}
			}
		}
		return new ImmutablePair<>(resultRundenNummer, runden);
	}

	private Runde dummyRunde(int resultRundenNummer, int mattenRunde, int gruppenRunde, Altersklasse altersKlasse, WettkampfGruppe gruppe) {
		logger.info("erstelle Pause");
		Begegnung pausenBegegnung = new Begegnung();
		return new Runde(resultRundenNummer, mattenRunde, gruppenRunde, null, null, altersKlasse, gruppe, List.of(pausenBegegnung));
	}

	private Pair<Integer, List<Runde>> gruppiereAbwechselndTrios(List<WettkampfGruppe> gruppen, int rundenNummer) {
		List<Runde> runden = new ArrayList<>();
		WettkampfGruppe gruppe1 = gruppen.get(0);
		WettkampfGruppe gruppe2 = gruppen.get(1);
		WettkampfGruppe gruppe3 = gruppen.get(2);
		Altersklasse altersKlasse1 = gruppe1.alleGruppenBegegnungen().get(0).get(0).getWettkaempfer1().altersklasse();
		Altersklasse altersKlasse2 = gruppe2.alleGruppenBegegnungen().get(0).get(0).getWettkaempfer1().altersklasse();
		Altersklasse altersKlasse3 = gruppe3.alleGruppenBegegnungen().get(0).get(0).getWettkaempfer1().altersklasse();

		int resultRundenNummer = rundenNummer;
		int gruppe1Runde = 0;
		int gruppe2Runde = 0;
		int gruppe3Runde = 0;

		// Abwechselnd die Begegnungen der gruppe1 und gruppe2 nehmen und der Matte hinzufügen
		for (int r = 0; r < Math.max(gruppe1.alleGruppenBegegnungen().size(), Math.max(gruppe2.alleGruppenBegegnungen().size(), gruppe3.alleGruppenBegegnungen().size())); r++) {
			if (gruppe1.alleGruppenBegegnungen().size() > r) {
				gruppe1Runde++;
				int mattenRunde = resultRundenNummer + 1;
				Runde runde1 = new Runde(resultRundenNummer, mattenRunde, gruppe1Runde, null, null, altersKlasse1, gruppe1, gruppe1.alleGruppenBegegnungen().get(r));
				runden.add(runde1);
				resultRundenNummer++;
			} else {
				// Gruppe 1 hat keine Teilnehmer mehr, wir fügen daher einen Dummy (zB Pause) ein
				logger.info("Gruppe 1 (von 3) ist leer, füge Dummy ein");
			}

			if (gruppe2.alleGruppenBegegnungen().size() > r) {
				gruppe2Runde++;
				int mattenRunde = resultRundenNummer + 1;
				Runde runde2 = new Runde(resultRundenNummer, mattenRunde, gruppe2Runde, null, null, altersKlasse2, gruppe2, gruppe2.alleGruppenBegegnungen().get(r));
				runden.add(runde2);
				resultRundenNummer++;
			} else {
				// Gruppe 2 hat keine Teilnehmer mehr, wir fügen daher einen Dummy (zB Pause) ein
				logger.info("Gruppe 2 (von 3) ist leer, füge Dummy ein");
			}

			if (gruppe3.alleGruppenBegegnungen().size() > r) {
				gruppe3Runde++;
				int mattenRunde = resultRundenNummer + 1;
				Runde runde3 = new Runde(resultRundenNummer, mattenRunde, gruppe3Runde, null, null, altersKlasse3, gruppe3, gruppe3.alleGruppenBegegnungen().get(r));
				runden.add(runde3);
				resultRundenNummer++;
			} else {
				// Gruppe 3 hat keine Teilnehmer mehr, wir fügen daher einen Dummy (zB Pause) ein
				logger.info("Gruppe 3 (von 3) ist leer, füge Dummy ein");
			}
		}
		return new ImmutablePair<>(resultRundenNummer, runden);
	}

	public List<Begegnung> sortiereBegegnungen(List<Begegnung> begegnungen) {
		List<Begegnung> sortedBegegnungen = new ArrayList<>(begegnungen);

		for (int i = 0; i < sortedBegegnungen.size() - 2; i++) {
			for (int j = 0; j < sortedBegegnungen.size() - i - 2; j++) {
				Wettkaempfer wettkaempfer1Jetzt = sortedBegegnungen.get(j).getWettkaempfer1();
				Wettkaempfer wettkaempfer2Jetzt = sortedBegegnungen.get(j).getWettkaempfer2();
				Wettkaempfer wettkaempfer1Danach = sortedBegegnungen.get(j + 1).getWettkaempfer1();
				Wettkaempfer wettkaempfer2Danach = sortedBegegnungen.get(j + 1).getWettkaempfer2();

				if (wettkaempfer1Jetzt.equals(wettkaempfer1Danach) || wettkaempfer1Jetzt.equals(wettkaempfer2Danach) ||
					wettkaempfer2Jetzt.equals(wettkaempfer1Danach) || wettkaempfer2Jetzt.equals(wettkaempfer2Danach)) {
					// Wenn Teilnehmer in aufeinanderfolgenden Begegnungen wiederholt auftreten,
					// tausche die Begegnungen
					tauscheBegegnungen(j + 1, j + 2, sortedBegegnungen);
				}
			}
		}

		return sortedBegegnungen;
	}

	public List<Begegnung> sortiereBegegnungen3(List<Begegnung> begegnungen) {
		List<Begegnung> sortedBegegnungen = new ArrayList<>(begegnungen);
		int consecutiveCount = 1;

		for (int i = 0; i < sortedBegegnungen.size() - 1; i++) {
			for (int j = 0; j < sortedBegegnungen.size() - i - 1; j++) {
				Wettkaempfer teilnehmerA1 = sortedBegegnungen.get(j).getWettkaempfer1();
				Wettkaempfer teilnehmerA2 = sortedBegegnungen.get(j).getWettkaempfer2();
				Wettkaempfer teilnehmerB1 = sortedBegegnungen.get(j + 1).getWettkaempfer1();
				Wettkaempfer teilnehmerB2 = sortedBegegnungen.get(j + 1).getWettkaempfer2();

				if (istTeilnehmerInAufeinanderfolgendenBegegnungen(teilnehmerA1, sortedBegegnungen) ||
					istTeilnehmerInAufeinanderfolgendenBegegnungen(teilnehmerA2, sortedBegegnungen)) {
					tauscheBegegnungen(j, j + 1, sortedBegegnungen);
					consecutiveCount = 1;
				} else if ((teilnehmerA1.equals(teilnehmerB1) || teilnehmerA1.equals(teilnehmerB2)) &&
					(teilnehmerA2.equals(teilnehmerB1) || teilnehmerA2.equals(teilnehmerB2))) {
					consecutiveCount++;
				} else {
					consecutiveCount = 1;
				}

				if (consecutiveCount == 3) {
					tauscheBegegnungen(j + 1, j + 2, sortedBegegnungen);
					consecutiveCount = 1;
				}
			}
		}

		return sortedBegegnungen;
	}

	private boolean istTeilnehmerInAufeinanderfolgendenBegegnungen(Wettkaempfer teilnehmer, List<Begegnung> sortedBegegnungen) {
		for (int i = 0; i < sortedBegegnungen.size() - 2; i++) {
			if (sortedBegegnungen.get(i).getWettkaempfer1().equals(teilnehmer) ||
				sortedBegegnungen.get(i).getWettkaempfer2().equals(teilnehmer) ||
				sortedBegegnungen.get(i + 1).getWettkaempfer1().equals(teilnehmer) ||
				sortedBegegnungen.get(i + 1).getWettkaempfer2().equals(teilnehmer) ||
				sortedBegegnungen.get(i + 2).getWettkaempfer1().equals(teilnehmer) ||
				sortedBegegnungen.get(i + 2).getWettkaempfer2().equals(teilnehmer)) {
				return true;
			}
		}
		return false;
	}

	private void tauscheBegegnungen(int indexA, int indexB, List<Begegnung> sortedBegegnungen) {
		Collections.swap(sortedBegegnungen, indexA, indexB);
	}
}
