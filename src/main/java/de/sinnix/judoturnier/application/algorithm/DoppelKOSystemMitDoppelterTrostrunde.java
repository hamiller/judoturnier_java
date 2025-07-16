package de.sinnix.judoturnier.application.algorithm;

import de.sinnix.judoturnier.model.GewichtsklassenGruppe;
import de.sinnix.judoturnier.model.WettkampfGruppeMitBegegnungen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DoppelKOSystemMitDoppelterTrostrunde implements Algorithmus {

	private static final Logger logger = LogManager.getLogger(DoppelKOSystemVorgepoolt.class);

	@Override
	public WettkampfGruppeMitBegegnungen erstelleWettkampfGruppe(GewichtsklassenGruppe gewichtsklassenGruppe) {
		throw new RuntimeException("DoppelKOSystemMitDoppelterTrostrunde ist nicht implementiert. Bitte wähle einen anderen Algorithmus.");
	}
}
