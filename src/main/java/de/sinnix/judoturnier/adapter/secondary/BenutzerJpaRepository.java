package de.sinnix.judoturnier.adapter.secondary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


import de.sinnix.judoturnier.adapter.secondary.jpa.BenutzerJpa;

@Repository
public interface BenutzerJpaRepository extends JpaRepository<BenutzerJpa, UUID> {
	Optional<BenutzerJpa> findByUsername(String username);
}
