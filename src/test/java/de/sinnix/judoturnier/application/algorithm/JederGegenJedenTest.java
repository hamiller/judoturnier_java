package de.sinnix.judoturnier.application.algorithm;

import de.sinnix.judoturnier.fixtures.GewichtsklassenGruppeFixture;
import de.sinnix.judoturnier.fixtures.WettkaempferFixtures;
import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Geschlecht;
import de.sinnix.judoturnier.model.GewichtsklassenGruppe;
import de.sinnix.judoturnier.model.RandoriGruppenName;
import de.sinnix.judoturnier.model.Wettkaempfer;
import de.sinnix.judoturnier.model.WettkampfGruppe;
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

	private JederGegenJeden algorithmus = new JederGegenJeden();

	private UUID turnierUUID;

	@BeforeEach
	void setUp() {
		turnierUUID = UUID.randomUUID();
	}

	@Test
	public void testErstelleWettkampfGruppenGeradeAnzahlTest() {
		GewichtsklassenGruppe gewichtsklassenGruppe = GewichtsklassenGruppeFixture.gewichtsklassenGruppen.getFirst();
		List<WettkampfGruppe> erstellteWettkampfgruppen = algorithmus.erstelleWettkampfGruppen(1, gewichtsklassenGruppe, 10);
		var n = gewichtsklassenGruppe.teilnehmer().size();

		assertTrue(gewichtsklassenGruppe.name().isPresent());
		assertEquals(RandoriGruppenName.Antilope, gewichtsklassenGruppe.name().get());
		assertEquals(23.8, gewichtsklassenGruppe.minGewicht());
		assertEquals(24.5, gewichtsklassenGruppe.maxGewicht());
		assertEquals(6, gewichtsklassenGruppe.teilnehmer().size());

		// Aufteilung in Gruppen
		assertEquals(1, erstellteWettkampfgruppen.size());
		assertEquals("(23.8-24.5 U11)", erstellteWettkampfgruppen.getFirst().typ());
		assertEquals("Antilope", erstellteWettkampfgruppen.getFirst().name());
		// Anzahl der Runden
		var anzahlRunden = n - 1;
		assertEquals(anzahlRunden, erstellteWettkampfgruppen.getFirst().alleGruppenBegegnungen().size());
		// Berechnung der Gesamtanzahl aller Begegnungen bei Jeder-Gegen-Jeden: N = (n * (n-1)) /2, mit n==AnzahlTeilnehmer
		var N = (n * (n-1)) /2;
		assertEquals(N, erstellteWettkampfgruppen.getFirst().alleGruppenBegegnungen().stream().mapToInt(begegnung -> begegnung.begegnungen().size()).sum());
	}

	@Test
	public void testErstelleWettkampfGruppenUngeradeAnzahlTest() {
		GewichtsklassenGruppe gewichtsklassenGruppe = GewichtsklassenGruppeFixture.gewichtsklassenGruppen.getLast();
		List<WettkampfGruppe> erstellteWettkampfgruppen = algorithmus.erstelleWettkampfGruppen(1, gewichtsklassenGruppe, 10);
		var n = gewichtsklassenGruppe.teilnehmer().size();

		assertTrue(gewichtsklassenGruppe.name().isPresent());
		assertEquals(RandoriGruppenName.Tiger, gewichtsklassenGruppe.name().get());
		assertEquals(28.0, gewichtsklassenGruppe.minGewicht());
		assertEquals(28.2, gewichtsklassenGruppe.maxGewicht());
		assertEquals(3, gewichtsklassenGruppe.teilnehmer().size());

		// Aufteilung in Gruppen
		assertEquals(1, erstellteWettkampfgruppen.size());
		assertEquals("(28.0-28.2 U11)", erstellteWettkampfgruppen.getFirst().typ());
		assertEquals("Tiger", erstellteWettkampfgruppen.getFirst().name());
		// Anzahl der Runden
		var anzahlRunden = n;
		assertEquals(anzahlRunden, erstellteWettkampfgruppen.getFirst().alleGruppenBegegnungen().size());
		// Berechnung der Gesamtanzahl aller Begegnungen bei Jeder-Gegen-Jeden: N = (n * (n-1)) /2, mit n==AnzahlTeilnehmer
		var N = (n * (n-1)) /2;
		assertEquals(N, erstellteWettkampfgruppen.getFirst().alleGruppenBegegnungen().stream().mapToInt(begegnung -> begegnung.begegnungen().size()).sum());

		System.out.println(erstellteWettkampfgruppen);
	}

	@Test
	public void testErstelleWettkampfGruppenFuerEinzelkaempfer() {
		List<Wettkaempfer> teilnehmer = List.of(WettkaempferFixtures.wettkaempfer1);
		GewichtsklassenGruppe gewichtsklassenGruppe = new GewichtsklassenGruppe(1, Altersklasse.U11, Optional.of(Geschlecht.w), teilnehmer, Optional.of(RandoriGruppenName.Adler), 25.0, 25.0, turnierUUID);

		List<WettkampfGruppe> erstellteWettkampfgruppen = algorithmus.erstelleWettkampfGruppen(1, gewichtsklassenGruppe, 10);

		assertEquals(1, erstellteWettkampfgruppen.size());
		assertTrue(!erstellteWettkampfgruppen.getFirst().alleGruppenBegegnungen().isEmpty());
		assertEquals(1, erstellteWettkampfgruppen.getFirst().alleGruppenBegegnungen().stream().mapToInt(b -> b.begegnungen().size()).sum());
	}
}