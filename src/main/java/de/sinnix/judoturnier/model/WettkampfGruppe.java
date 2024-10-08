package de.sinnix.judoturnier.model;

import java.util.List;
import java.util.UUID;

public record WettkampfGruppe(
	Integer id,
	String name,
	String typ,
	Altersklasse altersklasse,
	List<BegegnungenJeRunde> alleRundenBegegnungen,
	UUID turnierUUID) {
}