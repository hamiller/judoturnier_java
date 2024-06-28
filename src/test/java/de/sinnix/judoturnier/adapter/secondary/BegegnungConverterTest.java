package de.sinnix.judoturnier.adapter.secondary;

import de.sinnix.judoturnier.fixtures.WettkaempferFixtures;
import de.sinnix.judoturnier.model.Begegnung;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

	@InjectMocks
	private BegegnungConverter begegnungConverter;

	@Test
	void convertToBegegnung() {
		BegegnungJpa begegnungJpa = new BegegnungJpa();
		begegnungJpa.setId(1);
		begegnungJpa.setMatteId(2);
		begegnungJpa.setMattenRunde(123);
		begegnungJpa.setGruppenRunde(22);
		begegnungJpa.setWettkaempfer1(new WettkaempferJpa());
		begegnungJpa.setWettkaempfer2(new WettkaempferJpa());

		when(wettkaempferConverter.convertToWettkaempfer(any(WettkaempferJpa.class))).thenReturn(WettkaempferFixtures.wettkaempfer1);
		when(wertungConverter.convertToWertung(any())).thenReturn(null);

		Begegnung result = begegnungConverter.convertToBegegnung(begegnungJpa);

		assertTrue(result != null);
		assertEquals(result.getBegegnungId(), begegnungJpa.getId());
		assertEquals(result.getMatteId(), begegnungJpa.getMatteId());
		assertEquals(result.getMattenRunde(), begegnungJpa.getMattenRunde());
		assertEquals(result.getGruppenRunde(), begegnungJpa.getGruppenRunde());
	}
}