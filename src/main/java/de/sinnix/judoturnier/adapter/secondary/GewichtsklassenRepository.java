package de.sinnix.judoturnier.adapter.secondary;

import de.sinnix.judoturnier.application.IGewichtsklassenRepository;
import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.GewichtsklassenGruppe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GewichtsklassenRepository implements IGewichtsklassenRepository {

    @Autowired
    private GewichtsklassenJpaRepository gewichtsklassenJpaRepository;
    @Autowired
    private GewichtsklassenConverter gewichtsklassenConverter;

    @Override
    public List<GewichtsklassenGruppe> findAll() {
        return gewichtsklassenJpaRepository.findAll().stream()
                .map(jpa -> gewichtsklassenConverter.convertToGewichtsklassen(jpa))
                .toList();
    }

    @Override
    public void deleteAll() {
        gewichtsklassenJpaRepository.deleteAll();
    }

    @Override
    public void saveAll(List<GewichtsklassenGruppe> gewichtsklassenList) {
        List<GewichtsklassenJpa> gewichtsklassenJpaList = gewichtsklassenList.stream()
                .map(gwk -> gewichtsklassenConverter.convertFromGewichtsklassen(gwk))
                .toList();
        gewichtsklassenJpaRepository.saveAll(gewichtsklassenJpaList);
    }

    @Override
    public void deleteAllByAltersklasse(Altersklasse altersklasse) {
        gewichtsklassenJpaRepository.deleteAllByAltersklasse(altersklasse);
    }
}
