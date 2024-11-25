package de.sinnix.judoturnier.adapter.primary;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class MattenAnzeigeController {
	private static final Logger logger = LogManager.getLogger(MattenAnzeigeController.class);

	@GetMapping("/turnier/{turnierid}/mattenanzeige/{matte}/{wettkampfnummer}")
	public ModelAndView anzeigeMattenWertung(@PathVariable String turnierid, @PathVariable String matte, @PathVariable String wettkampfnummer) {
		logger.info("Öffne Anzeige des Wettkampfs {} für Matte {}", wettkampfnummer, matte);

		ModelAndView mav = new ModelAndView("mattenanzeige");
		mav.addObject("turnierid", turnierid);
		mav.addObject("matte", matte);
		mav.addObject("wettkampfnummer", wettkampfnummer);
		return mav;
	}
}
