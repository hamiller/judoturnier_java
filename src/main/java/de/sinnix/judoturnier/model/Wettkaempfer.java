package de.sinnix.judoturnier.model;

import java.util.Optional;

public record Wettkaempfer(
	Integer id,
	String name,
	Geschlecht geschlecht,
	Altersklasse altersklasse,
	Verein verein,
	Double gewicht,
	Optional<Farbe> farbe,
	Boolean checked,
	Boolean printed) {
}
