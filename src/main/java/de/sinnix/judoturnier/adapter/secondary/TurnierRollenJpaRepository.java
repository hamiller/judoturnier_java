package de.sinnix.judoturnier.adapter.secondary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;


import de.sinnix.judoturnier.adapter.secondary.jpa.TurnierRollenJpa;

@Repository
public interface TurnierRollenJpaRepository extends JpaRepository<TurnierRollenJpa, UUID> {
	@Modifying
	@Query("DELETE FROM TurnierRollenJpa tr WHERE tr.turnierUuid = :turnierUuid")
	void deleteAllByTurnierUuid(@Param("turnierUuid") UUID turnierUuid);
	Optional<TurnierRollenJpa> findAllByBenutzerIdAndTurnierUuid(UUID userId, UUID turnierUUID);
}
