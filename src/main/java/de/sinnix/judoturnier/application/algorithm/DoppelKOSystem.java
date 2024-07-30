package de.sinnix.judoturnier.application.algorithm;

import de.sinnix.judoturnier.model.Begegnung;
import de.sinnix.judoturnier.model.BegegnungenJeRunde;
import de.sinnix.judoturnier.model.GewichtsklassenGruppe;
import de.sinnix.judoturnier.model.Wettkaempfer;
import de.sinnix.judoturnier.model.WettkampfGruppe;

import java.util.ArrayList;
import java.util.List;

public class DoppelKOSystem implements Algorithmus {
	@Override
	public List<WettkampfGruppe> erstelleWettkampfGruppen(Integer gruppenid, GewichtsklassenGruppe gewichtsklassenGruppe, Integer maxGruppenGroesse) {
		List<WettkampfGruppe> wettkampfGruppen = new ArrayList<>();
		List<Wettkaempfer> teilnehmer = gewichtsklassenGruppe.teilnehmer();

		if (teilnehmer.size() < 1) {
			// wir wollen die Dummies nicht berechnen
			return List.of();
		}


		// Erstellen der Begegnungen für die Haupt- und Verlierer-Runden
		List<BegegnungenJeRunde> alleRundenBegegnungen = new ArrayList<>();

		// Initialisiere erste Runde mit allen Teilnehmern
		List<Begegnung> ersteRunde = erstelleBegegnungen(teilnehmer, 1);
		alleRundenBegegnungen.add(new BegegnungenJeRunde(ersteRunde));

		// Weitere Runden (Haupt- und Verlierer-Runden) werden in einer separaten Methode erstellt

		WettkampfGruppe gruppe = new WettkampfGruppe(gruppenid, "Doppel-KO Gruppe", "Doppel-KO", alleRundenBegegnungen, gewichtsklassenGruppe.turnierUUID());
		wettkampfGruppen.add(gruppe);

		return wettkampfGruppen;
	}

	private List<Begegnung> erstelleBegegnungen(List<Wettkaempfer> teilnehmer, int gruppenRunde) {
		List<Begegnung> begegnungen = new ArrayList<>();
		int begegnungId = 1;

		for (int i = 0; i < teilnehmer.size(); i += 2) {
			Wettkaempfer wettkaempfer1 = teilnehmer.get(i);
			Wettkaempfer wettkaempfer2 = (i + 1 < teilnehmer.size()) ? teilnehmer.get(i + 1) : null;

			var newBegegnung = new Begegnung();
			newBegegnung.setWettkaempfer1(wettkaempfer1);
			newBegegnung.setWettkaempfer2(wettkaempfer2);
			newBegegnung.setTurnierUUID(wettkaempfer1.turnierUUID());
			begegnungen.add(newBegegnung);
		}

		return begegnungen;
	}
}
