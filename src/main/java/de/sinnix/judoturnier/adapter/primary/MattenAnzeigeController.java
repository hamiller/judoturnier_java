package de.sinnix.judoturnier.adapter.primary;

import de.sinnix.judoturnier.application.EinstellungenService;
import de.sinnix.judoturnier.application.TurnierService;
import de.sinnix.judoturnier.application.WettkampfService;
import de.sinnix.judoturnier.model.Begegnung;
import de.sinnix.judoturnier.model.Einstellungen;
import de.sinnix.judoturnier.model.OidcBenutzer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@RestController
public class MattenAnzeigeController {
	private static final Logger logger = LogManager.getLogger(MattenAnzeigeController.class);

	@Autowired
	private TurnierService       turnierService;
	@Autowired
	private EinstellungenService einstellungenService;
	@Autowired
	private WettkampfService     wettkampfService;

	@GetMapping("/turnier/{turnierid}/mattenanzeige/randori/{matte}/{mattenrunde}")
	public ModelAndView anzeigeMattenWertungRandori(@PathVariable String turnierid, @PathVariable String matte, @PathVariable String mattenrunde) {
		logger.info("Öffne Anzeige der Runde {} auf Matte {}", mattenrunde, matte);
		OidcBenutzer oidcBenutzer = HelperSource.extractOidcBenutzer(SecurityContextHolder.getContext().getAuthentication());

		UUID turnierUUID = UUID.fromString(turnierid);
		Einstellungen einstellungen = einstellungenService.ladeEinstellungen(turnierUUID);
		List<Begegnung> begegnungen = turnierService.ladeMattenRunde(turnierUUID, Integer.parseInt(matte), Integer.parseInt(mattenrunde));
		List<BegegnungDto> begegnungDtos = begegnungen.stream().map(b -> DtosConverter.convertFromBegegnung(b)).toList();
		var altersklasse = begegnungen.stream().findFirst().flatMap(b -> b.getWettkaempfer1().map(wk -> wk.altersklasse()));
		Integer kampfzeit = einstellungenService.kampfZeit(turnierUUID, altersklasse.get());

		logger.debug("kampfzeit {}", kampfzeit);

		ModelAndView mav = new ModelAndView("mattenanzeige_randori");
		mav.addObject("turnierid", turnierid);
		mav.addObject("isloggedin", oidcBenutzer.isLoggedin());
		mav.addObject("matte", matte);
		mav.addObject("mattenrunde", mattenrunde);
		mav.addObject("begegnungen", begegnungDtos);
		mav.addObject("kampfzeit", kampfzeit);
		return mav;
	}

	@GetMapping("/turnier/{turnierid}/mattenanzeige/normal/{begegnungid}")
	public ModelAndView anzeigeMattenWertungNormal(@PathVariable String turnierid, @PathVariable String begegnungid) {
		logger.info("Öffne Anzeige der Begegnung {}", begegnungid);
		OidcBenutzer oidcBenutzer = HelperSource.extractOidcBenutzer(SecurityContextHolder.getContext().getAuthentication());

		UUID turnierUUID = UUID.fromString(turnierid);
		UUID begegnungUUID = UUID.fromString(begegnungid);
		Einstellungen einstellungen = einstellungenService.ladeEinstellungen(turnierUUID);
		Begegnung begegnung = wettkampfService.ladeBegegnung(begegnungUUID);
		BegegnungDto begegnungDto = DtosConverter.convertFromBegegnung(begegnung);
		var altersklasse = begegnung.getWettkampfGruppe().altersklasse();
		Integer kampfzeit = einstellungenService.kampfZeit(turnierUUID, altersklasse);
		Integer matte = begegnung.getMatteId();
		Integer mattenrunde = begegnung.getMattenRunde();

		logger.debug("kampfzeit {}", kampfzeit);

		ModelAndView mav = new ModelAndView("mattenanzeige_normal");
		mav.addObject("turnierid", turnierid);
		mav.addObject("isloggedin", oidcBenutzer.isLoggedin());
		mav.addObject("matte", matte);
		mav.addObject("mattenrunde", mattenrunde);
		mav.addObject("begegnung", begegnungDto);
		mav.addObject("kampfzeit", kampfzeit);
		return mav;
	}
}
