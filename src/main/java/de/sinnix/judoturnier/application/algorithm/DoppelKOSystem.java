package de.sinnix.judoturnier.application.algorithm;

import de.sinnix.judoturnier.model.Begegnung;
import de.sinnix.judoturnier.model.BegegnungenJeRunde;
import de.sinnix.judoturnier.model.GewichtsklassenGruppe;
import de.sinnix.judoturnier.model.RandoriGruppenName;
import de.sinnix.judoturnier.model.Wettkaempfer;
import de.sinnix.judoturnier.model.WettkampfGruppe;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Anzahl der Begegnungen bei 8 Teilnehmern:
 *
 *
 * Gewinnerrunde:
 * 1. Runde: 4 Begegnungen (8 Teilnehmer)
 * 2. Runde: 2 Begegnungen (4 verbleibende Teilnehmer)
 * 3. Runde: 1 Begegnung (2 verbleibende Teilnehmer)
 * Das ergibt 7 Begegnungen in der Gewinnerrunde.
 *
 * Trostrunde:
 * 1. Runde: 2 Begegnungen (4 Verlierer aus der 1. Runde der Gewinnerrunde)
 * 2. Runde: 1 Begegnung (Gewinner aus der 1. Runde der Trostrunde gegen die Verlierer aus der 2. Runde der Gewinnerrunde)
 * 3. Runde: 1 Begegnung (Gewinner aus der 2. Runde der Trostrunde gegen den Verlierer aus der 3. Runde der Gewinnerrunde um den 3. Platz)
 * Das ergibt 4 Begegnungen in der Trostrunde.
 *
 *
 */

public class DoppelKOSystem implements Algorithmus {

	private static final Logger logger          = LogManager.getLogger(DoppelKOSystem.class);

	@Override
	public List<WettkampfGruppe> erstelleWettkampfGruppen(Integer gruppenid, GewichtsklassenGruppe gewichtsklassenGruppe, Integer maxGruppenGroesse) {
		List<WettkampfGruppe> result = new ArrayList<>();

		// Teilnehmer der Gruppe
		List<Wettkaempfer> teilnehmer = gewichtsklassenGruppe.teilnehmer();

		List<Begegnung> begegnungen = erstelleBegegnungen(teilnehmer, gewichtsklassenGruppe.turnierUUID());
		// Ausgabe des Turnierbaums
		for (Begegnung begegnung : begegnungen) {
			logger.debug("{} - Wettkaempfer1: {}, Wettkaempfer2: {}, Ergebnis: {}", begegnung.getBegegnungId(), begegnung.getWettkaempfer1().map(Wettkaempfer::name), begegnung.getWettkaempfer2().map(Wettkaempfer::name), begegnung.getWertungen());
		}
		// Gruppiere nach Runde
		List<BegegnungenJeRunde> begegnungenJeRunde = begegnungen.stream()
			.collect(Collectors.groupingBy(begegnung -> begegnung.getBegegnungId().getRunde()))
			.values().stream()
			.map(BegegnungenJeRunde::new)
			.collect(Collectors.toList());

		String id = ((gruppenid + 1) * 10) + Integer.toString(1); // ids erstellen und konkatenieren
		WettkampfGruppe wettkampfGruppe = new WettkampfGruppe(
			Integer.parseInt(id),
			gewichtsklassenGruppe.name().orElseGet(() -> RandoriGruppenName.Ameise).name(),
			"(" + gewichtsklassenGruppe.minGewicht() + "-" + gewichtsklassenGruppe.maxGewicht() + " " + gewichtsklassenGruppe.altersKlasse() + ")",
			gewichtsklassenGruppe.altersKlasse(),
			begegnungenJeRunde,
			gewichtsklassenGruppe.turnierUUID()
		);
		result.add(wettkampfGruppe);
		return result;

	}

