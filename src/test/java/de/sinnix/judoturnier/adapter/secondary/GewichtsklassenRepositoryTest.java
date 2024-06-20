package de.sinnix.judoturnier.adapter.secondary;

import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Geschlecht;
import de.sinnix.judoturnier.model.GewichtsklassenGruppe;
import de.sinnix.judoturnier.model.Verein;
import de.sinnix.judoturnier.model.Wettkaempfer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GewichtsklassenRepositoryTest {

    @Mock
    private GewichtsklassenJpaRepository gewichtsklassenJpaRepository;

    @Mock
    private GewichtsklassenConverter gewichtsklassenConverter;

    @InjectMocks
    private GewichtsklassenRepository gewichtsklassenRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAll() {
        // Setup mocks
        var teilnehmerJpa = List.of(
                new WettkaempferJpa(1, "Melanie", "w", "Frauen", new VereinJpa(1, "Verein1"), 55d, null, false, false),
                new WettkaempferJpa(2, "Mira", "w", "Frauen", new VereinJpa(2, "Verein2"), 55d, null, false, false));
        GewichtsklassenJpa jpa1 = new GewichtsklassenJpa(1, "Frauen", "w", teilnehmerJpa, "gruppe1", 60d, 50d);
        GewichtsklassenJpa jpa2 = new GewichtsklassenJpa(2, "Frauen", "w", teilnehmerJpa, "gruppe2", 60d, 50d);
        List<GewichtsklassenJpa> jpaList = Arrays.asList(jpa1, jpa2);
        when(gewichtsklassenJpaRepository.findAll()).thenReturn(jpaList);

        var teilnehmer = List.of(
                new Wettkaempfer(1, "Melanie", Geschlecht.w, Altersklasse.Frauen, new Verein(1, "Verein1"), 55d, null, false, false),
                new Wettkaempfer(2, "Mira", Geschlecht.w, Altersklasse.Frauen, new Verein(2, "Verein2"), 55d, null, false, false));
        GewichtsklassenGruppe gruppe1 = new GewichtsklassenGruppe(1, Altersklasse.Frauen, Optional.of(Geschlecht.w), teilnehmer, "gruppe1", 60d, 50d);
        GewichtsklassenGruppe gruppe2 = new GewichtsklassenGruppe(2, Altersklasse.Frauen, Optional.of(Geschlecht.w), teilnehmer, "gruppe2", 60d, 50d);
        when(gewichtsklassenConverter.convertToGewichtsklassen(jpa1)).thenReturn(gruppe1);
        when(gewichtsklassenConverter.convertToGewichtsklassen(jpa2)).thenReturn(gruppe2);

        // Execute method under test
        List<GewichtsklassenGruppe> result = gewichtsklassenRepository.findAll();

        // Verify results
        assertEquals(2, result.size());
        assertTrue(result.contains(gruppe1));
        assertTrue(result.contains(gruppe2));
    }

    @Test
    public void testDeleteAll() {
        // Execute method under test
        gewichtsklassenRepository.deleteAll();

        // Verify that deleteAll on the repository was called
        verify(gewichtsklassenJpaRepository, times(1)).deleteAll();
    }

    @Test
    public void testSaveAll() {
        // Setup mocks
        var teilnehmer = List.of(
                new Wettkaempfer(1, "Melanie", Geschlecht.w, Altersklasse.Frauen, new Verein(1, "Verein1"), 55d, null, false, false),
                new Wettkaempfer(2, "Mira", Geschlecht.w, Altersklasse.Frauen, new Verein(2, "Verein2"), 55d, null, false, false));
        GewichtsklassenGruppe gruppe1 = new GewichtsklassenGruppe(1, Altersklasse.Frauen, Optional.of(Geschlecht.w), teilnehmer, "gruppe1", 60d, 50d);
        GewichtsklassenGruppe gruppe2 = new GewichtsklassenGruppe(2, Altersklasse.Frauen, Optional.of(Geschlecht.w), teilnehmer, "gruppe2", 60d, 50d);
        List<GewichtsklassenGruppe> gruppenList = Arrays.asList(gruppe1, gruppe2);

        var teilnehmerJpa = List.of(
                new WettkaempferJpa(1, "Melanie", "w", "Frauen", new VereinJpa(1, "Verein1"), 55d, null, false, false),
                new WettkaempferJpa(2, "Mira", "w", "Frauen", new VereinJpa(2, "Verein2"), 55d, null, false, false));
        GewichtsklassenJpa jpa1 = new GewichtsklassenJpa(1, "Frauen", "w", teilnehmerJpa, "gruppe1", 60d, 50d);
        GewichtsklassenJpa jpa2 = new GewichtsklassenJpa(2, "Frauen", "w", teilnehmerJpa, "gruppe2", 60d, 50d);

        when(gewichtsklassenConverter.convertFromGewichtsklassen(gruppe1)).thenReturn(jpa1);
        when(gewichtsklassenConverter.convertFromGewichtsklassen(gruppe2)).thenReturn(jpa2);

        // Execute method under test
        gewichtsklassenRepository.saveAll(gruppenList);

        // Verify that saveAll on the repository was called
        verify(gewichtsklassenJpaRepository, times(1)).saveAll(Arrays.asList(jpa1, jpa2));
    }

    @Test
    public void testDeleteAllByAltersklasse() {
        // Setup mocks
        Altersklasse altersklasse = Altersklasse.U18;

        // Execute method under test
        gewichtsklassenRepository.deleteAllByAltersklasse(altersklasse);

        // Verify that deleteAllByAltersklasse on the repository was called
        verify(gewichtsklassenJpaRepository, times(1)).deleteAllByAltersklasse(altersklasse);
    }
}
