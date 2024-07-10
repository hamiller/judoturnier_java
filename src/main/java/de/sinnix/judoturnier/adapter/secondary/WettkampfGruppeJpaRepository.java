package de.sinnix.judoturnier.adapter.secondary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WettkampfGruppeJpaRepository extends JpaRepository<WettkampfGruppeJpa, Integer> {
	List<WettkampfGruppeJpa> findAllByTurnierUUID(String turnierUuid);
}
