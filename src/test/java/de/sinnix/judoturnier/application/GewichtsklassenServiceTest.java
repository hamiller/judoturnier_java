package de.sinnix.judoturnier.application;

import de.sinnix.judoturnier.adapter.secondary.GewichtsklassenRepository;
import de.sinnix.judoturnier.adapter.secondary.WettkaempferRepository;
import de.sinnix.judoturnier.fixtures.WettkaempferFixtures;
import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Einstellungen;
import de.sinnix.judoturnier.model.Farbe;
import de.sinnix.judoturnier.model.Geschlecht;
import de.sinnix.judoturnier.model.GewichtsklassenGruppe;
import de.sinnix.judoturnier.model.MattenAnzahl;
import de.sinnix.judoturnier.model.RandoriGruppenName;
import de.sinnix.judoturnier.model.RandoriGruppengroesse;
import de.sinnix.judoturnier.model.TurnierTyp;
import de.sinnix.judoturnier.model.VariablerGewichtsteil;
import de.sinnix.judoturnier.model.Verein;
import de.sinnix.judoturnier.model.Wettkaempfer;
import de.sinnix.judoturnier.model.WettkampfReihenfolge;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
	@InjectMocks
	private GewichtsklassenService    gewichtsklassenService;

	@Test
	void testLadeGewichtsklassenGruppen() {
		// Setup mocks
		GewichtsklassenGruppe gruppe1 = new GewichtsklassenGruppe(1, Altersklasse.U18, Optional.of(Geschlecht.m), new ArrayList<>(), Optional.of(RandoriGruppenName.Adler), 50.0, 60.0);
		GewichtsklassenGruppe gruppe2 = new GewichtsklassenGruppe(2, Altersklasse.U18, Optional.of(Geschlecht.m), new ArrayList<>(), Optional.of(RandoriGruppenName.Bär), 60.0, 70.0);
		List<GewichtsklassenGruppe> gruppenList = Arrays.asList(gruppe1, gruppe2);

		when(gewichtsklassenRepository.findAll()).thenReturn(gruppenList);

		// Execute method under test
		List<GewichtsklassenGruppe> result = gewichtsklassenService.ladeGewichtsklassenGruppen();

		// Verify results
		assertEquals(2, result.size());
		assertTrue(result.contains(gruppe1));
		assertTrue(result.contains(gruppe2));
	}

	@Test
	void testLoescheAlles() {
		// Execute method under test
		gewichtsklassenService.loescheAlles();

		// Verify that deleteAll on the repository was called
		verify(gewichtsklassenRepository, times(1)).deleteAll();
	}

	@Test
	void testSpeichere() {
		// Setup mocks
		GewichtsklassenGruppe gruppe1 = new GewichtsklassenGruppe(1, Altersklasse.U18, Optional.of(Geschlecht.m), new ArrayList<>(), Optional.of(RandoriGruppenName.Adler), 50.0, 60.0);
		GewichtsklassenGruppe gruppe2 = new GewichtsklassenGruppe(2, Altersklasse.U18, Optional.of(Geschlecht.m), new ArrayList<>(), Optional.of(RandoriGruppenName.Bär), 60.0, 70.0);
		List<GewichtsklassenGruppe> gruppenList = Arrays.asList(gruppe1, gruppe2);

		// Execute method under test
		gewichtsklassenService.speichere(gruppenList);

		// Verify that saveAll on the repository was called
		verify(gewichtsklassenRepository, times(1)).saveAll(gruppenList);
	}

	@Test
	void testLoescheAltersklasse() {
		// Setup mocks
		Altersklasse altersklasse = Altersklasse.U18;

		// Execute method under test
		gewichtsklassenService.loescheAltersklasse(altersklasse);

		// Verify that deleteAllByAltersklasse on the repository was called
		verify(gewichtsklassenRepository, times(1)).deleteAllByAltersklasse(altersklasse);
	}

	@Test
	void testAktualisiere() {
		// gegeben
		List<Wettkaempfer> alleWettkaempferList = WettkaempferFixtures.wettkaempferList;
		List<Wettkaempfer> teilnehmerListeGruppe1 = Arrays.asList(
			new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 25.0, Optional.of(Farbe.ORANGE), true, false),
			new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3"), 27.0, Optional.of(Farbe.GRUEN), true, true),
			new Wettkaempfer(5, "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5"), 29.0, Optional.of(Farbe.ORANGE), true, false)
		);
		List<Wettkaempfer> teilnehmerListeGruppe2 = List.of();
		GewichtsklassenGruppe gruppe1 = new GewichtsklassenGruppe(1, Altersklasse.U11, Optional.of(Geschlecht.m), teilnehmerListeGruppe1, Optional.of(RandoriGruppenName.Adler), 25.0, 29.0);
		GewichtsklassenGruppe gruppe2 = new GewichtsklassenGruppe(2, Altersklasse.U11, Optional.of(Geschlecht.m), teilnehmerListeGruppe2, Optional.of(RandoriGruppenName.Bär), 0.0, 200.0);

		// neu: Wir verschieben 1 Kämpfer von einer Gruppe in die andere Gruppe. 2 Kämpfer verbleibt in der bisherigen Gruppe und wir fügen einen weiteren Kämpfer hinzu
		List<Wettkaempfer> teilnehmerListeGruppe1Neu = Arrays.asList(
			new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 25.0, Optional.of(Farbe.ORANGE), true, false),
			new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3"), 27.0, Optional.of(Farbe.GRUEN), true, true),
			new Wettkaempfer(7, "Teilnehmer G", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 26.1, Optional.of(Farbe.GRUEN), true, true)
		);
		List<Wettkaempfer> teilnehmerListeGruppe2Neu = Arrays.asList(
			new Wettkaempfer(5, "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5"), 29.0, Optional.of(Farbe.ORANGE), true, false)
		);
		GewichtsklassenGruppe gruppe1Neu = new GewichtsklassenGruppe(1, Altersklasse.U11, Optional.of(Geschlecht.m), teilnehmerListeGruppe1Neu, Optional.of(RandoriGruppenName.Adler), 25.0, 27.0);
		GewichtsklassenGruppe gruppe2Neu = new GewichtsklassenGruppe(2, Altersklasse.U11, Optional.of(Geschlecht.m), teilnehmerListeGruppe2Neu, Optional.of(RandoriGruppenName.Bär), 29.0, 29.0);


		// Mocks einrichten
		when(gewichtsklassenRepository.findAll()).thenReturn(List.of(gruppe1, gruppe2));
		when(wettkaempferService.alleKaempfer()).thenReturn(alleWettkaempferList);

		// test
		HashMap<Integer, List<Integer>> gruppenTeilnehmer = new HashMap<>();
		gruppenTeilnehmer.put(1, List.of(1, 3, 7));
		gruppenTeilnehmer.put(2, List.of(5));
		gewichtsklassenService.aktualisiere(gruppenTeilnehmer);

		// ArgumentCaptor verwenden
		ArgumentCaptor<List<GewichtsklassenGruppe>> argumentCaptor = ArgumentCaptor.forClass(List.class);
		verify(gewichtsklassenRepository, times(1)).saveAll(argumentCaptor.capture());

		// Verifizieren
		List<GewichtsklassenGruppe> capturedArgument = argumentCaptor.getValue();
		assertEquals(2, capturedArgument.size());
		assertEquals(capturedArgument.get(0), gruppe1Neu);
		assertEquals(capturedArgument.get(1), gruppe2Neu);
	}

	@Test
	void testTeileInGewichtsklassenRandoriAbwechselnd() {
		List<Wettkaempfer> wettkaempferListe = new ArrayList<>();
		wettkaempferListe.add(new Wettkaempfer(1, "Wettkaempfer 1", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein 1"), 25.0, Optional.empty(), false, false));
		wettkaempferListe.add(new Wettkaempfer(2, "Wettkaempfer 2", Geschlecht.w, Altersklasse.U11, new Verein(2, "Verein 2"), 27.0, Optional.empty(), false, false));

		Einstellungen einstellungen = new Einstellungen(TurnierTyp.RANDORI, new MattenAnzahl(2), WettkampfReihenfolge.ABWECHSELND, new RandoriGruppengroesse(2), new VariablerGewichtsteil(5.0));
		when(einstellungenService.ladeEinstellungen()).thenReturn(einstellungen);

		List<GewichtsklassenGruppe> result = gewichtsklassenService.teileInGewichtsklassen(wettkaempferListe);

		assertNotNull(result);
		assertEquals(2, result.size());
	}

	@Test
	void testTeileInGewichtsklassenRandoriAlle() {
		List<Wettkaempfer> wettkaempferListe = new ArrayList<>();
		wettkaempferListe.add(new Wettkaempfer(1, "Wettkaempfer 1", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein 1"), 25.0, Optional.empty(), false, false));
		wettkaempferListe.add(new Wettkaempfer(2, "Wettkaempfer 2", Geschlecht.w, Altersklasse.U11, new Verein(2, "Verein 2"), 27.0, Optional.empty(), false, false));

		Einstellungen einstellungen = new Einstellungen(TurnierTyp.RANDORI, new MattenAnzahl(2), WettkampfReihenfolge.ALLE, new RandoriGruppengroesse(2), new VariablerGewichtsteil(5.0));
		when(einstellungenService.ladeEinstellungen()).thenReturn(einstellungen);

		List<GewichtsklassenGruppe> result = gewichtsklassenService.teileInGewichtsklassen(wettkaempferListe);

		assertNotNull(result);
		assertEquals(2, result.size());
	}

	@Test
	void testTeileInGewichtsklassenNormalesTurnier() {
		List<Wettkaempfer> wettkaempferListe = new ArrayList<>();
		wettkaempferListe.add(new Wettkaempfer(1, "Wettkaempfer 1", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein 1"), 25.0, Optional.empty(), false, false));
		wettkaempferListe.add(new Wettkaempfer(2, "Wettkaempfer 2", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein 2"), 27.0, Optional.empty(), false, false));

		Einstellungen einstellungen = new Einstellungen(TurnierTyp.STANDARD, new MattenAnzahl(2), WettkampfReihenfolge.ABWECHSELND, new RandoriGruppengroesse(2), new VariablerGewichtsteil(5.0));
		when(einstellungenService.ladeEinstellungen()).thenReturn(einstellungen);

		List<GewichtsklassenGruppe> result = gewichtsklassenService.teileInGewichtsklassen(wettkaempferListe);

		assertNotNull(result);
		assertEquals(1, result.size());
	}
}
