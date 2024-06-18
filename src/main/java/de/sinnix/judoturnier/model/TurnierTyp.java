package de.sinnix.judoturnier.model;

public enum TurnierTyp {

    RANDORI("Randori"),
    STANDARD("Standard");

    public static final String TYP = "turniertyp";

    private final String bezeichnung;

    TurnierTyp(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }
}
