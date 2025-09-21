package de.sinnix.judoturnier.adapter.primary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;


import de.sinnix.judoturnier.adapter.primary.dto.BenutzerZuordnungDto;
import de.sinnix.judoturnier.adapter.primary.dto.TurnierDto;
import de.sinnix.judoturnier.application.BenutzerService;
import de.sinnix.judoturnier.application.TurnierService;
import de.sinnix.judoturnier.model.Benutzer;
import de.sinnix.judoturnier.model.OidcBenutzer;
import de.sinnix.judoturnier.model.Turnier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RestController
public class TurnierVerwaltungController {
	private static final Logger logger = LogManager.getLogger(TurnierVerwaltungController.class);

	@Autowired
	private TurnierService         turnierService;
	@Autowired
	private BuildProperties        buildProperties;
	@Autowired
	private BenutzerService        benutzerService;

	@GetMapping("/turnierverwaltung")
	public ModelAndView turnierverwaltung() {
		logger.debug("lade vorhandene Turniere");
		OidcBenutzer oidcBenutzer = HelperSource.extractOidcBenutzer(SecurityContextHolder.getContext().getAuthentication());
		logger.info("Eingeloggter User {}", oidcBenutzer);

		List<Turnier> turniere = new ArrayList<>();
		List<Benutzer> benutzerList;
		if (oidcBenutzer.istAdmin()) {
			turniere = turnierService.ladeAlleTurniere();
		 	benutzerList = benutzerService.holeAlleBenutzer();
		} else {
			benutzerList = new ArrayList<>();
		}

		List<TurnierDto> turnierDtoList = turniere.stream().map(turnier -> {
			var benutzerDtoList = benutzerList.stream().map(b -> DtosConverter.convertFromBenutzer(b, turnier.uuid())).toList();
			return DtosConverter.convertFromTurnier(turnier, benutzerDtoList);
		}).toList();

		logger.info("benutzerList {}", benutzerList);

		ModelAndView mav = new ModelAndView("turniere");
		mav.addObject("turniere", turnierDtoList);
		mav.addObject("isadmin", oidcBenutzer.istAdmin());
		mav.addObject("isloggedin", oidcBenutzer.isLoggedin());
		mav.addObject("anzahlturniere", turniere.size());
		mav.addObject("enableEditing", oidcBenutzer.istAdmin());
		mav.addObject("software_version", buildProperties.getVersion());
		mav.addObject("software_zeit", buildProperties.getTime());

		return mav;
	}

	@PostMapping("/turnierverwaltung")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ModelAndView erstelleTurnier(@RequestBody MultiValueMap<String, String> formData) {
		logger.debug("erstelle ein neues Turnier {}", formData);
		OidcBenutzer benutzer = HelperSource.extractOidcBenutzer(SecurityContextHolder.getContext().getAuthentication());

		var name = formData.get("name").getFirst();
		var ort = formData.get("ort").getFirst();
		var datum = formData.get("datum").getFirst();

		Turnier turnier = turnierService.erstelleTurnier(name, ort, datum);

		return new ModelAndView("redirect:/turnier/" + turnier.uuid().toString());
	}

	@PostMapping("/turnierverwaltung/{turnierid}/benutzer")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ModelAndView speichereBenutzerZuTurnier(@PathVariable String turnierid, @RequestBody Map<String, BenutzerZuordnungDto> formData) {
		logger.debug("Speichere Benutzer zu Turnier {}: {}", turnierid, formData);
		OidcBenutzer benutzer = HelperSource.extractOidcBenutzer(SecurityContextHolder.getContext().getAuthentication());
		UUID turnierUUID = UUID.fromString(turnierid);

		formData.forEach((userid, userData) -> {
			logger.debug("UserID: {}, Daten: {}", userid, userData);
		});

		if (!benutzer.istAdmin()) {
			return new ModelAndView("redirect:/turnier/" + turnierid + "?error=Unerlaubter_Vorgang");
		}

		List<UUID> benutzerDesTurniers = formData.values().stream()
			.filter(bz -> bz.zugeordnetZuTurnier())
			.map(bz -> UUID.fromString(bz.userid()))
			.collect(Collectors.toList());
		benutzerService.ordneBenutzerZuTurnier(benutzerDesTurniers, turnierUUID);

		List<UUID> benutzerNichtDesTurniers = formData.values().stream()
			.filter(bz -> !bz.zugeordnetZuTurnier())
			.map(bz -> UUID.fromString(bz.userid()))
			.collect(Collectors.toList());
		benutzerService.entferneBenutzerVonTurnier(benutzerNichtDesTurniers, turnierUUID);

		return new ModelAndView("redirect:/turnier/" + turnierid);
	}

}
