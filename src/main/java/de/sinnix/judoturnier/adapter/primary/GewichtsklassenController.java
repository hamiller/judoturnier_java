package de.sinnix.judoturnier.adapter.primary;

import de.sinnix.judoturnier.application.EinstellungenService;
import de.sinnix.judoturnier.application.GewichtsklassenService;
import de.sinnix.judoturnier.application.WettkaempferService;
import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Geschlecht;
import de.sinnix.judoturnier.model.GewichtsklassenGruppe;
import de.sinnix.judoturnier.model.GewichtsklassenGruppen;
import de.sinnix.judoturnier.model.OidcBenutzer;
import de.sinnix.judoturnier.model.TurnierTyp;
import jakarta.annotation.security.PermitAll;
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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RestController
public class GewichtsklassenController {
	private static final Logger logger = LogManager.getLogger(GewichtsklassenController.class);
	private static final String REGEX  = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}";

	@Autowired
	private WettkaempferService    wettkaempferService;
	@Autowired
	private GewichtsklassenService gewichtsklassenService;
	@Autowired
	private EinstellungenService   einstellungenService;

	@GetMapping("/turnier/{turnierid}/gewichtsklassen")
	public ModelAndView ladeGewichtsklassen(@PathVariable String turnierid) {
		logger.info("GewichtsklassenController ladeGewichtsklassen, Turnier {}", turnierid);

		OidcBenutzer oidcBenutzer = HelperSource.extractOidcBenutzer(SecurityContextHolder.getContext().getAuthentication());
		var turnierUUID = UUID.fromString(turnierid);
		var wks = wettkaempferService.alleKaempfer(turnierUUID);
		var currentGwks = gewichtsklassenService.ladeGewichtsklassenGruppen(turnierUUID);

		var groupedByAge = this.groupByAge(currentGwks);
		var groupedByFemale = this.groupByGender(currentGwks, Geschlecht.w); currentGwks.stream().filter(gruppe -> gruppe.gruppenGeschlecht().isPresent() && gruppe.gruppenGeschlecht().get() == Geschlecht.w);
		var groupedByMale = this.groupByGender(currentGwks, Geschlecht.m);

		logger.trace("groupedByAge: {}", groupedByAge);
		logger.trace("groupedByFemale: {}", groupedByFemale);
		logger.trace("groupedByMale: {}", groupedByMale);

		var einstellungen = einstellungenService.ladeEinstellungen(UUID.fromString(turnierid));
		var anzahlwkInGroups = currentGwks.stream()
			.mapToInt(group -> group.teilnehmer().size())
			.sum();

		ModelAndView mav = new ModelAndView("gewichtsklassen");
		mav.addObject("turnierid", turnierid);
		mav.addObject("isadmin", oidcBenutzer.istAdmin());
		mav.addObject("gewichtsklassengruppenWeiblich", groupedByFemale);
		mav.addObject("gewichtsklassengruppenMaennlich", groupedByMale);
		mav.addObject("gewichtsklassengruppenAlter", groupedByAge);
		mav.addObject("anzahlwk", wks.size());
		mav.addObject("anzahlwkInGroups", anzahlwkInGroups);
		mav.addObject("anzahlUnterschiedlich", anzahlwkInGroups != wks.size());
		mav.addObject("standardturnier", einstellungen.turnierTyp() == TurnierTyp.STANDARD);
		return mav;
	}

	@PermitAll
	@GetMapping("/turnier/{turnierid}/gewichtsklassen/randori_printview_groups/{altersklasse}")
	public ModelAndView ladeDruckAnsichtGruppenRandori(@PathVariable String turnierid, @PathVariable String altersklasse) {
		logger.info("lade Druckansicht Randori-Gruppen für " + altersklasse);
		var turnierUUID = UUID.fromString(turnierid);
		var currentGwks = gewichtsklassenService.ladeGewichtsklassenGruppen(turnierUUID);

		ModelAndView mav = new ModelAndView("druckansicht_gruppen_randori");
		mav.addObject("turnierid", turnierid);
		mav.addObject("gruppen", currentGwks.stream().filter(gwk -> gwk.altersKlasse().name().equalsIgnoreCase(altersklasse)).toList());
		return mav;
	}

	@PermitAll
	@GetMapping("/turnier/{turnierid}/gewichtsklassen/turnier_printview_groups/{geschlecht}/{altersklasse}")
	public ModelAndView ladeDruckAnsichtGruppenTurnier(@PathVariable String turnierid, @PathVariable String geschlecht, @PathVariable String altersklasse) {
		logger.info("Lade Druckansicht für Turnier-Gruppen {}, Geschlecht {} und Altersklasse {}", turnierid, geschlecht, altersklasse);
		var turnierUUID = UUID.fromString(turnierid);
		var currentGwks = gewichtsklassenService.ladeGewichtsklassenGruppen(turnierUUID);

		ModelAndView mav = new ModelAndView("druckansicht_gruppen_turnier");
		mav.addObject("turnierid", turnierid);
		mav.addObject("gruppen", currentGwks.stream().filter(gwk -> gwk.altersKlasse().name().equalsIgnoreCase(altersklasse)).toList());
		return mav;
	}

	@PostMapping("/turnier/{turnierid}/gewichtsklassen-renew")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ModelAndView erstelleGewichtsklassenNeu(@PathVariable String turnierid) {
		logger.info("erstelle Gewichtsklassen");
		var wks = wettkaempferService.alleKaempfer(UUID.fromString(turnierid));
		var gwks = gewichtsklassenService.teileInGewichtsklassen(wks, UUID.fromString(turnierid));
		gewichtsklassenService.loescheAlles(UUID.fromString(turnierid));
		gewichtsklassenService.speichere(gwks);
		return new ModelAndView("redirect:/turnier/" + turnierid + "/gewichtsklassen");
	}

	@PostMapping("/turnier/{turnierid}/gewichtsklasse-renew")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ModelAndView erstelleGewichtsklasseNeu(@PathVariable String turnierid, @RequestBody MultiValueMap<String, String> formData) {
		logger.info("erneuere Gewichtsklasse für Altersklasse und Geschlecht {}", formData);
		if (formData == null || formData.isEmpty()) {
			throw new IllegalArgumentException();
		}

		var turnierUUID = UUID.fromString(turnierid);
		var altersklasse = Altersklasse.valueOf(formData.getFirst("altersklasse"));
		var geschlecht = (formData.keySet().contains("geschlecht") && formData.getFirst("geschlecht") != "") ? Geschlecht.valueOf(formData.getFirst("geschlecht")) : null;
		var wk = (wettkaempferService.alleKaempfer(UUID.fromString(turnierid))).stream()
			.filter(kaempfer -> kaempfer.altersklasse() == altersklasse)
			.filter(kaempfer -> geschlecht != null ? kaempfer.geschlecht() == geschlecht : true)
			.collect(Collectors.toList());
		var gwks = gewichtsklassenService.teileInGewichtsklassen(wk, UUID.fromString(turnierid));

		gewichtsklassenService.loescheAltersklasse(turnierUUID, altersklasse);
		gewichtsklassenService.speichere(gwks);

		return new ModelAndView("redirect:/turnier/" + turnierid + "/gewichtsklassen");
	}

	@PostMapping("/turnier/{turnierid}/gewichtsklassen")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ModelAndView speichereGewichtsklassen(@PathVariable String turnierid, @RequestBody MultiValueMap<String, String> formData) {
		logger.info("speichere Gewichtsklassen {}", formData);

		var gruppenTeilnehmer = new HashMap<UUID, List<UUID>>();
		for (String w : formData.get("gruppen_teilnehmer")) {
			String[] ids = Pattern.compile(REGEX)
				.matcher(w)
				.results()
				.map(r -> r.group())
				.toArray(String[]::new);

			var gruppeId = UUID.fromString(ids[0]);
			var teilnehmerId = UUID.fromString(ids[1]);

			if (!gruppenTeilnehmer.containsKey(gruppeId)) {
				gruppenTeilnehmer.put(gruppeId, new ArrayList());
			}
			gruppenTeilnehmer.get(gruppeId).add(teilnehmerId);
		}

		gewichtsklassenService.aktualisiere(gruppenTeilnehmer, UUID.fromString(turnierid));

		return new ModelAndView("redirect:/turnier/" + turnierid + "/gewichtsklassen");
	}

	private List<GewichtsklassenGruppen> groupByAge(List<GewichtsklassenGruppe> gwk) {
		// Gruppieren nach Altersklasse
		Map<Altersklasse, List<GewichtsklassenGruppe>> groupedByAge = gwk.stream()
			.collect(Collectors.groupingBy(GewichtsklassenGruppe::altersKlasse));

		// Erstellen der GewichtsklassenGruppen Objekte
		return groupedByAge.entrySet().stream()
			.map(entry -> {
				Altersklasse altersKlasse = entry.getKey();
				List<GewichtsklassenGruppe> gruppen = entry.getValue();
				int anzahlTeilnehmer = gruppen.stream()
					.mapToInt(g -> g.teilnehmer().size())
					.sum();
				return new GewichtsklassenGruppen(altersKlasse, anzahlTeilnehmer, gruppen);
			})
			.sorted(Comparator.comparingInt(a -> a.altersKlasse().getReihenfolge()))
			.collect(Collectors.toList());
	}

	private List<GewichtsklassenGruppen> groupByGender(List<GewichtsklassenGruppe> gwk, Geschlecht geschlecht) {
		// Filtern nach Geschlecht
		List<GewichtsklassenGruppe> groupeByGender = gwk.stream().filter(gruppe -> gruppe.gruppenGeschlecht().isPresent() && gruppe.gruppenGeschlecht().get().equals(geschlecht)).collect(Collectors.toList());
		// Gruppieren nach Alter
		return groupByAge(groupeByGender);
	}

}
