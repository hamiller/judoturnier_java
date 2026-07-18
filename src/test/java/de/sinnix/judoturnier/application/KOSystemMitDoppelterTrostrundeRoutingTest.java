package de.sinnix.judoturnier.application;

import de.sinnix.judoturnier.application.algorithm.KOSystemMitDoppelterTrostrunde;
import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Begegnung;
import de.sinnix.judoturnier.model.Farbe;
import de.sinnix.judoturnier.model.Geschlecht;
import de.sinnix.judoturnier.model.GewichtsklassenGruppe;
import de.sinnix.judoturnier.model.Runde;
import de.sinnix.judoturnier.model.Verein;
import de.sinnix.judoturnier.model.Wettkaempfer;
import de.sinnix.judoturnier.model.WettkampfGruppe;
import de.sinnix.judoturnier.model.WettkampfGruppeMitBegegnungen;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class KOSystemMitDoppelterTrostrundeRoutingTest {

	private final UUID turnierUUID = UUID.randomUUID();

	@Test
	void setztErstNachDemViertelfinaleDieVerliererGegenDenPoolsiegerInDieTrostrunde() {
		List<Wettkaempfer> teilnehmer = teilnehmer(32);
		WettkampfGruppeMitBegegnungen wettkampfGruppe = new KOSystemMitDoppelterTrostrunde()
			.erstelleWettkampfGruppe(gewichtsklassenGruppe(teilnehmer));
		List<Runde> runden = runden(wettkampfGruppe);

		Wettkaempfer poolsieger = teilnehmer.get(0);
		Wettkaempfer verliererRunde1 = teilnehmer.get(16);
		Wettkaempfer verliererRunde2 = teilnehmer.get(8);
		Wettkaempfer verliererViertelfinale = teilnehmer.get(4);

		Begegnung runde2 = begegnung(runden, Begegnung.RundenTyp.GEWINNERRUNDE, 2, 1);
		runde2.setWettkaempfer1(Optional.of(poolsieger));
		runde2.setWettkaempfer2(Optional.of(verliererRunde2));

		Begegnung viertelfinale = begegnung(runden, Begegnung.RundenTyp.GEWINNERRUNDE, 3, 1);
		viertelfinale.setWettkaempfer1(Optional.of(poolsieger));
		viertelfinale.setWettkaempfer2(Optional.of(verliererViertelfinale));

		Sortierer.NachfolgeBelegung nachfolger = Sortierer.nachfolgeBelegungen(viertelfinale, poolsieger, wettkampfGruppe.gruppe(), runden);

		assertEquals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 4, 1), nachfolger.gewinnerBegegnung().orElseThrow().getBegegnungId());
		assertEquals(3, nachfolger.trostBelegungen().size());
		assertTrostBelegung(nachfolger, Begegnung.RundenTyp.TROSTRUNDE, 3, 1, verliererRunde1);
		assertTrostBelegung(nachfolger, Begegnung.RundenTyp.TROSTRUNDE, 3, 1, verliererRunde2);
		assertTrostBelegung(nachfolger, Begegnung.RundenTyp.TROSTRUNDE, 4, 1, verliererViertelfinale);
	}

	@Test
	void trostrundensiegerWerdenBisZumUeberkreuztenBronzekampfWeitergesetzt() {
		List<Wettkaempfer> teilnehmer = teilnehmer(32);
		WettkampfGruppeMitBegegnungen wettkampfGruppe = new KOSystemMitDoppelterTrostrunde()
			.erstelleWettkampfGruppe(gewichtsklassenGruppe(teilnehmer));
		List<Runde> runden = runden(wettkampfGruppe);

		Wettkaempfer trostSiegerViertel = teilnehmer.get(16);
		Begegnung viertelTrostFinale = begegnung(runden, Begegnung.RundenTyp.TROSTRUNDE, 4, 1);
		viertelTrostFinale.setWettkaempfer1(Optional.of(trostSiegerViertel));
		viertelTrostFinale.setWettkaempfer2(Optional.of(teilnehmer.get(4)));

		Sortierer.NachfolgeBelegung nachViertelTrost = Sortierer.nachfolgeBelegungen(viertelTrostFinale, trostSiegerViertel, wettkampfGruppe.gruppe(), runden);
		assertEquals(1, nachViertelTrost.trostBelegungen().size());
		assertTrostBelegung(nachViertelTrost, Begegnung.RundenTyp.TROSTRUNDE, 5, 1, trostSiegerViertel);

		Begegnung haelftenTrostFinale = begegnung(runden, Begegnung.RundenTyp.TROSTRUNDE, 5, 1);
		haelftenTrostFinale.setWettkaempfer1(Optional.of(trostSiegerViertel));
		haelftenTrostFinale.setWettkaempfer2(Optional.of(teilnehmer.get(24)));

		Sortierer.NachfolgeBelegung nachHaelftenTrost = Sortierer.nachfolgeBelegungen(haelftenTrostFinale, trostSiegerViertel, wettkampfGruppe.gruppe(), runden);
		assertEquals(1, nachHaelftenTrost.trostBelegungen().size());
		assertTrostBelegung(nachHaelftenTrost, Begegnung.RundenTyp.TROSTRUNDE, 6, 2, trostSiegerViertel);
	}

	private void assertTrostBelegung(Sortierer.NachfolgeBelegung nachfolger, Begegnung.RundenTyp rundenTyp, int runde, int paarung, Wettkaempfer wettkaempfer) {
		assertTrue(nachfolger.trostBelegungen().stream()
			.anyMatch(belegung -> belegung.begegnung().getBegegnungId().equals(new Begegnung.BegegnungId(rundenTyp, runde, paarung))
				&& belegung.wettkaempfer().equals(wettkaempfer)));
	}

	private List<Runde> runden(WettkampfGruppeMitBegegnungen wettkampfGruppe) {
		WettkampfGruppe gruppe = wettkampfGruppe.gruppe();
		return IntStream.range(0, wettkampfGruppe.alleRundenBegegnungen().size())
			.mapToObj(index -> new Runde(
				UUID.randomUUID(),
				index + 1,
				index + 1,
				index + 1,
				1,
				gruppe.altersklasse(),
				gruppe,
				wettkampfGruppe.alleRundenBegegnungen().get(index).begegnungenJeRunde()
			))
			.toList();
	}

	private Begegnung begegnung(List<Runde> runden, Begegnung.RundenTyp rundenTyp, int runde, int paarung) {
		return runden.stream()
			.flatMap(r -> r.begegnungen().stream())
			.filter(begegnung -> begegnung.getBegegnungId().equals(new Begegnung.BegegnungId(rundenTyp, runde, paarung)))
			.findFirst()
			.orElseThrow();
	}

	private GewichtsklassenGruppe gewichtsklassenGruppe(List<Wettkaempfer> teilnehmer) {
		return new GewichtsklassenGruppe(UUID.randomUUID(), Altersklasse.MAENNER, Optional.of(Geschlecht.m), teilnehmer, Optional.empty(), 60.0, 66.0, turnierUUID);
	}

	private List<Wettkaempfer> teilnehmer(int anzahl) {
		return IntStream.rangeClosed(1, anzahl)
			.mapToObj(nr -> new Wettkaempfer(
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
			))
			.toList();
	}
}
