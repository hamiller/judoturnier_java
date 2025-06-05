package de.sinnix.judoturnier.adapter.secondary;

import de.sinnix.judoturnier.adapter.secondary.converter.WettkaempferConverter;
import de.sinnix.judoturnier.adapter.secondary.jpa.VereinJpa;
import de.sinnix.judoturnier.adapter.secondary.jpa.WettkaempferJpa;
import de.sinnix.judoturnier.model.Altersklasse;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WettkaempferRepositoryTest {

    @Mock
    private WettkaempferJpaRepository wettkaempferJpaRepository;

    @Mock
    private WettkaempferConverter wettkaempferConverter;

    @Mock
    private VereinJpaRepository vereinJpaRepository;

    @InjectMocks
    private WettkaempferRepository wettkaempferRepository;

    private Wettkaempfer    wettkaempfer;
    private WettkaempferJpa wettkaempferJpa;
	private VereinJpa       vereinJpa;
    private UUID            turnierUUID;
    private UUID wkUUID;
    private UUID vUUID;

    @BeforeEach
    public void setUp() {
        turnierUUID = UUID.randomUUID();
        wkUUID = UUID.randomUUID();
        vUUID = UUID.randomUUID();
        Verein verein = new Verein(vUUID, "Verein A", turnierUUID);
        wettkaempfer = new Wettkaempfer(wkUUID, "John Doe", Geschlecht.m, Altersklasse.U18, verein, 70d, Optional.empty(), false, false, turnierUUID);
        wettkaempferJpa = new WettkaempferJpa();
        wettkaempferJpa.setId(wkUUID);
        wettkaempferJpa.setName("John Doe");
        wettkaempferJpa.setGeschlecht("MAENNLICH");
        wettkaempferJpa.setAltersklasse("U18");
		vereinJpa = new VereinJpa("Verein A", turnierUUID);
		vereinJpa.setId(vUUID);
        wettkaempferJpa.setVerein(vereinJpa);
        wettkaempferJpa.setGewicht(70d);
        wettkaempferJpa.setFarbe(null);
        wettkaempferJpa.setChecked(false);
        wettkaempferJpa.setPrinted(false);
        wettkaempferJpa.setTurnierUUID(turnierUUID);
    }

    @Test
    public void testFindAll() {
        when(wettkaempferJpaRepository.findAllByTurnierUUID(turnierUUID)).thenReturn(List.of(wettkaempferJpa));
        when(wettkaempferConverter.convertToWettkaempfer(wettkaempferJpa)).thenReturn(wettkaempfer);

        List<Wettkaempfer> result = wettkaempferRepository.findAll(turnierUUID);

        assertEquals(1, result.size());
        assertEquals(wettkaempfer, result.get(0));
        verify(wettkaempferJpaRepository, times(1)).findAllByTurnierUUID(turnierUUID);
        verify(wettkaempferConverter, times(1)).convertToWettkaempfer(wettkaempferJpa);
    }

    @Test
    public void testDeleteById() {
        when(wettkaempferJpaRepository.findById(wkUUID)).thenReturn(Optional.of(wettkaempferJpa));

        wettkaempferRepository.deleteById(wkUUID);

        verify(wettkaempferJpaRepository, times(1)).findById(wkUUID);
        verify(wettkaempferJpaRepository, times(1)).deleteById(wkUUID);
    }

    @Test
    public void testFindById() {
        when(wettkaempferJpaRepository.findById(wkUUID)).thenReturn(Optional.of(wettkaempferJpa));
        when(wettkaempferConverter.convertToWettkaempfer(wettkaempferJpa)).thenReturn(wettkaempfer);

        Optional<Wettkaempfer> result = wettkaempferRepository.findById(wkUUID);

        assertNotNull(result);
        assertEquals(wettkaempfer, result.get());
        verify(wettkaempferJpaRepository, times(1)).findById(wkUUID);
        verify(wettkaempferConverter, times(1)).convertToWettkaempfer(wettkaempferJpa);
    }

    @Test
    public void testSave_NewWettkaempfer() {
		when(vereinJpaRepository.findById(any())).thenReturn(Optional.of(vereinJpa));
        when(wettkaempferJpaRepository.save(any(WettkaempferJpa.class))).thenReturn(wettkaempferJpa);
        when(wettkaempferConverter.convertFromWettkaempfer(wettkaempfer)).thenReturn(wettkaempferJpa);
        when(wettkaempferConverter.convertToWettkaempfer(wettkaempferJpa)).thenReturn(wettkaempfer);

        Wettkaempfer result = wettkaempferRepository.save(wettkaempfer);

        assertNotNull(result);
        assertEquals(wettkaempfer, result);
        verify(wettkaempferJpaRepository, times(1)).save(wettkaempferJpa);
        verify(wettkaempferConverter, times(1)).convertFromWettkaempfer(wettkaempfer);
        verify(wettkaempferConverter, times(1)).convertToWettkaempfer(wettkaempferJpa);
    }

    @Test
    public void testSave_ExistingWettkaempfer() {
		when(vereinJpaRepository.findById(any())).thenReturn(Optional.of(vereinJpa));
        when(wettkaempferJpaRepository.findById(wkUUID)).thenReturn(Optional.of(wettkaempferJpa));
        when(wettkaempferJpaRepository.save(any(WettkaempferJpa.class))).thenReturn(wettkaempferJpa);
        when(wettkaempferConverter.convertFromWettkaempfer(wettkaempfer)).thenReturn(wettkaempferJpa);
        when(wettkaempferConverter.convertToWettkaempfer(wettkaempferJpa)).thenReturn(wettkaempfer);

        Wettkaempfer result = wettkaempferRepository.save(wettkaempfer);

        assertNotNull(result);
        assertEquals(wettkaempfer, result);
        verify(wettkaempferJpaRepository, times(1)).findById(wkUUID);
        verify(wettkaempferJpaRepository, times(1)).save(wettkaempferJpa);
        verify(wettkaempferConverter, times(1)).convertToWettkaempfer(wettkaempferJpa);
    }
}
