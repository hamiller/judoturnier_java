package de.sinnix.judoturnier.adapter.primary;

import java.util.List;

public record GruppeTypenRundenDto(String gruppenId, String gruppenTyp, List<RundeDto> gewinnerrundeDtoList, List<RundeDto> trostrundeDtoList) {
}
