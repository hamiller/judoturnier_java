package de.sinnix.judoturnier.model;

import java.util.List;

public record GeschlechtAltersklasse(Geschlecht geschlecht, List<AltersklasseGewicht> altersKlassenGewichte) {
}
