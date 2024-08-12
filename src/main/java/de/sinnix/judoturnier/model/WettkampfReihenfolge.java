package de.sinnix.judoturnier.model;

public enum WettkampfReihenfolge {

	ABWECHSELND("Abwechselnd"),		// Immer 2 Gruppen kämpfen abwechselnd bis sie komplett fertig sind
	ALLE("Alle");					// Es kämpfen je Durchgang alle Gruppen

	public static final String TYP = "wettkampfreihenfolge";
	private final       String bezeichnung;

	WettkampfReihenfolge(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}

	public String getBezeichnung() {
		return bezeichnung;
	}
}
