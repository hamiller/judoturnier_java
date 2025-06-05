package de.sinnix.judoturnier.adapter.secondary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


import de.sinnix.judoturnier.adapter.secondary.jpa.WettkaempferJpa;

@Repository
public interface WettkaempferJpaRepository extends JpaRepository<WettkaempferJpa, UUID> {
	List<WettkaempferJpa> findAllByTurnierUUID(UUID turnierUuid);
}
