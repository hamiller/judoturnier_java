package de.sinnix.judoturnier.adapter.secondary.converter;

import de.sinnix.judoturnier.adapter.secondary.jpa.BenutzerJpa;
import de.sinnix.judoturnier.adapter.secondary.jpa.TurnierRollenJpa;
import de.sinnix.judoturnier.model.Benutzer;
import de.sinnix.judoturnier.model.TurnierRollen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BenutzerConverter {
	private static final Logger logger = LogManager.getLogger(BenutzerConverter.class);

	public Benutzer convertToBenutzer(BenutzerJpa jpa) {
		logger.trace("BenutzerConverter.convertToBenutzer {}", jpa);
		if (jpa == null) {
			return null;
		}

		return new Benutzer(
			jpa.getId(),
			jpa.getUsername(),
			jpa.getName(),
			jpa.getTurnierRollen().stream()
				.map(turnierRolle -> {
					return new TurnierRollen(turnierRolle.getId(),
						turnierRolle.getTurnierUuid(),
						turnierRolle.getRollen());
				})
				.collect(Collectors.toList()),
			jpa.getRollen().stream().collect(Collectors.toList()));
	}

	public BenutzerJpa convertFromBenutzer(Benutzer benutzer) {
		logger.trace("BenutzerConverter.convertFromBenutzer {}", benutzer);
		BenutzerJpa jpa = new BenutzerJpa();
		jpa.setUsername(benutzer.username());
		jpa.setName(benutzer.name());
		jpa.setTurnierRollen(convertFromTurnierRollen(benutzer.turnierRollen(), jpa));
		jpa.setRollen(benutzer.benutzerRollen());
		return jpa;
	}

	private List<TurnierRollenJpa> convertFromTurnierRollen(List<TurnierRollen> turnierRollen, BenutzerJpa benutzer) {
		logger.trace("BenutzerConverter.convertFromTurnierRollen {}", turnierRollen);
		if (turnierRollen == null) {
			return new ArrayList<>();
		}
		return turnierRollen.stream().map(turnierRolle -> {
				TurnierRollenJpa jpa = new TurnierRollenJpa();
				jpa.setBenutzer(benutzer);
				jpa.setTurnierUuid(turnierRolle.turnierId() != null ? turnierRolle.turnierId() : null);
				jpa.setRollen(turnierRolle.rollen());
				return jpa;
			})
			.collect(Collectors.toList());
	}
}
