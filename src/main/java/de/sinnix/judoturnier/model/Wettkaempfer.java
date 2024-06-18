package de.sinnix.judoturnier.model;

public record Wettkaempfer(
    Long id,
    String name,
    Geschlecht geschlecht,
    Altersklasse altersklasse,
    Verein verein,
    Double gewicht,
    Farbe farbe,
    Boolean checked,
    Boolean printed) {}
