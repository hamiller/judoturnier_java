package de.sinnix.judoturnier.adapter.secondary;

import de.sinnix.judoturnier.model.Benutzer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class BenutzerRepository {
	private static final Logger                logger = LogManager.getLogger(BenutzerRepository.class);
	@Autowired
	private              BenutzerJpaRepository benutzerJpaRepository;
	@Autowired
	private              BenutzerConverter     benutzerConverter;


	public Optional<Benutzer> findBenutzer(UUID benutzerId) {
		return benutzerJpaRepository.findById(benutzerId.toString())
			.map(jpa -> benutzerConverter.convertToBenutzer(jpa));
	}

	public Optional<Benutzer> findBenutzerByUsername(String username) {
		return benutzerJpaRepository.findByUsername(username)
			.map(jpa -> benutzerConverter.convertToBenutzer(jpa));
	}

	public Benutzer save(Benutzer benutzer) {
		logger.info("Suche nach Benutzer mit id {}", benutzer.uuid().toString());
		Optional<BenutzerJpa> optionalBenutzerJpa = benutzerJpaRepository.findById(benutzer.uuid().toString());

		BenutzerJpa futureBenutzerJpa = benutzerConverter.convertFromBenutzer(benutzer);
		if (optionalBenutzerJpa.isPresent()) {
			BenutzerJpa currentBenutzerJpa = optionalBenutzerJpa.get();
			logger.info("Benutzer gefunden! {}", currentBenutzerJpa);
			currentBenutzerJpa.setRollen(futureBenutzerJpa.getRollen());
			currentBenutzerJpa.setTurnierRollen(futureBenutzerJpa.getTurnierRollen());
			benutzerJpaRepository.save(currentBenutzerJpa);
			return benutzer;
		}

		logger.info("Erstelle Benutzer neu {}", futureBenutzerJpa);
		benutzerJpaRepository.save(futureBenutzerJpa);
		return benutzer;
	}
}
