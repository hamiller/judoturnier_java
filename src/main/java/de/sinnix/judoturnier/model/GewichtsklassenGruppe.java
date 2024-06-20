package de.sinnix.judoturnier.model;

import java.util.List;
import java.util.Optional;

public record GewichtsklassenGruppe(
        Integer id,
        Altersklasse altersKlasse,
        Optional<Geschlecht> gruppenGeschlecht,
        List<Wettkaempfer> teilnehmer,
        String name,
        Double minGewicht,
        Double maxGewicht) {
}
