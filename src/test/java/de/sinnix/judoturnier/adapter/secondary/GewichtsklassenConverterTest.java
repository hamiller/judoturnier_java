package de.sinnix.judoturnier.adapter.secondary;

import de.sinnix.judoturnier.fixtures.WettkaempferFixtures;
import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Geschlecht;
import de.sinnix.judoturnier.model.GewichtsklassenGruppe;
import de.sinnix.judoturnier.model.RandoriGruppenName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GewichtsklassenConverterTest {

    @Mock
    private WettkaempferConverter wettkaempferConverter;

    @InjectMocks
    private GewichtsklassenConverter converter = new GewichtsklassenConverter();

    @Test
    void convertToGewichtsklassen() {
        UUID turnierUUID = UUID.randomUUID();
        var teilnehmer = WettkaempferFixtures.wettkaempferList;
        var teilnehmerJpa = WettkaempferFixtures.wettkaempferJpaList;
        when(wettkaempferConverter.convertToWettkaempferList(anyList())).thenReturn(teilnehmer);
        when(wettkaempferConverter.convertFromWettkaempferList(anyList())).thenReturn(teilnehmerJpa);

        GewichtsklassenJpa jpa = new GewichtsklassenJpa(1, "U18", "m", teilnehmerJpa, "Tiger", 12d, 123.0, turnierUUID.toString());
        var gewichtklassen = converter.convertToGewichtsklassen(jpa);
        var converted = converter.convertFromGewichtsklassen(gewichtklassen);

        assertEquals(jpa, converted);
    }

    @Test
    void convertFromGewichtsklassen() {
        UUID turnierUUID = UUID.randomUUID();
        var teilnehmer = WettkaempferFixtures.wettkaempferList;
        var teilnehmerJpa = WettkaempferFixtures.wettkaempferJpaList;
        when(wettkaempferConverter.convertToWettkaempferList(anyList())).thenReturn(teilnehmer);
        when(wettkaempferConverter.convertFromWettkaempferList(anyList())).thenReturn(teilnehmerJpa);

        GewichtsklassenGruppe gewichtklassen = new GewichtsklassenGruppe(1, Altersklasse.U18, Optional.of(Geschlecht.w), teilnehmer, Optional.of(RandoriGruppenName.Adler), 12d, 133.0, turnierUUID);
        var jpa = converter.convertFromGewichtsklassen(gewichtklassen);
        var converted = converter.convertToGewichtsklassen(jpa);

        assertEquals(gewichtklassen, converted);
    }

    @Test
    void convertMissingFields() {
        UUID turnierUUID = UUID.randomUUID();
        var teilnehmer = WettkaempferFixtures.wettkaempferList;
        var teilnehmerJpa = WettkaempferFixtures.wettkaempferJpaList;
        when(wettkaempferConverter.convertToWettkaempferList(anyList())).thenReturn(teilnehmer);
        when(wettkaempferConverter.convertFromWettkaempferList(anyList())).thenReturn(teilnehmerJpa);

        GewichtsklassenGruppe gewichtklassen = new GewichtsklassenGruppe(1, Altersklasse.U18, Optional.empty(), teilnehmer, Optional.of(RandoriGruppenName.Adler), 12d, 133.0, turnierUUID);
        var jpa = converter.convertFromGewichtsklassen(gewichtklassen);
        var converted = converter.convertToGewichtsklassen(jpa);

        assertEquals(gewichtklassen, converted);
    }
}