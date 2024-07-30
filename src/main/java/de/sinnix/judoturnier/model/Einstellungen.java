package de.sinnix.judoturnier.model;

import java.util.UUID;

public record Einstellungen(
	TurnierTyp turnierTyp,
	MattenAnzahl mattenAnzahl,
	WettkampfReihenfolge wettkampfReihenfolge,
	Gruppengroesse gruppengroesse,
	VariablerGewichtsteil variablerGewichtsteil,
	SeparateAlterklassen separateAlterklassen,
	UUID turnierUUID) {
}
