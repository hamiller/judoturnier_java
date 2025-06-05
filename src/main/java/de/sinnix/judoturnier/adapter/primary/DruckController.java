package de.sinnix.judoturnier.adapter.primary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


import de.sinnix.judoturnier.adapter.primary.dto.MatteDto;
import de.sinnix.judoturnier.application.GewichtsklassenService;
import de.sinnix.judoturnier.application.TurnierService;
import de.sinnix.judoturnier.model.Matte;
import de.sinnix.judoturnier.model.OidcBenutzer;
import jakarta.annotation.security.PermitAll;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RestController
@RequestMapping(DruckController.CONTROLLER_URI)
public class DruckController {
	public static final String CONTROLLER_URI                           = "/turnier/{turnierid}/druck";
	public static final String DRUCK_RANDORI_BEGEGNUNGEN_URI            = "/randori_printview_matches/{altersklasse}";
	public static final String DRUCK_TURNIER_BEGEGNUNGEN_URI            = "/turnier_printview_matches/{geschlecht}/{altersklasse}";
	public static final String DRUCK_RANDORI_GEWICHTSKLASSE_URI         = "/randori_printview_groups/{altersklasse}";
	public static final String DRUCK_TURNIER_GEWICHTSKLASSE_URI         = "/turnier_printview_groups/{geschlecht}/{altersklasse}";
	public static final String DRUCK_RANDORI_BEGEGNUNG_DATENEINGABE_URI = "/randori_printview_matches_inserting_data/{altersklasse}";

	private static final Logger logger = LogManager.getLogger(DruckController.class);

	@Autowired
	private TurnierService         turnierService;
	@Autowired
	private GewichtsklassenService gewichtsklassenService;

	@PermitAll
	@GetMapping(DRUCK_RANDORI_BEGEGNUNGEN_URI)
	public ModelAndView ladeDruckAnsichtBegegnungenRandori(@PathVariable String turnierid, @PathVariable String altersklasse) {
		logger.info("Lade Randori-Druckansicht für Turnier {} und Altersklasse {}", turnierid, altersklasse);
		OidcBenutzer oidcBenutzer = HelperSource.extractOidcBenutzer(SecurityContextHolder.getContext().getAuthentication());
		UUID turnierUUID = UUID.fromString(turnierid);
		List<Matte> wettkampfreihenfolgeJeMatte = turnierService.ladeWettkampfreihenfolge(turnierUUID).stream()
			.sorted(Comparator.comparingInt(Matte::id))
			.toList();
		List<Matte> gefilterteMatten = MattenFilterer.filtereMatten(altersklasse, wettkampfreihenfolgeJeMatte);
		List<MatteDto> wettkampfreihenfolgeJeMatteGefiltertUndGruppiert = gruppiereNachGruppen(gefilterteMatten);

		ModelAndView mav = new ModelAndView("druckansicht_begegnungen_randori");
		mav.addObject("turnierid", turnierid);
		mav.addObject("isadmin", oidcBenutzer.istAdmin());
		mav.addObject("isloggedin", oidcBenutzer.isLoggedin());
		mav.addObject("matten", wettkampfreihenfolgeJeMatteGefiltertUndGruppiert);
		return mav;
	}

	@PermitAll
	@GetMapping(DRUCK_TURNIER_BEGEGNUNGEN_URI)
	public ModelAndView ladeDruckAnsichtBegegnungenTurnier(@PathVariable String turnierid, @PathVariable String geschlecht, @PathVariable String altersklasse) {
		logger.info("Lade Turnier-Druckansicht für Turnier {}, Geschlecht {} und Altersklasse {}", turnierid, geschlecht, altersklasse);
		OidcBenutzer oidcBenutzer = HelperSource.extractOidcBenutzer(SecurityContextHolder.getContext().getAuthentication());
		UUID turnierUUID = UUID.fromString(turnierid);
		List<Matte> wettkampfreihenfolgeJeMatte = turnierService.ladeWettkampfreihenfolge(turnierUUID).stream()
			.sorted(Comparator.comparingInt(Matte::id))
			.toList();
		List<Matte> gefilterteMatten = MattenFilterer.filtereMatten(altersklasse, wettkampfreihenfolgeJeMatte);
		List<MatteDto> wettkampfreihenfolgeJeMatteGefiltertUndGruppiert = gruppiereNachGruppen(gefilterteMatten);

		ModelAndView mav = new ModelAndView("druckansicht_begegnungen_turnier");
		mav.addObject("turnierid", turnierid);
		mav.addObject("isadmin", oidcBenutzer.istAdmin());
		mav.addObject("isloggedin", oidcBenutzer.isLoggedin());
		mav.addObject("matten", wettkampfreihenfolgeJeMatteGefiltertUndGruppiert);
		return mav;
	}

