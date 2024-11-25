package de.sinnix.judoturnier.adapter.primary;

import de.sinnix.judoturnier.application.EinstellungenService;
import de.sinnix.judoturnier.application.GewichtsklassenService;
import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Einstellungen;
import de.sinnix.judoturnier.model.Gruppengroesse;
import de.sinnix.judoturnier.model.Kampfsystem;
import de.sinnix.judoturnier.model.MattenAnzahl;
import de.sinnix.judoturnier.model.OidcBenutzer;
import de.sinnix.judoturnier.model.SeparateAlterklassen;
import de.sinnix.judoturnier.model.TurnierTyp;
import de.sinnix.judoturnier.model.VariablerGewichtsteil;
import de.sinnix.judoturnier.model.WettkampfReihenfolge;
import de.sinnix.judoturnier.model.Wettkampfzeiten;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class EinstellungenController {

	private static final Logger logger = LogManager.getLogger(EinstellungenController.class);

	@Autowired
	private EinstellungenService   einstellungenService;
	@Autowired
	private GewichtsklassenService gewichtsklassenService;

	@GetMapping("/turnier/{turnierid}/einstellungen")
	public ModelAndView ladeEinstellungen(@PathVariable String turnierid) {
		logger.debug("Lade Einstellungen");
		OidcBenutzer oidcBenutzer = HelperSource.extractOidcBenutzer(SecurityContextHolder.getContext().getAuthentication());
		var turnierUUID = UUID.fromString(turnierid);
		var einstellungen = einstellungenService.ladeEinstellungen(turnierUUID);
		var turnierdaten = einstellungenService.ladeTurnierDaten(turnierUUID);
		var gwks = gewichtsklassenService.ladeGewichtsklassenGruppen(turnierUUID);

		List<Map.Entry<Altersklasse, Integer>> mapAsList = einstellungen.wettkampfzeiten().altersklasseKampfzeitSekunden().entrySet().stream()
			.sorted(Comparator.comparing(entry -> entry.getKey().ordinal()))
			.toList();

		logger.debug("Wettkampfzeiten: {}", mapAsList);

		ModelAndView mav = new ModelAndView("einstellungen");
		mav.addObject("turnierid", turnierid);
		mav.addObject("isadmin", oidcBenutzer.istAdmin());
		mav.addObject("gewichtsklassengruppen", gwks);
		mav.addObject("kampfsysteme", Kampfsystem.values());
		mav.addObject("turniertyp", einstellungen.turnierTyp());
		mav.addObject("mattenanzahl", einstellungen.mattenAnzahl());
		mav.addObject("wettkampfreihenfolge", einstellungen.wettkampfReihenfolge());
		mav.addObject("gruppengroesse", einstellungen.gruppengroesse());
		mav.addObject("variablergewichtsteil", einstellungen.variablerGewichtsteil());
		mav.addObject("separatealtersklassen", einstellungen.separateAlterklassen());
		mav.addObject("wettkampfzeiten", mapAsList);
		mav.addObject("turniername", turnierdaten.name());
		mav.addObject("turnierort", turnierdaten.ort());
		mav.addObject("turnierdatum", turnierdaten.datum());
		return mav;
	}

	@PostMapping("/turnier/{turnierid}/einstellungen")
	public ModelAndView speichereTurnierEinstellungen(@PathVariable String turnierid, @RequestBody MultiValueMap<String, String> formData) {
		logger.debug("speichere Turnier-Einstellungen {}", formData);

		OidcBenutzer oidcBenutzer = HelperSource.extractOidcBenutzer(SecurityContextHolder.getContext().getAuthentication());
		var turnierTyp = TurnierTyp.valueOf(formData.getFirst(TurnierTyp.TYP));
		var mattenAnzahl = new MattenAnzahl(Integer.parseInt(formData.getFirst(MattenAnzahl.TYP)));
		var wettkampfReihenfolge = WettkampfReihenfolge.valueOf(formData.getFirst(WettkampfReihenfolge.TYP));
		var gruppengroesse = new Gruppengroesse(Integer.parseInt(formData.getFirst(Gruppengroesse.TYP)));
		var variablerGewichtsteil = new VariablerGewichtsteil(Double.parseDouble(formData.getFirst(VariablerGewichtsteil.TYP)));
		var separateAltersklassen = SeparateAlterklassen.valueOf(formData.getFirst(SeparateAlterklassen.TYP));
		var turnierUUID = UUID.fromString(turnierid);

		var wkz = formData.entrySet().stream()
			.filter(key -> key.getKey().startsWith(Wettkampfzeiten.TYP))
			.flatMap(entry -> entry.getValue().stream()
				.map(value -> new AbstractMap.SimpleEntry<>(
					Altersklasse.fromString(entry.getKey().replaceAll(Wettkampfzeiten.TYP + "\\[|\\]", "")).orElse(null),
					Integer.parseInt(value.replaceAll("[\\[\\]]", ""))
				))
			)
			.filter(entry -> entry.getKey() != null) // Filter ungültige Altersklassen
			.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

		var wettkampfzeiten = new Wettkampfzeiten(wkz);
		logger.debug("wettkampfzeiten: {}", wettkampfzeiten);
		var einstellungen = new Einstellungen(turnierTyp, mattenAnzahl, wettkampfReihenfolge, gruppengroesse, variablerGewichtsteil, separateAltersklassen, wettkampfzeiten, turnierUUID);

		einstellungenService.speichereTurnierEinstellungen(einstellungen);

		return new ModelAndView("redirect:/turnier/{turnierid}/einstellungen");
	}
}
