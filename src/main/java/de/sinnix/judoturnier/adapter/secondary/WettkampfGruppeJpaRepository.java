package de.sinnix.judoturnier.adapter.secondary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WettkampfGruppeJpaRepository extends JpaRepository<WettkampfGruppeJpa, Integer> {
	List<WettkampfGruppeJpa> findAllByTurnierUUID(String turnierUuid);
	Optional<WettkampfGruppeJpa> findByIdAndTurnierUUID(Integer id, String turnierUuid);
	void deleteAllByTurnierUUID(String turnierUuid);
}
