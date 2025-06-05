package de.sinnix.judoturnier.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.Duration;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
@Setter
public class Wertung {
	private UUID uuid;

	// turnie;
	private Wettkaempfer sieger;
	private Duration zeit;
	private Integer punkteWettkaempferWeiss;
	private Integer strafenWettkaempferWeiss;
	private Integer punkteWettkaempferRot;
	private Integer strafenWettkaempferRot;

	// randor;
	private Integer kampfgeistWettkaempfer1;
	private Integer technikWettkaempfer1;
	private Integer kampfstilWettkaempfer1;
	private Integer vielfaltWettkaempfer1;

	private Integer kampfgeistWettkaempfer2;
	private Integer technikWettkaempfer2;
	private Integer kampfstilWettkaempfer2;
	private Integer vielfaltWettkaempfer2;

	@ToString.Exclude
	private Benutzer bewerter;
}
