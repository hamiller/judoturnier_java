package de.sinnix.judoturnier.model;

import java.util.List;
import java.util.UUID;

public record OidcBenutzer(UUID uuid, String username, String name, List<BenutzerRolle> benutzerRollen) {
	public boolean istAdmin() {
		return benutzerRollen.contains(BenutzerRolle.ADMINISTRATOR);
	}
}
