package de.sinnix.judoturnier.adapter.secondary;

import de.sinnix.judoturnier.model.Verein;
import org.springframework.stereotype.Component;

@Component
public class VereinConverter {
    public Verein converToVerein(VereinJpa jpa) {
        return new Verein(jpa.getId(), jpa.getName());
    }

    public VereinJpa convertFromVerein(Verein verein) {
        return new VereinJpa(verein.id(), verein.name());
    }
}
