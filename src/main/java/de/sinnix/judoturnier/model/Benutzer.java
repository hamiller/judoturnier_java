package de.sinnix.judoturnier.model;

import java.util.List;
import java.util.UUID;

public record Benutzer(UUID uuid, String username, String name, List<TurnierRollen> turnierRollen, List<BenutzerRolle> benutzerRollen) {

	public boolean istKampfrichter(UUID turnierId) {
		return turnierRollen.stream()
			.filter(t -> t.turnierId().equals(turnierId))
			.filter(r -> r.rollen().contains(BenutzerRolle.KAMPFRICHTER))
			.findFirst().isPresent();
	}

	public boolean istAdmin() {
		return benutzerRollen.contains(BenutzerRolle.ADMINISTRATOR);
	}
}
