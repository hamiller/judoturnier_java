package de.sinnix.judoturnier.adapter.primary;

import de.sinnix.judoturnier.application.EinstellungenService;
import de.sinnix.judoturnier.application.GewichtsklassenService;
import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Einstellungen;
import de.sinnix.judoturnier.model.Gruppengroessen;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.AbstractMap;
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

		List<Map.Entry<Altersklasse, Integer>> wettkampfzeitenAsList = einstellungen.wettkampfzeiten().altersklasseKampfzeitSekunden().entrySet().stream()
			.sorted(Comparator.comparing(entry -> entry.getKey().ordinal()))
			.toList();
		List<Map.Entry<Altersklasse, Integer>> gruppengroessenAsList = einstellungen.gruppengroessen().altersklasseGruppengroesse().entrySet().stream()
			.sorted(Comparator.comparing(entry -> entry.getKey().ordinal()))
			.toList();

		logger.debug("Wettkampfzeiten: {}, Gruppengrößen: {}", wettkampfzeitenAsList, gruppengroessenAsList);

		ModelAndView mav = new ModelAndView("einstellungen");
		mav.addObject("turnierid", turnierid);
		mav.addObject("isadmin", oidcBenutzer.istAdmin());
		mav.addObject("isloggedin", oidcBenutzer.isLoggedin());
		mav.addObject("gewichtsklassengruppen", gwks);
		mav.addObject("kampfsysteme", Kampfsystem.values());
		mav.addObject("turniertyp", einstellungen.turnierTyp());
		mav.addObject("mattenanzahl", einstellungen.mattenAnzahl());
		mav.addObject("wettkampfreihenfolge", einstellungen.wettkampfReihenfolge());
		mav.addObject("gruppengroessen", gruppengroessenAsList);
		mav.addObject("variablergewichtsteil", einstellungen.variablerGewichtsteil());
		mav.addObject("separatealtersklassen", einstellungen.separateAlterklassen());
		mav.addObject("wettkampfzeiten", wettkampfzeitenAsList);
		mav.addObject("turniername", turnierdaten.name());
		mav.addObject("turnierort", turnierdaten.ort());
		mav.addObject("turnierdatum", turnierdaten.datum());
		return mav;
	}

	@PostMapping("/turnier/{turnierid}/einstellungen")
	@PreAuthorize("hasAnyRole('ROLE_AMDIN')")
	public ModelAndView speichereTurnierEinstellungen(@PathVariable String turnierid, @RequestBody MultiValueMap<String, String> formData) {
		logger.debug("speichere Turnier-Einstellungen {}", formData);

		OidcBenutzer oidcBenutzer = HelperSource.extractOidcBenutzer(SecurityContextHolder.getContext().getAuthentication());
		var turnierTyp = TurnierTyp.valueOf(formData.getFirst(TurnierTyp.TYP));
		var mattenAnzahl = new MattenAnzahl(Integer.parseInt(formData.getFirst(MattenAnzahl.TYP)));
		var wettkampfReihenfolge = WettkampfReihenfolge.valueOf(formData.getFirst(WettkampfReihenfolge.TYP));
		var variablerGewichtsteil = new VariablerGewichtsteil(Double.parseDouble(formData.getFirst(VariablerGewichtsteil.TYP)));
		var separateAltersklassen = SeparateAlterklassen.valueOf(formData.getFirst(SeparateAlterklassen.TYP));
		var turnierUUID = UUID.fromString(turnierid);

		var gg = getAltersklasseIntegerMap(formData, Gruppengroessen.TYP);
		var gruppengroessen = new Gruppengroessen(gg);
		var wkz = getAltersklasseIntegerMap(formData, Wettkampfzeiten.TYP);
		var wettkampfzeiten = new Wettkampfzeiten(wkz);
		logger.debug("wettkampfzeiten: {}, gruppengroessen: {}", wettkampfzeiten, gruppengroessen);
		var einstellungen = new Einstellungen(turnierTyp, mattenAnzahl, wettkampfReihenfolge, gruppengroessen, variablerGewichtsteil, separateAltersklassen, wettkampfzeiten, turnierUUID);

		einstellungenService.speichereTurnierEinstellungen(einstellungen);

		return new ModelAndView("redirect:/turnier/{turnierid}/einstellungen");
	}

	private static Map<Altersklasse, Integer> getAltersklasseIntegerMap(MultiValueMap<String, String> formData, String typ) {
		var wkz = formData.entrySet().stream()
			.filter(key -> key.getKey().startsWith(typ))
			.flatMap(entry -> entry.getValue().stream()
				.map(value -> new AbstractMap.SimpleEntry<>(
					Altersklasse.fromString(entry.getKey().replaceAll(typ + "\\[|\\]", "")).orElse(null),
					Integer.parseInt(value.replaceAll("[\\[\\]]", ""))
				))
			)
			.filter(entry -> entry.getKey() != null) // Filter ungültige Altersklassen
			.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
		return wkz;
	}
}
