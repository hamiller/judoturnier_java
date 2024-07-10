package de.sinnix.judoturnier.adapter.secondary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BegegnungJpaRepository extends JpaRepository<BegegnungJpa, Integer> {
	void deleteAllByTurnierUUID(String turnierUuid);
	List<BegegnungJpa> findAllByTurnierUUID(String turnierUuid);
}
