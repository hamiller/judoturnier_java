package de.sinnix.judoturnier.application;

import de.sinnix.judoturnier.model.WettkampfGruppe;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HelpersTest {

	private Helpers helpers = new Helpers();

	@Test
	public void testEmptySplitArray() {
		List<WettkampfGruppe> wettkampfGruppen = new ArrayList<>();
		List<List<WettkampfGruppe>> aufgeteilt = helpers.splitArray(wettkampfGruppen, 3);

		assertEquals(3, aufgeteilt.size());
	}

	@Test
	public void testGutesSplitArray() {
		List<String> strings = new ArrayList<>();
		strings.add("A");
		strings.add("B");
		strings.add("C");
		strings.add("D");
		strings.add("E");
		strings.add("F");
		strings.add("G");
		strings.add("H");

		List<List<String>> aufgeteilt = helpers.splitArray(strings, 2);
		assertEquals(2, aufgeteilt.size());
		assertEquals(4, aufgeteilt.get(0).size());
		assertEquals(4, aufgeteilt.get(1).size());
	}

	@Test
	public void testGutesUngeradesSplitArray() {
		List<String> strings = new ArrayList<>();
		strings.add("A");
		strings.add("B");
		strings.add("C");
		strings.add("D");
		strings.add("E");
		strings.add("F");
		strings.add("G");
		strings.add("H");

		List<List<String>> aufgeteilt = helpers.splitArray(strings, 3);
		assertEquals(3, aufgeteilt.size());
		assertEquals(3, aufgeteilt.get(0).size());
		assertEquals(3, aufgeteilt.get(1).size());
		assertEquals(2, aufgeteilt.get(2).size());
	}

	@Test
	public void testGutesSplitArrayAnders1() {
		List<Integer> ints = List.of(1,2,3,4,5,6,7);

		List<List<Integer>> aufgeteilt = helpers.splitArray(ints, 3);
		assertEquals(3, aufgeteilt.size());
		assertEquals(3, aufgeteilt.get(0).size());
		assertEquals(2, aufgeteilt.get(1).size());
		assertEquals(2, aufgeteilt.get(2).size());
	}

	@Test
	public void testGutesSplitArrayAnders2() {
		List<Integer> ints = List.of(1,2,3,4,5,6,7,8,9,10,11);

		List<List<Integer>> aufgeteilt = helpers.splitArray(ints, 3);
		assertEquals(3, aufgeteilt.size());
		assertEquals(4, aufgeteilt.get(0).size());
		assertEquals(4, aufgeteilt.get(1).size());
		assertEquals(3, aufgeteilt.get(2).size());
	}
}