package de.sinnix.judoturnier.adapter.secondary;

import de.sinnix.judoturnier.model.Benutzer;
import de.sinnix.judoturnier.model.TurnierRollen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
				.map(turnierRolle -> {
					return new TurnierRollen(UUID.fromString(turnierRolle.getUuid()),
						UUID.fromString(turnierRolle.getTurnierUuid()),
						turnierRolle.getRollen());
				})
				.collect(Collectors.toList()),
			jpa.getRollen().stream().collect(Collectors.toList()));
	}

	public BenutzerJpa convertFromBenutzer(Benutzer benutzer) {
		BenutzerJpa jpa = new BenutzerJpa();
		if (benutzer.uuid() != null) jpa.setUuid(benutzer.uuid().toString());
		jpa.setUsername(benutzer.username());
		jpa.setName(benutzer.name());
		jpa.setTurnierRollen(convertFromTurnierRollen(benutzer.turnierRollen(), jpa));
		jpa.setRollen(benutzer.benutzerRollen());
		return jpa;
	}

	private List<TurnierRollenJpa> convertFromTurnierRollen(List<TurnierRollen> turnierRollen, BenutzerJpa benutzer) {
		if (turnierRollen == null) {
			return new ArrayList<>();
		}
		return turnierRollen.stream().map(turnierRolle -> {
				TurnierRollenJpa jpa = new TurnierRollenJpa();
				jpa.setUuid(turnierRolle.uuid() != null ? turnierRolle.uuid().toString() : null);
				jpa.setBenutzer(benutzer);
				jpa.setTurnierUuid(turnierRolle.turnierId() != null ? turnierRolle.turnierId().toString() : null);
				jpa.setRollen(turnierRolle.rollen());
				return jpa;
			})
			.collect(Collectors.toList());
	}
}
