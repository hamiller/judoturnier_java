package de.sinnix.judoturnier.model;

import java.util.List;

public record Matte(
    Integer id,
    List<Runde> runden,
    List<GruppenRunde> gruppenRunden) {
}
