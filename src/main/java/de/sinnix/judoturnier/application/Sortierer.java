package de.sinnix.judoturnier.application;

import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Begegnung;
import de.sinnix.judoturnier.model.BegegnungenJeRunde;
import de.sinnix.judoturnier.model.Runde;
import de.sinnix.judoturnier.model.Wettkaempfer;
import de.sinnix.judoturnier.model.WettkampfGruppe;
import de.sinnix.judoturnier.model.WettkampfGruppeMitBegegnungen;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


/**
 * Sortiert die Begegnungen in der richtigen Reihenfolge.
 *
 * - Siegerrunde
 * - Runde1
 * Kämpfer1 ---------
 *                 1 |---------
 * Kämpfer2 ---------          |
 *                             |
 *                           7 |--------- Sieger1 vs Sieger2 ------
 * Kämpfer3 ---------          |                                   |
 *                 2 |---------                                    |
 * Kämpfer4 ---------                                              |
 *                                                                 |
 *                                                              11 |------ 1. Platz / 2. Platz
 * Kämpfer3 ---------                                              |
 *                 3 |---------                                    |
 * Kämpfer4 ---------          |                                   |
 *                             |                                   |
 *                           8 |--------- Sieger3 vs Sieger4 ------
 * Kämpfer3 ---------          |
 *                 4 |---------
 * Kämpfer4 ---------
 *
 *
 *
 * - Trostrunde
 * - Runde2
 *            Verlierer aus 1 ---
 *                             5 |---------------
 *            Verlierer aus 2 ---                |
 *                                             9 |------------- 3. Platz
 *                         Verlierer aus 7 ------
 *
 *
 *            Verlierer aus 3 ---
 *               vs            6 |---------------
 *            Verlierer aus 4 ---                |
 *                                            10 |------------- 3. Platz
 *                         Verlierer aus 8 ------
 *
 * @return Nachfolgende Begegnungen. Links: Gewinnerrunde, Rechts: Trostrunde
 */
public class Sortierer {

	private static final Logger logger = LogManager.getLogger(Sortierer.class);

	private static final int PAUSEN             = 2;
	private static final int DEFAULT_MAX_RUNDEN = 100;

	private int mattenRunde;
	private int rundeGesamt;

	private record GeplanteRunde(int gruppenRunde, List<Begegnung> begegnungen) {
	}

	public record TrostBelegung(Begegnung begegnung, Wettkaempfer wettkaempfer) {
	}

	public record NachfolgeBelegung(Optional<Begegnung> gewinnerBegegnung, List<TrostBelegung> trostBelegungen) {
	}

	public Sortierer(Integer rundeGesamt, Integer mattenRunde) {
		this.mattenRunde = mattenRunde;
		this.rundeGesamt = rundeGesamt;
	}

	/**
	 * Finde die beiden Paarungen vor dieser - falls diese exixtieren.
	 */
	public static List<Begegnung> vorgaengerBegegnungen(Begegnung.BegegnungId begegnungId, WettkampfGruppe wettkampfGruppe, List<Runde> alleRunden) {
		logger.info("Suche Vorgaenger zu Begegnung {}", begegnungId);
		var rundeTyp = begegnungId.rundenTyp;
		var paarungNr = begegnungId.paarungNummer;
		var rundeNr = begegnungId.rundenNummerDesTyps;
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
	 * @return Nachfolgende Begegnungen. Links: Gewinnerrunde, Rechts: Trostrunde
	 */
	public static Pair<Optional<Begegnung>,Optional<Begegnung>> nachfolgeBegegnungen(Begegnung.BegegnungId begegnungId, WettkampfGruppe wettkampfGruppe, List<Runde> alleRunden) {
		logger.info("Suche Nachfolger zu Begegnung {}", begegnungId);
		var paarungNr = begegnungId.paarungNummer;
		var rundenNummerDesTyps = begegnungId.rundenNummerDesTyps;
		var rudenDerWettkmapfgruppe = alleRunden.stream().filter(runde -> runde.gruppe().equals(wettkampfGruppe)).toList();

		// Bilde die aktuelle paarungNr auf die kommende Runde ab:
		var nextRundenNummerDesTyps = rundenNummerDesTyps + 1;
		var nextPaarungNr = (paarungNr + 1) / 2;
		var nextBegegnungSiegerrunde = findeBegegnungInGruppenRunden(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, nextRundenNummerDesTyps, nextPaarungNr), rudenDerWettkmapfgruppe);
		var nextBegegnungTrostrunde = findeBegegnungInGruppenRunden(new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, nextRundenNummerDesTyps, nextPaarungNr), rudenDerWettkmapfgruppe);

