package de.sinnix.judoturnier.adapter.primary.dto;

import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.WettkampfGruppe;

import java.util.List;
import java.util.UUID;

public record RundeDto(UUID rundeId,
					   Integer mattenRunde,
					   Integer gruppenRunde, // Optional
					   Integer rundeGesamt, // Optional
					   Integer matteId, // Optional
					   Altersklasse altersklasse,
					   WettkampfGruppe gruppe,
					   List<BegegnungDto> begegnungen) {
}
