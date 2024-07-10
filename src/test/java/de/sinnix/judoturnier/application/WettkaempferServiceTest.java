package de.sinnix.judoturnier.application;

import de.sinnix.judoturnier.adapter.secondary.WettkaempferRepository;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WettkaempferServiceTest {

    @Mock
    private WettkaempferRepository wettkaempferRepository;

    @InjectMocks
    private WettkaempferService wettkaempferService;

    private Wettkaempfer wettkaempfer;
    private UUID         turnierUUID;

    @BeforeEach
    void setUp() {
        turnierUUID = UUID.randomUUID();
        wettkaempfer = new Wettkaempfer(1, "Max", Geschlecht.m, Altersklasse.U18, new Verein(1,"Verein1", turnierUUID), 70d, Optional.of(Farbe.BLAU), true, false, turnierUUID);
    }

    @Test
    void testAlleKaempfer() {
        when(wettkaempferRepository.findAll(turnierUUID)).thenReturn(List.of(wettkaempfer));

        List<Wettkaempfer> wettkaempferList = wettkaempferService.alleKaempfer(turnierUUID);

        assertEquals(1, wettkaempferList.size());
        assertEquals(wettkaempfer, wettkaempferList.get(0));
    }

    @Test
    void testLoescheKaempfer() {
        wettkaempferService.loescheKaempfer(1);

        verify(wettkaempferRepository, times(1)).deleteById(1);
    }

    @Test
    void testLadeKaempfer() {
        when(wettkaempferRepository.findById(anyInt())).thenReturn(Optional.of(wettkaempfer));

        Optional<Wettkaempfer> result = wettkaempferService.ladeKaempfer(1);

        assertEquals(wettkaempfer, result.get());
    }

    @Test
    void testLadeKaempferNotFound() {
        when(wettkaempferRepository.findById(anyInt())).thenReturn(Optional.empty());

        Optional<Wettkaempfer> result = wettkaempferService.ladeKaempfer(1);

        assertTrue(result.isEmpty());
    }

    @Test
    void testSpeichereKaempfer() {
        when(wettkaempferRepository.save(any(Wettkaempfer.class))).thenReturn(wettkaempfer);

        Wettkaempfer result = wettkaempferService.speichereKaempfer(wettkaempfer);

        assertEquals(wettkaempfer, result);
    }
}