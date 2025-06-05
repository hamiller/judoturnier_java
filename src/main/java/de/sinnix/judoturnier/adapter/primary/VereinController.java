package de.sinnix.judoturnier.adapter.primary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import java.util.UUID;


import de.sinnix.judoturnier.application.VereinService;
import de.sinnix.judoturnier.model.OidcBenutzer;
import de.sinnix.judoturnier.model.Verein;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RestController
@RequestMapping(VereinController.CONTROLLER_URI)
public class VereinController {
	public static final String CONTROLLER_URI    = "/turnier/{turnierid}/vereine";
	public static final String VEREIN_URI        = "/verein/{vereinid}";
	public static final String CREATE_VEREIN_URI = "/verein-neu";

	private static final Logger logger = LogManager.getLogger(VereinController.class);

	@Autowired
	private VereinService vereinService;

	@GetMapping
	public ModelAndView ladeVereineListe(@PathVariable String turnierid, @RequestParam(name = "success", required = false) String id,
										 @RequestParam(name = "error", required = false) String error) {
		logger.info("Lade Vereine...");
		OidcBenutzer oidcBenutzer = HelperSource.extractOidcBenutzer(SecurityContextHolder.getContext().getAuthentication());
		UUID turnierUUID = UUID.fromString(turnierid);
		var vereine = vereinService.holeAlleVereine(turnierUUID).toArray(Verein[]::new);

		ModelAndView mav = new ModelAndView("vereineliste");
		mav.addObject("turnierid", turnierid);
		mav.addObject("isadmin", oidcBenutzer.istAdmin());
		mav.addObject("isloggedin", oidcBenutzer.isLoggedin());
		mav.addObject("vereine", vereine);
		mav.addObject("anzahlvereine", vereine.length);
		mav.addObject("prevsuccess", id);
		mav.addObject("preverror", error);
		return mav;
	}

	@GetMapping(VEREIN_URI)
	public ModelAndView verein(@PathVariable String turnierid, @PathVariable String vereinid) {
		logger.info("Lade Verein {}", vereinid);
		OidcBenutzer oidcBenutzer = HelperSource.extractOidcBenutzer(SecurityContextHolder.getContext().getAuthentication());
		UUID turnierUUID = UUID.fromString(turnierid);
		UUID vereinID = UUID.fromString(vereinid);
		var verein = vereinService.holeVerein(vereinID, turnierUUID);

		ModelAndView mav = new ModelAndView("verein");
		mav.addObject("turnierid", turnierid);
		mav.addObject("isadmin", oidcBenutzer.istAdmin());
		mav.addObject("isloggedin", oidcBenutzer.isLoggedin());
		mav.addObject("vereinid", verein.id());
		mav.addObject("name", verein.name());
		mav.addObject("neuerEintrag", false);
		return mav;
	}

	@PostMapping
	@PreAuthorize("hasAnyRole('ROLE_AMDIN', 'ROLE_TRAINER')")
	public ModelAndView speichereVerein(@PathVariable String turnierid, @RequestBody MultiValueMap<String, String> formData) {
		logger.info("empfange Verein {}", formData);

		UUID turnierUUID = UUID.fromString(turnierid);
		String name = formData.getFirst("name");
		Boolean neuerEintrag = Boolean.parseBoolean(formData.getFirst("neuereintrag"));

		try {
			Verein v = new Verein(
				notEmpty(formData.getFirst("id")) ? UUID.fromString(formData.getFirst("id")) : null,
				name.strip(),
				turnierUUID);

			v = vereinService.speichereVerein(v);
			logger.info("Verein erfolgreich angelegt {}", v.id());

			if (neuerEintrag) {
				return new ModelAndView("redirect:/turnier/" + turnierid + "/vereine/verein-neu?success=" + v.id(), formData);
			}
			return new ModelAndView("redirect:/turnier/" + turnierid + "/vereine?success=" + v.id(), formData);
		} catch (Exception err) {
			logger.error("Konnte den Verein nicht anlegen!", err);
			return new ModelAndView("redirect:/turnier/" + turnierid + "/vereine/verein-neu", formData);
		}
	}

	@DeleteMapping(VEREIN_URI)
	@PreAuthorize("hasAnyRole('ROLE_AMDIN', 'ROLE_TRAINER')")
	public void loescheVerein(@PathVariable String turnierid, @PathVariable String vereinid) {
		logger.debug("lösche Verein {} für Turnier {}", vereinid, turnierid);
		vereinService.loescheVerein(UUID.fromString(vereinid), UUID.fromString(turnierid));
	}

	@GetMapping(CREATE_VEREIN_URI)
	public ModelAndView leererVerein(@PathVariable String turnierid, @RequestParam(name = "success", required = false) String id, @RequestParam(name = "error", required = false) String error) {
		logger.debug("Verein-Seite");
		OidcBenutzer oidcBenutzer = HelperSource.extractOidcBenutzer(SecurityContextHolder.getContext().getAuthentication());

		ModelAndView mav = new ModelAndView("verein");
		mav.addObject("turnierid", turnierid);
		mav.addObject("isadmin", oidcBenutzer.istAdmin());
		mav.addObject("isloggedin", oidcBenutzer.isLoggedin());
		mav.addObject("vereinid", null);
		mav.addObject("name", null);
		mav.addObject("prevsuccess", id);
		mav.addObject("preverror", error);
		mav.addObject("neuerEintrag", true);
		return mav;
	}

	private static boolean notEmpty(String entry) {
		return entry != null && !entry.isBlank();
	}
}
