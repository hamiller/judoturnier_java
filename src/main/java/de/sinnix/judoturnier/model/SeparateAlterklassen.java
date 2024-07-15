package de.sinnix.judoturnier.model;

public enum SeparateAlterklassen {
	GETRENNT("Komplett getrennt"),
	GETRENNT_NACH_MATTEN("Getrennt nach Matten"),
	ZUSAMMEN("Nacheinander");

	public static final String TYP = "separatealtersklassen";

	private final String bezeichnung;

	SeparateAlterklassen(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}

	public String getBezeichnung() {
		return bezeichnung;
	}
}
