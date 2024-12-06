package de.sinnix.judoturnier.application;

import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Begegnung;
import de.sinnix.judoturnier.model.BegegnungenJeRunde;
import de.sinnix.judoturnier.model.Runde;
import de.sinnix.judoturnier.model.Wettkaempfer;
import de.sinnix.judoturnier.model.WettkampfGruppe;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Sortierer {

	private static final Logger logger = LogManager.getLogger(Sortierer.class);

	private static final int PAUSEN             = 2;
	private static final int DEFAULT_MAX_RUNDEN = 100;

	private int mattenRunde;
	private int gruppe1Runde;
	private int gruppe2Runde;
	private int gruppe3Runde;
	private int rundeGesamt;

	public Sortierer(Integer rundeGesamt, Integer mattenRunde) {
		this.gruppe1Runde = 1;
		this.gruppe2Runde = 1;
		this.gruppe3Runde = 1;
		this.mattenRunde = mattenRunde;
		this.rundeGesamt = rundeGesamt;
	}

	/**
	 * Finde die beiden Paarungen vor dieser - falls diese exixtieren.
	 */
	public static List<Begegnung> vorgaengerBegegnungen(Begegnung.BegegnungId begegnungId, WettkampfGruppe wettkampfGruppe, List<Runde> alleRunden) {
		logger.info("Suche Vorgaenger zu Begegnung {}", begegnungId);
		var rundeTyp = begegnungId.rundenTyp;
		var paarungNr = begegnungId.akuellePaarung;
		var rundeNr = begegnungId.runde;
		var rudenDerWettkmapfgruppe = alleRunden.stream().filter(runde -> runde.gruppe().equals(wettkampfGruppe)).toList();

		if (rundeTyp.equals(Begegnung.RundenTyp.GEWINNERRUNDE) && rundeNr <= 1) {
			logger.warn("Für die erste Runde gibt es keine Vorgänger");
			return List.of();
		}

		var aktuelleBegegnung = findeBegegnungInGruppenRunden(begegnungId, rudenDerWettkmapfgruppe).orElseThrow(() -> new RuntimeException("Konnte die Begegnung nicht finden!"));
		logger.info("Aktuelle Begegnung: {} {} vs {}", aktuelleBegegnung.getId(), aktuelleBegegnung.getWettkaempfer1().isPresent() ? aktuelleBegegnung.getWettkaempfer1().get().name() : "/", aktuelleBegegnung.getWettkaempfer2().isPresent() ? aktuelleBegegnung.getWettkaempfer2().get().name() : "/");

		// Bilde die aktuelle paarungNr auf die vorherige Runde ab:

		var rundeNrNext = rundeNr - 1;
		int paarungNrPrev1 = (paarungNr - 1) * 2 + 1;
		int paarungNrPrev2 = paarungNrPrev1 + 1;
		List<Begegnung> result = new ArrayList<>();
		if (rundeTyp.equals(Begegnung.RundenTyp.GEWINNERRUNDE)) {
			var prevBegegnungSiegerrunde1 = findeBegegnungInGruppenRunden(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, rundeNrNext, paarungNrPrev1), rudenDerWettkmapfgruppe);
			var prevBegegnungSiegerrunde2 = findeBegegnungInGruppenRunden(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, rundeNrNext, paarungNrPrev2), rudenDerWettkmapfgruppe);
			if (prevBegegnungSiegerrunde1.isPresent()) {
				result.add(prevBegegnungSiegerrunde1.get());
			}
			if (prevBegegnungSiegerrunde2.isPresent()) {
				result.add(prevBegegnungSiegerrunde2.get());
			}
		}
		else if (rundeTyp.equals(Begegnung.RundenTyp.TROSTRUNDE)) {
			var prevBegegnungTrostrunde1 = findeBegegnungInGruppenRunden(new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, rundeNrNext, paarungNrPrev1), rudenDerWettkmapfgruppe);
			var prevBegegnungTrostrunde2 = findeBegegnungInGruppenRunden(new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, rundeNrNext, paarungNrPrev2), rudenDerWettkmapfgruppe);
			if (prevBegegnungTrostrunde1.isPresent()) {
				result.add(prevBegegnungTrostrunde1.get());
			}
			if (prevBegegnungTrostrunde2.isPresent()) {
				result.add(prevBegegnungTrostrunde2.get());
			}
		}
		return result;
	}

	/**
	 * Finde mögliche Paarungen nach dieser. Links: Gewinnerrunde, Rechts: Trostrunde
	 *
	 * .(Runde1, Siegerrunden, Paarung1)
	 * .Kämpfer1 vs Kämpfer2          ---------         (Runde2, Siegerrunde, Paarung1)
	 * .                                       |___________ Sieger1 vs Sieger2 ---------
	 * .(Runde1, Siegerrunden, Paarung2)       |                                        |
	 * .Kämpfer3 vs Kämpfer4          ---------                                         |          (Runde3, Siegerrunde, Paarung1)
	 * .                                                                                |------------ Sieger5 vs Sieger6
	 * .(Runde1, Siegerrunden, Paarung3)                                                |
	 * .Kämpfer5 vs /                 ---------         (Runde2, Siegerrunde, Paarung2) |
	 * .                                       |___________ Sieger3 vs Sieger4 ---------
	 * .(Runde1, Siegerrunden, Paarung4)       |
	 * .Kämpfer6 vs Kämpfer7          ---------
	 *
	 *
	 * @return Nachfolgende Begegnungen. Links: Gewinnerrunde, Rechts: Trostrunde
	 */
	public static Pair<Optional<Begegnung>,Optional<Begegnung>> nachfolgeBegegnungen(Begegnung.BegegnungId begegnungId, WettkampfGruppe wettkampfGruppe, List<Runde> alleRunden) {
		logger.info("Suche Nachfolger zu Begegnung {}", begegnungId);
		var rundeTyp = begegnungId.rundenTyp;
		var paarungNr = begegnungId.akuellePaarung;
		var rundeNr = begegnungId.runde;
		var rudenDerWettkmapfgruppe = alleRunden.stream().filter(runde -> runde.gruppe().equals(wettkampfGruppe)).toList();

		// Bilde die aktuelle paarungNr auf die kommende Runde ab:
		var rundeNrNext = rundeNr + 1;
		var paarungNrNext = (paarungNr + 1) / 2;
		var nextBegegnungSiegerrunde = findeBegegnungInGruppenRunden(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, rundeNrNext, paarungNrNext), rudenDerWettkmapfgruppe);
		var nextBegegnungTrostrunde = findeBegegnungInGruppenRunden(new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, rundeNrNext, paarungNrNext), rudenDerWettkmapfgruppe);

		logger.info("Nächste Begegnung Gewinnerrunde (left): {}", nextBegegnungSiegerrunde);
		logger.info("Nächste Begegnung Trostrunde (right): {}", nextBegegnungTrostrunde);
		return new ImmutablePair(nextBegegnungSiegerrunde, nextBegegnungTrostrunde);
	}

	private static Optional<Begegnung> findeBegegnungInGruppenRunden(Begegnung.BegegnungId begegnungId, List<Runde> rudenDerWettkmapfgruppe) {
		return rudenDerWettkmapfgruppe.stream()
			.flatMap(runde -> runde.begegnungen().stream().filter(b -> b.getBegegnungId().equals(begegnungId)))
			.findFirst();
	}

	/**
	 * Es wird die erste Runde aller Gruppen gekämpft, danach die zweite Runde aller Gruppen usw.
	 */
	public Pair<Integer, List<Runde>> erstelleReihenfolgeMitAllenGruppenJeDurchgang(List<WettkampfGruppe> gruppen, Integer matteId) {
		logger.info("erstelle Reihenfolge -mit allen Gruppen je Durchgang- für Matte {}...", matteId);
		List<Runde> runden = new ArrayList<>();
		int gruppenRunde = 1;
		for (int rundenNummer = 0; rundenNummer < DEFAULT_MAX_RUNDEN; rundenNummer++) {
			for (int gruppenNr = 0; gruppenNr < gruppen.size(); gruppenNr++) {
				WettkampfGruppe gruppe = gruppen.get(gruppenNr);

				if (gruppe.alleRundenBegegnungen().size() - 1 < rundenNummer) {
					// wir sind hier fertig
					break;
				}

				// hole Altersklasse der Gruppe
				Altersklasse altersKlasse = gruppe.altersklasse();

				logger.debug("Gruppe: {}", gruppe.name());
				Runde runde = new Runde(null, mattenRunde, gruppenRunde, rundeGesamt, matteId, altersKlasse, gruppe, gruppe.alleRundenBegegnungen().get(rundenNummer).begegnungenJeRunde());
				runden.add(runde);
				rundeGesamt++;
				mattenRunde++;
				gruppenRunde++;
			}
		}
		return new ImmutablePair(rundeGesamt, runden);
	}

	/**
	 * Es werden immer zwei Gruppen abwechselnd kämpfen, bis in diesen Gruppen alle Runden gekämpft sind, danach kommen die nächsten beiden Gruppen an die Reihe.
	 * Bei ungerader Anzahl wechseln sich die letzten drei Gruppen ab, davor gilt weiterhin, dass immer zwei Gruppen abwechselnd an der Reihe sind.
	 */
	public Pair<Integer, List<Runde>> erstelleReihenfolgeMitAbwechselndenGruppen(List<WettkampfGruppe> gruppen, Integer matteId) {
		logger.info("erstelle Reihenfolge -mit abwechselnden Gruppen- für Matte {}...", matteId);
		List<Runde> runden = new ArrayList<>();
		// gerade Anzahl an Gruppen -> 2 Gruppen je Matte
		if (gruppen.size() % 2 == 0) {
			logger.info("Gerade Anzahl an Gruppen ({})", gruppen.size());
			var result = gruppiereAbwechselndPaare(gruppen, matteId);
			runden.addAll(result);
		}
		// ungerade Anzahl an Gruppen -> 2 Gruppen je Matte und einmal 3 Gruppen je Matte
		else {
			logger.info("Ungerade Anzahl an Gruppen ({})", gruppen.size());
			if (gruppen.size() > 1) {
				logger.info("Wir haben mehr als 1 Gruppe, also splitten wir");
				// behandle die letzten 3 Gruppen separat und gruppiere zuerst die anderen Gruppen
				List<WettkampfGruppe> letztenDreiGruppen = gruppen.subList(gruppen.size() - 3, gruppen.size());
				List<WettkampfGruppe> andereGruppen = gruppen.subList(0, gruppen.size() - 3);
				var result = gruppiereAbwechselndPaare(andereGruppen, matteId);
				runden.addAll(result);

				// jetzt die letzten drei Gruppen
				result = gruppiereAbwechselndTrios(letztenDreiGruppen, matteId);
				runden.addAll(result);
			} else {
				logger.info("Es existiert nur eine Gruppe, daher fügen wir diese komplett hinzu");
				WettkampfGruppe gruppeZuletzt = gruppen.get(gruppen.size() - 1);
				logger.debug("Gruppe {}", gruppeZuletzt);
				int gruppenRunde = 1;
				for (BegegnungenJeRunde begegnungen : gruppeZuletzt.alleRundenBegegnungen()) {
					Altersklasse altersKlasseZuletzt = gruppeZuletzt.altersklasse();
					Runde rundeZuletzt = new Runde(null, mattenRunde, gruppenRunde, rundeGesamt, matteId, altersKlasseZuletzt, gruppeZuletzt, begegnungen.begegnungenJeRunde());
					runden.add(rundeZuletzt);
					rundeGesamt++;
					mattenRunde++;
					gruppenRunde++;
				}
			}
		}
		return new ImmutablePair(rundeGesamt, runden);
	}

	private List<Runde> gruppiereAbwechselndPaare(List<WettkampfGruppe> gruppen, Integer matteId) {
		List<Runde> runden = new ArrayList<>();
		for (int gruppenNr = 0; gruppenNr < gruppen.size(); gruppenNr += 2) {
			WettkampfGruppe gruppe1 = gruppen.get(gruppenNr);
			WettkampfGruppe gruppe2 = gruppen.get(gruppenNr + 1);
			Altersklasse altersKlasse1 = gruppe1.altersklasse();
			Altersklasse altersKlasse2 = gruppe2.altersklasse();

			// Abwechselnd die Begegnungen der gruppe1 und gruppe2 nehmen und der Matte hinzufügen
			int maxAnzahlBegegnungen = Math.max(gruppe1.alleRundenBegegnungen().size(), gruppe2.alleRundenBegegnungen().size());
			for (int g = 0; g < maxAnzahlBegegnungen; g++) {
				if (gruppe1.alleRundenBegegnungen().size() > g) {
					Runde runde1 = new Runde(null, mattenRunde, gruppe1Runde, rundeGesamt, matteId, altersKlasse1, gruppe1, gruppe1.alleRundenBegegnungen().get(g).begegnungenJeRunde());
					runden.add(runde1);
					gruppe1Runde++;
					rundeGesamt++;
					mattenRunde++;
				} else {
					// Gruppe 1 hat keine Teilnehmer mehr, wir fügen daher einen Dummy (Pause) ein
					logger.info("Gruppe 1 (von 2) ist leer, füge Dummy ein");
					runden.add(dummyRunde(altersKlasse1, gruppe1, gruppe1Runde));
					gruppe1Runde++;
					rundeGesamt++;
					mattenRunde++;
				}

				if (gruppe2.alleRundenBegegnungen().size() > g) {
					Runde runde2 = new Runde(null, mattenRunde, gruppe2Runde, rundeGesamt, matteId, altersKlasse2, gruppe2, gruppe2.alleRundenBegegnungen().get(g).begegnungenJeRunde());
					runden.add(runde2);
					gruppe2Runde++;
					rundeGesamt++;
					mattenRunde++;
				} else {
					// Gruppe 2 hat keine Teilnehmer mehr, wir fügen daher einen Dummy (Pause) ein - es sei denn, dass dies die letzte Runde wäre, dann ist eine Pause am Ende unnötig
					if (g == maxAnzahlBegegnungen - 1) {
						logger.info("Gruppe 2 (von 2) ist leer, aber wir sind fertig und stoppen hier");
						break;
					}
					logger.info("Gruppe 2 (von 2) ist leer, füge Dummy ein");
					runden.add(dummyRunde(altersKlasse2, gruppe2, gruppe2Runde));
					gruppe2Runde++;
					rundeGesamt++;
					mattenRunde++;
				}


			}
		}
		return runden;
	}

	private Runde dummyRunde(Altersklasse altersKlasse, WettkampfGruppe gruppe, Integer gruppenRunde) {
		logger.info("erstelle Pause");
		Begegnung pausenBegegnung = new Begegnung();
		pausenBegegnung.setBegegnungId(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, mattenRunde, gruppenRunde));
		pausenBegegnung.setWettkaempfer1(Optional.empty());
		pausenBegegnung.setWettkaempfer2(Optional.empty());
		pausenBegegnung.setTurnierUUID(gruppe.turnierUUID());
		return new Runde(null, mattenRunde, gruppenRunde, rundeGesamt, null, altersKlasse, gruppe, List.of(pausenBegegnung));
	}

	private List<Runde> gruppiereAbwechselndTrios(List<WettkampfGruppe> gruppen, Integer matteId) {
		logger.debug("gruppiere Matte {}", matteId);
		List<Runde> runden = new ArrayList<>();
		WettkampfGruppe gruppe1 = gruppen.get(0);
		WettkampfGruppe gruppe2 = gruppen.get(1);
		WettkampfGruppe gruppe3 = gruppen.get(2);
		Altersklasse altersKlasse1 = gruppe1.altersklasse();
		Altersklasse altersKlasse2 = gruppe2.altersklasse();
		Altersklasse altersKlasse3 = gruppe3.altersklasse();

		// Abwechselnd die Begegnungen der gruppe1 und gruppe2 nehmen und der Matte hinzufügen
		for (int r = 0; r < Math.max(gruppe1.alleRundenBegegnungen().size(), Math.max(gruppe2.alleRundenBegegnungen().size(), gruppe3.alleRundenBegegnungen().size())); r++) {
			if (gruppe1.alleRundenBegegnungen().size() > r) {
				Runde runde1 = new Runde(null, mattenRunde, gruppe1Runde, rundeGesamt, matteId, altersKlasse1, gruppe1, gruppe1.alleRundenBegegnungen().get(r).begegnungenJeRunde());
				runden.add(runde1);
				gruppe1Runde++;
				rundeGesamt++;
				mattenRunde++;
			} else {
				// Gruppe 1 hat keine Teilnehmer mehr, wir fügen daher einen Dummy (zB Pause) ein
				logger.info("Gruppe 1 (von 3) ist leer, füge Dummy ein");
			}

			if (gruppe2.alleRundenBegegnungen().size() > r) {
				Runde runde2 = new Runde(null, mattenRunde, gruppe2Runde, rundeGesamt, matteId, altersKlasse2, gruppe2, gruppe2.alleRundenBegegnungen().get(r).begegnungenJeRunde());
				runden.add(runde2);
				gruppe2Runde++;
				rundeGesamt++;
				mattenRunde++;
			} else {
				// Gruppe 2 hat keine Teilnehmer mehr, wir fügen daher einen Dummy (zB Pause) ein
				logger.info("Gruppe 2 (von 3) ist leer, füge Dummy ein");
			}

			if (gruppe3.alleRundenBegegnungen().size() > r) {
				Runde runde3 = new Runde(null, mattenRunde, gruppe3Runde, rundeGesamt, matteId, altersKlasse3, gruppe3, gruppe3.alleRundenBegegnungen().get(r).begegnungenJeRunde());
				runden.add(runde3);
				gruppe3Runde++;
				rundeGesamt++;
				mattenRunde++;
			} else {
				// Gruppe 3 hat keine Teilnehmer mehr, wir fügen daher einen Dummy (zB Pause) ein
				logger.info("Gruppe 3 (von 3) ist leer, füge Dummy ein");
			}
		}
		return runden;
	}

	public List<Begegnung> sortiereBegegnungen(List<Begegnung> begegnungen) {
		List<Begegnung> sortedBegegnungen = new ArrayList<>(begegnungen);

		for (int i = 0; i < sortedBegegnungen.size() - 2; i++) {
			for (int j = 0; j < sortedBegegnungen.size() - i - 2; j++) {
				Wettkaempfer wettkaempfer1Jetzt = sortedBegegnungen.get(j).getWettkaempfer1().get();
				Wettkaempfer wettkaempfer2Jetzt = sortedBegegnungen.get(j).getWettkaempfer2().get();
				Wettkaempfer wettkaempfer1Danach = sortedBegegnungen.get(j + 1).getWettkaempfer1().get();
				Wettkaempfer wettkaempfer2Danach = sortedBegegnungen.get(j + 1).getWettkaempfer2().get();

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
				Wettkaempfer teilnehmerA1 = sortedBegegnungen.get(j).getWettkaempfer1().get();
				Wettkaempfer teilnehmerA2 = sortedBegegnungen.get(j).getWettkaempfer2().get();
				Wettkaempfer teilnehmerB1 = sortedBegegnungen.get(j + 1).getWettkaempfer1().get();
				Wettkaempfer teilnehmerB2 = sortedBegegnungen.get(j + 1).getWettkaempfer2().get();

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
