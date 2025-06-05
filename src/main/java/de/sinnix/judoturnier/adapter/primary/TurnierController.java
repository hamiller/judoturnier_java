package de.sinnix.judoturnier.adapter.primary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


import de.sinnix.judoturnier.adapter.primary.dto.BegegnungDto;
import de.sinnix.judoturnier.adapter.primary.dto.GruppeTypenRundenDto;
import de.sinnix.judoturnier.adapter.primary.dto.GruppenRundeDto;
import de.sinnix.judoturnier.adapter.primary.dto.MatteDto;
import de.sinnix.judoturnier.adapter.primary.dto.RundeDto;
import de.sinnix.judoturnier.application.EinstellungenService;
import de.sinnix.judoturnier.application.GewichtsklassenService;
import de.sinnix.judoturnier.application.TurnierService;
import de.sinnix.judoturnier.application.VereinService;
import de.sinnix.judoturnier.application.WettkaempferService;
import de.sinnix.judoturnier.application.WettkampfService;
import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Begegnung;
import de.sinnix.judoturnier.model.Einstellungen;
import de.sinnix.judoturnier.model.GewichtsklassenGruppe;
import de.sinnix.judoturnier.model.Matte;
import de.sinnix.judoturnier.model.OidcBenutzer;
import de.sinnix.judoturnier.model.Turnier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RestController
@RequestMapping(TurnierController.CONTROLLER_URI)
public class TurnierController {
	public static final String CONTROLLER_URI          = "/turnier/{turnierid}";
	public static final String BEGEGNUNGEN_URI         = "/begegnungen";
	public static final String BEGEGNUNG_URI           = "/begegnung";
	public static final String BEGEGNUNGEN_RANDORI_URI = "/begegnungen/randori";
	public static final String BEGEGNUNGEN_TURNIER_URI = "/begegnungen/normal";
	public static final String UPLOAD_VEREIN_URI       = "/uploadVerein";

	public static final String EXPORT_WETTKAEMPFER_URI = "/export-wk";

	private static final Logger logger = LogManager.getLogger(TurnierController.class);

	@Autowired
	private WettkaempferService    wettkaempferService;
	@Autowired
	private WettkampfService       wettkampfService;
	@Autowired
	private EinstellungenService   einstellungenService;
	@Autowired
	private GewichtsklassenService gewichtsklassenService;
	@Autowired
	private TurnierService         turnierService;
	@Autowired
	private VereinService          vereinService;


	@GetMapping
	public ModelAndView turnierUebersicht(@PathVariable String turnierid) {
		logger.debug("Turnierübersicht {} angefragt, eingeloggt", turnierid);

		// aktuell nur zum prüfen...
		OidcBenutzer oidcBenutzer = HelperSource.extractOidcBenutzer(SecurityContextHolder.getContext().getAuthentication());
		UUID turnierUUID = UUID.fromString(turnierid);
		Turnier turnierdaten = turnierService.ladeTurnier(turnierUUID);

		var wks = wettkaempferService.alleKaempfer(turnierUUID);
		var einstellungen = einstellungenService.ladeEinstellungen(turnierUUID);
		var vereine = vereinService.holeAlleVereine(turnierUUID);

		ModelAndView mav = new ModelAndView("turnieruebersicht");
		mav.addObject("turnierid", turnierid);
		mav.addObject("isadmin", oidcBenutzer.istAdmin());
		mav.addObject("isloggedin", oidcBenutzer.isLoggedin());
		mav.addObject("anzahlwk", wks.size());
		mav.addObject("anzahlv", vereine.size());
		mav.addObject("turniertyp", einstellungen.turnierTyp());
		mav.addObject("enableEditing", oidcBenutzer.istAdmin());
		mav.addObject("turniername", turnierdaten.name());
		mav.addObject("turnierort", turnierdaten.ort());
		mav.addObject("turnierdatum", turnierdaten.datum());
		return mav;
	}

	@GetMapping(BEGEGNUNGEN_URI)
	public ModelAndView unterscheideBegegungen(@PathVariable String turnierid) {
		if (einstellungenService.isRandori(UUID.fromString(turnierid))) {
			return new ModelAndView("redirect:/turnier/" + turnierid + "/begegnungen/randori");
		} else {
			return new ModelAndView("redirect:/turnier/" + turnierid + "/begegnungen/normal");
		}
	}

