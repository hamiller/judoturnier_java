package de.sinnix.judoturnier.model;

public enum Altersklasse {

	U9("U9", 1),
	U11("U11", 2),
	U12("U12", 3),
	U13("U13", 4),
	U15("U15", 5),
	U18("U18", 6),
	U21("U21", 7),
	Maenner("Männer", 8),
	Frauen("Frauen", 9);

	private final String bezeichnung;
	private final int    reihenfolge;

	Altersklasse(String bezeichnung, int reihenfolge) {
		this.bezeichnung = bezeichnung;
		this.reihenfolge = reihenfolge;
	}

	public String getBezeichnung() {
		return bezeichnung;
	}

	public int getReihenfolge() {
		return reihenfolge;
	}
}
