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
import de.sinnix.judoturnier.model.WettkampfGruppeMitBegegnungen;
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
		UUID v1Id = UUID.randomUUID();
		UUID v2Id = UUID.randomUUID();
		UUID v3Id = UUID.randomUUID();
		UUID v4Id = UUID.randomUUID();

		UUID wk1Id = UUID.randomUUID();
		UUID wk2Id = UUID.randomUUID();
		UUID wk3Id = UUID.randomUUID();
		UUID wk4Id = UUID.randomUUID();

		// Teilnehmer einer Gruppe
		List<Wettkaempfer> teilnehmer = Arrays.asList(
			new Wettkaempfer(wk1Id, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(v1Id, "Verein1", turnierUUID), 25.0, Optional.of(Farbe.ORANGE), true, false, turnierUUID),
			new Wettkaempfer(wk2Id, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(v3Id, "Verein3", turnierUUID), 27.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(wk3Id, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(v4Id, "Verein4", turnierUUID), 29.0, Optional.of(Farbe.ORANGE), true, false, turnierUUID),
			new Wettkaempfer(wk4Id, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(v2Id, "Verein2", turnierUUID), 26.1, Optional.of(Farbe.GRUEN), true, true, turnierUUID)
		);
		GewichtsklassenGruppe gewichtsklassengruppe = new GewichtsklassenGruppe(UUID.randomUUID(), Altersklasse.U11, Optional.of(Geschlecht.m), teilnehmer, Optional.empty(), 25.0, 26.1, turnierUUID);

		WettkampfGruppeMitBegegnungen wettkampfGruppe = algorithmus.erstelleWettkampfGruppe(gewichtsklassengruppe);
		// Ausgabe des Turnierbaums
		int r = 1;
		for (BegegnungenJeRunde begegnungenJeRunde : wettkampfGruppe.alleRundenBegegnungen()) {
			logger.debug("Runde {}", r);
			for (Begegnung begegnung : begegnungenJeRunde.begegnungenJeRunde()) {
				logger.debug("{} - Wettkaempfer1: {}, Wettkaempfer2: {}", begegnung.getBegegnungId(),  begegnung.getWettkaempfer1().map(Wettkaempfer::name), begegnung.getWettkaempfer2().map(Wettkaempfer::name));
			}
			r++;
		}

		assertEquals(2, wettkampfGruppe.alleRundenBegegnungen().size()); // Tiefe des Turniers, also wieviele Runden bis Finale
		assertEquals(2, wettkampfGruppe.alleRundenBegegnungen().get(0).begegnungenJeRunde().size()); // Runde 1 sind 2 Begegnungen in der Siegerrunde keine Verliererrunde
		assertEquals(1, wettkampfGruppe.alleRundenBegegnungen().get(1).begegnungenJeRunde().size()); // Runde 2 sind 1 Begegnungen in der Siegerrunde

		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1), wettkampfGruppe.alleRundenBegegnungen().get(0).begegnungenJeRunde().get(0).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 2), wettkampfGruppe.alleRundenBegegnungen().get(0).begegnungenJeRunde().get(1).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 2, 1), wettkampfGruppe.alleRundenBegegnungen().get(1).begegnungenJeRunde().get(0).getBegegnungId());
	}

	@Test
	void testErstelleWettkampfgruppen6(){
		// Teilnehmer einer Gruppe
		List<Wettkaempfer> teilnehmer = Arrays.asList(
			new Wettkaempfer(UUID.randomUUID(), "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein1", turnierUUID), 25.0, Optional.of(Farbe.ORANGE), true, false, turnierUUID),
			new Wettkaempfer(UUID.randomUUID(), "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein3", turnierUUID), 27.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(UUID.randomUUID(), "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein5", turnierUUID), 29.0, Optional.of(Farbe.ORANGE), true, false, turnierUUID),
			new Wettkaempfer(UUID.randomUUID(), "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein2", turnierUUID), 26.1, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(UUID.randomUUID(), "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein4", turnierUUID), 33.0, Optional.of(Farbe.ORANGE), true, false, turnierUUID),
			new Wettkaempfer(UUID.randomUUID(), "Teilnehmer F", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein1", turnierUUID), 35.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID)
		);


		GewichtsklassenGruppe gewichtsklassengruppe = new GewichtsklassenGruppe(UUID.randomUUID(), Altersklasse.U11, Optional.of(Geschlecht.m), teilnehmer, Optional.empty(), 25.0, 43.0, turnierUUID);

		WettkampfGruppeMitBegegnungen wettkampfGruppe = algorithmus.erstelleWettkampfGruppe(gewichtsklassengruppe);

		// Ausgabe des Turnierbaums
		int r = 1;
		for (BegegnungenJeRunde begegnungenJeRunde : wettkampfGruppe.alleRundenBegegnungen()) {
			logger.debug("Runde {}", r);
			for (Begegnung begegnung : begegnungenJeRunde.begegnungenJeRunde()) {
				logger.debug("{} - Wettkaempfer1: {}, Wettkaempfer2: {}", begegnung.getBegegnungId(),  begegnung.getWettkaempfer1().map(Wettkaempfer::name), begegnung.getWettkaempfer2().map(Wettkaempfer::name));
			}
			r++;
		}

		assertEquals(3, wettkampfGruppe.alleRundenBegegnungen().size()); // Tiefe des Turniers, also wieviele Runden bis Finale

		// Runde 1 sind 4 Begegnungen in der Siegerrunde plus 2 Begegnung in der Verliererrunde
		assertEquals(4+2, wettkampfGruppe.alleRundenBegegnungen().get(0).begegnungenJeRunde().size());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1), wettkampfGruppe.alleRundenBegegnungen().get(0).begegnungenJeRunde().get(0).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 2), wettkampfGruppe.alleRundenBegegnungen().get(0).begegnungenJeRunde().get(1).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 3), wettkampfGruppe.alleRundenBegegnungen().get(0).begegnungenJeRunde().get(2).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 4), wettkampfGruppe.alleRundenBegegnungen().get(0).begegnungenJeRunde().get(3).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 1, 1), wettkampfGruppe.alleRundenBegegnungen().get(0).begegnungenJeRunde().get(4).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 1, 2), wettkampfGruppe.alleRundenBegegnungen().get(0).begegnungenJeRunde().get(5).getBegegnungId());

		// Runde 2 sind 2 Begegnungen (Halbfinale) in der Siegerrunde plus 2 Begegnung in der Verliererrunde
		assertEquals(2+2, wettkampfGruppe.alleRundenBegegnungen().get(1).begegnungenJeRunde().size());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 2, 1), wettkampfGruppe.alleRundenBegegnungen().get(1).begegnungenJeRunde().get(0).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 2, 2), wettkampfGruppe.alleRundenBegegnungen().get(1).begegnungenJeRunde().get(1).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 2, 1), wettkampfGruppe.alleRundenBegegnungen().get(1).begegnungenJeRunde().get(2).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 2, 2), wettkampfGruppe.alleRundenBegegnungen().get(1).begegnungenJeRunde().get(3).getBegegnungId());

		// Runde 3 sind 1 Begegnungen (Finale)
		assertEquals(1, wettkampfGruppe.alleRundenBegegnungen().get(2).begegnungenJeRunde().size());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 3, 1), wettkampfGruppe.alleRundenBegegnungen().get(2).begegnungenJeRunde().get(0).getBegegnungId());
	}

	@Test
	void testErstelleWettkampfgruppen8(){
		// Teilnehmer einer Gruppe
		List<Wettkaempfer> teilnehmer = Arrays.asList(
			new Wettkaempfer(UUID.randomUUID(), "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein1", turnierUUID), 25.0, Optional.of(Farbe.ORANGE), true, false, turnierUUID),
			new Wettkaempfer(UUID.randomUUID(), "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein3", turnierUUID), 27.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(UUID.randomUUID(), "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein5", turnierUUID), 29.0, Optional.of(Farbe.ORANGE), true, false, turnierUUID),
			new Wettkaempfer(UUID.randomUUID(), "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein2", turnierUUID), 26.1, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(UUID.randomUUID(), "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein4", turnierUUID), 33.0, Optional.of(Farbe.ORANGE), true, false, turnierUUID),
			new Wettkaempfer(UUID.randomUUID(), "Teilnehmer F", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein1", turnierUUID), 35.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(UUID.randomUUID(), "Teilnehmer G", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein3", turnierUUID), 37.0, Optional.of(Farbe.ORANGE), true, false, turnierUUID),
			new Wettkaempfer(UUID.randomUUID(), "Teilnehmer H", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein5", turnierUUID), 39.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID)
		);


		GewichtsklassenGruppe gewichtsklassengruppe = new GewichtsklassenGruppe(UUID.randomUUID(), Altersklasse.U11, Optional.of(Geschlecht.m), teilnehmer, Optional.empty(), 25.0, 43.0, turnierUUID);

		WettkampfGruppeMitBegegnungen wettkampfGruppe = algorithmus.erstelleWettkampfGruppe(gewichtsklassengruppe);

		// Ausgabe des Turnierbaums
		int r = 1;
		for (BegegnungenJeRunde begegnungenJeRunde : wettkampfGruppe.alleRundenBegegnungen()) {
			logger.debug("Runde {}", r);
			for (Begegnung begegnung : begegnungenJeRunde.begegnungenJeRunde()) {
				logger.debug("{} - Wettkaempfer1: {}, Wettkaempfer2: {}", begegnung.getBegegnungId(),  begegnung.getWettkaempfer1().map(Wettkaempfer::name), begegnung.getWettkaempfer2().map(Wettkaempfer::name));
			}
			r++;
		}

		assertEquals(3, wettkampfGruppe.alleRundenBegegnungen().size()); // Tiefe des Turniers, also wieviele Runden bis Finale

		// Runde 1 sind 4 Begegnungen in der Siegerrunde plus 2 Begegnung in der Verliererrunde
		assertEquals(4+2, wettkampfGruppe.alleRundenBegegnungen().get(0).begegnungenJeRunde().size());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1), wettkampfGruppe.alleRundenBegegnungen().get(0).begegnungenJeRunde().get(0).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 2), wettkampfGruppe.alleRundenBegegnungen().get(0).begegnungenJeRunde().get(1).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 3), wettkampfGruppe.alleRundenBegegnungen().get(0).begegnungenJeRunde().get(2).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 4), wettkampfGruppe.alleRundenBegegnungen().get(0).begegnungenJeRunde().get(3).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 1, 1), wettkampfGruppe.alleRundenBegegnungen().get(0).begegnungenJeRunde().get(4).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 1, 2), wettkampfGruppe.alleRundenBegegnungen().get(0).begegnungenJeRunde().get(5).getBegegnungId());

		// Runde 2 sind 2 Begegnungen (Halbfinale) in der Siegerrunde plus 2 Begegnung in der Verliererrunde
		assertEquals(2+2, wettkampfGruppe.alleRundenBegegnungen().get(1).begegnungenJeRunde().size());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 2, 1), wettkampfGruppe.alleRundenBegegnungen().get(1).begegnungenJeRunde().get(0).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 2, 2), wettkampfGruppe.alleRundenBegegnungen().get(1).begegnungenJeRunde().get(1).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 2, 1), wettkampfGruppe.alleRundenBegegnungen().get(1).begegnungenJeRunde().get(2).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 2, 2), wettkampfGruppe.alleRundenBegegnungen().get(1).begegnungenJeRunde().get(3).getBegegnungId());

		// Runde 3 sind 1 Begegnungen (Finale)
		assertEquals(1, wettkampfGruppe.alleRundenBegegnungen().get(2).begegnungenJeRunde().size());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 3, 1), wettkampfGruppe.alleRundenBegegnungen().get(2).begegnungenJeRunde().get(0).getBegegnungId());
	}

	@Test
	void testErstelleWettkampfgruppen10(){
		// Teilnehmer einer Gruppe
		List<Wettkaempfer> teilnehmer = Arrays.asList(
			new Wettkaempfer(UUID.randomUUID(), "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein1", turnierUUID), 25.0, Optional.of(Farbe.ORANGE), true, false, turnierUUID),
			new Wettkaempfer(UUID.randomUUID(), "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein3", turnierUUID), 27.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(UUID.randomUUID(), "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein5", turnierUUID), 29.0, Optional.of(Farbe.ORANGE), true, false, turnierUUID),
			new Wettkaempfer(UUID.randomUUID(), "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein2", turnierUUID), 26.1, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(UUID.randomUUID(), "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein4", turnierUUID), 33.0, Optional.of(Farbe.ORANGE), true, false, turnierUUID),
			new Wettkaempfer(UUID.randomUUID(), "Teilnehmer F", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein1", turnierUUID), 35.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(UUID.randomUUID(), "Teilnehmer G", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein3", turnierUUID), 37.0, Optional.of(Farbe.ORANGE), true, false, turnierUUID),
			new Wettkaempfer(UUID.randomUUID(), "Teilnehmer H", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein5", turnierUUID), 39.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(UUID.randomUUID(), "Teilnehmer I", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein2", turnierUUID), 41.0, Optional.of(Farbe.ORANGE), true, false, turnierUUID),
			new Wettkaempfer(UUID.randomUUID(), "Teilnehmer J", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein4", turnierUUID), 43.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID)
		);


		GewichtsklassenGruppe gewichtsklassengruppe = new GewichtsklassenGruppe(UUID.randomUUID(), Altersklasse.U11, Optional.of(Geschlecht.m), teilnehmer, Optional.empty(), 25.0, 43.0, turnierUUID);

		WettkampfGruppeMitBegegnungen wettkampfGruppe = algorithmus.erstelleWettkampfGruppe(gewichtsklassengruppe);

		// Ausgabe des Turnierbaums
		int r = 1;
		for (BegegnungenJeRunde begegnungenJeRunde : wettkampfGruppe.alleRundenBegegnungen()) {
			logger.debug("Runde {}", r);
			for (Begegnung begegnung : begegnungenJeRunde.begegnungenJeRunde()) {
				logger.debug("{} - Wettkaempfer1: {}, Wettkaempfer2: {}", begegnung.getBegegnungId(),  begegnung.getWettkaempfer1().map(Wettkaempfer::name), begegnung.getWettkaempfer2().map(Wettkaempfer::name));
			}
			r++;
		}

		assertEquals(4, wettkampfGruppe.alleRundenBegegnungen().size()); // Tiefe des Turniers, also wieviele Runden bis Finale

		// Runde 1 sind 8 Begegnungen plus 4 Dummy-Begegnung für die Trostrunde
		assertEquals(8+4, wettkampfGruppe.alleRundenBegegnungen().get(0).begegnungenJeRunde().size());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1), wettkampfGruppe.alleRundenBegegnungen().get(0).begegnungenJeRunde().get(0).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 2), wettkampfGruppe.alleRundenBegegnungen().get(0).begegnungenJeRunde().get(1).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 3), wettkampfGruppe.alleRundenBegegnungen().get(0).begegnungenJeRunde().get(2).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 4), wettkampfGruppe.alleRundenBegegnungen().get(0).begegnungenJeRunde().get(3).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 5), wettkampfGruppe.alleRundenBegegnungen().get(0).begegnungenJeRunde().get(4).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 6), wettkampfGruppe.alleRundenBegegnungen().get(0).begegnungenJeRunde().get(5).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 7), wettkampfGruppe.alleRundenBegegnungen().get(0).begegnungenJeRunde().get(6).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 8), wettkampfGruppe.alleRundenBegegnungen().get(0).begegnungenJeRunde().get(7).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 1, 1), wettkampfGruppe.alleRundenBegegnungen().get(0).begegnungenJeRunde().get(8).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 1, 2), wettkampfGruppe.alleRundenBegegnungen().get(0).begegnungenJeRunde().get(9).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 1, 3), wettkampfGruppe.alleRundenBegegnungen().get(0).begegnungenJeRunde().get(10).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 1, 4), wettkampfGruppe.alleRundenBegegnungen().get(0).begegnungenJeRunde().get(11).getBegegnungId());

		// Runde 2 sind 4 Begegnungen plus 4 Dummy-Begegnung für die Trostrunde
		assertEquals(4+4, wettkampfGruppe.alleRundenBegegnungen().get(1).begegnungenJeRunde().size());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 2, 1), wettkampfGruppe.alleRundenBegegnungen().get(1).begegnungenJeRunde().get(0).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 2, 2), wettkampfGruppe.alleRundenBegegnungen().get(1).begegnungenJeRunde().get(1).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 2, 3), wettkampfGruppe.alleRundenBegegnungen().get(1).begegnungenJeRunde().get(2).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 2, 4), wettkampfGruppe.alleRundenBegegnungen().get(1).begegnungenJeRunde().get(3).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 2, 1), wettkampfGruppe.alleRundenBegegnungen().get(1).begegnungenJeRunde().get(4).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 2, 2), wettkampfGruppe.alleRundenBegegnungen().get(1).begegnungenJeRunde().get(5).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 2, 3), wettkampfGruppe.alleRundenBegegnungen().get(1).begegnungenJeRunde().get(6).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 2, 4), wettkampfGruppe.alleRundenBegegnungen().get(1).begegnungenJeRunde().get(7).getBegegnungId());

		// Runde 3 sind 2 Begegnungen (Halbfinale) plus 2 Dummy-Begegnung für die Trostrunde
		assertEquals(2+2, wettkampfGruppe.alleRundenBegegnungen().get(2).begegnungenJeRunde().size());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 3, 1), wettkampfGruppe.alleRundenBegegnungen().get(2).begegnungenJeRunde().get(0).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 3, 2), wettkampfGruppe.alleRundenBegegnungen().get(2).begegnungenJeRunde().get(1).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 3, 1), wettkampfGruppe.alleRundenBegegnungen().get(2).begegnungenJeRunde().get(2).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 3, 2), wettkampfGruppe.alleRundenBegegnungen().get(2).begegnungenJeRunde().get(3).getBegegnungId());

		// Runde 4 sind 1 Begegnungen (Finale) plus 2 Dummy-Begegnung für die Trostrunde
		assertEquals(1+2, wettkampfGruppe.alleRundenBegegnungen().get(3).begegnungenJeRunde().size());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 4, 1), wettkampfGruppe.alleRundenBegegnungen().get(3).begegnungenJeRunde().get(0).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 4, 1), wettkampfGruppe.alleRundenBegegnungen().get(3).begegnungenJeRunde().get(1).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 4, 2), wettkampfGruppe.alleRundenBegegnungen().get(3).begegnungenJeRunde().get(2).getBegegnungId());
	}

	@Test
	void testErstelleWettkampfgruppen16(){
		// Teilnehmer einer Gruppe
		List<Wettkaempfer> teilnehmer = Arrays.asList(
			new Wettkaempfer(UUID.randomUUID(), "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein1", turnierUUID), 25.0, Optional.of(Farbe.ORANGE), true, false, turnierUUID),
			new Wettkaempfer(UUID.randomUUID(), "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein3", turnierUUID), 27.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(UUID.randomUUID(), "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein5", turnierUUID), 29.0, Optional.of(Farbe.ORANGE), true, false, turnierUUID),
			new Wettkaempfer(UUID.randomUUID(), "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein2", turnierUUID), 26.1, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(UUID.randomUUID(), "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein4", turnierUUID), 33.0, Optional.of(Farbe.ORANGE), true, false, turnierUUID),
			new Wettkaempfer(UUID.randomUUID(), "Teilnehmer F", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein1", turnierUUID), 35.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(UUID.randomUUID(), "Teilnehmer G", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein3", turnierUUID), 37.0, Optional.of(Farbe.ORANGE), true, false, turnierUUID),
			new Wettkaempfer(UUID.randomUUID(), "Teilnehmer H", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein5", turnierUUID), 39.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(UUID.randomUUID(), "Teilnehmer I", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein2", turnierUUID), 41.0, Optional.of(Farbe.ORANGE), true, false, turnierUUID),
			new Wettkaempfer(UUID.randomUUID(), "Teilnehmer J", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein4", turnierUUID), 43.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(UUID.randomUUID(), "Teilnehmer K", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein4", turnierUUID), 43.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(UUID.randomUUID(), "Teilnehmer L", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein4", turnierUUID), 43.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(UUID.randomUUID(), "Teilnehmer M", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein4", turnierUUID), 43.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(UUID.randomUUID(), "Teilnehmer N", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein4", turnierUUID), 43.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(UUID.randomUUID(), "Teilnehmer O", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein4", turnierUUID), 43.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(UUID.randomUUID(), "Teilnehmer P", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein4", turnierUUID), 43.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID)
		);


		GewichtsklassenGruppe gewichtsklassengruppe = new GewichtsklassenGruppe(UUID.randomUUID(), Altersklasse.U11, Optional.of(Geschlecht.m), teilnehmer, Optional.empty(), 25.0, 43.0, turnierUUID);

		WettkampfGruppeMitBegegnungen wettkampfGruppe = algorithmus.erstelleWettkampfGruppe(gewichtsklassengruppe);

		// Ausgabe des Turnierbaums
		int r = 1;
		for (BegegnungenJeRunde begegnungenJeRunde : wettkampfGruppe.alleRundenBegegnungen()) {
			logger.debug("Runde {}", r);
			for (Begegnung begegnung : begegnungenJeRunde.begegnungenJeRunde()) {
				logger.debug("{} - Wettkaempfer1: {}, Wettkaempfer2: {}", begegnung.getBegegnungId(),  begegnung.getWettkaempfer1().map(Wettkaempfer::name), begegnung.getWettkaempfer2().map(Wettkaempfer::name));
			}
			r++;
		}

		assertEquals(4, wettkampfGruppe.alleRundenBegegnungen().size()); // Tiefe des Turniers, also wieviele Runden bis Finale

		// Runde 1 sind 8 Begegnungen plus 4 Dummy-Begegnung für die Trostrunde
		assertEquals(8+4, wettkampfGruppe.alleRundenBegegnungen().get(0).begegnungenJeRunde().size());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1), wettkampfGruppe.alleRundenBegegnungen().get(0).begegnungenJeRunde().get(0).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 2), wettkampfGruppe.alleRundenBegegnungen().get(0).begegnungenJeRunde().get(1).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 3), wettkampfGruppe.alleRundenBegegnungen().get(0).begegnungenJeRunde().get(2).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 4), wettkampfGruppe.alleRundenBegegnungen().get(0).begegnungenJeRunde().get(3).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 5), wettkampfGruppe.alleRundenBegegnungen().get(0).begegnungenJeRunde().get(4).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 6), wettkampfGruppe.alleRundenBegegnungen().get(0).begegnungenJeRunde().get(5).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 7), wettkampfGruppe.alleRundenBegegnungen().get(0).begegnungenJeRunde().get(6).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 8), wettkampfGruppe.alleRundenBegegnungen().get(0).begegnungenJeRunde().get(7).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 1, 1), wettkampfGruppe.alleRundenBegegnungen().get(0).begegnungenJeRunde().get(8).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 1, 2), wettkampfGruppe.alleRundenBegegnungen().get(0).begegnungenJeRunde().get(9).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 1, 3), wettkampfGruppe.alleRundenBegegnungen().get(0).begegnungenJeRunde().get(10).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 1, 4), wettkampfGruppe.alleRundenBegegnungen().get(0).begegnungenJeRunde().get(11).getBegegnungId());

		// Runde 2 sind 4 Begegnungen plus 4 Dummy-Begegnung für die Trostrunde
		assertEquals(4+4, wettkampfGruppe.alleRundenBegegnungen().get(1).begegnungenJeRunde().size());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 2, 1), wettkampfGruppe.alleRundenBegegnungen().get(1).begegnungenJeRunde().get(0).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 2, 2), wettkampfGruppe.alleRundenBegegnungen().get(1).begegnungenJeRunde().get(1).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 2, 3), wettkampfGruppe.alleRundenBegegnungen().get(1).begegnungenJeRunde().get(2).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 2, 4), wettkampfGruppe.alleRundenBegegnungen().get(1).begegnungenJeRunde().get(3).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 2, 1), wettkampfGruppe.alleRundenBegegnungen().get(1).begegnungenJeRunde().get(4).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 2, 2), wettkampfGruppe.alleRundenBegegnungen().get(1).begegnungenJeRunde().get(5).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 2, 3), wettkampfGruppe.alleRundenBegegnungen().get(1).begegnungenJeRunde().get(6).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 2, 4), wettkampfGruppe.alleRundenBegegnungen().get(1).begegnungenJeRunde().get(7).getBegegnungId());

		// Runde 3 sind 2 Begegnungen (Halbfinale) plus 2 Dummy-Begegnung für die Trostrunde
		assertEquals(2+2, wettkampfGruppe.alleRundenBegegnungen().get(2).begegnungenJeRunde().size());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 3, 1), wettkampfGruppe.alleRundenBegegnungen().get(2).begegnungenJeRunde().get(0).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 3, 2), wettkampfGruppe.alleRundenBegegnungen().get(2).begegnungenJeRunde().get(1).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 3, 1), wettkampfGruppe.alleRundenBegegnungen().get(2).begegnungenJeRunde().get(2).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 3, 2), wettkampfGruppe.alleRundenBegegnungen().get(2).begegnungenJeRunde().get(3).getBegegnungId());

		// Runde 4 sind 1 Begegnungen (Finale) plus 2 Dummy-Begegnung für die Trostrunde
		assertEquals(1+2, wettkampfGruppe.alleRundenBegegnungen().get(3).begegnungenJeRunde().size());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 4, 1), wettkampfGruppe.alleRundenBegegnungen().get(3).begegnungenJeRunde().get(0).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 4, 1), wettkampfGruppe.alleRundenBegegnungen().get(3).begegnungenJeRunde().get(1).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 4, 2), wettkampfGruppe.alleRundenBegegnungen().get(3).begegnungenJeRunde().get(2).getBegegnungId());
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
			new Wettkaempfer(UUID.randomUUID(), "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein1", turnierUUID), 25.0, Optional.of(Farbe.ORANGE), true, false, turnierUUID),
			new Wettkaempfer(UUID.randomUUID(), "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein3", turnierUUID), 27.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(UUID.randomUUID(), "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein5", turnierUUID), 29.0, Optional.of(Farbe.ORANGE), true, false, turnierUUID),
			new Wettkaempfer(UUID.randomUUID(), "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein2", turnierUUID), 26.1, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(UUID.randomUUID(), "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein4", turnierUUID), 33.0, Optional.of(Farbe.ORANGE), true, false, turnierUUID),
			new Wettkaempfer(UUID.randomUUID(), "Teilnehmer F", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein1", turnierUUID), 35.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(UUID.randomUUID(), "Teilnehmer G", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein3", turnierUUID), 37.0, Optional.of(Farbe.ORANGE), true, false, turnierUUID),
			new Wettkaempfer(UUID.randomUUID(), "Teilnehmer H", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein5", turnierUUID), 39.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(UUID.randomUUID(), "Teilnehmer I", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein2", turnierUUID), 41.0, Optional.of(Farbe.ORANGE), true, false, turnierUUID),
			new Wettkaempfer(UUID.randomUUID(), "Teilnehmer J", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein4", turnierUUID), 43.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(UUID.randomUUID(), "Teilnehmer K", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein4", turnierUUID), 43.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(UUID.randomUUID(), "Teilnehmer L", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein4", turnierUUID), 43.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(UUID.randomUUID(), "Teilnehmer M", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein4", turnierUUID), 43.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(UUID.randomUUID(), "Teilnehmer N", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein4", turnierUUID), 43.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(UUID.randomUUID(), "Teilnehmer O", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein4", turnierUUID), 43.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(UUID.randomUUID(), "Teilnehmer P", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein4", turnierUUID), 43.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID),

			new Wettkaempfer(UUID.randomUUID(), "Teilnehmer Q", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein1", turnierUUID), 25.0, Optional.of(Farbe.ORANGE), true, false, turnierUUID),
			new Wettkaempfer(UUID.randomUUID(), "Teilnehmer R", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein3", turnierUUID), 27.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(UUID.randomUUID(), "Teilnehmer S", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein5", turnierUUID), 29.0, Optional.of(Farbe.ORANGE), true, false, turnierUUID),
			new Wettkaempfer(UUID.randomUUID(), "Teilnehmer T", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein2", turnierUUID), 26.1, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(UUID.randomUUID(), "Teilnehmer U", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein4", turnierUUID), 33.0, Optional.of(Farbe.ORANGE), true, false, turnierUUID),
			new Wettkaempfer(UUID.randomUUID(), "Teilnehmer V", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein1", turnierUUID), 35.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(UUID.randomUUID(), "Teilnehmer W", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein3", turnierUUID), 37.0, Optional.of(Farbe.ORANGE), true, false, turnierUUID),
			new Wettkaempfer(UUID.randomUUID(), "Teilnehmer X", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein5", turnierUUID), 39.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(UUID.randomUUID(), "Teilnehmer Y", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein2", turnierUUID), 41.0, Optional.of(Farbe.ORANGE), true, false, turnierUUID),
			new Wettkaempfer(UUID.randomUUID(), "Teilnehmer Z", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein4", turnierUUID), 43.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(UUID.randomUUID(), "Teilnehmer a", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein4", turnierUUID), 43.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(UUID.randomUUID(), "Teilnehmer b", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein4", turnierUUID), 43.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(UUID.randomUUID(), "Teilnehmer c", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein4", turnierUUID), 43.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(UUID.randomUUID(), "Teilnehmer d", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein4", turnierUUID), 43.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(UUID.randomUUID(), "Teilnehmer e", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein4", turnierUUID), 43.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
			new Wettkaempfer(UUID.randomUUID(), "Teilnehmer f", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein4", turnierUUID), 43.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID)
		);


		GewichtsklassenGruppe gewichtsklassengruppe = new GewichtsklassenGruppe(UUID.randomUUID(), Altersklasse.U11, Optional.of(Geschlecht.m), teilnehmer, Optional.empty(), 25.0, 43.0, turnierUUID);

		WettkampfGruppeMitBegegnungen wettkampfGruppe = algorithmus.erstelleWettkampfGruppe(gewichtsklassengruppe);

		// Ausgabe des Turnierbaums
		int r = 1;
		for (BegegnungenJeRunde begegnungenJeRunde : wettkampfGruppe.alleRundenBegegnungen()) {
			logger.debug("Runde {}", r);
			for (Begegnung begegnung : begegnungenJeRunde.begegnungenJeRunde()) {
				logger.debug("{} - Wettkaempfer1: {}, Wettkaempfer2: {}", begegnung.getBegegnungId(),  begegnung.getWettkaempfer1().map(Wettkaempfer::name), begegnung.getWettkaempfer2().map(Wettkaempfer::name));
			}
			r++;
		}

		assertEquals(6, wettkampfGruppe.alleRundenBegegnungen().size()); // Tiefe des Turniers, also wieviele Runden bis Finale
		assertEquals(24, wettkampfGruppe.alleRundenBegegnungen().get(0).begegnungenJeRunde().size()); // Runde 1 sind 16 Begegnungen plus 8 Dummy-Begegnung für die Trostrunde
		assertEquals(16, wettkampfGruppe.alleRundenBegegnungen().get(1).begegnungenJeRunde().size()); // Runde 2 sind 8 Begegnungen plus 8 Dummy-Begegnung für die Trostrunde
		assertEquals(8, wettkampfGruppe.alleRundenBegegnungen().get(2).begegnungenJeRunde().size());  // Runde 3 sind 4 Begegnungen plus 4 Dummy-Begegnung für die Trostrunde
		assertEquals(6, wettkampfGruppe.alleRundenBegegnungen().get(3).begegnungenJeRunde().size());  // Runde 4 sind 2 Begegnungen plus 4 Dummy-Begegnung für die Trostrunde
		assertEquals(4, wettkampfGruppe.alleRundenBegegnungen().get(4).begegnungenJeRunde().size());  // Runde 5 sind 2 Begegnungen plus 2 Dummy-Begegnung für die Trostrunde -> hier kommt eine weitere Runde hinzu, weil die Sieger der Trostrunde in das Viertelfinale aufgenommen werden
		assertEquals(4, wettkampfGruppe.alleRundenBegegnungen().get(5).begegnungenJeRunde().size());  // Runde 6 sind 1 Begegnungen plus 2 Dummy-Begegnung für die Trostrunde
	}

	@Test
	public void rechnerTrostrunden() {
		IntUnaryOperator calcGewinnerRunden = n -> (int) Math.ceil(Math.log(n) / Math.log(2));
		IntUnaryOperator calcTrostRunden = n -> (int) (2* (calcGewinnerRunden.applyAsInt(n)-1) -2);

		var teilnehmer = List.of(4, 6, 8, 16, 32);
		teilnehmer.forEach(t -> logger.debug("{} Teilnehmer - Gewinnerrunden: {}, Trostrunden: {}", t, calcGewinnerRunden.applyAsInt(t), calcTrostRunden.applyAsInt(t)));
	}

}
