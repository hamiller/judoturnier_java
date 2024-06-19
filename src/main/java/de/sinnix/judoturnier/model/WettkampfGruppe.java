package de.sinnix.judoturnier.model;

import java.util.List;

public record WettkampfGruppe(
        Integer id,
        String name,
        String typ,
        List<List<Begegnung>> alleGruppenBegegnungen) {
}
