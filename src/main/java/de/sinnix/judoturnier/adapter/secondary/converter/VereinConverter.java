package de.sinnix.judoturnier.adapter.secondary.converter;

import de.sinnix.judoturnier.adapter.secondary.jpa.VereinJpa;
import de.sinnix.judoturnier.model.Verein;
import org.springframework.stereotype.Component;

@Component
public class VereinConverter {
	public Verein converToVerein(VereinJpa jpa) {
		if (jpa == null) {
			return null;
		}
		return new Verein(jpa.getId(), jpa.getName(), jpa.getTurnierUUID());
	}

	public VereinJpa convertFromVerein(Verein verein) {
		if (verein == null) {
			return null;
		}

		return new VereinJpa (
			verein.name(),
			verein.turnierUUID());
	}
}
