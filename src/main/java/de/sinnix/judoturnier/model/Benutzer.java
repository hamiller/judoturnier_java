package de.sinnix.judoturnier.model;

import java.util.List;

public record Benutzer(String id, String username, String name, List<String> rollen) {

	public boolean istKampfrichter() {
		return rollen.contains("ROLE_KAMPFRICHTER");
	}

	public boolean istAdmin() {
		return rollen.contains("ROLE_ADMIN");
	}
}
