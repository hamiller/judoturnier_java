package de.sinnix.judoturnier.application.algorithm;

import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.TurnierTyp;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

class KampfsystemAlgorithmusResolverTest {

	private final KampfsystemAlgorithmusResolver resolver = new KampfsystemAlgorithmusResolver();

	@Test
	void jugendbereichBisU18() {
		assertThrows(IllegalArgumentException.class, () -> resolver.resolve(TurnierTyp.STANDARD, Altersklasse.U18, 1));
		assertAlgorithmus(BesterAusDrei.class, resolver.resolve(TurnierTyp.STANDARD, Altersklasse.U18, 2));
		assertAlgorithmus(JederGegenJeden.class, resolver.resolve(TurnierTyp.STANDARD, Altersklasse.U18, 5));
		assertAlgorithmus(ZweiPoolsJederGegenJeden.class, resolver.resolve(TurnierTyp.STANDARD, Altersklasse.U18, 6));
		assertAlgorithmus(ZweiPoolsJederGegenJeden.class, resolver.resolve(TurnierTyp.STANDARD, Altersklasse.U18, 8));
		assertAlgorithmus(DoppelKOSystem.class, resolver.resolve(TurnierTyp.STANDARD, Altersklasse.U18, 9));
		assertAlgorithmus(DoppelKOSystem.class, resolver.resolve(TurnierTyp.STANDARD, Altersklasse.U18, 33));
	}

	@Test
	void u11GewichtsnahePoolsMitMaximalFuenfTeilnehmern() {
		assertThrows(IllegalArgumentException.class, () -> resolver.resolve(TurnierTyp.STANDARD, Altersklasse.U11, 1));
		assertAlgorithmus(BesterAusDrei.class, resolver.resolve(TurnierTyp.STANDARD, Altersklasse.U11, 2));
		assertAlgorithmus(JederGegenJeden.class, resolver.resolve(TurnierTyp.STANDARD, Altersklasse.U11, 4));
		assertAlgorithmus(JederGegenJeden.class, resolver.resolve(TurnierTyp.STANDARD, Altersklasse.U11, 5));
		assertThrows(IllegalArgumentException.class, () -> resolver.resolve(TurnierTyp.STANDARD, Altersklasse.U11, 6));
	}

	@Test
	void erwachsenenbereichAbU21VerwendetAktuellImplementierteDefaults() {
		assertThrows(IllegalArgumentException.class, () -> resolver.resolve(TurnierTyp.STANDARD, Altersklasse.U21, 1));
		assertAlgorithmus(BesterAusDrei.class, resolver.resolve(TurnierTyp.STANDARD, Altersklasse.U21, 2));
		assertAlgorithmus(JederGegenJeden.class, resolver.resolve(TurnierTyp.STANDARD, Altersklasse.U21, 5));
		assertAlgorithmus(DoppelKOSystem.class, resolver.resolve(TurnierTyp.STANDARD, Altersklasse.U21, 6));
		assertAlgorithmus(DoppelKOSystem.class, resolver.resolve(TurnierTyp.STANDARD, Altersklasse.FRAUEN, 9));
		assertAlgorithmus(DoppelKOSystem.class, resolver.resolve(TurnierTyp.STANDARD, Altersklasse.MAENNER, 9));
		assertAlgorithmus(DoppelKOSystem.class, resolver.resolve(TurnierTyp.STANDARD, Altersklasse.MAENNER, 32));
		assertAlgorithmus(KOSystemMitDoppelterTrostrunde.class, resolver.resolve(TurnierTyp.STANDARD, Altersklasse.MAENNER, 33));
		assertAlgorithmus(KOSystemMitDoppelterTrostrunde.class, resolver.resolve(TurnierTyp.STANDARD, Altersklasse.U21, 33));
	}

	@Test
	void randoriVerwendetBeiZweiTeilnehmernBestAusDreiUndDanachJederGegenJeden() {
		assertThrows(IllegalArgumentException.class, () -> resolver.resolve(TurnierTyp.RANDORI, Altersklasse.U11, 1));
		assertAlgorithmus(BesterAusDrei.class, resolver.resolve(TurnierTyp.RANDORI, Altersklasse.U11, 2));
		assertAlgorithmus(JederGegenJeden.class, resolver.resolve(TurnierTyp.RANDORI, Altersklasse.U11, 6));
	}

	private static void assertAlgorithmus(Class<? extends Algorithmus> erwarteterTyp, Algorithmus algorithmus) {
		assertInstanceOf(erwarteterTyp, algorithmus);
	}
}
