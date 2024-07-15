package de.sinnix.judoturnier.adapter.primary;

import de.sinnix.judoturnier.model.GruppenRunde;

import java.util.List;

public record MatteDto(Integer id, List<GruppenRunde> gruppenRunden) {
}
