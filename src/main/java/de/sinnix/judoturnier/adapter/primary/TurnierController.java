package de.sinnix.judoturnier.adapter.primary;

import de.sinnix.judoturnier.application.BenutzerService;
import de.sinnix.judoturnier.application.EinstellungenService;
import de.sinnix.judoturnier.application.GewichtsklassenService;
import de.sinnix.judoturnier.application.TurnierService;
import de.sinnix.judoturnier.application.VereinService;
import de.sinnix.judoturnier.application.WettkaempferService;
import de.sinnix.judoturnier.application.WettkampfService;
import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Begegnung;
import de.sinnix.judoturnier.model.Benutzer;
import de.sinnix.judoturnier.model.Einstellungen;
import de.sinnix.judoturnier.model.GewichtsklassenGruppe;
import de.sinnix.judoturnier.model.Matte;
import de.sinnix.judoturnier.model.OidcBenutzer;
import de.sinnix.judoturnier.model.Turnier;
import jakarta.annotation.security.PermitAll;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class TurnierController {
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
	@Autowired
	private BuildProperties        buildProperties;
	@Autowired
	private BenutzerService        benutzerService;

	@GetMapping("/")
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

	@GetMapping("/kontakt")
	public ModelAndView kontaktPage() {
		OidcBenutzer oidcBenutzer = HelperSource.extractOidcBenutzer(SecurityContextHolder.getContext().getAuthentication());
		logger.info("Turniere-Kontaktseite. User {}", oidcBenutzer);

		ModelAndView mav = new ModelAndView("kontakt");
		mav.addObject("isloggedin", oidcBenutzer.isLoggedin());
		return mav;
	}

	@PostMapping("/kontakt")
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

	@GetMapping("/turniere")
	public ModelAndView turniere() {
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

	@PostMapping("/turniere")
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

	@GetMapping("/turnier/{turnierid}")
	public ModelAndView turnierUebersicht(@PathVariable String turnierid) {
		logger.debug("Turnierübersicht {} angefragt, eingeloggt", turnierid);

		// aktuell nur zum prüfen...
		OidcBenutzer oidcBenutzer = HelperSource.extractOidcBenutzer(SecurityContextHolder.getContext().getAuthentication());
		UUID turnierUUID = UUID.fromString(turnierid);
		Turnier turnierdaten = turnierService.ladeTurnier(turnierUUID);

		var wks = wettkaempferService.alleKaempfer(turnierUUID);
		var einstellungen = einstellungenService.ladeEinstellungen(turnierUUID);
		var anzahlv = wks.stream().map(wk -> wk.verein().id()).collect(Collectors.toSet()).size();

		ModelAndView mav = new ModelAndView("turnieruebersicht");
		mav.addObject("turnierid", turnierid);
		mav.addObject("isadmin", oidcBenutzer.istAdmin());
		mav.addObject("isloggedin", oidcBenutzer.isLoggedin());
		mav.addObject("anzahlwk", wks.size());
		mav.addObject("anzahlv", anzahlv);
		mav.addObject("turniertyp", einstellungen.turnierTyp());
		mav.addObject("enableEditing", oidcBenutzer.istAdmin());
		mav.addObject("turniername", turnierdaten.name());
		mav.addObject("turnierort", turnierdaten.ort());
		mav.addObject("turnierdatum", turnierdaten.datum());
		return mav;
	}

	@PostMapping("/turniere/{turnierid}/benutzer")
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

		return new ModelAndView("redirect:/turnier/" + turnierid);
	}

	@GetMapping("/turnier/{turnierid}/begegnungen")
	public ModelAndView unterscheideBegegungen(@PathVariable String turnierid) {
		if (einstellungenService.isRandori(UUID.fromString(turnierid))) {
			return new ModelAndView("redirect:/turnier/" + turnierid + "/begegnungen/randori");
		} else {
			return new ModelAndView("redirect:/turnier/" + turnierid + "/begegnungen/normal");
		}
	}

	@GetMapping("/turnier/{turnierid}/begegnungen/randori")
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

		List<MatteDto> gefilterteMatten = filtereMatten(altersklasse, wettkampfreihenfolgeJeMatte).stream()
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

	@GetMapping("/turnier/{turnierid}/begegnungen/normal")
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

		List<MatteDto> gefilterteMatten = filtereMatten(altersklasse, wettkampfreihenfolgeJeMatte).stream()
			.map(m -> DtosConverter.convertFromMatte(m))
			.collect(Collectors.toList());

		Map<Integer, List<GruppeTypenRundenDto>> gruppenListMitTypen = gefilterteMatten.stream()
			.collect(Collectors.toMap(
				matte -> matte.id(), // Key: ID aus MatteDto als String
				matte -> reorganize(matte.gruppenRunden()) // Value: Ergebnis der reorganize-Methode
			));

		logger.info("wettkampfreihenfolgeJeMatte: {}", wettkampfreihenfolgeJeMatte);

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

	@PostMapping("/turnier/{turnierid}/uploadVerein")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ModelAndView ladeCsvVereinHoch(@PathVariable String turnierid, @RequestParam("fileVereine") MultipartFile file) {
		logger.info("Hochgeladene CSV-Datei für Vereine {}", file.getName());
		UUID turnierUUID = UUID.fromString(turnierid);
		vereinService.speichereCSV(turnierUUID, file);

		return new ModelAndView("redirect:/turnier/" + turnierid);
	}

	@PostMapping("/turnier/{turnierid}/uploadWettkaempfer")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ModelAndView ladeCsvWettkaempferHoch(@PathVariable String turnierid, @RequestParam("fileWettkaempfer") MultipartFile file) {
		logger.info("Hochgeladene CSV-Datei für Wettkaempfer {}", file.getName());
		UUID turnierUUID = UUID.fromString(turnierid);
		wettkaempferService.speichereCSV(turnierUUID, file);

		return new ModelAndView("redirect:/turnier/" + turnierid);
	}

	@PostMapping("/turnier/{turnierid}/begegnungen")
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

	@PostMapping(value = "/turnier/{turnierid}/begegnung")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ModelAndView erneuerWettkampfreihenfolgeFuerAltersklasse(@PathVariable String turnierid, @RequestBody String altersklasse) {
		logger.info("Erneuere Begegnungen für Turnier {} und Altersklasse {}", turnierid, altersklasse);
		String error = "";
		UUID turnierUUID = UUID.fromString(turnierid);
		try {
			wettkampfService.loescheWettkampfreihenfolgeAltersklasse(Altersklasse.valueOf(altersklasse), turnierUUID);
			wettkampfService.erstelleWettkampfreihenfolgeAltersklasse(Optional.of(Altersklasse.valueOf(altersklasse)), turnierUUID);
		} catch (Exception e) {
			error = e.toString();
		}
		if (einstellungenService.isRandori(turnierUUID)) {
			return new ModelAndView("redirect:/turnier/" + turnierid + "/begegnungen/randori?error=" + error);
		} else {
			return new ModelAndView("redirect:/turnier/" + turnierid + "/begegnungen/normal?error=" + error);
		}
	}

	@DeleteMapping("/turnier/{turnierid}/begegnung")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void entferneWettkampfreihenfolgen(@PathVariable String turnierid) {
		logger.info("Lösche alle Begegnungen in Turnier {}", turnierid);
		String error = "";
		turnierService.loescheWettkampfreihenfolge(UUID.fromString(turnierid));
	}

	@PermitAll
	@GetMapping("/turnier/{turnierid}/begegnungen/randori_printview_matches/{altersklasse}")
	public ModelAndView ladeDruckAnsichtBegegnungenRandori(@PathVariable String turnierid, @PathVariable String altersklasse) {
		logger.info("Lade Randori-Druckansicht für Turnier {} und Altersklasse {}", turnierid, altersklasse);
		OidcBenutzer oidcBenutzer = HelperSource.extractOidcBenutzer(SecurityContextHolder.getContext().getAuthentication());
		UUID turnierUUID = UUID.fromString(turnierid);
		List<Matte> wettkampfreihenfolgeJeMatte = turnierService.ladeWettkampfreihenfolge(turnierUUID).stream()
			.sorted(Comparator.comparingInt(Matte::id))
			.toList();
		List<Matte> gefilterteMatten = filtereMatten(altersklasse, wettkampfreihenfolgeJeMatte);
		List<MatteDto> wettkampfreihenfolgeJeMatteGefiltertUndGruppiert = gruppiereNachGruppen(gefilterteMatten);

		ModelAndView mav = new ModelAndView("druckansicht_begegnungen_randori");
		mav.addObject("turnierid", turnierid);
		mav.addObject("isadmin", oidcBenutzer.istAdmin());
		mav.addObject("isloggedin", oidcBenutzer.isLoggedin());
		mav.addObject("matten", wettkampfreihenfolgeJeMatteGefiltertUndGruppiert);
		return mav;
	}

	@PermitAll
	@GetMapping("/turnier/{turnierid}/begegnungen/turnier_printview_matches/{geschlecht}/{altersklasse}")
	public ModelAndView ladeDruckAnsichtBegegnungenTurnier(@PathVariable String turnierid, @PathVariable String geschlecht, @PathVariable String altersklasse) {
		logger.info("Lade Turnier-Druckansicht für Turnier {}, Geschlecht {} und Altersklasse {}", turnierid, geschlecht, altersklasse);
		OidcBenutzer oidcBenutzer = HelperSource.extractOidcBenutzer(SecurityContextHolder.getContext().getAuthentication());
		UUID turnierUUID = UUID.fromString(turnierid);
		List<Matte> wettkampfreihenfolgeJeMatte = turnierService.ladeWettkampfreihenfolge(turnierUUID).stream()
			.sorted(Comparator.comparingInt(Matte::id))
			.toList();
		List<Matte> gefilterteMatten = filtereMatten(altersklasse, wettkampfreihenfolgeJeMatte);
		List<MatteDto> wettkampfreihenfolgeJeMatteGefiltertUndGruppiert = gruppiereNachGruppen(gefilterteMatten);

		ModelAndView mav = new ModelAndView("druckansicht_begegnungen_turnier");
		mav.addObject("turnierid", turnierid);
		mav.addObject("isadmin", oidcBenutzer.istAdmin());
		mav.addObject("isloggedin", oidcBenutzer.isLoggedin());
		mav.addObject("matten", wettkampfreihenfolgeJeMatteGefiltertUndGruppiert);
		return mav;
	}

	@GetMapping("/turnier/{turnierid}/begegnungen/randori_printview_matches_inserting_data/{altersklasse}")
	public ModelAndView ladeDruckAnsichtBegegnungenRandoriDateneintrag(@PathVariable String turnierid, @PathVariable String altersklasse) {
		logger.info("Lade Wertungs-Eintrag 'Randori' für Turnier {} und Altersklasse {}", turnierid, altersklasse);
		OidcBenutzer oidcBenutzer = HelperSource.extractOidcBenutzer(SecurityContextHolder.getContext().getAuthentication());
		UUID turnierUUID = UUID.fromString(turnierid);
		List<Matte> wettkampfreihenfolgeJeMatte = turnierService.ladeWettkampfreihenfolge(turnierUUID);
		List<Matte> gefilterteMatten = filtereMatten(altersklasse, wettkampfreihenfolgeJeMatte);
		List<MatteDto> wettkampfreihenfolgeJeMatteGefiltertUndGruppiert = gruppiereNachGruppen(gefilterteMatten);

		ModelAndView mav = new ModelAndView("druckansicht_begegnungen_randori_inserting_data");
		mav.addObject("turnierid", turnierid);
		mav.addObject("isadmin", oidcBenutzer.istAdmin());
		mav.addObject("isloggedin", oidcBenutzer.isLoggedin());
		mav.addObject("matten", wettkampfreihenfolgeJeMatteGefiltertUndGruppiert);
		return mav;
	}

	@GetMapping("/turnier/{turnierid}/export-wk")
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

	private List<MatteDto> gruppiereNachGruppen(List<Matte> matten) {
		return matten.stream().map(mat -> DtosConverter.convertFromMatte(mat)).collect(Collectors.toList());
	}

	private static List<Matte> filtereMatten(String altersklasse, List<Matte> wettkampfreihenfolgeJeMatte) {
		if (!StringUtils.isBlank(altersklasse)) {
			var altersKlasse = Altersklasse.valueOf(altersklasse);
			// Filtern der Liste nach Altersklasse "U11" und Erstellen der neuen Liste von Matten
			return wettkampfreihenfolgeJeMatte.stream()
				.map(matte -> new Matte(
					matte.id(),
					matte.runden().stream()
						.filter(runde -> altersKlasse.equals(runde.altersklasse()))
						.collect(Collectors.toList())
				))
				.filter(matte -> !matte.runden().isEmpty())
				.collect(Collectors.toList());
		}
		return wettkampfreihenfolgeJeMatte;
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
					logger.trace("Runde {}, aktuelle Paarung {}" ,begegnung.runde(), begegnung.akuellePaarung());
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
