package de.sinnix.judoturnier.application.algorithm;

import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.TurnierTyp;
import de.sinnix.judoturnier.model.Wettkampfsystem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WettkampfsystemResolverTest {

	private final WettkampfsystemResolver resolver = new WettkampfsystemResolver();

	@Test
	void jugendbereichBisU18() {
		assertEquals(Wettkampfsystem.KEIN_WETTKAMPF, resolver.resolve(TurnierTyp.STANDARD, Altersklasse.U18, 1));
		assertEquals(Wettkampfsystem.BESTER_AUS_DREI, resolver.resolve(TurnierTyp.STANDARD, Altersklasse.U18, 2));
		assertEquals(Wettkampfsystem.JEDER_GEGEN_JEDEN, resolver.resolve(TurnierTyp.STANDARD, Altersklasse.U18, 5));
		assertEquals(Wettkampfsystem.ZWEI_POOLS_JEDER_GEGEN_JEDEN, resolver.resolve(TurnierTyp.STANDARD, Altersklasse.U18, 6));
		assertEquals(Wettkampfsystem.ZWEI_POOLS_JEDER_GEGEN_JEDEN, resolver.resolve(TurnierTyp.STANDARD, Altersklasse.U18, 8));
		assertEquals(Wettkampfsystem.DOPPELTES_KO_SYSTEM_MIT_TROSTRUNDE, resolver.resolve(TurnierTyp.STANDARD, Altersklasse.U18, 9));
	}

	@Test
	void u11GewichtsnahePoolsMitMaximalFuenfTeilnehmern() {
		assertEquals(Wettkampfsystem.KEIN_WETTKAMPF, resolver.resolve(TurnierTyp.STANDARD, Altersklasse.U11, 1));
		assertEquals(Wettkampfsystem.BESTER_AUS_DREI, resolver.resolve(TurnierTyp.STANDARD, Altersklasse.U11, 2));
		assertEquals(Wettkampfsystem.JEDER_GEGEN_JEDEN, resolver.resolve(TurnierTyp.STANDARD, Altersklasse.U11, 4));
		assertEquals(Wettkampfsystem.JEDER_GEGEN_JEDEN, resolver.resolve(TurnierTyp.STANDARD, Altersklasse.U11, 5));
		assertThrows(IllegalArgumentException.class, () -> resolver.resolve(TurnierTyp.STANDARD, Altersklasse.U11, 6));
	}

	@Test
	void erwachsenenbereichAbU21VerwendetAktuellImplementierteDefaults() {
		assertEquals(Wettkampfsystem.KEIN_WETTKAMPF, resolver.resolve(TurnierTyp.STANDARD, Altersklasse.U21, 1));
		assertEquals(Wettkampfsystem.BESTER_AUS_DREI, resolver.resolve(TurnierTyp.STANDARD, Altersklasse.U21, 2));
		assertEquals(Wettkampfsystem.POOLSYSTEM_JEDER_GEGEN_JEDEN, resolver.resolve(TurnierTyp.STANDARD, Altersklasse.U21, 5));
		assertEquals(Wettkampfsystem.DOPPELTES_KO_SYSTEM_MIT_TROSTRUNDE, resolver.resolve(TurnierTyp.STANDARD, Altersklasse.U21, 6));
		assertEquals(Wettkampfsystem.DOPPELTES_KO_SYSTEM_MIT_TROSTRUNDE, resolver.resolve(TurnierTyp.STANDARD, Altersklasse.FRAUEN, 9));
		assertEquals(Wettkampfsystem.DOPPELTES_KO_SYSTEM_MIT_TROSTRUNDE, resolver.resolve(TurnierTyp.STANDARD, Altersklasse.MAENNER, 9));
	}

	@Test
	void randoriVerwendetBeiZweiTeilnehmernBestAusDreiUndDanachJederGegenJeden() {
		assertEquals(Wettkampfsystem.KEIN_WETTKAMPF, resolver.resolve(TurnierTyp.RANDORI, Altersklasse.U11, 1));
		assertEquals(Wettkampfsystem.BESTER_AUS_DREI, resolver.resolve(TurnierTyp.RANDORI, Altersklasse.U11, 2));
		assertEquals(Wettkampfsystem.JEDER_GEGEN_JEDEN, resolver.resolve(TurnierTyp.RANDORI, Altersklasse.U11, 6));
	}
}