	@GetMapping(BEGEGNUNGEN_RANDORI_URI)
	public ModelAndView ladeWettkampfreihenfolgeJeMatteRandori(@PathVariable String turnierid, @RequestParam(value = "error", required = false) String error, @RequestParam(value = "altersklasse", required = false) String altersklasse) {
		logger.info("Lade Randori-Begegnungen für Turnier {} und Altersklasse {}", turnierid, altersklasse);
		OidcBenutzer oidcBenutzer = HelperSource.extractOidcBenutzer(SecurityContextHolder.getContext().getAuthentication());
		UUID turnierUUID = UUID.fromString(turnierid);
		List<GewichtsklassenGruppe> gwks = gewichtsklassenService.ladeGewichtsklassenGruppen(turnierUUID);
		List<Matte> wettkampfreihenfolgeJeMatte = turnierService.ladeWettkampfreihenfolge(turnierUUID);
		Set<Altersklasse> altersklassen = gwks.stream()
			.map(GewichtsklassenGruppe::altersKlasse)
			.collect(Collectors.toSet());
		Einstellungen einstellungen = einstellungenService.ladeEinstellungen(turnierUUID);

		List<MatteDto> gefilterteMatten = MattenFilterer.filtereMatten(altersklasse, wettkampfreihenfolgeJeMatte).stream()
			.map(m -> DtosConverter.convertFromMatte(m))
			.collect(Collectors.toList());


		logger.trace("wettkampfreihenfolgeJeMatte {} ", wettkampfreihenfolgeJeMatte);
		ModelAndView mav = new ModelAndView("begegnungen_randori");
		mav.addObject("turnierid", turnierid);
		mav.addObject("isadmin", oidcBenutzer.istAdmin());
		mav.addObject("isloggedin", oidcBenutzer.isLoggedin());
		mav.addObject("gewichtsklassenGruppe", gwks);
		mav.addObject("matten", gefilterteMatten);
		mav.addObject("altersklassen", altersklassen);
		mav.addObject("preverror", error);
		mav.addObject("separatealtersklassen", einstellungen.separateAlterklassen());
		return mav;
	}

	@GetMapping(BEGEGNUNGEN_TURNIER_URI)
	public ModelAndView ladeWettkampfreihenfolgeJeMatteNormal(@PathVariable String turnierid, @RequestParam(value = "error", required = false) String error, @RequestParam(value = "altersklasse", required = false) String altersklasse) {
		logger.info("Lade Begegnungen für Turnier {}", turnierid);
		OidcBenutzer oidcBenutzer = HelperSource.extractOidcBenutzer(SecurityContextHolder.getContext().getAuthentication());
		UUID turnierUUID = UUID.fromString(turnierid);
		List<GewichtsklassenGruppe> gwks = gewichtsklassenService.ladeGewichtsklassenGruppen(turnierUUID);
		List<Matte> wettkampfreihenfolgeJeMatte = turnierService.ladeWettkampfreihenfolge(turnierUUID);
		Set<Altersklasse> altersklassen = gwks.stream()
			.map(GewichtsklassenGruppe::altersKlasse)
			.collect(Collectors.toSet());
		Einstellungen einstellungen = einstellungenService.ladeEinstellungen(turnierUUID);

		List<MatteDto> gefilterteMatten = MattenFilterer.filtereMatten(altersklasse, wettkampfreihenfolgeJeMatte).stream()
			.map(m -> DtosConverter.convertFromMatte(m))
			.collect(Collectors.toList());

		Map<Integer, List<GruppeTypenRundenDto>> gruppenListMitTypen = gefilterteMatten.stream()
			.collect(Collectors.toMap(
				matte -> matte.id(), // Key: ID aus MatteDto als String
				matte -> reorganize(matte.gruppenRunden()) // Value: Ergebnis der reorganize-Methode
			));

		logger.trace("gefilterteMatten: {}", gefilterteMatten);

		ModelAndView mav = new ModelAndView("begegnungen_normal");
		mav.addObject("turnierid", turnierid);
		mav.addObject("isadmin", oidcBenutzer.istAdmin());
		mav.addObject("isloggedin", oidcBenutzer.isLoggedin());
		mav.addObject("gewichtsklassenGruppe", gwks);
		mav.addObject("matten", gefilterteMatten);
		mav.addObject("gruppenListMitTypen", gruppenListMitTypen);
		mav.addObject("altersklassen", altersklassen);
		mav.addObject("preverror", error);
		mav.addObject("separatealtersklassen", einstellungen.separateAlterklassen());
		return mav;
	}

	@PostMapping(UPLOAD_VEREIN_URI)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ModelAndView ladeCsvVereinHoch(@PathVariable String turnierid, @RequestParam("fileVereine") MultipartFile file) {
		logger.info("Hochgeladene CSV-Datei für Vereine {}", file.getName());
		UUID turnierUUID = UUID.fromString(turnierid);
		vereinService.speichereCSV(turnierUUID, file);

		return new ModelAndView("redirect:/turnier/" + turnierid);
	}

	@PostMapping(BEGEGNUNGEN_URI)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ModelAndView erstelleWettkampfreihenfolgeJeMatte(@PathVariable String turnierid) {
		logger.info("Erstelle Begegnungen für Turnier {}", turnierid);
		String error = "";
		try {
			UUID turnierUUID = UUID.fromString(turnierid);
			turnierService.loescheWettkampfreihenfolge(turnierUUID);
			wettkampfService.erstelleWettkampfreihenfolge(turnierUUID);
		} catch (Exception e) {
			logger.error("Ein unerwarteter Fehler ist aufgetreten", e);
			error = e.toString();
		}
		if (einstellungenService.isRandori(UUID.fromString(turnierid))) {
			return new ModelAndView("redirect:/turnier/" + turnierid + "/begegnungen/randori?error=" + error);
		} else {
			return new ModelAndView("redirect:/turnier/" + turnierid + "/begegnungen/normal?error=" + error);
		}
	}

