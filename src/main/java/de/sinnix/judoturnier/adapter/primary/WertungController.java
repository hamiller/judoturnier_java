package de.sinnix.judoturnier.adapter.primary;

import de.sinnix.judoturnier.application.TurnierService;
import de.sinnix.judoturnier.model.Begegnung;
import de.sinnix.judoturnier.model.Benutzer;
import de.sinnix.judoturnier.model.Metadaten;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
public class WertungController {

	private static final Logger logger = LogManager.getLogger(WertungController.class);

	@Autowired
	private TurnierService turnierService;

	@GetMapping("/turnier/{turnierid}/begegnungen/randori/{id}")
	public ModelAndView begegnungRandori(@PathVariable String turnierid, @PathVariable String id) {
		logger.info("Lade Wertung für Begegnung {}", id);
		Integer begegnungId = Integer.parseInt(id);
		Benutzer benutzer = HelperSource.extractBewerter(SecurityContextHolder.getContext().getAuthentication());
		Begegnung begegnung = turnierService.ladeBegegnung(begegnungId);

		Metadaten metadaten = turnierService.ladeMetadaten(begegnungId, UUID.fromString(turnierid));
		BegegnungDto begegnungDto = convertFromBegegnung(begegnung, benutzer.id(), metadaten.vorherigeBegegnungId(), metadaten.nachfolgendeBegegnungId());

		ModelAndView mav = new ModelAndView("wettkampf_randori");
		mav.addObject("turnierid", turnierid);
		mav.addObject("begegnung", begegnungDto);
		mav.addObject("begegnungid", id);
		mav.addObject("bewerter", benutzer);
		mav.addObject("enableEditing", benutzer.istKampfrichter());
		mav.addObject("wertungsOptionen", List.of(1, 2, 3, 4, 5, 6));
		return mav;
	}

	@GetMapping("/turnier/{turnierid}/begegnungen/normal/{id}")
	public ModelAndView begegnungTurnier(@PathVariable String turnierid, @PathVariable String id) {
		logger.info("Lade Wertung für Begegnung {}", id);
		Benutzer benutzer = HelperSource.extractBewerter(SecurityContextHolder.getContext().getAuthentication());
		Begegnung begegnung = turnierService.ladeBegegnung(Integer.parseInt(id));
		BegegnungDto begegnungDto = convertFromBegegnung(begegnung, benutzer.id(), null, null);

		ModelAndView mav = new ModelAndView("wettkampf_normal");
		mav.addObject("turnierid", turnierid);
		mav.addObject("begegnung", begegnungDto);
		mav.addObject("begegnungid", id);
		mav.addObject("bewerter", benutzer);
		mav.addObject("enableEditing", benutzer.istKampfrichter());
		mav.addObject("wertungsOptionen", List.of(1, 2, 3, 4, 5, 6));
		return mav;
	}

	private BegegnungDto convertFromBegegnung(Begegnung begegnung, String userid, Optional<Integer> vorherigeBegegnungId, Optional<Integer> nachfolgendeBegegnungId) {
		var begegnungId = begegnung.getBegegnungId();
		var	wettkaempfer1 = begegnung.getWettkaempfer1();
		var	wettkaempfer2 = begegnung.getWettkaempfer2();
		var kampfrichterWertung = begegnung.getWertungen().stream().filter(w -> w.getBewerter().id().equals(userid)).findFirst();
		var vorher = vorherigeBegegnungId.map(id -> String.valueOf(id)).orElseGet(() -> "");
		var nachher = nachfolgendeBegegnungId.map(id -> String.valueOf(id)).orElseGet(() -> "");
		return new BegegnungDto(begegnungId, wettkaempfer1, wettkaempfer2, kampfrichterWertung, begegnung.getWertungen(), vorher, nachher);
	}

	@PostMapping("/turnier/{turnierid}/begegnungen/randori/{begegnungId}")
	@PreAuthorize("hasRole('ROLE_KAMPFRICHTER')")
	public ModelAndView speichereBegegnungRandori(@PathVariable String turnierid, @PathVariable String begegnungId, @RequestBody MultiValueMap<String, String> formData) {
		logger.info("Speichere Wertung für Begegnung {}: {}", begegnungId, formData);

		Benutzer benutzer = HelperSource.extractBewerter(SecurityContextHolder.getContext().getAuthentication());

		var kampfgeist1 = Integer.parseInt(formData.get("kampfgeist1").getFirst());
		var technik1 = Integer.parseInt(formData.get("technik1").getFirst());
		var stil1 = Integer.parseInt(formData.get("stil1").getFirst());
		var fairness1 = Integer.parseInt(formData.get("fairness1").getFirst());
		var kampfgeist2 = Integer.parseInt(formData.get("kampfgeist2").getFirst());
		var technik2 = Integer.parseInt(formData.get("technik2").getFirst());
		var stil2 = Integer.parseInt(formData.get("stil2").getFirst());
		var fairness2 = Integer.parseInt(formData.get("fairness2").getFirst());

		turnierService.speichereRandoriWertung(begegnungId, kampfgeist1, technik1, stil1, fairness1, kampfgeist2, technik2, stil2, fairness2, benutzer.id());
		return new ModelAndView("redirect:/turnier/" + turnierid + "/begegnungen/randori");
	}

	@PostMapping("/turnier/{turnierid}/begegnungen/normal/{begegnungId}")
	@PreAuthorize("hasRole('ROLE_KAMPFRICHTER')")
	public ModelAndView speichereBegegnungTurnier(@PathVariable String turnierid, @PathVariable String begegnungId, @RequestBody MultiValueMap<String, String> formData) {
		logger.info("Speichere Wertung für Begegnung {}: {}", begegnungId, formData);

		Benutzer benutzer = HelperSource.extractBewerter(SecurityContextHolder.getContext().getAuthentication());

		var scoreWeiss = Integer.parseInt(formData.get("score_weiss").getFirst());
		var penaltiesWeiss = Integer.parseInt(formData.get("penalties_weiss").getFirst());
		var scoreBlau = Integer.parseInt(formData.get("score_blau").getFirst());
		var penaltiesBlau = Integer.parseInt(formData.get("penalties_blau").getFirst());
		var fightTime = Integer.parseInt(formData.get("fightTime").getFirst());
		var sieger = Integer.parseInt(formData.get("sieger").getFirst());

		turnierService.speichereTurnierWertung(begegnungId, scoreWeiss, scoreBlau, penaltiesWeiss, penaltiesBlau, fightTime, sieger, benutzer.id());
		return new ModelAndView("redirect:/turnier/" + turnierid + "/begegnungen/normal");
	}
}
