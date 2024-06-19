package de.sinnix.judoturnier.adapter.secondary;

import de.sinnix.judoturnier.model.Verein;
import de.sinnix.judoturnier.model.Wettkaempfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WettkaempferConverter {

    @Autowired
    private VereinConverter vereinConverter;

    public WettkaempferJpa convertFromWettkaempfer(Wettkaempfer wettkaempfer) {
        WettkaempferJpa jpa = new WettkaempferJpa();
        if (wettkaempfer.id() != null) jpa.setId(wettkaempfer.id());
        jpa.setName(wettkaempfer.name());
        jpa.setGeschlecht(wettkaempfer.geschlecht());
        jpa.setAltersklasse(wettkaempfer.altersklasse());
        jpa.setVerein(vereinConverter.convertFromVerein(wettkaempfer.verein()));
        jpa.setGewicht(wettkaempfer.gewicht());
        jpa.setFarbe(wettkaempfer.farbe());
        jpa.setChecked(wettkaempfer.checked());
        jpa.setPrinted(wettkaempfer.printed());
        return jpa;
    }

    public Wettkaempfer convertToWettkaempfer(WettkaempferJpa jpa) {
        Wettkaempfer wettkaempfer = new Wettkaempfer(
                jpa.getId(),
                jpa.getName(),
                jpa.getGeschlecht(),
                jpa.getAltersklasse(),
                vereinConverter.converToVerein(jpa.getVerein()),
                jpa.getGewicht(),
                jpa.getFarbe(),
                jpa.getChecked(),
                jpa.getPrinted()
        );
        return wettkaempfer;
    }
}
