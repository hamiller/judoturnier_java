package de.sinnix.judoturnier.adapter.primary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import java.util.List;
import java.util.UUID;


import de.sinnix.judoturnier.adapter.primary.dto.KontaktFormularDto;
import de.sinnix.judoturnier.application.BenutzerService;
import de.sinnix.judoturnier.application.TurnierService;
import de.sinnix.judoturnier.model.Benutzer;
import de.sinnix.judoturnier.model.OidcBenutzer;
import de.sinnix.judoturnier.model.Turnier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RestController
public class BaseController {
	public static final String BASE_URI    = "/";
	public static final String KONTAKT_URI = "/kontakt";

	private static final Logger logger = LogManager.getLogger(BaseController.class);

	@Autowired
	private BenutzerService benutzerService;
	@Autowired
	private TurnierService  turnierService;

	@GetMapping(BASE_URI)
	public ModelAndView startPage() {
		OidcBenutzer oidcBenutzer = HelperSource.extractOidcBenutzer(SecurityContextHolder.getContext().getAuthentication());
		logger.info("Turniere-Startseite. User {}", oidcBenutzer);

		Benutzer benutzer = benutzerService.holeBenutzer(oidcBenutzer);
		List<UUID> turnierIds = benutzer.turnierRollen().stream().map(tr -> tr.turnierId()).toList();
		List<Turnier> turniere = turnierService.ladeTurniere(turnierIds);
		logger.info("Zu Nutzer {} gehören folgende Turniere: {}", benutzer.uuid(), turniere);

		ModelAndView mav = new ModelAndView("startseite");
		mav.addObject("turniere", turniere);
		mav.addObject("isadmin", oidcBenutzer.istAdmin());
		mav.addObject("isloggedin", oidcBenutzer.isLoggedin());
		mav.addObject("username", benutzer.username());
		return mav;
	}

	@GetMapping(KONTAKT_URI)
	public ModelAndView kontaktPage() {
		OidcBenutzer oidcBenutzer = HelperSource.extractOidcBenutzer(SecurityContextHolder.getContext().getAuthentication());
		logger.info("Turniere-Kontaktseite. User {}", oidcBenutzer);

		ModelAndView mav = new ModelAndView("kontakt");
		mav.addObject("isloggedin", oidcBenutzer.isLoggedin());
		return mav;
	}

	@PostMapping(KONTAKT_URI)
	public ResponseEntity<String> kontaktSenden(@RequestBody KontaktFormularDto kontaktFormular) {
		// Validierung oder Logik hier
		if (kontaktFormular.getName().isEmpty() || kontaktFormular.getEmail().isEmpty() || kontaktFormular.getMessage().isEmpty()) {
			return ResponseEntity.badRequest().body("Alle Felder müssen ausgefüllt sein.");
		}

		logger.info("Erhalte Kontakanfrage: {}", kontaktFormular);
		// TODO
		// Beispiel: Nachricht speichern oder verarbeiten
		// saveMessage(kontaktFormular);

		// Erfolgsantwort zurückgeben
		return ResponseEntity.ok("Vielen Dank für Ihre Nachricht!");
	}
}
