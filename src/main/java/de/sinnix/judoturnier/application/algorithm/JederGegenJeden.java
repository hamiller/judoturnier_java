package de.sinnix.judoturnier.application.algorithm;

import de.sinnix.judoturnier.model.Begegnung;
import de.sinnix.judoturnier.model.BegegnungenJeRunde;
import de.sinnix.judoturnier.model.GewichtsklassenGruppe;
import de.sinnix.judoturnier.model.RandoriGruppenName;
import de.sinnix.judoturnier.model.Wettkaempfer;
import de.sinnix.judoturnier.model.WettkampfGruppe;
import de.sinnix.judoturnier.model.WettkampfGruppeMitBegegnungen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JederGegenJeden implements Algorithmus {
	private static final Logger logger                   = LogManager.getLogger(JederGegenJeden.class);

	@Override
	public WettkampfGruppeMitBegegnungen erstelleWettkampfGruppe(GewichtsklassenGruppe gewichtsklassenGruppe) {
		logger.info("JederGegenJeden Algorithmus genutzt");
		List<Wettkaempfer> wettkaempferGruppe = gewichtsklassenGruppe.teilnehmer();
		List<BegegnungenJeRunde> begegnungenJeRunde = berechneBegegnungen(wettkaempferGruppe);

		WettkampfGruppe wettkampfGruppe = new WettkampfGruppe(
			null,
			gewichtsklassenGruppe.name().orElseGet(() -> RandoriGruppenName.Ameise).name(),
			"(" + gewichtsklassenGruppe.minGewicht() + "-" + gewichtsklassenGruppe.maxGewicht() + " " + gewichtsklassenGruppe.altersKlasse() + ")",
			gewichtsklassenGruppe.altersKlasse(),
			gewichtsklassenGruppe.turnierUUID()
		);
		return new WettkampfGruppeMitBegegnungen(wettkampfGruppe, begegnungenJeRunde);
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
			var begegnungsId = new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1);
			dummyBegegnung.setBegegnungId(begegnungsId);
			dummyBegegnung.setWettkaempfer1(Optional.of(teilnehmer.get(0)));
			dummyBegegnung.setWettkaempfer2(Optional.empty());
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
					var begegnungsId = new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, i+1, j+1);
					newBegegnung.setBegegnungId(begegnungsId);
					newBegegnung.setWettkaempfer1(Optional.of(teilnehmer.get(k)));
					newBegegnung.setWettkaempfer2(Optional.empty());
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
				alleRundenBegegnungen.get(i).begegnungenJeRunde().get(j).setWettkaempfer2(Optional.of(teilnehmer.get(k)));
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
				var begegnungsId = new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, i+1, j+1);
				newBegegnung.setBegegnungId(begegnungsId);
				newBegegnung.setWettkaempfer1(Optional.of(teilnehmer.get(k)));
				newBegegnung.setWettkaempfer2(Optional.empty());
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
				alleRundenBegegnungen.get(i).begegnungenJeRunde().get(0).setWettkaempfer2(Optional.of(teilnehmer.get(teilnehmerZahl - 1)));
			} else {
				alleRundenBegegnungen.get(i).begegnungenJeRunde().get(0).setWettkaempfer2(alleRundenBegegnungen.get(i).begegnungenJeRunde().get(0).getWettkaempfer1());
				alleRundenBegegnungen.get(i).begegnungenJeRunde().get(0).setWettkaempfer1(Optional.of(teilnehmer.get(teilnehmerZahl - 1)));
			}
		}

		int letzteUngeradeTeilnehmerZahl = teilnehmerZahl - 2;
		for (int i = 0, k = letzteUngeradeTeilnehmerZahl; i < anzahlRunden; i++) {
			for (int j = 1; j < anzahlBegegnungenJeRunden; j++) {
				alleRundenBegegnungen.get(i).begegnungenJeRunde().get(j).setWettkaempfer2(Optional.of(teilnehmer.get(k)));
				k--;
				if (k == -1) {
					k = letzteUngeradeTeilnehmerZahl;
				}
			}
		}

		return alleRundenBegegnungen;
	}

}
