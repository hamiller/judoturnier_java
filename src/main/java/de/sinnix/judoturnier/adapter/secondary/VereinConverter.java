package de.sinnix.judoturnier.adapter.secondary;

import de.sinnix.judoturnier.model.Verein;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class VereinConverter {
	public Verein converToVerein(VereinJpa jpa) {
		return new Verein(jpa.getId(), jpa.getName(), UUID.fromString(jpa.getTurnierUUID()));
	}

	public VereinJpa convertFromVerein(Verein verein) {
		return new VereinJpa(verein.id(), verein.name(), verein.turnierUUID().toString());
	}
}
