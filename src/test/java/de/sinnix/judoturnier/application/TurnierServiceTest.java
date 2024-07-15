package de.sinnix.judoturnier.application;

import de.sinnix.judoturnier.adapter.secondary.BewerterRepository;
import de.sinnix.judoturnier.adapter.secondary.TurnierRepository;
import de.sinnix.judoturnier.fixtures.GewichtsklassenGruppeFixture;
import de.sinnix.judoturnier.fixtures.WettkaempferFixtures;
import de.sinnix.judoturnier.fixtures.WettkampfgruppeFixture;
import de.sinnix.judoturnier.model.Begegnung;
import de.sinnix.judoturnier.model.Bewerter;
import de.sinnix.judoturnier.model.Einstellungen;
import de.sinnix.judoturnier.model.GewichtsklassenGruppe;
import de.sinnix.judoturnier.model.Matte;
import de.sinnix.judoturnier.model.MattenAnzahl;
import de.sinnix.judoturnier.model.RandoriGruppengroesse;
import de.sinnix.judoturnier.model.SeparateAlterklassen;
import de.sinnix.judoturnier.model.TurnierTyp;
import de.sinnix.judoturnier.model.VariablerGewichtsteil;
import de.sinnix.judoturnier.model.Wertung;
import de.sinnix.judoturnier.model.WettkampfReihenfolge;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TurnierServiceTest {
	@Mock
	private TurnierRepository      turnierRepository;
	@Mock
	private EinstellungenService   einstellungenService;
	@Mock
	private GewichtsklassenService gewichtsklassenService;
	@Mock
	private BewerterRepository     bewerterRepository;

	@Spy
	private Sortierer sortierer = new Sortierer();

	@InjectMocks
	private TurnierService turnierService;

	private UUID turnierUUID;

	@BeforeEach
	void setUp() {
		turnierUUID = UUID.randomUUID();
	}

	@Test
	void testErstelleWettkampfreihenfolgeAltersklasse() {
		List<GewichtsklassenGruppe> gewichtsklassenGruppen = GewichtsklassenGruppeFixture.gewichtsklassenGruppen;
		Einstellungen einstellungen = new Einstellungen(TurnierTyp.RANDORI, new MattenAnzahl(2), WettkampfReihenfolge.ABWECHSELND, new RandoriGruppengroesse(6), new VariablerGewichtsteil(0.2), SeparateAlterklassen.ZUSAMMEN, turnierUUID);

		when(einstellungenService.ladeEinstellungen(turnierUUID)).thenReturn(einstellungen);
		when(gewichtsklassenService.ladeGewichtsklassenGruppen(turnierUUID)).thenReturn(gewichtsklassenGruppen);

		turnierService.erstelleWettkampfreihenfolgeAltersklasse(Optional.empty(), turnierUUID);

		assertEquals(5, gewichtsklassenGruppen.size());
		assertEquals(25, gewichtsklassenGruppen.stream().mapToInt(g -> g.teilnehmer().size()).sum()); // Anzahl Teilnehmer insgesamt
		// ArgumentCaptor verwenden
		ArgumentCaptor<List<Matte>> argumentCaptor = ArgumentCaptor.forClass(List.class);
		verify(turnierRepository, times(1)).speichereMatten(argumentCaptor.capture());
		// Verifizieren
		List<Matte> matten = argumentCaptor.getValue();
		assertEquals(2, matten.size());
		int anzahlBegegnungen = matten.stream().mapToInt(m -> m.runden().stream().mapToInt(r -> r.begegnungen().size()).sum()).sum();
		int berechneteBegegnungen = 0;
		// da Jeder-gegen-Jeden nur je Gruppe gilt, muss die Anzahl für jede Gruppe separat geprüft werden
		for (GewichtsklassenGruppe gruppe : gewichtsklassenGruppen) {
			var n = gruppe.teilnehmer().size();
			var N = (n * (n - 1)) / 2; // Berechnete Begegnungen in dieser Gruppe
			berechneteBegegnungen += N;
		}
		assertEquals(berechneteBegegnungen, anzahlBegegnungen);
	}

	@Test
	void testAktualisiereExistierendeRandoriWertung() {
		Bewerter bewerter = new Bewerter(UUID.randomUUID().toString(), "username", "name", List.of("ROLE_KAMPFRICHTER"));
		UUID turnierUUID = UUID.randomUUID();
		String begegnungId = "1";
		List<Wertung> wertungList = new ArrayList<>();
		Wertung alteWertung = new Wertung(UUID.randomUUID(), null, null, null, null, null, null,
			1, 2, 3, 4, 4, 3, 2, 1,
			bewerter
		);
		wertungList.add(alteWertung);
		Begegnung begegnung = new Begegnung(Integer.parseInt(begegnungId),
			1, 2, 3,
			WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2,
			wertungList,
			WettkampfgruppeFixture.gruppe1,
			turnierUUID
		);

		List<Wertung> neueWertungList = new ArrayList<>();
		Wertung neueWertung = new Wertung(alteWertung.getUuid(), null, null, null, null, null, null,
			1, 2, 3, 4, 4, 3, 2, 1,
			bewerter
		);
		neueWertungList.add(neueWertung);
		Begegnung updatedBegegnung = new Begegnung(Integer.parseInt(begegnungId),
			1, 2, 3,
			WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2,
			neueWertungList,
			WettkampfgruppeFixture.gruppe1,
			turnierUUID
		);


		when(turnierRepository.ladeBegegnung(Integer.parseInt(begegnungId))).thenReturn(begegnung);
		when(bewerterRepository.findById(bewerter.id())).thenReturn(bewerter);

		turnierService.speichereRandoriWertung(begegnungId, 1, 2, 3, 4, 4, 3, 2, 1, bewerter.id());

		ArgumentCaptor<Begegnung> argumentCaptor = ArgumentCaptor.forClass(Begegnung.class);
		verify(turnierRepository).speichereBegegnung(argumentCaptor.capture());
		Begegnung result = argumentCaptor.getValue();
		assertEquals(neueWertungList, result.getWertungen());
	}

	@Test
	void testWeitereRandoriWertung() {
		Bewerter bewerterA = new Bewerter(UUID.randomUUID().toString(), "username", "name", List.of("ROLE_KAMPFRICHTER"));
		Bewerter bewerterB = new Bewerter(UUID.randomUUID().toString(), "username", "name", List.of("ROLE_KAMPFRICHTER"));
		UUID turnierUUID = UUID.randomUUID();
		String begegnungId = "1";
		List<Wertung> wertungList = new ArrayList<>();
		Wertung alteWertung = new Wertung(UUID.randomUUID(), null, null, null, null, null, null,
			1, 2, 3, 4, 4, 3, 2, 1,
			bewerterA
		);
		wertungList.add(alteWertung);
		Begegnung begegnung = new Begegnung(Integer.parseInt(begegnungId),
			1, 2, 3,
			WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2,
			wertungList,
			WettkampfgruppeFixture.gruppe1,
			turnierUUID
		);

		List<Wertung> neueWertungList = new ArrayList<>();
		Wertung neueWertung = new Wertung(null, null, null, null, null, null, null,
			1, 2, 3, 4, 4, 3, 2, 1,
			bewerterB
		);
		neueWertungList.add(alteWertung);
		neueWertungList.add(neueWertung);
		Begegnung updatedBegegnung = new Begegnung(Integer.parseInt(begegnungId),
			1, 2, 3,
			WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2,
			neueWertungList,
			WettkampfgruppeFixture.gruppe1,
			turnierUUID
		);


		when(turnierRepository.ladeBegegnung(Integer.parseInt(begegnungId))).thenReturn(begegnung);
		when(bewerterRepository.findById(bewerterB.id())).thenReturn(bewerterB);

		turnierService.speichereRandoriWertung(begegnungId, 1, 2, 3, 4, 4, 3, 2, 1, bewerterB.id());

		ArgumentCaptor<Begegnung> argumentCaptor = ArgumentCaptor.forClass(Begegnung.class);
		verify(turnierRepository).speichereBegegnung(argumentCaptor.capture());
		Begegnung result = argumentCaptor.getValue();
		assertEquals(2, result.getWertungen().size());
		assertEquals(bewerterA, result.getWertungen().get(0).getBewerter());
		assertEquals(bewerterB, result.getWertungen().get(1).getBewerter());
	}

	@Test
	void testSpeichereNeueRandoriWertung() {
		Bewerter bewerter = new Bewerter(UUID.randomUUID().toString(), "username", "name", List.of("ROLE_KAMPFRICHTER"));
		UUID turnierUUID = UUID.randomUUID();
		String begegnungId = "1";

		Begegnung begegnung = new Begegnung(Integer.parseInt(begegnungId),
			1, 2, 3,
			WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2,
			new ArrayList<>(),
			WettkampfgruppeFixture.gruppe1,
			turnierUUID
		);

		when(turnierRepository.ladeBegegnung(Integer.parseInt(begegnungId))).thenReturn(begegnung);
		when(bewerterRepository.findById(bewerter.id())).thenReturn(bewerter);

		turnierService.speichereRandoriWertung(begegnungId, 1, 2, 3, 4, 4, 3, 2, 1, bewerter.id());

		ArgumentCaptor<Begegnung> argumentCaptor = ArgumentCaptor.forClass(Begegnung.class);
		verify(turnierRepository).speichereBegegnung(argumentCaptor.capture());
		Begegnung result = argumentCaptor.getValue();
		assertEquals(1, result.getWertungen().size());
		assertEquals(bewerter, result.getWertungen().get(0).getBewerter());
		assertEquals(1, result.getWertungen().get(0).getKampfgeistWettkaempfer1());
		assertEquals(2, result.getWertungen().get(0).getTechnikWettkaempfer1());
		assertEquals(3, result.getWertungen().get(0).getKampfstilWettkaempfer1());
		assertEquals(4, result.getWertungen().get(0).getFairnessWettkaempfer1());
		assertEquals(4, result.getWertungen().get(0).getKampfgeistWettkaempfer2());
		assertEquals(3, result.getWertungen().get(0).getTechnikWettkaempfer2());
		assertEquals(2, result.getWertungen().get(0).getKampfstilWettkaempfer2());
		assertEquals(1, result.getWertungen().get(0).getFairnessWettkaempfer2());
	}
}