package de.sinnix.judoturnier.model;

import java.util.Map;

public record Gruppengroessen(Map<Altersklasse, Integer> altersklasseGruppengroesse) {
	public static final String  TYP               = "gruppengroesse";
}
