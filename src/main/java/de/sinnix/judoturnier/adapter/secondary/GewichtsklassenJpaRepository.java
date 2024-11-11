package de.sinnix.judoturnier.adapter.secondary;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GewichtsklassenJpaRepository extends JpaRepository<GewichtsklassenJpa, String> {
	List<GewichtsklassenJpa> findAllByTurnierUUID(String turnierUuid);
	@Transactional
	void deleteAllByTurnierUUID(String turnierUuid);
	@Transactional
	void deleteAllByAltersklasseAndTurnierUUID(String altersklasse, String turnierUuid);
}
