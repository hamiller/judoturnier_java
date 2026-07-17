package de.sinnix.judoturnier.application.algorithm;

import de.sinnix.judoturnier.model.Begegnung;
import de.sinnix.judoturnier.model.BegegnungenJeRunde;
import de.sinnix.judoturnier.model.GewichtsklassenGruppe;
import de.sinnix.judoturnier.model.RandoriGruppenName;
import de.sinnix.judoturnier.model.Wettkaempfer;
import de.sinnix.judoturnier.model.WettkampfGruppe;
import de.sinnix.judoturnier.model.WettkampfGruppeMitBegegnungen;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * KO-System mit doppelter Trostrunde nach offizieller Listenlogik:
 *
 * - Die Hauptrunde ist ein normales KO-System mit vier Vierteln der Hauptliste.
 * - Nur Verlierer gegen die vier Teilnehmer, die um den Einzug ins Finale kämpfen
 *   (Poolsieger A-D bzw. Halbfinalisten), kommen in die Trostrunde.
 * - Verlierer aus demselben Viertel kämpfen in der Trostrunde sukzessive gegeneinander.
 * - Die beiden Trostrundensieger aus den Hauptlistenhälften kämpfen überkreuz gegen die
 *   Halbfinalverlierer um die beiden dritten Plätze.
 */
public class KOSystemMitDoppelterTrostrunde implements Algorithmus {

	@Override
	public WettkampfGruppeMitBegegnungen erstelleWettkampfGruppe(GewichtsklassenGruppe gewichtsklassenGruppe) {
		List<Wettkaempfer> teilnehmer = gewichtsklassenGruppe.teilnehmer();
		int listenGroesse = listenGroesse(teilnehmer.size());
		List<BegegnungenJeRunde> runden = erstelleRunden(teilnehmer, listenGroesse, gewichtsklassenGruppe.turnierUUID());

		WettkampfGruppe wettkampfGruppe = new WettkampfGruppe(
			null,
			gewichtsklassenGruppe.name().orElseGet(() -> RandoriGruppenName.Ameise).name(),
			"(" + gewichtsklassenGruppe.minGewicht() + "-" + gewichtsklassenGruppe.maxGewicht() + " " + gewichtsklassenGruppe.altersKlasse() + ")",
			gewichtsklassenGruppe.altersKlasse(),
			gewichtsklassenGruppe.turnierUUID()
		);
		return new WettkampfGruppeMitBegegnungen(wettkampfGruppe, runden);
	}

	private List<BegegnungenJeRunde> erstelleRunden(List<Wettkaempfer> teilnehmer, int listenGroesse, UUID turnierUUID) {
		int gewinnerRunden = log2(listenGroesse);
		int ersteTrostRunde = gewinnerRunden - 2;
		int viertelTrostEnde = ersteTrostRunde + ersteTrostRunde - 2;
		int haelftenTrostRunde = viertelTrostEnde + 1;
		int bronzeRunde = haelftenTrostRunde + 1;

		List<BegegnungenJeRunde> result = new ArrayList<>();

		for (int runde = 1; runde < gewinnerRunden; runde++) {
			List<Begegnung> begegnungen = new ArrayList<>();
			begegnungen.addAll(gewinnerRundenBegegnungen(teilnehmer, listenGroesse, runde, turnierUUID));
			if (runde >= ersteTrostRunde && runde <= viertelTrostEnde) {
				begegnungen.addAll(leereTrostrunde(runde, 4, turnierUUID));
			}
			result.add(new BegegnungenJeRunde(begegnungen));
		}

		for (int runde = gewinnerRunden; runde <= viertelTrostEnde; runde++) {
			result.add(new BegegnungenJeRunde(leereTrostrunde(runde, 4, turnierUUID)));
		}
		result.add(new BegegnungenJeRunde(leereTrostrunde(haelftenTrostRunde, 2, turnierUUID)));
		result.add(new BegegnungenJeRunde(leereTrostrunde(bronzeRunde, 2, turnierUUID)));
		result.add(new BegegnungenJeRunde(gewinnerRundenBegegnungen(teilnehmer, listenGroesse, gewinnerRunden, turnierUUID)));

		return result;
	}

