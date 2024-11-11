package de.sinnix.judoturnier.adapter.secondary;

import de.sinnix.judoturnier.model.Benutzer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class BenutzerConverter {
	private static final Logger logger = LogManager.getLogger(BenutzerConverter.class);


	public Benutzer convertToBenutzer(String uuid) {
		logger.debug("convertToBenutzer: No real entity for bewerter, therefor returning just an object with the uuid {}", uuid);
		return new Benutzer(uuid, null, null, List.of());
	}

	public String convertFromBenutzer(Benutzer benutzer) {
		logger.debug("convertFromBenutzer: No real entity for bewerter, therefor returning just the id from bewerter {}", benutzer);
		return benutzer.id();
	}
}
