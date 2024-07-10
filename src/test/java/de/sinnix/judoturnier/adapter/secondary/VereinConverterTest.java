package de.sinnix.judoturnier.adapter.secondary;

import de.sinnix.judoturnier.model.Verein;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class VereinConverterTest {

    @InjectMocks
    private VereinConverter vereinConverter;

    @Test
    void testConvertToVerein() {
        UUID turnierUUID = UUID.randomUUID();
        VereinJpa vereinJpa = new VereinJpa(1, "Verein1", turnierUUID.toString());

        Verein verein = vereinConverter.converToVerein(vereinJpa);

        assertNotNull(verein);
        assertEquals(1, verein.id());
        assertEquals("Verein1", verein.name());
        assertEquals(turnierUUID.toString(), verein.turnierUUID().toString());
    }

    @Test
    void testConvertFromVerein() {
        UUID turnierUUID = UUID.randomUUID();
        Verein verein = new Verein(1, "Verein1", turnierUUID);

        VereinJpa vereinJpa = vereinConverter.convertFromVerein(verein);

        assertNotNull(vereinJpa);
        assertEquals(1, vereinJpa.getId());
        assertEquals("Verein1", vereinJpa.getName());
        assertEquals(turnierUUID.toString(), vereinJpa.getTurnierUUID());
    }
}