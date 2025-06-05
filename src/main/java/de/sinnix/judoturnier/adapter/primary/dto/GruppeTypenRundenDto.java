package de.sinnix.judoturnier.adapter.primary.dto;

import java.util.List;

public record GruppeTypenRundenDto(String gruppenId, String gruppenTyp, List<RundeDto> gewinnerrundeDtoList, List<RundeDto> trostrundeDtoList) {
}
