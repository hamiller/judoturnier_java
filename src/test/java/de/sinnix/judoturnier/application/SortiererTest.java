package de.sinnix.judoturnier.application;

import de.sinnix.judoturnier.fixtures.WettkampfgruppeFixture;
import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Begegnung;
import de.sinnix.judoturnier.model.Farbe;
import de.sinnix.judoturnier.model.Geschlecht;
import de.sinnix.judoturnier.model.Runde;
import de.sinnix.judoturnier.model.Verein;
import de.sinnix.judoturnier.model.Wettkaempfer;
import de.sinnix.judoturnier.model.WettkampfGruppe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class SortiererTest {

	@InjectMocks
	private Sortierer sortierer;
	private UUID      turnierUUID;

	@BeforeEach
	void setUp() {
		turnierUUID = UUID.randomUUID();
	}

	@Test
	public void testErstelleReihenfolgeMitAllenGruppenJeDurchgangUndEinzelnerWettkampfgruppe() {
		List<WettkampfGruppe> wettkampfGruppeList = WettkampfgruppeFixture.wks2;
		var n = 4; // Anzahl Teilnehmer
		var N = (n * (n-1)) /2; // Berechnete Begegnungen
		assertEquals(1, wettkampfGruppeList.size());
		assertEquals(N, wettkampfGruppeList.get(0).alleGruppenBegegnungen().stream().mapToInt(g -> g.size()).sum());

		List<Runde> runden = sortierer.erstelleReihenfolgeMitAllenGruppenJeDurchgang(wettkampfGruppeList);
		assertEquals(3, runden.size());
		var sortierteBegegnungen = runden.stream().mapToInt(r -> r.begegnungen().size()).sum();
		assertEquals(N, sortierteBegegnungen);
	}

	@Test
	public void testErstelleReihenfolgeMitAbwechselndenGruppenUndEinzelnerWettkampfgruppe() {
		List<WettkampfGruppe> wettkampfGruppeList = WettkampfgruppeFixture.wks2;
		var n = 4; // Anzahl Teilnehmer
		var N = (n * (n-1)) /2; // Berechnete Begegnungen
		assertEquals(1, wettkampfGruppeList.size());
		assertEquals(N, wettkampfGruppeList.get(0).alleGruppenBegegnungen().stream().mapToInt(g -> g.size()).sum());

		List<Runde> runden = sortierer.erstelleReihenfolgeMitAbwechselndenGruppen(wettkampfGruppeList);
		assertEquals(3, runden.size());
		var sortierteBegegnungen = runden.stream().mapToInt(r -> r.begegnungen().size()).sum();
		assertEquals(N, sortierteBegegnungen);
	}

	@Test
	public void testErstelleReihenfolgeMitAllenGruppenJeDurchgangUndVierWettkampfgruppen() {
		List<WettkampfGruppe> wettkampfGruppeList = WettkampfgruppeFixture.wks_gerade;
		assertEquals(4, wettkampfGruppeList.size());
		assertEquals(51, wettkampfGruppeList.stream().mapToInt(wg -> wg.alleGruppenBegegnungen().stream().mapToInt(g -> g.size()).sum()).sum());

		List<Runde> runden = sortierer.erstelleReihenfolgeMitAllenGruppenJeDurchgang(wettkampfGruppeList);

		assertEquals(18, runden.size());
		var sortierteBegegnungen = runden.stream().mapToInt(r -> r.begegnungen().size()).sum();
		assertEquals(51, sortierteBegegnungen);
	}

	@Test
	public void testPausenBeiAbwechselndenGruppen() {
		List<WettkampfGruppe> wettkampfGruppeList = Arrays.asList(
			new WettkampfGruppe(100, "Antilope", "(Gewichtskl.1 U11)", Arrays.asList(
				Arrays.asList(
					new Begegnung(1, 1, 1, 1,
						new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
						new Wettkaempfer(4, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID),
					new Begegnung(2, 1, 1, 1,
						new Wettkaempfer(2, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
						new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID)),
				Arrays.asList(
					new Begegnung(3, 1, 3, 2,
						new Wettkaempfer(4, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
						new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID),
					new Begegnung(4, 1, 3, 2,
						new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
						new Wettkaempfer(2, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID)),
				Arrays.asList(
					new Begegnung(5, 1,4 , 3,
						new Wettkaempfer(2, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
						new Wettkaempfer(4, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID),
					new Begegnung(6, 1, 4, 3,
						new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
						new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID))
			), turnierUUID),
			new WettkampfGruppe(101, "Tiger", "(Gewichtskl.1 U11)", Arrays.asList(
				Arrays.asList(
					new Begegnung(7, 1, 2, 1,
						new Wettkaempfer(5, "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
						new Wettkaempfer(6, "Teilnehmer F", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID)
					)
			), turnierUUID)
		);


		List<Runde> runden = sortierer.erstelleReihenfolgeMitAbwechselndenGruppen(wettkampfGruppeList);


		var anzahlRunden = 4 + 1; // 4 Runden plus 1 Pause
		assertEquals(anzahlRunden, runden.size());

		assertEquals(0, runden.get(0).id());
		assertEquals(2, runden.get(0).begegnungen().size());
		assertEquals(wettkampfGruppeList.get(0).alleGruppenBegegnungen().get(0).get(0).getBegegnungId(), runden.get(0).begegnungen().get(0).getBegegnungId());
		assertEquals(wettkampfGruppeList.get(0).alleGruppenBegegnungen().get(0).get(1).getBegegnungId(), runden.get(0).begegnungen().get(1).getBegegnungId());

		assertEquals(1, runden.get(1).id());
		assertEquals(1, runden.get(1).begegnungen().size());
		assertEquals(wettkampfGruppeList.get(1).alleGruppenBegegnungen().get(0).get(0).getBegegnungId(), runden.get(1).begegnungen().get(0).getBegegnungId());

		assertEquals(2, runden.get(2).id());
		assertEquals(2, runden.get(2).begegnungen().size());

		// PAUSE
		assertEquals(3, runden.get(3).id());
		assertEquals(1, runden.get(3).begegnungen().size());
		assertTrue(runden.get(3).begegnungen().get(0).getBegegnungId() == null);

		assertEquals(4, runden.get(4).id());
		assertEquals(2, runden.get(4).begegnungen().size());
	}

}