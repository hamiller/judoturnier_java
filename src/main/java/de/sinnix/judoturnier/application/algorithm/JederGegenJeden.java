package de.sinnix.judoturnier.application.algorithm;

import de.sinnix.judoturnier.model.Begegnung;
import de.sinnix.judoturnier.model.BegegnungenJeRunde;
import de.sinnix.judoturnier.model.GewichtsklassenGruppe;
import de.sinnix.judoturnier.model.RandoriGruppenName;
import de.sinnix.judoturnier.model.Wettkaempfer;
import de.sinnix.judoturnier.model.WettkampfGruppe;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class JederGegenJeden implements Algorithmus {
	private static final Logger logger                   = LogManager.getLogger(JederGegenJeden.class);

	@Override
	public List<WettkampfGruppe> erstelleWettkampfGruppen(Integer gruppenid, GewichtsklassenGruppe gewichtsklassenGruppe, Integer maxGruppenGroesse) {
		logger.info("JederGegenJeden Algorithmus genutzt");

		List<WettkampfGruppe> result = new ArrayList<>();

		// erstellt Gruppen mit bis zu 6 Kämpfern
		List<List<Wettkaempfer>> wettkaempferGruppen = splitArrayToChunkSize(gewichtsklassenGruppe.teilnehmer(), maxGruppenGroesse);

		// Alle möglichen Begegnungen in jeder Gruppe generieren
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
		if (teilnehmer.size() % 2 == 0) {
			return berechneBegegnungenMitGeraderAnzahl(teilnehmer);
		}
		return berechneBegegnungenMitUngeraderAnzahl(teilnehmer);
	}

	private List<BegegnungenJeRunde> berechneBegegnungenMitUngeraderAnzahl(List<Wettkaempfer> teilnehmer) {
		int teilnehmerZahl = teilnehmer.size();
		int anzahlRunden = teilnehmerZahl;
		List<BegegnungenJeRunde> alleRundenBegegnungen = new ArrayList<>(anzahlRunden);
		int anzahlBegegnungenJeRunden = (int) Math.floor(teilnehmerZahl / 2.0);

		if (teilnehmerZahl == 1) {
			logger.warn("Einzelner Teilnehmer in einer Gruppe, KEINE Kampfbegegnungen! Erstelle einzelne Dummy-Begegnung...");
			var dummyBegegnung = new Begegnung();
			dummyBegegnung.setWettkaempfer1(teilnehmer.get(0));
			dummyBegegnung.setTurnierUUID(teilnehmer.get(0).turnierUUID());
			alleRundenBegegnungen.add(new BegegnungenJeRunde(List.of(dummyBegegnung)));
			return alleRundenBegegnungen;
		}

		for (int i = 0, k = 0; i < anzahlRunden; i++) {
			List<Begegnung> begegnungList = new ArrayList<>(anzahlBegegnungenJeRunden);
			BegegnungenJeRunde begegnungenJeRunde = new BegegnungenJeRunde(begegnungList);
			for (int j = -1; j < anzahlBegegnungenJeRunden; j++) {
				if (j >= 0) {
					var newBegegnung = new Begegnung();
					newBegegnung.setWettkaempfer1(teilnehmer.get(k));
					newBegegnung.setTurnierUUID(teilnehmer.get(k).turnierUUID());
					begegnungList.add(newBegegnung);
				}
				k++;
				if (k == anzahlRunden) {
					k = 0;
				}
			}
			alleRundenBegegnungen.add(begegnungenJeRunde);
		}

		int letzteTeilnehmerZahl = teilnehmerZahl - 1;
		for (int i = 0, k = letzteTeilnehmerZahl; i < anzahlRunden; i++) {
			for (int j = 0; j < anzahlBegegnungenJeRunden; j++) {
				alleRundenBegegnungen.get(i).begegnungenJeRunde().get(j).setWettkaempfer2(teilnehmer.get(k));
				k--;
				if (k == -1) {
					k = letzteTeilnehmerZahl;
				}
			}
		}

		return alleRundenBegegnungen;
	}

	private List<BegegnungenJeRunde> berechneBegegnungenMitGeraderAnzahl(List<Wettkaempfer> teilnehmer) {
		int teilnehmerZahl = teilnehmer.size();
		int anzahlRunden = teilnehmerZahl - 1;
		int anzahlBegegnungenJeRunden = (int) Math.floor(teilnehmerZahl / 2.0);
		List<BegegnungenJeRunde> alleRundenBegegnungen = new ArrayList<>(anzahlRunden);

		for (int i = 0, k = 0; i < anzahlRunden; i++) {
			List<Begegnung> begegnungList = new ArrayList<>(anzahlBegegnungenJeRunden);
			BegegnungenJeRunde begegnungenJeRunde = new BegegnungenJeRunde(begegnungList);
			for (int j = 0; j < anzahlBegegnungenJeRunden; j++) {
				var newBegegnung = new Begegnung();
				newBegegnung.setWettkaempfer1(teilnehmer.get(k));
				newBegegnung.setTurnierUUID(teilnehmer.get(k).turnierUUID());
				begegnungList.add(newBegegnung);
				k++;
				if (k == anzahlRunden) {
					k = 0;
				}
			}
			alleRundenBegegnungen.add(begegnungenJeRunde);
		}

		for (int i = 0; i < anzahlRunden; i++) {
			if (i % 2 == 0) {
				alleRundenBegegnungen.get(i).begegnungenJeRunde().get(0).setWettkaempfer2(teilnehmer.get(teilnehmerZahl - 1));
			} else {
				alleRundenBegegnungen.get(i).begegnungenJeRunde().get(0).setWettkaempfer2(alleRundenBegegnungen.get(i).begegnungenJeRunde().get(0).getWettkaempfer1());
				alleRundenBegegnungen.get(i).begegnungenJeRunde().get(0).setWettkaempfer1(teilnehmer.get(teilnehmerZahl - 1));
			}
		}

		int letzteUngeradeTeilnehmerZahl = teilnehmerZahl - 2;
		for (int i = 0, k = letzteUngeradeTeilnehmerZahl; i < anzahlRunden; i++) {
			for (int j = 1; j < anzahlBegegnungenJeRunden; j++) {
				alleRundenBegegnungen.get(i).begegnungenJeRunde().get(j).setWettkaempfer2(teilnehmer.get(k));
				k--;
				if (k == -1) {
					k = letzteUngeradeTeilnehmerZahl;
				}
			}
		}

		return alleRundenBegegnungen;
	}

	private List<List<Wettkaempfer>> splitArrayToChunkSize(List<Wettkaempfer> arr, int chunkSize) {
		List<List<Wettkaempfer>> result = new ArrayList<>();
		for (int i = 0; i < arr.size(); i += chunkSize) {
			result.add(arr.subList(i, Math.min(i + chunkSize, arr.size())));
		}
		return result;
	}
}
