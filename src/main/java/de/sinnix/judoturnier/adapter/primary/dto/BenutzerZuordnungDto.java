package de.sinnix.judoturnier.adapter.primary.dto;

public record BenutzerZuordnungDto(String userid, Boolean zugeordnetZuTurnier) {

	public BenutzerZuordnungDto {
		zugeordnetZuTurnier = Boolean.TRUE.equals(zugeordnetZuTurnier);
	}

	public boolean istZugeordnetZuTurnier() {
		return zugeordnetZuTurnier;
	}
}
