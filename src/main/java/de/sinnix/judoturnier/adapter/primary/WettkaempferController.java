package de.sinnix.judoturnier.adapter.primary;

import de.sinnix.judoturnier.application.VereinService;
import de.sinnix.judoturnier.application.WettkaempferService;
import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Farbe;
import de.sinnix.judoturnier.model.Geschlecht;
import de.sinnix.judoturnier.model.Verein;
import de.sinnix.judoturnier.model.Wettkaempfer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.ModelAndView;

import java.util.Comparator;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class WettkaempferController {

	private static final Logger logger = LogManager.getLogger(WettkaempferController.class);

	@Autowired
	private WettkaempferService wiegenService;
	@Autowired
	private VereinService       vereinService;

	@GetMapping("/turnier/{turnierid}/wettkaempfer")
	public ModelAndView ladeWettkaempferListe(@PathVariable String turnierid, @RequestParam(name = "success", required = false) String id, @RequestParam(name = "error", required = false) String error) {
		logger.debug("Alle Wettkaempfer angefragt");
		var wks = wiegenService.alleKaempfer(UUID.fromString(turnierid)).stream()
			.sorted(Comparator.comparing(Wettkaempfer::name))
			.collect(Collectors.toList());

		ModelAndView mav = new ModelAndView("wettkaempferliste");
		mav.addObject("turnierid", turnierid);
		mav.addObject("kaempferListe", wks);
		mav.addObject("anzahlwk", wks.size());
		mav.addObject("prevsuccess", id);
		mav.addObject("preverror", error);
		return mav;
	}

	@PostMapping("/turnier/{turnierid}/wettkaempfer")
	@PreAuthorize("hasAnyRole('ROLE_AMDIN', 'ROLE_TRAINER')")
	public ModelAndView speichereWettkaempfer(@PathVariable String turnierid, @RequestBody MultiValueMap<String, String> formData) {
		logger.info("empfange Wettkaempfer {}", formData);

		UUID turnierUUID = UUID.fromString(turnierid);
		Geschlecht geschlecht = Geschlecht.valueOf(formData.getFirst("geschlecht"));
		Altersklasse altersklasse = Altersklasse.valueOf(formData.getFirst("altersklasse"));
		Verein verein = vereinService.holeVerein(Integer.parseInt(formData.getFirst("vereinsid")), turnierUUID);

		Boolean neuerEintrag = Boolean.parseBoolean(formData.getFirst("neuereintrag"));

		Wettkaempfer wettkaempfer = new Wettkaempfer(
			notEmpty(formData.getFirst("id")) ? Integer.parseInt(formData.getFirst("id")) : null,
			formData.getFirst("name"),
			geschlecht,
			altersklasse,
			verein,
			notEmpty(formData.getFirst("gewicht")) ? Double.parseDouble(formData.getFirst("gewicht")) : 0d,
			notEmpty(formData.getFirst("farbe")) ? Optional.of(Farbe.valueOf(formData.getFirst("farbe"))) : Optional.empty(),
			notEmpty(formData.getFirst("checked")) ? true : false,
			notEmpty(formData.getFirst("printed")) ? true : false,
			UUID.fromString(turnierid));

		if (wettkaempfer.name().isBlank()) {
			logger.info("Kämpfer hat keinen Namen!");
			return new ModelAndView("redirect:/turnier/" + turnierid + "/wettkaempfer-neu?error='Name fehlt'", formData);
		}

		try {
			var kaempfer = wiegenService.speichereKaempfer(wettkaempfer);
			logger.info("Kämpfer erfolgreich angelegt {}", kaempfer.id());

			if (neuerEintrag) {
				return new ModelAndView("redirect:/turnier/" + turnierid + "/wettkaempfer-neu?success=" + kaempfer.id(), formData);
			}
			return new ModelAndView("redirect:/turnier/" + turnierid + "/wettkaempfer?success=" + kaempfer.id(), formData);
		} catch (Exception err) {
			logger.error("Konnte den Kämpfer nicht anlegen!", err);
			return new ModelAndView("redirect:/turnier/" + turnierid + "/wettkaempfer-neu", formData);
		}
	}

	@DeleteMapping("/turnier/{turnierid}/wettkaempfer/{id}")
	@PreAuthorize("hasAnyRole('ROLE_AMDIN', 'ROLE_TRAINER')")
	public ModelAndView loescheWettkaempfer(@PathVariable String turnierid, @PathVariable Integer id) {
		logger.debug("lösche Wettkaempfer {}", id);
		wiegenService.loescheKaempfer(id);
		return new ModelAndView("redirect:/turnier/" + turnierid + "/wettkaempfer");
	}

	@GetMapping("/turnier/{turnierid}/wettkaempfer/{id}")
	public ModelAndView ladeWettkaempfer(@PathVariable String turnierid, @PathVariable Integer id) {
		logger.debug("Wettkaempfer-Seite angefragt " + id);
		UUID turnierUUID = UUID.fromString(turnierid);
		var wk = wiegenService.ladeKaempfer(id);
		if (!wk.isPresent()) {
			logger.warn("Wettkämper: {} nicht gefunden!");
			throw new HttpClientErrorException(HttpStatusCode.valueOf(404));
		}
		logger.info("Wettkämper: {}", wk);
		var vs = vereinService.holeAlleVereine(turnierUUID).stream()
			.sorted(Comparator.comparing(Verein::name))
			.collect(Collectors.toList());

		ModelAndView mav = new ModelAndView("wettkaempfer");
		mav.addObject("turnierid", turnierid);
		mav.addObject("kaempfer", wk.get());
		mav.addObject("vereine", vs);
		mav.addObject("geschlechter", Geschlecht.values());
		mav.addObject("altersklasse", Altersklasse.values());
		mav.addObject("neuerEintrag", false);
		return mav;
	}

	@GetMapping("/turnier/{turnierid}/wettkaempfer-neu")
	public ModelAndView leererWettkaempfer(@PathVariable String turnierid, @RequestParam(name = "success", required = false) String id, @RequestParam(name = "error", required = false) String error) {
		logger.debug("Wettkaempfer-Seite");
		UUID turnierUUID = UUID.fromString(turnierid);
		var vs = vereinService.holeAlleVereine(turnierUUID);

		ModelAndView mav = new ModelAndView("wettkaempfer");
		mav.addObject("turnierid", turnierid);
		mav.addObject("kaempfer", null);
		mav.addObject("vereine", vs);
		mav.addObject("geschlechter", Geschlecht.values());
		mav.addObject("altersklasse", Altersklasse.values());
		mav.addObject("prevsuccess", id);
		mav.addObject("preverror", error);
		mav.addObject("neuerEintrag", true);
		return mav;
	}

	private static boolean notEmpty(String entry) {
		return entry != null && !entry.isBlank();
	}
}
