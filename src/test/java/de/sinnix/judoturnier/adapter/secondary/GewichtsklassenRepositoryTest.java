package de.sinnix.judoturnier.adapter.secondary;

import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Geschlecht;
import de.sinnix.judoturnier.model.GewichtsklassenGruppe;
import de.sinnix.judoturnier.model.RandoriGruppenName;
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
import java.util.UUID;

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

    private UUID turnierUUID;

    @BeforeEach
    public void setUp() {
        turnierUUID = UUID.randomUUID();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAll() {
        // Setup mocks
        var teilnehmerJpa = List.of(
                new WettkaempferJpa(1, "Melanie", "w", "Frauen", new VereinJpa(1, "Verein1", turnierUUID.toString()), 55d, null, false, false, turnierUUID.toString()),
                new WettkaempferJpa(2, "Mira", "w", "Frauen", new VereinJpa(2, "Verein2", turnierUUID.toString()), 55d, null, false, false, turnierUUID.toString()));
        GewichtsklassenJpa jpa1 = new GewichtsklassenJpa(1, "Frauen", "w", teilnehmerJpa, "Adler", 60d, 50d, turnierUUID.toString());
        GewichtsklassenJpa jpa2 = new GewichtsklassenJpa(2, "Frauen", "w", teilnehmerJpa, "Bär", 60d, 50d, turnierUUID.toString());
        List<GewichtsklassenJpa> jpaList = Arrays.asList(jpa1, jpa2);
        when(gewichtsklassenJpaRepository.findAllByTurnierUUID(turnierUUID.toString())).thenReturn(jpaList);

        var teilnehmer = List.of(
                new Wettkaempfer(1, "Melanie", Geschlecht.w, Altersklasse.Frauen, new Verein(1, "Verein1", turnierUUID), 55d, null, false, false, turnierUUID),
                new Wettkaempfer(2, "Mira", Geschlecht.w, Altersklasse.Frauen, new Verein(2, "Verein2", turnierUUID), 55d, null, false, false, turnierUUID));
        GewichtsklassenGruppe gruppe1 = new GewichtsklassenGruppe(1, Altersklasse.Frauen, Optional.of(Geschlecht.w), teilnehmer, Optional.of(RandoriGruppenName.Adler), 60d, 50d, turnierUUID);
        GewichtsklassenGruppe gruppe2 = new GewichtsklassenGruppe(2, Altersklasse.Frauen, Optional.of(Geschlecht.w), teilnehmer, Optional.of(RandoriGruppenName.Bär), 60d, 50d, turnierUUID);
        when(gewichtsklassenConverter.convertToGewichtsklassen(jpa1)).thenReturn(gruppe1);
        when(gewichtsklassenConverter.convertToGewichtsklassen(jpa2)).thenReturn(gruppe2);

        // Execute method under test
        List<GewichtsklassenGruppe> result = gewichtsklassenRepository.findAll(turnierUUID);

        // Verify results
        assertEquals(2, result.size());
        assertTrue(result.contains(gruppe1));
        assertTrue(result.contains(gruppe2));
    }

    @Test
    public void testDeleteAll() {
        // Execute method under test
        gewichtsklassenRepository.deleteAll(turnierUUID);

        // Verify that deleteAll on the repository was called
        verify(gewichtsklassenJpaRepository, times(1)).deleteAllByTurnierUUID(turnierUUID.toString());
    }

    @Test
    public void testSaveAll() {
        // Setup mocks
        var teilnehmer = List.of(
                new Wettkaempfer(1, "Melanie", Geschlecht.w, Altersklasse.Frauen, new Verein(1, "Verein1", turnierUUID), 55d, null, false, false, turnierUUID),
                new Wettkaempfer(2, "Mira", Geschlecht.w, Altersklasse.Frauen, new Verein(2, "Verein2", turnierUUID), 55d, null, false, false, turnierUUID));
        GewichtsklassenGruppe gruppe1 = new GewichtsklassenGruppe(1, Altersklasse.Frauen, Optional.of(Geschlecht.w), teilnehmer, Optional.of(RandoriGruppenName.Adler), 60d, 50d, turnierUUID);
        GewichtsklassenGruppe gruppe2 = new GewichtsklassenGruppe(2, Altersklasse.Frauen, Optional.of(Geschlecht.w), teilnehmer, Optional.of(RandoriGruppenName.Bär), 60d, 50d, turnierUUID);
        List<GewichtsklassenGruppe> gruppenList = Arrays.asList(gruppe1, gruppe2);

        var teilnehmerJpa = List.of(
                new WettkaempferJpa(1, "Melanie", "w", "Frauen", new VereinJpa(1, "Verein1", turnierUUID.toString()), 55d, null, false, false, turnierUUID.toString()),
                new WettkaempferJpa(2, "Mira", "w", "Frauen", new VereinJpa(2, "Verein2", turnierUUID.toString()), 55d, null, false, false, turnierUUID.toString()));
        GewichtsklassenJpa jpa1 = new GewichtsklassenJpa(1, "Frauen", "w", teilnehmerJpa, "Adler", 60d, 50d, turnierUUID.toString());
        GewichtsklassenJpa jpa2 = new GewichtsklassenJpa(2, "Frauen", "w", teilnehmerJpa, "Bär", 60d, 50d, turnierUUID.toString());

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
        gewichtsklassenRepository.deleteAllByAltersklasse(turnierUUID, altersklasse);

        // Verify that deleteAllByAltersklasse on the repository was called
        verify(gewichtsklassenJpaRepository, times(1)).deleteAllByAltersklasseAndTurnierUUID(altersklasse.name(), turnierUUID.toString());
    }
}
