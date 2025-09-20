package de.sinnix.judoturnier.adapter.secondary;

import de.sinnix.judoturnier.adapter.secondary.converter.BenutzerConverter;
import de.sinnix.judoturnier.adapter.secondary.jpa.BenutzerJpa;
import de.sinnix.judoturnier.adapter.secondary.jpa.TurnierRollenJpa;
import de.sinnix.judoturnier.model.Benutzer;
import de.sinnix.judoturnier.model.TurnierRollen;
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
	private TurnierRollenJpaRepository turnierRollenJpaRepository;
	@Autowired
	private BenutzerConverter          benutzerConverter;

	@Transactional
	public Optional<Benutzer> findBenutzer(UUID benutzerId) {
		logger.debug("BenutzerRepository.findBenutzer {}", benutzerId);
		return benutzerJpaRepository.findById(benutzerId)
			.map(jpa -> benutzerConverter.convertToBenutzer(jpa));
	}

	public Optional<Benutzer> findBenutzerByUsername(String username) {
		logger.debug("BenutzerRepository.findBenutzerByUsername {}", username);
		return benutzerJpaRepository.findByUsername(username)
			.map(jpa -> {
				logger.debug("Benutzer gefunden {}", jpa);
				return benutzerConverter.convertToBenutzer(jpa);
			});
	}

	@Transactional
	public Benutzer save(Benutzer benutzer) {
		logger.info("Suche nach Benutzer mit id {}", benutzer.uuid().toString());
		Optional<BenutzerJpa> optionalBenutzerJpa = benutzerJpaRepository.findById(benutzer.uuid());

		BenutzerJpa futureJpa = benutzerConverter.convertFromBenutzer(benutzer);
		if (optionalBenutzerJpa.isPresent()) {
			logger.info("Benutzer mit id {} gefunden, aktualisiere", benutzer.uuid().toString());
			BenutzerJpa existingJpa = optionalBenutzerJpa.get();
			existingJpa.updateFrom(futureJpa);
			return benutzerConverter.convertToBenutzer(benutzerJpaRepository.save(existingJpa));
		}

		logger.info("neu anlegen");
		logger.info("Benutzer: {}", futureJpa);
		return benutzerConverter.convertToBenutzer(benutzerJpaRepository.save(futureJpa));
	}

	public List<Benutzer> findAll() {
		logger.info("Hole alle Benutzer des Systems");
		return benutzerJpaRepository.findAll().stream()
			.map(jpa -> benutzerConverter.convertToBenutzer(jpa))
			.peek(b -> logger.trace("Benutzer {}", b))
			.collect(Collectors.toList());
	}

	public void addTurnierRollen(UUID userId, TurnierRollen turnierRollen, UUID turnierUUID) {
		logger.info("Speichere TurnierRollen für Benutzer");
		Optional<TurnierRollenJpa> optionalTurnierRollen = turnierRollenJpaRepository.findAllByBenutzerIdAndTurnierUuid(userId, turnierUUID);

		if (optionalTurnierRollen.isPresent()) {
			logger.debug("TurnierRollen existiert bereits, aktualisiere: {}", turnierRollen);
			TurnierRollenJpa existing = optionalTurnierRollen.get();
			existing.updateFrom(turnierRollen);
			turnierRollenJpaRepository.save(existing);
			return;
		}

		logger.debug("TurnierRollen wird neu angelegt: {}", turnierRollen);
		BenutzerJpa benutzerJpa = benutzerJpaRepository.findById(userId).orElseThrow();
		TurnierRollenJpa newTurnierRollen = new TurnierRollenJpa(benutzerJpa, turnierRollen.rollen(), turnierUUID);
		turnierRollenJpaRepository.save(newTurnierRollen);
	}
}
