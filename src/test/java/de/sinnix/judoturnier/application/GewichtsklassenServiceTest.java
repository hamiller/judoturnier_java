package de.sinnix.judoturnier.application;

import de.sinnix.judoturnier.adapter.secondary.GewichtsklassenRepository;
import de.sinnix.judoturnier.adapter.secondary.WettkaempferRepository;
import de.sinnix.judoturnier.fixtures.WettkaempferFixtures;
import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Einstellungen;
import de.sinnix.judoturnier.model.Farbe;
import de.sinnix.judoturnier.model.Geschlecht;
import de.sinnix.judoturnier.model.GewichtsklassenGruppe;
import de.sinnix.judoturnier.model.Gruppengroesse;
import de.sinnix.judoturnier.model.MattenAnzahl;
import de.sinnix.judoturnier.model.RandoriGruppenName;
import de.sinnix.judoturnier.model.SeparateAlterklassen;
import de.sinnix.judoturnier.model.TurnierTyp;
import de.sinnix.judoturnier.model.VariablerGewichtsteil;
import de.sinnix.judoturnier.model.Verein;
import de.sinnix.judoturnier.model.Wettkaempfer;
import de.sinnix.judoturnier.model.WettkampfReihenfolge;
import de.sinnix.judoturnier.model.Wettkampfzeiten;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GewichtsklassenServiceTest {

	@Mock
	private GewichtsklassenRepository gewichtsklassenRepository;
	@Mock
	private WettkaempferService       wettkaempferService;
	@Mock
	private EinstellungenService      einstellungenService;
	@Mock
	private WettkaempferRepository    wettkaempferRepository;
	@Mock
	private TurnierService            turnierService;
	@InjectMocks
	private GewichtsklassenService    gewichtsklassenService;
	
	private UUID v1UUID = WettkaempferFixtures.v1UUID;
	private UUID v2UUID = WettkaempferFixtures.v2UUID;
	private UUID v3UUID = WettkaempferFixtures.v3UUID;
	private UUID v4UUID = WettkaempferFixtures.v4UUID;
	private UUID v5UUID = WettkaempferFixtures.v5UUID;
	private UUID wk1UUID = WettkaempferFixtures.w1;
	private UUID wk2UUID = WettkaempferFixtures.w2;
	private UUID wk3UUID = WettkaempferFixtures.w3;
	private UUID wk4UUID = WettkaempferFixtures.w4;
	private UUID wk5UUID = WettkaempferFixtures.w5;
	private UUID wk7UUID = WettkaempferFixtures.w7;
	private UUID gwkgk1UUID = UUID.randomUUID();
	private UUID gwkgk2UUID = UUID.randomUUID();
	
	@BeforeEach
	void setup() {
	}

	@Test
	void testLadeGewichtsklassenGruppen() {
		// Setup mocks
		UUID uuid = UUID.randomUUID();
		GewichtsklassenGruppe gruppe1 = new GewichtsklassenGruppe(UUID.randomUUID(), Altersklasse.U18, Optional.of(Geschlecht.m), new ArrayList<>(), Optional.of(RandoriGruppenName.Adler), 50.0, 60.0, uuid);
		GewichtsklassenGruppe gruppe2 = new GewichtsklassenGruppe(UUID.randomUUID(), Altersklasse.U18, Optional.of(Geschlecht.m), new ArrayList<>(), Optional.of(RandoriGruppenName.Bär), 60.0, 70.0, uuid);
		List<GewichtsklassenGruppe> gruppenList = Arrays.asList(gruppe1, gruppe2);

		when(gewichtsklassenRepository.findAll(uuid)).thenReturn(gruppenList);

		// Execute method under test
		List<GewichtsklassenGruppe> result = gewichtsklassenService.ladeGewichtsklassenGruppen(uuid);

		// Verify results
		assertEquals(2, result.size());
		assertTrue(result.contains(gruppe1));
		assertTrue(result.contains(gruppe2));
	}

	@Test
	void testLoescheAlles() {
		UUID uuid = UUID.randomUUID();

		// Execute method under test
		gewichtsklassenService.loescheAlles(uuid);

		// Verify that deleteAll on the repository was called
		verify(gewichtsklassenRepository, times(1)).deleteAll(uuid);
	}

	@Test
	void testSpeichere() {
		// Setup mocks
		UUID uuid = UUID.randomUUID();
		GewichtsklassenGruppe gruppe1 = new GewichtsklassenGruppe(null, Altersklasse.U18, Optional.of(Geschlecht.m), new ArrayList<>(), Optional.of(RandoriGruppenName.Adler), 50.0, 60.0, uuid);
		GewichtsklassenGruppe gruppe2 = new GewichtsklassenGruppe(null, Altersklasse.U18, Optional.of(Geschlecht.m), new ArrayList<>(), Optional.of(RandoriGruppenName.Bär), 60.0, 70.0, uuid);
		List<GewichtsklassenGruppe> gruppenList = Arrays.asList(gruppe1, gruppe2);

		// Execute method under test
		gewichtsklassenService.speichere(gruppenList);

		// Verify that saveAll on the repository was called
		verify(gewichtsklassenRepository, times(1)).saveAll(gruppenList);
	}

	@Test
	void testLoescheAltersklasse() {
		// Setup mocks
		UUID uuid = UUID.randomUUID();
		Altersklasse altersklasse = Altersklasse.U18;

		// Execute method under test
		gewichtsklassenService.loescheAltersklasse(uuid, altersklasse);

		// Verify that deleteAllByAltersklasse on the repository was called
		verify(gewichtsklassenRepository, times(1)).deleteAllByAltersklasse(uuid, altersklasse);
	}

	@Test
	void testAktualisiere() {
		// gegeben
		UUID uuid = WettkaempferFixtures.turnierUUID;
		List<Wettkaempfer> alleWettkaempferList = WettkaempferFixtures.wettkaempferList;
		List<Wettkaempfer> teilnehmerListeGruppe1 = Arrays.asList(
			new Wettkaempfer(wk1UUID, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(v1UUID, "Verein1", uuid), 25.0, Optional.of(Farbe.ORANGE), true, false, uuid),
			new Wettkaempfer(wk3UUID, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(v3UUID, "Verein3", uuid), 27.0, Optional.of(Farbe.GRUEN), true, true, uuid),
			new Wettkaempfer(wk5UUID, "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new Verein(v5UUID, "Verein5", uuid), 29.0, Optional.of(Farbe.ORANGE), true, false, uuid)
		);
		List<Wettkaempfer> teilnehmerListeGruppe2 = List.of();
		GewichtsklassenGruppe gruppe1 = new GewichtsklassenGruppe(gwkgk1UUID, Altersklasse.U11, Optional.of(Geschlecht.m), teilnehmerListeGruppe1, Optional.of(RandoriGruppenName.Adler), 25.0, 29.0, uuid);
		GewichtsklassenGruppe gruppe2 = new GewichtsklassenGruppe(gwkgk2UUID, Altersklasse.U11, Optional.of(Geschlecht.m), teilnehmerListeGruppe2, Optional.of(RandoriGruppenName.Bär), 0.0, 200.0, uuid);

		// neu: Wir verschieben 1 Kämpfer von einer Gruppe in die andere Gruppe. 2 Kämpfer verbleibt in der bisherigen Gruppe und wir fügen einen weiteren Kämpfer hinzu
		List<Wettkaempfer> teilnehmerListeGruppe1Neu = Arrays.asList(
			new Wettkaempfer(wk1UUID, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(v1UUID, "Verein1", uuid), 25.0, Optional.of(Farbe.ORANGE), true, false, uuid),
			new Wettkaempfer(wk3UUID, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(v3UUID, "Verein3", uuid), 27.0, Optional.of(Farbe.GRUEN), true, true, uuid),
			new Wettkaempfer(wk7UUID, "Teilnehmer G", Geschlecht.m, Altersklasse.U11, new Verein(v2UUID, "Verein2", uuid), 26.1, Optional.of(Farbe.GRUEN), true, true, uuid)
		);
		List<Wettkaempfer> teilnehmerListeGruppe2Neu = Arrays.asList(
			new Wettkaempfer(wk5UUID, "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new Verein(v5UUID, "Verein5", uuid), 29.0, Optional.of(Farbe.ORANGE), true, false, uuid)
		);
		GewichtsklassenGruppe gruppe1Neu = new GewichtsklassenGruppe(gwkgk1UUID, Altersklasse.U11, Optional.of(Geschlecht.m), teilnehmerListeGruppe1Neu, Optional.of(RandoriGruppenName.Adler), 25.0, 27.0, uuid);
		GewichtsklassenGruppe gruppe2Neu = new GewichtsklassenGruppe(gwkgk2UUID, Altersklasse.U11, Optional.of(Geschlecht.m), teilnehmerListeGruppe2Neu, Optional.of(RandoriGruppenName.Bär), 29.0, 29.0, uuid);


		// Mocks einrichten
		when(gewichtsklassenRepository.findAll(uuid)).thenReturn(List.of(gruppe1, gruppe2));
		when(wettkaempferService.alleKaempfer(uuid)).thenReturn(alleWettkaempferList);

		// test
		HashMap<UUID, List<UUID>> gruppenTeilnehmer = new HashMap<>();
		gruppenTeilnehmer.put(gwkgk1UUID, List.of(wk1UUID, wk3UUID, wk7UUID));
		gruppenTeilnehmer.put(gwkgk2UUID, List.of(wk5UUID));
		gewichtsklassenService.aktualisiere(gruppenTeilnehmer, uuid);

		// ArgumentCaptor verwenden
		ArgumentCaptor<List<GewichtsklassenGruppe>> argumentCaptor = ArgumentCaptor.forClass(List.class);
		verify(gewichtsklassenRepository, times(1)).saveAll(argumentCaptor.capture());

		// Verifizieren
		List<GewichtsklassenGruppe> capturedArgument = argumentCaptor.getValue();
		assertEquals(2, capturedArgument.size());
		assertEquals(gruppe1Neu, capturedArgument.get(0));
		assertEquals(gruppe2Neu, capturedArgument.get(1));
	}

	@Test
	void testTeileInGewichtsklassenRandoriAbwechselnd() {
		UUID uuid = UUID.randomUUID();
		List<Wettkaempfer> wettkaempferListe = new ArrayList<>();
		wettkaempferListe.add(new Wettkaempfer(wk1UUID, "Wettkaempfer 1", Geschlecht.m, Altersklasse.U11, new Verein(v1UUID, "Verein 1", uuid), 25.0, Optional.empty(), false, false, uuid));
		wettkaempferListe.add(new Wettkaempfer(wk2UUID, "Wettkaempfer 2", Geschlecht.w, Altersklasse.U11, new Verein(v2UUID, "Verein 2", uuid), 27.0, Optional.empty(), false, false, uuid));

		Einstellungen einstellungen = new Einstellungen(TurnierTyp.RANDORI, new MattenAnzahl(2), WettkampfReihenfolge.ABWECHSELND, new Gruppengroesse(2), new VariablerGewichtsteil(5.0), SeparateAlterklassen.ZUSAMMEN, new Wettkampfzeiten(Map.of()), uuid);
		when(einstellungenService.ladeEinstellungen(any())).thenReturn(einstellungen);

		List<GewichtsklassenGruppe> result = gewichtsklassenService.teileInGewichtsklassen(wettkaempferListe, uuid);

		assertNotNull(result);
		assertEquals(2, result.size());
	}

	@Test
	void testTeileInGewichtsklassenRandoriAlle() {
		UUID uuid = UUID.randomUUID();
		List<Wettkaempfer> wettkaempferListe = new ArrayList<>();
		wettkaempferListe.add(new Wettkaempfer(wk1UUID, "Wettkaempfer 1", Geschlecht.m, Altersklasse.U11, new Verein(v1UUID, "Verein 1", uuid), 25.0, Optional.empty(), false, false, uuid));
		wettkaempferListe.add(new Wettkaempfer(wk2UUID, "Wettkaempfer 2", Geschlecht.w, Altersklasse.U11, new Verein(v2UUID, "Verein 2", uuid), 27.0, Optional.empty(), false, false, uuid));

		Einstellungen einstellungen = new Einstellungen(TurnierTyp.RANDORI, new MattenAnzahl(2), WettkampfReihenfolge.ALLE, new Gruppengroesse(2), new VariablerGewichtsteil(5.0), SeparateAlterklassen.ZUSAMMEN, new Wettkampfzeiten(Map.of()), uuid);
		when(einstellungenService.ladeEinstellungen(any())).thenReturn(einstellungen);

		List<GewichtsklassenGruppe> result = gewichtsklassenService.teileInGewichtsklassen(wettkaempferListe, uuid);

		assertNotNull(result);
		assertEquals(2, result.size());
	}

	@Test
	void testTeileInGewichtsklassenNormalesTurnierFuerUeberU13() {
		// Fixe Gewichtsklassen bei U13 und aufwärts
		UUID uuid = UUID.randomUUID();
		List<Wettkaempfer> wettkaempferListe = new ArrayList<>();
		wettkaempferListe.add(new Wettkaempfer(wk1UUID, "Wettkaempfer 1", Geschlecht.m, Altersklasse.U13, new Verein(v1UUID, "Verein 1", uuid), 25.0, Optional.empty(), false, false, uuid));
		wettkaempferListe.add(new Wettkaempfer(wk2UUID, "Wettkaempfer 2", Geschlecht.m, Altersklasse.U13, new Verein(v2UUID, "Verein 2", uuid), 27.0, Optional.empty(), false, false, uuid));
		wettkaempferListe.add(new Wettkaempfer(wk3UUID, "Wettkaempfer 3", Geschlecht.m, Altersklasse.U13, new Verein(v3UUID, "Verein 3", uuid), 28.0, Optional.empty(), false, false, uuid));
		wettkaempferListe.add(new Wettkaempfer(wk4UUID, "Wettkaempfer 4", Geschlecht.m, Altersklasse.U13, new Verein(v4UUID, "Verein 4", uuid), 29.0, Optional.empty(), false, false, uuid));
		wettkaempferListe.add(new Wettkaempfer(wk5UUID, "Wettkaempfer 5", Geschlecht.m, Altersklasse.U13, new Verein(v5UUID, "Verein 5", uuid), 31.0, Optional.empty(), false, false, uuid));

		Einstellungen einstellungen = new Einstellungen(TurnierTyp.STANDARD, new MattenAnzahl(2), WettkampfReihenfolge.ABWECHSELND, new Gruppengroesse(6), new VariablerGewichtsteil(0.2), SeparateAlterklassen.ZUSAMMEN, new Wettkampfzeiten(Map.of()), uuid);
		when(einstellungenService.ladeEinstellungen(any())).thenReturn(einstellungen);

		List<GewichtsklassenGruppe> result = gewichtsklassenService.teileInGewichtsklassen(wettkaempferListe, uuid);

		assertNotNull(result);

		assertEquals(3, result.size()); // 2 Gruppen plus eine leere Dummy-Gruppe
		assertNotNull(result.get(0).gruppenGeschlecht());
		assertEquals(Optional.of(Geschlecht.m), result.get(0).gruppenGeschlecht());
		assertEquals(3, result.get(0).teilnehmer().size());
		assertEquals(Optional.empty(), result.get(0).name());
		assertEquals(Altersklasse.U13, result.get(0).altersKlasse());
		assertEquals(25.0, result.get(0).minGewicht());
		assertEquals(28.0, result.get(0).maxGewicht());

		assertNotNull(result.get(1).gruppenGeschlecht());
		assertEquals(Optional.of(Geschlecht.m), result.get(1).gruppenGeschlecht());
		assertEquals(2, result.get(1).teilnehmer().size());
		assertEquals(Optional.empty(), result.get(1).name());
		assertEquals(Altersklasse.U13, result.get(1).altersKlasse());
		assertEquals(29.0, result.get(1).minGewicht());
		assertEquals(31.0, result.get(1).maxGewicht());

		assertEquals(200.0, result.get(2).maxGewicht()); // dummy
	}

	@Test
	void testTeileInGewichtsklassenNormalesTurnierFuerUnterU13() {
		// Es gibt keine fixen Gewichtsklassen bei unter U13
		UUID uuid = UUID.randomUUID();
		List<Wettkaempfer> wettkaempferListe = new ArrayList<>();
		wettkaempferListe.add(new Wettkaempfer(wk1UUID, "Wettkaempfer 1", Geschlecht.m, Altersklasse.U9, new Verein(v1UUID, "Verein 1", uuid), 25.0, Optional.empty(), false, false, uuid));
		wettkaempferListe.add(new Wettkaempfer(wk2UUID, "Wettkaempfer 2", Geschlecht.m, Altersklasse.U9, new Verein(v2UUID, "Verein 2", uuid), 27.0, Optional.empty(), false, false, uuid));
		wettkaempferListe.add(new Wettkaempfer(wk3UUID, "Wettkaempfer 3", Geschlecht.m, Altersklasse.U9, new Verein(v3UUID, "Verein 3", uuid), 28.0, Optional.empty(), false, false, uuid));
		wettkaempferListe.add(new Wettkaempfer(wk4UUID, "Wettkaempfer 4", Geschlecht.m, Altersklasse.U9, new Verein(v4UUID, "Verein 4", uuid), 29.0, Optional.empty(), false, false, uuid));
		wettkaempferListe.add(new Wettkaempfer(wk5UUID, "Wettkaempfer 5", Geschlecht.m, Altersklasse.U9, new Verein(v5UUID, "Verein 5", uuid), 31.0, Optional.empty(), false, false, uuid));

		Einstellungen einstellungen = new Einstellungen(TurnierTyp.STANDARD, new MattenAnzahl(2), WettkampfReihenfolge.ABWECHSELND, new Gruppengroesse(3), new VariablerGewichtsteil(0.2), SeparateAlterklassen.ZUSAMMEN, new Wettkampfzeiten(Map.of()), uuid);
		when(einstellungenService.ladeEinstellungen(any())).thenReturn(einstellungen);

		List<GewichtsklassenGruppe> result = gewichtsklassenService.teileInGewichtsklassen(wettkaempferListe, uuid);

		assertNotNull(result);

		assertEquals(3, result.size()); // 2 Gruppen (wegen Gruppengröße=3 und keine fixen Gewichtsklassen bei U9) plus eine leere Dummy-Gruppe
		assertNotNull(result.get(0).gruppenGeschlecht());
		assertEquals(Optional.of(Geschlecht.m), result.get(0).gruppenGeschlecht());
		assertEquals(3, result.get(0).teilnehmer().size());
		assertEquals(Optional.empty(), result.get(0).name());
		assertEquals(Altersklasse.U9, result.get(0).altersKlasse());
		assertEquals(25.0, result.get(0).minGewicht());
		assertEquals(28.0, result.get(0).maxGewicht());

		assertNotNull(result.get(1).gruppenGeschlecht());
		assertEquals(Optional.of(Geschlecht.m), result.get(1).gruppenGeschlecht());
		assertEquals(2, result.get(1).teilnehmer().size());
		assertEquals(Optional.empty(), result.get(1).name());
		assertEquals(Altersklasse.U9, result.get(1).altersKlasse());
		assertEquals(29.0, result.get(1).minGewicht());
		assertEquals(31.0, result.get(1).maxGewicht());
	}
}
