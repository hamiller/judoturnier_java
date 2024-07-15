package de.sinnix.judoturnier.adapter.secondary;

import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.GewichtsklassenGruppe;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class GewichtsklassenRepository {
	private static final Logger logger = LogManager.getLogger(GewichtsklassenRepository.class);

	@Autowired
	private GewichtsklassenJpaRepository gewichtsklassenJpaRepository;
	@Autowired
	private GewichtsklassenConverter     gewichtsklassenConverter;

	public List<GewichtsklassenGruppe> findAll(UUID turnierUUID) {
		return gewichtsklassenJpaRepository.findAllByTurnierUUID(turnierUUID.toString()).stream()
			.map(jpa -> gewichtsklassenConverter.convertToGewichtsklassen(jpa))
			.toList();
	}

	public void deleteAll() {
		gewichtsklassenJpaRepository.deleteAll();
	}

	public void saveAll(List<GewichtsklassenGruppe> gewichtsklassenList) {
		logger.info("Speichere Liste der GewichtsklassenGruppe in db: {}", gewichtsklassenList);
		List<GewichtsklassenJpa> gewichtsklassenJpaList = gewichtsklassenList.stream()
			.map(gwk -> gewichtsklassenConverter.convertFromGewichtsklassen(gwk))
			.toList();
		gewichtsklassenJpaRepository.saveAll(gewichtsklassenJpaList);
	}

	@Transactional
	public void deleteAllByAltersklasse(UUID turnierUUID, Altersklasse altersklasse) {
		gewichtsklassenJpaRepository.deleteAllByAltersklasseAndTurnierUUID(altersklasse.name(), turnierUUID.toString());
	}
}
