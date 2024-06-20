package de.sinnix.judoturnier.adapter.secondary;

import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Geschlecht;
import de.sinnix.judoturnier.model.GewichtsklassenGruppe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GewichtsklassenConverter {

    @Autowired
    private WettkaempferConverter wettkaempferConverter;

    public GewichtsklassenGruppe convertToGewichtsklassen(GewichtsklassenJpa jpa) {
        return new GewichtsklassenGruppe(
                jpa.getId(),
                Altersklasse.valueOf(jpa.getAltersklasse()),
                jpa.getGruppengeschlecht() != null ? Optional.of(Geschlecht.valueOf(jpa.getGruppengeschlecht())) : Optional.empty(),
                wettkaempferConverter.convertToWettkaempferList(jpa.getTeilnehmer()),
                jpa.getName(),
                jpa.getMingewicht(),
                jpa.getMaxgewicht());
    }

    public GewichtsklassenJpa convertFromGewichtsklassen(GewichtsklassenGruppe gwk) {
        GewichtsklassenJpa jpa = new GewichtsklassenJpa();
        if (gwk.id() != null) jpa.setId(gwk.id());
        jpa.setName(gwk.name());
        jpa.setAltersklasse(gwk.altersKlasse().name());
        jpa.setMingewicht(gwk.minGewicht());
        jpa.setMaxgewicht(gwk.maxGewicht());
        jpa.setGruppengeschlecht(gwk.gruppenGeschlecht().map(g -> g.name()).orElse(null));
        jpa.setTeilnehmer(wettkaempferConverter.convertFromWettkaempferList(gwk.teilnehmer()));
        return jpa;
    }
}
