package de.sinnix.judoturnier.adapter.secondary;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GewichtsklassenJpaRepository extends JpaRepository<GewichtsklassenJpa, Integer> {
	@Transactional
	void deleteAllByAltersklasse(String altersklasse);
}
