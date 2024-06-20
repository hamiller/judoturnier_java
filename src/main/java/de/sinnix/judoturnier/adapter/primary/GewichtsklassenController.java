package de.sinnix.judoturnier.adapter.primary;

import de.sinnix.judoturnier.application.EinstellungenService;
import de.sinnix.judoturnier.application.GewichtsklassenService;
import de.sinnix.judoturnier.application.WettkaempferService;
import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Geschlecht;
import de.sinnix.judoturnier.model.GewichtsklassenGruppe;
import de.sinnix.judoturnier.model.GewichtsklassenGruppen;
import de.sinnix.judoturnier.model.TurnierTyp;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RestController
public class GewichtsklassenController {
    private static final Logger logger = LogManager.getLogger(GewichtsklassenController.class);
    private static final Pattern REGEX = Pattern.compile("/^gruppe_(\\d+)_teilnehmer_(\\d+)$/");

    @Autowired
    private WettkaempferService wettkaempferService;
    @Autowired
    private GewichtsklassenService gewichtsklassenService;

    @Autowired
    private EinstellungenService einstellungenService;

    @GetMapping("/gewichtsklassen")
    public ModelAndView ladeGewichtsklassen() {
        var wks = wettkaempferService.alleKaempfer();
        var currentGwks = gewichtsklassenService.lade();

        var groupedByAge = this.groupByAge(currentGwks);
        var einstellungen = einstellungenService.ladeEinstellungen();
        var anzahlwkInGroups = currentGwks.stream()
                .mapToInt(group -> group.teilnehmer().size())
                .sum();


        ModelAndView mav = new ModelAndView("gewichtsklassen");
        mav.addObject("gewichtsklassengruppenWeiblich", currentGwks.stream().filter(gruppe -> gruppe.gruppenGeschlecht().isPresent() && gruppe.gruppenGeschlecht().get() == Geschlecht.w));
        mav.addObject("gewichtsklassengruppenMaennlich", currentGwks.stream().filter(gruppe -> gruppe.gruppenGeschlecht().isPresent() && gruppe.gruppenGeschlecht().get() == Geschlecht.m));
        mav.addObject("anzahlwk", wks.size());
        mav.addObject("anzahlwkInGroups", anzahlwkInGroups);
        mav.addObject("anzahlUnterschiedlich", anzahlwkInGroups != wks.size());
        mav.addObject("standardturnier", einstellungen.turnierTyp() == TurnierTyp.STANDARD);
        mav.addObject("gruppiertBeiAlter", groupedByAge);
        return mav;
    }

    @GetMapping("/gewichtsklassen/randori_printview_groups/{altersklasse}")
    public ModelAndView ladeDruckAnsichtGruppenRandori(@PathVariable("altersklasse") String altersklasse) {
        logger.debug("lade Druckansicht Randori-Gruppen für " + altersklasse);
        var currentGwks = gewichtsklassenService.lade();

        ModelAndView mav = new ModelAndView("druckansicht_gruppen_randori");
        mav.addObject("gruppen", currentGwks.stream().filter(gwk -> gwk.altersKlasse().name() == altersklasse));
        return mav;
    }

    @PostMapping("/gewichtsklassen-renew")
    public ModelAndView erstelleGewichtsklassenNeu() {
        logger.debug("erstelle Gewichtsklassen");
        var wks = wettkaempferService.alleKaempfer();
        var gwks = gewichtsklassenService.teileInGewichtsklassen(wks);
        gewichtsklassenService.loescheAlles();
        gewichtsklassenService.speichere(gwks);
        return new ModelAndView("redirect:/gewichtsklassen");
    }

    @PostMapping("/gewichtsklasse-renew")
    public ModelAndView erstelleGewichtsklasseNeu(@RequestBody Altersklasse altersklasse) {
        logger.debug("erneuere Gewichtsklasse für Altersklasse {}", altersklasse);
        var wk = (wettkaempferService.alleKaempfer()).stream()
                .filter(kaempfer -> kaempfer.altersklasse() == altersklasse)
                .collect(Collectors.toList());
        var gwks = gewichtsklassenService.teileInGewichtsklassen(wk);

        gewichtsklassenService.loescheAltersklasse(altersklasse);
        gewichtsklassenService.speichere(gwks);

        return new ModelAndView("redirect:/gewichtsklassen");
    }

    @PostMapping("/gewichtsklassen")
    public ModelAndView speichereGewichtsklassen(@RequestBody MultiValueMap<String, String> formData) {
        logger.debug("speichere Gewichtsklassen {}", formData);

        var gruppenTeilnehmer = new HashMap<Integer, List<Integer>>();
        for (String w : formData.get("gruppen_teilnehmer")) {
            var match = REGEX.matcher(w);
            var gruppeNummer = Integer.parseInt(match.group(1), 10);
            var teilnehmerNummer = Integer.parseInt(match.group(2), 10);

            if (!gruppenTeilnehmer.containsKey(gruppeNummer)) {
                gruppenTeilnehmer.put(gruppeNummer, new ArrayList());
            }
            gruppenTeilnehmer.get(gruppeNummer).add(teilnehmerNummer);
        }

        gewichtsklassenService.aktualisiere(gruppenTeilnehmer);

        return new ModelAndView("redirect:/gewichtsklassen");
    }

    private List<GewichtsklassenGruppen> groupByAge(List<GewichtsklassenGruppe> gwk ) {
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
                .collect(Collectors.toList());
    }
}
