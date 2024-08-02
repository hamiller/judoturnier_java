package de.sinnix.judoturnier.adapter.primary;

import de.sinnix.judoturnier.application.VereinService;
import de.sinnix.judoturnier.model.Verein;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class VereinController {

	private static final Logger logger = LogManager.getLogger(VereinController.class);

	@Autowired
	private VereinService vereinService;

	@GetMapping("/turnier/{turnierid}/vereine")
	public Verein[] vereine(@PathVariable String turnierid) {
		logger.info("Lade Vereine...");
		UUID turnierUUID = UUID.fromString(turnierid);
		return vereinService.holeAlleVereine(turnierUUID).toArray(Verein[]::new);
	}
}
