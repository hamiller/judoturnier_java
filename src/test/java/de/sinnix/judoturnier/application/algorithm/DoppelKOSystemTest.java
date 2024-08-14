package de.sinnix.judoturnier.application.algorithm;

import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Begegnung;
import de.sinnix.judoturnier.model.BegegnungenJeRunde;
import de.sinnix.judoturnier.model.Farbe;
import de.sinnix.judoturnier.model.Geschlecht;
import de.sinnix.judoturnier.model.GewichtsklassenGruppe;
import de.sinnix.judoturnier.model.Verein;
import de.sinnix.judoturnier.model.Wettkaempfer;
import de.sinnix.judoturnier.model.WettkampfGruppe;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.IntUnaryOperator;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class DoppelKOSystemTest {

	private static final Logger         logger      = LogManager.getLogger(DoppelKOSystemTest.class);
	private              DoppelKOSystem algorithmus = new DoppelKOSystem();

	private UUID turnierUUID;

	@BeforeEach
	void setUp() {
		turnierUUID = UUID.randomUUID();
	}

	@Test
	void testErstelleWettkampfgruppen4(){
		// Teilnehmer einer Gruppe
		List<Wettkaempfer> teilnehmer = Arrays.asList(
			new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 25.0, Optional.of(Farbe.ORANGE), true, false, turnierUUID),
			new Wettkaempfer(2, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3", turnierUUID), 27.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5", turnierUUID), 29.0, Optional.of(Farbe.ORANGE), true, false, turnierUUID),
			new Wettkaempfer(4, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2", turnierUUID), 26.1, Optional.of(Farbe.GRUEN), true, true, turnierUUID)
		);
		GewichtsklassenGruppe gewichtsklassengruppe = new GewichtsklassenGruppe(1, Altersklasse.U11, Optional.of(Geschlecht.m), teilnehmer, Optional.empty(), 25.0, 26.1, turnierUUID);

		List<WettkampfGruppe> wettkampfGruppen = algorithmus.erstelleWettkampfGruppen(1, gewichtsklassengruppe, 0);
		// Ausgabe des Turnierbaums
		for (WettkampfGruppe wettkampfGruppe : wettkampfGruppen) {
			int r = 1;
			for (BegegnungenJeRunde begegnungenJeRunde : wettkampfGruppe.alleRundenBegegnungen()) {
				logger.debug("Runde {}", r);
				for (Begegnung begegnung : begegnungenJeRunde.begegnungenJeRunde()) {
					logger.debug("{} - Wettkaempfer1: {}, Wettkaempfer2: {}", begegnung.getBegegnungId(),  begegnung.getWettkaempfer1().map(Wettkaempfer::name), begegnung.getWettkaempfer2().map(Wettkaempfer::name));
				}
				r++;
			}
		}
		assertEquals(1, wettkampfGruppen.size());
		assertEquals(2, wettkampfGruppen.get(0).alleRundenBegegnungen().size()); // Tiefe des Turniers, also wieviele Runden bis Finale
		assertEquals(2, wettkampfGruppen.get(0).alleRundenBegegnungen().get(0).begegnungenJeRunde().size()); // Runde 1 sind 2 Begegnungen in der Siegerrunde keine Verliererrunde
		assertEquals(1, wettkampfGruppen.get(0).alleRundenBegegnungen().get(1).begegnungenJeRunde().size()); // Runde 2 sind 1 Begegnungen in der Siegerrunde

		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1), wettkampfGruppen.get(0).alleRundenBegegnungen().get(0).begegnungenJeRunde().get(0).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 2), wettkampfGruppen.get(0).alleRundenBegegnungen().get(0).begegnungenJeRunde().get(1).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 2, 1), wettkampfGruppen.get(0).alleRundenBegegnungen().get(1).begegnungenJeRunde().get(0).getBegegnungId());
	}

	@Test
	void testErstelleWettkampfgruppen6(){
		// Teilnehmer einer Gruppe
		List<Wettkaempfer> teilnehmer = Arrays.asList(
			new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 25.0, Optional.of(Farbe.ORANGE), true, false, turnierUUID),
			new Wettkaempfer(2, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3", turnierUUID), 27.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5", turnierUUID), 29.0, Optional.of(Farbe.ORANGE), true, false, turnierUUID),
			new Wettkaempfer(4, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2", turnierUUID), 26.1, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(5, "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 33.0, Optional.of(Farbe.ORANGE), true, false, turnierUUID),
			new Wettkaempfer(6, "Teilnehmer F", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 35.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID)
		);


		GewichtsklassenGruppe gewichtsklassengruppe = new GewichtsklassenGruppe(1, Altersklasse.U11, Optional.of(Geschlecht.m), teilnehmer, Optional.empty(), 25.0, 43.0, turnierUUID);

		List<WettkampfGruppe> wettkampfGruppen = algorithmus.erstelleWettkampfGruppen(1, gewichtsklassengruppe, 0);

		// Ausgabe des Turnierbaums
		for (WettkampfGruppe wettkampfGruppe : wettkampfGruppen) {
			int r = 1;
			for (BegegnungenJeRunde begegnungenJeRunde : wettkampfGruppe.alleRundenBegegnungen()) {
				logger.debug("Runde {}", r);
				for (Begegnung begegnung : begegnungenJeRunde.begegnungenJeRunde()) {
					logger.debug("{} - Wettkaempfer1: {}, Wettkaempfer2: {}", begegnung.getBegegnungId(),  begegnung.getWettkaempfer1().map(Wettkaempfer::name), begegnung.getWettkaempfer2().map(Wettkaempfer::name));
				}
				r++;
			}
		}

		assertEquals(1, wettkampfGruppen.size());
		assertEquals(3, wettkampfGruppen.get(0).alleRundenBegegnungen().size()); // Tiefe des Turniers, also wieviele Runden bis Finale

		// Runde 1 sind 4 Begegnungen in der Siegerrunde plus 2 Begegnung in der Verliererrunde
		assertEquals(4+2, wettkampfGruppen.get(0).alleRundenBegegnungen().get(0).begegnungenJeRunde().size());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1), wettkampfGruppen.get(0).alleRundenBegegnungen().get(0).begegnungenJeRunde().get(0).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 2), wettkampfGruppen.get(0).alleRundenBegegnungen().get(0).begegnungenJeRunde().get(1).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 3), wettkampfGruppen.get(0).alleRundenBegegnungen().get(0).begegnungenJeRunde().get(2).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 4), wettkampfGruppen.get(0).alleRundenBegegnungen().get(0).begegnungenJeRunde().get(3).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 1, 1), wettkampfGruppen.get(0).alleRundenBegegnungen().get(0).begegnungenJeRunde().get(4).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 1, 2), wettkampfGruppen.get(0).alleRundenBegegnungen().get(0).begegnungenJeRunde().get(5).getBegegnungId());

		// Runde 2 sind 2 Begegnungen (Halbfinale) in der Siegerrunde plus 2 Begegnung in der Verliererrunde
		assertEquals(2+2, wettkampfGruppen.get(0).alleRundenBegegnungen().get(1).begegnungenJeRunde().size());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 2, 1), wettkampfGruppen.get(0).alleRundenBegegnungen().get(1).begegnungenJeRunde().get(0).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 2, 2), wettkampfGruppen.get(0).alleRundenBegegnungen().get(1).begegnungenJeRunde().get(1).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 2, 1), wettkampfGruppen.get(0).alleRundenBegegnungen().get(1).begegnungenJeRunde().get(2).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 2, 2), wettkampfGruppen.get(0).alleRundenBegegnungen().get(1).begegnungenJeRunde().get(3).getBegegnungId());

		// Runde 3 sind 1 Begegnungen (Finale)
		assertEquals(1, wettkampfGruppen.get(0).alleRundenBegegnungen().get(2).begegnungenJeRunde().size());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 3, 1), wettkampfGruppen.get(0).alleRundenBegegnungen().get(2).begegnungenJeRunde().get(0).getBegegnungId());
	}

	@Test
	void testErstelleWettkampfgruppen8(){
		// Teilnehmer einer Gruppe
		List<Wettkaempfer> teilnehmer = Arrays.asList(
			new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 25.0, Optional.of(Farbe.ORANGE), true, false, turnierUUID),
			new Wettkaempfer(2, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3", turnierUUID), 27.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5", turnierUUID), 29.0, Optional.of(Farbe.ORANGE), true, false, turnierUUID),
			new Wettkaempfer(4, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2", turnierUUID), 26.1, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(5, "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 33.0, Optional.of(Farbe.ORANGE), true, false, turnierUUID),
			new Wettkaempfer(6, "Teilnehmer F", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 35.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(7, "Teilnehmer G", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3", turnierUUID), 37.0, Optional.of(Farbe.ORANGE), true, false, turnierUUID),
			new Wettkaempfer(8, "Teilnehmer H", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5", turnierUUID), 39.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID)
		);


		GewichtsklassenGruppe gewichtsklassengruppe = new GewichtsklassenGruppe(1, Altersklasse.U11, Optional.of(Geschlecht.m), teilnehmer, Optional.empty(), 25.0, 43.0, turnierUUID);

		List<WettkampfGruppe> wettkampfGruppen = algorithmus.erstelleWettkampfGruppen(1, gewichtsklassengruppe, 0);

		// Ausgabe des Turnierbaums
		for (WettkampfGruppe wettkampfGruppe : wettkampfGruppen) {
			int r = 1;
			for (BegegnungenJeRunde begegnungenJeRunde : wettkampfGruppe.alleRundenBegegnungen()) {
				logger.debug("Runde {}", r);
				for (Begegnung begegnung : begegnungenJeRunde.begegnungenJeRunde()) {
					logger.debug("{} - Wettkaempfer1: {}, Wettkaempfer2: {}", begegnung.getBegegnungId(),  begegnung.getWettkaempfer1().map(Wettkaempfer::name), begegnung.getWettkaempfer2().map(Wettkaempfer::name));
				}
				r++;
			}
		}

		assertEquals(1, wettkampfGruppen.size());
		assertEquals(3, wettkampfGruppen.get(0).alleRundenBegegnungen().size()); // Tiefe des Turniers, also wieviele Runden bis Finale

		// Runde 1 sind 4 Begegnungen in der Siegerrunde plus 2 Begegnung in der Verliererrunde
		assertEquals(4+2, wettkampfGruppen.get(0).alleRundenBegegnungen().get(0).begegnungenJeRunde().size());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1), wettkampfGruppen.get(0).alleRundenBegegnungen().get(0).begegnungenJeRunde().get(0).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 2), wettkampfGruppen.get(0).alleRundenBegegnungen().get(0).begegnungenJeRunde().get(1).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 3), wettkampfGruppen.get(0).alleRundenBegegnungen().get(0).begegnungenJeRunde().get(2).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 4), wettkampfGruppen.get(0).alleRundenBegegnungen().get(0).begegnungenJeRunde().get(3).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 1, 1), wettkampfGruppen.get(0).alleRundenBegegnungen().get(0).begegnungenJeRunde().get(4).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 1, 2), wettkampfGruppen.get(0).alleRundenBegegnungen().get(0).begegnungenJeRunde().get(5).getBegegnungId());

		// Runde 2 sind 2 Begegnungen (Halbfinale) in der Siegerrunde plus 2 Begegnung in der Verliererrunde
		assertEquals(2+2, wettkampfGruppen.get(0).alleRundenBegegnungen().get(1).begegnungenJeRunde().size());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 2, 1), wettkampfGruppen.get(0).alleRundenBegegnungen().get(1).begegnungenJeRunde().get(0).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 2, 2), wettkampfGruppen.get(0).alleRundenBegegnungen().get(1).begegnungenJeRunde().get(1).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 2, 1), wettkampfGruppen.get(0).alleRundenBegegnungen().get(1).begegnungenJeRunde().get(2).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 2, 2), wettkampfGruppen.get(0).alleRundenBegegnungen().get(1).begegnungenJeRunde().get(3).getBegegnungId());

		// Runde 3 sind 1 Begegnungen (Finale)
		assertEquals(1, wettkampfGruppen.get(0).alleRundenBegegnungen().get(2).begegnungenJeRunde().size());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 3, 1), wettkampfGruppen.get(0).alleRundenBegegnungen().get(2).begegnungenJeRunde().get(0).getBegegnungId());
	}

	@Test
	void testErstelleWettkampfgruppen10(){
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

		// Ausgabe des Turnierbaums
		for (WettkampfGruppe wettkampfGruppe : wettkampfGruppen) {
			int r = 1;
			for (BegegnungenJeRunde begegnungenJeRunde : wettkampfGruppe.alleRundenBegegnungen()) {
				logger.debug("Runde {}", r);
				for (Begegnung begegnung : begegnungenJeRunde.begegnungenJeRunde()) {
					logger.debug("{} - Wettkaempfer1: {}, Wettkaempfer2: {}", begegnung.getBegegnungId(),  begegnung.getWettkaempfer1().map(Wettkaempfer::name), begegnung.getWettkaempfer2().map(Wettkaempfer::name));
				}
				r++;
			}
		}

		assertEquals(1, wettkampfGruppen.size());
		assertEquals(4, wettkampfGruppen.get(0).alleRundenBegegnungen().size()); // Tiefe des Turniers, also wieviele Runden bis Finale

		// Runde 1 sind 8 Begegnungen plus 4 Dummy-Begegnung für die Trostrunde
		assertEquals(8+4, wettkampfGruppen.get(0).alleRundenBegegnungen().get(0).begegnungenJeRunde().size());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1), wettkampfGruppen.get(0).alleRundenBegegnungen().get(0).begegnungenJeRunde().get(0).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 2), wettkampfGruppen.get(0).alleRundenBegegnungen().get(0).begegnungenJeRunde().get(1).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 3), wettkampfGruppen.get(0).alleRundenBegegnungen().get(0).begegnungenJeRunde().get(2).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 4), wettkampfGruppen.get(0).alleRundenBegegnungen().get(0).begegnungenJeRunde().get(3).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 5), wettkampfGruppen.get(0).alleRundenBegegnungen().get(0).begegnungenJeRunde().get(4).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 6), wettkampfGruppen.get(0).alleRundenBegegnungen().get(0).begegnungenJeRunde().get(5).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 7), wettkampfGruppen.get(0).alleRundenBegegnungen().get(0).begegnungenJeRunde().get(6).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 8), wettkampfGruppen.get(0).alleRundenBegegnungen().get(0).begegnungenJeRunde().get(7).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 1, 1), wettkampfGruppen.get(0).alleRundenBegegnungen().get(0).begegnungenJeRunde().get(8).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 1, 2), wettkampfGruppen.get(0).alleRundenBegegnungen().get(0).begegnungenJeRunde().get(9).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 1, 3), wettkampfGruppen.get(0).alleRundenBegegnungen().get(0).begegnungenJeRunde().get(10).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 1, 4), wettkampfGruppen.get(0).alleRundenBegegnungen().get(0).begegnungenJeRunde().get(11).getBegegnungId());

		// Runde 2 sind 4 Begegnungen plus 4 Dummy-Begegnung für die Trostrunde
		assertEquals(4+4, wettkampfGruppen.get(0).alleRundenBegegnungen().get(1).begegnungenJeRunde().size());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 2, 1), wettkampfGruppen.get(0).alleRundenBegegnungen().get(1).begegnungenJeRunde().get(0).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 2, 2), wettkampfGruppen.get(0).alleRundenBegegnungen().get(1).begegnungenJeRunde().get(1).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 2, 3), wettkampfGruppen.get(0).alleRundenBegegnungen().get(1).begegnungenJeRunde().get(2).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 2, 4), wettkampfGruppen.get(0).alleRundenBegegnungen().get(1).begegnungenJeRunde().get(3).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 2, 1), wettkampfGruppen.get(0).alleRundenBegegnungen().get(1).begegnungenJeRunde().get(4).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 2, 2), wettkampfGruppen.get(0).alleRundenBegegnungen().get(1).begegnungenJeRunde().get(5).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 2, 3), wettkampfGruppen.get(0).alleRundenBegegnungen().get(1).begegnungenJeRunde().get(6).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 2, 4), wettkampfGruppen.get(0).alleRundenBegegnungen().get(1).begegnungenJeRunde().get(7).getBegegnungId());

		// Runde 3 sind 2 Begegnungen (Halbfinale) plus 2 Dummy-Begegnung für die Trostrunde
		assertEquals(2+2, wettkampfGruppen.get(0).alleRundenBegegnungen().get(2).begegnungenJeRunde().size());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 3, 1), wettkampfGruppen.get(0).alleRundenBegegnungen().get(2).begegnungenJeRunde().get(0).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 3, 2), wettkampfGruppen.get(0).alleRundenBegegnungen().get(2).begegnungenJeRunde().get(1).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 3, 1), wettkampfGruppen.get(0).alleRundenBegegnungen().get(2).begegnungenJeRunde().get(2).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 3, 2), wettkampfGruppen.get(0).alleRundenBegegnungen().get(2).begegnungenJeRunde().get(3).getBegegnungId());

		// Runde 4 sind 1 Begegnungen (Finale) plus 2 Dummy-Begegnung für die Trostrunde
		assertEquals(1+2, wettkampfGruppen.get(0).alleRundenBegegnungen().get(3).begegnungenJeRunde().size());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 4, 1), wettkampfGruppen.get(0).alleRundenBegegnungen().get(3).begegnungenJeRunde().get(0).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 4, 1), wettkampfGruppen.get(0).alleRundenBegegnungen().get(3).begegnungenJeRunde().get(1).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 4, 2), wettkampfGruppen.get(0).alleRundenBegegnungen().get(3).begegnungenJeRunde().get(2).getBegegnungId());
	}

	@Test
	void testErstelleWettkampfgruppen16(){
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
			new Wettkaempfer(10, "Teilnehmer J", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 43.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(11, "Teilnehmer K", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 43.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(12, "Teilnehmer L", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 43.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(13, "Teilnehmer M", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 43.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(14, "Teilnehmer N", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 43.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(15, "Teilnehmer O", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 43.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(16, "Teilnehmer P", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 43.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID)
		);


		GewichtsklassenGruppe gewichtsklassengruppe = new GewichtsklassenGruppe(1, Altersklasse.U11, Optional.of(Geschlecht.m), teilnehmer, Optional.empty(), 25.0, 43.0, turnierUUID);

		List<WettkampfGruppe> wettkampfGruppen = algorithmus.erstelleWettkampfGruppen(1, gewichtsklassengruppe, 0);

		// Ausgabe des Turnierbaums
		for (WettkampfGruppe wettkampfGruppe : wettkampfGruppen) {
			int r = 1;
			for (BegegnungenJeRunde begegnungenJeRunde : wettkampfGruppe.alleRundenBegegnungen()) {
				logger.debug("Runde {}", r);
				for (Begegnung begegnung : begegnungenJeRunde.begegnungenJeRunde()) {
					logger.debug("{} - Wettkaempfer1: {}, Wettkaempfer2: {}", begegnung.getBegegnungId(),  begegnung.getWettkaempfer1().map(Wettkaempfer::name), begegnung.getWettkaempfer2().map(Wettkaempfer::name));
				}
				r++;
			}
		}

		assertEquals(1, wettkampfGruppen.size());
		assertEquals(4, wettkampfGruppen.get(0).alleRundenBegegnungen().size()); // Tiefe des Turniers, also wieviele Runden bis Finale

		// Runde 1 sind 8 Begegnungen plus 4 Dummy-Begegnung für die Trostrunde
		assertEquals(8+4, wettkampfGruppen.get(0).alleRundenBegegnungen().get(0).begegnungenJeRunde().size());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1), wettkampfGruppen.get(0).alleRundenBegegnungen().get(0).begegnungenJeRunde().get(0).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 2), wettkampfGruppen.get(0).alleRundenBegegnungen().get(0).begegnungenJeRunde().get(1).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 3), wettkampfGruppen.get(0).alleRundenBegegnungen().get(0).begegnungenJeRunde().get(2).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 4), wettkampfGruppen.get(0).alleRundenBegegnungen().get(0).begegnungenJeRunde().get(3).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 5), wettkampfGruppen.get(0).alleRundenBegegnungen().get(0).begegnungenJeRunde().get(4).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 6), wettkampfGruppen.get(0).alleRundenBegegnungen().get(0).begegnungenJeRunde().get(5).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 7), wettkampfGruppen.get(0).alleRundenBegegnungen().get(0).begegnungenJeRunde().get(6).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 8), wettkampfGruppen.get(0).alleRundenBegegnungen().get(0).begegnungenJeRunde().get(7).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 1, 1), wettkampfGruppen.get(0).alleRundenBegegnungen().get(0).begegnungenJeRunde().get(8).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 1, 2), wettkampfGruppen.get(0).alleRundenBegegnungen().get(0).begegnungenJeRunde().get(9).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 1, 3), wettkampfGruppen.get(0).alleRundenBegegnungen().get(0).begegnungenJeRunde().get(10).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 1, 4), wettkampfGruppen.get(0).alleRundenBegegnungen().get(0).begegnungenJeRunde().get(11).getBegegnungId());

		// Runde 2 sind 4 Begegnungen plus 4 Dummy-Begegnung für die Trostrunde
		assertEquals(4+4, wettkampfGruppen.get(0).alleRundenBegegnungen().get(1).begegnungenJeRunde().size());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 2, 1), wettkampfGruppen.get(0).alleRundenBegegnungen().get(1).begegnungenJeRunde().get(0).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 2, 2), wettkampfGruppen.get(0).alleRundenBegegnungen().get(1).begegnungenJeRunde().get(1).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 2, 3), wettkampfGruppen.get(0).alleRundenBegegnungen().get(1).begegnungenJeRunde().get(2).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 2, 4), wettkampfGruppen.get(0).alleRundenBegegnungen().get(1).begegnungenJeRunde().get(3).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 2, 1), wettkampfGruppen.get(0).alleRundenBegegnungen().get(1).begegnungenJeRunde().get(4).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 2, 2), wettkampfGruppen.get(0).alleRundenBegegnungen().get(1).begegnungenJeRunde().get(5).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 2, 3), wettkampfGruppen.get(0).alleRundenBegegnungen().get(1).begegnungenJeRunde().get(6).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 2, 4), wettkampfGruppen.get(0).alleRundenBegegnungen().get(1).begegnungenJeRunde().get(7).getBegegnungId());

		// Runde 3 sind 2 Begegnungen (Halbfinale) plus 2 Dummy-Begegnung für die Trostrunde
		assertEquals(2+2, wettkampfGruppen.get(0).alleRundenBegegnungen().get(2).begegnungenJeRunde().size());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 3, 1), wettkampfGruppen.get(0).alleRundenBegegnungen().get(2).begegnungenJeRunde().get(0).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 3, 2), wettkampfGruppen.get(0).alleRundenBegegnungen().get(2).begegnungenJeRunde().get(1).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 3, 1), wettkampfGruppen.get(0).alleRundenBegegnungen().get(2).begegnungenJeRunde().get(2).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 3, 2), wettkampfGruppen.get(0).alleRundenBegegnungen().get(2).begegnungenJeRunde().get(3).getBegegnungId());

		// Runde 4 sind 1 Begegnungen (Finale) plus 2 Dummy-Begegnung für die Trostrunde
		assertEquals(1+2, wettkampfGruppen.get(0).alleRundenBegegnungen().get(3).begegnungenJeRunde().size());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 4, 1), wettkampfGruppen.get(0).alleRundenBegegnungen().get(3).begegnungenJeRunde().get(0).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 4, 1), wettkampfGruppen.get(0).alleRundenBegegnungen().get(3).begegnungenJeRunde().get(1).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 4, 2), wettkampfGruppen.get(0).alleRundenBegegnungen().get(3).begegnungenJeRunde().get(2).getBegegnungId());
	}

	/**
	 * // TODO: Implementieren
	 * Sonderfall, weil Teilnehmer der Trostrunde wieder in den Kampf um Platz 1 einsteigen
	 */
	@Test
	@Disabled
	void testErstelleWettkampfgruppen32(){
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
			new Wettkaempfer(10, "Teilnehmer J", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 43.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(11, "Teilnehmer K", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 43.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(12, "Teilnehmer L", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 43.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(13, "Teilnehmer M", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 43.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(14, "Teilnehmer N", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 43.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(15, "Teilnehmer O", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 43.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(16, "Teilnehmer P", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 43.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID),

			new Wettkaempfer(17, "Teilnehmer Q", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 25.0, Optional.of(Farbe.ORANGE), true, false, turnierUUID),
			new Wettkaempfer(18, "Teilnehmer R", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3", turnierUUID), 27.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(19, "Teilnehmer S", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5", turnierUUID), 29.0, Optional.of(Farbe.ORANGE), true, false, turnierUUID),
			new Wettkaempfer(20, "Teilnehmer T", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2", turnierUUID), 26.1, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(21, "Teilnehmer U", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 33.0, Optional.of(Farbe.ORANGE), true, false, turnierUUID),
			new Wettkaempfer(22, "Teilnehmer V", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 35.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(23, "Teilnehmer W", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3", turnierUUID), 37.0, Optional.of(Farbe.ORANGE), true, false, turnierUUID),
			new Wettkaempfer(24, "Teilnehmer X", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5", turnierUUID), 39.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(25, "Teilnehmer Y", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2", turnierUUID), 41.0, Optional.of(Farbe.ORANGE), true, false, turnierUUID),
			new Wettkaempfer(26, "Teilnehmer Z", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 43.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(27, "Teilnehmer a", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 43.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(28, "Teilnehmer b", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 43.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(29, "Teilnehmer c", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 43.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(30, "Teilnehmer d", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 43.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(31, "Teilnehmer e", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 43.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(32, "Teilnehmer f", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 43.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID)
		);


		GewichtsklassenGruppe gewichtsklassengruppe = new GewichtsklassenGruppe(1, Altersklasse.U11, Optional.of(Geschlecht.m), teilnehmer, Optional.empty(), 25.0, 43.0, turnierUUID);

		List<WettkampfGruppe> wettkampfGruppen = algorithmus.erstelleWettkampfGruppen(1, gewichtsklassengruppe, 0);

		// Ausgabe des Turnierbaums
		for (WettkampfGruppe wettkampfGruppe : wettkampfGruppen) {
			int r = 1;
			for (BegegnungenJeRunde begegnungenJeRunde : wettkampfGruppe.alleRundenBegegnungen()) {
				logger.debug("Runde {}", r);
				for (Begegnung begegnung : begegnungenJeRunde.begegnungenJeRunde()) {
					logger.debug("{} - Wettkaempfer1: {}, Wettkaempfer2: {}", begegnung.getBegegnungId(),  begegnung.getWettkaempfer1().map(Wettkaempfer::name), begegnung.getWettkaempfer2().map(Wettkaempfer::name));
				}
				r++;
			}
		}

		assertEquals(1, wettkampfGruppen.size());
		assertEquals(6, wettkampfGruppen.get(0).alleRundenBegegnungen().size()); // Tiefe des Turniers, also wieviele Runden bis Finale
		assertEquals(24, wettkampfGruppen.get(0).alleRundenBegegnungen().get(0).begegnungenJeRunde().size()); // Runde 1 sind 16 Begegnungen plus 8 Dummy-Begegnung für die Trostrunde
		assertEquals(16, wettkampfGruppen.get(0).alleRundenBegegnungen().get(1).begegnungenJeRunde().size()); // Runde 2 sind 8 Begegnungen plus 8 Dummy-Begegnung für die Trostrunde
		assertEquals(8, wettkampfGruppen.get(0).alleRundenBegegnungen().get(2).begegnungenJeRunde().size());  // Runde 3 sind 4 Begegnungen plus 4 Dummy-Begegnung für die Trostrunde
		assertEquals(6, wettkampfGruppen.get(0).alleRundenBegegnungen().get(3).begegnungenJeRunde().size());  // Runde 4 sind 2 Begegnungen plus 4 Dummy-Begegnung für die Trostrunde
		assertEquals(4, wettkampfGruppen.get(0).alleRundenBegegnungen().get(4).begegnungenJeRunde().size());  // Runde 5 sind 2 Begegnungen plus 2 Dummy-Begegnung für die Trostrunde -> hier kommt eine weitere Runde hinzu, weil die Sieger der Trostrunde in das Viertelfinale aufgenommen werden
		assertEquals(4, wettkampfGruppen.get(0).alleRundenBegegnungen().get(5).begegnungenJeRunde().size());  // Runde 6 sind 1 Begegnungen plus 2 Dummy-Begegnung für die Trostrunde
	}

	@Test
	public void rechnerTrostrunden() {
		IntUnaryOperator calcGewinnerRunden = n -> (int) Math.ceil(Math.log(n) / Math.log(2));
		IntUnaryOperator calcTrostRunden = n -> (int) (2* (calcGewinnerRunden.applyAsInt(n)-1) -2);

		var teilnehmer = List.of(4, 6, 8, 16, 32);
		teilnehmer.forEach(t -> logger.debug("{} Teilnehmer - Gewinnerrunden: {}, Trostrunden: {}", t, calcGewinnerRunden.applyAsInt(t), calcTrostRunden.applyAsInt(t)));
	}

}