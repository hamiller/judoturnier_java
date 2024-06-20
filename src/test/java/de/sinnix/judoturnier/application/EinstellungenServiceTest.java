package de.sinnix.judoturnier.application;

import de.sinnix.judoturnier.adapter.secondary.EinstellungJpa;
import de.sinnix.judoturnier.adapter.secondary.EinstellungJpaRepository;
import de.sinnix.judoturnier.model.Einstellungen;
import de.sinnix.judoturnier.model.MattenAnzahl;
import de.sinnix.judoturnier.model.TurnierTyp;
import de.sinnix.judoturnier.model.WettkampfReihenfolge;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EinstellungenServiceTest {

    @Mock
    private EinstellungJpaRepository einstellungJpaRepository;

    @InjectMocks
    private EinstellungenService einstellungenService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testLadeEinstellungen() {
        EinstellungJpa turnierTypJpa = new EinstellungJpa(TurnierTyp.TYP, "RANDORI");
        EinstellungJpa mattenAnzahlJpa = new EinstellungJpa(MattenAnzahl.TYP, "4");
        EinstellungJpa wettkampfReihenfolgeJpa = new EinstellungJpa(WettkampfReihenfolge.TYP, "ABWECHSELND");

        when(einstellungJpaRepository.findAll()).thenReturn(List.of(turnierTypJpa, mattenAnzahlJpa, wettkampfReihenfolgeJpa));

        Einstellungen einstellungen = einstellungenService.ladeEinstellungen();

        assertEquals(TurnierTyp.RANDORI, einstellungen.turnierTyp());
        assertEquals(4, einstellungen.mattenAnzahl().anzahl());
        assertEquals(WettkampfReihenfolge.ABWECHSELND, einstellungen.wettkampfReihenfolge());
    }

    @Test
    void testSpeichereTurnierEinstellungen() {
        Einstellungen einstellungen = new Einstellungen(TurnierTyp.RANDORI, new MattenAnzahl(4), WettkampfReihenfolge.ABWECHSELND);

        EinstellungJpa turnierTypJpa = new EinstellungJpa(TurnierTyp.TYP, "RANDORI");
        EinstellungJpa mattenAnzahlJpa = new EinstellungJpa(MattenAnzahl.TYP, "4");
        EinstellungJpa wettkampfReihenfolgeJpa = new EinstellungJpa(WettkampfReihenfolge.TYP, "ABWECHSELND");

        when(einstellungJpaRepository.saveAll(anyList())).thenReturn(List.of(turnierTypJpa, mattenAnzahlJpa, wettkampfReihenfolgeJpa));
        when(einstellungJpaRepository.findAll()).thenReturn(List.of(turnierTypJpa, mattenAnzahlJpa, wettkampfReihenfolgeJpa));

        Einstellungen gespeicherteEinstellungen = einstellungenService.speichereTurnierEinstellungen(einstellungen);

        assertEquals(TurnierTyp.RANDORI, gespeicherteEinstellungen.turnierTyp());
        assertEquals(4, gespeicherteEinstellungen.mattenAnzahl().anzahl());
        assertEquals(WettkampfReihenfolge.ABWECHSELND, gespeicherteEinstellungen.wettkampfReihenfolge());
    }

    @Test
    void testIsRandori() {
        EinstellungJpa turnierTypJpa = new EinstellungJpa(TurnierTyp.TYP, "RANDORI");

        when(einstellungJpaRepository.findById(TurnierTyp.TYP)).thenReturn(Optional.of(turnierTypJpa));

        boolean isRandori = einstellungenService.isRandori();

        assertTrue(isRandori);
    }

    @Test
    void testIsNotRandori() {
        EinstellungJpa turnierTypJpa = new EinstellungJpa(TurnierTyp.TYP, "STANDARD");

        when(einstellungJpaRepository.findById(TurnierTyp.TYP)).thenReturn(Optional.of(turnierTypJpa));

        boolean isRandori = einstellungenService.isRandori();

        assertFalse(isRandori);
    }
}