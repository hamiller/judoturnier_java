package de.sinnix.judoturnier.adapter.secondary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface TurnierJpaRepository extends JpaRepository<TurnierJpa, UUID> {
}
