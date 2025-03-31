package de.sinnix.judoturnier.application.algorithm;

import de.sinnix.judoturnier.model.Begegnung;
import de.sinnix.judoturnier.model.BegegnungenJeRunde;
import de.sinnix.judoturnier.model.GewichtsklassenGruppe;
import de.sinnix.judoturnier.model.RandoriGruppenName;
import de.sinnix.judoturnier.model.Wettkaempfer;
import de.sinnix.judoturnier.model.WettkampfGruppe;
import de.sinnix.judoturnier.model.WettkampfGruppeMitBegegnungen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementiert den Algorithmus für Gewichtsklassengruppen mit 2 oder weniger Kämpfern bei normalen Turnieren.
 *
 * Bei zwei Kämpfern gibt es vorerst zwei Begegnungen - sollte dabei jeder Kämpfer jeweils einmal gewinnen, gibt
 * es eine dritte, entscheidende Begegnung. (Daher der Name: "Bester aus 3 Begegnungen")
 */
public class BesterAusDrei implements Algorithmus {

	private static final Logger logger = LogManager.getLogger(BesterAusDrei.class);

	@Override
	public WettkampfGruppeMitBegegnungen erstelleWettkampfGruppe(GewichtsklassenGruppe gewichtsklassenGruppe) {
		// Teilnehmer der Gruppe
		List<Wettkaempfer> teilnehmer = gewichtsklassenGruppe.teilnehmer();

		// erstelle die Begegnungen
		List<Begegnung> begegnungen = erstelleBegegnungen(teilnehmer, gewichtsklassenGruppe.turnierUUID());

		// Gruppiere nach Runde
		List<BegegnungenJeRunde> begegnungenJeRunde = begegnungen.stream()
			.collect(Collectors.groupingBy(begegnung -> begegnung.getBegegnungId().getRunde()))
			.values().stream()
			.map(BegegnungenJeRunde::new)
			.collect(Collectors.toList());

		WettkampfGruppe wettkampfGruppe = new WettkampfGruppe(
			UUID.randomUUID(),
			gewichtsklassenGruppe.name().orElseGet(() -> RandoriGruppenName.Ameise).name(),
			"(" + gewichtsklassenGruppe.minGewicht() + "-" + gewichtsklassenGruppe.maxGewicht() + " " + gewichtsklassenGruppe.altersKlasse() + ")",
			gewichtsklassenGruppe.altersKlasse(),
			gewichtsklassenGruppe.turnierUUID()
		);
		return new WettkampfGruppeMitBegegnungen(wettkampfGruppe, begegnungenJeRunde);
	}

	private List<Begegnung> erstelleBegegnungen(List<Wettkaempfer> teilnehmer, UUID turnierUUID) {
		List<Begegnung> result = new ArrayList<>();

		if (teilnehmer.size() > 2) {
			logger.error("Best-of-3 kann nur bei zwei Teilnehmern verwendet werden!");
			throw new RuntimeException("Turniermodus BesterAusDrei ungültig bei mehr als 2 Wettkämpfern");
		}


		if (teilnehmer.size() < 2) {
			var wk = teilnehmer.getFirst();
			logger.info("Wettkämpfer {} ist allein in seiner Gewichtsklasse. Es werden keine Begegnungen erstellt!", wk.name());
			var begegnung = new Begegnung();
			begegnung.setTurnierUUID(turnierUUID);
			begegnung.setBegegnungId(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1));
			begegnung.setWettkaempfer1(Optional.of(wk));
			begegnung.setWettkaempfer2(Optional.empty());

			result.add(begegnung);
		}

		Wettkaempfer wettkaempfer1 = teilnehmer.get(0);
		Wettkaempfer wettkaempfer2 = teilnehmer.get(1);

		logger.info("wk1 {}, wk2 {}", wettkaempfer1.name(), wettkaempfer2.name());

		// Drei Begegnungen für das Best-of-3
		for (int k = 1; k <= 3; k++) {
			logger.info("Teilnehmer Runde k {}", k);
			var begegnung = new Begegnung();
			begegnung.setTurnierUUID(turnierUUID);
			begegnung.setBegegnungId(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, k, 1));
			begegnung.setWettkaempfer1(Optional.of(wettkaempfer1));
			begegnung.setWettkaempfer2(Optional.of(wettkaempfer2));

			result.add(begegnung);
		}

		return result;
	}
}