	@PostMapping(BEGEGNUNG_URI)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ModelAndView erneuerWettkampfreihenfolgeFuerAltersklasse(@PathVariable String turnierid, @RequestBody String altersklasse) {
		logger.info("Erneuere Begegnungen für Turnier {} und Altersklasse {}", turnierid, altersklasse);
		String error = "";
		UUID turnierUUID = UUID.fromString(turnierid);
		try {
			wettkampfService.loescheWettkampfreihenfolgeAltersklasse(Altersklasse.valueOf(altersklasse), turnierUUID);
			wettkampfService.erstelleWettkampfreihenfolgeAltersklasse(Optional.of(Altersklasse.valueOf(altersklasse)), turnierUUID);
		} catch (Exception e) {
			logger.error("Ein unerwarteter Fehler ist aufgetreten", e);
			error = e.toString();
		}
		if (einstellungenService.isRandori(turnierUUID)) {
			return new ModelAndView("redirect:/turnier/" + turnierid + "/begegnungen/randori?error=" + error);
		} else {
			return new ModelAndView("redirect:/turnier/" + turnierid + "/begegnungen/normal?error=" + error);
		}
	}

	@DeleteMapping(BEGEGNUNG_URI)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void entferneWettkampfreihenfolgen(@PathVariable String turnierid) {
		logger.info("Lösche alle Begegnungen in Turnier {}", turnierid);
		String error = "";
		turnierService.loescheWettkampfreihenfolge(UUID.fromString(turnierid));
	}

	@GetMapping(EXPORT_WETTKAEMPFER_URI)
	public ResponseEntity<Resource> exportiereWettkaempfer(@PathVariable String turnierid) {
		logger.info("Exportiere alle Wettkämpfer von Turnier {}", turnierid);
		UUID turnierUUID = UUID.fromString(turnierid);

		ByteArrayResource resource = wettkaempferService.erstelleWettkaempferCSV(turnierUUID);

		return ResponseEntity.ok()
			.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=turnier_" + turnierid + ".csv")
			.contentType(MediaType.parseMediaType("text/csv"))
			.contentLength(resource.contentLength())
			.body(resource);
	}

	public static List<GruppeTypenRundenDto> reorganize(List<GruppenRundeDto> gruppen) {
		Map<UUID, GruppeTypenRundenDto> map = new HashMap<>();
		for (GruppenRundeDto gruppe : gruppen) {
			var wkg = gruppe.runde().getFirst().gruppe();
			UUID gruppenId = wkg.id();
			logger.trace("Gruppe: {}", gruppenId);
			map.putIfAbsent(gruppenId, new GruppeTypenRundenDto(gruppenId.toString(), wkg.typ(), new ArrayList<>(), new ArrayList<>()));

			GruppeTypenRundenDto gtr = map.get(gruppenId);
			Map<Integer, RundeDto> gewinnerRunden = new LinkedHashMap<>();
			Map<Integer, RundeDto> trostRunden = new LinkedHashMap<>();
			for (int i = 0; i < gruppe.runde().size(); i++) {
				logger.trace("Runde {}", i);

				// alle ids sind hier gleich
				for (BegegnungDto begegnung : gruppe.runde().get(i).begegnungen()) {
					logger.trace("Runde {}, aktuelle Paarung {}", begegnung.runde(), begegnung.akuellePaarung());
					if (begegnung.rundenTyp().equals(Begegnung.RundenTyp.GEWINNERRUNDE)) {
						logger.trace("Eine Gewinnerrunde!");
						gewinnerRunden.putIfAbsent(i, newRundeDto(gruppe.runde().get(i)));
						gewinnerRunden.get(i).begegnungen().add(begegnung);
					} else if (begegnung.rundenTyp().equals(Begegnung.RundenTyp.TROSTRUNDE)) {
						logger.trace("Eine Trostrunde!");
						trostRunden.putIfAbsent(i, newRundeDto(gruppe.runde().get(i)));
						trostRunden.get(i).begegnungen().add(begegnung);
					}


				}
			}
			gtr.gewinnerrundeDtoList().addAll(gewinnerRunden.values().stream().toList());
			gtr.trostrundeDtoList().addAll(trostRunden.values().stream().toList());

			logger.trace("erstellte liste: {}", gtr.gewinnerrundeDtoList());
			logger.trace("----------");
		}

		return map.values().stream()
			.collect(Collectors.toList());
	}

	private static RundeDto newRundeDto(RundeDto rundeDto) {
		return new RundeDto(
			rundeDto.rundeId(),
			rundeDto.mattenRunde(),
			rundeDto.gruppenRunde(),
			rundeDto.rundeGesamt(),
			rundeDto.matteId(),
			rundeDto.altersklasse(),
			rundeDto.gruppe(),
			new ArrayList<>()
		);
	}
}
