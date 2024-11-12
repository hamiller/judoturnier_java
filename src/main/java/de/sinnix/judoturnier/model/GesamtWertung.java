package de.sinnix.judoturnier.model;

import java.util.List;

public record GesamtWertung(Double kampfgeist, List<Integer> kampfgeistListe, Double technik, List<Integer> technikListe, Double kampfstil, List<Integer> kampfstilListe, Double fairness, List<Integer> fairnessListe) {
}
