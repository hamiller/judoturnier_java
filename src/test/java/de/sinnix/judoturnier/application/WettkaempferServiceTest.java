package de.sinnix.judoturnier.application;

import de.sinnix.judoturnier.adapter.secondary.VereinConverter;
import de.sinnix.judoturnier.adapter.secondary.VereinJpa;
import de.sinnix.judoturnier.adapter.secondary.WettkaempferConverter;
import de.sinnix.judoturnier.adapter.secondary.WettkaempferJpa;
import de.sinnix.judoturnier.adapter.secondary.WettkaempferJpaRepository;
import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Farbe;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WettkaempferServiceTest {

    @Mock
    private WettkaempferJpaRepository wettkaempferJpaRepository;

    @Mock
    private WettkaempferConverter wettkaempferConverter;

    @Mock
    private VereinConverter vereinConverter;

    @InjectMocks
    private WettkaempferService wettkaempferService;

    private Wettkaempfer wettkaempfer;
    private WettkaempferJpa wettkaempferJpa;

    @BeforeEach
    void setUp() {
        wettkaempfer = new Wettkaempfer(1, "Max", Geschlecht.m, Altersklasse.U18, new Verein(1,"Verein1"), 70d, Optional.of(Farbe.BLAU), true, false);
        wettkaempferJpa = new WettkaempferJpa(1, "Max", "m", "U18", new VereinJpa(1,"Verein1"), 70d, "BLAU", true, false);
    }

    @Test
    void testAlleKaempfer() {
        when(wettkaempferJpaRepository.findAll()).thenReturn(List.of(wettkaempferJpa));
        when(wettkaempferConverter.convertToWettkaempfer(any(WettkaempferJpa.class))).thenReturn(wettkaempfer);

        List<Wettkaempfer> wettkaempferList = wettkaempferService.alleKaempfer();

        assertEquals(1, wettkaempferList.size());
        assertEquals(wettkaempfer, wettkaempferList.get(0));
    }

    @Test
    void testLoescheKaempfer() {
        when(wettkaempferJpaRepository.findById(anyInt())).thenReturn(Optional.of(wettkaempferJpa));

        wettkaempferService.loescheKaempfer(1);

        verify(wettkaempferJpaRepository, times(1)).deleteById(1);
    }

    @Test
    void testLoescheKaempferNotFound() {
        when(wettkaempferJpaRepository.findById(anyInt())).thenReturn(Optional.empty());

        wettkaempferService.loescheKaempfer(1);

        verify(wettkaempferJpaRepository, never()).deleteById(anyInt());
    }

    @Test
    void testLadeKaempfer() {
        when(wettkaempferJpaRepository.findById(anyInt())).thenReturn(Optional.of(wettkaempferJpa));
        when(wettkaempferConverter.convertToWettkaempfer(any(WettkaempferJpa.class))).thenReturn(wettkaempfer);

        Wettkaempfer result = wettkaempferService.ladeKaempfer(1);

        assertEquals(wettkaempfer, result);
    }

    @Test
    void testLadeKaempferNotFound() {
        when(wettkaempferJpaRepository.findById(anyInt())).thenReturn(Optional.empty());

        Wettkaempfer result = wettkaempferService.ladeKaempfer(1);

        assertNull(result);
    }

    @Test
    void testSpeichereKaempfer() {
        when(wettkaempferJpaRepository.findById(anyInt())).thenReturn(Optional.of(wettkaempferJpa));
        when(wettkaempferJpaRepository.save(any(WettkaempferJpa.class))).thenReturn(wettkaempferJpa);
        when(wettkaempferConverter.convertToWettkaempfer(any(WettkaempferJpa.class))).thenReturn(wettkaempfer);
        when(vereinConverter.convertFromVerein(any(Verein.class))).thenReturn(new VereinJpa(1,"Verein1"));

        Wettkaempfer result = wettkaempferService.speichereKaempfer(wettkaempfer);

        assertEquals(wettkaempfer, result);
    }

    @Test
    void testSpeichereNeuenKaempfer() {
        when(wettkaempferJpaRepository.save(any(WettkaempferJpa.class))).thenReturn(wettkaempferJpa);
        when(wettkaempferConverter.convertToWettkaempfer(any(WettkaempferJpa.class))).thenReturn(wettkaempfer);
        when(wettkaempferConverter.convertFromWettkaempfer(any(Wettkaempfer.class))).thenReturn(wettkaempferJpa);

        Wettkaempfer result = wettkaempferService.speichereKaempfer(wettkaempfer);

        assertEquals(wettkaempfer, result);
    }
}