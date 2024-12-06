package de.sinnix.judoturnier.application.algorithm;

import de.sinnix.judoturnier.fixtures.GewichtsklassenGruppeFixture;
import de.sinnix.judoturnier.fixtures.WettkaempferFixtures;
import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Begegnung;
import de.sinnix.judoturnier.model.BegegnungenJeRunde;
import de.sinnix.judoturnier.model.Geschlecht;
import de.sinnix.judoturnier.model.GewichtsklassenGruppe;
import de.sinnix.judoturnier.model.RandoriGruppenName;
import de.sinnix.judoturnier.model.Wettkaempfer;
import de.sinnix.judoturnier.model.WettkampfGruppe;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class JederGegenJedenTest {

	private static final Logger          logger      = LogManager.getLogger(JederGegenJeden.class);

	private              JederGegenJeden algorithmus = new JederGegenJeden();

	private UUID turnierUUID;

	@BeforeEach
	void setUp() {
		turnierUUID = UUID.randomUUID();
	}

	@Test
	public void testErstelleWettkampfGruppenGeradeAnzahlTest() {
		/**
		 * 6 Teilnehmer, jeder gegen jeden -> 5 Runden
		 */
		GewichtsklassenGruppe gewichtsklassenGruppe = GewichtsklassenGruppeFixture.gewichtsklassenGruppen.getFirst();
		WettkampfGruppe erstellteWettkampfgruppe = algorithmus.erstelleWettkampfGruppe(gewichtsklassenGruppe);
		var n = gewichtsklassenGruppe.teilnehmer().size();

		assertTrue(gewichtsklassenGruppe.name().isPresent());
		assertEquals(RandoriGruppenName.Antilope, gewichtsklassenGruppe.name().get());
		assertEquals(23.8, gewichtsklassenGruppe.minGewicht());
		assertEquals(24.5, gewichtsklassenGruppe.maxGewicht());
		assertEquals(6, gewichtsklassenGruppe.teilnehmer().size());

		// Aufteilung in Gruppen
		assertEquals("(23.8-24.5 U11)", erstellteWettkampfgruppe.typ());
		assertEquals("Antilope", erstellteWettkampfgruppe.name());
		// Anzahl der Runden
		var anzahlRunden = n - 1;
		assertEquals(anzahlRunden, erstellteWettkampfgruppe.alleRundenBegegnungen().size());
		// Berechnung der Gesamtanzahl aller Begegnungen bei Jeder-Gegen-Jeden: N = (n * (n-1)) /2, mit n==AnzahlTeilnehmer
		var N = (n * (n-1)) /2;
		assertEquals(N, erstellteWettkampfgruppe.alleRundenBegegnungen().stream().mapToInt(begegnung -> begegnung.begegnungenJeRunde().size()).sum());

		// Ausgabe des Turnierbaums
		int r = 1;
		for (BegegnungenJeRunde begegnungenJeRunde : erstellteWettkampfgruppe.alleRundenBegegnungen()) {
			logger.debug("Runde {}", r);
			for (Begegnung begegnung : begegnungenJeRunde.begegnungenJeRunde()) {
				logger.debug("{} - Wettkaempfer1: {}, Wettkaempfer2: {}", begegnung.getBegegnungId(),  begegnung.getWettkaempfer1().map(Wettkaempfer::name), begegnung.getWettkaempfer2().map(Wettkaempfer::name));
			}
			r++;
		}

		// Runde 1
		assertEquals(3, erstellteWettkampfgruppe.alleRundenBegegnungen().get(0).begegnungenJeRunde().size());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1), erstellteWettkampfgruppe.alleRundenBegegnungen().get(0).begegnungenJeRunde().get(0).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 2), erstellteWettkampfgruppe.alleRundenBegegnungen().get(0).begegnungenJeRunde().get(1).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 3), erstellteWettkampfgruppe.alleRundenBegegnungen().get(0).begegnungenJeRunde().get(2).getBegegnungId());

		// Runde 2
		assertEquals(3, erstellteWettkampfgruppe.alleRundenBegegnungen().get(1).begegnungenJeRunde().size());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 2, 1), erstellteWettkampfgruppe.alleRundenBegegnungen().get(1).begegnungenJeRunde().get(0).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 2, 2), erstellteWettkampfgruppe.alleRundenBegegnungen().get(1).begegnungenJeRunde().get(1).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 2, 3), erstellteWettkampfgruppe.alleRundenBegegnungen().get(1).begegnungenJeRunde().get(2).getBegegnungId());

		// Runde 3
		assertEquals(3, erstellteWettkampfgruppe.alleRundenBegegnungen().get(2).begegnungenJeRunde().size());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 3, 1), erstellteWettkampfgruppe.alleRundenBegegnungen().get(2).begegnungenJeRunde().get(0).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 3, 2), erstellteWettkampfgruppe.alleRundenBegegnungen().get(2).begegnungenJeRunde().get(1).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 3, 3), erstellteWettkampfgruppe.alleRundenBegegnungen().get(2).begegnungenJeRunde().get(2).getBegegnungId());

		// Runde 4
		assertEquals(3, erstellteWettkampfgruppe.alleRundenBegegnungen().get(3).begegnungenJeRunde().size());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 4, 1), erstellteWettkampfgruppe.alleRundenBegegnungen().get(3).begegnungenJeRunde().get(0).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 4, 2), erstellteWettkampfgruppe.alleRundenBegegnungen().get(3).begegnungenJeRunde().get(1).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 4, 3), erstellteWettkampfgruppe.alleRundenBegegnungen().get(3).begegnungenJeRunde().get(2).getBegegnungId());

		// Runde 5
		assertEquals(3, erstellteWettkampfgruppe.alleRundenBegegnungen().get(4).begegnungenJeRunde().size());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 5, 1), erstellteWettkampfgruppe.alleRundenBegegnungen().get(4).begegnungenJeRunde().get(0).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 5, 2), erstellteWettkampfgruppe.alleRundenBegegnungen().get(4).begegnungenJeRunde().get(1).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 5, 3), erstellteWettkampfgruppe.alleRundenBegegnungen().get(4).begegnungenJeRunde().get(2).getBegegnungId());
	}

	@Test
	public void testErstelleWettkampfGruppenUngeradeAnzahlTest() {
		/**
		 * 3 Teilnehmer, jeder gegen jeden -> 3 Runden
		 */
		GewichtsklassenGruppe gewichtsklassenGruppe = GewichtsklassenGruppeFixture.gewichtsklassenGruppen.getLast();
		WettkampfGruppe erstellteWettkampfgruppe = algorithmus.erstelleWettkampfGruppe(gewichtsklassenGruppe);
		var n = gewichtsklassenGruppe.teilnehmer().size();

		assertTrue(gewichtsklassenGruppe.name().isPresent());
		assertEquals(RandoriGruppenName.Tiger, gewichtsklassenGruppe.name().get());
		assertEquals(28.0, gewichtsklassenGruppe.minGewicht());
		assertEquals(28.2, gewichtsklassenGruppe.maxGewicht());
		assertEquals(3, gewichtsklassenGruppe.teilnehmer().size());

		// Aufteilung in Gruppen
		assertEquals("(28.0-28.2 U11)", erstellteWettkampfgruppe.typ());
		assertEquals("Tiger", erstellteWettkampfgruppe.name());
		// Anzahl der Runden
		var anzahlRunden = n;
		assertEquals(anzahlRunden, erstellteWettkampfgruppe.alleRundenBegegnungen().size());
		// Berechnung der Gesamtanzahl aller Begegnungen bei Jeder-Gegen-Jeden: N = (n * (n-1)) /2, mit n==AnzahlTeilnehmer
		var N = (n * (n-1)) /2;
		assertEquals(N, erstellteWettkampfgruppe.alleRundenBegegnungen().stream().mapToInt(begegnung -> begegnung.begegnungenJeRunde().size()).sum());

		// Ausgabe des Turnierbaums

		int r = 1;
		for (BegegnungenJeRunde begegnungenJeRunde : erstellteWettkampfgruppe.alleRundenBegegnungen()) {
			logger.debug("Runde {}", r);
			for (Begegnung begegnung : begegnungenJeRunde.begegnungenJeRunde()) {
				logger.debug("{} - Wettkaempfer1: {}, Wettkaempfer2: {}", begegnung.getBegegnungId(),  begegnung.getWettkaempfer1().map(Wettkaempfer::name), begegnung.getWettkaempfer2().map(Wettkaempfer::name));
			}
			r++;
		}

		// Runde 1
		assertEquals(1, erstellteWettkampfgruppe.alleRundenBegegnungen().get(0).begegnungenJeRunde().size());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1), erstellteWettkampfgruppe.alleRundenBegegnungen().get(0).begegnungenJeRunde().get(0).getBegegnungId());

		// Runde 2
		assertEquals(1, erstellteWettkampfgruppe.alleRundenBegegnungen().get(1).begegnungenJeRunde().size());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 2, 1), erstellteWettkampfgruppe.alleRundenBegegnungen().get(1).begegnungenJeRunde().get(0).getBegegnungId());

		// Runde 3
		assertEquals(1, erstellteWettkampfgruppe.alleRundenBegegnungen().get(2).begegnungenJeRunde().size());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 3, 1), erstellteWettkampfgruppe.alleRundenBegegnungen().get(2).begegnungenJeRunde().get(0).getBegegnungId());
	}

	@Test
	public void testErstelleWettkampfGruppenFuerEinzelkaempfer() {
		List<Wettkaempfer> teilnehmer = List.of(WettkaempferFixtures.wettkaempfer1.get());
		GewichtsklassenGruppe gewichtsklassenGruppe = new GewichtsklassenGruppe(UUID.randomUUID(), Altersklasse.U11, Optional.of(Geschlecht.w), teilnehmer, Optional.of(RandoriGruppenName.Adler), 25.0, 25.0, turnierUUID);

		WettkampfGruppe erstellteWettkampfgruppe = algorithmus.erstelleWettkampfGruppe(gewichtsklassenGruppe);

		assertTrue(!erstellteWettkampfgruppe.alleRundenBegegnungen().isEmpty());
		assertEquals(1, erstellteWettkampfgruppe.alleRundenBegegnungen().stream().mapToInt(b -> b.begegnungenJeRunde().size()).sum());
	}
}
