package de.sinnix.judoturnier.adapter.primary;

import de.sinnix.judoturnier.application.EinstellungenService;
import de.sinnix.judoturnier.application.GewichtsklassenService;
import de.sinnix.judoturnier.application.WettkaempferService;
import de.sinnix.judoturnier.model.Einstellungen;
import de.sinnix.judoturnier.model.Kampfsystem;
import de.sinnix.judoturnier.model.MattenAnzahl;
import de.sinnix.judoturnier.model.RandoriGruppengroesse;
import de.sinnix.judoturnier.model.SeparateAlterklassen;
import de.sinnix.judoturnier.model.TurnierTyp;
import de.sinnix.judoturnier.model.VariablerGewichtsteil;
import de.sinnix.judoturnier.model.WettkampfReihenfolge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@RestController
public class EinstellungenController {

	private static final Logger logger = LogManager.getLogger(EinstellungenController.class);

	@Autowired
	private EinstellungenService   einstellungenService;
	@Autowired
	private WettkaempferService    wiegenService;
	@Autowired
	private GewichtsklassenService gewichtsklassenService;

	@GetMapping("/turnier/{turnierid}/einstellungen")
	public ModelAndView ladeEinstellungen(@PathVariable String turnierid) {
		logger.debug("Lade Einstellungen");

		var turnierUUID = UUID.fromString(turnierid);
		var einstellungen = einstellungenService.ladeEinstellungen(turnierUUID);
		var wks = wiegenService.alleKaempfer(turnierUUID);
		var gwks = gewichtsklassenService.ladeGewichtsklassenGruppen(turnierUUID);

		ModelAndView mav = new ModelAndView("einstellungen");
		mav.addObject("turnierid", turnierid);
		mav.addObject("gewichtsklassengruppen", gwks);
		mav.addObject("anzahlwk", wks.size());
		mav.addObject("kampfsysteme", Kampfsystem.values());
		mav.addObject("turniertyp", einstellungen.turnierTyp());
		mav.addObject("mattenanzahl", einstellungen.mattenAnzahl());
		mav.addObject("wettkampfreihenfolge", einstellungen.wettkampfReihenfolge());
		mav.addObject("randorigruppengroesse", einstellungen.randoriGruppengroesse());
		mav.addObject("variablergewichtsteil", einstellungen.variablerGewichtsteil());
		mav.addObject("separatealtersklassen", einstellungen.separateAlterklassen());
		return mav;
	}

	@PostMapping("/turnier/{turnierid}/einstellungen")
	public ModelAndView speichereTurnierEinstellungen(@PathVariable String turnierid, @RequestBody MultiValueMap<String, String> formData) {
		logger.debug("speichere Turnier-Einstellungen {}", formData);

		var turnierTyp = TurnierTyp.valueOf(formData.getFirst(TurnierTyp.TYP));
		var mattenAnzahl = new MattenAnzahl(Integer.parseInt(formData.getFirst(MattenAnzahl.TYP)));
		var wettkampfReihenfolge = WettkampfReihenfolge.valueOf(formData.getFirst(WettkampfReihenfolge.TYP));
		var randoriGruppengroesse = new RandoriGruppengroesse(Integer.parseInt(formData.getFirst(RandoriGruppengroesse.TYP)));
		var variablerGewichtsteil = new VariablerGewichtsteil(Double.parseDouble(formData.getFirst(VariablerGewichtsteil.TYP)));
		var separateAltersklassen = SeparateAlterklassen.valueOf(formData.getFirst(SeparateAlterklassen.TYP));
		var turnierUUID = UUID.fromString(turnierid);

		var einstellungen = new Einstellungen(turnierTyp, mattenAnzahl, wettkampfReihenfolge, randoriGruppengroesse, variablerGewichtsteil, separateAltersklassen, turnierUUID);

		einstellungen = einstellungenService.speichereTurnierEinstellungen(einstellungen);
		var wks = wiegenService.alleKaempfer(UUID.fromString(turnierid));
		var gwks = gewichtsklassenService.ladeGewichtsklassenGruppen(turnierUUID);

		ModelAndView mav = new ModelAndView("einstellungen");
		mav.addObject("turnierid", turnierid);
		mav.addObject("gewichtsklassengruppen", gwks);
		mav.addObject("anzahlwk", wks.size());
		mav.addObject("kampfsysteme", Kampfsystem.values());
		mav.addObject("turniertyp", einstellungen.turnierTyp());
		mav.addObject("mattenanzahl", einstellungen.mattenAnzahl());
		mav.addObject("wettkampfreihenfolge", einstellungen.wettkampfReihenfolge());
		mav.addObject("randorigruppengroesse", einstellungen.randoriGruppengroesse());
		mav.addObject("variablergewichtsteil", einstellungen.variablerGewichtsteil());
		mav.addObject("separatealtersklassen", einstellungen.separateAlterklassen());
		return mav;
	}
}
