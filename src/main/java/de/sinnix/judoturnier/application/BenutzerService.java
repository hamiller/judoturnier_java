package de.sinnix.judoturnier.application;

import de.sinnix.judoturnier.adapter.secondary.BenutzerRepository;
import de.sinnix.judoturnier.adapter.secondary.TurnierRollenJpaRepository;
import de.sinnix.judoturnier.model.Benutzer;
import de.sinnix.judoturnier.model.OidcBenutzer;
import de.sinnix.judoturnier.model.TurnierRollen;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class BenutzerService {
	private static final Logger logger = LogManager.getLogger(BenutzerService.class);

	@Autowired
	private BenutzerRepository         benutzerRepository;
	@Autowired
	private TurnierRollenJpaRepository turnierRollenJpaRepository;

	@Transactional
	public Benutzer holeBenutzer(OidcBenutzer oidcBenutzer) {
		validiereOidcBenutzerFuerSuche(oidcBenutzer);

		if (Benutzer.ANONYMOUS_USERNAME.equals(oidcBenutzer.username())) {
			return benutzerRepository.findBenutzerByUsername(Benutzer.ANONYMOUS_USERNAME).orElseThrow(() -> new RuntimeException("Dummy Nutzer ist nicht in der Datenbank hinterlegt!"));
		}

		// TODO: Falls konfiguriert, werden Benutzer automatisch im System angelegt
		// - das ist durch das vorgeschaltete Keycloak abgesichert, sodass nur (im Keycloak) existierende Nutzer angelegt werden
		Optional<Benutzer> benutzer = benutzerRepository.findBenutzerByUsername(oidcBenutzer.username());
		if (benutzer.isPresent()) {
			return benutzer.get();
		}

		validiereOidcBenutzerFuerAnlage(oidcBenutzer);

		logger.warn("TODO: make configureable/ausschaltbar to auto add new users");

		Benutzer neuerBenutzer = new Benutzer(
			oidcBenutzer.uuid(),
			oidcBenutzer.username(),
			oidcBenutzer.name(),
			List.of(),
			oidcBenutzer.benutzerRollen());
		logger.info("Lege Benutzer neu an {}", neuerBenutzer);
		return benutzerRepository.save(neuerBenutzer);
	}

	private void validiereOidcBenutzerFuerSuche(OidcBenutzer oidcBenutzer) {
		if (oidcBenutzer == null) {
			throw new IllegalArgumentException("Benutzer kann nicht geladen werden: OIDC-Daten fehlen.");
		}
		if (isBlank(oidcBenutzer.username())) {
			throw new IllegalArgumentException("Benutzer kann nicht geladen werden: OIDC-Daten unvollständig (username). Bitte Keycloak-Mapper und Benutzerprofil prüfen.");
		}
	}

	private void validiereOidcBenutzerFuerAnlage(OidcBenutzer oidcBenutzer) {
		List<String> fehlendeFelder = new ArrayList<>();
		if (oidcBenutzer.uuid() == null) {
			fehlendeFelder.add("uuid");
		}
		if (isBlank(oidcBenutzer.name())) {
			fehlendeFelder.add("name");
		}
		if (oidcBenutzer.benutzerRollen() == null || oidcBenutzer.benutzerRollen().isEmpty() || oidcBenutzer.benutzerRollen().stream().anyMatch(Objects::isNull)) {
			fehlendeFelder.add("rollen");
		}

		if (!fehlendeFelder.isEmpty()) {
			throw new IllegalArgumentException("Benutzer kann nicht automatisch angelegt werden: Keycloak/OIDC-Daten unvollständig (" + String.join(", ", fehlendeFelder) + "). Bitte Keycloak-Mapper und Benutzerprofil prüfen.");
		}
	}

	private boolean isBlank(String value) {
		return value == null || value.isBlank();
	}

	@Transactional
	public List<Benutzer> holeAlleBenutzer() {
		logger.info("Lade alle Benutzer");
		return benutzerRepository.findAll();
	}

	@Transactional
	public void ordneBenutzerZuTurnier(List<UUID> benutzerIds, UUID turnierUUID) {
		logger.info("Ordne Benutzer [{}] zu Turnier {}", benutzerIds, turnierUUID);
		for (UUID userId : benutzerIds) {
			var rollen = benutzerRepository.findBenutzer(userId).map(Benutzer::benutzerRollen).orElseThrow();
			TurnierRollen turnierRollen = new TurnierRollen(null, turnierUUID, rollen);
			benutzerRepository.addTurnierRollen(userId, turnierRollen, turnierUUID);
		}
	}

	@Transactional
	public void entferneBenutzerVonTurnier(List<UUID> benutzerIds, UUID turnierUUID) {
		logger.info("Entferne Benutzer [{}] von Turnier {}", benutzerIds, turnierUUID);
		for (UUID userId : benutzerIds) {
			benutzerRepository.deleteTurnierRollen(userId, turnierUUID);
		}
	}
}
