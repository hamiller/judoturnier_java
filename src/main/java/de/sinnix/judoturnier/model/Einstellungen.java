package de.sinnix.judoturnier.model;

import java.util.UUID;

public record Einstellungen(
	TurnierTyp turnierTyp,
	MattenAnzahl mattenAnzahl,
	WettkampfReihenfolge wettkampfReihenfolge,
	Gruppengroessen gruppengroessen,
	VariablerGewichtsteil variablerGewichtsteil,
	SeparateAlterklassen separateAlterklassen,
	Wettkampfzeiten wettkampfzeiten,
	UUID turnierUUID) {
}
