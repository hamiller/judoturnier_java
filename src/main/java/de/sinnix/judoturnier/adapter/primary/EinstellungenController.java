package de.sinnix.judoturnier.adapter.primary;

import de.sinnix.judoturnier.application.EinstellungenService;
import de.sinnix.judoturnier.application.GewichtsklassenGruppenService;
import de.sinnix.judoturnier.application.TurnierService;
import de.sinnix.judoturnier.application.WiegenService;
import de.sinnix.judoturnier.model.Kampfsystem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class EinstellungenController {

    private static final Logger logger = LogManager.getLogger(EinstellungenController.class);

    @Autowired
    private EinstellungenService einstellungenService;
    @Autowired
    private WiegenService wiegenService;
    @Autowired
    private GewichtsklassenGruppenService gewichtsklassenGruppenService;

    @GetMapping("/turnier/einstellungen")
    public ModelAndView ladeEinstellungen() {
        logger.debug("Lade Einstellungen");

        var einstellungen = einstellungenService.ladeEinstellungen();
        var wks = wiegenService.alleKaempfer();
        var gwks = gewichtsklassenGruppenService.lade();

        ModelAndView mav = new ModelAndView("einstellungen");
        mav.addObject("gewichtsklassengruppen", gwks);
        mav.addObject("anzahlwk", wks.size());
        mav.addObject("kampfsysteme", Kampfsystem.values());
        mav.addObject("turniertyp", einstellungen.turnierTyp());
        mav.addObject("mattenanzahl", einstellungen.mattenAnzahl());
        mav.addObject("wettkampfreihenfolge", einstellungen.wettkampfReihenfolge());

        return mav;
    }
}