	private List<Begegnung> gewinnerRundenBegegnungen(List<Wettkaempfer> teilnehmer, int listenGroesse, int runde, UUID turnierUUID) {
		if (runde == 1) {
			return ersteGewinnerRunde(teilnehmer, listenGroesse, turnierUUID);
		}

		int anzahl = listenGroesse / (int) Math.pow(2, runde);
		List<Begegnung> begegnungen = new ArrayList<>();
		for (int paarung = 1; paarung <= anzahl; paarung++) {
			begegnungen.add(leereBegegnung(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, runde, paarung), turnierUUID));
		}
		return begegnungen;
	}

	private List<Begegnung> ersteGewinnerRunde(List<Wettkaempfer> teilnehmer, int listenGroesse, UUID turnierUUID) {
		List<Begegnung> result = new ArrayList<>();
		List<Pair<Optional<Wettkaempfer>, Optional<Wettkaempfer>>> paarungen = freilosMarkierung(teilnehmer, listenGroesse);
		for (int paarung = 1; paarung <= paarungen.size(); paarung++) {
			Begegnung begegnung = new Begegnung();
			begegnung.setTurnierUUID(turnierUUID);
			begegnung.setBegegnungId(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, paarung));
			begegnung.setWettkaempfer1(paarungen.get(paarung - 1).getLeft());
			begegnung.setWettkaempfer2(paarungen.get(paarung - 1).getRight());
			result.add(begegnung);
		}
		return result;
	}

	private List<Begegnung> leereTrostrunde(int runde, int anzahl, UUID turnierUUID) {
		List<Begegnung> begegnungen = new ArrayList<>();
		for (int paarung = 1; paarung <= anzahl; paarung++) {
			begegnungen.add(leereBegegnung(new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, runde, paarung), turnierUUID));
		}
		return begegnungen;
	}

	private Begegnung leereBegegnung(Begegnung.BegegnungId begegnungId, UUID turnierUUID) {
		Begegnung begegnung = new Begegnung();
		begegnung.setTurnierUUID(turnierUUID);
		begegnung.setBegegnungId(begegnungId);
		begegnung.setWettkaempfer1(Optional.empty());
		begegnung.setWettkaempfer2(Optional.empty());
		return begegnung;
	}

	private List<Pair<Optional<Wettkaempfer>, Optional<Wettkaempfer>>> freilosMarkierung(List<Wettkaempfer> wettkaempfer, int listenGroesse) {
		List<Pair<Optional<Wettkaempfer>, Optional<Wettkaempfer>>> paarungen = new ArrayList<>();
		List<Integer> losnummern = offizielleLosnummernReihenfolge(listenGroesse);

		for (int i = 0; i < losnummern.size(); i += 2) {
			Optional<Wettkaempfer> wettkaempfer1 = wettkaempferMitLosnummer(wettkaempfer, losnummern.get(i));
			Optional<Wettkaempfer> wettkaempfer2 = wettkaempferMitLosnummer(wettkaempfer, losnummern.get(i + 1));
			paarungen.add(new ImmutablePair<>(wettkaempfer1, wettkaempfer2));
		}

		return paarungen;
	}

	private Optional<Wettkaempfer> wettkaempferMitLosnummer(List<Wettkaempfer> wettkaempfer, int losnummer) {
		if (losnummer > wettkaempfer.size()) {
			return Optional.empty();
		}
		return Optional.of(wettkaempfer.get(losnummer - 1));
	}

	private List<Integer> offizielleLosnummernReihenfolge(int listenGroesse) {
		if (listenGroesse < 2 || Integer.bitCount(listenGroesse) != 1) {
			throw new IllegalArgumentException("KO-Listen müssen eine Zweierpotenz als Größe haben: " + listenGroesse);
		}
		if (listenGroesse == 2) {
			return List.of(1, 2);
		}

		List<Integer> kleinereListe = offizielleLosnummernReihenfolge(listenGroesse / 2);
		List<Integer> result = new ArrayList<>(listenGroesse);
		for (Integer losnummer : kleinereListe) {
			result.add(losnummer);
			result.add(losnummer + (listenGroesse / 2));
		}
		return result;
	}

	private int listenGroesse(int teilnehmerAnzahl) {
		if (teilnehmerAnzahl < 9) {
			throw new IllegalArgumentException("KO-System mit doppelter Trostrunde ist erst ab 9 Teilnehmern vorgesehen.");
		}
		if (teilnehmerAnzahl <= 32) {
			return 32;
		}
		if (teilnehmerAnzahl <= 64) {
			return 64;
		}
		throw new IllegalArgumentException("KO-System mit doppelter Trostrunde ist nur für 32er- und 64er-Listen implementiert. Teilnehmer: " + teilnehmerAnzahl);
	}

	private int log2(int wert) {
		return (int) (Math.log(wert) / Math.log(2));
	}
}
