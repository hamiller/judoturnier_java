package de.sinnix.judoturnier.model;

public enum Geschlecht {
    m("Männlich"),
    w("Weiblich");

    private final String bezeichnung;

    Geschlecht(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }
}
