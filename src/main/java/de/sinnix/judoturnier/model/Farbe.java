package de.sinnix.judoturnier.model;

public enum Farbe {

    WEISS("weiß"),
    BRAUN("braun"),
    GELB("gelb"),
    ORANGE("orange"),
    GRUEN("grün"),
    BLAU("blau");

    private final String bezeichnung;

    Farbe(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }
}
