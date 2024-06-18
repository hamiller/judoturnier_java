package de.sinnix.judoturnier.model;

public enum WettkampfReihenfolge {

    ABWECHSELND("Abwechselnd"),
    ALLE("Alle");

    public static final String TYP = "wettkampfreihenfolge";
    private final String bezeichnung;

    WettkampfReihenfolge(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }
}
