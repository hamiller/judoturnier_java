package de.sinnix.judoturnier.model;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RandoriGruppenNameTest {
    @Test
    public void testRandomRandoriGruppenNamen_NoDuplicates() {
        int count = 5;
        List<RandoriGruppenName> randomNames = RandoriGruppenName.randomRandoriGruppenNamen(count);

        // Prüfe, dass die Anzahl der zurückgegebenen Namen gleich der gewünschten Anzahl ist
        assertEquals(count, randomNames.size(), "Die Größe der zurückgegebenen Liste sollte " + count + " sein");

        // Prüfe, dass es keine Duplikate gibt
        Set<RandoriGruppenName> uniqueNames = new HashSet<>(randomNames);
        assertEquals(randomNames.size(), uniqueNames.size(), "Es sollten keine Duplikate in der Liste sein");

        // Prüfe, dass alle zurückgegebenen Namen gültige Enum-Werte sind
        for (RandoriGruppenName name : randomNames) {
            assertTrue(name instanceof RandoriGruppenName, "Alle zurückgegebenen Namen sollten gültige Enum-Werte sein");
        }
    }

    @Test
    public void testRandomRandoriGruppenNamen_AllValuesUsed() {
        // Teste mit der Anzahl der Enum-Werte
        int totalValues = RandoriGruppenName.values().length;
        List<RandoriGruppenName> randomNames = RandoriGruppenName.randomRandoriGruppenNamen(totalValues);

        // Prüfe, dass die Anzahl der zurückgegebenen Namen gleich der Anzahl der Enum-Werte ist
        assertEquals(totalValues, randomNames.size(), "Die Größe der zurückgegebenen Liste sollte " + totalValues + " sein");

        // Prüfe, dass es keine Duplikate gibt
        Set<RandoriGruppenName> uniqueNames = new HashSet<>(randomNames);
        assertEquals(randomNames.size(), uniqueNames.size(), "Es sollten keine Duplikate in der Liste sein");
    }
}