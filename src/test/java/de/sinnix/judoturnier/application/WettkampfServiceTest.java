package de.sinnix.judoturnier.application;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;


import de.sinnix.judoturnier.adapter.secondary.TurnierRepository;
import de.sinnix.judoturnier.fixtures.GewichtsklassenGruppeFixture;
import de.sinnix.judoturnier.fixtures.MatteFixtures;
import de.sinnix.judoturnier.fixtures.WettkaempferFixtures;
import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Begegnung;
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
import de.sinnix.judoturnier.model.WettkampfReihenfolge;
import de.sinnix.judoturnier.model.Wettkampfzeiten;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

	@Spy
	private Helpers   helpers   = new Helpers();

	@InjectMocks
	private WettkampfService wettkampfService;

	private final UUID     turnierUUID = MatteFixtures.turnierUUID;

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

	@Test
	void testErstelleWettkampfreihenfolgeAltersklasseNormal() {
		List<GewichtsklassenGruppe> gewichtsklassenGruppen = GewichtsklassenGruppeFixture.gewichtsklassenGruppen;
		// 5 Gruppen
		//   Gruppe1: 6 Teilnehmer => wird auf 8 erweitert. 11 Begegnungen
		//   Gruppe2: 6 Teilnehmer => wird auf 8 erweitert. 11 Begegnungen
		//   Gruppe3: 6 Teilnehmer => wird auf 8 erweitert. 11 Begegnungen
		//   Gruppe4: 4 Teilnehmer => bleibt bei 4.          3 Begegnungen
		//   Gruppe5: 3 Teilnehmer => wird auf 4 erweitert.  3 Begegnungen

		Einstellungen einstellungen = new Einstellungen(TurnierTyp.STANDARD, new MattenAnzahl(2), WettkampfReihenfolge.ABWECHSELND, new Gruppengroessen(Map.of(Altersklasse.U9, 6, Altersklasse.U11, 50)), new VariablerGewichtsteil(0.2), SeparateAlterklassen.GETRENNT, new Wettkampfzeiten(Map.of()), turnierUUID);

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
		// Anzahl aller Begegnungen
		assertEquals(39, matten.stream().mapToInt(m -> m.runden().stream().mapToInt(r -> r.begegnungen().size()).sum()).sum());

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
		assertEquals(6, matten.get(1).runden().stream().mapToInt(r -> r.begegnungen().size()).sum());

		for (Matte matte : matten) {
			for (Runde runde : matte.runden()) {
				logger.debug("Runde {}  Gruppe: {}", runde.gruppenRunde(), runde.gruppe().name());
				for (Begegnung begegnung : runde.begegnungen()) {
					logger.debug("{} - Wettkaempfer1: {}, Wettkaempfer2: {}", begegnung.getBegegnungId(), begegnung.getWettkaempfer1().map(Wettkaempfer::name), begegnung.getWettkaempfer2().map(Wettkaempfer::name));
				}
			}
		}

		// Die MattenRunde wird immer erhöht
		assertTrue(IntStream.range(1, matten.getFirst().runden().size())
			.allMatch(i -> matten.getFirst().runden().get(i - 1).mattenRunde() < matten.getFirst().runden().get(i).mattenRunde()));
		assertTrue(IntStream.range(1, matten.get(1).runden().size())
			.allMatch(i -> matten.get(1).runden().get(i - 1).mattenRunde() < matten.get(1).runden().get(i).mattenRunde()));
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
		//   Gruppe1: 6 Teilnehmer => wird auf 8 erweitert. 11 Begegnungen
		//   Gruppe2: 6 Teilnehmer => wird auf 8 erweitert. 11 Begegnungen
		//   Gruppe3: 6 Teilnehmer => wird auf 8 erweitert. 11 Begegnungen
		//   Gruppe4: 4 Teilnehmer => bleibt bei 4.          3 Begegnungen
		//   Gruppe5: 3 Teilnehmer => wird auf 4 erweitert.  3 Begegnungen

		Einstellungen einstellungen = new Einstellungen(TurnierTyp.STANDARD, new MattenAnzahl(2), WettkampfReihenfolge.ABWECHSELND, new Gruppengroessen(Map.of(Altersklasse.U9, 6, Altersklasse.U11, 50)), new VariablerGewichtsteil(0.2), SeparateAlterklassen.GETRENNT, new Wettkampfzeiten(Map.of()), turnierUUID);

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
		// Anzahl aller Begegnungen
		assertEquals(39, matten.stream().mapToInt(m -> m.runden().stream().mapToInt(r -> r.begegnungen().size()).sum()).sum());

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
		assertEquals(6, matten.get(1).runden().stream().mapToInt(r -> r.begegnungen().size()).sum());

		for (Matte matte : matten) {
			for (Runde runde : matte.runden()) {
				logger.debug("Runde {}  Gruppe: {}", runde.gruppenRunde(), runde.gruppe().name());
				for (Begegnung begegnung : runde.begegnungen()) {
					logger.debug("{} - Wettkaempfer1: {}, Wettkaempfer2: {}", begegnung.getBegegnungId(), begegnung.getWettkaempfer1().map(Wettkaempfer::name), begegnung.getWettkaempfer2().map(Wettkaempfer::name));
				}
			}
		}

		// Die MattenRunde wird immer erhöht
		assertTrue(IntStream.range(1, matten.getFirst().runden().size())
			.allMatch(i -> matten.getFirst().runden().get(i - 1).mattenRunde() < matten.getFirst().runden().get(i).mattenRunde()));
		assertTrue(IntStream.range(1, matten.get(1).runden().size())
			.allMatch(i -> matten.get(1).runden().get(i - 1).mattenRunde() < matten.get(1).runden().get(i).mattenRunde()));
	}

	@Test
	void testTurnierverlaufAltersklasseNormal() {
		// 1 Gruppe
		//   Gruppe1: 6 Teilnehmer => wird auf 8 erweitert. 11 Begegnungen
		List<GewichtsklassenGruppe> gewichtsklassenGruppen = Arrays.asList(
			new GewichtsklassenGruppe(GewichtsklassenGruppeFixture.gwkg1, Altersklasse.Frauen, Optional.of(Geschlecht.w), Arrays.asList(
				WettkaempferFixtures.wettkaempferin1,
				WettkaempferFixtures.wettkaempferin2,
				WettkaempferFixtures.wettkaempferin3,
				WettkaempferFixtures.wettkaempferin4,
				WettkaempferFixtures.wettkaempferin5,
				WettkaempferFixtures.wettkaempferin6), Optional.empty(), 58.0, 62.0, turnierUUID));

		Einstellungen einstellungen = new Einstellungen(TurnierTyp.STANDARD, new MattenAnzahl(2), WettkampfReihenfolge.ABWECHSELND, new Gruppengroessen(Map.of(Altersklasse.Frauen, 8)), new VariablerGewichtsteil(0.2), SeparateAlterklassen.GETRENNT, new Wettkampfzeiten(Map.of()), turnierUUID);

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
		assertEquals(1, gewichtsklassenGruppen.size());
		// Anzahl Teilnehmer insgesamt
		assertEquals(6, gewichtsklassenGruppen.stream().mapToInt(g -> g.teilnehmer().size()).sum());
		// Anzahl aller Begegnungen
		assertEquals(11, matten.stream().mapToInt(m -> m.runden().stream().mapToInt(r -> r.begegnungen().size()).sum()).sum());
		// Alle Begegnungungen nur auf Matte 1
		Matte matte1 = matten.getFirst();
		assertEquals(11, matte1.runden().stream().mapToInt(r -> r.begegnungen().size()).sum());

		// Anzahl der Runden: 3 Gewinnerrunden, x Trostrunden
		assertEquals(3, matte1.runden().size());

		logger.debug("1 ------------------------------");

		// 1. Runde
		Runde runde1 = matte1.runden().get(0);
		logger.debug("Runde 1: {}", runde1);
//		runde1.gruppe().alleRundenBegegnungen()
//			.forEach(br -> br.begegnungenJeRunde()
//				.forEach(b -> logger.debug("[Runde {}] Begegnung: {}", b.getBegegnungId(), (b.getWettkaempfer1().isPresent() ? b.getWettkaempfer1().get().name() : "/")  + " - " + (b.getWettkaempfer2().isPresent() ? b.getWettkaempfer2().get().name() : "/"))));
//
//		// 2. Runde
//		Runde runde2 = matte1.runden().get(1);
//		logger.debug("Runde 2: {}", runde2);
//		runde2.gruppe().alleRundenBegegnungen()
//			.forEach(br -> br.begegnungenJeRunde()
//				.forEach(b -> logger.debug("[Runde {}] Begegnung: {}", b.getBegegnungId(),(b.getWettkaempfer1().isPresent() ? b.getWettkaempfer1().get().name() : "/")  + " - " + (b.getWettkaempfer2().isPresent() ? b.getWettkaempfer2().get().name() : "/"))));
//
//		// 3. Runde
//		Runde runde3 = matte1.runden().get(2);
//		logger.debug("Runde 3: {}", runde3);
//		runde3.gruppe().alleRundenBegegnungen()
//			.forEach(br -> br.begegnungenJeRunde()
//				.forEach(b -> logger.debug("[Runde {}] Begegnung: {}", b.getBegegnungId(), (b.getWettkaempfer1().isPresent() ? b.getWettkaempfer1().get().name() : "/")  + " - " + (b.getWettkaempfer2().isPresent() ? b.getWettkaempfer2().get().name() : "/"))));

		logger.debug("2 ------------------------------");
		for (Runde runde : matte1.runden()) {
			logger.debug("Runde {}  Gruppe: {}", runde.gruppenRunde(), runde.gruppe().id());
			for (Begegnung begegnung : runde.begegnungen()) {
				logger.debug("{} - Wettkaempfer1: {}, Wettkaempfer2: {}", begegnung.getBegegnungId(), begegnung.getWettkaempfer1().map(Wettkaempfer::name), begegnung.getWettkaempfer2().map(Wettkaempfer::name));
			}
		}
	}

}
