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
}
