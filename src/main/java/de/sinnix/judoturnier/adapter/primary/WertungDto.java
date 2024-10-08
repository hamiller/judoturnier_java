package de.sinnix.judoturnier.adapter.primary;

import de.sinnix.judoturnier.model.Benutzer;
import de.sinnix.judoturnier.model.Wettkaempfer;

public record WertungDto(Wettkaempfer sieger,
	 String zeit,
	 Integer punkteWettkaempferWeiss,
	 Integer strafenWettkaempferWeiss,
	 Integer punkteWettkaempferRot,
	 Integer strafenWettkaempferRot,
	 Integer kampfgeistWettkaempfer1,
	 Integer technikWettkaempfer1,
	 Integer kampfstilWettkaempfer1,
	 Integer fairnessWettkaempfer1,
	 Integer kampfgeistWettkaempfer2,
	 Integer technikWettkaempfer2,
	 Integer kampfstilWettkaempfer2,
	 Integer fairnessWettkaempfer2,
	 Benutzer bewerter) {
}
