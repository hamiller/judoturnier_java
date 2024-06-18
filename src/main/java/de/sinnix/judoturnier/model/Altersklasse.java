package de.sinnix.judoturnier.model;

public enum Altersklasse {

    U9("U9"),
    U11("U11"),
    U12("U12"),
    U13("U13"),
    U15("U15"),
    U18("U18"),
    U21("U21"),
    Maenner("Männer"),
    Frauen("Frauen");

    private final String bezeichnung;

    Altersklasse(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }
}
