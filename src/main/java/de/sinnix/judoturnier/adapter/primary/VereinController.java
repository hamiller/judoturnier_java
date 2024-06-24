package de.sinnix.judoturnier.adapter.primary;

import de.sinnix.judoturnier.application.VereinService;
import de.sinnix.judoturnier.model.Verein;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VereinController {

	private static final Logger logger = LogManager.getLogger(VereinController.class);

	@Autowired
	private VereinService vereinService;

	@GetMapping("/vereine")
	public Verein[] vereine() {
		logger.info("Lade Vereine...");
		return vereinService.holeAlleVereine().toArray(Verein[]::new);
	}
}
