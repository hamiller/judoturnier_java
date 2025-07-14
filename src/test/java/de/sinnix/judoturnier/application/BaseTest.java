package de.sinnix.judoturnier.application;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BaseTest {

	@Test
	public void testPlusPlus1() {
		int i = 0;
		assertEquals(0, i++);
		assertEquals(1, i);
	}

	@Test
	public void testPlusPlus2() {
		int i = 0;
		assertEquals(1, ++i);
		assertEquals(1, i);
	}
}
