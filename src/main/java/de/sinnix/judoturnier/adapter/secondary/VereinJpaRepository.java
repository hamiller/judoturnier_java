package de.sinnix.judoturnier.adapter.secondary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VereinJpaRepository extends JpaRepository<VereinJpa, Integer> {
	List<VereinJpa> findAllByTurnierUUID(String turnierUuid);
}
