package de.sinnix.judoturnier.adapter.secondary;

import de.sinnix.judoturnier.model.Turnier;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class TurnierConverter {

	public Turnier convertToTurnier(TurnierJpa jpa) {
		return new Turnier(
			UUID.fromString(jpa.getUuid()),
			jpa.getName(),
			jpa.getOrt(),
			new Date(jpa.getDatum().getTime()));
	}

	public TurnierJpa convertFromTurnier(Turnier turnier) {
		TurnierJpa jpa = new TurnierJpa();
		jpa.setUuid(fromUUID(turnier.uuid()));
		jpa.setName(turnier.name());
		jpa.setOrt(turnier.ort());
		jpa.setDatum(new java.sql.Date(turnier.datum().getTime()));
		return jpa;
	}

	private String fromUUID(UUID uuid) {
		if (uuid != null) {
			return uuid.toString();
		}

		return UUID.randomUUID().toString();
	}
}
