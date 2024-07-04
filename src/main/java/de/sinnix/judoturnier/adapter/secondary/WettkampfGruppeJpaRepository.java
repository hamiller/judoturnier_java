package de.sinnix.judoturnier.adapter.secondary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WettkampfGruppeJpaRepository extends JpaRepository<WettkampfGruppeJpa, Integer> {
}
