package de.sinnix.judoturnier.application;

import de.sinnix.judoturnier.adapter.secondary.BenutzerRepository;
import de.sinnix.judoturnier.adapter.secondary.TurnierRepository;
import de.sinnix.judoturnier.model.Benutzer;
import de.sinnix.judoturnier.model.Turnier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class BenutzerService {
	private static final Logger logger = LogManager.getLogger(BenutzerService.class);

	@Autowired
	private BenutzerRepository benutzerRepository;
	@Lazy
	@Autowired
	private TurnierRepository  turnierRepository;

	// zu jeder benutzerId eine Liste mit TurnierIds
	private volatile Map<UUID, List<UUID>> benutzerTurniereMap = new HashMap<>();

	public List<Turnier> holeAlleTurniereZuBenutzer(UUID benutzerId) {
		logger.info("Hole alle Turniere für Benutzer {}", benutzerId);

		return List.of();
	}

	public List<Benutzer> holeAlleBenutzerZuTurnier(UUID turnierId) {
		logger.info("Hole alle Benutzer für Turnier {}", turnierId);

		return List.of();
	}

	public boolean darfBenutzerTurnierSehen(UUID benutzerId, UUID turnierId) {
		List<UUID> turnierIds = benutzerTurniereMap.get(benutzerId);
		if (turnierIds == null) {
			Benutzer benutzer = benutzerRepository.getBenutzer(benutzerId);

			benutzerTurniereMap.put(benutzerId, List.of());
			Optional<Turnier> turnier = turnierRepository.ladeAlleTurniere().stream().filter(t -> t.uuid().equals(turnierId)).findFirst();
			if (!turnier.isPresent()) {
				logger.info("Turnier {} existiert nicht", turnierId);
				return false;
			}
		}

		return false;
	}
}
