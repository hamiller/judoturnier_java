package de.sinnix.judoturnier.adapter.secondary;

import de.sinnix.judoturnier.fixtures.WettkaempferFixtures;
import de.sinnix.judoturnier.model.Begegnung;
import de.sinnix.judoturnier.model.Wertung;
import de.sinnix.judoturnier.model.Wettkaempfer;
import de.sinnix.judoturnier.model.WettkampfGruppe;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
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

	@Test
	void testConvertToBegegnung() {
		UUID turnierUUID = UUID.randomUUID();
		WettkampfGruppeJpa wettkampfGruppeJpa = new WettkampfGruppeJpa(1, "Gruppe1", "typ1", turnierUUID.toString());
		BegegnungJpa begegnungJpa = new BegegnungJpa();
		begegnungJpa.setId(1);
		begegnungJpa.setMatteId(2);
		begegnungJpa.setMattenRunde(123);
		begegnungJpa.setGruppenRunde(22);
		begegnungJpa.setWettkaempfer1(new WettkaempferJpa());
		begegnungJpa.setWettkaempfer2(new WettkaempferJpa());
		begegnungJpa.setWettkampfGruppeId(wettkampfGruppeJpa.getId());
		begegnungJpa.setTurnierUUID(turnierUUID.toString());

		when(wettkaempferConverter.convertToWettkaempfer(any(WettkaempferJpa.class))).thenReturn(WettkaempferFixtures.wettkaempfer1);
		when(wertungConverter.convertToWertung(any())).thenReturn(null);
		when(wettkampfGruppeConverter.convertToWettkampfGruppe(any(), any())).thenReturn(new WettkampfGruppe(1, "name", "typ", List.of(), turnierUUID));

		Begegnung result = begegnungConverter.convertToBegegnung(begegnungJpa, List.of(wettkampfGruppeJpa));

		assertTrue(result != null);
		assertEquals(result.getBegegnungId(), begegnungJpa.getId());
		assertEquals(result.getMatteId(), begegnungJpa.getMatteId());
		assertEquals(result.getMattenRunde(), begegnungJpa.getMattenRunde());
		assertEquals(result.getGruppenRunde(), begegnungJpa.getGruppenRunde());
		assertTrue(result.getWettkampfGruppe() != null);
	}

	@Test
	public void testConvertFromBegegnung() {
		UUID turnierUUID = UUID.randomUUID();
		Wertung wertung = new Wertung(
			UUID.randomUUID(),
			WettkaempferFixtures.wettkaempfer1,
			Duration.of(3l, ChronoUnit.MINUTES),
			1,
			0,
			0,
			1,
			null, null, null, null, null, null, null, null);

		Begegnung begegnung = new Begegnung(1, 2, 123, 22, WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2, Optional.of(wertung), new WettkampfGruppe(1, "Gruppe1", "typ1", List.of(), turnierUUID), turnierUUID);


		when(wettkaempferConverter.convertFromWettkaempfer(WettkaempferFixtures.wettkaempfer1)).thenReturn(WettkaempferFixtures.wettkaempferJpa1);
		when(wettkaempferConverter.convertFromWettkaempfer(WettkaempferFixtures.wettkaempfer2)).thenReturn(WettkaempferFixtures.wettkaempferJpa2);

		BegegnungJpa result = begegnungConverter.convertFromBegegnung(begegnung);

		WettkampfGruppeJpa wettkampfGruppeJpa = new WettkampfGruppeJpa(1, "Gruppe1", "typ1", turnierUUID.toString());
		BegegnungJpa begegnungJpa = new BegegnungJpa();
		begegnungJpa.setId(1);
		begegnungJpa.setMatteId(2);
		begegnungJpa.setMattenRunde(123);
		begegnungJpa.setGruppenRunde(22);
		begegnungJpa.setWettkaempfer1(WettkaempferFixtures.wettkaempferJpa1);
		begegnungJpa.setWettkaempfer2(WettkaempferFixtures.wettkaempferJpa2);
		begegnungJpa.setWettkampfGruppeId(wettkampfGruppeJpa.getId());
		begegnungJpa.setTurnierUUID(turnierUUID.toString());
		assertEquals(begegnungJpa, result);
	}
}