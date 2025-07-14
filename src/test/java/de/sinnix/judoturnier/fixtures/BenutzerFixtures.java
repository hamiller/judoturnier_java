package de.sinnix.judoturnier.fixtures;

import java.util.List;
import java.util.UUID;


import de.sinnix.judoturnier.model.Benutzer;
import de.sinnix.judoturnier.model.BenutzerRolle;

public class BenutzerFixtures {
	public static Benutzer DUMMY_KAMPFRICHTER = new Benutzer(
		UUID.fromString("659b104d-6267-4fe0-b70e-641c455b6030"),
		"dummy_kampfrichter",
		"Dummy Kampfrichter",
		List.of(),
		List.of(BenutzerRolle.KAMPFRICHTER)
	);
}
