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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

		assertEquals(7, runden.size());
		assertEquals(3, kampfRunden(runden).size());
		assertEquals(4, anzahlPausen(runden));
		var sortierteBegegnungen = anzahlKampfBegegnungen(runden);
		assertEquals(N, sortierteBegegnungen);
		assertMindestensZweiRundenPauseJeGruppe(runden);
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

		assertEquals(7, runden.size());
		assertEquals(3, kampfRunden(runden).size());
		assertEquals(4, anzahlPausen(runden));
		var sortierteBegegnungen = anzahlKampfBegegnungen(runden);
		assertEquals(N, sortierteBegegnungen);
		assertMindestensZweiRundenPauseJeGruppe(runden);
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

		assertEquals(18, kampfRunden(runden).size());
		var sortierteBegegnungen = anzahlKampfBegegnungen(runden);
		assertEquals(51, sortierteBegegnungen);
		assertMindestensZweiRundenPauseJeGruppe(runden);
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


		var anzahlRunden = 4 + 3; // 4 Kampfrunden plus 3 Pausen
		assertEquals(anzahlRunden, runden.size());
		assertEquals(4, kampfRunden(runden).size());
		assertEquals(3, anzahlPausen(runden));

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

		// PAUSE
		assertEquals(3, runden.get(2).rundeGesamt());
		assertEquals(3, runden.get(2).mattenRunde());
		assertEquals(2, runden.get(2).gruppenRunde());
		assertEquals(1, runden.get(2).begegnungen().size());
		assertEquals(Altersklasse.PAUSE, runden.get(2).altersklasse());
		assertTrue(runden.get(2).begegnungen().get(0).getBegegnungId() != null);

		assertEquals(4, runden.get(3).rundeGesamt());
		assertEquals(4, runden.get(3).mattenRunde());
		assertEquals(2, runden.get(3).gruppenRunde());
		assertEquals(2, runden.get(3).begegnungen().size());

		assertEquals(Altersklasse.PAUSE, runden.get(4).altersklasse());
		assertEquals(Altersklasse.PAUSE, runden.get(5).altersklasse());

		assertEquals(7, runden.get(6).rundeGesamt());
		assertEquals(7, runden.get(6).mattenRunde());
		assertEquals(3, runden.get(6).gruppenRunde());
		assertEquals(2, runden.get(6).begegnungen().size());
		assertMindestensZweiRundenPauseJeGruppe(runden);
	}

	@Test
	public void trenntGewinnerrundeUndTrostrundeBeiAbwechselndenGruppen() {
		WettkampfGruppe gruppe1 = new WettkampfGruppe(UUID.randomUUID(), "Gruppe 1", "(Gewichtskl.1 U11)", Altersklasse.U11, turnierUUID);
		WettkampfGruppe gruppe2 = new WettkampfGruppe(UUID.randomUUID(), "Gruppe 2", "(Gewichtskl.2 U11)", Altersklasse.U11, turnierUUID);
		Begegnung gruppe1Gewinnerrunde = begegnung(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1);
		Begegnung gruppe1Trostrunde = begegnung(Begegnung.RundenTyp.TROSTRUNDE, 1, 1);
		Begegnung gruppe2Gewinnerrunde = begegnung(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1);
		Begegnung gruppe2Trostrunde = begegnung(Begegnung.RundenTyp.TROSTRUNDE, 1, 1);
		List<WettkampfGruppeMitBegegnungen> wettkampfGruppeList = List.of(
			new WettkampfGruppeMitBegegnungen(gruppe1, List.of(new BegegnungenJeRunde(List.of(gruppe1Gewinnerrunde, gruppe1Trostrunde)))),
			new WettkampfGruppeMitBegegnungen(gruppe2, List.of(new BegegnungenJeRunde(List.of(gruppe2Gewinnerrunde, gruppe2Trostrunde))))
		);

		Sortierer sortierer = new Sortierer(1, 1);
		List<Runde> runden = sortierer.erstelleReihenfolgeMitAbwechselndenGruppen(wettkampfGruppeList, 1).getRight();

		assertEquals(5, runden.size());
		assertEquals(List.of(gruppe1Gewinnerrunde), runden.get(0).begegnungen());
		assertEquals(List.of(gruppe2Gewinnerrunde), runden.get(1).begegnungen());
		assertEquals(Altersklasse.PAUSE, runden.get(2).altersklasse());
		assertEquals(List.of(gruppe1Trostrunde), runden.get(3).begegnungen());
		assertEquals(List.of(gruppe2Trostrunde), runden.get(4).begegnungen());
		assertTrue(runden.stream().allMatch(runde -> runde.gruppenRunde() == 1));
		assertMindestensZweiRundenPauseJeGruppe(runden);
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
		List<Runde> kampfRundenListe = kampfRunden(rundenAllerWettkampfgruppen);
		Runde runden1 = kampfRundenListe.get(0);
		assertEquals(4, runden1.begegnungen().size()); // 4 Begegnungen Gewinnerrunde
		Begegnung begegnung1 = runden1.begegnungen().get(0);
		assertEquals(WettkampfgruppeFixture.b1UUID, begegnung1.getId());

		Runde runden2 = kampfRundenListe.get(1);
		assertEquals(2, runden2.begegnungen().size()); // 2 Begegnungen in Trostrunde

		// Sieger aus Paarung1 und Paarung2 kommen zusammen in die gleiche Paarung/Begegnung in Runde 3
		Pair<Optional<Begegnung>,Optional<Begegnung>> begegnung = Sortierer.nachfolgeBegegnungen(begegnung1.getBegegnungId(), wettkampfGruppe1.gruppe(), rundenAllerWettkampfgruppen);


		assertTrue(begegnung.getLeft().isPresent());
		assertTrue(begegnung.getRight().isPresent());

		var sieger = begegnung.getLeft().get();
		var verlierer = begegnung.getRight().get();
		assertEquals(WettkampfgruppeFixture.b7UUID, sieger.getId());
		assertEquals(WettkampfgruppeFixture.b5UUID, verlierer.getId());

		Pair<Optional<Begegnung>,Optional<Begegnung>> trostNachfolger = Sortierer.nachfolgeBegegnungen(verlierer.getBegegnungId(), wettkampfGruppe1.gruppe(), rundenAllerWettkampfgruppen);
		assertTrue(trostNachfolger.getLeft().isEmpty());
		assertTrue(trostNachfolger.getRight().isPresent());
		assertEquals(WettkampfgruppeFixture.b9UUID, trostNachfolger.getRight().get().getId());
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
		List<Runde> kampfRundenListe = kampfRunden(rundenAllerWettkampfgruppen);
		Runde runden2 = kampfRundenListe.get(2);
		assertEquals(2, runden2.begegnungen().size()); // 2 Begegnungen Gewinnerrunde
		Runde runden3 = kampfRundenListe.get(3);
		assertEquals(2, runden3.begegnungen().size()); // 2 Begegnungen in Trostrunde

		Begegnung begegnung7 = runden2.begegnungen().get(0);
		assertEquals(WettkampfgruppeFixture.b7UUID, begegnung7.getId());


		List<Begegnung> begegnungen = Sortierer.vorgaengerBegegnungen(begegnung7.getBegegnungId(), wettkampfGruppe1.gruppe(), rundenAllerWettkampfgruppen);


		assertEquals(2, begegnungen.size());
		assertEquals(WettkampfgruppeFixture.b1UUID, begegnungen.get(0).getId());
		assertEquals(WettkampfgruppeFixture.b2UUID, begegnungen.get(1).getId());
	}

	private List<Runde> kampfRunden(List<Runde> runden) {
		return runden.stream()
			.filter(runde -> !runde.istPause())
			.toList();
	}

	private int anzahlKampfBegegnungen(List<Runde> runden) {
		return kampfRunden(runden).stream()
			.mapToInt(runde -> runde.begegnungen().size())
			.sum();
	}

	private long anzahlPausen(List<Runde> runden) {
		return runden.stream()
			.filter(Runde::istPause)
			.count();
	}

	private void assertMindestensZweiRundenPauseJeGruppe(List<Runde> runden) {
		Map<WettkampfGruppe,Integer> letzteMattenRundeJeGruppe = new HashMap<>();
		for (Runde runde : runden) {
			if (runde.istPause()) {
				continue;
			}
			Integer letzteMattenRunde = letzteMattenRundeJeGruppe.put(runde.gruppe(), runde.mattenRunde());
			if (letzteMattenRunde != null) {
				assertTrue(runde.mattenRunde() - letzteMattenRunde - 1 >= 2);
			}
		}
	}

	private Begegnung begegnung(Begegnung.RundenTyp rundenTyp, int rundenNummerDesTyps, int paarungNummer) {
		Begegnung begegnung = new Begegnung();
		begegnung.setBegegnungId(new Begegnung.BegegnungId(rundenTyp, rundenNummerDesTyps, paarungNummer));
		begegnung.setWettkaempfer1(Optional.empty());
		begegnung.setWettkaempfer2(Optional.empty());
		begegnung.setTurnierUUID(turnierUUID);
		return begegnung;
	}
}
