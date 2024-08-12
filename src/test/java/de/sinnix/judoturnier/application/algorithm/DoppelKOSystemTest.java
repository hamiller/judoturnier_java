package de.sinnix.judoturnier.application.algorithm;

import de.sinnix.judoturnier.fixtures.GewichtsklassenGruppeFixture;
import de.sinnix.judoturnier.fixtures.WettkaempferFixtures;
import de.sinnix.judoturnier.model.GewichtsklassenGruppe;
import de.sinnix.judoturnier.model.Wettkaempfer;
import de.sinnix.judoturnier.model.WettkampfGruppe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class DoppelKOSystemTest {

	private DoppelKOSystem algorithmus = new DoppelKOSystem();

	private UUID turnierUUID;

	@BeforeEach
	void setUp() {
		turnierUUID = UUID.randomUUID();
	}

	@Test
	void erstelleWettkampfGruppen() {
		GewichtsklassenGruppe gewichtsklassenGruppe = GewichtsklassenGruppeFixture.gewichtsklassenGruppen.getFirst();
		List<WettkampfGruppe> erstellteWettkampfgruppen = algorithmus.erstelleWettkampfGruppen(1, gewichtsklassenGruppe, 6);

		System.out.println(erstellteWettkampfgruppen);
	}

	@Test
	void test(){
		// Teilnehmer einer Gruppe
		List<Wettkaempfer> teilnehmer = WettkaempferFixtures.wettkaempferList;

		// Beispiel, wie das Turnier in Java initialisiert werden kann
		DKOSystem turnier = new DKOSystem();
		int gesamtAnzahlSpieler = teilnehmer.size();
		int gesamtRunden = (int) Math.ceil(Math.log(gesamtAnzahlSpieler) / Math.log(2));
		boolean istLang = true;
		int letzteRunde = DKOSystem.GEWINNER_RUNDE;

		List<DKOSystem.Begegnung> begegnungList = DKOSystem.eliminierung(gesamtAnzahlSpieler, gesamtRunden, letzteRunde, istLang);

		// Beispiel für den Fortschritt eines Spiels
		DKOSystem.Id begegnungId = begegnungList.get(0).id;
		turnier.spieleinsatz(begegnungId, 1, begegnungList);

		// Ausgabe des Turnierbaums
		for (DKOSystem.Begegnung begegnung : begegnungList) {
			System.out.println(begegnung.id + " - Wettkaempfer: " + begegnung.wettkaempfer + " - Ergebnis: " + begegnung.ergebnis);
		}
	}
}