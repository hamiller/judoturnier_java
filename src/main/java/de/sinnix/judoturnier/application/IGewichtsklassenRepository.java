package de.sinnix.judoturnier.application;

import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.GewichtsklassenGruppe;

import java.util.List;

public interface IGewichtsklassenRepository {
    List<GewichtsklassenGruppe> findAll();

    void deleteAll();

    void saveAll(List<GewichtsklassenGruppe> gewichtsklassenList);

    void deleteAllByAltersklasse(Altersklasse altersklasse);
}
