package de.sinnix.judoturnier.adapter.secondary;

import de.sinnix.judoturnier.model.Benutzer;
import de.sinnix.judoturnier.model.TurnierRollen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class BenutzerConverter {
	private static final Logger logger = LogManager.getLogger(BenutzerConverter.class);

	public Benutzer convertToBenutzer(BenutzerJpa jpa) {
		if (jpa == null) {
			return null;
		}

		return new Benutzer(
			UUID.fromString(jpa.getUuid()),
			jpa.getUsername(),
			jpa.getName(),
			jpa.getTurnierRollen().stream()
				.map(turnierRolle -> new TurnierRollen(UUID.fromString(turnierRolle.getId().getTurnierUuid()), turnierRolle.getRollen()))
				.collect(Collectors.toList()),
			jpa.getRollen().stream().collect(Collectors.toList()));
	}

	public BenutzerJpa convertFromBenutzer(Benutzer benutzer) {
		BenutzerJpa jpa = new BenutzerJpa();
		jpa.setUsername(benutzer.username());
		jpa.setName(benutzer.name());
		jpa.setTurnierRollen(convertFromTurnierRollen(benutzer.turnierRollen(), benutzer));
		jpa.setRollen(benutzer.benutzerRollen());
		return jpa;
	}

	private List<TurnierRollenJpa> convertFromTurnierRollen(List<TurnierRollen> turnierRollen, Benutzer benutzer) {
		if (turnierRollen == null) {
			return List.of();
		}
		return turnierRollen.stream().map(turnierRolle -> {
				TurnierRollenJpa jpa = new TurnierRollenJpa();
				jpa.setId(new TurnierRollenJpa.TurnierRollenId(
					benutzer.uuid() != null ? benutzer.uuid().toString() : null,
					turnierRolle.turnierId().toString()));
				jpa.setRollen(turnierRolle.rollen());
				return jpa;
			})
			.toList();
	}
}
