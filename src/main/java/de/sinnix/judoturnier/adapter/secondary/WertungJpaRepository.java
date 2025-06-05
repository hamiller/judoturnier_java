package de.sinnix.judoturnier.adapter.secondary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;


import de.sinnix.judoturnier.adapter.secondary.jpa.WertungJpa;

@Repository
public interface WertungJpaRepository extends JpaRepository<WertungJpa, UUID> {
}
