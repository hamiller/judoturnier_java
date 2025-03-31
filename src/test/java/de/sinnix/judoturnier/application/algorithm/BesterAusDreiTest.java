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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class BesterAusDreiTest {
	private static final Logger        logger      = LogManager.getLogger(BesterAusDreiTest.class);
	private              BesterAusDrei algorithmus = new BesterAusDrei();
	private              UUID          turnierUUID;

	@BeforeEach
	void setUp() {
		turnierUUID = UUID.randomUUID();
	}

	@Test
	void erstelleWettkampfGruppeMit2() {
		UUID wk1Id = UUID.randomUUID();
		UUID wk2Id = UUID.randomUUID();

		// Teilnehmer einer Gruppe
		List<Wettkaempfer> teilnehmer = Arrays.asList(
			new Wettkaempfer(wk1Id, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein1", turnierUUID), 25.0, Optional.of(Farbe.ORANGE), true, false, turnierUUID),
			new Wettkaempfer(wk2Id, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(UUID.randomUUID(), "Verein3", turnierUUID), 27.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID)
		);
		GewichtsklassenGruppe gewichtsklassengruppe = new GewichtsklassenGruppe(UUID.randomUUID(), Altersklasse.U11, Optional.of(Geschlecht.m), teilnehmer, Optional.empty(), 25.0, 26.1, turnierUUID);

		WettkampfGruppeMitBegegnungen wettkampfGruppe = algorithmus.erstelleWettkampfGruppe(gewichtsklassengruppe);

		// Erwartungen prüfen
		assertNotNull(wettkampfGruppe);
		assertNotNull(wettkampfGruppe.alleRundenBegegnungen());

		// Ausgabe des Turnierbaums
		int r = 1;
		for (BegegnungenJeRunde begegnungenJeRunde : wettkampfGruppe.alleRundenBegegnungen()) {
			logger.debug("Runde {}", r);
			for (Begegnung begegnung : begegnungenJeRunde.begegnungenJeRunde()) {
				logger.debug("{} - Wettkaempfer1: {}, Wettkaempfer2: {}", begegnung.getBegegnungId(), begegnung.getWettkaempfer1().map(Wettkaempfer::name), begegnung.getWettkaempfer2().map(Wettkaempfer::name));
				assertNotNull(begegnung);
				assertTrue(begegnung.getWettkaempfer1().isPresent());
				assertTrue(begegnung.getWettkaempfer2().isPresent());
			}
			r++;
		}

		// Es sollten (n * (n-1) / 2) * 3 Begegnungen sein (Best-of-3 für jede Paarung)
		int expectedSize = 3; // 3 Begegnungen maximal
		assertEquals(expectedSize, wettkampfGruppe.alleRundenBegegnungen().stream().mapToInt(runde -> runde.begegnungenJeRunde().size()).sum());
		assertEquals(expectedSize, wettkampfGruppe.alleRundenBegegnungen().size()); // Begegnungen aufgeteilt in die Runden
		assertEquals(1, wettkampfGruppe.alleRundenBegegnungen().getFirst().begegnungenJeRunde().getFirst().getBegegnungId().getRunde());
	}
}
