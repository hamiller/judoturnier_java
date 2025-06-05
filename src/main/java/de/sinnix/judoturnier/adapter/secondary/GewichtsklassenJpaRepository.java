package de.sinnix.judoturnier.adapter.secondary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


import de.sinnix.judoturnier.adapter.secondary.jpa.GewichtsklassenJpa;

@Repository
public interface GewichtsklassenJpaRepository extends JpaRepository<GewichtsklassenJpa, UUID> {
	List<GewichtsklassenJpa> findAllByTurnierUUID(UUID turnierUuid);
	void deleteAllByTurnierUUID(UUID turnierUuid);
	void deleteAllByAltersklasseAndTurnierUUID(String altersklasse, UUID turnierUuid);
}
