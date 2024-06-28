package de.sinnix.judoturnier.adapter.primary;

import de.sinnix.judoturnier.application.EinstellungenService;
import de.sinnix.judoturnier.application.GewichtsklassenService;
import de.sinnix.judoturnier.application.WettkaempferService;
import de.sinnix.judoturnier.model.Einstellungen;
import de.sinnix.judoturnier.model.Kampfsystem;
import de.sinnix.judoturnier.model.MattenAnzahl;
import de.sinnix.judoturnier.model.RandoriGruppengroesse;
import de.sinnix.judoturnier.model.TurnierTyp;
import de.sinnix.judoturnier.model.VariablerGewichtsteil;
import de.sinnix.judoturnier.model.WettkampfReihenfolge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class EinstellungenController {

	private static final Logger logger = LogManager.getLogger(EinstellungenController.class);

	@Autowired
	private EinstellungenService   einstellungenService;
	@Autowired
	private WettkaempferService    wiegenService;
	@Autowired
	private GewichtsklassenService gewichtsklassenService;

	@GetMapping("/turnier/einstellungen")
	public ModelAndView ladeEinstellungen() {
		logger.debug("Lade Einstellungen");

		var einstellungen = einstellungenService.ladeEinstellungen();
		var wks = wiegenService.alleKaempfer();
		var gwks = gewichtsklassenService.ladeGewichtsklassenGruppen();

		ModelAndView mav = new ModelAndView("einstellungen");
		mav.addObject("gewichtsklassengruppen", gwks);
		mav.addObject("anzahlwk", wks.size());
		mav.addObject("kampfsysteme", Kampfsystem.values());
		mav.addObject("turniertyp", einstellungen.turnierTyp());
		mav.addObject("mattenanzahl", einstellungen.mattenAnzahl());
		mav.addObject("wettkampfreihenfolge", einstellungen.wettkampfReihenfolge());
		return mav;
	}

	@PostMapping("/turnier/einstellungen-wettkampf")
	public ModelAndView speichereKampfsystemEinstellungen(@RequestBody MultiValueMap<String, String> formData) {
		logger.debug("speichere WettkampfGruppen-Einstellungen {}", formData);

		// todo: Speichern!
		var einstellungen = einstellungenService.ladeEinstellungen();
		var wks = wiegenService.alleKaempfer();
		var gwks = gewichtsklassenService.ladeGewichtsklassenGruppen();

		ModelAndView mav = new ModelAndView("einstellungen");
		mav.addObject("gewichtsklassengruppen", gwks);
		mav.addObject("anzahlwk", wks.size());
		mav.addObject("kampfsysteme", Kampfsystem.values());
		mav.addObject("turniertyp", einstellungen.turnierTyp());
		mav.addObject("mattenanzahl", einstellungen.mattenAnzahl());
		mav.addObject("wettkampfreihenfolge", einstellungen.wettkampfReihenfolge());
		return mav;
	}

	@PostMapping("/turnier/einstellungen")
	public ModelAndView speichereTurnierEinstellungen(@RequestBody MultiValueMap<String, String> formData) {
		logger.debug("speichere Turnier-Einstellungen {}", formData);

		var turnierTyp = TurnierTyp.valueOf(formData.getFirst(TurnierTyp.TYP));
		var mattenAnzahl = new MattenAnzahl(Integer.parseInt(formData.getFirst(MattenAnzahl.TYP)));
		var wettkampfReihenfolge = WettkampfReihenfolge.valueOf(formData.getFirst(WettkampfReihenfolge.TYP));
		var randoriGruppengroesse = new RandoriGruppengroesse(Integer.parseInt(formData.getFirst(RandoriGruppengroesse.TYP)));
		var variablerGewichtsteil = new VariablerGewichtsteil(Double.parseDouble(formData.getFirst(VariablerGewichtsteil.TYP)));

		var einstellungen = new Einstellungen(turnierTyp, mattenAnzahl, wettkampfReihenfolge, randoriGruppengroesse, variablerGewichtsteil);

		einstellungen = einstellungenService.speichereTurnierEinstellungen(einstellungen);
		var wks = wiegenService.alleKaempfer();
		var gwks = gewichtsklassenService.ladeGewichtsklassenGruppen();

		ModelAndView mav = new ModelAndView("einstellungen");
		mav.addObject("gewichtsklassengruppen", gwks);
		mav.addObject("anzahlwk", wks.size());
		mav.addObject("kampfsysteme", Kampfsystem.values());
		mav.addObject("turniertyp", einstellungen.turnierTyp());
		mav.addObject("mattenanzahl", einstellungen.mattenAnzahl());
		mav.addObject("wettkampfreihenfolge", einstellungen.wettkampfReihenfolge());
		mav.addObject("randorigruppengroesse", einstellungen.wettkampfReihenfolge());
		mav.addObject("variablergewichtsteil", einstellungen.wettkampfReihenfolge());
		return mav;
	}
}
