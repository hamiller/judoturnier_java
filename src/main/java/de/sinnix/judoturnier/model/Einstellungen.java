package de.sinnix.judoturnier.model;

import java.util.UUID;

public record Einstellungen(
	TurnierTyp turnierTyp,
	MattenAnzahl mattenAnzahl,
	WettkampfReihenfolge wettkampfReihenfolge,
	RandoriGruppengroesse randoriGruppengroesse,
	VariablerGewichtsteil variablerGewichtsteil,
	UUID turnierUUID) {
}
