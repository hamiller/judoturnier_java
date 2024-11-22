package de.sinnix.judoturnier.model;

import java.util.List;
import java.util.UUID;

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
			", begegnungen(size)=" + begegnungen.size() +
			'}';
	}
}
