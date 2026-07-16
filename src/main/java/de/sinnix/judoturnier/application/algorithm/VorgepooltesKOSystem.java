package de.sinnix.judoturnier.application.algorithm;

import de.sinnix.judoturnier.model.GewichtsklassenGruppe;
import de.sinnix.judoturnier.model.WettkampfGruppeMitBegegnungen;

public class VorgepooltesKOSystem implements Algorithmus {

	@Override
	public WettkampfGruppeMitBegegnungen erstelleWettkampfGruppe(GewichtsklassenGruppe gewichtsklassenGruppe) {
		throw new RuntimeException("VorgepooltesKOSystem ist nicht implementiert. Bitte wähle einen anderen Algorithmus.");
	}
}
