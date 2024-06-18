package de.sinnix.judoturnier.model;

public enum Kampfsystem {

    POOL4("4er Pool"),
    POOL("Pool"),
    KO("KO-System"),
    DOPPELKO("doppeltes KO-System");

    private final String bezeichnung;

    Kampfsystem(final String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }
}
