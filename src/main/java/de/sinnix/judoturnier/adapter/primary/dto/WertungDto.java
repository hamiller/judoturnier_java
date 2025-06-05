package de.sinnix.judoturnier.adapter.primary.dto;

import de.sinnix.judoturnier.model.Wettkaempfer;

public record WertungDto(Wettkaempfer sieger,
	 Long zeit,
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
	 BenutzerDto bewerter) {
}
