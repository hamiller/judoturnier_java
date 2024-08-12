package de.sinnix.judoturnier.application.algorithm;

import de.sinnix.judoturnier.model.Begegnung;
import de.sinnix.judoturnier.model.BegegnungenJeRunde;
import de.sinnix.judoturnier.model.GewichtsklassenGruppe;
import de.sinnix.judoturnier.model.RandoriGruppenName;
import de.sinnix.judoturnier.model.Wettkaempfer;
import de.sinnix.judoturnier.model.WettkampfGruppe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DoppelKOSystem implements Algorithmus {
	@Override
	public List<WettkampfGruppe> erstelleWettkampfGruppen(Integer gruppenid, GewichtsklassenGruppe gewichtsklassenGruppe, Integer maxGruppenGroesse) {
		List<WettkampfGruppe> result = new ArrayList<>();

		// Teilnehmer der Gruppe
		List<Wettkaempfer> teilnehmer = gewichtsklassenGruppe.teilnehmer();
		int teilnehmerAnzahl = teilnehmer.size();

		// Gruppieren der Teilnehmer in kleinere Gruppen, falls notwendig
		List<List<Wettkaempfer>> wettkaempferGruppen = splitArrayToChunkSize(gewichtsklassenGruppe.teilnehmer(), maxGruppenGroesse);

		// Alle Begegnungen in jeder Gruppe generieren
		for (int i = 0; i < wettkaempferGruppen.size(); i++) {
			List<Wettkaempfer> wettkaempferGruppe = wettkaempferGruppen.get(i);

			List<BegegnungenJeRunde> begegnungen = berechneBegegnungen(wettkaempferGruppe);

			String id = ((gruppenid + 1) * 10) + Integer.toString(i); // ids erstellen und konkatenieren
			WettkampfGruppe wettkampfGruppe = new WettkampfGruppe(
				Integer.parseInt(id),
				gewichtsklassenGruppe.name().orElseGet(() -> RandoriGruppenName.Ameise).name(),
				"(" + gewichtsklassenGruppe.minGewicht() + "-" + gewichtsklassenGruppe.maxGewicht() + " " + gewichtsklassenGruppe.altersKlasse() + ")",
				begegnungen,
				gewichtsklassenGruppe.turnierUUID()
			);
			result.add(wettkampfGruppe);
		}
		return result;
	}

	private List<BegegnungenJeRunde> berechneBegegnungen(List<Wettkaempfer> teilnehmer) {
		List<BegegnungenJeRunde> alleRundenBegegnungen = new ArrayList<>();
		Map<Integer, Begegnung> platzhalterBegegnungen = new HashMap<>();

		int rundenAnzahl = berechneRundenAnzahl(teilnehmer.size());
		var turnierUUID = teilnehmer.get(0).turnierUUID();

		// Gewinnerbaum
		for (int runde = 1; runde <= rundenAnzahl; runde++) {
			List<Begegnung> begegnungenJeRunde = new ArrayList<>();
			for (int i = 0; i < teilnehmer.size(); i += 2) {
				Wettkaempfer wettkaempfer1 = teilnehmer.get(i);
				Wettkaempfer wettkaempfer2 = i + 1 < teilnehmer.size() ? teilnehmer.get(i + 1) : null;

				var newBegegnung = new Begegnung();
				newBegegnung.setTurnierUUID(turnierUUID);
				newBegegnung.setWettkaempfer1(wettkaempfer1);
				if (wettkaempfer2 != null) {
					newBegegnung.setWettkaempfer2(wettkaempfer2);
				}
				platzhalterBegegnungen.put(i / 2, newBegegnung);
				begegnungenJeRunde.add(newBegegnung);
			}
			alleRundenBegegnungen.add(new BegegnungenJeRunde(begegnungenJeRunde));

			// Verliererbaum
			if (runde > 1) {
				List<Begegnung> verliererBegegnungen = new ArrayList<>();
				for (int j = 0; j < platzhalterBegegnungen.size(); j += 2) {
					var newVerliererBegegnung = new Begegnung();
					newVerliererBegegnung.setTurnierUUID(turnierUUID);
					newVerliererBegegnung.setWettkaempfer1(platzhalterBegegnungen.get(j).getVerlierer());
					newVerliererBegegnung.setWettkaempfer2(platzhalterBegegnungen.get(j + 1).getVerlierer());

					verliererBegegnungen.add(newVerliererBegegnung);
				}
				alleRundenBegegnungen.add(new BegegnungenJeRunde(verliererBegegnungen));
			}
		}

		// Finale Begegnungen
		Begegnung newFinaleBegegnung = new Begegnung();
		newFinaleBegegnung.setTurnierUUID(turnierUUID);
		newFinaleBegegnung.setWettkaempfer1(platzhalterBegegnungen.get(0).getGewinner());
		newFinaleBegegnung.setWettkaempfer2(platzhalterBegegnungen.get(1).getGewinner());
		alleRundenBegegnungen.add(new BegegnungenJeRunde(Collections.singletonList(newFinaleBegegnung)));

		return alleRundenBegegnungen;
	}

	private int berechneRundenAnzahl(int teilnehmerAnzahl) {
		if (teilnehmerAnzahl <= 1) return 0;

		int rundenAnzahl = (int) Math.ceil(Math.log(teilnehmerAnzahl) / Math.log(2));

		// In einem Doppel-KO-System hat man die Gewinner- und die Verliererrunde,
		// daher verdoppelt sich die Anzahl der Runden
		return rundenAnzahl * 2;
	}

	private List<List<Wettkaempfer>> splitArrayToChunkSize(List<Wettkaempfer> arr, int chunkSize) {
		List<List<Wettkaempfer>> result = new ArrayList<>();
		for (int i = 0; i < arr.size(); i += chunkSize) {
			result.add(arr.subList(i, Math.min(i + chunkSize, arr.size())));
		}
		return result;
	}
}
