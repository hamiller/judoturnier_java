package de.sinnix.judoturnier.application.algorithm;

import de.sinnix.judoturnier.model.Wettkampfsystem;

public final class AlgorithmusFactory {

	private AlgorithmusFactory() {
	}

	public static Algorithmus from(Wettkampfsystem wettkampfsystem) {
		return switch (wettkampfsystem) {
			case KEIN_WETTKAMPF -> throw new IllegalArgumentException("Für KEIN_WETTKAMPF wird kein Algorithmus erzeugt.");
			case BESTER_AUS_DREI -> new BesterAusDrei();
			case JEDER_GEGEN_JEDEN, POOLSYSTEM_JEDER_GEGEN_JEDEN -> new JederGegenJeden();
			case ZWEI_POOLS_JEDER_GEGEN_JEDEN -> new ZweiPoolsJederGegenJeden();
			case DOPPELTES_KO_SYSTEM_MIT_TROSTRUNDE -> new DoppelKOSystem();
			case BRASILIANISCHES_KO_SYSTEM_MIT_DOPPELTER_TROSTRUNDE -> new BrasilianischesKOSystemMitDoppelterTrostrunde();
			case VORGEPOOLTES_KO_SYSTEM -> new VorgepooltesKOSystem();
			case KO_SYSTEM_MIT_DOPPELTER_TROSTRUNDE -> new KOSystemMitDoppelterTrostrunde();
		};
	}
}
