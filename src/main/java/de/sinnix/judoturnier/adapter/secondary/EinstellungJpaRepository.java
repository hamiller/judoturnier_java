package de.sinnix.judoturnier.adapter.secondary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import de.sinnix.judoturnier.adapter.secondary.jpa.EinstellungJpa;

@Repository
public interface EinstellungJpaRepository extends JpaRepository<EinstellungJpa, EinstellungJpa.EinstellungId> {
}
