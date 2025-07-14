package de.sinnix.judoturnier.application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;


import com.fasterxml.jackson.core.JsonProcessingException;
import de.sinnix.judoturnier.adapter.secondary.BenutzerRepository;
import de.sinnix.judoturnier.adapter.secondary.TurnierRepository;
import de.sinnix.judoturnier.adapter.secondary.WertungRepository;
import de.sinnix.judoturnier.fixtures.BenutzerFixtures;
import de.sinnix.judoturnier.fixtures.GewichtsklassenGruppeFixture;
import de.sinnix.judoturnier.fixtures.MatteFixtures;
import de.sinnix.judoturnier.fixtures.WettkaempferFixtures;
import de.sinnix.judoturnier.fixtures.WettkampfgruppeFixture;
import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Begegnung;
import de.sinnix.judoturnier.model.Benutzer;
import de.sinnix.judoturnier.model.Einstellungen;
import de.sinnix.judoturnier.model.Geschlecht;
import de.sinnix.judoturnier.model.GewichtsklassenGruppe;
import de.sinnix.judoturnier.model.Gruppengroessen;
import de.sinnix.judoturnier.model.Matte;
import de.sinnix.judoturnier.model.MattenAnzahl;
import de.sinnix.judoturnier.model.Runde;
import de.sinnix.judoturnier.model.SeparateAlterklassen;
import de.sinnix.judoturnier.model.TurnierTyp;
import de.sinnix.judoturnier.model.VariablerGewichtsteil;
import de.sinnix.judoturnier.model.Wettkaempfer;
import de.sinnix.judoturnier.model.WettkampfGruppe;
import de.sinnix.judoturnier.model.WettkampfReihenfolge;
import de.sinnix.judoturnier.model.Wettkampfzeiten;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WettkampfServiceTest {

	private static final Logger logger = LogManager.getLogger(WettkampfServiceTest.class);

	@Mock
	private TurnierRepository      turnierRepository;
	@Mock
	private EinstellungenService   einstellungenService;
	@Mock
	private GewichtsklassenService gewichtsklassenService;
	@Mock
	private BenutzerRepository     benutzerRepository;
	@Mock
	private WertungRepository      wertungRepository;
	@Spy
	private Helpers                helpers = new Helpers();

	@InjectMocks
	private WettkampfService wettkampfService;

	private final UUID turnierUUID = MatteFixtures.turnierUUID;

	@BeforeEach
	void setUp() {
	}

	@Test
	void testErstelleWettkampfreihenfolgeAltersklasseRandori() {
		List<GewichtsklassenGruppe> gewichtsklassenGruppen = GewichtsklassenGruppeFixture.gewichtsklassenGruppen;
		Einstellungen einstellungen = new Einstellungen(TurnierTyp.RANDORI, new MattenAnzahl(2), WettkampfReihenfolge.ABWECHSELND, new Gruppengroessen(Map.of(Altersklasse.U9, 6, Altersklasse.U11, 6)), new VariablerGewichtsteil(0.2), SeparateAlterklassen.ZUSAMMEN, new Wettkampfzeiten(Map.of()), turnierUUID);

		when(einstellungenService.ladeEinstellungen(turnierUUID)).thenReturn(einstellungen);
		when(gewichtsklassenService.ladeGewichtsklassenGruppen(turnierUUID)).thenReturn(gewichtsklassenGruppen);
		ArgumentCaptor<List> gruppenCaptor = ArgumentCaptor.forClass(List.class);
		when(turnierRepository.speichereGruppen(gruppenCaptor.capture())).thenAnswer(invocation -> gruppenCaptor.getValue());

		wettkampfService.erstelleWettkampfreihenfolgeAltersklasse(Optional.empty(), turnierUUID);


		// ArgumentCaptor verwenden
		ArgumentCaptor<List<Matte>> argumentCaptor = ArgumentCaptor.forClass(List.class);
		verify(turnierRepository, times(1)).speichereMatten(argumentCaptor.capture());
		List<Matte> matten = argumentCaptor.getValue();
		// Anzahl der Matten
		assertEquals(2, matten.size());
		// Anzahl der GewichtsklassenGruppen
		assertEquals(5, gewichtsklassenGruppen.size());
		// Anzahl Teilnehmer insgesamt
		assertEquals(25, gewichtsklassenGruppen.stream().mapToInt(g -> g.teilnehmer().size()).sum());

		// 1. Runde für Gruppe A, 1. Runde auf der Matte, 1. Runde ingesamt
		assertEquals(1, matten.getFirst().runden().getFirst().mattenRunde());
		assertEquals(1, matten.getFirst().runden().getFirst().gruppenRunde());
		assertEquals(1, matten.getFirst().runden().getFirst().rundeGesamt());
		// 1. Runde für Gruppe B, 2. Runde auf der Matte, 2. Runde ingesamt
		assertEquals(2, matten.getFirst().runden().get(1).mattenRunde());
		assertEquals(1, matten.getFirst().runden().get(1).gruppenRunde());
		assertEquals(2, matten.getFirst().runden().get(1).rundeGesamt());
		// 1. Runde für Gruppe C, 3. Runde auf der Matte, 3. Runde ingesamt
		assertEquals(3, matten.getFirst().runden().get(2).mattenRunde());
		assertEquals(1, matten.getFirst().runden().get(2).gruppenRunde());
		assertEquals(3, matten.getFirst().runden().get(2).rundeGesamt());
		// 2. Runde für Gruppe A, 4. Runde auf der Matte, 4. Runde ingesamt
		assertEquals(4, matten.getFirst().runden().get(3).mattenRunde());
		assertEquals(2, matten.getFirst().runden().get(3).gruppenRunde());
		assertEquals(4, matten.getFirst().runden().get(3).rundeGesamt());

		// Anzahl der Begegnungen auf Matte 1
		assertEquals(45, matten.getFirst().runden().stream().mapToInt(r -> r.begegnungen().size()).sum());

		// Anzahl der Begegnungen auf Matte 1
		assertEquals(9, matten.get(1).runden().stream().mapToInt(r -> r.begegnungen().size()).sum());


		// da Jeder-gegen-Jeden nur je Gruppe gilt, muss die Anzahl für jede Gruppe separat geprüft werden
		int anzahlBegegnungen = matten.stream().mapToInt(m -> m.runden().stream().mapToInt(r -> r.begegnungen().size()).sum()).sum();
		int berechneteBegegnungen = 0;
		for (GewichtsklassenGruppe gruppe : gewichtsklassenGruppen) {
			var n = gruppe.teilnehmer().size();
			var N = (n * (n - 1)) / 2; // Berechnete Begegnungen in dieser Gruppe
			berechneteBegegnungen += N;
		}
		assertEquals(berechneteBegegnungen, anzahlBegegnungen);

		// Die MattenRunde wird immer erhöht
		assertTrue(IntStream.range(1, matten.getFirst().runden().size())
			.allMatch(i -> matten.getFirst().runden().get(i - 1).mattenRunde() < matten.getFirst().runden().get(i).mattenRunde()));
		assertTrue(IntStream.range(1, matten.get(1).runden().size())
			.allMatch(i -> matten.get(1).runden().get(i - 1).mattenRunde() < matten.get(1).runden().get(i).mattenRunde()));
	}

	@ParameterizedTest
	@EnumSource(WettkampfReihenfolge.class)
	void testErstelleWettkampfreihenfolgeAltersklasseNormal8Teilnehmer(WettkampfReihenfolge reihenfolge) {
		/*
		 1 Gruppe
		   Gruppe1: 8 Teilnehmer, 11 Begegnungen:
		   B1, B2, B3, B4: Runde 1 Gewinnerrunde

		   B5, B6: Runde 1 Trostrunde

		   B7, B8: Runde 2 Gewinnerrunde

		   B9, B10: Runde 2 Trostrunde

		   B11: Runde 3 Gewinnerrunde
		 */
		List<GewichtsklassenGruppe> gewichtsklassenGruppen = GewichtsklassenGruppeFixture.gewichtsklassenGruppe8Teilnehmer;
		Einstellungen einstellungen = new Einstellungen(TurnierTyp.STANDARD, new MattenAnzahl(1), reihenfolge, new Gruppengroessen(Map.of(Altersklasse.U9, 6, Altersklasse.U11, 50)), new VariablerGewichtsteil(0.2), SeparateAlterklassen.GETRENNT, new Wettkampfzeiten(Map.of()), turnierUUID);
		when(einstellungenService.ladeEinstellungen(turnierUUID)).thenReturn(einstellungen);
		when(gewichtsklassenService.ladeGewichtsklassenGruppen(turnierUUID)).thenReturn(gewichtsklassenGruppen);
		ArgumentCaptor<List> gruppenCaptor = ArgumentCaptor.forClass(List.class);
		when(turnierRepository.speichereGruppen(gruppenCaptor.capture())).thenAnswer(invocation -> gruppenCaptor.getValue());
		when(benutzerRepository.findBenutzerByUsername(Benutzer.ANONYMOUS_KAMPFRICHTER)).thenReturn(Optional.of(BenutzerFixtures.DUMMY_KAMPFRICHTER));


		// erstelle Wettkampfreihenfolge
		wettkampfService.erstelleWettkampfreihenfolgeAltersklasse(Optional.empty(), turnierUUID);


		// Prüfe
		ArgumentCaptor<List<Matte>> argumentCaptor = ArgumentCaptor.forClass(List.class);
		verify(turnierRepository, times(1)).speichereMatten(argumentCaptor.capture());
		List<Matte> matten = argumentCaptor.getValue();
		assertEquals(1, matten.size(), "Es sollte nur eine Matte geben");
		assertEquals(1, gewichtsklassenGruppen.size(), "Es sollte nur eine GewichtsklassenGruppe geben");
		assertEquals(8, gewichtsklassenGruppen.stream().mapToInt(g -> g.teilnehmer().size()).sum(), "Es sollten 8 Teilnehmer in der Gruppe sein");
		assertEquals(11, matten.stream().mapToInt(m -> m.runden().stream().mapToInt(r -> r.begegnungen().size()).sum()).sum(), "Es sollten 11 Begegnungen auf der Matte sein");
		assertEquals(5, matten.getFirst().runden().size(), "Es sollten 5 Runden geben (3 Gewinnerrunden, 2 Trostrunden)");

		var ersteRunde = matten.getFirst().runden().get(0); // 1. Runde für Gruppe, 1. Runde auf der Matte, 1. Runde ingesamt, Gewinnerrunde
		assertEquals(1, ersteRunde.gruppenRunde(), "GruppenRunde sollte 1 sein");
		assertEquals(1, ersteRunde.mattenRunde(), "MattenRunde sollte 1 sein");
		assertEquals(1, ersteRunde.rundeGesamt(), "RundeGesamt sollte 1 sein");
		assertEquals(4, ersteRunde.begegnungen().size(), "Anzahl der Begegnungen in dieser Runde sollte 4 sein");
		assertEquals(Begegnung.RundenTyp.GEWINNERRUNDE, ersteRunde.begegnungen().get(0).getBegegnungId().rundenTyp, "Begegnung 1 sollte eine Gewinnerrunde sein");
		assertTrue(ersteRunde.begegnungen().get(0).getWettkaempfer1().isPresent(), "Wettkaempfer 1 sollte vorhanden sein");
		assertTrue(ersteRunde.begegnungen().get(0).getWettkaempfer2().isPresent(), "Wettkaempfer 2 sollte vorhanden sein");
		assertEquals(Begegnung.RundenTyp.GEWINNERRUNDE, ersteRunde.begegnungen().get(1).getBegegnungId().rundenTyp, "Begegnung 2 sollte eine Gewinnerrunde sein");
		assertTrue(ersteRunde.begegnungen().get(1).getWettkaempfer1().isPresent(), "Wettkaempfer 1 sollte vorhanden sein");
		assertTrue(ersteRunde.begegnungen().get(1).getWettkaempfer2().isPresent(), "Wettkaempfer 2 sollte vorhanden sein");
		assertEquals(Begegnung.RundenTyp.GEWINNERRUNDE, ersteRunde.begegnungen().get(2).getBegegnungId().rundenTyp, "Begegnung 3 sollte eine Gewinnerrunde sein");
		assertTrue(ersteRunde.begegnungen().get(2).getWettkaempfer1().isPresent(), "Wettkaempfer 1 sollte vorhanden sein");
		assertTrue(ersteRunde.begegnungen().get(2).getWettkaempfer2().isPresent(), "Wettkaempfer 2 sollte vorhanden sein");
		assertEquals(Begegnung.RundenTyp.GEWINNERRUNDE, ersteRunde.begegnungen().get(3).getBegegnungId().rundenTyp, "Begegnung 4 sollte eine Gewinnerrunde sein");
		assertTrue(ersteRunde.begegnungen().get(3).getWettkaempfer1().isPresent(), "Wettkaempfer 1 sollte vorhanden sein");
		assertTrue(ersteRunde.begegnungen().get(3).getWettkaempfer2().isPresent(), "Wettkaempfer 2 sollte vorhanden sein");

		var zweiteRunde = matten.getFirst().runden().get(1); // 1. Runde für Gruppe, 2. Runde auf der Matte, 2. Runde ingesamt, Trostrunde
		assertEquals(1, zweiteRunde.gruppenRunde(), "GruppenRunde sollte 1 sein");
		assertEquals(2, zweiteRunde.mattenRunde(), "MattenRunde sollte 2 sein");
		assertEquals(2, zweiteRunde.rundeGesamt(), "RundeGesamt sollte 2 sein");
		assertEquals(2, zweiteRunde.begegnungen().size(), "Anzahl der Begegnungen in dieser Runde sollte 2 sein");
		assertEquals(Begegnung.RundenTyp.TROSTRUNDE, zweiteRunde.begegnungen().get(0).getBegegnungId().rundenTyp, "Begegnung 1 sollte eine Trostrunden sein");
		assertTrue(zweiteRunde.begegnungen().get(0).getWettkaempfer1().isEmpty(), "Trostrunde sollte leer sein");
		assertTrue(zweiteRunde.begegnungen().get(0).getWettkaempfer2().isEmpty(), "Trostrunde sollte leer sein");
		assertEquals(Begegnung.RundenTyp.TROSTRUNDE, zweiteRunde.begegnungen().get(1).getBegegnungId().rundenTyp, "Begegnung 2 sollte eine Trostrunden sein");
		assertTrue(zweiteRunde.begegnungen().get(1).getWettkaempfer1().isEmpty(), "Trostrunde sollte leer sein");
		assertTrue(zweiteRunde.begegnungen().get(1).getWettkaempfer2().isEmpty(), "Trostrunde sollte leer sein");

		var dritteRunde = matten.getFirst().runden().get(2); // 2. Runde für Gruppe, 3. Runde auf der Matte, 3. Runde ingesamt, Gewinnerrunde
		assertEquals(2, dritteRunde.gruppenRunde(), "GruppenRunde sollte 2 sein");
		assertEquals(3, dritteRunde.mattenRunde(), "MattenRunde sollte 3 sein");
		assertEquals(3, dritteRunde.rundeGesamt(), "RundeGesamt sollte 3 sein");
		assertEquals(2, dritteRunde.begegnungen().size(), "Anzahl der Begegnungen in dieser Runde sollte 2 sein");
		assertEquals(Begegnung.RundenTyp.GEWINNERRUNDE, dritteRunde.begegnungen().get(0).getBegegnungId().rundenTyp, "Begegnung 1 sollte eine Gewinnerrunde sein");
		assertTrue(dritteRunde.begegnungen().get(0).getWettkaempfer1().isEmpty(), "Wettkaempfer 1 sollte leer sein");
		assertTrue(dritteRunde.begegnungen().get(0).getWettkaempfer2().isEmpty(), "Wettkaempfer 2 sollte leer sein");
		assertEquals(Begegnung.RundenTyp.GEWINNERRUNDE, dritteRunde.begegnungen().get(1).getBegegnungId().rundenTyp, "Begegnung 2 sollte eine Gewinnerrunde sein");
		assertTrue(dritteRunde.begegnungen().get(1).getWettkaempfer1().isEmpty(), "Wettkaempfer 1 sollte leer sein");
		assertTrue(dritteRunde.begegnungen().get(1).getWettkaempfer2().isEmpty(), "Wettkaempfer 2 sollte leer sein");

		var vierteRunde = matten.getFirst().runden().get(3); // 2. Runde für Gruppe, 4. Runde auf der Matte, 4. Runde ingesamt, Trostrunde
		assertEquals(2, vierteRunde.gruppenRunde(), "GruppenRunde sollte 2 sein");
		assertEquals(4, vierteRunde.mattenRunde(), "MattenRunde sollte 4 sein");
		assertEquals(4, vierteRunde.rundeGesamt(), "RundeGesamt sollte 4 sein");
		assertEquals(2, vierteRunde.begegnungen().size(), "Anzahl der Begegnungen in dieser Runde sollte 2 sein");
		assertEquals(Begegnung.RundenTyp.TROSTRUNDE, vierteRunde.begegnungen().get(0).getBegegnungId().rundenTyp, "Begegnung 1 sollte eine Trostrunden sein");
		assertTrue(vierteRunde.begegnungen().get(0).getWettkaempfer1().isEmpty(), "Trostrunde sollte leer sein");
		assertTrue(vierteRunde.begegnungen().get(0).getWettkaempfer2().isEmpty(), "Trostrunde sollte leer sein");
		assertEquals(Begegnung.RundenTyp.TROSTRUNDE, vierteRunde.begegnungen().get(1).getBegegnungId().rundenTyp, "Begegnung 2 sollte eine Trostrunden sein");
		assertTrue(vierteRunde.begegnungen().get(1).getWettkaempfer1().isEmpty(), "Trostrunde sollte leer sein");
		assertTrue(vierteRunde.begegnungen().get(1).getWettkaempfer2().isEmpty(), "Trostrunde sollte leer sein");

		var fuenfteRunde = matten.getFirst().runden().get(4); // 3. Runde für Gruppe, 5. Runde auf der Matte, 5. Runde ingesamt, letzte Gewinnerrunde
		assertEquals(3, fuenfteRunde.gruppenRunde(), "GruppenRunde sollte 3 sein");
		assertEquals(5, fuenfteRunde.mattenRunde(), "MattenRunde sollte 5 sein");
		assertEquals(5, fuenfteRunde.rundeGesamt(), "RundeGesamt sollte 5 sein");
		assertEquals(1, fuenfteRunde.begegnungen().size(), "Anzahl der Begegnungen in dieser Runde sollte 1 (Finale) sein");
		assertEquals(Begegnung.RundenTyp.GEWINNERRUNDE, fuenfteRunde.begegnungen().get(0).getBegegnungId().rundenTyp, "Begegnung 1 sollte eine Gewinnerrunde sein");
		assertTrue(fuenfteRunde.begegnungen().get(0).getWettkaempfer1().isEmpty(), "Wettkaempfer 1 sollte leer sein");
		assertTrue(fuenfteRunde.begegnungen().get(0).getWettkaempfer2().isEmpty(), "Wettkaempfer 2 sollte leer sein");
	}

	@ParameterizedTest
	@EnumSource(WettkampfReihenfolge.class)
	void testErstelleWettkampfreihenfolgeAltersklasseNormalErweitertTeilnehmerMitFreilos(WettkampfReihenfolge reihenfolge) {
		/*
		 1 Gruppe
		   Gruppe1: 5 Teilnehmer, wird auf 8 Teilnehmer erweitert
		   11 Begegnungen:
		   B1, B2, B3, B4: Runde 1 Gewinnerrunde

		   B5, B6: Runde 1 Trostrunde

		   B7, B8: Runde 2 Gewinnerrunde

		   B9, B10: Runde 2 Trostrunde

		   B11: Runde 3 Gewinnerrunde
		 */
		List<GewichtsklassenGruppe> gewichtsklassenGruppen = Arrays.asList(
			new GewichtsklassenGruppe(GewichtsklassenGruppeFixture.gwkg1, Altersklasse.FRAUEN, Optional.of(Geschlecht.w), Arrays.asList(
				WettkaempferFixtures.wettkaempferin1,
				WettkaempferFixtures.wettkaempferin2,
				WettkaempferFixtures.wettkaempferin3,
				WettkaempferFixtures.wettkaempferin4,
				WettkaempferFixtures.wettkaempferin5), Optional.empty(), 58.0, 62.0, turnierUUID));
		WettkampfGruppe wettkampfGruppe = WettkampfgruppeFixture.gruppe1.gruppe();
		List<Begegnung> initialeBegegnungen = Arrays.asList(
			new Begegnung(UUID.randomUUID(), new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1), UUID.randomUUID(), 1, 1, 1, 1, Optional.of(WettkaempferFixtures.wettkaempferin1), Optional.empty(), List.of(), wettkampfGruppe, turnierUUID),
			new Begegnung(UUID.randomUUID(), new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 2), UUID.randomUUID(), 1, 1, 1, 2, Optional.of(WettkaempferFixtures.wettkaempferin2), Optional.empty(), List.of(), wettkampfGruppe, turnierUUID),
			new Begegnung(UUID.randomUUID(), new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 3), UUID.randomUUID(), 1, 1, 1, 3, Optional.of(WettkaempferFixtures.wettkaempferin3), Optional.empty(), List.of(), wettkampfGruppe, turnierUUID),
			new Begegnung(UUID.randomUUID(), new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 4), UUID.randomUUID(), 1, 1, 1, 4, Optional.of(WettkaempferFixtures.wettkaempferin4), Optional.of(WettkaempferFixtures.wettkaempferin5), List.of(), wettkampfGruppe, turnierUUID),
			new Begegnung(UUID.randomUUID(), new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 1, 1), UUID.randomUUID(), 1, 2, 1, 5, Optional.empty(), Optional.empty(), List.of(), wettkampfGruppe, turnierUUID),
			new Begegnung(UUID.randomUUID(), new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 1, 2), UUID.randomUUID(), 1, 2, 1, 6, Optional.empty(), Optional.empty(), List.of(), wettkampfGruppe, turnierUUID),
			new Begegnung(UUID.randomUUID(), new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 2, 1), UUID.randomUUID(), 1, 3, 2, 7, Optional.empty(), Optional.empty(), List.of(), wettkampfGruppe, turnierUUID),
			new Begegnung(UUID.randomUUID(), new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 2, 2), UUID.randomUUID(), 1, 3, 2, 8, Optional.empty(), Optional.empty(), List.of(), wettkampfGruppe, turnierUUID),
			new Begegnung(UUID.randomUUID(), new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 2, 1), UUID.randomUUID(), 1, 4, 2, 9, Optional.empty(), Optional.empty(), List.of(), wettkampfGruppe, turnierUUID),
			new Begegnung(UUID.randomUUID(), new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 2, 2), UUID.randomUUID(), 1, 4, 2, 10, Optional.empty(), Optional.empty(), List.of(), wettkampfGruppe, turnierUUID),
			new Begegnung(UUID.randomUUID(), new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 3, 1), UUID.randomUUID(), 1, 5, 3, 11, Optional.empty(), Optional.empty(), List.of(), wettkampfGruppe, turnierUUID)
		);
		List<Runde> runden = Arrays.asList(
			new Runde(UUID.randomUUID(), 1, 1, 1, 1, Altersklasse.FRAUEN, wettkampfGruppe, List.of(initialeBegegnungen.get(0), initialeBegegnungen.get(1), initialeBegegnungen.get(2), initialeBegegnungen.get(3))),
			new Runde(UUID.randomUUID(), 2, 1, 2, 1, Altersklasse.FRAUEN, wettkampfGruppe, List.of(initialeBegegnungen.get(4), initialeBegegnungen.get(5))),
			new Runde(UUID.randomUUID(), 3, 2, 3, 1, Altersklasse.FRAUEN, wettkampfGruppe, List.of(initialeBegegnungen.get(6), initialeBegegnungen.get(7))),
			new Runde(UUID.randomUUID(), 4, 2, 4, 1, Altersklasse.FRAUEN, wettkampfGruppe, List.of(initialeBegegnungen.get(8), initialeBegegnungen.get(9))),
			new Runde(UUID.randomUUID(), 5, 3, 5, 1, Altersklasse.FRAUEN, wettkampfGruppe, List.of(initialeBegegnungen.get(10)))
		);

		Einstellungen einstellungen = new Einstellungen(TurnierTyp.STANDARD, new MattenAnzahl(1), reihenfolge, new Gruppengroessen(Map.of(Altersklasse.FRAUEN, 8)), new VariablerGewichtsteil(0.2), SeparateAlterklassen.GETRENNT, new Wettkampfzeiten(Map.of()), turnierUUID);
		when(einstellungenService.ladeEinstellungen(turnierUUID)).thenReturn(einstellungen);
		when(gewichtsklassenService.ladeGewichtsklassenGruppen(turnierUUID)).thenReturn(gewichtsklassenGruppen);
		ArgumentCaptor<List> gruppenCaptor = ArgumentCaptor.forClass(List.class);
		when(turnierRepository.speichereGruppen(gruppenCaptor.capture())).thenAnswer(invocation -> gruppenCaptor.getValue());
		when(benutzerRepository.findBenutzerByUsername(Benutzer.ANONYMOUS_KAMPFRICHTER)).thenReturn(Optional.of(BenutzerFixtures.DUMMY_KAMPFRICHTER));
		when(turnierRepository.ladeAlleBegegnungen(turnierUUID)).thenReturn(initialeBegegnungen);
		when(turnierRepository.ladeWettkampfgruppeRunden(wettkampfGruppe.id(), turnierUUID)).thenReturn(runden);

		// anstatt ArgumentCaptor
		List<Begegnung> freilose = new ArrayList<>();
		doAnswer(invocation -> {
			Begegnung original = invocation.getArgument(0);
			freilose.add(deepCopyBegegnung(original));
			return null;
		}).when(turnierRepository).speichereBegegnung(any(Begegnung.class));


		// erstelle Wettkampfreihenfolge
		wettkampfService.erstelleWettkampfreihenfolgeAltersklasse(Optional.empty(), turnierUUID);


		// Prüfe
		verify(wertungRepository, times(3)).speichereWertungInBegegnung(any(), any());
		assertEquals(3, freilose.size(), "Es sollten 3 Freilose gespeichert werden");

		var freilos1 = freilose.get(0);
		assertTrue(freilos1.getWettkaempfer1().isPresent(), "Wettkaempfer sollte ein Freilos haben");
		assertTrue(freilos1.getWettkaempfer2().isEmpty(), "Dies sollte ein Freilos sein");
		assertEquals(WettkaempferFixtures.wettkaempferin1, freilos1.getWettkaempfer1().get(), "Wettkaempferin 1 erwartet");

		var freilos2 = freilose.get(1);
		assertTrue(freilos2.getWettkaempfer1().isPresent(), "Wettkaempfer sollte mit anderem Freilos kombiniert werden");
		assertTrue(freilos2.getWettkaempfer2().isPresent(), "Wettkaempfer sollte mit anderem Freilos kombiniert werden");
		assertEquals(WettkaempferFixtures.wettkaempferin1, freilos2.getWettkaempfer1().get(), "Wettkaempferin 1 erwartet");
		assertEquals(WettkaempferFixtures.wettkaempferin2, freilos2.getWettkaempfer2().get(), "Wettkaempferin 2 erwartet");

		var freilos3 = freilose.get(2);
		assertTrue(freilos3.getWettkaempfer1().isPresent(), "Wettkaempfer sollte ein Freilos haben");
		assertTrue(freilos3.getWettkaempfer2().isEmpty(), "Dies sollte ein Freilos sein");
		assertEquals(WettkaempferFixtures.wettkaempferin3, freilos3.getWettkaempfer1().get(), "Wettkaempferin 1 erwartet");
	}

	@Test
	void testMattenRundeIstKorrekt() {
		List<GewichtsklassenGruppe> gewichtsklassenGruppen = GewichtsklassenGruppeFixture.gewichtsklassenGruppenZweiAltersklassen;
		Einstellungen einstellungen = new Einstellungen(TurnierTyp.RANDORI, new MattenAnzahl(2), WettkampfReihenfolge.ABWECHSELND, new Gruppengroessen(Map.of(Altersklasse.U9, 6, Altersklasse.U11, 6, Altersklasse.U13, 6)), new VariablerGewichtsteil(0.2), SeparateAlterklassen.GETRENNT, new Wettkampfzeiten(Map.of()), turnierUUID);

		when(einstellungenService.ladeEinstellungen(turnierUUID)).thenReturn(einstellungen);
		when(gewichtsklassenService.ladeGewichtsklassenGruppen(turnierUUID)).thenReturn(gewichtsklassenGruppen);
		ArgumentCaptor<List> gruppenCaptor = ArgumentCaptor.forClass(List.class);
		when(turnierRepository.speichereGruppen(gruppenCaptor.capture())).thenAnswer(invocation -> gruppenCaptor.getValue());

		wettkampfService.erstelleWettkampfreihenfolgeAltersklasse(Optional.empty(), turnierUUID);


		ArgumentCaptor<List<Matte>> argumentCaptor = ArgumentCaptor.forClass(List.class);
		verify(turnierRepository, times(2)).speichereMatten(argumentCaptor.capture());
		List<List<Matte>> mattenListe = argumentCaptor.getAllValues();
		System.out.println(mattenListe);

		Integer erwarteteRundeGesamt = 21;

		assertEquals(2, mattenListe.size());
		assertEquals(erwarteteRundeGesamt, mattenListe.stream().mapToInt(matten -> matten.stream().mapToInt(matte -> matte.runden().size()).sum()).sum());
		assertEquals(erwarteteRundeGesamt, mattenListe.getLast().getLast().runden().getLast().rundeGesamt());
	}

	@Test
	void testErstelleWettkampfreihenfolgeAltersklasseNormal2() {
		List<GewichtsklassenGruppe> gewichtsklassenGruppen = GewichtsklassenGruppeFixture.gewichtsklassenGruppen;
		// 5 Gruppen
		//   Gruppe1: 6 Teilnehmer => wird auf 8 erweitert. 11 Begegnungen - doppelt KO
		//   Gruppe2: 6 Teilnehmer => wird auf 8 erweitert. 11 Begegnungen - doppelt KO
		//   Gruppe3: 6 Teilnehmer => wird auf 8 erweitert. 11 Begegnungen - doppelt KO
		//   Gruppe4: 4 Teilnehmer => bleibt bei 4.          6 Begegnungen - jeder gegen jeden
		//   Gruppe5: 3 Teilnehmer => bleibt bei 3.          3 Begegnungen - jeder gegen jeden

		Einstellungen einstellungen = new Einstellungen(TurnierTyp.STANDARD, new MattenAnzahl(2), WettkampfReihenfolge.ABWECHSELND, new Gruppengroessen(Map.of(Altersklasse.U9, 6, Altersklasse.U11, 50)), new VariablerGewichtsteil(0.2), SeparateAlterklassen.GETRENNT, new Wettkampfzeiten(Map.of()), turnierUUID);

		when(einstellungenService.ladeEinstellungen(turnierUUID)).thenReturn(einstellungen);
		when(gewichtsklassenService.ladeGewichtsklassenGruppen(turnierUUID)).thenReturn(gewichtsklassenGruppen);
		ArgumentCaptor<List> gruppenCaptor = ArgumentCaptor.forClass(List.class);
		when(turnierRepository.speichereGruppen(gruppenCaptor.capture())).thenAnswer(invocation -> gruppenCaptor.getValue());
		when(benutzerRepository.findBenutzerByUsername(Benutzer.ANONYMOUS_KAMPFRICHTER)).thenReturn(Optional.of(BenutzerFixtures.DUMMY_KAMPFRICHTER));

		wettkampfService.erstelleWettkampfreihenfolgeAltersklasse(Optional.empty(), turnierUUID);


		// ArgumentCaptor verwenden
		ArgumentCaptor<List<Matte>> argumentCaptor = ArgumentCaptor.forClass(List.class);
		verify(turnierRepository, times(1)).speichereMatten(argumentCaptor.capture());
		List<Matte> matten = argumentCaptor.getValue();

		// Anzahl der Matten
		assertEquals(2, matten.size());
		// Anzahl der GewichtsklassenGruppen
		assertEquals(5, gewichtsklassenGruppen.size());
		// Anzahl Teilnehmer insgesamt
		assertEquals(25, gewichtsklassenGruppen.stream().mapToInt(g -> g.teilnehmer().size()).sum());
		// Anzahl aller Begegnungen
		assertEquals(42, matten.stream().mapToInt(m -> m.runden().stream().mapToInt(r -> r.begegnungen().size()).sum()).sum());

		// 1. Runde für Gruppe A, 1. Runde auf der Matte, 1. Runde ingesamt
		assertEquals(1, matten.getFirst().runden().getFirst().mattenRunde());
		assertEquals(1, matten.getFirst().runden().getFirst().gruppenRunde());
		assertEquals(1, matten.getFirst().runden().getFirst().rundeGesamt());
		// 1. Runde für Gruppe B, 2. Runde auf der Matte, 2. Runde ingesamt
		assertEquals(2, matten.getFirst().runden().get(1).mattenRunde());
		assertEquals(1, matten.getFirst().runden().get(1).gruppenRunde());
		assertEquals(2, matten.getFirst().runden().get(1).rundeGesamt());
		// 1. Runde für Gruppe C, 3. Runde auf der Matte, 3. Runde ingesamt
		assertEquals(3, matten.getFirst().runden().get(2).mattenRunde());
		assertEquals(1, matten.getFirst().runden().get(2).gruppenRunde());
		assertEquals(3, matten.getFirst().runden().get(2).rundeGesamt());
		// 2. Runde für Gruppe A, 4. Runde auf der Matte, 4. Runde ingesamt
		assertEquals(4, matten.getFirst().runden().get(3).mattenRunde());
		assertEquals(2, matten.getFirst().runden().get(3).gruppenRunde());
		assertEquals(4, matten.getFirst().runden().get(3).rundeGesamt());

		// Anzahl der Begegnungen auf Matte 1
		assertEquals(33, matten.getFirst().runden().stream().mapToInt(r -> r.begegnungen().size()).sum());

		// Anzahl der Begegnungen auf Matte 2
		assertEquals(9, matten.get(1).runden().stream().mapToInt(r -> r.begegnungen().size()).sum());

		// Die MattenRunde wird immer erhöht
		assertTrue(IntStream.range(1, matten.getFirst().runden().size())
			.allMatch(i -> matten.getFirst().runden().get(i - 1).mattenRunde() < matten.getFirst().runden().get(i).mattenRunde()));
		assertTrue(IntStream.range(1, matten.get(1).runden().size())
			.allMatch(i -> matten.get(1).runden().get(i - 1).mattenRunde() < matten.get(1).runden().get(i).mattenRunde()));
	}

	private Begegnung deepCopyBegegnung(Begegnung original) throws JsonProcessingException {
		Begegnung b = new Begegnung();
		b.setId(original.getId());
		b.setBegegnungId(original.getBegegnungId());
		b.setRundeId(original.getRundeId());
		b.setMatteId(original.getMatteId());
		b.setMattenRunde(original.getMattenRunde());
		b.setGruppenRunde(original.getGruppenRunde());
		b.setGesamtBegegnung(original.getGesamtBegegnung());
		b.setWettkaempfer1(original.getWettkaempfer1().map(w -> copyWettkaempfer(w))); // copy selbst definieren
		b.setWettkaempfer2(original.getWettkaempfer2().map(w -> copyWettkaempfer(w)));
		b.setWertungen(new ArrayList<>(original.getWertungen())); // ggf. tiefer kopieren
		b.setWettkampfGruppe(original.getWettkampfGruppe());
		b.setTurnierUUID(original.getTurnierUUID());
		return b;
	}

	private Wettkaempfer copyWettkaempfer(Wettkaempfer w) {
		return new Wettkaempfer(w.id(), w.name(), w.geschlecht(), w.altersklasse(), w.verein(), w.gewicht(), w.farbe(), w.checked(), w.printed(), w.turnierUUID());
	}
}