		logger.info("Nächste Begegnung Gewinnerrunde (left): {}", nextBegegnungSiegerrunde);
		logger.info("Nächste Begegnung Trostrunde (right): {}", nextBegegnungTrostrunde);
		return new ImmutablePair(nextBegegnungSiegerrunde, nextBegegnungTrostrunde);
	}

	public static NachfolgeBelegung nachfolgeBelegungen(Begegnung begegnung, Wettkaempfer sieger, WettkampfGruppe wettkampfGruppe, List<Runde> alleRunden) {
		var rundenDerWettkampfgruppe = alleRunden.stream().filter(runde -> runde.gruppe().equals(wettkampfGruppe)).toList();
		if (istKoSystemMitDoppelterTrostrunde(rundenDerWettkampfgruppe)) {
			return nachfolgeBelegungenKoSystemMitDoppelterTrostrunde(begegnung, sieger, rundenDerWettkampfgruppe);
		}

		Pair<Optional<Begegnung>, Optional<Begegnung>> nachfolger = nachfolgeBegegnungen(begegnung.getBegegnungId(), wettkampfGruppe, alleRunden);
		List<TrostBelegung> trostBelegungen = new ArrayList<>();
		Optional<Wettkaempfer> verlierer = findeVerlierer(begegnung, sieger);
		if (nachfolger.getRight().isPresent() && verlierer.isPresent()) {
			trostBelegungen.add(new TrostBelegung(nachfolger.getRight().get(), verlierer.get()));
		}
		return new NachfolgeBelegung(nachfolger.getLeft(), trostBelegungen);
	}

	private static NachfolgeBelegung nachfolgeBelegungenKoSystemMitDoppelterTrostrunde(Begegnung begegnung, Wettkaempfer sieger, List<Runde> rundenDerWettkampfgruppe) {
		Begegnung.BegegnungId begegnungId = begegnung.getBegegnungId();
		int gewinnerRunden = maxRunde(Begegnung.RundenTyp.GEWINNERRUNDE, rundenDerWettkampfgruppe);
		int viertelFinalRunde = gewinnerRunden - 2;
		int viertelTrostEnde = viertelFinalRunde + viertelFinalRunde - 2;
		int haelftenTrostRunde = viertelTrostEnde + 1;
		int bronzeRunde = haelftenTrostRunde + 1;

		Optional<Begegnung> gewinnerBegegnung = Optional.empty();
		List<TrostBelegung> trostBelegungen = new ArrayList<>();

		if (begegnungId.rundenTyp == Begegnung.RundenTyp.GEWINNERRUNDE) {
			if (begegnungId.rundenNummerDesTyps < gewinnerRunden) {
				gewinnerBegegnung = findeBegegnungInGruppenRunden(
					new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, begegnungId.rundenNummerDesTyps + 1, (begegnungId.paarungNummer + 1) / 2),
					rundenDerWettkampfgruppe
				);
			}

			if (begegnungId.rundenNummerDesTyps == viertelFinalRunde) {
				List<Wettkaempfer> verliererAufDemPfad = verliererAufGewinnerPfad(begegnung, sieger, rundenDerWettkampfgruppe);
				trostBelegungen.addAll(trostBelegungenFuerViertel(begegnungId.paarungNummer, verliererAufDemPfad, viertelFinalRunde, viertelTrostEnde, haelftenTrostRunde, rundenDerWettkampfgruppe));
			}
			else if (begegnungId.rundenNummerDesTyps == gewinnerRunden - 1) {
				Optional<Wettkaempfer> verlierer = findeVerlierer(begegnung, sieger);
				Optional<Begegnung> bronzeBegegnung = findeBegegnungInGruppenRunden(
					new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, bronzeRunde, begegnungId.paarungNummer),
					rundenDerWettkampfgruppe
				);
				if (verlierer.isPresent() && bronzeBegegnung.isPresent()) {
					trostBelegungen.add(new TrostBelegung(bronzeBegegnung.get(), verlierer.get()));
				}
			}
			return new NachfolgeBelegung(gewinnerBegegnung, trostBelegungen);
		}

		if (begegnungId.rundenTyp == Begegnung.RundenTyp.TROSTRUNDE) {
			Optional<Begegnung> naechsteTrostBegegnung = Optional.empty();
			if (begegnungId.rundenNummerDesTyps < viertelTrostEnde && begegnungId.paarungNummer <= 4) {
				naechsteTrostBegegnung = findeBegegnungInGruppenRunden(
					new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, begegnungId.rundenNummerDesTyps + 1, begegnungId.paarungNummer),
					rundenDerWettkampfgruppe
				);
			}
			else if (begegnungId.rundenNummerDesTyps == viertelTrostEnde && begegnungId.paarungNummer <= 4) {
				naechsteTrostBegegnung = findeBegegnungInGruppenRunden(
					new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, haelftenTrostRunde, (begegnungId.paarungNummer + 1) / 2),
					rundenDerWettkampfgruppe
				);
			}
			else if (begegnungId.rundenNummerDesTyps == haelftenTrostRunde) {
				int bronzePaarung = begegnungId.paarungNummer == 1 ? 2 : 1;
				naechsteTrostBegegnung = findeBegegnungInGruppenRunden(
					new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, bronzeRunde, bronzePaarung),
					rundenDerWettkampfgruppe
				);
			}

			if (naechsteTrostBegegnung.isPresent()) {
				trostBelegungen.add(new TrostBelegung(naechsteTrostBegegnung.get(), sieger));
			}
		}

		return new NachfolgeBelegung(Optional.empty(), trostBelegungen);
	}

	private static List<TrostBelegung> trostBelegungenFuerViertel(int viertel, List<Wettkaempfer> verliererAufDemPfad, int viertelFinalRunde, int viertelTrostEnde, int haelftenTrostRunde, List<Runde> rundenDerWettkampfgruppe) {
		if (verliererAufDemPfad.isEmpty()) {
			return List.of();
		}

		if (verliererAufDemPfad.size() == 1) {
			Optional<Begegnung> haelftenTrostBegegnung = findeBegegnungInGruppenRunden(
				new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, haelftenTrostRunde, (viertel + 1) / 2),
				rundenDerWettkampfgruppe
			);
			return haelftenTrostBegegnung
				.map(begegnung -> List.of(new TrostBelegung(begegnung, verliererAufDemPfad.getFirst())))
				.orElseGet(List::of);
		}

		List<TrostBelegung> result = new ArrayList<>();
		int ersteGenutzteTrostRunde = viertelTrostEnde - (verliererAufDemPfad.size() - 2);
		ersteGenutzteTrostRunde = Math.max(viertelFinalRunde, ersteGenutzteTrostRunde);
		for (int i = 0; i < verliererAufDemPfad.size(); i++) {
			int runde = i < 2 ? ersteGenutzteTrostRunde : ersteGenutzteTrostRunde + i - 1;
			Optional<Begegnung> trostBegegnung = findeBegegnungInGruppenRunden(
				new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, runde, viertel),
				rundenDerWettkampfgruppe
			);
			if (trostBegegnung.isPresent()) {
				result.add(new TrostBelegung(trostBegegnung.get(), verliererAufDemPfad.get(i)));
			}
		}
		return result;
	}

	private static List<Wettkaempfer> verliererAufGewinnerPfad(Begegnung begegnung, Wettkaempfer sieger, List<Runde> rundenDerWettkampfgruppe) {
		List<Wettkaempfer> result = new ArrayList<>();
		Begegnung aktuelleBegegnung = begegnung;

		while (aktuelleBegegnung != null) {
			findeVerlierer(aktuelleBegegnung, sieger).ifPresent(verlierer -> result.add(0, verlierer));
			int vorherigeRunde = aktuelleBegegnung.getBegegnungId().rundenNummerDesTyps - 1;
			if (vorherigeRunde < 1) {
				break;
			}

			int paarung1 = (aktuelleBegegnung.getBegegnungId().paarungNummer - 1) * 2 + 1;
			int paarung2 = paarung1 + 1;
			Optional<Begegnung> vorherigeBegegnung = List.of(paarung1, paarung2).stream()
				.map(paarung -> findeBegegnungInGruppenRunden(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, vorherigeRunde, paarung), rundenDerWettkampfgruppe))
				.flatMap(Optional::stream)
				.filter(kandidat -> enthaeltWettkaempfer(kandidat, sieger))
				.findFirst();

			aktuelleBegegnung = vorherigeBegegnung.orElse(null);
		}

		return result;
	}

	private static boolean enthaeltWettkaempfer(Begegnung begegnung, Wettkaempfer wettkaempfer) {
		return begegnung.getWettkaempfer1().filter(wettkaempfer::equals).isPresent()
			|| begegnung.getWettkaempfer2().filter(wettkaempfer::equals).isPresent();
	}

	private static Optional<Wettkaempfer> findeVerlierer(Begegnung begegnung, Wettkaempfer sieger) {
		if (begegnung.getWettkaempfer1().filter(sieger::equals).isPresent()) {
			return begegnung.getWettkaempfer2();
		}
		if (begegnung.getWettkaempfer2().filter(sieger::equals).isPresent()) {
			return begegnung.getWettkaempfer1();
		}
		return Optional.empty();
	}

	private static boolean istKoSystemMitDoppelterTrostrunde(List<Runde> rundenDerWettkampfgruppe) {
		int gewinnerRunden = maxRunde(Begegnung.RundenTyp.GEWINNERRUNDE, rundenDerWettkampfgruppe);
		int ersteTrostRunde = minRunde(Begegnung.RundenTyp.TROSTRUNDE, rundenDerWettkampfgruppe);
		return gewinnerRunden >= 5 && ersteTrostRunde == gewinnerRunden - 2;
	}

	private static int maxRunde(Begegnung.RundenTyp rundenTyp, List<Runde> runden) {
		return runden.stream()
			.flatMap(runde -> runde.begegnungen().stream())
			.filter(begegnung -> begegnung.getBegegnungId().rundenTyp == rundenTyp)
			.mapToInt(begegnung -> begegnung.getBegegnungId().rundenNummerDesTyps)
			.max()
			.orElse(0);
	}

	private static int minRunde(Begegnung.RundenTyp rundenTyp, List<Runde> runden) {
		return runden.stream()
			.flatMap(runde -> runde.begegnungen().stream())
			.filter(begegnung -> begegnung.getBegegnungId().rundenTyp == rundenTyp)
			.mapToInt(begegnung -> begegnung.getBegegnungId().rundenNummerDesTyps)
			.min()
			.orElse(0);
	}

	private static Optional<Begegnung> findeBegegnungInGruppenRunden(Begegnung.BegegnungId begegnungId, List<Runde> rudenDerWettkmapfgruppe) {
		return rudenDerWettkmapfgruppe.stream()
			.flatMap(runde -> runde.begegnungen().stream().filter(b -> b.getBegegnungId().equals(begegnungId)))
			.findFirst();
	}

	/**
	 * Es wird die erste Runde aller Gruppen gekämpft, danach die zweite Runde aller Gruppen usw.
	 */
	public Pair<Integer, List<Runde>> erstelleReihenfolgeMitAllenGruppenJeDurchgang(List<WettkampfGruppeMitBegegnungen> gruppen, Integer matteId) {
		logger.info("erstelle Reihenfolge -mit allen Gruppen je Durchgang- für Matte {}...", matteId);
		List<Runde> runden = planeMitPausen(gruppen, matteId);
		return new ImmutablePair(rundeGesamt, runden);
	}

	/**
	 * Es werden immer zwei Gruppen abwechselnd kämpfen, bis in diesen Gruppen alle Runden gekämpft sind, danach kommen die nächsten beiden Gruppen an die Reihe.
	 * Bei ungerader Anzahl wechseln sich die letzten drei Gruppen ab, davor gilt weiterhin, dass immer zwei Gruppen abwechselnd an der Reihe sind.
	 */
	public Pair<Integer, List<Runde>> erstelleReihenfolgeMitAbwechselndenGruppen(List<WettkampfGruppeMitBegegnungen> gruppen, Integer matteId) {
		logger.info("erstelle Reihenfolge -mit abwechselnden Gruppen- für Matte {}...", matteId);
		List<Runde> runden = new ArrayList<>();
		// gerade Anzahl an Gruppen -> 2 Gruppen je Matte
		if (gruppen.size() % 2 == 0) {
			logger.info("Gerade Anzahl an Gruppen ({})", gruppen.size());
			runden.addAll(gruppiereAbwechselndPaare(gruppen, matteId));
		}
		// ungerade Anzahl an Gruppen -> 2 Gruppen je Matte und einmal 3 Gruppen je Matte
		else {
			logger.info("Ungerade Anzahl an Gruppen ({})", gruppen.size());
			if (gruppen.size() > 1) {
				logger.info("Wir haben mehr als 1 Gruppe, also splitten wir");
				// behandle die letzten 3 Gruppen separat und gruppiere zuerst die anderen Gruppen
				List<WettkampfGruppeMitBegegnungen> letztenDreiGruppen = gruppen.subList(gruppen.size() - 3, gruppen.size());
				List<WettkampfGruppeMitBegegnungen> andereGruppen = gruppen.subList(0, gruppen.size() - 3);
				runden.addAll(gruppiereAbwechselndPaare(andereGruppen, matteId));

				// jetzt die letzten drei Gruppen
				runden.addAll(gruppiereAbwechselndTrios(letztenDreiGruppen, matteId));
			} else {
				logger.info("Es existiert nur eine Gruppe, daher fügen wir diese komplett hinzu");
				runden.addAll(planeMitPausen(gruppen, matteId));
			}
		}
		return new ImmutablePair(rundeGesamt, runden);
	}

	private List<Runde> gruppiereAbwechselndPaare(List<WettkampfGruppeMitBegegnungen> gruppen, Integer matteId) {
		List<Runde> runden = new ArrayList<>();
		for (int gruppenNr = 0; gruppenNr < gruppen.size(); gruppenNr += 2) {
			runden.addAll(planeMitPausen(gruppen.subList(gruppenNr, gruppenNr + 2), matteId));
		}
		return runden;
	}

	private Runde dummyRunde(Integer matteId, WettkampfGruppe gruppe, Integer gruppenRunde) {
		logger.info("erstelle Pause");
		Begegnung pausenBegegnung = new Begegnung();
		pausenBegegnung.setBegegnungId(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, gruppenRunde, 0));
		pausenBegegnung.setWettkaempfer1(Optional.empty());
		pausenBegegnung.setWettkaempfer2(Optional.empty());
		pausenBegegnung.setTurnierUUID(gruppe.turnierUUID());
		return new Runde(null, mattenRunde, gruppenRunde, rundeGesamt, matteId, Altersklasse.PAUSE, gruppe, List.of(pausenBegegnung));
	}

	private List<Runde> gruppiereAbwechselndTrios(List<WettkampfGruppeMitBegegnungen> gruppen, Integer matteId) {
		logger.debug("gruppiere Matte {}", matteId);
		return planeMitPausen(gruppen, matteId);
	}

	/**
	 * Plant die Runden gruppenweise und erzwingt vor der nächsten Runde derselben Gruppe mindestens zwei Mattenrunden Pause.
	 * Gibt es nicht genug andere Gruppenrunden, werden explizite Pausenrunden eingefügt; jede Pause steht für eine Wettkampfzeit.
	 */
	private List<Runde> planeMitPausen(List<WettkampfGruppeMitBegegnungen> gruppen, Integer matteId) {
		List<Runde> runden = new ArrayList<>();
		if (gruppen.isEmpty()) {
			return runden;
		}

		List<List<GeplanteRunde>> geplanteRundenJeGruppe = gruppen.stream()
			.map(this::geplanteRunden)
			.toList();
		int[] naechsteRundeJeGruppe = new int[gruppen.size()];
		int[] letzteMattenRundeJeGruppe = new int[gruppen.size()];
		Arrays.fill(letzteMattenRundeJeGruppe, Integer.MIN_VALUE);
		int naechsteGruppe = 0;
		int sicherheitsZaehler = 0;

		while (hatOffeneRunden(geplanteRundenJeGruppe, naechsteRundeJeGruppe)) {
			if (sicherheitsZaehler++ > DEFAULT_MAX_RUNDEN * Math.max(1, gruppen.size())) {
				throw new IllegalStateException("Konnte keine gültige Kampfplanung mit Pausen erzeugen.");
			}

			int gruppenIndex = findePlanbareGruppe(geplanteRundenJeGruppe, naechsteRundeJeGruppe, letzteMattenRundeJeGruppe, naechsteGruppe);
			if (gruppenIndex >= 0) {
				WettkampfGruppeMitBegegnungen gruppe = gruppen.get(gruppenIndex);
				GeplanteRunde geplanteRunde = geplanteRundenJeGruppe.get(gruppenIndex).get(naechsteRundeJeGruppe[gruppenIndex]);
				runden.add(runde(matteId, gruppe.gruppe().altersklasse(), gruppe.gruppe(), geplanteRunde));
				letzteMattenRundeJeGruppe[gruppenIndex] = mattenRunde - 1;
				naechsteRundeJeGruppe[gruppenIndex]++;
				naechsteGruppe = (gruppenIndex + 1) % gruppen.size();
				continue;
			}

			gruppenIndex = findeNaechsteOffeneGruppe(geplanteRundenJeGruppe, naechsteRundeJeGruppe, naechsteGruppe);
			WettkampfGruppeMitBegegnungen gruppe = gruppen.get(gruppenIndex);
			GeplanteRunde naechsteGeplanteRunde = geplanteRundenJeGruppe.get(gruppenIndex).get(naechsteRundeJeGruppe[gruppenIndex]);
			runden.add(dummyRunde(matteId, gruppe.gruppe(), naechsteGeplanteRunde.gruppenRunde()));
			rundeGesamt++;
			mattenRunde++;
			naechsteGruppe = (gruppenIndex + 1) % gruppen.size();
		}

		return runden;
	}

	private List<GeplanteRunde> geplanteRunden(WettkampfGruppeMitBegegnungen gruppe) {
		List<GeplanteRunde> result = new ArrayList<>();
		int gruppenRunde = 1;
		for (BegegnungenJeRunde begegnungen : gruppe.alleRundenBegegnungen()) {
			result.addAll(geplanteRunden(begegnungen, gruppenRunde));
			gruppenRunde++;
		}
		return result;
	}

	private List<GeplanteRunde> geplanteRunden(BegegnungenJeRunde begegnungen, int gruppenRunde) {
		List<GeplanteRunde> result = new ArrayList<>();
		List<Begegnung> begegnungenGewinnerrunde = begegnungen.begegnungenJeRunde().stream().filter(b -> b.getBegegnungId().getRundenTyp() == Begegnung.RundenTyp.GEWINNERRUNDE).toList();
		List<Begegnung> begegnungenTrostrunde = begegnungen.begegnungenJeRunde().stream().filter(b -> b.getBegegnungId().getRundenTyp() == Begegnung.RundenTyp.TROSTRUNDE).toList();
		if (!begegnungenGewinnerrunde.isEmpty()) {
			result.add(new GeplanteRunde(gruppenRunde, begegnungenGewinnerrunde));
		}
		if (!begegnungenTrostrunde.isEmpty()) {
			result.add(new GeplanteRunde(gruppenRunde, begegnungenTrostrunde));
		}
		return result;
	}

	private Runde runde(Integer matteId, Altersklasse altersKlasse, WettkampfGruppe gruppe, GeplanteRunde geplanteRunde) {
		return new Runde(null, mattenRunde++, geplanteRunde.gruppenRunde(), rundeGesamt++, matteId, altersKlasse, gruppe, geplanteRunde.begegnungen());
	}

	private boolean hatOffeneRunden(List<List<GeplanteRunde>> geplanteRundenJeGruppe, int[] naechsteRundeJeGruppe) {
		for (int i = 0; i < geplanteRundenJeGruppe.size(); i++) {
			if (naechsteRundeJeGruppe[i] < geplanteRundenJeGruppe.get(i).size()) {
				return true;
			}
		}
		return false;
	}

	private int findePlanbareGruppe(List<List<GeplanteRunde>> geplanteRundenJeGruppe, int[] naechsteRundeJeGruppe, int[] letzteMattenRundeJeGruppe, int startIndex) {
		for (int offset = 0; offset < geplanteRundenJeGruppe.size(); offset++) {
			int gruppenIndex = (startIndex + offset) % geplanteRundenJeGruppe.size();
			if (naechsteRundeJeGruppe[gruppenIndex] < geplanteRundenJeGruppe.get(gruppenIndex).size() && hatAusreichendPause(letzteMattenRundeJeGruppe[gruppenIndex])) {
				return gruppenIndex;
			}
		}
		return -1;
	}

	private int findeNaechsteOffeneGruppe(List<List<GeplanteRunde>> geplanteRundenJeGruppe, int[] naechsteRundeJeGruppe, int startIndex) {
		for (int offset = 0; offset < geplanteRundenJeGruppe.size(); offset++) {
			int gruppenIndex = (startIndex + offset) % geplanteRundenJeGruppe.size();
			if (naechsteRundeJeGruppe[gruppenIndex] < geplanteRundenJeGruppe.get(gruppenIndex).size()) {
				return gruppenIndex;
			}
		}
		throw new IllegalStateException("Keine offene Gruppe gefunden.");
	}

	private boolean hatAusreichendPause(int letzteMattenRundeDerGruppe) {
		return letzteMattenRundeDerGruppe == Integer.MIN_VALUE || mattenRunde - letzteMattenRundeDerGruppe - 1 >= PAUSEN;
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
