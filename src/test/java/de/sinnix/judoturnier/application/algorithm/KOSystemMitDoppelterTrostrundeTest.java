package de.sinnix.judoturnier.application.algorithm;

import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Begegnung;
import de.sinnix.judoturnier.model.Farbe;
import de.sinnix.judoturnier.model.Geschlecht;
import de.sinnix.judoturnier.model.GewichtsklassenGruppe;
import de.sinnix.judoturnier.model.Verein;
import de.sinnix.judoturnier.model.Wettkaempfer;
import de.sinnix.judoturnier.model.WettkampfGruppeMitBegegnungen;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class KOSystemMitDoppelterTrostrundeTest {

	private final KOSystemMitDoppelterTrostrunde algorithmus = new KOSystemMitDoppelterTrostrunde();

	private UUID turnierUUID;

	@BeforeEach
	void setUp() {
		turnierUUID = UUID.randomUUID();
	}

	@Test
	void erstelle32erListeMitDoppelterTrostrunde() {
		WettkampfGruppeMitBegegnungen wettkampfGruppe = algorithmus.erstelleWettkampfGruppe(gewichtsklassenGruppe(32));

		assertEquals(7, wettkampfGruppe.alleRundenBegegnungen().size());
		assertEquals(43, wettkampfGruppe.alleRundenBegegnungen().stream().mapToInt(runde -> runde.begegnungenJeRunde().size()).sum());

		assertRunde(wettkampfGruppe, 0, 16, 0);
		assertRunde(wettkampfGruppe, 1, 8, 0);
		assertRunde(wettkampfGruppe, 2, 4, 4);
		assertRunde(wettkampfGruppe, 3, 2, 4);
		assertRunde(wettkampfGruppe, 4, 0, 2);
		assertRunde(wettkampfGruppe, 5, 0, 2);
		assertRunde(wettkampfGruppe, 6, 1, 0);

		assertPaarung(wettkampfGruppe, 0, 0, "Teilnehmer 01", "Teilnehmer 17");
		assertPaarung(wettkampfGruppe, 0, 1, "Teilnehmer 09", "Teilnehmer 25");
		assertPaarung(wettkampfGruppe, 0, 2, "Teilnehmer 05", "Teilnehmer 21");
		assertPaarung(wettkampfGruppe, 0, 3, "Teilnehmer 13", "Teilnehmer 29");

		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 3, 1), wettkampfGruppe.alleRundenBegegnungen().get(2).begegnungenJeRunde().get(4).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 6, 2), wettkampfGruppe.alleRundenBegegnungen().get(5).begegnungenJeRunde().get(1).getBegegnungId());
		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 5, 1), wettkampfGruppe.alleRundenBegegnungen().get(6).begegnungenJeRunde().getFirst().getBegegnungId());
	}

	@Test
	void erstelle64erListeFuerMehrAls32Teilnehmer() {
		WettkampfGruppeMitBegegnungen wettkampfGruppe = algorithmus.erstelleWettkampfGruppe(gewichtsklassenGruppe(33));

		assertEquals(9, wettkampfGruppe.alleRundenBegegnungen().size());
		assertEquals(79, wettkampfGruppe.alleRundenBegegnungen().stream().mapToInt(runde -> runde.begegnungenJeRunde().size()).sum());
		assertRunde(wettkampfGruppe, 0, 32, 0);
		assertRunde(wettkampfGruppe, 3, 4, 4);
		assertRunde(wettkampfGruppe, 5, 0, 4);
		assertRunde(wettkampfGruppe, 6, 0, 2);
		assertRunde(wettkampfGruppe, 7, 0, 2);
		assertRunde(wettkampfGruppe, 8, 1, 0);
	}

	@Test
	void lehntZuKleineGruppenAb() {
		assertThrows(IllegalArgumentException.class, () -> algorithmus.erstelleWettkampfGruppe(gewichtsklassenGruppe(8)));
	}

	private void assertRunde(WettkampfGruppeMitBegegnungen wettkampfGruppe, int rundenIndex, int gewinnerRunden, int trostRunden) {
		List<Begegnung> begegnungen = wettkampfGruppe.alleRundenBegegnungen().get(rundenIndex).begegnungenJeRunde();
		assertEquals(gewinnerRunden, begegnungen.stream().filter(begegnung -> begegnung.getBegegnungId().rundenTyp == Begegnung.RundenTyp.GEWINNERRUNDE).count());
		assertEquals(trostRunden, begegnungen.stream().filter(begegnung -> begegnung.getBegegnungId().rundenTyp == Begegnung.RundenTyp.TROSTRUNDE).count());
	}

	private void assertPaarung(WettkampfGruppeMitBegegnungen wettkampfGruppe, int rundenIndex, int begegnungIndex, String name1, String name2) {
		Begegnung begegnung = wettkampfGruppe.alleRundenBegegnungen().get(rundenIndex).begegnungenJeRunde().get(begegnungIndex);
		assertEquals(name1, begegnung.getWettkaempfer1().map(Wettkaempfer::name).orElse("-"));
		assertEquals(name2, begegnung.getWettkaempfer2().map(Wettkaempfer::name).orElse("-"));
	}

	private GewichtsklassenGruppe gewichtsklassenGruppe(int teilnehmerAnzahl) {
		List<Wettkaempfer> teilnehmer = IntStream.rangeClosed(1, teilnehmerAnzahl)
			.mapToObj(this::wettkaempfer)
			.toList();
		return new GewichtsklassenGruppe(UUID.randomUUID(), Altersklasse.MAENNER, Optional.of(Geschlecht.m), teilnehmer, Optional.empty(), 60.0, 66.0, turnierUUID);
	}

	private Wettkaempfer wettkaempfer(int nr) {
		return new Wettkaempfer(
			UUID.randomUUID(),
			"Teilnehmer %02d".formatted(nr),
			Geschlecht.m,
			Altersklasse.MAENNER,
			new Verein(UUID.randomUUID(), "Verein %02d".formatted(nr), turnierUUID),
			60.0 + nr / 100.0,
			Optional.of(Farbe.WEISS),
			true,
			false,
			turnierUUID
		);
	}
}
