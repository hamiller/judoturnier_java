package de.sinnix.judoturnier.application.algorithm;

import de.sinnix.judoturnier.fixtures.GewichtsklassenGruppeFixture;
import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Begegnung;
import de.sinnix.judoturnier.model.Farbe;
import de.sinnix.judoturnier.model.Geschlecht;
import de.sinnix.judoturnier.model.GewichtsklassenGruppe;
import de.sinnix.judoturnier.model.Verein;
import de.sinnix.judoturnier.model.Wettkaempfer;
import de.sinnix.judoturnier.model.WettkampfGruppe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
	void testErstelleBegegnungen(){
		// Teilnehmer einer Gruppe
		List<Wettkaempfer> teilnehmer = Arrays.asList(
			new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 25.0, Optional.of(Farbe.ORANGE), true, false, turnierUUID),
			new Wettkaempfer(2, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3", turnierUUID), 27.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5", turnierUUID), 29.0, Optional.of(Farbe.ORANGE), true, false, turnierUUID),
			new Wettkaempfer(4, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2", turnierUUID), 26.1, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(5, "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 33.0, Optional.of(Farbe.ORANGE), true, false, turnierUUID),
			new Wettkaempfer(6, "Teilnehmer F", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 35.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(7, "Teilnehmer G", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3", turnierUUID), 37.0, Optional.of(Farbe.ORANGE), true, false, turnierUUID),
			new Wettkaempfer(8, "Teilnehmer H", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5", turnierUUID), 39.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(9, "Teilnehmer I", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2", turnierUUID), 41.0, Optional.of(Farbe.ORANGE), true, false, turnierUUID),
			new Wettkaempfer(10, "Teilnehmer J", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 43.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID)
		);

		// Beispiel, wie das Turnier in Java initialisiert werden kann
		int gesamtAnzahlSpieler = teilnehmer.size();
		int gesamtRunden = (int) Math.ceil(Math.log(gesamtAnzahlSpieler) / Math.log(2));
		boolean bronzeFinale = true;
		int letzteRunde = 1;

		List<Begegnung> begegnungList = algorithmus.erstelleBegegnungen(teilnehmer, gesamtAnzahlSpieler, gesamtRunden, letzteRunde, bronzeFinale);
		assertEquals(16, begegnungList.size());

		// Ausgabe des Turnierbaums
		for (Begegnung begegnung : begegnungList) {
			System.out.println(begegnung.getBegegnungId() + " - Wettkaempfer1: " + begegnung.getWettkaempfer1().map(Wettkaempfer::name) + ", Wettkaempfer2: " + begegnung.getWettkaempfer2().map(Wettkaempfer::name) + " - Ergebnis: " +
				begegnung.getWertungen());
		}

		// Beispiel für den Fortschritt eines Spiels
		Wettkaempfer sieger1 = teilnehmer.get(1);
//		Begegnung.BegegnungId begegnungId = begegnungList.get(0).getBegegnungId();
//		turnier.setzeSieger(begegnungId, sieger1, begegnungList);
	}

	@Test
	void testErstelleWettkampfgruppen(){
		// Teilnehmer einer Gruppe
		List<Wettkaempfer> teilnehmer = Arrays.asList(
			new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 25.0, Optional.of(Farbe.ORANGE), true, false, turnierUUID),
			new Wettkaempfer(2, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3", turnierUUID), 27.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5", turnierUUID), 29.0, Optional.of(Farbe.ORANGE), true, false, turnierUUID),
			new Wettkaempfer(4, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2", turnierUUID), 26.1, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(5, "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 33.0, Optional.of(Farbe.ORANGE), true, false, turnierUUID),
			new Wettkaempfer(6, "Teilnehmer F", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 35.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(7, "Teilnehmer G", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3", turnierUUID), 37.0, Optional.of(Farbe.ORANGE), true, false, turnierUUID),
			new Wettkaempfer(8, "Teilnehmer H", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5", turnierUUID), 39.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(9, "Teilnehmer I", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2", turnierUUID), 41.0, Optional.of(Farbe.ORANGE), true, false, turnierUUID),
			new Wettkaempfer(10, "Teilnehmer J", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 43.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID)
		);


		GewichtsklassenGruppe gewichtsklassengruppe = new GewichtsklassenGruppe(1, Altersklasse.U11, Optional.of(Geschlecht.m), teilnehmer, Optional.empty(), 25.0, 43.0, turnierUUID);

		List<WettkampfGruppe> wettkampfGruppen = algorithmus.erstelleWettkampfGruppen(1, gewichtsklassengruppe, 0);
		assertEquals(1, wettkampfGruppen.size());

		// Ausgabe des Turnierbaums
		for (WettkampfGruppe wettkampfGruppe : wettkampfGruppen) {
//			System.out.println(begegnung.getBegegnungId() + " - Wettkaempfer1: " + begegnung.getWettkaempfer1().map(Wettkaempfer::name) + ", Wettkaempfer2: " + begegnung.getWettkaempfer2().map(Wettkaempfer::name) + " - Ergebnis: " +
//				begegnung.getWertungen());
		}

		// Beispiel für den Fortschritt eines Spiels
		Wettkaempfer sieger1 = teilnehmer.get(1);
		//		Begegnung.BegegnungId begegnungId = begegnungList.get(0).getBegegnungId();
		//		turnier.setzeSieger(begegnungId, sieger1, begegnungList);
	}

}