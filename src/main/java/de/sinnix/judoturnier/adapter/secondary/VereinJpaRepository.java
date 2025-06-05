package de.sinnix.judoturnier.adapter.secondary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


import de.sinnix.judoturnier.adapter.secondary.jpa.VereinJpa;

@Repository
public interface VereinJpaRepository extends JpaRepository<VereinJpa, UUID> {
	List<VereinJpa> findAllByTurnierUUID(UUID turnierUuid);
}
