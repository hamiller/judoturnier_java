package de.sinnix.judoturnier.adapter.secondary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TurnierRollenJpaRepository extends JpaRepository<TurnierRollenJpa, String> {
	@Modifying
	@Query("DELETE FROM TurnierRollenJpa tr WHERE tr.turnierUuid = :turnierUuid")
	void deleteAllByTurnierUuid(@Param("turnierUuid") String turnierUuid);
}
