package de.sinnix.judoturnier.model;

import java.util.List;

public record GewichtsklassenGruppen(
        Altersklasse altersKlasse,
        Integer anzahlTeilnehmer,
        List<GewichtsklassenGruppe> gruppen) {
}
