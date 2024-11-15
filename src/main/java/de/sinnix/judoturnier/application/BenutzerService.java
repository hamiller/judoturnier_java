package de.sinnix.judoturnier.application;

import de.sinnix.judoturnier.adapter.secondary.BenutzerRepository;
import de.sinnix.judoturnier.adapter.secondary.TurnierRepository;
import de.sinnix.judoturnier.model.Benutzer;
import de.sinnix.judoturnier.model.OidcBenutzer;
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

//	public boolean darfBenutzerTurnierSehen(UUID benutzerId, UUID turnierId) {
//		List<UUID> turnierIds = benutzerTurniereMap.get(benutzerId);
//		if (turnierIds == null) {
//			Optional<Benutzer> benutzer = benutzerRepository.findBenutzer(benutzerId);
//
//			benutzerTurniereMap.put(benutzerId, List.of());
//			Optional<Turnier> turnier = turnierRepository.ladeAlleTurniere().stream().filter(t -> t.uuid().equals(turnierId)).findFirst();
//			if (!turnier.isPresent()) {
//				logger.info("Turnier {} existiert nicht", turnierId);
//				return false;
//			}
//		}
//
//		return false;
//	}

	public Benutzer holeBenutzer(OidcBenutzer oidcBenutzer) {
		if (oidcBenutzer.username().equals(Benutzer.ANONYMOUS_USERNAME)) {
			return benutzerRepository.findBenutzerByUsername(Benutzer.ANONYMOUS_USERNAME).orElseThrow(() -> new RuntimeException("Dummy Nutzer ist nicht in der Datenbank hinterlegt!"));
		}

		// Falls konfiguriert, werden Benutzer automatisch im System angelegt
		// - das ist durch das vorgeschaltete Keycloak abgesichert, sodass nur (im Keycloak) existierende Nutzer angelegt werden
		Optional<Benutzer> benutzer = benutzerRepository.findBenutzer(oidcBenutzer.uuid());
		if (benutzer.isPresent()) {
			return benutzer.get();
		}

		Benutzer neuerBenutzer = new Benutzer(
			oidcBenutzer.uuid(),
			oidcBenutzer.username(),
			oidcBenutzer.name(),
			List.of(),
			oidcBenutzer.benutzerRollen());
		logger.info("Lege Benutzer neu an {}", neuerBenutzer);
		return benutzerRepository.save(neuerBenutzer);
	}

}
