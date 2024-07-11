package de.sinnix.judoturnier.model;

import java.time.Duration;
import java.util.UUID;

public record Wertung(
	UUID uuid,

	// turnier
	Wettkaempfer sieger,
	Duration zeit,
	Integer punkteWettkaempferWeiss,
	Integer strafenWettkaempferWeiss,
	Integer punkteWettkaempferRot,
	Integer strafenWettkaempferRot,

	// randori
	Integer kampfgeistWettkaempfer1,
	Integer technikWettkaempfer1,
	Integer kampfstilWettkaempfer1,
	Integer fairnessWettkaempfer1,

	Integer kampfgeistWettkaempfer2,
	Integer technikWettkaempfer2,
	Integer kampfstilWettkaempfer2,
	Integer fairnessWettkaempfer2,

	Bewerter bewerter) {
}
