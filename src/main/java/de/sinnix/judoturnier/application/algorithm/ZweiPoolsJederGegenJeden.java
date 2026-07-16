package de.sinnix.judoturnier.application.algorithm;

import de.sinnix.judoturnier.model.GewichtsklassenGruppe;
import de.sinnix.judoturnier.model.WettkampfGruppeMitBegegnungen;

public class ZweiPoolsJederGegenJeden implements Algorithmus {

	@Override
	public WettkampfGruppeMitBegegnungen erstelleWettkampfGruppe(GewichtsklassenGruppe gewichtsklassenGruppe) {
		throw new RuntimeException("ZweiPoolsJederGegenJeden ist nicht implementiert. Bitte wähle einen anderen Algorithmus.");
	}
}