	// Erstelle Begegnungen dieser Gruppe (Gewinner- und Trostrunden)
	private List<Begegnung> erstelleBegegnungen(List<Wettkaempfer> teilnehmer, UUID turnierUUID) {
		List<Begegnung> result = new ArrayList<>();

		// Alle Begegnungen in der Gruppe generieren
		var teilnehmerAnzahl = teilnehmer.size();
		var gesamtRunden = (int) Math.ceil(Math.log(teilnehmerAnzahl) / Math.log(2));

		logger.debug("Teilnehmer dieser Gruppe: {}", teilnehmerAnzahl);
		logger.debug("Gesamtrunden dieser Gruppe: {}", gesamtRunden);

		List<Wettkaempfer> gemischteTeilnehmer = mischeTeilnehmer(teilnehmer);

		// in der ersten Runde müssen eventuell Freilose erstellt werden, sodass im weiteren Turnierverlauf ein ausgeglichener Baum entsteht
		List<Pair<Optional<Wettkaempfer>, Optional<Wettkaempfer>>> paarungenRunde1 = freilosMarkierung(gemischteTeilnehmer, gesamtRunden);
		logger.debug("Paarungen: {}", paarungenRunde1.size());

		// erste Gewinner-Runde zur Initialisierung der Spieler
		for (int akuellePaarungsId = 1; akuellePaarungsId <= paarungenRunde1.size(); akuellePaarungsId++) {
			var begegnung = new Begegnung();
			begegnung.setTurnierUUID(turnierUUID);
			begegnung.setBegegnungId(erstelleID(Begegnung.RundenTyp.GEWINNERRUNDE, 1, akuellePaarungsId));
			begegnung.setWettkaempfer1(paarungenRunde1.get(akuellePaarungsId - 1).getLeft());
			begegnung.setWettkaempfer2(paarungenRunde1.get(akuellePaarungsId - 1).getRight());
			result.add(begegnung);
		}
		logger.trace("Jetzt haben wir {} Begegnungen", result.size());

		// leere Gewinner-Runden
		for (int aktuelleRunde = 2; aktuelleRunde <= gesamtRunden; aktuelleRunde++) {
			for (int akuellePaarungsId = 1; akuellePaarungsId <= Math.pow(2, gesamtRunden - aktuelleRunde); akuellePaarungsId++) {
				result.add(leereBegegnung(erstelleID(Begegnung.RundenTyp.GEWINNERRUNDE, aktuelleRunde, akuellePaarungsId), turnierUUID));
			}
		}
		logger.trace("Jetzt haben wir {} Begegnungen", result.size());

		// leere Trost-Runden
		// TODO: die Trostrunde darf erst später beginnen, damit nicht die ausgeschiedenen Kämpfer sofort wieder dran sind...
		var trostrunden = (2* (gesamtRunden-1) ) -2;
		logger.trace("Anzahl Trostrunden: {}", trostrunden);
		for (int aktuelleRunde = 1; aktuelleRunde <= trostrunden; aktuelleRunde++) {
			logger.trace("Trostrunde: {} von {}", aktuelleRunde, gesamtRunden);
			// Anzahl der Begegnungen halbiert sich in jeder ungeraden Runde im Trostrunden-Turnierbaum
			for (int akuellePaarungsId = 1; akuellePaarungsId <= Math.pow(2, gesamtRunden - 1 - Math.floorDiv(aktuelleRunde + 1, 2)); akuellePaarungsId++) {
				logger.trace("akuellePaarungsId {}", akuellePaarungsId);
				result.add(leereBegegnung(erstelleID(Begegnung.RundenTyp.TROSTRUNDE, aktuelleRunde, akuellePaarungsId), turnierUUID));
			}
		}
		logger.trace("Jetzt haben wir {} Begegnungen", result.size());
		return result;
	}

	private static List<Wettkaempfer> mischeTeilnehmer(List<Wettkaempfer> teilnehmer) {
		// TODO
		return teilnehmer;
	}

	/**
	 * Erstellt Begegnungen mit Freiloses, d.h. einzelne Kämpfer haben keinen Gegner. Die ist erforderlich um einen ausgeglichenen Baum zu erhalten:
	 * Das Finale ist eine Begegnung, das Halbfinale zwei Begegnungen, Vierltelfinale vier Begegnungen usw.
	 */
	private static List<Pair<Optional<Wettkaempfer>, Optional<Wettkaempfer>>> freilosMarkierung(List<Wettkaempfer> wettkaempfer, int gesamtRunden) {
		List<Pair<Optional<Wettkaempfer>, Optional<Wettkaempfer>>> paarungen = new ArrayList<>();

		// Berechne die Anzahl der Paarungen in der ersten Runde
		// für einen ausgeglichenen Baum benötigen wir immer ein Vielfaches von 2^x
		int anzahlPaarungenRunde1 = (int) Math.pow(2, gesamtRunden - 1);
		logger.trace("Paarungen Runde 1: {}", anzahlPaarungenRunde1);

		List<Wettkaempfer> erweiterteListe = new ArrayList<>(wettkaempfer);

		// wir starten erst mit dem zweiten Eintrag, der erste Kämpfer ist nie ein Freilos
		int insertPosition = 1;
		// Füge null an jeder zweiten Stelle ein, bis die Ziel-Länge erreicht ist
		while (erweiterteListe.size() < anzahlPaarungenRunde1 * 2) {
			erweiterteListe.add(insertPosition, null);
			insertPosition += 2;
		}
		logger.trace("Erweitert auf {} Teilnehmer - erweiterteListe: {}", erweiterteListe.size(), erweiterteListe);

		// Erstelle die Paarungen
		for (int i = 0; i < anzahlPaarungenRunde1 * 2; i += 2) {
			Optional<Wettkaempfer> wettkaempfer1 = Optional.ofNullable(erweiterteListe.get(i));
			Optional<Wettkaempfer> wettkaempfer2 = Optional.ofNullable(erweiterteListe.get(i + 1));
			paarungen.add(new ImmutablePair<>(wettkaempfer1, wettkaempfer2));
		}

		return paarungen;
	}

	// Abkürzung, um eine Spiel-ID zu erstellen, da KO-Turniere sehr spezifisch bezüglich der Positionen sind
	private Begegnung.BegegnungId erstelleID(Begegnung.RundenTyp rundenTyp, int runde, int akuellePaarungsId) {
		return new Begegnung.BegegnungId(rundenTyp, runde, akuellePaarungsId);
	}

	private Begegnung leereBegegnung(Begegnung.BegegnungId begegnungId, UUID turnierUUID) {
		var begegnung = new Begegnung();
		begegnung.setTurnierUUID(turnierUUID);
		begegnung.setBegegnungId(begegnungId);
		// leer
		begegnung.setWettkaempfer1(Optional.empty());
		begegnung.setWettkaempfer2(Optional.empty());
		return begegnung;
	}

}
