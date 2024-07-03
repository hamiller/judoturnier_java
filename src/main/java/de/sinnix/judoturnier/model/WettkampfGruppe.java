package de.sinnix.judoturnier.model;

import java.util.List;

public record WettkampfGruppe(
	Integer id,
	String name,
	String typ,
	// TODO refactor: das hier ist eigentlich eine Liste von Runden
	List<List<Begegnung>> alleGruppenBegegnungen) {
}
