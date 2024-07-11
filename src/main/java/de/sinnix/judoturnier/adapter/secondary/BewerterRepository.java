package de.sinnix.judoturnier.adapter.secondary;

import de.sinnix.judoturnier.model.Bewerter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BewerterRepository {
	private static final Logger logger = LogManager.getLogger(BewerterRepository.class);

	@Autowired
	private BewerterConverter bewerterConverter;

	public Bewerter findById(String bewerterUUID) {
		return bewerterConverter.convertToBewerter(bewerterUUID);
	}
}
