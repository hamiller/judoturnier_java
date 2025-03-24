package de.sinnix.judoturnier.adapter.secondary;

import de.sinnix.judoturnier.model.Benutzer;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class BenutzerRepository {
	private static final Logger                     logger = LogManager.getLogger(BenutzerRepository.class);
	@Autowired
	private              BenutzerJpaRepository      benutzerJpaRepository;
	@Autowired
	private              TurnierRollenJpaRepository turnierRollenJpaRepository;
	@Autowired
	private              BenutzerConverter          benutzerConverter;

	public Optional<Benutzer> findBenutzer(UUID benutzerId) {
		logger.debug("BenutzerRepository.findBenutzer {}", benutzerId);
		return benutzerJpaRepository.findById(benutzerId.toString())
			.map(jpa -> benutzerConverter.convertToBenutzer(jpa));
	}

	public Optional<Benutzer> findBenutzerByUsername(String username) {
		logger.debug("BenutzerRepository.findBenutzerByUsername {}", username);
		return benutzerJpaRepository.findByUsername(username)
			.map(jpa -> benutzerConverter.convertToBenutzer(jpa));
	}

	public Benutzer save(Benutzer benutzer) {
		logger.info("Suche nach Benutzer mit id {}", benutzer.uuid().toString());
		Optional<BenutzerJpa> optionalBenutzerJpa = benutzerJpaRepository.findById(benutzer.uuid().toString());

		BenutzerJpa futureBenutzerJpa = benutzerConverter.convertFromBenutzer(benutzer);
		if (optionalBenutzerJpa.isPresent()) {
			BenutzerJpa currentBenutzerJpa = optionalBenutzerJpa.get();
			logger.debug("existierenden Benutzer {} gefunden,\n schreibe neue Werte {}", currentBenutzerJpa, futureBenutzerJpa);
			if (currentBenutzerJpa.getRollen() != null) {
				currentBenutzerJpa.getRollen().clear();
				currentBenutzerJpa.getRollen().addAll(futureBenutzerJpa.getRollen());
			}
			if (currentBenutzerJpa.getTurnierRollen() != null) {
				currentBenutzerJpa.getTurnierRollen().clear();
				currentBenutzerJpa.getTurnierRollen().addAll(futureBenutzerJpa.getTurnierRollen());
			}
			benutzerJpaRepository.save(currentBenutzerJpa);
			return benutzer;
		}

		logger.info("Erstelle Benutzer neu {}", futureBenutzerJpa);
		benutzerJpaRepository.save(futureBenutzerJpa);
		return benutzer;
	}

	public List<Benutzer> findAll() {
		logger.info("Hole alle Benutzer des Systems");
		return benutzerJpaRepository.findAll().stream()
			.map(jpa -> benutzerConverter.convertToBenutzer(jpa))
			.peek(b -> logger.trace("Benutzer {}", b))
			.collect(Collectors.toList());
	}

	public void deleteTurnierRollen(UUID turnierUUID) {
		turnierRollenJpaRepository.deleteAllByTurnierUuid(turnierUUID.toString());
	}
}
