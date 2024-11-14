package de.sinnix.judoturnier.adapter.secondary;

import de.sinnix.judoturnier.fixtures.WettkaempferFixtures;
import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Begegnung;
import de.sinnix.judoturnier.model.Benutzer;
import de.sinnix.judoturnier.model.BenutzerRolle;
import de.sinnix.judoturnier.model.Wertung;
import de.sinnix.judoturnier.model.WettkampfGruppe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BegegnungConverterTest {

	@Mock
	private WettkaempferConverter wettkaempferConverter;
	@Mock
	private WertungConverter      wertungConverter;
	@Mock
	private WettkampfGruppeConverter wettkampfGruppeConverter;

	@InjectMocks
	private BegegnungConverter begegnungConverter;

	private Benutzer bewerter;

	@BeforeEach
	void setUp() {
		bewerter = new Benutzer(UUID.randomUUID(), "user1", "Name, Vorname", List.of(), List.of(BenutzerRolle.BEOBACHTER));
	}

	@Test
	void testConvertToBegegnung() {
		UUID turnierUUID = UUID.randomUUID();
		UUID rundeUUID = UUID.randomUUID();
		UUID begegnungUUID = UUID.randomUUID();
		UUID wkgUUID = UUID.randomUUID();

		Begegnung.BegegnungId begegnungId = new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1);
		WettkampfGruppeJpa wettkampfGruppeJpa = new WettkampfGruppeJpa(wkgUUID.toString(), "Gruppe1", "typ1", "U11", turnierUUID.toString());
		BegegnungJpa begegnungJpa = new BegegnungJpa();
		begegnungJpa.setUuid(begegnungUUID.toString());
		begegnungJpa.setRundeUUID(rundeUUID.toString());
		begegnungJpa.setMatteId(2);
		begegnungJpa.setMattenRunde(123);
		begegnungJpa.setGruppenRunde(22);
		begegnungJpa.setWettkaempfer1(new WettkaempferJpa());
		begegnungJpa.setWettkaempfer2(new WettkaempferJpa());
		begegnungJpa.setWettkampfGruppeId(wettkampfGruppeJpa.getUuid());
		begegnungJpa.setTurnierUUID(turnierUUID.toString());
		begegnungJpa.setWertungen(List.of());
		begegnungJpa.setRundenTyp(begegnungId.getRundenTyp().getValue());
		begegnungJpa.setRunde(begegnungId.getRunde());
		begegnungJpa.setPaarung(begegnungId.getAkuellePaarung());

		when(wettkaempferConverter.convertToWettkaempfer(any(WettkaempferJpa.class))).thenReturn(WettkaempferFixtures.wettkaempfer1.get());
		when(wettkampfGruppeConverter.convertToWettkampfGruppe(any())).thenReturn(new WettkampfGruppe(wkgUUID, "name", "typ", Altersklasse.U11, List.of(), turnierUUID));

		Begegnung result = begegnungConverter.convertToBegegnung(begegnungJpa, List.of(wettkampfGruppeJpa));

		assertTrue(result != null);
		assertEquals(begegnungJpa.getUuid(), result.getId().toString());
		assertNotNull(result.getBegegnungId());
		assertEquals(begegnungJpa.getRundenTyp(), result.getBegegnungId().getRundenTyp().getValue());
		assertEquals(begegnungJpa.getRunde(), result.getBegegnungId().getRunde());
		assertEquals(begegnungJpa.getPaarung(), result.getBegegnungId().getAkuellePaarung());
		assertEquals(begegnungJpa.getRundeUUID(), result.getRundeId().toString());
		assertEquals(begegnungJpa.getMatteId(), result.getMatteId());
		assertEquals(begegnungJpa.getMattenRunde(), result.getMattenRunde());
		assertEquals(begegnungJpa.getGruppenRunde(), result.getGruppenRunde());
		assertTrue(result.getWettkampfGruppe() != null);
		assertEquals(begegnungId, result.getBegegnungId());
	}

	@Test
	public void testConvertFromBegegnung() {
		UUID turnierUUID = UUID.randomUUID();
		UUID rundeUUID = UUID.randomUUID();
		UUID begegnungUUID = UUID.randomUUID();
		UUID wkgUUID = UUID.randomUUID();
		Wertung wertung = new Wertung(
			UUID.randomUUID(),
			WettkaempferFixtures.wettkaempfer1.get(),
			Duration.of(3l, ChronoUnit.MINUTES),
			1,
			0,
			0,
			1,
			null, null, null, null, null, null, null, null,
			bewerter);

		Begegnung.BegegnungId begegnungId = new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1);
		Begegnung begegnung = new Begegnung(begegnungUUID, begegnungId, rundeUUID,2, 123, 22, 13, WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2, List.of(wertung), new WettkampfGruppe(wkgUUID, "Gruppe1", "typ1", Altersklasse.U11, List.of(), turnierUUID), turnierUUID);

		WertungJpa wertungJpa = new WertungJpa();
		wertungJpa.setUuid(wertung.getUuid().toString());
		wertungJpa.setZeit(wertung.getZeit().getSeconds());
		wertungJpa.setSieger(WettkaempferFixtures.wettkaempferJpa1);
		wertungJpa.setPunkteWettkaempfer1(1);
		wertungJpa.setStrafenWettkaempfer1(0);
		wertungJpa.setPunkteWettkaempfer2(0);
		wertungJpa.setStrafenWettkaempfer2(1);

		when(wettkaempferConverter.convertFromWettkaempfer(WettkaempferFixtures.wettkaempfer1.get())).thenReturn(WettkaempferFixtures.wettkaempferJpa1);
		when(wettkaempferConverter.convertFromWettkaempfer(WettkaempferFixtures.wettkaempfer2.get())).thenReturn(WettkaempferFixtures.wettkaempferJpa2);
		when(wertungConverter.convertFromWertung(wertung)).thenReturn(wertungJpa);

		BegegnungJpa result = begegnungConverter.convertFromBegegnung(begegnung);

		WettkampfGruppeJpa wettkampfGruppeJpa = new WettkampfGruppeJpa(wkgUUID.toString(), "Gruppe1", "typ1", "U11", turnierUUID.toString());
		BegegnungJpa begegnungJpa = new BegegnungJpa();
		begegnungJpa.setRundeUUID(rundeUUID.toString());
		begegnungJpa.setUuid(begegnungUUID.toString());
		begegnungJpa.setMatteId(2);
		begegnungJpa.setMattenRunde(123);
		begegnungJpa.setGruppenRunde(22);
		begegnungJpa.setGesamtRunde(13);
		begegnungJpa.setWettkaempfer1(WettkaempferFixtures.wettkaempferJpa1);
		begegnungJpa.setWettkaempfer2(WettkaempferFixtures.wettkaempferJpa2);
		begegnungJpa.setWettkampfGruppeId(wettkampfGruppeJpa.getUuid());
		begegnungJpa.setTurnierUUID(turnierUUID.toString());
		begegnungJpa.setWertungen(List.of(wertungJpa));
		begegnungJpa.setRunde(begegnungId.getRunde());
		begegnungJpa.setPaarung(begegnungId.getAkuellePaarung());
		begegnungJpa.setRundenTyp(begegnungId.getRundenTyp().getValue());

		assertEquals(begegnungJpa, result);
	}
}