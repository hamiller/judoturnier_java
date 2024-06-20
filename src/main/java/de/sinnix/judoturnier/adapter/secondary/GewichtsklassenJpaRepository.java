package de.sinnix.judoturnier.adapter.secondary;

import de.sinnix.judoturnier.model.Altersklasse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GewichtsklassenJpaRepository extends JpaRepository<GewichtsklassenJpa, Integer> {
    void deleteAllByAltersklasse(Altersklasse altersklasse);
}
