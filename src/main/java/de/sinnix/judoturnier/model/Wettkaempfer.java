package de.sinnix.judoturnier.model;

import java.util.Optional;

public record Wettkaempfer(
	Integer id,
	String name,
	Geschlecht geschlecht,
	Altersklasse altersklasse,
	Verein verein,
	Double gewicht,
	Optional<Farbe> farbe,  // TODO die Farbe sollte nicht am Wettkaempfer hinterlegt sein -> dann kann er bei jeder Begegnung eine neue Farbe erhalten
	Boolean checked,
	Boolean printed) {
}
