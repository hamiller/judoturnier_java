package de.sinnix.judoturnier.application;

import de.sinnix.judoturnier.adapter.secondary.EinstellungJpa;
import de.sinnix.judoturnier.adapter.secondary.EinstellungJpaRepository;
import de.sinnix.judoturnier.model.Einstellungen;
import de.sinnix.judoturnier.model.MattenAnzahl;
import de.sinnix.judoturnier.model.Gruppengroesse;
import de.sinnix.judoturnier.model.SeparateAlterklassen;
import de.sinnix.judoturnier.model.TurnierTyp;
import de.sinnix.judoturnier.model.VariablerGewichtsteil;
import de.sinnix.judoturnier.model.WettkampfReihenfolge;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EinstellungenServiceTest {

    @Mock
    private EinstellungJpaRepository einstellungJpaRepository;

    @InjectMocks
    private EinstellungenService einstellungenService;

    private UUID turnierUUID;

    @BeforeEach
    void setUp() {
        turnierUUID = UUID.randomUUID();
    }

    @Test
    void testLadeEinstellungen() {
        EinstellungJpa turnierTypJpa = new EinstellungJpa(new EinstellungJpa.EinstellungId(TurnierTyp.TYP, turnierUUID.toString()), "RANDORI");
        EinstellungJpa mattenAnzahlJpa = new EinstellungJpa(new EinstellungJpa.EinstellungId(MattenAnzahl.TYP, turnierUUID.toString()), "4");
        EinstellungJpa wettkampfReihenfolgeJpa = new EinstellungJpa(new EinstellungJpa.EinstellungId(WettkampfReihenfolge.TYP, turnierUUID.toString()), "ABWECHSELND");

        when(einstellungJpaRepository.findAll()).thenReturn(List.of(turnierTypJpa, mattenAnzahlJpa, wettkampfReihenfolgeJpa));

        Einstellungen einstellungen = einstellungenService.ladeEinstellungen(turnierUUID);

        assertEquals(TurnierTyp.RANDORI, einstellungen.turnierTyp());
        assertEquals(4, einstellungen.mattenAnzahl().anzahl());
        assertEquals(WettkampfReihenfolge.ABWECHSELND, einstellungen.wettkampfReihenfolge());
        assertEquals(turnierUUID.toString(), einstellungen.turnierUUID().toString());
    }

    @Test
    void testSpeichereTurnierEinstellungen() {
        Einstellungen einstellungen = new Einstellungen(TurnierTyp.RANDORI, new MattenAnzahl(4), WettkampfReihenfolge.ABWECHSELND, new Gruppengroesse(6), new VariablerGewichtsteil(0.2d), SeparateAlterklassen.ZUSAMMEN, turnierUUID);

        EinstellungJpa turnierTypJpa = new EinstellungJpa(new EinstellungJpa.EinstellungId(TurnierTyp.TYP, turnierUUID.toString()), "RANDORI");
        EinstellungJpa mattenAnzahlJpa = new EinstellungJpa(new EinstellungJpa.EinstellungId(MattenAnzahl.TYP, turnierUUID.toString()), "4");
        EinstellungJpa wettkampfReihenfolgeJpa = new EinstellungJpa(new EinstellungJpa.EinstellungId(WettkampfReihenfolge.TYP, turnierUUID.toString()), "ABWECHSELND");

        when(einstellungJpaRepository.saveAll(anyList())).thenReturn(List.of(turnierTypJpa, mattenAnzahlJpa, wettkampfReihenfolgeJpa));
        when(einstellungJpaRepository.findAll()).thenReturn(List.of(turnierTypJpa, mattenAnzahlJpa, wettkampfReihenfolgeJpa));

        Einstellungen gespeicherteEinstellungen = einstellungenService.speichereTurnierEinstellungen(einstellungen);

        assertEquals(TurnierTyp.RANDORI, gespeicherteEinstellungen.turnierTyp());
        assertEquals(4, gespeicherteEinstellungen.mattenAnzahl().anzahl());
        assertEquals(WettkampfReihenfolge.ABWECHSELND, gespeicherteEinstellungen.wettkampfReihenfolge());
    }

    @Test
    void testIsRandori() {
        EinstellungJpa turnierTypJpa = new EinstellungJpa(new EinstellungJpa.EinstellungId(TurnierTyp.TYP, turnierUUID.toString()), "RANDORI");

        when(einstellungJpaRepository.findById(new EinstellungJpa.EinstellungId(TurnierTyp.TYP, turnierUUID.toString()))).thenReturn(Optional.of(turnierTypJpa));

        boolean isRandori = einstellungenService.isRandori(turnierUUID);

        assertTrue(isRandori);
    }

    @Test
    void testIsNotRandori() {
        EinstellungJpa turnierTypJpa = new EinstellungJpa(new EinstellungJpa.EinstellungId(TurnierTyp.TYP, turnierUUID.toString()), "STANDARD");

        when(einstellungJpaRepository.findById(new EinstellungJpa.EinstellungId(TurnierTyp.TYP, turnierUUID.toString()))).thenReturn(Optional.of(turnierTypJpa));

        boolean isRandori = einstellungenService.isRandori(turnierUUID);

        assertFalse(isRandori);
    }
}