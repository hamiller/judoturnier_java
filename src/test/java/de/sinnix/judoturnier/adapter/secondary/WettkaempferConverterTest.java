package de.sinnix.judoturnier.adapter.secondary;

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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WettkaempferConverterTest {

    @Mock
    private VereinConverter vereinConverter;

    @InjectMocks
    private WettkaempferConverter wettkaempferConverter;

    private Wettkaempfer wettkaempfer;
    private WettkaempferJpa wettkaempferJpa;
    private Verein verein;
    private VereinJpa vereinJpa;

    @BeforeEach
    void setUp() {
        verein = new Verein(1, "Verein1");
        vereinJpa = new VereinJpa(1, "Verein1");

        wettkaempfer = new Wettkaempfer(1, "Max", Geschlecht.m, Altersklasse.U18, verein, 70d, Farbe.BLAU, true, false);
        wettkaempferJpa = new WettkaempferJpa(1, "Max", Geschlecht.m, Altersklasse.U18, vereinJpa, 70d, Farbe.BLAU, true, false);
    }

    @Test
    void testConvertFromWettkaempfer() {
        when(vereinConverter.convertFromVerein(any(Verein.class))).thenReturn(vereinJpa);

        WettkaempferJpa result = wettkaempferConverter.convertFromWettkaempfer(wettkaempfer);

        assertNotNull(result);
        assertEquals(wettkaempfer.id(), result.getId());
        assertEquals(wettkaempfer.name(), result.getName());
        assertEquals(wettkaempfer.geschlecht(), result.getGeschlecht());
        assertEquals(wettkaempfer.altersklasse(), result.getAltersklasse());
        assertEquals(wettkaempfer.gewicht(), result.getGewicht());
        assertEquals(wettkaempfer.farbe(), result.getFarbe());
        assertEquals(wettkaempfer.checked(), result.getChecked());
        assertEquals(wettkaempfer.printed(), result.getPrinted());
        assertEquals(vereinJpa, result.getVerein());
    }

    @Test
    void testConvertToWettkaempfer() {
        when(vereinConverter.converToVerein(any(VereinJpa.class))).thenReturn(verein);

        Wettkaempfer result = wettkaempferConverter.convertToWettkaempfer(wettkaempferJpa);

        assertNotNull(result);
        assertEquals(wettkaempferJpa.getId(), result.id());
        assertEquals(wettkaempferJpa.getName(), result.name());
        assertEquals(wettkaempferJpa.getGeschlecht(), result.geschlecht());
        assertEquals(wettkaempferJpa.getAltersklasse(), result.altersklasse());
        assertEquals(wettkaempferJpa.getGewicht(), result.gewicht());
        assertEquals(wettkaempferJpa.getFarbe(), result.farbe());
        assertEquals(wettkaempferJpa.getChecked(), result.checked());
        assertEquals(wettkaempferJpa.getPrinted(), result.printed());
        assertEquals(verein, result.verein());
    }
}