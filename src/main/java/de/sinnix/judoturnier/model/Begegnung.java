package de.sinnix.judoturnier.model;

public record Begegnung(
	Integer begegnungId,
	Integer matteId,
	Integer mattenRunde,
	Integer gruppenRunde,
	Wettkaempfer wettkaempfer1,
	Wettkaempfer wettkaempfer2,
	Wertung wertung) {
}
