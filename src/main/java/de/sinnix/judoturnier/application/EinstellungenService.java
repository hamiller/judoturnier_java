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
        TurnierTyp turnierTyp = einstellungenList.stream().filter(e -> e.getArt().equalsIgnoreCase(TurnierTyp.TYP)).findFirst().map(t -> TurnierTyp.valueOf(t.getWert())).orElseThrow();
        MattenAnzahl mattenAnzahl = einstellungenList.stream().filter(e -> e.getArt().equalsIgnoreCase(MattenAnzahl.TYP)).findFirst().map(t -> new MattenAnzahl(Integer.parseInt(t.getWert()))).orElseThrow();
        WettkampfReihenfolge wettkampfReihenfolge = einstellungenList.stream().filter(e -> e.getArt().equalsIgnoreCase(WettkampfReihenfolge.TYP)).findFirst().map(t -> WettkampfReihenfolge.valueOf(t.getWert())).orElseThrow();
        return new Einstellungen(turnierTyp, mattenAnzahl, wettkampfReihenfolge);
    }

    public Einstellungen speichereTurnierEinstellungen(Einstellungen einstellungen) {
        logger.info("EinstellungenService speichereTurnierEinstellungen()");
        List<EinstellungJpa> jpaList = List.of(
                new EinstellungJpa(einstellungen.turnierTyp().TYP, einstellungen.turnierTyp().name()),
                new EinstellungJpa(einstellungen.mattenAnzahl().TYP, einstellungen.mattenAnzahl().anzahl().toString()),
                new EinstellungJpa(einstellungen.wettkampfReihenfolge().TYP, einstellungen.wettkampfReihenfolge().name()));
        einstellungRepository.saveAll(jpaList);
        return ladeEinstellungen();
    }

    public boolean isRandori() {
        TurnierTyp turnierTyp = einstellungRepository.findById(TurnierTyp.TYP).map(typ -> TurnierTyp.valueOf(typ.getWert())).orElseThrow();
        return turnierTyp == TurnierTyp.RANDORI;
    }
}