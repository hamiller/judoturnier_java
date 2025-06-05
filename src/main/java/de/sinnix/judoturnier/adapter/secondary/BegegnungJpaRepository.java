package de.sinnix.judoturnier.adapter.secondary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


import de.sinnix.judoturnier.adapter.secondary.jpa.BegegnungJpa;

@Repository
public interface BegegnungJpaRepository extends JpaRepository<BegegnungJpa, UUID> {
	void deleteAllByTurnierUUID(UUID turnierUuid);
	List<BegegnungJpa> findAllByTurnierUUID(UUID turnierUuid);
	List<BegegnungJpa> findAllByTurnierUUIDAndWettkampfGruppeId(UUID turnierUuid, UUID wettkampfGruppeId);
}
