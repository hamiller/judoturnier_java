package de.sinnix.judoturnier.model;

import java.util.List;
import java.util.UUID;

public record WettkampfGruppe(
	UUID id,
	String name,
	String typ,
	Altersklasse altersklasse,
	List<BegegnungenJeRunde> alleRundenBegegnungen,
	UUID turnierUUID) {
	@Override
	public String toString() {
		return "WettkampfGruppe{" +
			"id=" + id +
			", name='" + name + '\'' +
			", typ='" + typ + '\'' +
			", altersklasse=" + altersklasse +
			", alleRundenBegegnungen(size)=" + alleRundenBegegnungen +
			", turnierUUID=" + turnierUUID +
			'}';
	}
}
