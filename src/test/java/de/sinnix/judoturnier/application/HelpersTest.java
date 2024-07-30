package de.sinnix.judoturnier.application;

import de.sinnix.judoturnier.model.WettkampfGruppe;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HelpersTest {

	private Helpers helpers = new Helpers();

	@Test
	public void testEmptySplitArrayToParts() {
		List<WettkampfGruppe> wettkampfGruppen = new ArrayList<>();
		List<List<WettkampfGruppe>> aufgeteilt = helpers.splitArrayToParts(wettkampfGruppen, 3);

		assertEquals(3, aufgeteilt.size());
	}

	@Test
	public void testGutesSplitArrayToParts() {
		List<String> strings = new ArrayList<>();
		strings.add("A");
		strings.add("B");
		strings.add("C");
		strings.add("D");
		strings.add("E");
		strings.add("F");
		strings.add("G");
		strings.add("H");

		List<List<String>> aufgeteilt = helpers.splitArrayToParts(strings, 2);
		assertEquals(2, aufgeteilt.size());
		assertEquals(4, aufgeteilt.get(0).size());
		assertEquals(4, aufgeteilt.get(1).size());
	}

	@Test
	public void testGutesUngeradesSplitArrayToParts() {
		List<String> strings = new ArrayList<>();
		strings.add("A");
		strings.add("B");
		strings.add("C");
		strings.add("D");
		strings.add("E");
		strings.add("F");
		strings.add("G");
		strings.add("H");

		List<List<String>> aufgeteilt = helpers.splitArrayToParts(strings, 3);
		assertEquals(3, aufgeteilt.size());
		assertEquals(3, aufgeteilt.get(0).size());
		assertEquals(3, aufgeteilt.get(1).size());
		assertEquals(2, aufgeteilt.get(2).size());
	}

	@Test
	public void testGutesSplitArrayToPartsAnders1() {
		List<Integer> ints = List.of(1,2,3,4,5,6,7);

		List<List<Integer>> aufgeteilt = helpers.splitArrayToParts(ints, 3);
		assertEquals(3, aufgeteilt.size());
		assertEquals(3, aufgeteilt.get(0).size());
		assertEquals(2, aufgeteilt.get(1).size());
		assertEquals(2, aufgeteilt.get(2).size());
	}

	@Test
	public void testGutesSplitArrayToPartsAnders2() {
		List<Integer> ints = List.of(1,2,3,4,5,6,7,8,9,10,11);

		List<List<Integer>> aufgeteilt = helpers.splitArrayToParts(ints, 3);
		assertEquals(3, aufgeteilt.size());
		assertEquals(4, aufgeteilt.get(0).size());
		assertEquals(4, aufgeteilt.get(1).size());
		assertEquals(3, aufgeteilt.get(2).size());
	}
}