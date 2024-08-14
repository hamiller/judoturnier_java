package de.sinnix.judoturnier.application;

import de.sinnix.judoturnier.fixtures.WettkampfgruppeFixture;
import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Begegnung;
import de.sinnix.judoturnier.model.BegegnungenJeRunde;
import de.sinnix.judoturnier.model.Farbe;
import de.sinnix.judoturnier.model.Geschlecht;
import de.sinnix.judoturnier.model.Runde;
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
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class SortiererTest {

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
		assertEquals(N, wettkampfGruppeList.get(0).alleRundenBegegnungen().stream().mapToInt(g -> g.begegnungenJeRunde().size()).sum());

		Sortierer sortierer = new Sortierer(1);
		List<Runde> runden = sortierer.erstelleReihenfolgeMitAllenGruppenJeDurchgang(wettkampfGruppeList, 1).getRight();

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
		assertEquals(N, wettkampfGruppeList.get(0).alleRundenBegegnungen().stream().mapToInt(g -> g.begegnungenJeRunde().size()).sum());

		Sortierer sortierer = new Sortierer(1);
		List<Runde> runden = sortierer.erstelleReihenfolgeMitAbwechselndenGruppen(wettkampfGruppeList, 1).getRight();

		assertEquals(3, runden.size());
		var sortierteBegegnungen = runden.stream().mapToInt(r -> r.begegnungen().size()).sum();
		assertEquals(N, sortierteBegegnungen);
	}

	@Test
	public void testErstelleReihenfolgeMitAllenGruppenJeDurchgangUndVierWettkampfgruppen() {
		List<WettkampfGruppe> wettkampfGruppeList = WettkampfgruppeFixture.wks_gerade;
		assertEquals(4, wettkampfGruppeList.size());
		assertEquals(51, wettkampfGruppeList.stream().mapToInt(wg -> wg.alleRundenBegegnungen().stream().mapToInt(g -> g.begegnungenJeRunde().size()).sum()).sum());

		Sortierer sortierer = new Sortierer(1);
		List<Runde> runden = sortierer.erstelleReihenfolgeMitAllenGruppenJeDurchgang(wettkampfGruppeList, 1).getRight();

		assertEquals(18, runden.size());
		var sortierteBegegnungen = runden.stream().mapToInt(r -> r.begegnungen().size()).sum();
		assertEquals(51, sortierteBegegnungen);
	}

	@Test
	public void testPausenBeiAbwechselndenGruppen() {
		UUID rundeUUID1 = UUID.randomUUID();
		UUID rundeUUID2 = UUID.randomUUID();
		UUID rundeUUID3 = UUID.randomUUID();
		UUID rundeUUID4 = UUID.randomUUID();

		List<WettkampfGruppe> wettkampfGruppeList = Arrays.asList(
			new WettkampfGruppe(100, "Antilope", "(Gewichtskl.1 U11)", Arrays.asList(new BegegnungenJeRunde(
				Arrays.asList(
					new Begegnung(1, new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1), rundeUUID1, 1, 1, 1, 1,
						Optional.of(new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
						Optional.of(new Wettkaempfer(4, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
					new Begegnung(2, new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 2), rundeUUID1, 1, 1, 1, 2,
						Optional.of(new Wettkaempfer(2, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
						Optional.of(new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID))),
				new BegegnungenJeRunde(
				Arrays.asList(
					new Begegnung(3, new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 2, 1), rundeUUID2, 1, 3, 2, 3,
						Optional.of(new Wettkaempfer(4, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
						Optional.of(new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
					new Begegnung(4, new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 2, 2), rundeUUID2, 1, 3, 2, 4,
						Optional.of(new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
						Optional.of(new Wettkaempfer(2, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID))),
				new BegegnungenJeRunde(Arrays.asList(
					new Begegnung(5, new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 3, 1), rundeUUID3, 1, 4 , 3,5,
						Optional.of(new Wettkaempfer(2, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
						Optional.of(new Wettkaempfer(4, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
					new Begegnung(6, new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 3, 2), rundeUUID3, 1, 4, 3, 6,
						Optional.of(new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
						Optional.of(new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID)))
			), turnierUUID),
			new WettkampfGruppe(101, "Tiger", "(Gewichtskl.1 U11)", Arrays.asList(new BegegnungenJeRunde(
				Arrays.asList(
					new Begegnung(7, new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1), rundeUUID4, 1, 2, 1, 7,
						Optional.of(new Wettkaempfer(5, "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
						Optional.of(new Wettkaempfer(6, "Teilnehmer F", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID)
					)
			)), turnierUUID)
		);

		Sortierer sortierer = new Sortierer(1);
		List<Runde> runden = sortierer.erstelleReihenfolgeMitAbwechselndenGruppen(wettkampfGruppeList, 1).getRight();


		var anzahlRunden = 4 + 1; // 4 Runden plus 1 Pause
		assertEquals(anzahlRunden, runden.size());

		assertEquals(1, runden.get(0).rundeGesamt());
		assertEquals(1, runden.get(0).mattenRunde());
		assertEquals(1, runden.get(0).gruppenRunde());
		assertEquals(2, runden.get(0).begegnungen().size());
		assertEquals(wettkampfGruppeList.get(0).alleRundenBegegnungen().get(0).begegnungenJeRunde().get(0).getBegegnungId(), runden.get(0).begegnungen().get(0).getBegegnungId());
		assertEquals(wettkampfGruppeList.get(0).alleRundenBegegnungen().get(0).begegnungenJeRunde().get(1).getBegegnungId(), runden.get(0).begegnungen().get(1).getBegegnungId());

		assertEquals(2, runden.get(1).rundeGesamt());
		assertEquals(2, runden.get(1).mattenRunde());
		assertEquals(1, runden.get(1).gruppenRunde());
		assertEquals(1, runden.get(1).begegnungen().size());
		assertEquals(wettkampfGruppeList.get(1).alleRundenBegegnungen().get(0).begegnungenJeRunde().get(0).getBegegnungId(), runden.get(1).begegnungen().get(0).getBegegnungId());

		assertEquals(3, runden.get(2).rundeGesamt());
		assertEquals(3, runden.get(2).mattenRunde());
		assertEquals(2, runden.get(2).gruppenRunde());
		assertEquals(2, runden.get(2).begegnungen().size());

		// PAUSE
		assertEquals(4, runden.get(3).rundeGesamt());
		assertEquals(4, runden.get(3).mattenRunde());
		assertEquals(2, runden.get(3).gruppenRunde());
		assertEquals(1, runden.get(3).begegnungen().size());
		assertTrue(runden.get(3).begegnungen().get(0).getBegegnungId() != null);

		assertEquals(5, runden.get(4).rundeGesamt());
		assertEquals(5, runden.get(4).mattenRunde());
		assertEquals(3, runden.get(4).gruppenRunde());
		assertEquals(2, runden.get(4).begegnungen().size());
	}

}