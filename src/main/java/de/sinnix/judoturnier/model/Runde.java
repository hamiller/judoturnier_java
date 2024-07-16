package de.sinnix.judoturnier.model;

import java.util.List;

public record Runde(
	Integer id,
	Integer mattenRunde,
	Integer gruppenRunde, // Optional
	Integer rundeGesamt, // Optional
	Integer matteId, // Optional
	Altersklasse altersklasse,
	WettkampfGruppe gruppe,
	List<Begegnung> begegnungen) {
}
