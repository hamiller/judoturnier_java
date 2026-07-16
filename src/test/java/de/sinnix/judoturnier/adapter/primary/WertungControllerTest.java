package de.sinnix.judoturnier.adapter.primary;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WertungControllerTest {

	@Test
	void parseIntOrZeroDefaultsEmptyValuesToZero() {
		assertEquals(0, WertungController.parseIntOrZero(null));
		assertEquals(0, WertungController.parseIntOrZero(""));
		assertEquals(0, WertungController.parseIntOrZero("  "));
	}

	@Test
	void parseIntOrZeroKeepsSubmittedValues() {
		assertEquals(2, WertungController.parseIntOrZero("2"));
	}

	@Test
	void parseShidoLimitsValuesToThree() {
		assertEquals(0, WertungController.parseShido("-1"));
		assertEquals(2, WertungController.parseShido("2"));
		assertEquals(3, WertungController.parseShido("4"));
	}
}
