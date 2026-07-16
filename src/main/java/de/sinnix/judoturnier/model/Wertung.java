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

	// turnier
	private Wettkaempfer sieger;
	private Duration zeit;
	private Integer ipponWettkaempferWeiss;
	private Integer wazariWettkaempferWeiss;
	private Integer yukoWettkaempferWeiss;
	private Integer shidoWettkaempferWeiss;
	private Boolean hansokuMakeWettkaempferWeiss;
	private Integer ipponWettkaempferRot;
	private Integer wazariWettkaempferRot;
	private Integer yukoWettkaempferRot;
	private Integer shidoWettkaempferRot;
	private Boolean hansokuMakeWettkaempferRot;

	// randori
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

	public Wertung(UUID uuid,
	               Wettkaempfer sieger,
	               Duration zeit,
	               Integer punkteWettkaempferWeiss,
	               Integer strafenWettkaempferWeiss,
	               Integer punkteWettkaempferRot,
	               Integer strafenWettkaempferRot,
	               Integer kampfgeistWettkaempfer1,
	               Integer technikWettkaempfer1,
	               Integer kampfstilWettkaempfer1,
	               Integer vielfaltWettkaempfer1,
	               Integer kampfgeistWettkaempfer2,
	               Integer technikWettkaempfer2,
	               Integer kampfstilWettkaempfer2,
	               Integer vielfaltWettkaempfer2,
	               Benutzer bewerter) {
		this(uuid, sieger, zeit,
			0, 0, punkteWettkaempferWeiss, strafenWettkaempferWeiss, strafenWettkaempferWeiss != null && strafenWettkaempferWeiss >= 3,
			0, 0, punkteWettkaempferRot, strafenWettkaempferRot, strafenWettkaempferRot != null && strafenWettkaempferRot >= 3,
			kampfgeistWettkaempfer1, technikWettkaempfer1, kampfstilWettkaempfer1, vielfaltWettkaempfer1,
			kampfgeistWettkaempfer2, technikWettkaempfer2, kampfstilWettkaempfer2, vielfaltWettkaempfer2,
			bewerter);
	}
}
