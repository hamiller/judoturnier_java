package de.sinnix.judoturnier.model;

import java.util.Optional;
import java.util.UUID;

public record Wettkaempfer(
	Integer id,
	String name,
	Geschlecht geschlecht,
	Altersklasse altersklasse,
	Verein verein,
	Double gewicht,
	Optional<Farbe> farbe,  // TODO die Farbe sollte nicht am Wettkaempfer hinterlegt sein -> dann kann er bei jeder Begegnung eine neue Farbe erhalten
	Boolean checked,
	Boolean printed,
	UUID turnierUUID) {

	public static Wettkaempfer Freilos() {
		return new Wettkaempfer(-0, null, null, null, null, null, Optional.empty(), false, false, null);
	}
}
