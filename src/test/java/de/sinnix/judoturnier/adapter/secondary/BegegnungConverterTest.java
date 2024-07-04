package de.sinnix.judoturnier.adapter.secondary;

import de.sinnix.judoturnier.fixtures.WettkaempferFixtures;
import de.sinnix.judoturnier.model.Begegnung;
import de.sinnix.judoturnier.model.WettkampfGruppe;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

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
	void convertToBegegnung() {
		WettkampfGruppeJpa wettkampfGruppeJpa = new WettkampfGruppeJpa(1, "Gruppe1", "typ1");
		BegegnungJpa begegnungJpa = new BegegnungJpa();
		begegnungJpa.setId(1);
		begegnungJpa.setMatteId(2);
		begegnungJpa.setMattenRunde(123);
		begegnungJpa.setGruppenRunde(22);
		begegnungJpa.setWettkaempfer1(new WettkaempferJpa());
		begegnungJpa.setWettkaempfer2(new WettkaempferJpa());
		begegnungJpa.setGruppe(wettkampfGruppeJpa);

		when(wettkaempferConverter.convertToWettkaempfer(any(WettkaempferJpa.class))).thenReturn(WettkaempferFixtures.wettkaempfer1);
		when(wertungConverter.convertToWertung(any())).thenReturn(null);
		when(wettkampfGruppeConverter.convertToWettkampfGruppe(any())).thenReturn(new WettkampfGruppe(1, "name", "typ", List.of()));

		Begegnung result = begegnungConverter.convertToBegegnung(begegnungJpa);

		assertTrue(result != null);
		assertEquals(result.getBegegnungId(), begegnungJpa.getId());
		assertEquals(result.getMatteId(), begegnungJpa.getMatteId());
		assertEquals(result.getMattenRunde(), begegnungJpa.getMattenRunde());
		assertEquals(result.getGruppenRunde(), begegnungJpa.getGruppenRunde());
		assertTrue(result.getWettkampfGruppe() != null);
	}
}