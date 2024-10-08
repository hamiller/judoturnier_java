package de.sinnix.judoturnier.application;

import de.sinnix.judoturnier.adapter.secondary.BenutzerRepository;
import de.sinnix.judoturnier.adapter.secondary.TurnierRepository;
import de.sinnix.judoturnier.fixtures.GewichtsklassenGruppeFixture;
import de.sinnix.judoturnier.fixtures.MatteFixtures;
import de.sinnix.judoturnier.fixtures.WettkaempferFixtures;
import de.sinnix.judoturnier.fixtures.WettkampfgruppeFixture;
import de.sinnix.judoturnier.model.Begegnung;
import de.sinnix.judoturnier.model.Benutzer;
import de.sinnix.judoturnier.model.Einstellungen;
import de.sinnix.judoturnier.model.GewichtsklassenGruppe;
import de.sinnix.judoturnier.model.Gruppengroesse;
import de.sinnix.judoturnier.model.Matte;
import de.sinnix.judoturnier.model.MattenAnzahl;
import de.sinnix.judoturnier.model.Runde;
import de.sinnix.judoturnier.model.SeparateAlterklassen;
import de.sinnix.judoturnier.model.TurnierTyp;
import de.sinnix.judoturnier.model.VariablerGewichtsteil;
import de.sinnix.judoturnier.model.Wertung;
import de.sinnix.judoturnier.model.Wettkaempfer;
import de.sinnix.judoturnier.model.WettkampfReihenfolge;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

	@Spy
	private Sortierer sortierer = new Sortierer(1);
	@Spy
	private Helpers   helpers   = new Helpers();

	@InjectMocks
	private TurnierService turnierService;

	private UUID turnierUUID;

	@BeforeEach
	void setUp() {
		turnierUUID = UUID.randomUUID();
	}

	@Test
	void testErstelleWettkampfreihenfolgeAltersklasseRandori() {
		List<GewichtsklassenGruppe> gewichtsklassenGruppen = GewichtsklassenGruppeFixture.gewichtsklassenGruppen;
		Einstellungen einstellungen = new Einstellungen(TurnierTyp.RANDORI, new MattenAnzahl(2), WettkampfReihenfolge.ABWECHSELND, new Gruppengroesse(6), new VariablerGewichtsteil(0.2), SeparateAlterklassen.ZUSAMMEN, turnierUUID);

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
	}

	@Test
	void testAktualisiereExistierendeRandoriWertung() {
		Benutzer benutzer = new Benutzer(UUID.randomUUID().toString(), "username", "name", List.of("ROLE_KAMPFRICHTER"));
		UUID turnierUUID = UUID.randomUUID();
		UUID rundeUUID = UUID.randomUUID();
		String id = "1";
		List<Wertung> wertungList = new ArrayList<>();
		Wertung alteWertung = new Wertung(UUID.randomUUID(), null, null, null, null, null, null,
			1, 2, 3, 4, 4, 3, 2, 1,
			benutzer
		);
		wertungList.add(alteWertung);
		Begegnung.BegegnungId begegnungId = new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1);
		Begegnung begegnung = new Begegnung(Integer.parseInt(id), begegnungId, rundeUUID,
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
		Begegnung updatedBegegnung = new Begegnung(Integer.parseInt(id), begegnungId, rundeUUID,
			1, 2, 3, 3,
			WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2,
			neueWertungList,
			WettkampfgruppeFixture.gruppe1,
			turnierUUID
		);


		when(turnierRepository.ladeBegegnung(Integer.parseInt(id))).thenReturn(begegnung);
		when(benutzerRepository.findById(benutzer.id())).thenReturn(benutzer);

		turnierService.speichereRandoriWertung(id, 1, 2, 3, 4, 4, 3, 2, 1, benutzer.id());

		ArgumentCaptor<Begegnung> argumentCaptor = ArgumentCaptor.forClass(Begegnung.class);
		verify(turnierRepository).speichereBegegnung(argumentCaptor.capture());
		Begegnung result = argumentCaptor.getValue();
		assertEquals(neueWertungList, result.getWertungen());
	}

	@Test
	void testWeitereRandoriWertung() {
		Benutzer benutzerA = new Benutzer(UUID.randomUUID().toString(), "username", "name", List.of("ROLE_KAMPFRICHTER"));
		Benutzer benutzerB = new Benutzer(UUID.randomUUID().toString(), "username", "name", List.of("ROLE_KAMPFRICHTER"));
		UUID turnierUUID = UUID.randomUUID();
		UUID rundeUUID = UUID.randomUUID();
		String id = "1";
		List<Wertung> wertungList = new ArrayList<>();
		Wertung alteWertung = new Wertung(UUID.randomUUID(), null, null, null, null, null, null,
			1, 2, 3, 4, 4, 3, 2, 1,
			benutzerA
		);
		wertungList.add(alteWertung);
		Begegnung.BegegnungId begegnungId = new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1);
		Begegnung begegnung = new Begegnung(Integer.parseInt(id),
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
		Begegnung updatedBegegnung = new Begegnung(Integer.parseInt(id),
			begegnungId,
			rundeUUID,
			1, 2, 3, 3,
			WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2,
			neueWertungList,
			WettkampfgruppeFixture.gruppe1,
			turnierUUID
		);


		when(turnierRepository.ladeBegegnung(Integer.parseInt(id))).thenReturn(begegnung);
		when(benutzerRepository.findById(benutzerB.id())).thenReturn(benutzerB);

		turnierService.speichereRandoriWertung(id, 1, 2, 3, 4, 4, 3, 2, 1, benutzerB.id());

		ArgumentCaptor<Begegnung> argumentCaptor = ArgumentCaptor.forClass(Begegnung.class);
		verify(turnierRepository).speichereBegegnung(argumentCaptor.capture());
		Begegnung result = argumentCaptor.getValue();
		assertEquals(2, result.getWertungen().size());
		assertEquals(benutzerA, result.getWertungen().get(0).getBewerter());
		assertEquals(benutzerB, result.getWertungen().get(1).getBewerter());
	}

	@Test
	void testSpeichereNeueRandoriWertung() {
		Benutzer benutzer = new Benutzer(UUID.randomUUID().toString(), "username", "name", List.of("ROLE_KAMPFRICHTER"));
		UUID turnierUUID = UUID.randomUUID();
		UUID rundeUUID = UUID.randomUUID();
		String id = "1";
		Begegnung.BegegnungId begegnungId = new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1);
		Begegnung begegnung = new Begegnung(Integer.parseInt(id), begegnungId, rundeUUID,
			1, 2, 3, 3,
			WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2,
			new ArrayList<>(),
			WettkampfgruppeFixture.gruppe1,
			turnierUUID
		);

		when(turnierRepository.ladeBegegnung(Integer.parseInt(id))).thenReturn(begegnung);
		when(benutzerRepository.findById(benutzer.id())).thenReturn(benutzer);

		turnierService.speichereRandoriWertung(id, 1, 2, 3, 4, 4, 3, 2, 1, benutzer.id());

		ArgumentCaptor<Begegnung> argumentCaptor = ArgumentCaptor.forClass(Begegnung.class);
		verify(turnierRepository).speichereBegegnung(argumentCaptor.capture());
		Begegnung result = argumentCaptor.getValue();
		assertEquals(1, result.getWertungen().size());
		assertEquals(benutzer, result.getWertungen().get(0).getBewerter());
		assertEquals(1, result.getWertungen().get(0).getKampfgeistWettkaempfer1());
		assertEquals(2, result.getWertungen().get(0).getTechnikWettkaempfer1());
		assertEquals(3, result.getWertungen().get(0).getKampfstilWettkaempfer1());
		assertEquals(4, result.getWertungen().get(0).getFairnessWettkaempfer1());
		assertEquals(4, result.getWertungen().get(0).getKampfgeistWettkaempfer2());
		assertEquals(3, result.getWertungen().get(0).getTechnikWettkaempfer2());
		assertEquals(2, result.getWertungen().get(0).getKampfstilWettkaempfer2());
		assertEquals(1, result.getWertungen().get(0).getFairnessWettkaempfer2());
	}

	@Test
	void testLadeMetadaten() {
		Integer begegnungId = 2;

		when(turnierRepository.ladeMatten(turnierUUID)).thenReturn(MatteFixtures.matteList);

		var metadaten = turnierService.ladeMetadaten(begegnungId, turnierUUID);

		assertNotNull(metadaten);
		assertEquals(3, metadaten.alleRundenBegegnungIds().size());
		assertEquals(1, metadaten.vorherigeBegegnungId().get());
		assertEquals(3, metadaten.nachfolgendeBegegnungId().get());
	}

	@Test
	void testLadeMetadatenLetzteBegegnung() {
		Integer begegnungId = 15;

		when(turnierRepository.ladeMatten(turnierUUID)).thenReturn(MatteFixtures.matteList);

		var metadaten = turnierService.ladeMetadaten(begegnungId, turnierUUID);

		assertNotNull(metadaten);
		assertEquals(3, metadaten.alleRundenBegegnungIds().size());
		assertEquals(14, metadaten.vorherigeBegegnungId().get());
		assertEquals(Optional.empty(), metadaten.nachfolgendeBegegnungId());
	}

	@Test
	void testLadeMetadatenErsteBegegnung() {
		Integer begegnungId = 1;

		when(turnierRepository.ladeMatten(turnierUUID)).thenReturn(MatteFixtures.matteList);

		var metadaten = turnierService.ladeMetadaten(begegnungId, turnierUUID);

		assertNotNull(metadaten);
		assertEquals(3, metadaten.alleRundenBegegnungIds().size());
		assertEquals(Optional.empty(), metadaten.vorherigeBegegnungId());
		assertEquals(2, metadaten.nachfolgendeBegegnungId().get());
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

		Einstellungen einstellungen = new Einstellungen(TurnierTyp.STANDARD, new MattenAnzahl(2), WettkampfReihenfolge.ABWECHSELND, new Gruppengroesse(0), new VariablerGewichtsteil(0.2), SeparateAlterklassen.GETRENNT, turnierUUID);

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
					logger.debug("{} - Wettkaempfer1: {}, Wettkaempfer2: {}", begegnung.getBegegnungId(),  begegnung.getWettkaempfer1().map(Wettkaempfer::name), begegnung.getWettkaempfer2().map(Wettkaempfer::name));
				}
			}
		}
	}
}