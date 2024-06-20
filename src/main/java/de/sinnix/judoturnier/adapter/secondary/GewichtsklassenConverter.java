package de.sinnix.judoturnier.adapter.secondary;

import de.sinnix.judoturnier.model.GewichtsklassenGruppe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GewichtsklassenConverter {

    @Autowired
    private WettkaempferConverter wettkaempferConverter;

    public GewichtsklassenGruppe convertToGewichtsklassen(GewichtsklassenJpa jpa) {
        return new GewichtsklassenGruppe(
                jpa.getId(),
                jpa.getAltersKlasse(),
                jpa.getGruppenGeschlecht(),
                wettkaempferConverter.convertToWettkaempferList(jpa.getTeilnehmer()),
                jpa.getName(),
                jpa.getMinGewicht(),
                jpa.getMaxGewicht());
    }

    public GewichtsklassenJpa convertFromGewichtsklassen(GewichtsklassenGruppe gwk) {
        GewichtsklassenJpa jpa = new GewichtsklassenJpa();
        if (gwk.id() != null) jpa.setId(gwk.id());
        jpa.setName(gwk.name());
        jpa.setAltersKlasse(gwk.altersKlasse());
        jpa.setMinGewicht(gwk.minGewicht());
        jpa.setMaxGewicht(gwk.maxGewicht());
        jpa.setGruppenGeschlecht(gwk.gruppenGeschlecht());
        jpa.setTeilnehmer(wettkaempferConverter.convertFromWettkaempferList(gwk.teilnehmer()));
        return jpa;
    }
}
