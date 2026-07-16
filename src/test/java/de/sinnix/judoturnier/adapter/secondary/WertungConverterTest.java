package de.sinnix.judoturnier.adapter.secondary;

import de.sinnix.judoturnier.adapter.secondary.converter.BenutzerConverter;
import de.sinnix.judoturnier.adapter.secondary.converter.WertungConverter;
import de.sinnix.judoturnier.adapter.secondary.converter.WettkaempferConverter;
import de.sinnix.judoturnier.adapter.secondary.jpa.VereinJpa;
import de.sinnix.judoturnier.adapter.secondary.jpa.WertungJpa;
import de.sinnix.judoturnier.adapter.secondary.jpa.WettkaempferJpa;
import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Benutzer;
import de.sinnix.judoturnier.model.BenutzerRolle;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WertungConverterTest {

	@Mock
	private WettkaempferConverter wettkaempferConverter;
	@Mock
	private BenutzerConverter     benutzerConverter;

	@InjectMocks
	private WertungConverter wertungConverter;

	private static final UUID            UUID_TURNIER = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
	private              UUID            vereinId     = UUID.randomUUID();
	private UUID            wkId   = UUID.randomUUID();
	private WettkaempferJpa wk1Jpa = new WettkaempferJpa("Teilnehmer A", "m", "U11", new VereinJpa("Verein1", UUID_TURNIER), 25.0, "ORANGE", true, false, UUID_TURNIER);
	private Wettkaempfer    wk1    = new Wettkaempfer(wkId, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(vereinId, "Verein1", UUID_TURNIER), 25.0, Optional.of(Farbe.ORANGE), true, false, UUID_TURNIER);
	private              Benutzer        bewerter;

	@BeforeEach
	void setUp() {
		bewerter = new Benutzer(UUID.randomUUID(), "user1", "Name, Vorname", List.of(), List.of(BenutzerRolle.KAMPFRICHTER));
	}

	@Test
	void convertToWertungTurnier() {
		WertungJpa jpa = new WertungJpa();
		jpa.setId(UUID.randomUUID());
		jpa.setSieger(wk1Jpa);
		jpa.setZeit(180_000_000_000l); // 3 Minuten
		jpa.setIpponWettkaempfer1(1);
		jpa.setWazariWettkaempfer1(0);
		jpa.setYukoWettkaempfer1(2);
		jpa.setShidoWettkaempfer1(0);
		jpa.setHansokuMakeWettkaempfer1(false);
		jpa.setIpponWettkaempfer2(0);
		jpa.setWazariWettkaempfer2(1);
		jpa.setYukoWettkaempfer2(0);
		jpa.setShidoWettkaempfer2(1);
		jpa.setHansokuMakeWettkaempfer2(true);

		when(wettkaempferConverter.convertToWettkaempfer(wk1Jpa)).thenReturn(wk1);

		Wertung wertung = wertungConverter.convertToWertung(jpa);

		assertEquals(wertung.getUuid(), jpa.getId());
		assertEquals(wertung.getSieger(), wk1);
		assertEquals(wertung.getZeit(), Duration.of(3l, ChronoUnit.MINUTES));
		assertEquals(wertung.getIpponWettkaempferWeiss(), jpa.getIpponWettkaempfer1());
		assertEquals(wertung.getWazariWettkaempferWeiss(), jpa.getWazariWettkaempfer1());
		assertEquals(wertung.getYukoWettkaempferWeiss(), jpa.getYukoWettkaempfer1());
		assertEquals(wertung.getShidoWettkaempferWeiss(), jpa.getShidoWettkaempfer1());
		assertEquals(wertung.getHansokuMakeWettkaempferWeiss(), jpa.getHansokuMakeWettkaempfer1());
		assertEquals(wertung.getIpponWettkaempferRot(), jpa.getIpponWettkaempfer2());
		assertEquals(wertung.getWazariWettkaempferRot(), jpa.getWazariWettkaempfer2());
		assertEquals(wertung.getYukoWettkaempferRot(), jpa.getYukoWettkaempfer2());
		assertEquals(wertung.getShidoWettkaempferRot(), jpa.getShidoWettkaempfer2());
		assertEquals(wertung.getHansokuMakeWettkaempferRot(), jpa.getHansokuMakeWettkaempfer2());
	}

	@Test
	void convertFromWertungTurnier() {
		Wertung wertung = new Wertung(
			null,
			wk1,
			Duration.of(3l, ChronoUnit.MINUTES),
			1,
			0,
			2,
			0,
			false,
			0,
			1,
			0,
			1,
			true,
			null, null, null, null, null, null, null, null,
			bewerter
		);


		when(wettkaempferConverter.convertFromWettkaempfer(wk1)).thenReturn(wk1Jpa);

		WertungJpa jpa = wertungConverter.convertFromWertung(wertung);

		assertEquals(jpa.getSieger(), wk1Jpa);
		assertEquals(jpa.getZeit(), 180_000_000_000l);
		assertEquals(jpa.getIpponWettkaempfer1(), wertung.getIpponWettkaempferWeiss());
		assertEquals(jpa.getWazariWettkaempfer1(), wertung.getWazariWettkaempferWeiss());
		assertEquals(jpa.getYukoWettkaempfer1(), wertung.getYukoWettkaempferWeiss());
		assertEquals(jpa.getShidoWettkaempfer1(), wertung.getShidoWettkaempferWeiss());
		assertEquals(jpa.getHansokuMakeWettkaempfer1(), wertung.getHansokuMakeWettkaempferWeiss());
		assertEquals(jpa.getIpponWettkaempfer2(), wertung.getIpponWettkaempferRot());
		assertEquals(jpa.getWazariWettkaempfer2(), wertung.getWazariWettkaempferRot());
		assertEquals(jpa.getYukoWettkaempfer2(), wertung.getYukoWettkaempferRot());
		assertEquals(jpa.getShidoWettkaempfer2(), wertung.getShidoWettkaempferRot());
		assertEquals(jpa.getHansokuMakeWettkaempfer2(), wertung.getHansokuMakeWettkaempferRot());
	}

	@Test
	void convertToWertungRandori() {
		WertungJpa jpa = new WertungJpa();
		jpa.setId(UUID.randomUUID());
		jpa.setZeit(180_000_000_000l); // 3 Minuten
		jpa.setKampfgeistWettkaempfer1(1);
		jpa.setTechnikWettkaempfer1(2);
		jpa.setKampfstilWettkaempfer1(3);
		jpa.setVielfaltWettkaempfer1(4);
		jpa.setKampfgeistWettkaempfer2(5);
		jpa.setTechnikWettkaempfer2(6);
		jpa.setKampfstilWettkaempfer2(1);
		jpa.setVielfaltWettkaempfer2(2);

		Wertung wertung = wertungConverter.convertToWertung(jpa);

		assertEquals(wertung.getUuid(), jpa.getId());
		assertEquals(wertung.getZeit(), Duration.of(3l, ChronoUnit.MINUTES));
		assertEquals(wertung.getKampfgeistWettkaempfer1(), jpa.getKampfgeistWettkaempfer1());
		assertEquals(wertung.getTechnikWettkaempfer1(), jpa.getTechnikWettkaempfer1());
		assertEquals(wertung.getKampfstilWettkaempfer1(), jpa.getKampfstilWettkaempfer1());
		assertEquals(wertung.getVielfaltWettkaempfer1(), jpa.getVielfaltWettkaempfer1());
		assertEquals(wertung.getKampfgeistWettkaempfer2(), jpa.getKampfgeistWettkaempfer2());
		assertEquals(wertung.getTechnikWettkaempfer2(), jpa.getTechnikWettkaempfer2());
		assertEquals(wertung.getKampfstilWettkaempfer2(), jpa.getKampfstilWettkaempfer2());
		assertEquals(wertung.getVielfaltWettkaempfer2(), jpa.getVielfaltWettkaempfer2());
	}

	@Test
	void convertFromWertungRandori() {
		Wertung wertung = new Wertung(
			null,
			null,
			Duration.of(3l, ChronoUnit.MINUTES),
			null, null, null, null,
			1, 2, 3, 4, 5, 6, 1, 2,
			bewerter);

		WertungJpa jpa = wertungConverter.convertFromWertung(wertung);

		assertEquals(jpa.getSieger(), null);
		assertEquals(jpa.getZeit(), 180_000_000_000l);
		assertEquals(jpa.getKampfgeistWettkaempfer1(), wertung.getKampfgeistWettkaempfer1());
		assertEquals(jpa.getTechnikWettkaempfer1(), wertung.getTechnikWettkaempfer1());
		assertEquals(jpa.getKampfstilWettkaempfer1(), wertung.getKampfstilWettkaempfer1());
		assertEquals(jpa.getVielfaltWettkaempfer1(), wertung.getVielfaltWettkaempfer1());
		assertEquals(jpa.getKampfgeistWettkaempfer2(), wertung.getKampfgeistWettkaempfer2());
		assertEquals(jpa.getTechnikWettkaempfer2(), wertung.getTechnikWettkaempfer2());
		assertEquals(jpa.getKampfstilWettkaempfer2(), wertung.getKampfstilWettkaempfer2());
		assertEquals(jpa.getVielfaltWettkaempfer2(), wertung.getVielfaltWettkaempfer2());
	}
}
