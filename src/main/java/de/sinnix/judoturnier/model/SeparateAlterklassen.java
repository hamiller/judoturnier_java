package de.sinnix.judoturnier.model;

public enum SeparateAlterklassen {
	GETRENNT("Komplett getrennt"),						// Die Jahrgänge kämpfen gleichzeitig
	GETRENNT_NACH_MATTEN("Getrennt nach Matten"),		// Die Jahrgänge kämpfen parallel, jedoch strikt nach Matten getrennt
	ZUSAMMEN("Nacheinander");							// Die Jahrgänge kämpfen nacheinander, immer nur ein Jahrgang gleichzeitig bis er komplett fertig ist

	public static final String TYP = "separatealtersklassen";

	private final String bezeichnung;

	SeparateAlterklassen(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}

	public String getBezeichnung() {
		return bezeichnung;
	}
}
