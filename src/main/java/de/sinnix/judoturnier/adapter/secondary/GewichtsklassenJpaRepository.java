package de.sinnix.judoturnier.adapter.secondary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GewichtsklassenJpaRepository extends JpaRepository<GewichtsklassenJpa, String> {
	List<GewichtsklassenJpa> findAllByTurnierUUID(String turnierUuid);
	void deleteAllByTurnierUUID(String turnierUuid);
	void deleteAllByAltersklasseAndTurnierUUID(String altersklasse, String turnierUuid);
}
