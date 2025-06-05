package de.sinnix.judoturnier.adapter.secondary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


import de.sinnix.judoturnier.adapter.secondary.jpa.WettkampfGruppeJpa;

@Repository
public interface WettkampfGruppeJpaRepository extends JpaRepository<WettkampfGruppeJpa, UUID> {
	List<WettkampfGruppeJpa> findAllByTurnierUUID(UUID turnierUuid);
	Optional<WettkampfGruppeJpa> findByIdAndTurnierUUID(UUID id, UUID turnierUuid);
	void deleteAllByTurnierUUID(UUID turnierUuid);
}
