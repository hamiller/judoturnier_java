package de.sinnix.judoturnier.adapter.primary;

import de.sinnix.judoturnier.application.EinstellungenService;
import de.sinnix.judoturnier.application.GewichtsklassenService;
import de.sinnix.judoturnier.application.TurnierService;
import de.sinnix.judoturnier.application.WettkaempferService;
import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.GewichtsklassenGruppe;
import de.sinnix.judoturnier.model.GruppenRunde;
import de.sinnix.judoturnier.model.Matte;
import de.sinnix.judoturnier.model.Wertung;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
	public ModelAndView turnierUebersicht() {
		logger.debug("Turnierübersicht angefragt");
		var wks = wettkaempferService.alleKaempfer();
		var einstellungen = einstellungenService.ladeEinstellungen();

		ModelAndView mav = new ModelAndView("turnieruebersicht");
		mav.addObject("anzahlwk", wks.size());
		mav.addObject("turniertyp", einstellungen.turnierTyp());
		return mav;
	}

	@GetMapping("/turnier/begegnungen")
	public ModelAndView unterscheideBegegungen() {
		if (einstellungenService.isRandori()) {
			return new ModelAndView("redirect:/turnier/begegnungen/randori");
		} else {
			return new ModelAndView("redirect:/turnier/begegnungen/normal");
		}
	}

	@GetMapping("/turnier/begegnungen/randori")
	public ModelAndView ladeWettkampfreihenfolgeJeMatteRandori(@RequestParam(value = "error", required = false) String error) {
		List<GewichtsklassenGruppe> gwks = gewichtsklassenService.ladeGewichtsklassenGruppen();
		List<Matte> wettkampfreihenfolgeJeMatte = turnierService.ladeWettkampfreihenfolge();
		Set<Altersklasse> altersklassen = gwks.stream()
			.map(GewichtsklassenGruppe::altersKlasse)
			.collect(Collectors.toSet());

		ModelAndView mav = new ModelAndView("begegnungen_randori");
		mav.addObject("gewichtsklassenGruppe", gwks);
		mav.addObject("matten", wettkampfreihenfolgeJeMatte);
		mav.addObject("altersklassen", altersklassen);
		mav.addObject("preverror", error);
		return mav;
	}

	@GetMapping("/turnier/begegnungen/normal")
	public ModelAndView ladeWettkampfreihenfolgeJeMatteNormal() {
		List<GewichtsklassenGruppe> gwks = gewichtsklassenService.ladeGewichtsklassenGruppen();
		List<Matte> wettkampfreihenfolgeJeMatte = turnierService.ladeWettkampfreihenfolge();

		ModelAndView mav = new ModelAndView("begegnungen_normal");
		mav.addObject("gewichtsklassenGruppe", gwks);
		mav.addObject("matten", wettkampfreihenfolgeJeMatte);
		return mav;
	}

	@PostMapping("/turnier/begegnungen")
	public ModelAndView erstelleWettkampfreihenfolgeJeMatte() {
		String error = "";
		try {
			turnierService.loescheWettkampfreihenfolge();
			turnierService.erstelleWettkampfreihenfolge();
		} catch (Exception e) {
			error = e.toString();
		}
		if (einstellungenService.isRandori()) {
			return new ModelAndView("redirect:/turnier/begegnungen/randori?error=" + error);
		} else {
			return new ModelAndView("redirect:/turnier/begegnungen/normal?error=" + error);
		}
	}

	@PostMapping("/turnier/begegnung")
	public ModelAndView erneuerWettkampfreihenfolgeFuerAltersklasse(@RequestBody Altersklasse ak) {
		String error = "";
		try {
			turnierService.loescheWettkampfreihenfolgeAltersklasse(ak);
			turnierService.erstelleWettkampfreihenfolgeAltersklasse(Optional.of(ak));
		} catch (Exception e) {
			error = e.toString();
		}
		if (einstellungenService.isRandori()) {
			return new ModelAndView("redirect:/turnier/begegnungen/randori?error=" + error);
		} else {
			return new ModelAndView("redirect:/turnier/begegnungen/normal?error=" + error);
		}
	}

	@DeleteMapping("/turnier/begegnung")
	public ModelAndView entferneWettkampfreihenfolgeFuerAltersklasse() {
		String error = "";
		try {
			turnierService.loescheWettkampfreihenfolge();
		} catch (Exception e) {
			error = e.toString();
		}
		if (einstellungenService.isRandori()) {
			return new ModelAndView("redirect:/turnier/begegnungen/randori?error=" + error);
		} else {
			return new ModelAndView("redirect:/turnier/begegnungen/normal?error=" + error);
		}
	}

	@GetMapping("/turnier/begegnungen/randori_printview_matches/{altersklasse}")
	public ModelAndView ladeDruckAnsichtBegegnungenRandori(@PathVariable String altersklasse) {
		List<Matte> wettkampfreihenfolgeJeMatte = turnierService.ladeWettkampfreihenfolge().stream()
			.sorted(Comparator.comparingInt(Matte::id))
			.toList();
		List<Matte> wettkampfreihenfolgeJeMatteGefiltert = wettkampfreihenfolgeJeMatte.stream()
			.filter(matte -> matte.runden().stream().anyMatch(r -> r.altersklasse().name().equals(altersklasse)))
			.collect(Collectors.toList());
		List<Matte> wettkampfreihenfolgeJeMatteGefiltertUndGruppiert = gruppiereNachGruppen(wettkampfreihenfolgeJeMatteGefiltert);

		ModelAndView mav = new ModelAndView("druckansicht_begegnungen_randori");
		mav.addObject("matten", wettkampfreihenfolgeJeMatteGefiltertUndGruppiert);
		return mav;
	}

	@GetMapping("/turnier/begegnungen/randori_printview_matches_inserting_data/{altersklasse}")
	public ModelAndView ladeDruckAnsichtBegegnungenRandoriDateneintrag(@PathVariable String altersklasse) {
		List<Matte> wettkampfreihenfolgeJeMatte = turnierService.ladeWettkampfreihenfolge();
		List<Matte> wettkampfreihenfolgeJeMatteGefiltert = wettkampfreihenfolgeJeMatte.stream()
			.filter(matte -> matte.runden().stream().anyMatch(r -> r.altersklasse().name().equals(altersklasse)))
			.collect(Collectors.toList());
		List<Matte> wettkampfreihenfolgeJeMatteGefiltertUndGruppiert = gruppiereNachGruppen(wettkampfreihenfolgeJeMatteGefiltert);

		ModelAndView mav = new ModelAndView("druckansicht_begegnungen_randori_inserting_data");
		mav.addObject("matten", wettkampfreihenfolgeJeMatteGefiltertUndGruppiert);
		return mav;
	}

	@GetMapping("/turnier/begegnungen/randori/{id}")
	public ModelAndView begegnungRandori(@PathVariable String id) {
		Optional<Wertung> begegnung = turnierService.ladeWertungFuerWettkampf(id);

		ModelAndView mav = new ModelAndView("wettkampf_randori");
		mav.addObject("begegnung", begegnung);
		mav.addObject("begegnungid", id);
		return mav;
	}

	@PostMapping("/turnier/begegnungen/randori/{id}")
	public ModelAndView speichereBegegnungRandori(@PathVariable String id, @RequestBody Wertung wertung) {
		if (id == wertung.uuid()) {
			turnierService.speichereWertung(wertung);
		}
		return new ModelAndView("redirect:/turnier/begegnungen/randori");
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
