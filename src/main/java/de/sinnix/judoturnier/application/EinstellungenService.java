package de.sinnix.judoturnier.application;

import de.sinnix.judoturnier.adapter.secondary.EinstellungJpa;
import de.sinnix.judoturnier.adapter.secondary.EinstellungRepository;
import de.sinnix.judoturnier.model.Einstellungen;
import de.sinnix.judoturnier.model.MattenAnzahl;
import de.sinnix.judoturnier.model.TurnierTyp;
import de.sinnix.judoturnier.model.WettkampfReihenfolge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EinstellungenService {
    private static final Logger logger = LogManager.getLogger(EinstellungenService.class);

    @Autowired
    private EinstellungRepository einstellungRepository;

    public Einstellungen ladeEinstellungen() {
        logger.info("EinstellungenService ladeEinstellungen()");
        List<EinstellungJpa> einstellungenList = einstellungRepository.findAll();
        TurnierTyp turnierTyp = einstellungenList.stream().filter(e -> e.art().equalsIgnoreCase(TurnierTyp.TYP)).findFirst().map(t -> TurnierTyp.valueOf(t.wert())).orElseThrow();
        MattenAnzahl mattenAnzahl = einstellungenList.stream().filter(e -> e.art().equalsIgnoreCase(MattenAnzahl.TYP)).findFirst().map(t -> new MattenAnzahl(Integer.parseInt(t.wert()))).orElseThrow();
        WettkampfReihenfolge wettkampfReihenfolge = einstellungenList.stream().filter(e -> e.art().equalsIgnoreCase(WettkampfReihenfolge.TYP)).findFirst().map(t -> WettkampfReihenfolge.valueOf(t.wert())).orElseThrow();
        return new Einstellungen(turnierTyp, mattenAnzahl, wettkampfReihenfolge);
    }
}
