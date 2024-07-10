package de.sinnix.judoturnier.adapter.secondary;

import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Geschlecht;
import de.sinnix.judoturnier.model.Verein;
import de.sinnix.judoturnier.model.Wettkaempfer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WettkaempferRepositoryTest {

    @Mock
    private WettkaempferJpaRepository wettkaempferJpaRepository;

    @Mock
    private WettkaempferConverter wettkaempferConverter;

    @Mock
    private VereinConverter vereinConverter;

    @InjectMocks
    private WettkaempferRepository wettkaempferRepository;

    private Wettkaempfer wettkaempfer;
    private WettkaempferJpa wettkaempferJpa;
    private UUID turnierUUID;

    @BeforeEach
    public void setUp() {
        turnierUUID = UUID.randomUUID();
        Verein verein = new Verein(1, "Verein A", turnierUUID);
        wettkaempfer = new Wettkaempfer(1, "John Doe", Geschlecht.m, Altersklasse.U18, verein, 70d, Optional.empty(), false, false, turnierUUID);
        wettkaempferJpa = new WettkaempferJpa();
        wettkaempferJpa.setId(1);
        wettkaempferJpa.setName("John Doe");
        wettkaempferJpa.setGeschlecht("MAENNLICH");
        wettkaempferJpa.setAltersklasse("U18");
        wettkaempferJpa.setVerein(new VereinJpa(1, "Verein A", turnierUUID.toString()));
        wettkaempferJpa.setGewicht(70d);
        wettkaempferJpa.setFarbe(null);
        wettkaempferJpa.setChecked(false);
        wettkaempferJpa.setPrinted(false);
        wettkaempferJpa.setTurnierUUID(turnierUUID.toString());
    }

    @Test
    public void testFindAll() {
        when(wettkaempferJpaRepository.findAllByTurnierUUID(turnierUUID.toString())).thenReturn(List.of(wettkaempferJpa));
        when(wettkaempferConverter.convertToWettkaempfer(wettkaempferJpa)).thenReturn(wettkaempfer);

        List<Wettkaempfer> result = wettkaempferRepository.findAll(turnierUUID);

        assertEquals(1, result.size());
        assertEquals(wettkaempfer, result.get(0));
        verify(wettkaempferJpaRepository, times(1)).findAllByTurnierUUID(turnierUUID.toString());
        verify(wettkaempferConverter, times(1)).convertToWettkaempfer(wettkaempferJpa);
    }

    @Test
    public void testDeleteById() {
        when(wettkaempferJpaRepository.findById(1)).thenReturn(Optional.of(wettkaempferJpa));

        wettkaempferRepository.deleteById(1);

        verify(wettkaempferJpaRepository, times(1)).findById(1);
        verify(wettkaempferJpaRepository, times(1)).deleteById(1);
    }

    @Test
    public void testFindById() {
        when(wettkaempferJpaRepository.findById(1)).thenReturn(Optional.of(wettkaempferJpa));
        when(wettkaempferConverter.convertToWettkaempfer(wettkaempferJpa)).thenReturn(wettkaempfer);

        Optional<Wettkaempfer> result = wettkaempferRepository.findById(1);

        assertNotNull(result);
        assertEquals(wettkaempfer, result.get());
        verify(wettkaempferJpaRepository, times(1)).findById(1);
        verify(wettkaempferConverter, times(1)).convertToWettkaempfer(wettkaempferJpa);
    }

    @Test
    public void testSave_NewWettkaempfer() {
        when(wettkaempferJpaRepository.save(any(WettkaempferJpa.class))).thenReturn(wettkaempferJpa);
        when(wettkaempferConverter.convertFromWettkaempfer(wettkaempfer)).thenReturn(wettkaempferJpa);
        when(wettkaempferConverter.convertToWettkaempfer(wettkaempferJpa)).thenReturn(wettkaempfer);

        Wettkaempfer result = wettkaempferRepository.save(wettkaempfer);

        assertNotNull(result);
        assertEquals(wettkaempfer, result);
        verify(wettkaempferJpaRepository, times(1)).save(wettkaempferJpa);
        verify(wettkaempferConverter, times(1)).convertFromWettkaempfer(wettkaempfer);
        verify(wettkaempferConverter, times(1)).convertToWettkaempfer(wettkaempferJpa);
    }

    @Test
    public void testSave_ExistingWettkaempfer() {
        when(wettkaempferJpaRepository.findById(1)).thenReturn(Optional.of(wettkaempferJpa));
        when(wettkaempferJpaRepository.save(any(WettkaempferJpa.class))).thenReturn(wettkaempferJpa);
        when(wettkaempferConverter.convertToWettkaempfer(wettkaempferJpa)).thenReturn(wettkaempfer);

        Wettkaempfer result = wettkaempferRepository.save(wettkaempfer);

        assertNotNull(result);
        assertEquals(wettkaempfer, result);
        verify(wettkaempferJpaRepository, times(1)).findById(1);
        verify(wettkaempferJpaRepository, times(1)).save(wettkaempferJpa);
        verify(wettkaempferConverter, times(1)).convertToWettkaempfer(wettkaempferJpa);
    }
}