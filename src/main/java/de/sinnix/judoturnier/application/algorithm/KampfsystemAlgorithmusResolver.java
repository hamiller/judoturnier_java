package de.sinnix.judoturnier.application.algorithm;

import static de.sinnix.judoturnier.model.Altersklasse.FRAUEN;
import static de.sinnix.judoturnier.model.Altersklasse.MAENNER;
import static de.sinnix.judoturnier.model.Altersklasse.U9;
import static de.sinnix.judoturnier.model.Altersklasse.U11;
import static de.sinnix.judoturnier.model.Altersklasse.U12;
import static de.sinnix.judoturnier.model.Altersklasse.U13;
import static de.sinnix.judoturnier.model.Altersklasse.U15;
import static de.sinnix.judoturnier.model.Altersklasse.U18;
import static de.sinnix.judoturnier.model.Altersklasse.U21;
import static de.sinnix.judoturnier.model.TurnierTyp.RANDORI;
import static de.sinnix.judoturnier.model.TurnierTyp.STANDARD;
import java.util.List;
import java.util.function.Supplier;
import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Einstellungen;
import de.sinnix.judoturnier.model.GewichtsklassenGruppe;
import de.sinnix.judoturnier.model.TurnierTyp;

public class KampfsystemAlgorithmusResolver {

	record KampfsystemAlgorithmusRegel(TurnierTyp turnierTyp, List<Altersklasse> altersklassen, int minTeilnehmer, int maxTeilnehmer, Supplier<Algorithmus> algorithmusSupplier) {

		boolean match(TurnierTyp turnierTyp, Altersklasse altersklasse, int teilnehmerAnzahl) {
			return this.turnierTyp == turnierTyp
				&& (altersklassen.isEmpty() || altersklassen.contains(altersklasse))
				&& teilnehmerAnzahl >= minTeilnehmer
				&& teilnehmerAnzahl <= maxTeilnehmer;
		}
	}

	private static final List<KampfsystemAlgorithmusRegel> REGELN = List.of(
		new KampfsystemAlgorithmusRegel(RANDORI, List.of(), 2, 2, BesterAusDrei::new),
		new KampfsystemAlgorithmusRegel(RANDORI, List.of(), 3, Integer.MAX_VALUE, JederGegenJeden::new),

		new KampfsystemAlgorithmusRegel(STANDARD, List.of(U9, U11, U12, U13, U15, U18, U21, FRAUEN, MAENNER), 2, 2, BesterAusDrei::new),
		new KampfsystemAlgorithmusRegel(STANDARD, List.of(U9, U11, U12, U13, U15, U18, U21, FRAUEN, MAENNER), 3, 5, JederGegenJeden::new),
		
		new KampfsystemAlgorithmusRegel(STANDARD, List.of(U9, U12, U13, U15, U18), 6, 8, ZweiPoolsJederGegenJeden::new),
		new KampfsystemAlgorithmusRegel(STANDARD, List.of(U9, U12, U13, U15, U18), 9, Integer.MAX_VALUE, DoppelKOSystem::new),

		new KampfsystemAlgorithmusRegel(STANDARD, List.of(U21, FRAUEN, MAENNER), 6, 32, DoppelKOSystem::new),
		new KampfsystemAlgorithmusRegel(STANDARD, List.of(U21, FRAUEN, MAENNER), 33, Integer.MAX_VALUE, KOSystemMitDoppelterTrostrunde::new)
	);

	public Algorithmus resolve(Einstellungen einstellungen, GewichtsklassenGruppe gewichtsklassenGruppe) {
		return resolve(
			einstellungen.turnierTyp(),
			gewichtsklassenGruppe.altersKlasse(),
			gewichtsklassenGruppe.teilnehmer().size()
		);
	}

	public Algorithmus resolve(TurnierTyp turnierTyp, Altersklasse altersklasse, int teilnehmerAnzahl) {
		return REGELN.stream()
			.filter(kandidat -> kandidat.match(turnierTyp, altersklasse, teilnehmerAnzahl))
			.findFirst()
			.map(KampfsystemAlgorithmusRegel::algorithmusSupplier)
			.map(Supplier::get)
			.orElseThrow(() -> new IllegalArgumentException(
				"Keine Kampfsystem-Regel für TurnierTyp " + turnierTyp
					+ ", Altersklasse " + altersklasse
					+ " und Teilnehmerzahl " + teilnehmerAnzahl
			));
	}
}
