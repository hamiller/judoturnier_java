package de.sinnix.judoturnier.adapter.primary;

import de.sinnix.judoturnier.application.BenutzerService;
import de.sinnix.judoturnier.application.EinstellungenService;
import de.sinnix.judoturnier.application.TurnierService;
import de.sinnix.judoturnier.application.WertungService;
import de.sinnix.judoturnier.application.WettkampfService;
import de.sinnix.judoturnier.model.Begegnung;
import de.sinnix.judoturnier.model.Benutzer;
import de.sinnix.judoturnier.model.Metadaten;
import de.sinnix.judoturnier.model.OidcBenutzer;
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
import java.util.UUID;

@RestController
public class WertungController {

	private static final Logger logger = LogManager.getLogger(WertungController.class);

	@Autowired
	private TurnierService       turnierService;
	@Autowired
	private BenutzerService      benutzerService;
	@Autowired
	private EinstellungenService einstellungenService;
	@Autowired
	private WettkampfService     wettkampfService;
	@Autowired
	private WertungService       wertungService;

	@GetMapping("/turnier/{turnierid}/begegnungen/randori/{id}")
	public ModelAndView begegnungRandori(@PathVariable String turnierid, @PathVariable String id) {
		logger.info("Lade Wertung für Begegnung {}", id);
		UUID begegnungId = UUID.fromString(id);
		UUID turnierId = UUID.fromString(turnierid);
		OidcBenutzer oidcBenutzer = HelperSource.extractOidcBenutzer(SecurityContextHolder.getContext().getAuthentication());
		Benutzer benutzer = benutzerService.holeBenutzer(oidcBenutzer);
		Begegnung begegnung = wettkampfService.ladeBegegnung(begegnungId);

		Metadaten metadaten = turnierService.ladeMetadaten(begegnungId, turnierId);
		BegegnungDto begegnungDto = DtosConverter.convertFromBegegnung(begegnung, benutzer.uuid(), metadaten.vorherigeBegegnungId(), metadaten.nachfolgendeBegegnungId());

		ModelAndView mav = new ModelAndView("wettkampf_randori");
		mav.addObject("turnierid", turnierid);
		mav.addObject("isadmin", oidcBenutzer.istAdmin());
		mav.addObject("begegnung", begegnungDto);
		mav.addObject("begegnungid", id);
		mav.addObject("bewerter", benutzer);
		mav.addObject("enableEditing", benutzer.istKampfrichter(turnierId));
		mav.addObject("wertungsOptionen", List.of(1, 2, 3, 4, 5, 6));
		return mav;
	}

	@GetMapping("/turnier/{turnierid}/begegnungen/normal/{id}")
	public ModelAndView begegnungTurnier(@PathVariable String turnierid, @PathVariable String id) {
		logger.info("Lade Wertung für Begegnung {}", id);
		UUID begegnungId = UUID.fromString(id);
		UUID turnierUUID = UUID.fromString(turnierid);
		OidcBenutzer oidcBenutzer = HelperSource.extractOidcBenutzer(SecurityContextHolder.getContext().getAuthentication());
		Benutzer benutzer = benutzerService.holeBenutzer(oidcBenutzer);
		Begegnung begegnung = wettkampfService.ladeBegegnung(begegnungId);
		Integer kampfzeit = einstellungenService.kampfZeit(turnierUUID, begegnung.getWettkampfGruppe().altersklasse());
		Integer matteid = begegnung.getMatteId();
		Integer mattenrunde = begegnung.getMattenRunde();

		Metadaten metadaten = turnierService.ladeMetadaten(begegnungId, turnierUUID);
		BegegnungDto begegnungDto = DtosConverter.convertFromBegegnung(begegnung, benutzer.uuid(), metadaten.vorherigeBegegnungId(), metadaten.nachfolgendeBegegnungId());

		ModelAndView mav = new ModelAndView("wettkampf_normal");
		mav.addObject("turnierid", turnierid);
		mav.addObject("isadmin", oidcBenutzer.istAdmin());
		mav.addObject("begegnung", begegnungDto);
		mav.addObject("begegnungid", id);
		mav.addObject("bewerter", benutzer);
		mav.addObject("enableEditing", benutzer.istKampfrichter(turnierUUID));
		mav.addObject("wertungsOptionen", List.of(1, 2, 3, 4, 5, 6));
		mav.addObject("kampfzeit", kampfzeit);
		mav.addObject("matteid", matteid);
		mav.addObject("mattenrunde", mattenrunde);
		return mav;
	}

	@PostMapping("/turnier/{turnierid}/begegnungen/randori/{id}")
	@PreAuthorize("hasRole('ROLE_KAMPFRICHTER')")
	public ModelAndView speichereBegegnungRandori(@PathVariable String turnierid, @PathVariable String id, @RequestBody MultiValueMap<String, String> formData) {
		logger.info("Speichere Wertung für Begegnung {}: {}", id, formData);

		OidcBenutzer benutzer = HelperSource.extractOidcBenutzer(SecurityContextHolder.getContext().getAuthentication());
		UUID begegnungId = UUID.fromString(id);

		var kampfgeist1 = Integer.parseInt(formData.get("kampfgeist1").getFirst());
		var technik1 = Integer.parseInt(formData.get("technik1").getFirst());
		var stil1 = Integer.parseInt(formData.get("stil1").getFirst());
		var vielfalt1 = Integer.parseInt(formData.get("vielfalt1").getFirst());
		var kampfgeist2 = Integer.parseInt(formData.get("kampfgeist2").getFirst());
		var technik2 = Integer.parseInt(formData.get("technik2").getFirst());
		var stil2 = Integer.parseInt(formData.get("stil2").getFirst());
		var vielfalt2 = Integer.parseInt(formData.get("vielfalt2").getFirst());

		wertungService.speichereRandoriWertung(begegnungId, kampfgeist1, technik1, stil1, vielfalt1, kampfgeist2, technik2, stil2, vielfalt2, benutzer.uuid());
		return new ModelAndView("redirect:/turnier/" + turnierid + "/begegnungen/randori");
	}

	@PostMapping("/turnier/{turnierid}/begegnungen/normal/{id}")
	@PreAuthorize("hasRole('ROLE_KAMPFRICHTER')")
	public ModelAndView speichereBegegnungTurnier(@PathVariable String turnierid, @PathVariable String id, @RequestBody MultiValueMap<String, String> formData) {
		logger.info("Speichere Wertung für Begegnung {}: {}", id, formData);

		OidcBenutzer benutzer = HelperSource.extractOidcBenutzer(SecurityContextHolder.getContext().getAuthentication());
		UUID begegnungId = UUID.fromString(id);

		var scoreWeiss = Integer.parseInt(formData.get("score_weiss").getFirst());
		var penaltiesWeiss = Integer.parseInt(formData.get("penalties_weiss").getFirst());
		var scoreBlau = Integer.parseInt(formData.get("score_blau").getFirst());
		var penaltiesBlau = Integer.parseInt(formData.get("penalties_blau").getFirst());
		var fightTime = formData.get("fightTime").getFirst();
		var sieger = UUID.fromString(formData.get("sieger").getFirst());

		wertungService.speichereTurnierWertung(begegnungId, scoreWeiss, scoreBlau, penaltiesWeiss, penaltiesBlau, fightTime, sieger, benutzer.uuid());
		return new ModelAndView("redirect:/turnier/" + turnierid + "/begegnungen/normal");
	}
}
