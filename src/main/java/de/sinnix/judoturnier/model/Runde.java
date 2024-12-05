package de.sinnix.judoturnier.model;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public record Runde(
	UUID rundeId,
	Integer mattenRunde,
	Integer gruppenRunde, // Optional
	Integer rundeGesamt, // Optional
	Integer matteId, // Optional
	Altersklasse altersklasse,
	WettkampfGruppe gruppe,
	List<Begegnung> begegnungen) {
	@Override
	public String toString() {
		return "Runde{" +
			"rundeId=" + rundeId +
			", mattenRunde=" + mattenRunde +
			", gruppenRunde=" + gruppenRunde +
			", rundeGesamt=" + rundeGesamt +
			", matteId=" + matteId +
			", altersklasse=" + altersklasse +
			", gruppe=" + gruppe +
			", begegnungen()=" + begegnungen.stream().map(b -> String.valueOf(b.getBegegnungId().akuellePaarung)).collect(Collectors.joining(",")) +
			'}';
	}
}
