package de.sinnix.judoturnier.application;

import de.sinnix.judoturnier.adapter.secondary.BenutzerRepository;
import de.sinnix.judoturnier.adapter.secondary.TurnierRepository;
import de.sinnix.judoturnier.fixtures.GewichtsklassenGruppeFixture;
import de.sinnix.judoturnier.fixtures.MatteFixtures;
import de.sinnix.judoturnier.fixtures.WettkaempferFixtures;
import de.sinnix.judoturnier.fixtures.WettkampfgruppeFixture;
import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Begegnung;
import de.sinnix.judoturnier.model.Benutzer;
import de.sinnix.judoturnier.model.BenutzerRolle;
import de.sinnix.judoturnier.model.Einstellungen;
import de.sinnix.judoturnier.model.GewichtsklassenGruppe;
import de.sinnix.judoturnier.model.Gruppengroessen;
import de.sinnix.judoturnier.model.Matte;
import de.sinnix.judoturnier.model.MattenAnzahl;
import de.sinnix.judoturnier.model.Runde;
import de.sinnix.judoturnier.model.SeparateAlterklassen;
import de.sinnix.judoturnier.model.TurnierTyp;
import de.sinnix.judoturnier.model.VariablerGewichtsteil;
import de.sinnix.judoturnier.model.Wertung;
import de.sinnix.judoturnier.model.Wettkaempfer;
import de.sinnix.judoturnier.model.WettkampfGruppe;
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

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TurnierServiceTest {

	private static final Logger logger = LogManager.getLogger(TurnierServiceTest.class);

	@Mock
	private TurnierRepository      turnierRepository;
	@Mock
	private EinstellungenService   einstellungenService;
	@Mock
	private GewichtsklassenService gewichtsklassenService;
	@Mock
	private BenutzerRepository     benutzerRepository;
	@Mock
	private WettkaempferService    wettkaempferService;

	@Spy
	private Sortierer sortierer = new Sortierer(1, 1);
	@Spy
	private Helpers   helpers   = new Helpers();

	@InjectMocks
	private TurnierService turnierService;

	private UUID     turnierUUID = MatteFixtures.turnierUUID;
	private Benutzer benutzer;

	@BeforeEach
	void setUp() {
		benutzer = new Benutzer(UUID.fromString("898a7fcf-2fad-4ec9-8b4f-5513188af291"), "user1", "Name, Vorname", List.of(), List.of(BenutzerRolle.KAMPFRICHTER));
	}

	@Test
	void testErstelleWettkampfreihenfolgeAltersklasseRandori() {
		List<GewichtsklassenGruppe> gewichtsklassenGruppen = GewichtsklassenGruppeFixture.gewichtsklassenGruppen;
		Einstellungen einstellungen = new Einstellungen(TurnierTyp.RANDORI, new MattenAnzahl(2), WettkampfReihenfolge.ABWECHSELND, new Gruppengroessen(Map.of(Altersklasse.U9, 6, Altersklasse.U11, 6)), new VariablerGewichtsteil(0.2), SeparateAlterklassen.ZUSAMMEN, new Wettkampfzeiten(Map.of()), turnierUUID);

		when(einstellungenService.ladeEinstellungen(turnierUUID)).thenReturn(einstellungen);
		when(gewichtsklassenService.ladeGewichtsklassenGruppen(turnierUUID)).thenReturn(gewichtsklassenGruppen);

		turnierService.erstelleWettkampfreihenfolgeAltersklasse(Optional.empty(), turnierUUID);


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
		assertEquals(1, matten.get(0).runden().get(0).mattenRunde());
		assertEquals(1, matten.get(0).runden().get(0).gruppenRunde());
		assertEquals(1, matten.get(0).runden().get(0).rundeGesamt());
		// 1. Runde für Gruppe B, 2. Runde auf der Matte, 2. Runde ingesamt
		assertEquals(2, matten.get(0).runden().get(1).mattenRunde());
		assertEquals(1, matten.get(0).runden().get(1).gruppenRunde());
		assertEquals(2, matten.get(0).runden().get(1).rundeGesamt());
		// 1. Runde für Gruppe C, 3. Runde auf der Matte, 3. Runde ingesamt
		assertEquals(3, matten.get(0).runden().get(2).mattenRunde());
		assertEquals(1, matten.get(0).runden().get(2).gruppenRunde());
		assertEquals(3, matten.get(0).runden().get(2).rundeGesamt());
		// 2. Runde für Gruppe A, 4. Runde auf der Matte, 4. Runde ingesamt
		assertEquals(4, matten.get(0).runden().get(3).mattenRunde());
		assertEquals(2, matten.get(0).runden().get(3).gruppenRunde());
		assertEquals(4, matten.get(0).runden().get(3).rundeGesamt());

		// Anzahl der Begegnungen auf Matte 1
		assertEquals(45, matten.get(0).runden().stream().mapToInt(r -> r.begegnungen().size()).sum());

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
		assertTrue(IntStream.range(1, matten.get(0).runden().size())
			.allMatch(i -> matten.get(0).runden().get(i - 1).mattenRunde() < matten.get(0).runden().get(i).mattenRunde()));
		assertTrue(IntStream.range(1, matten.get(1).runden().size())
			.allMatch(i -> matten.get(1).runden().get(i - 1).mattenRunde() < matten.get(1).runden().get(i).mattenRunde()));
	}

	@Test
	void testAktualisiereExistierendeRandoriWertung() {
		UUID turnierUUID = UUID.randomUUID();
		UUID rundeUUID = UUID.randomUUID();
		UUID id = UUID.randomUUID();
		List<Wertung> wertungList = new ArrayList<>();
		Wertung alteWertung = new Wertung(UUID.randomUUID(), null, null, null, null, null, null,
			1, 2, 3, 4, 4, 3, 2, 1,
			benutzer
		);
		wertungList.add(alteWertung);
		Begegnung.BegegnungId begegnungId = new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1);
		Begegnung begegnung = new Begegnung(id, begegnungId, rundeUUID,
			1, 2, 3, 3,
			WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2,
			wertungList,
			WettkampfgruppeFixture.gruppe1,
			turnierUUID
		);

		List<Wertung> neueWertungList = new ArrayList<>();
		Wertung neueWertung = new Wertung(alteWertung.getUuid(), null, null, null, null, null, null,
			1, 2, 3, 4, 4, 3, 2, 1,
			benutzer
		);
		neueWertungList.add(neueWertung);
		Begegnung updatedBegegnung = new Begegnung(id, begegnungId, rundeUUID,
			1, 2, 3, 3,
			WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2,
			neueWertungList,
			WettkampfgruppeFixture.gruppe1,
			turnierUUID
		);


		when(turnierRepository.ladeBegegnung(id)).thenReturn(begegnung);
		when(benutzerRepository.findBenutzer(benutzer.uuid())).thenReturn(Optional.of(benutzer));

		turnierService.speichereRandoriWertung(id, 1, 2, 3, 4, 4, 3, 2, 1, benutzer.uuid());

		ArgumentCaptor<Begegnung> argumentCaptor = ArgumentCaptor.forClass(Begegnung.class);
		verify(turnierRepository).speichereBegegnung(argumentCaptor.capture());
		Begegnung result = argumentCaptor.getValue();
		assertEquals(neueWertungList, result.getWertungen());
	}

	@Test
	void testWeitereRandoriWertung() {
		Benutzer benutzerA = new Benutzer(UUID.randomUUID(), "username", "name", List.of(), List.of(BenutzerRolle.KAMPFRICHTER));
		Benutzer benutzerB = new Benutzer(UUID.randomUUID(), "username", "name", List.of(), List.of(BenutzerRolle.KAMPFRICHTER));
		UUID turnierUUID = UUID.randomUUID();
		UUID rundeUUID = UUID.randomUUID();
		UUID id = UUID.randomUUID();
		List<Wertung> wertungList = new ArrayList<>();
		Wertung alteWertung = new Wertung(UUID.randomUUID(), null, null, null, null, null, null,
			1, 2, 3, 4, 4, 3, 2, 1,
			benutzerA
		);
		wertungList.add(alteWertung);
		Begegnung.BegegnungId begegnungId = new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1);
		Begegnung begegnung = new Begegnung(id,
			begegnungId,
			rundeUUID,
			1, 2, 3, 3,
			WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2,
			wertungList,
			WettkampfgruppeFixture.gruppe1,
			turnierUUID
		);

		List<Wertung> neueWertungList = new ArrayList<>();
		Wertung neueWertung = new Wertung(null, null, null, null, null, null, null,
			1, 2, 3, 4, 4, 3, 2, 1,
			benutzerB
		);
		neueWertungList.add(alteWertung);
		neueWertungList.add(neueWertung);
		Begegnung updatedBegegnung = new Begegnung(id,
			begegnungId,
			rundeUUID,
			1, 2, 3, 3,
			WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2,
			neueWertungList,
			WettkampfgruppeFixture.gruppe1,
			turnierUUID
		);


		when(turnierRepository.ladeBegegnung(id)).thenReturn(begegnung);
		when(benutzerRepository.findBenutzer(benutzerB.uuid())).thenReturn(Optional.of(benutzerB));

		turnierService.speichereRandoriWertung(id, 1, 2, 3, 4, 4, 3, 2, 1, benutzerB.uuid());

		ArgumentCaptor<Begegnung> argumentCaptor = ArgumentCaptor.forClass(Begegnung.class);
		verify(turnierRepository).speichereBegegnung(argumentCaptor.capture());
		Begegnung result = argumentCaptor.getValue();
		assertEquals(2, result.getWertungen().size());
		assertEquals(benutzerA, result.getWertungen().get(0).getBewerter());
		assertEquals(benutzerB, result.getWertungen().get(1).getBewerter());
	}

	@Test
	void testSpeichereNeueRandoriWertung() {
		UUID turnierUUID = UUID.randomUUID();
		UUID rundeUUID = UUID.randomUUID();
		UUID id = UUID.randomUUID();
		Begegnung.BegegnungId begegnungId = new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1);
		Begegnung begegnung = new Begegnung(id, begegnungId, rundeUUID,
			1, 2, 3, 3,
			WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2,
			new ArrayList<>(),
			WettkampfgruppeFixture.gruppe1,
			turnierUUID
		);

		when(turnierRepository.ladeBegegnung(id)).thenReturn(begegnung);
		when(benutzerRepository.findBenutzer(benutzer.uuid())).thenReturn(Optional.of(benutzer));

		turnierService.speichereRandoriWertung(id, 1, 2, 3, 4, 4, 3, 2, 1, benutzer.uuid());

		ArgumentCaptor<Begegnung> argumentCaptor = ArgumentCaptor.forClass(Begegnung.class);
		verify(turnierRepository).speichereBegegnung(argumentCaptor.capture());
		Begegnung result = argumentCaptor.getValue();
		assertEquals(1, result.getWertungen().size());
		assertEquals(benutzer, result.getWertungen().get(0).getBewerter());
		assertEquals(1, result.getWertungen().get(0).getKampfgeistWettkaempfer1());
		assertEquals(2, result.getWertungen().get(0).getTechnikWettkaempfer1());
		assertEquals(3, result.getWertungen().get(0).getKampfstilWettkaempfer1());
		assertEquals(4, result.getWertungen().get(0).getVielfaltWettkaempfer1());
		assertEquals(4, result.getWertungen().get(0).getKampfgeistWettkaempfer2());
		assertEquals(3, result.getWertungen().get(0).getTechnikWettkaempfer2());
		assertEquals(2, result.getWertungen().get(0).getKampfstilWettkaempfer2());
		assertEquals(1, result.getWertungen().get(0).getVielfaltWettkaempfer2());
	}

	@Test
	void testLadeMetadaten() {
		UUID begegnungId = MatteFixtures.b2;

		when(turnierRepository.ladeMatten(turnierUUID)).thenReturn(MatteFixtures.matteList);

		var metadaten = turnierService.ladeMetadaten(begegnungId, turnierUUID);

		assertNotNull(metadaten);
		assertEquals(3, metadaten.alleRundenBegegnungIds().size());
		assertEquals(MatteFixtures.b1, metadaten.vorherigeBegegnungId().get());
		assertEquals(MatteFixtures.b3, metadaten.nachfolgendeBegegnungId().get());
	}

	@Test
	void testLadeMetadatenLetzteBegegnung() {
		UUID begegnungId = MatteFixtures.b15;

		when(turnierRepository.ladeMatten(turnierUUID)).thenReturn(MatteFixtures.matteList);

		var metadaten = turnierService.ladeMetadaten(begegnungId, turnierUUID);

		assertNotNull(metadaten);
		assertEquals(3, metadaten.alleRundenBegegnungIds().size());
		assertEquals(MatteFixtures.b14, metadaten.vorherigeBegegnungId().get());
		assertEquals(Optional.empty(), metadaten.nachfolgendeBegegnungId());
	}

	@Test
	void testLadeMetadatenErsteBegegnung() {
		UUID begegnungId = MatteFixtures.b1;

		when(turnierRepository.ladeMatten(turnierUUID)).thenReturn(MatteFixtures.matteList);

		var metadaten = turnierService.ladeMetadaten(begegnungId, turnierUUID);

		assertNotNull(metadaten);
		assertEquals(3, metadaten.alleRundenBegegnungIds().size());
		assertEquals(Optional.empty(), metadaten.vorherigeBegegnungId());
		assertEquals(MatteFixtures.b2, metadaten.nachfolgendeBegegnungId().get());
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

		Einstellungen einstellungen = new Einstellungen(TurnierTyp.STANDARD, new MattenAnzahl(2), WettkampfReihenfolge.ABWECHSELND, new Gruppengroessen(Map.of(Altersklasse.U9, 6)), new VariablerGewichtsteil(0.2), SeparateAlterklassen.GETRENNT, new Wettkampfzeiten(Map.of()), turnierUUID);

		when(einstellungenService.ladeEinstellungen(turnierUUID)).thenReturn(einstellungen);
		when(gewichtsklassenService.ladeGewichtsklassenGruppen(turnierUUID)).thenReturn(gewichtsklassenGruppen);

		turnierService.erstelleWettkampfreihenfolgeAltersklasse(Optional.empty(), turnierUUID);


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
		assertEquals(1, matten.get(0).runden().get(0).mattenRunde());
		assertEquals(1, matten.get(0).runden().get(0).gruppenRunde());
		assertEquals(1, matten.get(0).runden().get(0).rundeGesamt());
		// 1. Runde für Gruppe B, 2. Runde auf der Matte, 2. Runde ingesamt
		assertEquals(2, matten.get(0).runden().get(1).mattenRunde());
		assertEquals(1, matten.get(0).runden().get(1).gruppenRunde());
		assertEquals(2, matten.get(0).runden().get(1).rundeGesamt());
		// 1. Runde für Gruppe C, 3. Runde auf der Matte, 3. Runde ingesamt
		assertEquals(3, matten.get(0).runden().get(2).mattenRunde());
		assertEquals(1, matten.get(0).runden().get(2).gruppenRunde());
		assertEquals(3, matten.get(0).runden().get(2).rundeGesamt());
		// 2. Runde für Gruppe A, 4. Runde auf der Matte, 4. Runde ingesamt
		assertEquals(4, matten.get(0).runden().get(3).mattenRunde());
		assertEquals(2, matten.get(0).runden().get(3).gruppenRunde());
		assertEquals(4, matten.get(0).runden().get(3).rundeGesamt());

		// Anzahl der Begegnungen auf Matte 1
		assertEquals(33, matten.get(0).runden().stream().mapToInt(r -> r.begegnungen().size()).sum());

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
		assertTrue(IntStream.range(1, matten.get(0).runden().size())
			.allMatch(i -> matten.get(0).runden().get(i - 1).mattenRunde() < matten.get(0).runden().get(i).mattenRunde()));
		assertTrue(IntStream.range(1, matten.get(1).runden().size())
			.allMatch(i -> matten.get(1).runden().get(i - 1).mattenRunde() < matten.get(1).runden().get(i).mattenRunde()));
	}

	@Test
	void testMattenRundeIstKorrekt() {
		List<GewichtsklassenGruppe> gewichtsklassenGruppen = GewichtsklassenGruppeFixture.gewichtsklassenGruppenZweiAltersklassen;
		Einstellungen einstellungen = new Einstellungen(TurnierTyp.RANDORI, new MattenAnzahl(2), WettkampfReihenfolge.ABWECHSELND, new Gruppengroessen(Map.of(Altersklasse.U9, 6, Altersklasse.U11, 6, Altersklasse.U13, 6)), new VariablerGewichtsteil(0.2), SeparateAlterklassen.GETRENNT, new Wettkampfzeiten(Map.of()), turnierUUID);

		when(einstellungenService.ladeEinstellungen(turnierUUID)).thenReturn(einstellungen);
		when(gewichtsklassenService.ladeGewichtsklassenGruppen(turnierUUID)).thenReturn(gewichtsklassenGruppen);

		turnierService.erstelleWettkampfreihenfolgeAltersklasse(Optional.empty(), turnierUUID);


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

		Einstellungen einstellungen = new Einstellungen(TurnierTyp.STANDARD, new MattenAnzahl(2), WettkampfReihenfolge.ABWECHSELND, new Gruppengroessen(Map.of(Altersklasse.U9, 6)), new VariablerGewichtsteil(0.2), SeparateAlterklassen.GETRENNT, new Wettkampfzeiten(Map.of()), turnierUUID);

		when(einstellungenService.ladeEinstellungen(turnierUUID)).thenReturn(einstellungen);
		when(gewichtsklassenService.ladeGewichtsklassenGruppen(turnierUUID)).thenReturn(gewichtsklassenGruppen);

		turnierService.erstelleWettkampfreihenfolgeAltersklasse(Optional.empty(), turnierUUID);


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
		assertEquals(1, matten.get(0).runden().get(0).mattenRunde());
		assertEquals(1, matten.get(0).runden().get(0).gruppenRunde());
		assertEquals(1, matten.get(0).runden().get(0).rundeGesamt());
		// 1. Runde für Gruppe B, 2. Runde auf der Matte, 2. Runde ingesamt
		assertEquals(2, matten.get(0).runden().get(1).mattenRunde());
		assertEquals(1, matten.get(0).runden().get(1).gruppenRunde());
		assertEquals(2, matten.get(0).runden().get(1).rundeGesamt());
		// 1. Runde für Gruppe C, 3. Runde auf der Matte, 3. Runde ingesamt
		assertEquals(3, matten.get(0).runden().get(2).mattenRunde());
		assertEquals(1, matten.get(0).runden().get(2).gruppenRunde());
		assertEquals(3, matten.get(0).runden().get(2).rundeGesamt());
		// 2. Runde für Gruppe A, 4. Runde auf der Matte, 4. Runde ingesamt
		assertEquals(4, matten.get(0).runden().get(3).mattenRunde());
		assertEquals(2, matten.get(0).runden().get(3).gruppenRunde());
		assertEquals(4, matten.get(0).runden().get(3).rundeGesamt());

		// Anzahl der Begegnungen auf Matte 1
		assertEquals(33, matten.get(0).runden().stream().mapToInt(r -> r.begegnungen().size()).sum());

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
		assertTrue(IntStream.range(1, matten.get(0).runden().size())
			.allMatch(i -> matten.get(0).runden().get(i - 1).mattenRunde() < matten.get(0).runden().get(i).mattenRunde()));
		assertTrue(IntStream.range(1, matten.get(1).runden().size())
			.allMatch(i -> matten.get(1).runden().get(i - 1).mattenRunde() < matten.get(1).runden().get(i).mattenRunde()));
	}

	@Test
	void speichereTurnierWertungErsterKampf() {
		int scoreWeiss = 1;
		int scoreBlau = 0;
		int penaltiesWeiss = 0;
		int penaltiesBlau = 0;
		String fighttime = "02:03.54"; // entspricht 123.540ms
		UUID siegerUuid = WettkaempferFixtures.wettkaempfer1.get().id();

		int aktuelleMattenRunde = 2;
		int aktuellePaarung = 1;
		int aktuelleGruppenRunde = 1;
		int aktuelleGesamtRunde = 2;
		Begegnung.BegegnungId begegnungId = new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, aktuelleMattenRunde, aktuellePaarung);
		Begegnung aktuelleBegegnung = new Begegnung(UUID.randomUUID(), begegnungId, UUID.randomUUID(),
			1, aktuelleMattenRunde, aktuelleGruppenRunde, aktuelleGesamtRunde,
			WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2,
			new ArrayList<>(),
			WettkampfgruppeFixture.gruppe1,
			turnierUUID
		);
		Begegnung.BegegnungId begegnungNachfolgendId = new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, aktuelleMattenRunde +1, aktuellePaarung +1);
		Begegnung nachfolgendeBegegnung = new Begegnung(UUID.randomUUID(), begegnungNachfolgendId, UUID.randomUUID(),
			1, aktuelleMattenRunde+1, aktuelleGruppenRunde+1, aktuelleGesamtRunde+1,
			Optional.empty(), Optional.empty(),
			new ArrayList<>(),
			WettkampfgruppeFixture.gruppe1,
			turnierUUID
		);

		// Teil 1
		when(turnierRepository.ladeBegegnung(any())).thenReturn(aktuelleBegegnung);
		when(benutzerRepository.findBenutzer(eq(benutzer.uuid()))).thenReturn(Optional.of(benutzer));
		when(wettkaempferService.ladeKaempfer(eq(siegerUuid))).thenReturn(WettkaempferFixtures.wettkaempfer1);
		when(turnierRepository.ladeWettkampfgruppeRunden(eq(WettkampfgruppeFixture.gruppe1.id()), eq(turnierUUID))).thenReturn(List.of());

		turnierService.speichereTurnierWertung(aktuelleBegegnung.getId(), scoreWeiss, scoreBlau, penaltiesWeiss, penaltiesBlau, fighttime, siegerUuid, benutzer.uuid());


		ArgumentCaptor<Begegnung> argumentCaptor = ArgumentCaptor.forClass(Begegnung.class);
		verify(turnierRepository, times(1)).speichereBegegnung(argumentCaptor.capture());

		Begegnung gespeichert = argumentCaptor.getAllValues().get(0);
		assertEquals(begegnungId, gespeichert.getBegegnungId());
		assertEquals(1, gespeichert.getWertungen().size());
		assertEquals(siegerUuid, gespeichert.getWertungen().get(0).getSieger().id());
		assertEquals(scoreWeiss, gespeichert.getWertungen().get(0).getPunkteWettkaempferWeiss());
		assertEquals(scoreBlau, gespeichert.getWertungen().get(0).getPunkteWettkaempferRot());
		assertEquals(penaltiesWeiss, gespeichert.getWertungen().get(0).getStrafenWettkaempferWeiss());
		assertEquals(penaltiesBlau, gespeichert.getWertungen().get(0).getStrafenWettkaempferRot());
		assertEquals(Duration.ofMillis(123540), gespeichert.getWertungen().get(0).getZeit());
	}

	@Test
	void speichereTurnierWertungZweiterKampf() {
		int scoreWeiss = 1;
		int scoreBlau = 0;
		int penaltiesWeiss = 0;
		int penaltiesBlau = 0;
		String fighttime = "02:03.54"; // entspricht 123.540ms
		UUID siegerUuid = WettkaempferFixtures.wettkaempferin2.id();
		var aktuelleBegegnungUUID = WettkampfgruppeFixture.b2UUID;
		var aktuelleBegegnungId = new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 2);
		var naechsteBegegnungUUID = WettkampfgruppeFixture.b7UUID;
		var naechsteBegegnungId = new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 2, 1);

		WettkampfGruppe wkg = WettkampfgruppeFixture.wettkampfGruppeFrauen;
		List<Begegnung> begegnungListRunde1 = new ArrayList<>();
		begegnungListRunde1.add(new Begegnung(WettkampfgruppeFixture.b1UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1), null, null, null, null, null,
			Optional.of(WettkaempferFixtures.wettkaempferin1),
			Optional.empty(),
			null, wkg, turnierUUID));
		begegnungListRunde1.add(new Begegnung(aktuelleBegegnungUUID,   aktuelleBegegnungId,null, null, null, null, null,
			Optional.of(WettkaempferFixtures.wettkaempferin2),
			Optional.of(WettkaempferFixtures.wettkaempferin3),
			new ArrayList<>(), wkg, turnierUUID));
		begegnungListRunde1.add(new Begegnung(WettkampfgruppeFixture.b3UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 3),null, null, null, null, null,
			Optional.of(WettkaempferFixtures.wettkaempferin4),
			Optional.of(WettkaempferFixtures.wettkaempferin5),
			null, wkg, turnierUUID));
		begegnungListRunde1.add(new Begegnung(WettkampfgruppeFixture.b4UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 4),null, null, null, null, null,
			Optional.of(WettkaempferFixtures.wettkaempferin6),
			Optional.of(WettkaempferFixtures.wettkaempferin7),
			null, wkg, turnierUUID));
		begegnungListRunde1.add(new Begegnung(WettkampfgruppeFixture.b5UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 1, 1),null, null, null, null, null,
			Optional.empty(),
			Optional.empty(),
			null, wkg, turnierUUID));
		begegnungListRunde1.add(new Begegnung(WettkampfgruppeFixture.b6UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 1, 2),null, null, null, null, null,
			Optional.empty(),
			Optional.empty(),
			null, wkg, turnierUUID));
		List<Begegnung> begegnungListRunde2 = new ArrayList<>();
		begegnungListRunde2.add(new Begegnung(WettkampfgruppeFixture.b7UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 2, 1), null, null, null, null, null,
			Optional.empty(),
			Optional.empty(),
			null, wkg, turnierUUID));
		begegnungListRunde2.add(new Begegnung(naechsteBegegnungUUID, naechsteBegegnungId,null, null, null, null, null,
			Optional.empty(),
			Optional.empty(),
			null, wkg, turnierUUID));
		begegnungListRunde2.add(new Begegnung(WettkampfgruppeFixture.b9UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 2, 1),null, null, null, null, null,
			Optional.empty(),
			Optional.empty(),
			null, wkg, turnierUUID));
		begegnungListRunde2.add(new Begegnung(WettkampfgruppeFixture.b10UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 2, 2),null, null, null, null, null,
			Optional.empty(),
			Optional.empty(),
			null, wkg, turnierUUID));
		List<Begegnung> begegnungListRunde3 = new ArrayList<>();
		begegnungListRunde3.add(new Begegnung(WettkampfgruppeFixture.b11UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 3, 1), null, null, null, null, null,
			Optional.empty(),
			Optional.empty(),
			null, wkg, turnierUUID));

		List<Runde> rundenList = new ArrayList<>();
		UUID rundeUUID1 = UUID.randomUUID();
		UUID rundeUUID2 = UUID.randomUUID();
		UUID rundeUUID3 = UUID.randomUUID();
		rundenList.add(new Runde(rundeUUID1, 1, 1, 1, 1, Altersklasse.Frauen, WettkampfgruppeFixture.wettkampfGruppeFrauen, begegnungListRunde1));
		rundenList.add(new Runde(rundeUUID2, 2, 2, 2, 1, Altersklasse.Frauen, WettkampfgruppeFixture.wettkampfGruppeFrauen, begegnungListRunde2));
		rundenList.add(new Runde(rundeUUID3, 3, 3, 3, 1, Altersklasse.Frauen, WettkampfgruppeFixture.wettkampfGruppeFrauen, begegnungListRunde3));

		// Teil 1
		when(turnierRepository.ladeBegegnung(any())).thenReturn(begegnungListRunde1.get(1));
		when(benutzerRepository.findBenutzer(eq(benutzer.uuid()))).thenReturn(Optional.of(benutzer));
		when(wettkaempferService.ladeKaempfer(eq(siegerUuid))).thenReturn(Optional.ofNullable(WettkaempferFixtures.wettkaempferin2));

		// Teil 2
		when(turnierRepository.ladeWettkampfgruppeRunden(wkg.id(), turnierUUID)).thenReturn(rundenList);


		turnierService.speichereTurnierWertung(aktuelleBegegnungUUID, scoreWeiss, scoreBlau, penaltiesWeiss, penaltiesBlau, fighttime, siegerUuid, benutzer.uuid());


		// Verify Teil 1
		ArgumentCaptor<Begegnung> argumentCaptor = ArgumentCaptor.forClass(Begegnung.class);
		verify(turnierRepository, times(3)).speichereBegegnung(argumentCaptor.capture());

		Begegnung gespeichert = argumentCaptor.getAllValues().get(0);
		assertEquals(aktuelleBegegnungId, gespeichert.getBegegnungId());
		assertEquals(aktuelleBegegnungUUID, gespeichert.getId());
		assertEquals(1, gespeichert.getWertungen().size());
		assertEquals(siegerUuid, gespeichert.getWertungen().get(0).getSieger().id());
		assertEquals(scoreWeiss, gespeichert.getWertungen().get(0).getPunkteWettkaempferWeiss());
		assertEquals(scoreBlau, gespeichert.getWertungen().get(0).getPunkteWettkaempferRot());
		assertEquals(penaltiesWeiss, gespeichert.getWertungen().get(0).getStrafenWettkaempferWeiss());
		assertEquals(penaltiesBlau, gespeichert.getWertungen().get(0).getStrafenWettkaempferRot());
		assertEquals(Duration.ofMillis(123540), gespeichert.getWertungen().get(0).getZeit());

		// Verify Teil 2
		Begegnung gespeichertNext = argumentCaptor.getAllValues().get(1);
		assertEquals(naechsteBegegnungId, gespeichertNext.getBegegnungId());
		assertEquals(naechsteBegegnungUUID, gespeichertNext.getId());
	}

	@Test
	void ladeMattenRunde() {

		when(turnierRepository.ladeAlleBegegnungen(any())).thenReturn(List.of(
			new Begegnung(UUID.randomUUID(), new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1), UUID.randomUUID(), 1, 1, 1, 1, WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2, List.of(), WettkampfgruppeFixture.gruppe1, turnierUUID),
			new Begegnung(UUID.randomUUID(), new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1), UUID.randomUUID(), 2, 1, 1, 1, WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2, List.of(), WettkampfgruppeFixture.gruppe1, turnierUUID),
			new Begegnung(UUID.randomUUID(), new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1), UUID.randomUUID(), 1, 2, 1, 2, WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2, List.of(), WettkampfgruppeFixture.gruppe1, turnierUUID),
			new Begegnung(UUID.randomUUID(), new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1), UUID.randomUUID(), 1, 2, 1, 2, WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2, List.of(), WettkampfgruppeFixture.gruppe1, turnierUUID)
		));

		List<Begegnung> begegnungList = turnierService.ladeMattenRunde(turnierUUID, 1, 2);

		assertEquals(2, begegnungList.size());
	}
}
