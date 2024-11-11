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

import java.util.Optional;
import java.util.UUID;

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
    private UUID turnierUUID;
    private UUID vereinUUID;
    private UUID wkUUID;

    @BeforeEach
    void setUp() {
        turnierUUID = UUID.randomUUID();
        vereinUUID = UUID.randomUUID();
        wkUUID = UUID.randomUUID();
        verein = new Verein(vereinUUID, "Verein1", turnierUUID);
        vereinJpa = new VereinJpa(vereinUUID.toString(), "Verein1", turnierUUID.toString());

        wettkaempfer = new Wettkaempfer(wkUUID, "Max", Geschlecht.m, Altersklasse.U18, verein, 70d, Optional.of(Farbe.BLAU), true, false, turnierUUID);
        wettkaempferJpa = new WettkaempferJpa(wkUUID.toString(), "Max", "m", "U18", vereinJpa, 70d, "BLAU", true, false, turnierUUID.toString());
    }

    @Test
    void testConvertFromWettkaempfer() {
        when(vereinConverter.convertFromVerein(any(Verein.class))).thenReturn(vereinJpa);

        WettkaempferJpa result = wettkaempferConverter.convertFromWettkaempfer(wettkaempfer);

        assertNotNull(result);
        assertEquals(wettkaempfer.id().toString(), result.getUuid());
        assertEquals(wettkaempfer.name(), result.getName());
        assertEquals(wettkaempfer.geschlecht().name(), result.getGeschlecht());
        assertEquals(wettkaempfer.altersklasse().name(), result.getAltersklasse());
        assertEquals(wettkaempfer.gewicht(), result.getGewicht());
        assertEquals(wettkaempfer.farbe().get().name(), result.getFarbe());
        assertEquals(wettkaempfer.checked(), result.getChecked());
        assertEquals(wettkaempfer.printed(), result.getPrinted());
        assertEquals(vereinJpa, result.getVerein());
    }

    @Test
    void testConvertToWettkaempfer() {
        when(vereinConverter.converToVerein(any(VereinJpa.class))).thenReturn(verein);

        Wettkaempfer result = wettkaempferConverter.convertToWettkaempfer(wettkaempferJpa);

        assertNotNull(result);
        assertEquals(wettkaempferJpa.getUuid(), result.id().toString());
        assertEquals(wettkaempferJpa.getName(), result.name());
        assertEquals(wettkaempferJpa.getGeschlecht(), result.geschlecht().name());
        assertEquals(wettkaempferJpa.getAltersklasse(), result.altersklasse().name());
        assertEquals(wettkaempferJpa.getGewicht(), result.gewicht());
        assertEquals(wettkaempferJpa.getFarbe(), result.farbe().get().name());
        assertEquals(wettkaempferJpa.getChecked(), result.checked());
        assertEquals(wettkaempferJpa.getPrinted(), result.printed());
        assertEquals(verein, result.verein());
    }

    @Test
    void convertMissingFields() {
        Wettkaempfer wettkaempfer = new Wettkaempfer(UUID.randomUUID(), "Name", Geschlecht.m, Altersklasse.U18, verein, 0d, Optional.empty(), false, false, turnierUUID);

        when(vereinConverter.converToVerein(any(VereinJpa.class))).thenReturn(verein);
        when(vereinConverter.convertFromVerein(any(Verein.class))).thenReturn(vereinJpa);

        var jpa = wettkaempferConverter.convertFromWettkaempfer(wettkaempfer);
        var converted = wettkaempferConverter.convertToWettkaempfer(jpa);

        assertEquals(wettkaempfer, converted);
    }

    @Test
    void readJpaWithMissingFields() {
        WettkaempferJpa wettkaempferJpa = new WettkaempferJpa(wkUUID.toString(), "Name", null, null, vereinJpa, 0d, "", false, false, turnierUUID.toString());
        Wettkaempfer wettkaempfer = new Wettkaempfer(wkUUID, "Name", null, null, verein, 0d, Optional.empty(), false, false, turnierUUID);

        when(vereinConverter.converToVerein(any(VereinJpa.class))).thenReturn(verein);

        var converted = wettkaempferConverter.convertToWettkaempfer(wettkaempferJpa);

        assertEquals(converted, wettkaempfer);
    }
}