	@GetMapping(DRUCK_RANDORI_BEGEGNUNG_DATENEINGABE_URI)
	public ModelAndView ladeDruckAnsichtBegegnungenRandoriDateneintrag(@PathVariable String turnierid, @PathVariable String altersklasse) {
		logger.info("Lade Wertungs-Eintrag 'Randori' für Turnier {} und Altersklasse {}", turnierid, altersklasse);
		OidcBenutzer oidcBenutzer = HelperSource.extractOidcBenutzer(SecurityContextHolder.getContext().getAuthentication());
		UUID turnierUUID = UUID.fromString(turnierid);
		List<Matte> wettkampfreihenfolgeJeMatte = turnierService.ladeWettkampfreihenfolge(turnierUUID);
		List<Matte> gefilterteMatten = MattenFilterer.filtereMatten(altersklasse, wettkampfreihenfolgeJeMatte);
		List<MatteDto> wettkampfreihenfolgeJeMatteGefiltertUndGruppiert = gruppiereNachGruppen(gefilterteMatten);

		ModelAndView mav = new ModelAndView("druckansicht_begegnungen_randori_inserting_data");
		mav.addObject("turnierid", turnierid);
		mav.addObject("isadmin", oidcBenutzer.istAdmin());
		mav.addObject("isloggedin", oidcBenutzer.isLoggedin());
		mav.addObject("matten", wettkampfreihenfolgeJeMatteGefiltertUndGruppiert);
		return mav;
	}

	@PermitAll
	@GetMapping(DRUCK_RANDORI_GEWICHTSKLASSE_URI)
	public ModelAndView ladeDruckAnsichtGruppenRandori(@PathVariable String turnierid, @PathVariable String altersklasse) {
		logger.info("lade Druckansicht Randori-Gruppen für " + altersklasse);
		OidcBenutzer oidcBenutzer = HelperSource.extractOidcBenutzer(SecurityContextHolder.getContext().getAuthentication());

		var turnierUUID = UUID.fromString(turnierid);
		var currentGwks = gewichtsklassenService.ladeGewichtsklassenGruppen(turnierUUID);

		ModelAndView mav = new ModelAndView("druckansicht_gruppen_randori");
		mav.addObject("turnierid", turnierid);
		mav.addObject("isloggedin", oidcBenutzer.isLoggedin());
		mav.addObject("gruppen", currentGwks.stream().filter(gwk -> gwk.altersKlasse().name().equalsIgnoreCase(altersklasse)).toList());
		return mav;
	}

	@PermitAll
	@GetMapping(DRUCK_TURNIER_GEWICHTSKLASSE_URI)
	public ModelAndView ladeDruckAnsichtGruppenTurnier(@PathVariable String turnierid, @PathVariable String geschlecht, @PathVariable String altersklasse) {
		logger.info("Lade Druckansicht für Turnier-Gruppen {}, Geschlecht {} und Altersklasse {}", turnierid, geschlecht, altersklasse);
		OidcBenutzer oidcBenutzer = HelperSource.extractOidcBenutzer(SecurityContextHolder.getContext().getAuthentication());

		var turnierUUID = UUID.fromString(turnierid);
		var currentGwks = gewichtsklassenService.ladeGewichtsklassenGruppen(turnierUUID);

		ModelAndView mav = new ModelAndView("druckansicht_gruppen_turnier");
		mav.addObject("turnierid", turnierid);
		mav.addObject("isloggedin", oidcBenutzer.isLoggedin());
		mav.addObject("gruppen", currentGwks.stream().filter(gwk -> gwk.altersKlasse().name().equalsIgnoreCase(altersklasse)).toList());
		return mav;
	}

	private List<MatteDto> gruppiereNachGruppen(List<Matte> matten) {
		return matten.stream().map(mat -> DtosConverter.convertFromMatte(mat)).collect(Collectors.toList());
	}

}
