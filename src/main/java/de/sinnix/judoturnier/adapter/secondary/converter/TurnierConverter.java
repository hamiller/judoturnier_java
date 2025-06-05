package de.sinnix.judoturnier.adapter.secondary.converter;

import de.sinnix.judoturnier.adapter.secondary.jpa.TurnierJpa;
import de.sinnix.judoturnier.model.Turnier;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TurnierConverter {

	public Turnier convertToTurnier(TurnierJpa jpa) {
		return new Turnier(
			jpa.getId(),
			jpa.getName(),
			jpa.getOrt(),
			new Date(jpa.getDatum().getTime()));
	}

	public TurnierJpa convertFromTurnier(Turnier turnier) {
		TurnierJpa jpa = new TurnierJpa();
		jpa.setName(turnier.name());
		jpa.setOrt(turnier.ort());
		jpa.setDatum(new java.sql.Date(turnier.datum().getTime()));
		return jpa;
	}
}
