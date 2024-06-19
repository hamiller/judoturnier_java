package de.sinnix.judoturnier.application;

import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.GewichtsklassenGruppe;
import de.sinnix.judoturnier.model.Wettkaempfer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class GewichtsklassenGruppenService {

    private static final Logger logger = LogManager.getLogger(GewichtsklassenGruppenService.class);

    public List<GewichtsklassenGruppe> lade() {
        logger.warn("lade not yet implemented");
        return List.of();
    }

    public List<GewichtsklassenGruppe> teileInGewichtsklassen(List<Wettkaempfer> wks) {
        logger.warn("teileInGewichtsklassen not yet implemented");
        return List.of();
    }

    public void loescheAlles() {
        logger.warn("loescheAlles not yet implemented");
    }

    public void speichere(List<GewichtsklassenGruppe> gwks) {
        logger.warn("speichere not yet implemented");
    }

    public void loescheAltersklasse(Altersklasse altersklasse) {
        logger.warn("loescheAltersklasse not yet implemented");
    }

    public void aktualisiere(HashMap<Integer, List<Integer>> gruppenTeilnehmer) {
        logger.warn("aktualisiere not yet implemented");
    }
}
