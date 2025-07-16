package de.sinnix.judoturnier.application.algorithm;

import de.sinnix.judoturnier.model.GewichtsklassenGruppe;
import de.sinnix.judoturnier.model.WettkampfGruppeMitBegegnungen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 Bei diesem System gibt es 2 Poole, in denen "Jeder gegen Jeden" kämpft. Pro Pool werden die Punkte der einzelnen Kämpfe zusammengezählt, woraus sich dann die jeweiligen Platzierungen ergeben.

 Sind die Kämpfe in den Pools beendet, werden als nächstes die Halbfinals ausgetragen. Der Sieger des Pools "A" kämpft gegen den Zweiten des Pools "B" und der Zweite des Pools "A" kämpft gegen den Sieger des Pools "B" Die Verlierer der Halbfinals haben damit die 3. Plätze erreicht.

 Die Gewinner der Halbfinals stehen sich nun im Finale gegenüber und kämpfen um den 1. Platz.
 */

public class DoppelKOSystemVorgepoolt implements Algorithmus {

	private static final Logger logger          = LogManager.getLogger(DoppelKOSystemVorgepoolt.class);

	@Override
	public WettkampfGruppeMitBegegnungen erstelleWettkampfGruppe(GewichtsklassenGruppe gewichtsklassenGruppe) {
		throw new RuntimeException("DoppelKOSystemVorgepoolt ist nicht implementiert. Bitte wähle einen anderen Algorithmus.");
	}

}
