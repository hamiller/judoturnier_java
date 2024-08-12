package de.sinnix.judoturnier.adapter.primary;

import de.sinnix.judoturnier.application.EinstellungenService;
import de.sinnix.judoturnier.application.GewichtsklassenService;
import de.sinnix.judoturnier.application.TurnierService;
import de.sinnix.judoturnier.application.VereinService;
import de.sinnix.judoturnier.application.WettkaempferService;
import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Benutzer;
import de.sinnix.judoturnier.model.Einstellungen;
import de.sinnix.judoturnier.model.GewichtsklassenGruppe;
import de.sinnix.judoturnier.model.GruppenRunde;
import de.sinnix.judoturnier.model.Matte;
import de.sinnix.judoturnier.model.Turnier;
import jakarta.annotation.security.PermitAll;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
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
import java.util.List;
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
	private EinstellungenService   einstellungenService;
	@Autowired
	private GewichtsklassenService gewichtsklassenService;
	@Autowired
	private TurnierService         turnierService;
	@Autowired
	private VereinService          vereinService;
	@Autowired
	private BuildProperties        buildProperties;

	@GetMapping("/")
	public ModelAndView startPage() {
		Benutzer benutzer = HelperSource.extractBewerter(SecurityContextHolder.getContext().getAuthentication());
		logger.info("Turniere-Startseite. User {}", benutzer);

		ModelAndView mav = new ModelAndView("startseite");
		return mav;
	}

	@GetMapping("/turnier")
	public ModelAndView turniere() {
		logger.debug("lade vorhandene Turniere");
		Benutzer benutzer = HelperSource.extractBewerter(SecurityContextHolder.getContext().getAuthentication());
		logger.info("Eingeloggter User {}", benutzer);

		List<Turnier> turniere = new ArrayList<>();
		if (benutzer.istAdmin()) {
			turniere = turnierService.ladeTurniere();
		}
		ModelAndView mav = new ModelAndView("turniere");
		mav.addObject("turniere", turniere);
		mav.addObject("anzahlturniere", turniere.size());
		mav.addObject("enableEditing", benutzer.istAdmin());
		mav.addObject("software_version", buildProperties.getVersion());
		mav.addObject("software_zeit", buildProperties.getTime());
		return mav;
	}

	@PostMapping("/turnier")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ModelAndView erstelleTurnier(@RequestBody MultiValueMap<String, String> formData) {
		logger.debug("erstelle ein neues Turnier {}", formData);
		Benutzer benutzer = HelperSource.extractBewerter(SecurityContextHolder.getContext().getAuthentication());

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
		Benutzer benutzer = HelperSource.extractBewerter(SecurityContextHolder.getContext().getAuthentication());
		Turnier t = turnierService.ladeTurnier(turnierid);

		var wks = wettkaempferService.alleKaempfer(UUID.fromString(turnierid));
		var einstellungen = einstellungenService.ladeEinstellungen(UUID.fromString(turnierid));

		ModelAndView mav = new ModelAndView("turnieruebersicht");
		mav.addObject("anzahlwk", wks.size());
		mav.addObject("turniertyp", einstellungen.turnierTyp());
		mav.addObject("enableEditing", benutzer.istAdmin());
		return mav;
	}

	@GetMapping("/turnier/{turnierid}/begegnungen")
	public ModelAndView unterscheideBegegungen(@PathVariable String turnierid) {
		if (einstellungenService.isRandori()) {
			return new ModelAndView("redirect:/turnier/" + turnierid + "/begegnungen/randori");
		} else {
			return new ModelAndView("redirect:/turnier/" + turnierid + "/begegnungen/normal");
		}
	}

	@GetMapping("/turnier/{turnierid}/begegnungen/randori")
	public ModelAndView ladeWettkampfreihenfolgeJeMatteRandori(@PathVariable String turnierid, @RequestParam(value = "error", required = false) String error, @RequestParam(value = "altersklasse", required = false) String altersklasse) {
		logger.info("Lade Randori-Begegnungen für Turnier {} und Altersklasse {}", turnierid, altersklasse);
		var turnierUUID = UUID.fromString(turnierid);
		List<GewichtsklassenGruppe> gwks = gewichtsklassenService.ladeGewichtsklassenGruppen(turnierUUID);
		List<Matte> wettkampfreihenfolgeJeMatte = turnierService.ladeWettkampfreihenfolge(turnierUUID);
		Set<Altersklasse> altersklassen = gwks.stream()
			.map(GewichtsklassenGruppe::altersKlasse)
			.collect(Collectors.toSet());
		Einstellungen einstellungen = einstellungenService.ladeEinstellungen(turnierUUID);

		List<Matte> gefilterteMatten = filtereMatten(altersklasse, wettkampfreihenfolgeJeMatte);

		logger.trace("wettkampfreihenfolgeJeMatte {} ", wettkampfreihenfolgeJeMatte);
		ModelAndView mav = new ModelAndView("begegnungen_randori");
		mav.addObject("turnierid", turnierid);
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
		var turnierUUID = UUID.fromString(turnierid);
		List<GewichtsklassenGruppe> gwks = gewichtsklassenService.ladeGewichtsklassenGruppen(turnierUUID);
		List<Matte> wettkampfreihenfolgeJeMatte = turnierService.ladeWettkampfreihenfolge(turnierUUID);
		Set<Altersklasse> altersklassen = gwks.stream()
			.map(GewichtsklassenGruppe::altersKlasse)
			.collect(Collectors.toSet());
		Einstellungen einstellungen = einstellungenService.ladeEinstellungen(turnierUUID);

		List<Matte> gefilterteMatten = filtereMatten(altersklasse, wettkampfreihenfolgeJeMatte);

		ModelAndView mav = new ModelAndView("begegnungen_normal");
		mav.addObject("turnierid", turnierid);
		mav.addObject("gewichtsklassenGruppe", gwks);
		mav.addObject("matten", gefilterteMatten);
		mav.addObject("altersklassen", altersklassen);
		mav.addObject("preverror", error);
		mav.addObject("separatealtersklassen", einstellungen.separateAlterklassen());
		return mav;
	}

	@PostMapping("/turnier/{turnierid}/uploadVerein")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ModelAndView ladeCsvVereinHoch(@PathVariable String turnierid, @RequestParam("fileVereine") MultipartFile file) {
		logger.info("Hochgeladene CSV-Datei für Vereine {}", file.getName());
		var turnierUUID = UUID.fromString(turnierid);
		vereinService.speichereCSV(turnierUUID, file);

		return new ModelAndView("redirect:/turnier/" + turnierid);
	}

	@PostMapping("/turnier/{turnierid}/uploadWettkaempfer")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ModelAndView ladeCsvWettkaempferHoch(@PathVariable String turnierid, @RequestParam("fileWettkaempfer") MultipartFile file) {
		logger.info("Hochgeladene CSV-Datei für Wettkaempfer {}", file.getName());
		var turnierUUID = UUID.fromString(turnierid);
		wettkaempferService.speichereCSV(turnierUUID, file);

		return new ModelAndView("redirect:/turnier/" + turnierid);
	}

	@PostMapping("/turnier/{turnierid}/begegnungen")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ModelAndView erstelleWettkampfreihenfolgeJeMatte(@PathVariable String turnierid) {
		logger.info("Erstelle Begegnungen für Turnier {}", turnierid);
		String error = "";
		try {
			turnierService.loescheWettkampfreihenfolge(UUID.fromString(turnierid));
			turnierService.erstelleWettkampfreihenfolge(UUID.fromString(turnierid));
		} catch (Exception e) {
			logger.error("Ein unerwarteter Fehler ist aufgetreten", e);
			error = e.toString();
		}
		if (einstellungenService.isRandori()) {
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
		try {
			turnierService.loescheWettkampfreihenfolgeAltersklasse(Altersklasse.valueOf(altersklasse), UUID.fromString(turnierid));
			turnierService.erstelleWettkampfreihenfolgeAltersklasse(Optional.of(Altersklasse.valueOf(altersklasse)), UUID.fromString(turnierid));
		} catch (Exception e) {
			error = e.toString();
		}
		if (einstellungenService.isRandori()) {
			return new ModelAndView("redirect:/turnier/" + turnierid + "/begegnungen/randori?error=" + error);
		} else {
			return new ModelAndView("redirect:/turnier/" + turnierid + "/begegnungen/normal?error=" + error);
		}
	}

	@DeleteMapping("/turnier/{turnierid}/begegnung")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ModelAndView entferneWettkampfreihenfolgen(@PathVariable String turnierid) {
		logger.info("Lösche alle Begegnungen in Turnier {}", turnierid);
		String error = "";
		try {
			turnierService.loescheWettkampfreihenfolge(UUID.fromString(turnierid));
		} catch (Exception e) {
			error = e.toString();
		}
		if (einstellungenService.isRandori()) {
			return new ModelAndView("redirect:/turnier/" + turnierid + "/begegnungen/randori?error=" + error);
		} else {
			return new ModelAndView("redirect:/turnier/" + turnierid + "/begegnungen/normal?error=" + error);
		}
	}

	@PermitAll
	@GetMapping("/turnier/{turnierid}/begegnungen/randori_printview_matches/{altersklasse}")
	public ModelAndView ladeDruckAnsichtBegegnungenRandori(@PathVariable String turnierid, @PathVariable String altersklasse) {
		logger.info("Lade Randori-Druckansicht für Turnier {} und Altersklasse {}", turnierid, altersklasse);
		var turnierUUID = UUID.fromString(turnierid);
		List<Matte> wettkampfreihenfolgeJeMatte = turnierService.ladeWettkampfreihenfolge(turnierUUID).stream()
			.sorted(Comparator.comparingInt(Matte::id))
			.toList();
		List<Matte> gefilterteMatten = filtereMatten(altersklasse, wettkampfreihenfolgeJeMatte);
		List<MatteDto> wettkampfreihenfolgeJeMatteGefiltertUndGruppiert = gruppiereNachGruppen(gefilterteMatten);

		ModelAndView mav = new ModelAndView("druckansicht_begegnungen_randori");
		mav.addObject("turnierid", turnierid);
		mav.addObject("matten", wettkampfreihenfolgeJeMatteGefiltertUndGruppiert);
		return mav;
	}

	@GetMapping("/turnier/{turnierid}/begegnungen/randori_printview_matches_inserting_data/{altersklasse}")
	public ModelAndView ladeDruckAnsichtBegegnungenRandoriDateneintrag(@PathVariable String turnierid, @PathVariable String altersklasse) {
		logger.info("Lade Wertungs-Eintrag 'Randori' für Turnier {} und Altersklasse {}", turnierid, altersklasse);
		var turnierUUID = UUID.fromString(turnierid);
		List<Matte> wettkampfreihenfolgeJeMatte = turnierService.ladeWettkampfreihenfolge(turnierUUID);
		List<Matte> gefilterteMatten = filtereMatten(altersklasse, wettkampfreihenfolgeJeMatte);
		List<MatteDto> wettkampfreihenfolgeJeMatteGefiltertUndGruppiert = gruppiereNachGruppen(gefilterteMatten);

		ModelAndView mav = new ModelAndView("druckansicht_begegnungen_randori_inserting_data");
		mav.addObject("turnierid", turnierid);
		mav.addObject("matten", wettkampfreihenfolgeJeMatteGefiltertUndGruppiert);
		return mav;
	}

	private List<MatteDto> gruppiereNachGruppen(List<Matte> matten) {
		return matten.stream().map(mat -> {
			List<GruppenRunde> gruppenRunden = new ArrayList<>();
			gruppenRunden.add(new GruppenRunde(new ArrayList<>()));
			int gruppenRundenNummer = 0;

			for (int i = 0; i < mat.runden().size(); i++) {
				Integer aktuelleGruppe = mat.runden().get(i).gruppe().id();
				Integer vorherigeGruppe = i > 0 ? mat.runden().get(i - 1).gruppe().id() : aktuelleGruppe;

				if (!aktuelleGruppe.equals(vorherigeGruppe)) {
					gruppenRunden.add(new GruppenRunde(new ArrayList<>()));
					gruppenRundenNummer++;
				}
				gruppenRunden.get(gruppenRundenNummer).runde().add(mat.runden().get(i));
			}

			return new MatteDto(mat.id(), gruppenRunden);
		}).collect(Collectors.toList());
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
}
