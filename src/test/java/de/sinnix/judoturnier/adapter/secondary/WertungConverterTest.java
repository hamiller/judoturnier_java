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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WertungConverterTest {

	@Mock
	private WettkaempferConverter wettkaempferConverter;
	@InjectMocks
	private WertungConverter wertungConverter;

	private WettkaempferJpa wk1Jpa = new WettkaempferJpa(1, "Teilnehmer A", "m", "U11", new VereinJpa(1, "Verein1"), 25.0, "ORANGE", true, false);
	private WettkaempferJpa wk2Jpa = new WettkaempferJpa(2, "Teilnehmer B", "w", "U11", new VereinJpa(2, "Verein2"), 26.0, "BLAU", false, true);
	private Wettkaempfer    wk1    = new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 25.0, Optional.of(Farbe.ORANGE), true, false);
	private Wettkaempfer    wk2    = new Wettkaempfer(2, "Teilnehmer B", Geschlecht.w, Altersklasse.U11, new Verein(2, "Verein2"), 26.0, Optional.of(Farbe.BLAU), false, true);

	@BeforeEach
	void setUp() {
	}

	@Test
	void convertToWertungTurnier() {
		WertungJpa jpa = new WertungJpa();
		jpa.setId(1);
		jpa.setWettkaempfer1(wk1Jpa);
		jpa.setWettkaempfer2(wk2Jpa);
		jpa.setSieger(wk1Jpa);
		jpa.setZeit(180_000_000_000l); // 3 Minuten
		jpa.setPunkteWettkaempfer1(1);
		jpa.setPunkteWettkaempfer2(0);
		jpa.setStrafenWettkaempfer1(0);
		jpa.setStrafenWettkaempfer2(1);

		when(wettkaempferConverter.convertToWettkaempfer(wk1Jpa)).thenReturn(wk1);
		when(wettkaempferConverter.convertToWettkaempfer(wk2Jpa)).thenReturn(wk2);

		Wertung wertung =  wertungConverter.convertToWertung(jpa);

		assertEquals(wertung.id(), jpa.getId());
		assertEquals(wertung.sieger(), wk1);
		assertEquals(wertung.wettkaempfer1(), wk1);
		assertEquals(wertung.wettkaempfer2(), wk2);
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
			wk2,
			wk1,
			Duration.of(3l, ChronoUnit.MINUTES),
			1,
			0,
			0,
			1,
			null, null, null, null, null, null, null, null);


		when(wettkaempferConverter.convertFromWettkaempfer(wk1)).thenReturn(wk1Jpa);
		when(wettkaempferConverter.convertFromWettkaempfer(wk2)).thenReturn(wk2Jpa);

		WertungJpa jpa = wertungConverter.convertFromWertung(wertung);

		assertEquals(jpa.getId(), null);
		assertEquals(jpa.getSieger(), wk1Jpa);
		assertEquals(jpa.getWettkaempfer1(), wk1Jpa);
		assertEquals(jpa.getWettkaempfer2(), wk2Jpa);
		assertEquals(jpa.getZeit(), 180_000_000_000l);
		assertEquals(jpa.getPunkteWettkaempfer1(), wertung.punkteWettkaempferWeiss());
		assertEquals(jpa.getPunkteWettkaempfer2(), wertung.punkteWettkaempferRot());
		assertEquals(jpa.getStrafenWettkaempfer1(), wertung.strafenWettkaempferWeiss());
		assertEquals(jpa.getStrafenWettkaempfer2(), wertung.strafenWettkaempferRot());
	}
}