package de.sinnix.judoturnier.model;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public record GewichtsklassenGruppe(
	UUID id,
	Altersklasse altersKlasse,
	Optional<Geschlecht> gruppenGeschlecht,
	List<Wettkaempfer> teilnehmer,
	Optional<RandoriGruppenName> name,
	Double minGewicht,
	Double maxGewicht,
	UUID turnierUUID) {
}
