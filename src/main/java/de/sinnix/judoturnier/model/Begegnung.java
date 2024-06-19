package de.sinnix.judoturnier.model;

public record Begegnung(
        Integer begegnung_id,
        Wettkaempfer wettkaempfer1,
        Wettkaempfer wettkaempfer2,
        Wertung wertung) {
}
