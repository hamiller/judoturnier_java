package de.sinnix.judoturnier.application;

import de.sinnix.judoturnier.adapter.secondary.TurnierRepository;
import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Matte;
import de.sinnix.judoturnier.model.Wertung;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TurnierService {
	private static final Logger logger = LogManager.getLogger(TurnierService.class);

	@Autowired
	private TurnierRepository turnierRepository;

	public List<Matte> ladeWettkampfreihenfolge() {
		logger.warn("not implemented");
		return List.of();
	}

	public void loescheWettkampfreihenfolge() {
		logger.warn("not implemented");
	}

	public void erstelleWettkampfreihenfolge() {
		logger.warn("not implemented");
	}

	public void loescheWettkampfreihenfolgeAltersklasse(Altersklasse ak) {
		logger.warn("not implemented");
	}

	public void erstelleWettkampfreihenfolgeAltersklasse(Altersklasse ak) {
		logger.warn("not implemented");
	}

	public Wertung ladeWertungFuerWettkampf(int id) {
		logger.warn("not implemented");
		return null;
	}

	public void speichereWertung(Wertung wertung) {
		logger.warn("not implemented");
	}
}
