package de.sinnix.judoturnier.adapter.primary;

import de.sinnix.judoturnier.application.TurnierService;
import de.sinnix.judoturnier.model.Begegnung;
import de.sinnix.judoturnier.model.Bewerter;
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

@RestController
public class WertungController {

	private static final Logger logger = LogManager.getLogger(WertungController.class);

	@Autowired
	private TurnierService turnierService;

	@GetMapping("/turnier/{turnierid}/begegnungen/randori/{id}")
	public ModelAndView begegnungRandori(@PathVariable String turnierid, @PathVariable String id) {
		logger.info("Lade Wertung für Begegnung {}", id);
		Bewerter bewerter = HelperSource.extractBewerter(SecurityContextHolder.getContext().getAuthentication());
		Begegnung begegnung = turnierService.ladeBegegnung(Integer.parseInt(id));
		BegegnungDto begegnungDto = convertFromBegegnung(begegnung, bewerter.id());

		ModelAndView mav = new ModelAndView("wettkampf_randori");
		mav.addObject("turnierid", turnierid);
		mav.addObject("begegnung", begegnungDto);
		mav.addObject("begegnungid", id);
		mav.addObject("bewerter", bewerter);
		mav.addObject("enableEditing", bewerter.darfEditieren());
		return mav;
	}

	private BegegnungDto convertFromBegegnung(Begegnung begegnung, String userid) {
		var begegnungId = begegnung.getBegegnungId();
		var	wettkaempfer1 = begegnung.getWettkaempfer1();
		var	wettkaempfer2 = begegnung.getWettkaempfer2();
		var kampfrichterWertung = begegnung.getWertungen().stream().filter(w -> w.getBewerter().id().equals(userid)).findFirst();
		return new BegegnungDto(begegnungId, wettkaempfer1, wettkaempfer2, kampfrichterWertung, begegnung.getWertungen());
	}

	@PostMapping("/turnier/{turnierid}/begegnungen/randori/{begegnungId}")
	@PreAuthorize("hasRole('ROLE_KAMPFRICHTER')")
	public ModelAndView speichereBegegnungRandori(@PathVariable String turnierid, @PathVariable String begegnungId, @RequestBody MultiValueMap<String, String> formData) {
		logger.info("Speichere Wertung für Begegnung {}: {}", begegnungId, formData);

		Bewerter bewerter = HelperSource.extractBewerter(SecurityContextHolder.getContext().getAuthentication());

		var kampfgeist1 = Integer.parseInt(formData.get("kampfgeist1").getFirst());
		var technik1 = Integer.parseInt(formData.get("technik1").getFirst());
		var stil1 = Integer.parseInt(formData.get("stil1").getFirst());
		var fairness1 = Integer.parseInt(formData.get("fairness1").getFirst());
		var kampfgeist2 = Integer.parseInt(formData.get("kampfgeist2").getFirst());
		var technik2 = Integer.parseInt(formData.get("technik2").getFirst());
		var stil2 = Integer.parseInt(formData.get("stil2").getFirst());
		var fairness2 = Integer.parseInt(formData.get("fairness2").getFirst());

		turnierService.speichereRandoriWertung(begegnungId, kampfgeist1, technik1, stil1, fairness1, kampfgeist2, technik2, stil2, fairness2, bewerter.id());
		return new ModelAndView("redirect:/turnier/" + turnierid + "/begegnungen/randori");
	}
}