package de.sinnix.judoturnier.adapter.secondary;

import de.sinnix.judoturnier.model.Verein;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class VereinConverter {
	public Verein converToVerein(VereinJpa jpa) {
		if (jpa == null) {
			return null;
		}
		return new Verein(UUID.fromString(jpa.getUuid()), jpa.getName(), UUID.fromString(jpa.getTurnierUUID()));
	}

	public VereinJpa convertFromVerein(Verein verein) {
		if (verein == null) {
			return null;
		}

		return new VereinJpa (
			verein.id() != null ? verein.id().toString() : null,
			verein.name(),
			verein.turnierUUID().toString());
	}
}
