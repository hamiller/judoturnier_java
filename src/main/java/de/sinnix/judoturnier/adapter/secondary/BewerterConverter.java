package de.sinnix.judoturnier.adapter.secondary;

import de.sinnix.judoturnier.model.Bewerter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BewerterConverter {
	private static final Logger logger = LogManager.getLogger(BewerterConverter.class);


	public Bewerter convertToBewerter(String uuid) {
		logger.debug("convertToBewerter: No real entity for bewerter, therefor returning just an object with the uuid {}", uuid);
		return new Bewerter(uuid, null, null, List.of());
	}

	public String convertFromBewerter(Bewerter bewerter) {
		logger.debug("convertToBewerter: No real entity for bewerter, therefor returning just the id from bewerter {}", bewerter);
		return bewerter.id();
	}
}
