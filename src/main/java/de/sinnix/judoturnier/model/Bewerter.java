package de.sinnix.judoturnier.model;

import java.util.List;

public record Bewerter(String id, String username, String name, List<String> rollen) {

	public boolean darfEditieren() {
		return rollen.contains("ROLE_KAMPFRICHTER");
	}
}
