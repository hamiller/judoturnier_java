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
		if (turnier.uuid() != null) jpa.setUuid(turnier.uuid().toString());
		jpa.setName(turnier.name());
		jpa.setOrt(turnier.ort());
		jpa.setDatum(new java.sql.Date(turnier.datum().getTime()));
		return jpa;
	}
}
