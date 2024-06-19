package de.sinnix.judoturnier.model;

import java.util.List;

public record GewichtsklassenGruppe(
        Integer id,
        Altersklasse altersKlasse,
        Geschlecht gruppenGeschlecht,
        List<Wettkaempfer> teilnehmer,
        String name,
        Double minGewicht,
        Double maxGewicht) {
}
