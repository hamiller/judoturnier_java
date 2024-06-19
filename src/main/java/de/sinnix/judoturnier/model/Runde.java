package de.sinnix.judoturnier.model;

import java.util.List;

public record Runde(
        Integer id,
        Integer mattenRunde,
        Integer gruppenRunde, // Optional
        Integer rundeTotal, // Optional
        Integer matteId, // Optional
        Altersklasse altersklasse,
        WettkampfGruppe gruppe,
        List<Begegnung> begegnungen) {
}
