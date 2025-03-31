package de.sinnix.judoturnier.model;

import java.util.UUID;

public record WettkampfGruppe(
	UUID id,
	String name,
	String typ,
	Altersklasse altersklasse,
	UUID turnierUUID) {
	@Override
	public String toString() {
		return "WettkampfGruppe{" +
			"id=" + id +
			", name='" + name + '\'' +
			", typ='" + typ + '\'' +
			", altersklasse=" + altersklasse +
			", turnierUUID=" + turnierUUID +
			'}';
	}
}
