package de.sinnix.judoturnier.application;

import de.sinnix.judoturnier.fixtures.WettkaempferFixtures;
import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Farbe;
import de.sinnix.judoturnier.model.Geschlecht;
import de.sinnix.judoturnier.model.GewichtsklassenGruppe;
import de.sinnix.judoturnier.model.Verein;
import de.sinnix.judoturnier.model.Wettkaempfer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GewichtsklassenServiceTest {

    @Mock
    private IGewichtsklassenRepository gewichtsklassenRepository;
    @Mock
    private WettkaempferService wettkaempferService;
    @InjectMocks
    private GewichtsklassenService gewichtsklassenService;

    @BeforeEach
    public void setUp() {
        // Initialize the mocks if necessary
    }

    @Test
    void testLade() {
        // Setup mocks
        GewichtsklassenGruppe gruppe1 = new GewichtsklassenGruppe(1, Altersklasse.U18, Geschlecht.m, new ArrayList<>(), "Gruppe1", 50.0, 60.0);
        GewichtsklassenGruppe gruppe2 = new GewichtsklassenGruppe(2, Altersklasse.U18, Geschlecht.m, new ArrayList<>(), "Gruppe2", 60.0, 70.0);
        List<GewichtsklassenGruppe> gruppenList = Arrays.asList(gruppe1, gruppe2);

        when(gewichtsklassenRepository.findAll()).thenReturn(gruppenList);

        // Execute method under test
        List<GewichtsklassenGruppe> result = gewichtsklassenService.lade();

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
        GewichtsklassenGruppe gruppe1 = new GewichtsklassenGruppe(1, Altersklasse.U18, Geschlecht.m, new ArrayList<>(), "Gruppe1", 50.0, 60.0);
        GewichtsklassenGruppe gruppe2 = new GewichtsklassenGruppe(2, Altersklasse.U18, Geschlecht.m, new ArrayList<>(), "Gruppe2", 60.0, 70.0);
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
        List<Wettkaempfer> teilnehmerListe = Arrays.asList(
                new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 25.0, Farbe.ORANGE, true, false),
                new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3"), 27.0, Farbe.GRUEN, true, true),
                new Wettkaempfer(5, "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5"), 29.0, Farbe.ORANGE, true, false)
        );
        GewichtsklassenGruppe gruppe = new GewichtsklassenGruppe(1, Altersklasse.U11, Geschlecht.m, teilnehmerListe, "Gruppe1", 25.0, 29.0);
        GewichtsklassenGruppe gruppeFix = new GewichtsklassenGruppe(2, Altersklasse.U11, Geschlecht.m, teilnehmerListe, "Gruppe2", 25.0, 29.0);

        // neu
        List<Wettkaempfer> teilnehmerListeNeu = Arrays.asList(
                new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 25.0, Farbe.ORANGE, true, false),
                new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3"), 27.0, Farbe.GRUEN, true, true),
                new Wettkaempfer(7, "Teilnehmer G", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 29.0, Farbe.GRUEN, true, true)
        );
        GewichtsklassenGruppe gruppeNeu = new GewichtsklassenGruppe(1, Altersklasse.U11, Geschlecht.m, teilnehmerListeNeu, "Gruppe1", 25.0, 29.0);


        // test
        HashMap<Integer, List<Integer>> gruppenTeilnehmer = new HashMap<>();
        gruppenTeilnehmer.put(1, List.of(1, 3, 7));

        gewichtsklassenService.aktualisiere(gruppenTeilnehmer);

        when(gewichtsklassenRepository.findAll()).thenReturn(List.of(gruppe, gruppeFix));
        when(wettkaempferService.alleKaempfer()).thenReturn(alleWettkaempferList);
        verify(gewichtsklassenRepository, times(1)).saveAll(List.of(gruppeNeu, gruppeFix));
    }
}
