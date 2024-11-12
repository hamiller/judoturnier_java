package de.sinnix.judoturnier.adapter.secondary;

import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Benutzer;
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

	private static final String          UUID_STRING = "550e8400-e29b-41d4-a716-446655440000";
	private              UUID            vereinId    = UUID.randomUUID();
	private              UUID            wkId        = UUID.randomUUID();
	private              WettkaempferJpa wk1Jpa      = new WettkaempferJpa(wkId.toString(), "Teilnehmer A", "m", "U11", new VereinJpa(vereinId.toString(), "Verein1", UUID_STRING), 25.0, "ORANGE", true, false, UUID_STRING);
	private              Wettkaempfer    wk1         = new Wettkaempfer(wkId, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(vereinId, "Verein1", UUID.fromString(UUID_STRING)), 25.0, Optional.of(Farbe.ORANGE), true, false, UUID.fromString(UUID_STRING));
	private              Benutzer        bewerter;

	@BeforeEach
	void setUp() {
		bewerter = new Benutzer(UUID.randomUUID().toString(), "user1", "Name, Vorname", List.of("ROLE_ZUSCHAUER"));
	}

	@Test
	void convertToWertungTurnier() {
		WertungJpa jpa = new WertungJpa();
		jpa.setUuid(UUID.randomUUID().toString());
		jpa.setSieger(wk1Jpa);
		jpa.setZeit(180_000_000_000l); // 3 Minuten
		jpa.setPunkteWettkaempfer1(1);
		jpa.setPunkteWettkaempfer2(0);
		jpa.setStrafenWettkaempfer1(0);
		jpa.setStrafenWettkaempfer2(1);

		when(wettkaempferConverter.convertToWettkaempfer(wk1Jpa)).thenReturn(wk1);

		Wertung wertung = wertungConverter.convertToWertung(jpa);

		assertEquals(wertung.getUuid().toString(), jpa.getUuid());
		assertEquals(wertung.getSieger(), wk1);
		assertEquals(wertung.getZeit(), Duration.of(3l, ChronoUnit.MINUTES));
		assertEquals(wertung.getPunkteWettkaempferWeiss(), jpa.getPunkteWettkaempfer1());
		assertEquals(wertung.getPunkteWettkaempferRot(), jpa.getPunkteWettkaempfer2());
		assertEquals(wertung.getStrafenWettkaempferWeiss(), jpa.getStrafenWettkaempfer1());
		assertEquals(wertung.getStrafenWettkaempferRot(), jpa.getStrafenWettkaempfer2());
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
			null, null, null, null, null, null, null, null,
			bewerter
		);


		when(wettkaempferConverter.convertFromWettkaempfer(wk1)).thenReturn(wk1Jpa);

		WertungJpa jpa = wertungConverter.convertFromWertung(wertung);

		assertEquals(jpa.getSieger(), wk1Jpa);
		assertEquals(jpa.getZeit(), 180_000_000_000l);
		assertEquals(jpa.getPunkteWettkaempfer1(), wertung.getPunkteWettkaempferWeiss());
		assertEquals(jpa.getPunkteWettkaempfer2(), wertung.getPunkteWettkaempferRot());
		assertEquals(jpa.getStrafenWettkaempfer1(), wertung.getStrafenWettkaempferWeiss());
		assertEquals(jpa.getStrafenWettkaempfer2(), wertung.getStrafenWettkaempferRot());
	}

	@Test
	void convertToWertungRandori() {
		WertungJpa jpa = new WertungJpa();
		jpa.setUuid(UUID.randomUUID().toString());
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

		assertEquals(wertung.getUuid().toString(), jpa.getUuid());
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