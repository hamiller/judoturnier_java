package de.sinnix.judoturnier.adapter.secondary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BegegnungJpaRepository extends JpaRepository<BegegnungJpa, String> {
	void deleteAllByTurnierUUID(String turnierUuid);
	List<BegegnungJpa> findAllByTurnierUUID(String turnierUuid);
	List<BegegnungJpa> findAllByTurnierUUIDAndWettkampfGruppeId(String turnierUuid, String wettkampfGruppeId);
}
