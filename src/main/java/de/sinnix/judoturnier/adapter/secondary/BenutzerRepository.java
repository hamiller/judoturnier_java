package de.sinnix.judoturnier.adapter.secondary;

import de.sinnix.judoturnier.model.Benutzer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BenutzerRepository {
	private static final Logger logger = LogManager.getLogger(BenutzerRepository.class);

	@Autowired
	private BenutzerConverter benutzerConverter;

	public Benutzer findById(String bewerterUUID) {
		return benutzerConverter.convertToBenutzer(bewerterUUID);
	}
}
