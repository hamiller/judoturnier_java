package de.sinnix.judoturnier.adapter.secondary;

import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Farbe;
import de.sinnix.judoturnier.model.Geschlecht;
import de.sinnix.judoturnier.model.Verein;
import de.sinnix.judoturnier.model.Wertung;
import de.sinnix.judoturnier.model.Wettkaempfer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WertungConverterTest {

	@Mock
	private WettkaempferConverter wettkaempferConverter;
	@InjectMocks
	private WertungConverter wertungConverter;

	private WettkaempferJpa wk1Jpa = new WettkaempferJpa(1, "Teilnehmer A", "m", "U11", new VereinJpa(1, "Verein1"), 25.0, "ORANGE", true, false);
	private Wettkaempfer    wk1    = new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 25.0, Optional.of(Farbe.ORANGE), true, false);

	@BeforeEach
	void setUp() {
	}

	@Test
	void convertToWertungTurnier() {
		WertungJpa jpa = new WertungJpa();
		jpa.setUuid(UUID.randomUUID());
		jpa.setSieger(wk1Jpa);
		jpa.setZeit(180_000_000_000l); // 3 Minuten
		jpa.setPunkteWettkaempfer1(1);
		jpa.setPunkteWettkaempfer2(0);
		jpa.setStrafenWettkaempfer1(0);
		jpa.setStrafenWettkaempfer2(1);

		when(wettkaempferConverter.convertToWettkaempfer(wk1Jpa)).thenReturn(wk1);

		Wertung wertung =  wertungConverter.convertToWertung(jpa);

		assertEquals(wertung.uuid(), jpa.getUuid().toString());
		assertEquals(wertung.sieger(), wk1);
		assertEquals(wertung.zeit(), Duration.of(3l, ChronoUnit.MINUTES));
		assertEquals(wertung.punkteWettkaempferWeiss(), jpa.getPunkteWettkaempfer1());
		assertEquals(wertung.punkteWettkaempferRot(), jpa.getPunkteWettkaempfer2());
		assertEquals(wertung.strafenWettkaempferWeiss(), jpa.getStrafenWettkaempfer1());
		assertEquals(wertung.strafenWettkaempferRot(), jpa.getStrafenWettkaempfer2());
	}

	@Test
	void convertFromWertungTurnier() {
		Wertung wertung = new Wertung(
			null,
			wk1,
			Duration.of(3l, ChronoUnit.MINUTES),
			1,
			0,
			0,
			1,
			null, null, null, null, null, null, null, null);


		when(wettkaempferConverter.convertFromWettkaempfer(wk1)).thenReturn(wk1Jpa);

		WertungJpa jpa = wertungConverter.convertFromWertung(wertung);

		assertTrue(jpa.getUuid() != null);
		assertEquals(jpa.getSieger(), wk1Jpa);
		assertEquals(jpa.getZeit(), 180_000_000_000l);
		assertEquals(jpa.getPunkteWettkaempfer1(), wertung.punkteWettkaempferWeiss());
		assertEquals(jpa.getPunkteWettkaempfer2(), wertung.punkteWettkaempferRot());
		assertEquals(jpa.getStrafenWettkaempfer1(), wertung.strafenWettkaempferWeiss());
		assertEquals(jpa.getStrafenWettkaempfer2(), wertung.strafenWettkaempferRot());
	}

	@Test
	void convertToWertungRandori() {
		WertungJpa jpa = new WertungJpa();
		jpa.setUuid(UUID.randomUUID());
		jpa.setZeit(180_000_000_000l); // 3 Minuten
		jpa.setKampfgeistWettkaempfer1(1);
		jpa.setTechnikWettkaempfer1(2);
		jpa.setKampfstilWettkaempfer1(3);
		jpa.setFairnessWettkaempfer1(4);
		jpa.setKampfgeistWettkaempfer2(5);
		jpa.setTechnikWettkaempfer2(6);
		jpa.setKampfstilWettkaempfer2(1);
		jpa.setFairnessWettkaempfer2(2);

		Wertung wertung =  wertungConverter.convertToWertung(jpa);

		assertEquals(wertung.uuid(), jpa.getUuid().toString());
		assertEquals(wertung.zeit(), Duration.of(3l, ChronoUnit.MINUTES));
		assertEquals(wertung.kampfgeistWettkaempfer1(), jpa.getKampfgeistWettkaempfer1());
		assertEquals(wertung.technikWettkaempfer1(), jpa.getTechnikWettkaempfer1());
		assertEquals(wertung.kampfstilWettkaempfer1(), jpa.getKampfstilWettkaempfer1());
		assertEquals(wertung.fairnessWettkaempfer1(), jpa.getFairnessWettkaempfer1());
		assertEquals(wertung.kampfgeistWettkaempfer2(), jpa.getKampfgeistWettkaempfer2());
		assertEquals(wertung.technikWettkaempfer2(), jpa.getTechnikWettkaempfer2());
		assertEquals(wertung.kampfstilWettkaempfer2(), jpa.getKampfstilWettkaempfer2());
		assertEquals(wertung.fairnessWettkaempfer2(), jpa.getFairnessWettkaempfer2());
	}

	@Test
	void convertFromWertungRandori() {
		Wertung wertung = new Wertung(
			null,
			null,
			Duration.of(3l, ChronoUnit.MINUTES),
			null,null, null,null,
			1, 2, 3, 4, 5, 6, 1, 2);

		WertungJpa jpa = wertungConverter.convertFromWertung(wertung);

		assertTrue(jpa.getUuid() != null);
		assertEquals(jpa.getSieger(), null);
		assertEquals(jpa.getZeit(), 180_000_000_000l);
		assertEquals(jpa.getKampfgeistWettkaempfer1(), wertung.kampfgeistWettkaempfer1());
		assertEquals(jpa.getTechnikWettkaempfer1(), wertung.technikWettkaempfer1());
		assertEquals(jpa.getKampfstilWettkaempfer1(), wertung.kampfstilWettkaempfer1());
		assertEquals(jpa.getFairnessWettkaempfer1(), wertung.fairnessWettkaempfer1());
		assertEquals(jpa.getKampfgeistWettkaempfer2(), wertung.kampfgeistWettkaempfer2());
		assertEquals(jpa.getTechnikWettkaempfer2(), wertung.technikWettkaempfer2());
		assertEquals(jpa.getKampfstilWettkaempfer2(), wertung.kampfstilWettkaempfer2());
		assertEquals(jpa.getFairnessWettkaempfer2(), wertung.fairnessWettkaempfer2());
	}
}