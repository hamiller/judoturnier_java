package de.sinnix.judoturnier.adapter.secondary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BenutzerJpaRepository extends JpaRepository<BenutzerJpa, String> {
	Optional<BenutzerJpa> findByUsername(String username);
}
