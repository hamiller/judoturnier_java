package de.sinnix.judoturnier.model;

import java.util.Map;

// Die Kampfzeiten sind als Sekunden hinterlegt
public record Wettkampfzeiten(Map<Altersklasse, Integer> altersklasseKampfzeitSekunden) {
	public static final String  TYP               = "wettkampfzeit";
}
