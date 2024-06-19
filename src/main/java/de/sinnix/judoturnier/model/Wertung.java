package de.sinnix.judoturnier.model;

public record Wertung(
        Integer id,

        // turnier
        Wettkaempfer sieger,
        Integer zeit,
        Integer punkteWettkaempfer_weiss,
        Integer strafenWettkaempfer_weiss,
        Integer punkteWettkaempfer_blau,
        Integer strafenWettkaempfer_blau,

        // randori
        Integer kampfgeistWettkaempfer1,
        Integer technikWettkaempfer1,
        Integer kampfstilWettkaempfer1,
        Integer fairnessWettkaempfer1,

        Integer kampfgeistWettkaempfer2,
        Integer technikWettkaempfer2,
        Integer kampfstilWettkaempfer2,
        Integer fairnessWettkaempfer2) {
}
