package de.sinnix.judoturnier.adapter.secondary;

import de.sinnix.judoturnier.model.Verein;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VereinConverterTest {

    private VereinConverter vereinConverter;

    @BeforeEach
    void setUp() {
        vereinConverter = new VereinConverter();
    }

    @Test
    void testConvertToVerein() {
        VereinJpa vereinJpa = new VereinJpa(1, "Verein1");

        Verein verein = vereinConverter.converToVerein(vereinJpa);

        assertNotNull(verein);
        assertEquals(1, verein.id());
        assertEquals("Verein1", verein.name());
    }

    @Test
    void testConvertFromVerein() {
        Verein verein = new Verein(1, "Verein1");

        VereinJpa vereinJpa = vereinConverter.convertFromVerein(verein);

        assertNotNull(vereinJpa);
        assertEquals(1, vereinJpa.getId());
        assertEquals("Verein1", vereinJpa.getName());
    }
}