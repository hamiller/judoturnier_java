package de.sinnix.judoturnier.adapter.secondary;

import de.sinnix.judoturnier.adapter.secondary.converter.GewichtsklassenConverter;
import de.sinnix.judoturnier.adapter.secondary.jpa.GewichtsklassenJpa;
import de.sinnix.judoturnier.adapter.secondary.jpa.WettkaempferJpa;
import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.GewichtsklassenGruppe;
import de.sinnix.judoturnier.model.Wettkaempfer;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class GewichtsklassenRepository {
	private static final Logger logger = LogManager.getLogger(GewichtsklassenRepository.class);

	@Autowired
	private GewichtsklassenJpaRepository gewichtsklassenJpaRepository;
	@Autowired
	private GewichtsklassenConverter     gewichtsklassenConverter;
	@Autowired
	private WettkaempferJpaRepository    wettkaempferJpaRepository;

	public List<GewichtsklassenGruppe> findAll(UUID turnierUUID) {
		return gewichtsklassenJpaRepository.findAllByTurnierUUID(turnierUUID).stream()
			.map(jpa -> gewichtsklassenConverter.convertToGewichtsklassen(jpa))
			.toList();
	}

	@Transactional
	public void deleteAll(UUID turnierUUID) {
		gewichtsklassenJpaRepository.deleteAllByTurnierUUID(turnierUUID);
	}

	@Transactional
	public void saveAll(List<GewichtsklassenGruppe> gewichtsklassenList) {
		logger.info("Speichere Liste der GewichtsklassenGruppe");

		for (GewichtsklassenGruppe gwk : gewichtsklassenList) {
			List<WettkaempferJpa> teilnehmerJpaList = wettkaempferJpaRepository.findAllById(gwk.teilnehmer().stream().map(Wettkaempfer::id).toList());
			if (gwk.id() != null) {
				Optional<GewichtsklassenJpa> optionalGwkJpa = gewichtsklassenJpaRepository.findById(gwk.id());
				if (optionalGwkJpa.isPresent()) {
					logger.debug("GewichtsklassenGruppe existiert bereits, aktualisiere {}", gwk);
					GewichtsklassenJpa jpa = optionalGwkJpa.get();
					jpa.updateFrom(gewichtsklassenConverter.convertFromGewichtsklassen(gwk), teilnehmerJpaList);
					gewichtsklassenJpaRepository.save(jpa);
					continue;
				}
			}

			logger.debug("GewichtsklassenGruppe wird neu angelegt {}", gwk);
			GewichtsklassenJpa jpa = gewichtsklassenConverter.convertFromGewichtsklassen(gwk);
			jpa.setTeilnehmer(teilnehmerJpaList);
			gewichtsklassenJpaRepository.save(jpa);
		}
	}

	public void deleteAllByAltersklasse(UUID turnierUUID, Altersklasse altersklasse) {
		gewichtsklassenJpaRepository.deleteAllByAltersklasseAndTurnierUUID(altersklasse.name(), turnierUUID);
	}
}
