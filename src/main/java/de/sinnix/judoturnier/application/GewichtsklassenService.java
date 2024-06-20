package de.sinnix.judoturnier.application;

import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.GewichtsklassenGruppe;
import de.sinnix.judoturnier.model.Wettkaempfer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GewichtsklassenService {

    private static final Logger logger = LogManager.getLogger(GewichtsklassenService.class);

    @Autowired
    private IGewichtsklassenRepository gewichtsklassenRepository;

    @Autowired
    private WettkaempferService wettkaempferService;

    public List<GewichtsklassenGruppe> lade() {
        logger.info("lade alle GewichtsklassenGruppen");
        return gewichtsklassenRepository.findAll().stream()
                .sorted(Comparator.comparing(GewichtsklassenGruppe::altersKlasse))
                .sorted(Comparator.comparing(GewichtsklassenGruppe::maxGewicht))
                .collect(Collectors.toList());
    }

    public List<GewichtsklassenGruppe> teileInGewichtsklassen(List<Wettkaempfer> wks) {
        logger.warn("teileInGewichtsklassen not yet implemented");
        return List.of();
    }

    public void loescheAlles() {
        logger.info("loesche alle GewichtsklassenGruppen...");
        gewichtsklassenRepository.deleteAll();
    }

    public void speichere(List<GewichtsklassenGruppe> gwks) {
        logger.info("speichere Liste der GewichtsklassenGruppe");
        gewichtsklassenRepository.saveAll(gwks);
    }

    public void loescheAltersklasse(Altersklasse altersklasse) {
        logger.info("loesche Altersklasse {}", altersklasse);
        gewichtsklassenRepository.deleteAllByAltersklasse(altersklasse);
    }

    public void aktualisiere(HashMap<Integer, List<Integer>> gruppenTeilnehmer) {
        logger.info("aktualisiere GewichtsklassenGruppe {}", gruppenTeilnehmer);
        var gewichtsklassenGruppen = gewichtsklassenRepository.findAll();
        var wettkaempfer = wettkaempferService.alleKaempfer();

        List<GewichtsklassenGruppe> updatedGewichtsklassenGruppen = new ArrayList<>();
        for (var gewichtsklassenGruppe: gewichtsklassenGruppen) {
            var teilnehmerIds = gruppenTeilnehmer.get(gewichtsklassenGruppe.id());

            // benötigt für die leere Gruppe
            if (teilnehmerIds == null || teilnehmerIds.isEmpty()) {
                updatedGewichtsklassenGruppen.add(gewichtsklassenGruppe);
                continue;
            }

            var wettkaempferListe = getWettkeampferListe(teilnehmerIds, wettkaempfer);
            var updatedMinGewicht = wettkaempferListe.stream().map(Wettkaempfer::gewicht).min(Double::compareTo).orElse(0d);
            var updatedMaxGewicht = wettkaempferListe.stream().map(Wettkaempfer::gewicht).max(Double::compareTo).orElse(200d);
            GewichtsklassenGruppe updatedGewichtsklassenGruppe = new GewichtsklassenGruppe(
                    gewichtsklassenGruppe.id(),
                    gewichtsklassenGruppe.altersKlasse(),
                    gewichtsklassenGruppe.gruppenGeschlecht(),
                    wettkaempferListe,
                    gewichtsklassenGruppe.name(),
                    updatedMinGewicht,
                    updatedMaxGewicht
            );
            updatedGewichtsklassenGruppen.add(updatedGewichtsklassenGruppe);
        }

        gewichtsklassenRepository.saveAll(updatedGewichtsklassenGruppen);
    }

    private List<Wettkaempfer> getWettkeampferListe(List<Integer> teilnehmerIds, List<Wettkaempfer> wettkaempferList) {
        if (teilnehmerIds == null || teilnehmerIds.isEmpty()) return new ArrayList<>();

        List<Wettkaempfer> newWettkaempferList = new ArrayList<>();
        for (var id : teilnehmerIds) {
            for (var w : wettkaempferList) {
                if (id == w.id()) newWettkaempferList.add(w);
            }
        }
        return newWettkaempferList;
    }
}
