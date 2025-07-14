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
import de.sinnix.judoturnier.model.WettkampfGruppeMitBegegnungen;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

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
		List<WettkampfGruppeMitBegegnungen> wettkampfGruppeList = WettkampfgruppeFixture.wks2;
		var n = 4; // Anzahl Teilnehmer
		var N = (n * (n-1)) /2; // Berechnete Begegnungen
		assertEquals(1, wettkampfGruppeList.size());
		assertEquals(N, wettkampfGruppeList.get(0).alleRundenBegegnungen().stream().mapToInt(g -> g.begegnungenJeRunde().size()).sum());

		Sortierer sortierer = new Sortierer(1, 1);
		List<Runde> runden = sortierer.erstelleReihenfolgeMitAllenGruppenJeDurchgang(wettkampfGruppeList, 1).getRight();

		assertEquals(3, runden.size());
		var sortierteBegegnungen = runden.stream().mapToInt(r -> r.begegnungen().size()).sum();
		assertEquals(N, sortierteBegegnungen);
		assertTrue(runden.get(0).gruppenRunde() != runden.get(1).gruppenRunde());
		// Die MattenRunde wird immer erhöht
		assertTrue(IntStream.range(1, runden.size())
			.allMatch(i -> runden.get(i - 1).mattenRunde() < runden.get(i).mattenRunde()));
	}

	@Test
	public void testErstelleReihenfolgeMitAbwechselndenGruppenUndEinzelnerWettkampfgruppe() {
		List<WettkampfGruppeMitBegegnungen> wettkampfGruppeList = WettkampfgruppeFixture.wks2;
		var n = 4; // Anzahl Teilnehmer
		var N = (n * (n-1)) /2; // Berechnete Begegnungen
		assertEquals(1, wettkampfGruppeList.size());
		assertEquals(N, wettkampfGruppeList.get(0).alleRundenBegegnungen().stream().mapToInt(g -> g.begegnungenJeRunde().size()).sum());

		Sortierer sortierer = new Sortierer(1, 1);
		List<Runde> runden = sortierer.erstelleReihenfolgeMitAbwechselndenGruppen(wettkampfGruppeList, 1).getRight();

		assertEquals(3, runden.size());
		var sortierteBegegnungen = runden.stream().mapToInt(r -> r.begegnungen().size()).sum();
		assertEquals(N, sortierteBegegnungen);
		assertTrue(runden.get(0).gruppenRunde() != runden.get(1).gruppenRunde());
		// Die MattenRunde wird immer erhöht
		assertTrue(IntStream.range(1, runden.size())
			.allMatch(i -> runden.get(i - 1).mattenRunde() < runden.get(i).mattenRunde()));
	}

	@Test
	public void testErstelleReihenfolgeMitAllenGruppenJeDurchgangUndVierWettkampfgruppen() {
		List<WettkampfGruppeMitBegegnungen> wettkampfGruppeList = WettkampfgruppeFixture.wks_gerade;
		assertEquals(4, wettkampfGruppeList.size());
		assertEquals(51, wettkampfGruppeList.stream().mapToInt(wg -> wg.alleRundenBegegnungen().stream().mapToInt(g -> g.begegnungenJeRunde().size()).sum()).sum());

		Sortierer sortierer = new Sortierer(1, 1);
		List<Runde> runden = sortierer.erstelleReihenfolgeMitAllenGruppenJeDurchgang(wettkampfGruppeList, 1).getRight();

		assertEquals(18, runden.size());
		var sortierteBegegnungen = runden.stream().mapToInt(r -> r.begegnungen().size()).sum();
		assertEquals(51, sortierteBegegnungen);
		assertTrue(runden.get(0).gruppenRunde() != runden.get(1).gruppenRunde());
		// Die MattenRunde wird immer erhöht
		assertTrue(IntStream.range(1, runden.size())
			.allMatch(i -> runden.get(i - 1).mattenRunde() < runden.get(i).mattenRunde()));
	}

	@Test
	public void testPausenBeiAbwechselndenGruppen() {
		UUID rundeUUID1 = UUID.randomUUID();
		UUID rundeUUID2 = UUID.randomUUID();
		UUID rundeUUID3 = UUID.randomUUID();
		UUID rundeUUID4 = UUID.randomUUID();
		UUID v1Id = UUID.randomUUID();
		UUID v2Id = UUID.randomUUID();
		UUID v3Id = UUID.randomUUID();
		UUID v4Id = UUID.randomUUID();
		UUID wk1Id = UUID.randomUUID();
		UUID wk2Id = UUID.randomUUID();
		UUID wk3Id = UUID.randomUUID();
		UUID wk4Id = UUID.randomUUID();
		UUID wk5Id = UUID.randomUUID();
		UUID wk6Id = UUID.randomUUID();

		List<WettkampfGruppeMitBegegnungen> wettkampfGruppeList = Arrays.asList(
			new WettkampfGruppeMitBegegnungen(
				new WettkampfGruppe(UUID.randomUUID(), "Antilope", "(Gewichtskl.1 U11)", Altersklasse.U11, turnierUUID),
				Arrays.asList(new BegegnungenJeRunde(
					Arrays.asList(
						new Begegnung(UUID.randomUUID(), new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1), rundeUUID1, 1, 1, 1, 1,
							Optional.of(new Wettkaempfer(wk1Id, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(v1Id, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
							Optional.of(new Wettkaempfer(wk4Id, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(v4Id, "Verein4", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
						new Begegnung(UUID.randomUUID(), new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 2), rundeUUID1, 1, 1, 1, 2,
							Optional.of(new Wettkaempfer(wk2Id, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(v2Id, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
							Optional.of(new Wettkaempfer(wk3Id, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(v3Id, "Verein3", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID))),
					new BegegnungenJeRunde(
					Arrays.asList(
						new Begegnung(UUID.randomUUID(), new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 2, 1), rundeUUID2, 1, 3, 2, 3,
							Optional.of(new Wettkaempfer(wk4Id, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(v4Id, "Verein4", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
							Optional.of(new Wettkaempfer(wk3Id, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(v3Id, "Verein3", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
						new Begegnung(UUID.randomUUID(), new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 2, 2), rundeUUID2, 1, 3, 2, 4,
							Optional.of(new Wettkaempfer(wk1Id, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(v1Id, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
							Optional.of(new Wettkaempfer(wk2Id, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(v2Id, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID))),
					new BegegnungenJeRunde(Arrays.asList(
						new Begegnung(UUID.randomUUID(), new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 3, 1), rundeUUID3, 1, 4 , 3,5,
							Optional.of(new Wettkaempfer(wk2Id, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(v2Id, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
							Optional.of(new Wettkaempfer(wk4Id, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(v4Id, "Verein4", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
						new Begegnung(UUID.randomUUID(), new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 3, 2), rundeUUID3, 1, 4, 3, 6,
							Optional.of(new Wettkaempfer(wk3Id, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(v3Id, "Verein3", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
							Optional.of(new Wettkaempfer(wk1Id, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(v1Id, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID)))
				)),
			new WettkampfGruppeMitBegegnungen(
				new WettkampfGruppe(UUID.randomUUID(), "Tiger", "(Gewichtskl.1 U11)", Altersklasse.U11, turnierUUID),
				Arrays.asList(new BegegnungenJeRunde(
					Arrays.asList(
						new Begegnung(UUID.randomUUID(), new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1), rundeUUID4, 1, 2, 1, 7,
							Optional.of(new Wettkaempfer(wk5Id, "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new Verein(v1Id, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
							Optional.of(new Wettkaempfer(wk6Id, "Teilnehmer F", Geschlecht.m, Altersklasse.U11, new Verein(v4Id, "Verein4", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID)
						)
				)))
		);

		Sortierer sortierer = new Sortierer(1, 1);
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

	@Test
	public void sucheNachfolger() {
		/**
		 . - Siegerrunde
		 . - Runde1
		 . Kämpfer1 ---------
		 .                 1 |---------
		 . Kämpfer2 ---------          |
		 .                             |
		 .                           7 |--------- Sieger1 vs Sieger2 ------
		 . Kämpfer3 ---------          |                                   |
		 .                 2 |---------                                    |
		 . Kämpfer4 ---------                                              |
		 .                                                                 |
		 .                                                              11 |------ 1. Platz / 2. Platz
		 . Kämpfer3 ---------                                              |
		 .                 3 |---------                                    |
		 . Kämpfer4 ---------          |                                   |
		 .                             |                                   |
		 .                           8 |--------- Sieger3 vs Sieger4 ------
		 . Kämpfer3 ---------          |
		 .                 4 |---------
		 . Kämpfer4 ---------
		 .
		 .
		 .
		 . - Trostrunde
		 . - Runde2
		 .            Verlierer aus 1 ---
		 .                             5 |---------------
		 .            Verlierer aus 2 ---                |
		 .                                             9 |------------- 3. Platz
		 .                         Verlierer aus 7 ------
		 .
		 .
		 .            Verlierer aus 3 ---
		 .               vs            6 |---------------
		 .            Verlierer aus 4 ---                |
		 .                                            10 |------------- 3. Platz
		 .                         Verlierer aus 8 ------
		 */
		List<WettkampfGruppeMitBegegnungen> wettkampfGruppenListe = WettkampfgruppeFixture.wettkampfGruppenListeFrauen;
		assertEquals(1, wettkampfGruppenListe.size());
		assertEquals(11, wettkampfGruppenListe.stream().mapToInt(wg -> wg.alleRundenBegegnungen().stream().mapToInt(g -> g.begegnungenJeRunde().size()).sum()).sum());
		WettkampfGruppeMitBegegnungen wettkampfGruppe1 = wettkampfGruppenListe.get(0);
		Sortierer sortierer = new Sortierer(1, 1);
		List<Runde> rundenAllerWettkampfgruppen = sortierer.erstelleReihenfolgeMitAllenGruppenJeDurchgang(wettkampfGruppenListe, 1).getRight();
		Runde runden1 = rundenAllerWettkampfgruppen.get(0);
		assertEquals(4, runden1.begegnungen().size()); // 4 Begegnungen Gewinnerrunde
		Begegnung begegnung1 = runden1.begegnungen().get(0);
		assertEquals(WettkampfgruppeFixture.b1UUID, begegnung1.getId());

		Runde runden2 = rundenAllerWettkampfgruppen.get(1);
		assertEquals(2, runden2.begegnungen().size()); // 2 Begegnungen in Trostrunde

		// Sieger aus Paarung1 und Paarung2 kommen zusammen in die gleiche Paarung/Begegnung in Runde 3
		Pair<Optional<Begegnung>,Optional<Begegnung>> begegnung = Sortierer.nachfolgeBegegnungen(begegnung1.getBegegnungId(), wettkampfGruppe1.gruppe(), rundenAllerWettkampfgruppen);


		assertTrue(begegnung.getLeft().isPresent());
		assertTrue(begegnung.getRight().isPresent());

		var sieger = begegnung.getLeft().get();
		var verlierer = begegnung.getRight().get();
		assertEquals(WettkampfgruppeFixture.b7UUID, sieger.getId());
		assertEquals(WettkampfgruppeFixture.b9UUID, verlierer.getId());
	}

	@Test
	public void sucheVorgaenger() {
		/**
		 .(Runde1, Siegerrunden, Paarung1)
		 .Kämpfer1 vs Kämpfer2          ---------         (Runde2, Siegerrunde, Paarung1)
		 .                                       |___________ Sieger1 vs Sieger2 ---------
		 .(Runde1, Siegerrunden, Paarung2)       |                                        |
		 .Kämpfer3 vs Kämpfer4          ---------                                         |          (Runde3, Siegerrunde, Paarung1)
		 .                                                                                |------------ Sieger5 vs Sieger6
		 .(Runde1, Siegerrunden, Paarung3)                                                |
		 .Kämpfer5 vs /                 ---------         (Runde2, Siegerrunde, Paarung2) |
		 .                                       |___________ Sieger3 vs Sieger4 ---------
		 .(Runde1, Siegerrunden, Paarung4)       |
		 .Kämpfer6 vs Kämpfer7          ---------
		 */
		List<WettkampfGruppeMitBegegnungen> wettkampfGruppenListe = WettkampfgruppeFixture.wettkampfGruppenListeFrauen;
		assertEquals(1, wettkampfGruppenListe.size());
		assertEquals(11, wettkampfGruppenListe.stream().mapToInt(wg -> wg.alleRundenBegegnungen().stream().mapToInt(g -> g.begegnungenJeRunde().size()).sum()).sum());
		WettkampfGruppeMitBegegnungen wettkampfGruppe1 = wettkampfGruppenListe.get(0);
		Sortierer sortierer = new Sortierer(1, 1);
		List<Runde> rundenAllerWettkampfgruppen = sortierer.erstelleReihenfolgeMitAllenGruppenJeDurchgang(wettkampfGruppenListe, 1).getRight();
		Runde runden2 = rundenAllerWettkampfgruppen.get(2);
		assertEquals(2, runden2.begegnungen().size()); // 2 Begegnungen Gewinnerrunde
		Runde runden3 = rundenAllerWettkampfgruppen.get(3);
		assertEquals(2, runden3.begegnungen().size()); // 2 Begegnungen in Trostrunde

		Begegnung begegnung7 = runden2.begegnungen().get(0);
		assertEquals(WettkampfgruppeFixture.b7UUID, begegnung7.getId());


		List<Begegnung> begegnungen = Sortierer.vorgaengerBegegnungen(begegnung7.getBegegnungId(), wettkampfGruppe1.gruppe(), rundenAllerWettkampfgruppen);


		assertEquals(2, begegnungen.size());
		assertEquals(WettkampfgruppeFixture.b1UUID, begegnungen.get(0).getId());
		assertEquals(WettkampfgruppeFixture.b2UUID, begegnungen.get(1).getId());
	}
}
