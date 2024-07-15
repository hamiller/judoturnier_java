package de.sinnix.judoturnier.adapter.primary;

import de.sinnix.judoturnier.application.EinstellungenService;
import de.sinnix.judoturnier.application.GewichtsklassenService;
import de.sinnix.judoturnier.application.TurnierService;
import de.sinnix.judoturnier.application.WettkaempferService;
import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Begegnung;
import de.sinnix.judoturnier.model.Bewerter;
import de.sinnix.judoturnier.model.GewichtsklassenGruppe;
import de.sinnix.judoturnier.model.GruppenRunde;
import de.sinnix.judoturnier.model.Matte;
import de.sinnix.judoturnier.model.Turnier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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

	@GetMapping("/")
	public ModelAndView startPage() {
		var s = SecurityContextHolder.getContext().getAuthentication();
		logger.info("Turniere-Startseite. User {} {}", s.getName(), s.getAuthorities());

		ModelAndView mav = new ModelAndView("startseite");
		return mav;
	}

	@GetMapping("/turnier")
	public ModelAndView turniere() {
		logger.debug("lade vorhandene Turniere");
		var s = SecurityContextHolder.getContext().getAuthentication();
		logger.info("Eingeloggter User {} {}", s.getName(), s.getAuthorities());

		List<Turnier> turniere = turnierService.ladeTurniere();

		ModelAndView mav = new ModelAndView("turniere");
		mav.addObject("turniere", turniere);
		mav.addObject("anzahlturniere", turniere.size());
		return mav;
	}

	@PostMapping("/turnier")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ModelAndView erstelleTurnier(@RequestBody MultiValueMap<String, String> formData) {
		logger.debug("erstelle ein neues Turnier {}", formData);
		Bewerter bewerter = HelperSource.extractBewerter(SecurityContextHolder.getContext().getAuthentication());

		var name = formData.get("name").getFirst();
		var ort = formData.get("ort").getFirst();
		var datum = formData.get("datum").getFirst();

		Turnier turnier = turnierService.erstelleTurnier(name, ort, datum);

		return new ModelAndView("redirect:/turnier/" + turnier.uuid().toString() );
	}

	@GetMapping("/turnier/{turnierid}")
	public ModelAndView turnierUebersicht(@PathVariable String turnierid) {
		logger.debug("Turnierübersicht {} angefragt, eingeloggt", turnierid);

		// aktuell nur zum prüfen...
		Bewerter bewerter = HelperSource.extractBewerter(SecurityContextHolder.getContext().getAuthentication());
		Turnier t = turnierService.ladeTurnier(turnierid);

		var wks = wettkaempferService.alleKaempfer(UUID.fromString(turnierid));
		var einstellungen = einstellungenService.ladeEinstellungen(UUID.fromString(turnierid));

		ModelAndView mav = new ModelAndView("turnieruebersicht");
		mav.addObject("anzahlwk", wks.size());
		mav.addObject("turniertyp", einstellungen.turnierTyp());
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
	public ModelAndView ladeWettkampfreihenfolgeJeMatteRandori(@PathVariable String turnierid, @RequestParam(value = "error", required = false) String error) {
		var turnierUUID = UUID.fromString(turnierid);
		List<GewichtsklassenGruppe> gwks = gewichtsklassenService.ladeGewichtsklassenGruppen(turnierUUID);
		List<Matte> wettkampfreihenfolgeJeMatte = turnierService.ladeWettkampfreihenfolge(turnierUUID);
		Set<Altersklasse> altersklassen = gwks.stream()
			.map(GewichtsklassenGruppe::altersKlasse)
			.collect(Collectors.toSet());

		logger.trace("wettkampfreihenfolgeJeMatte {} ", wettkampfreihenfolgeJeMatte);
		ModelAndView mav = new ModelAndView("begegnungen_randori");
		mav.addObject("turnierid", turnierid);
		mav.addObject("gewichtsklassenGruppe", gwks);
		mav.addObject("matten", wettkampfreihenfolgeJeMatte);
		mav.addObject("altersklassen", altersklassen);
		mav.addObject("preverror", error);
		return mav;
	}

	@GetMapping("/turnier/{turnierid}/begegnungen/normal")
	public ModelAndView ladeWettkampfreihenfolgeJeMatteNormal(@PathVariable String turnierid) {
		var turnierUUID = UUID.fromString(turnierid);
		List<GewichtsklassenGruppe> gwks = gewichtsklassenService.ladeGewichtsklassenGruppen(turnierUUID);
		List<Matte> wettkampfreihenfolgeJeMatte = turnierService.ladeWettkampfreihenfolge(turnierUUID);

		ModelAndView mav = new ModelAndView("begegnungen_normal");
		mav.addObject("turnierid", turnierid);
		mav.addObject("gewichtsklassenGruppe", gwks);
		mav.addObject("matten", wettkampfreihenfolgeJeMatte);
		return mav;
	}

	@PostMapping("/turnier/{turnierid}/begegnungen")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ModelAndView erstelleWettkampfreihenfolgeJeMatte(@PathVariable String turnierid) {
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

	@PostMapping("/turnier/{turnierid}/begegnung")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ModelAndView erneuerWettkampfreihenfolgeFuerAltersklasse(@PathVariable String turnierid, @RequestBody Altersklasse ak) {
		String error = "";
		try {
			turnierService.loescheWettkampfreihenfolgeAltersklasse(ak, UUID.fromString(turnierid));
			turnierService.erstelleWettkampfreihenfolgeAltersklasse(Optional.of(ak), UUID.fromString(turnierid));
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
	public ModelAndView entferneWettkampfreihenfolgeFuerAltersklasse(@PathVariable String turnierid) {
		logger.info("Lösche alle Begegnungen...");
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

	@GetMapping("/turnier/{turnierid}/begegnungen/randori_printview_matches/{altersklasse}")
	public ModelAndView ladeDruckAnsichtBegegnungenRandori(@PathVariable String turnierid, @PathVariable String altersklasse) {
		var turnierUUID = UUID.fromString(turnierid);
		List<Matte> wettkampfreihenfolgeJeMatte = turnierService.ladeWettkampfreihenfolge(turnierUUID).stream()
			.sorted(Comparator.comparingInt(Matte::id))
			.toList();
		List<Matte> wettkampfreihenfolgeJeMatteGefiltert = wettkampfreihenfolgeJeMatte.stream()
			.filter(matte -> matte.runden().stream().anyMatch(r -> r.altersklasse().name().equals(altersklasse)))
			.collect(Collectors.toList());
		List<Matte> wettkampfreihenfolgeJeMatteGefiltertUndGruppiert = gruppiereNachGruppen(wettkampfreihenfolgeJeMatteGefiltert);

		ModelAndView mav = new ModelAndView("druckansicht_begegnungen_randori");
		mav.addObject("turnierid", turnierid);
		mav.addObject("matten", wettkampfreihenfolgeJeMatteGefiltertUndGruppiert);
		return mav;
	}

	@GetMapping("/turnier/{turnierid}/begegnungen/randori_printview_matches_inserting_data/{altersklasse}")
	public ModelAndView ladeDruckAnsichtBegegnungenRandoriDateneintrag(@PathVariable String turnierid, @PathVariable String altersklasse) {
		var turnierUUID = UUID.fromString(turnierid);
		List<Matte> wettkampfreihenfolgeJeMatte = turnierService.ladeWettkampfreihenfolge(turnierUUID);
		List<Matte> wettkampfreihenfolgeJeMatteGefiltert = wettkampfreihenfolgeJeMatte.stream()
			.filter(matte -> matte.runden().stream().anyMatch(r -> r.altersklasse().name().equals(altersklasse)))
			.collect(Collectors.toList());
		List<Matte> wettkampfreihenfolgeJeMatteGefiltertUndGruppiert = gruppiereNachGruppen(wettkampfreihenfolgeJeMatteGefiltert);

		ModelAndView mav = new ModelAndView("druckansicht_begegnungen_randori_inserting_data");
		mav.addObject("turnierid", turnierid);
		mav.addObject("matten", wettkampfreihenfolgeJeMatteGefiltertUndGruppiert);
		return mav;
	}

	@GetMapping("/turnier/{turnierid}/begegnungen/randori/{id}")
	public ModelAndView begegnungRandori(@PathVariable String turnierid, @PathVariable String id) {
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

	private List<Matte> gruppiereNachGruppen(List<Matte> matten) {
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

			return new Matte(mat.id(), new ArrayList<>(), gruppenRunden);
		}).collect(Collectors.toList());
	}
}
