package de.sinnix.judoturnier.application;

import de.sinnix.judoturnier.adapter.secondary.BenutzerRepository;
import de.sinnix.judoturnier.adapter.secondary.TurnierRepository;
import de.sinnix.judoturnier.adapter.secondary.WertungRepository;
import de.sinnix.judoturnier.fixtures.MatteFixtures;
import de.sinnix.judoturnier.fixtures.WettkaempferFixtures;
import de.sinnix.judoturnier.fixtures.WettkampfgruppeFixture;
import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Begegnung;
import de.sinnix.judoturnier.model.Benutzer;
import de.sinnix.judoturnier.model.BenutzerRolle;
import de.sinnix.judoturnier.model.Runde;
import de.sinnix.judoturnier.model.Wertung;
import de.sinnix.judoturnier.model.Wettkaempfer;
import de.sinnix.judoturnier.model.WettkampfGruppe;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WertungServiceTest {

	private static final Logger logger = LogManager.getLogger(WertungServiceTest.class);

	@Mock
	private WertungRepository   wertungRepository;
	@Mock
	private TurnierRepository   turnierRepository;
	@Mock
	private BenutzerRepository  benutzerRepository;
	@Mock
	private WettkaempferService wettkaempferService;
	@Mock
	private WettkampfService    wettkampfService;

	@InjectMocks
	private WertungService wertungService;

	private final UUID     turnierUUID = MatteFixtures.turnierUUID;
	private       Benutzer kampfrichter;

	@BeforeEach
	void setUp() {
		kampfrichter = new Benutzer(UUID.fromString("898a7fcf-2fad-4ec9-8b4f-5513188af291"), "user1", "Name, Vorname", List.of(), List.of(BenutzerRolle.KAMPFRICHTER));
	}

	@Test
	void speichereRandoriWertung() {
		UUID begegnungId = UUID.randomUUID();
		Wertung neueWertung = new Wertung(null, null, null, null, null, null, null,
			1, 2, 3, 4, 4, 3, 2, 1,
			kampfrichter
		);

		when(benutzerRepository.findBenutzer(kampfrichter.uuid())).thenReturn(Optional.of(kampfrichter));

		wertungService.speichereRandoriWertung(begegnungId, 1, 2, 3, 4, 4, 3, 2, 1, kampfrichter.uuid());

		verify(wertungRepository).speichereWertungInBegegnung(neueWertung, begegnungId);
	}

	@Test
	void speichereTurnierWertungErsterKampf() {
		UUID begegnungId = UUID.randomUUID();
		int scoreWeiss = 1;
		int scoreBlau = 0;
		int penaltiesWeiss = 0;
		int penaltiesBlau = 0;
		String fighttime = "02:03.54"; // entspricht 123.540ms
		Duration fighttimeDuration = Duration.ofMillis(123540);
		Wettkaempfer sieger = WettkaempferFixtures.wettkaempferin1;
		int aktuelleMattenRunde = 2;
		int aktuellePaarung = 1;
		int aktuelleGruppenRunde = 1;
		int aktuelleGesamtRunde = 2;
		Begegnung aktuelleBegegnung = new Begegnung(
			UUID.randomUUID(),
			new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, aktuelleMattenRunde, aktuellePaarung),
			UUID.randomUUID(),
			1, aktuelleMattenRunde, aktuelleGruppenRunde, aktuelleGesamtRunde,
			Optional.of(WettkaempferFixtures.wettkaempferin1), Optional.of(WettkaempferFixtures.wettkaempferin2),
			new ArrayList<>(),
			WettkampfgruppeFixture.gruppe1.gruppe(),
			turnierUUID
		);
		Wertung neueWertung = new Wertung(null, WettkaempferFixtures.wettkaempferin1, fighttimeDuration,
			scoreWeiss, 0, 0, penaltiesWeiss, false,
			scoreBlau, 0, 0, penaltiesBlau, false,
			null, null, null, null, null, null, null, null,
			kampfrichter
		);

		when(wettkaempferService.ladeKaempfer(sieger.id())).thenReturn(Optional.of(sieger));
		when(benutzerRepository.findBenutzer(eq(kampfrichter.uuid()))).thenReturn(Optional.of(kampfrichter));
		when(wettkampfService.ladeBegegnung(begegnungId)).thenReturn(aktuelleBegegnung);


		wertungService.speichereTurnierWertung(begegnungId, scoreWeiss, 0, 0, penaltiesWeiss, false, scoreBlau, 0, 0, penaltiesBlau, false, fighttime, sieger.id(), kampfrichter.uuid());

		verify(wertungRepository, times(1)).speichereWertungInBegegnung(neueWertung, begegnungId);
		verify(wettkampfService, times(1)).ladeBegegnung(begegnungId);
		verify(turnierRepository, times(1)).ladeWettkampfgruppeRunden(aktuelleBegegnung.getWettkampfGruppe().id(), turnierUUID);
		// keine weiteren Begegnungen
		verify(turnierRepository, times(0)).speichereBegegnung(any());
	}

	@Test
	void speichereTurnierWertungSetztHansokuMakeBeiDreiShido() {
		UUID begegnungId = UUID.randomUUID();
		String fighttime = "02:03.54";
		Duration fighttimeDuration = Duration.ofMillis(123540);
		Wettkaempfer sieger = WettkaempferFixtures.wettkaempferin2;
		Begegnung aktuelleBegegnung = new Begegnung(
			UUID.randomUUID(),
			new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),
			UUID.randomUUID(),
			1, 1, 1, 1,
			Optional.of(WettkaempferFixtures.wettkaempferin1), Optional.of(WettkaempferFixtures.wettkaempferin2),
			new ArrayList<>(),
			WettkampfgruppeFixture.gruppe1.gruppe(),
			turnierUUID
		);
		Wertung neueWertung = new Wertung(null, sieger, fighttimeDuration,
			0, 0, 0, 3, true,
			0, 0, 0, 0, false,
			null, null, null, null, null, null, null, null,
			kampfrichter
		);

		when(wettkaempferService.ladeKaempfer(sieger.id())).thenReturn(Optional.of(sieger));
		when(benutzerRepository.findBenutzer(eq(kampfrichter.uuid()))).thenReturn(Optional.of(kampfrichter));
		when(wettkampfService.ladeBegegnung(begegnungId)).thenReturn(aktuelleBegegnung);

		wertungService.speichereTurnierWertung(begegnungId, 0, 0, 0, 3, false, 0, 0, 0, 0, false, fighttime, sieger.id(), kampfrichter.uuid());

		verify(wertungRepository, times(1)).speichereWertungInBegegnung(neueWertung, begegnungId);
	}

	@Test
	void speichereTurnierWertungZweiterKampf() {
		int scoreWeiss = 1;
		int scoreBlau = 0;
		int penaltiesWeiss = 0;
		int penaltiesBlau = 0;
		String fighttime = "02:03.54"; // entspricht 123.540ms
		Duration fighttimeDuration = Duration.ofMillis(123540);
		Wettkaempfer sieger = WettkaempferFixtures.wettkaempferin2;
		var aktuelleBegegnungUUID = WettkampfgruppeFixture.b2UUID;
		var aktuelleBegegnungId = new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 2);
		var naechsteBegegnungUUID = WettkampfgruppeFixture.b7UUID;
		var naechsteBegegnungId = new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 2, 1);

		WettkampfGruppe wkg = WettkampfgruppeFixture.wettkampfGruppeFrauen.gruppe();
		List<Begegnung> begegnungListRunde1 = new ArrayList<>();
		begegnungListRunde1.add(new Begegnung(WettkampfgruppeFixture.b1UUID, new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1), null, null, null, null, null,
			Optional.of(WettkaempferFixtures.wettkaempferin1),
			Optional.empty(),
			null, wkg, turnierUUID));
		begegnungListRunde1.add(new Begegnung(aktuelleBegegnungUUID, aktuelleBegegnungId, null, null, null, null, null,
			Optional.of(WettkaempferFixtures.wettkaempferin2),
			Optional.of(WettkaempferFixtures.wettkaempferin3),
			new ArrayList<>(), wkg, turnierUUID));
		begegnungListRunde1.add(new Begegnung(WettkampfgruppeFixture.b3UUID, new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 3), null, null, null, null, null,
			Optional.of(WettkaempferFixtures.wettkaempferin4),
			Optional.of(WettkaempferFixtures.wettkaempferin5),
			null, wkg, turnierUUID));
		begegnungListRunde1.add(new Begegnung(WettkampfgruppeFixture.b4UUID, new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 4), null, null, null, null, null,
			Optional.of(WettkaempferFixtures.wettkaempferin6),
			Optional.of(WettkaempferFixtures.wettkaempferin7),
			null, wkg, turnierUUID));
		begegnungListRunde1.add(new Begegnung(WettkampfgruppeFixture.b5UUID, new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 1, 1), null, null, null, null, null,
			Optional.empty(),
			Optional.empty(),
			null, wkg, turnierUUID));
		begegnungListRunde1.add(new Begegnung(WettkampfgruppeFixture.b6UUID, new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 1, 2), null, null, null, null, null,
			Optional.empty(),
			Optional.empty(),
			null, wkg, turnierUUID));
		List<Begegnung> begegnungListRunde2 = new ArrayList<>();
		begegnungListRunde2.add(new Begegnung(WettkampfgruppeFixture.b7UUID, new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 2, 1), null, null, null, null, null,
			Optional.empty(),
			Optional.empty(),
			null, wkg, turnierUUID));
		begegnungListRunde2.add(new Begegnung(naechsteBegegnungUUID, naechsteBegegnungId, null, null, null, null, null,
			Optional.empty(),
			Optional.empty(),
			null, wkg, turnierUUID));
		begegnungListRunde2.add(new Begegnung(WettkampfgruppeFixture.b9UUID, new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 2, 1), null, null, null, null, null,
			Optional.empty(),
			Optional.empty(),
			null, wkg, turnierUUID));
		begegnungListRunde2.add(new Begegnung(WettkampfgruppeFixture.b10UUID, new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 2, 2), null, null, null, null, null,
			Optional.empty(),
			Optional.empty(),
			null, wkg, turnierUUID));
		List<Begegnung> begegnungListRunde3 = new ArrayList<>();
		begegnungListRunde3.add(new Begegnung(WettkampfgruppeFixture.b11UUID, new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 3, 1), null, null, null, null, null,
			Optional.empty(),
			Optional.empty(),
			null, wkg, turnierUUID));

		List<Runde> rundenList = new ArrayList<>();
		UUID rundeUUID1 = UUID.randomUUID();
		UUID rundeUUID2 = UUID.randomUUID();
		UUID rundeUUID3 = UUID.randomUUID();
		rundenList.add(new Runde(rundeUUID1, 1, 1, 1, 1, Altersklasse.FRAUEN, WettkampfgruppeFixture.wettkampfGruppeFrauen.gruppe(), begegnungListRunde1));
		rundenList.add(new Runde(rundeUUID2, 2, 2, 2, 1, Altersklasse.FRAUEN, WettkampfgruppeFixture.wettkampfGruppeFrauen.gruppe(), begegnungListRunde2));
		rundenList.add(new Runde(rundeUUID3, 3, 3, 3, 1, Altersklasse.FRAUEN, WettkampfgruppeFixture.wettkampfGruppeFrauen.gruppe(), begegnungListRunde3));

		Wertung neueWertung = new Wertung(null, WettkaempferFixtures.wettkaempferin2, fighttimeDuration,
			scoreWeiss, 0, 0, penaltiesWeiss, false,
			scoreBlau, 0, 0, penaltiesBlau, false,
			null, null, null, null, null, null, null, null,
			kampfrichter
		);

		when(wettkampfService.ladeBegegnung(any())).thenReturn(begegnungListRunde1.get(1));
		when(benutzerRepository.findBenutzer(eq(kampfrichter.uuid()))).thenReturn(Optional.of(kampfrichter));
		when(benutzerRepository.findBenutzerByUsername(Benutzer.ANONYMOUS_KAMPFRICHTER)).thenReturn(Optional.of(kampfrichter));
		when(wettkaempferService.ladeKaempfer(eq(sieger.id()))).thenReturn(Optional.ofNullable(sieger));
		when(turnierRepository.ladeWettkampfgruppeRunden(wkg.id(), turnierUUID)).thenReturn(rundenList);


		wertungService.speichereTurnierWertung(aktuelleBegegnungUUID, scoreWeiss, 0, 0, penaltiesWeiss, false, scoreBlau, 0, 0, penaltiesBlau, false, fighttime, sieger.id(), kampfrichter.uuid());

		verify(wertungRepository, times(1)).speichereWertungInBegegnung(neueWertung, aktuelleBegegnungUUID);
		verify(wertungRepository, times(2)).speichereWertungInBegegnung(any(), any());

		ArgumentCaptor<Begegnung> begegnungCaptor = ArgumentCaptor.forClass(Begegnung.class);
		verify(turnierRepository, times(3)).speichereBegegnung(begegnungCaptor.capture());
		List<Begegnung> begegnungList = begegnungCaptor.getAllValues();
		Begegnung nachfolgerGewinnerBegegnung = begegnungList.get(0);
		assertEquals(WettkampfgruppeFixture.b7UUID, nachfolgerGewinnerBegegnung.getId());
		assertEquals(sieger, nachfolgerGewinnerBegegnung.getWettkaempfer1().get());
		assertEquals(Optional.empty(), nachfolgerGewinnerBegegnung.getWettkaempfer2());
		Begegnung nachfolgerTrostBegegnung = begegnungList.get(1);
		assertEquals(WettkampfgruppeFixture.b5UUID, nachfolgerTrostBegegnung.getId());
		assertEquals(WettkaempferFixtures.wettkaempferin3, nachfolgerTrostBegegnung.getWettkaempfer1().get());
		assertEquals(Optional.empty(), nachfolgerTrostBegegnung.getWettkaempfer2());
		Begegnung freilosNachfolgerTrostBegegnung = begegnungList.get(2);
		assertEquals(WettkampfgruppeFixture.b9UUID, freilosNachfolgerTrostBegegnung.getId());
		assertEquals(WettkaempferFixtures.wettkaempferin3, freilosNachfolgerTrostBegegnung.getWettkaempfer1().get());
		assertEquals(Optional.empty(), freilosNachfolgerTrostBegegnung.getWettkaempfer2());
	}

	@Test
	void speichereTurnierWertungTrostrundeSetztSiegerInNaechsteTrostrunde() {
		int scoreWeiss = 1;
		int scoreBlau = 0;
		int penaltiesWeiss = 0;
		int penaltiesBlau = 0;
		String fighttime = "02:03.54";
		Duration fighttimeDuration = Duration.ofMillis(123540);
		Wettkaempfer sieger = WettkaempferFixtures.wettkaempferin3;
		var aktuelleBegegnungUUID = WettkampfgruppeFixture.b5UUID;
		var aktuelleBegegnungId = new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 1, 1);

		WettkampfGruppe wkg = WettkampfgruppeFixture.wettkampfGruppeFrauen.gruppe();
		Begegnung aktuelleBegegnung = new Begegnung(aktuelleBegegnungUUID, aktuelleBegegnungId, null, null, null, null, null,
			Optional.of(WettkaempferFixtures.wettkaempferin2),
			Optional.of(WettkaempferFixtures.wettkaempferin3),
			new ArrayList<>(), wkg, turnierUUID);
		Begegnung naechsteTrostrunde = new Begegnung(WettkampfgruppeFixture.b9UUID, new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 2, 1), null, null, null, null, null,
			Optional.empty(),
			Optional.empty(),
			new ArrayList<>(), wkg, turnierUUID);
		List<Runde> rundenList = List.of(
			new Runde(UUID.randomUUID(), 1, 1, 1, 1, Altersklasse.FRAUEN, wkg, List.of(aktuelleBegegnung)),
			new Runde(UUID.randomUUID(), 2, 2, 2, 1, Altersklasse.FRAUEN, wkg, List.of(naechsteTrostrunde))
		);
		Wertung neueWertung = new Wertung(null, sieger, fighttimeDuration,
			scoreWeiss, 0, 0, penaltiesWeiss, false,
			scoreBlau, 0, 0, penaltiesBlau, false,
			null, null, null, null, null, null, null, null,
			kampfrichter
		);

		when(wettkampfService.ladeBegegnung(aktuelleBegegnungUUID)).thenReturn(aktuelleBegegnung);
		when(benutzerRepository.findBenutzer(eq(kampfrichter.uuid()))).thenReturn(Optional.of(kampfrichter));
		when(wettkaempferService.ladeKaempfer(eq(sieger.id()))).thenReturn(Optional.of(sieger));
		when(turnierRepository.ladeWettkampfgruppeRunden(wkg.id(), turnierUUID)).thenReturn(rundenList);

		wertungService.speichereTurnierWertung(aktuelleBegegnungUUID, scoreWeiss, 0, 0, penaltiesWeiss, false, scoreBlau, 0, 0, penaltiesBlau, false, fighttime, sieger.id(), kampfrichter.uuid());

		verify(wertungRepository, times(1)).speichereWertungInBegegnung(neueWertung, aktuelleBegegnungUUID);
		ArgumentCaptor<Begegnung> begegnungCaptor = ArgumentCaptor.forClass(Begegnung.class);
		verify(turnierRepository, times(1)).speichereBegegnung(begegnungCaptor.capture());
		Begegnung gespeicherteTrostrunde = begegnungCaptor.getValue();
		assertEquals(WettkampfgruppeFixture.b9UUID, gespeicherteTrostrunde.getId());
		assertEquals(sieger, gespeicherteTrostrunde.getWettkaempfer1().get());
		assertEquals(Optional.empty(), gespeicherteTrostrunde.getWettkaempfer2());
	}

	@Test
	void speichereTurnierWertungErsteTrostrundeWartetOhneFreilosVorgaengerAufZweitenVerlierer() {
		int scoreWeiss = 1;
		int scoreBlau = 0;
		int penaltiesWeiss = 0;
		int penaltiesBlau = 0;
		String fighttime = "02:03.54";
		Duration fighttimeDuration = Duration.ofMillis(123540);
		Wettkaempfer sieger = WettkaempferFixtures.wettkaempferin1;
		var aktuelleBegegnungUUID = WettkampfgruppeFixture.b1UUID;
		var aktuelleBegegnungId = new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1);

		WettkampfGruppe wkg = WettkampfgruppeFixture.wettkampfGruppeFrauen.gruppe();
		Begegnung aktuelleBegegnung = new Begegnung(aktuelleBegegnungUUID, aktuelleBegegnungId, null, null, null, null, null,
			Optional.of(WettkaempferFixtures.wettkaempferin1),
			Optional.of(WettkaempferFixtures.wettkaempferin2),
			new ArrayList<>(), wkg, turnierUUID);
		Begegnung zweiteGewinnerrundenBegegnung = new Begegnung(WettkampfgruppeFixture.b2UUID, new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 2), null, null, null, null, null,
			Optional.of(WettkaempferFixtures.wettkaempferin3),
			Optional.of(WettkaempferFixtures.wettkaempferin4),
			new ArrayList<>(), wkg, turnierUUID);
		Begegnung naechsteGewinnerrunde = new Begegnung(WettkampfgruppeFixture.b7UUID, new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 2, 1), null, null, null, null, null,
			Optional.empty(),
			Optional.empty(),
			new ArrayList<>(), wkg, turnierUUID);
		Begegnung ersteTrostrunde = new Begegnung(WettkampfgruppeFixture.b5UUID, new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 1, 1), null, null, null, null, null,
			Optional.empty(),
			Optional.empty(),
			new ArrayList<>(), wkg, turnierUUID);
		List<Runde> rundenList = List.of(
			new Runde(UUID.randomUUID(), 1, 1, 1, 1, Altersklasse.FRAUEN, wkg, List.of(aktuelleBegegnung, zweiteGewinnerrundenBegegnung, ersteTrostrunde)),
			new Runde(UUID.randomUUID(), 2, 2, 2, 1, Altersklasse.FRAUEN, wkg, List.of(naechsteGewinnerrunde))
		);
		Wertung neueWertung = new Wertung(null, sieger, fighttimeDuration,
			scoreWeiss, 0, 0, penaltiesWeiss, false,
			scoreBlau, 0, 0, penaltiesBlau, false,
			null, null, null, null, null, null, null, null,
			kampfrichter
		);

		when(wettkampfService.ladeBegegnung(aktuelleBegegnungUUID)).thenReturn(aktuelleBegegnung);
		when(benutzerRepository.findBenutzer(eq(kampfrichter.uuid()))).thenReturn(Optional.of(kampfrichter));
		when(wettkaempferService.ladeKaempfer(eq(sieger.id()))).thenReturn(Optional.of(sieger));
		when(turnierRepository.ladeWettkampfgruppeRunden(wkg.id(), turnierUUID)).thenReturn(rundenList);

		wertungService.speichereTurnierWertung(aktuelleBegegnungUUID, scoreWeiss, 0, 0, penaltiesWeiss, false, scoreBlau, 0, 0, penaltiesBlau, false, fighttime, sieger.id(), kampfrichter.uuid());

		verify(wertungRepository, times(1)).speichereWertungInBegegnung(neueWertung, aktuelleBegegnungUUID);
		ArgumentCaptor<Begegnung> begegnungCaptor = ArgumentCaptor.forClass(Begegnung.class);
		verify(turnierRepository, times(2)).speichereBegegnung(begegnungCaptor.capture());
		List<Begegnung> gespeicherteBegegnungen = begegnungCaptor.getAllValues();
		assertEquals(WettkampfgruppeFixture.b7UUID, gespeicherteBegegnungen.get(0).getId());
		assertEquals(WettkampfgruppeFixture.b5UUID, gespeicherteBegegnungen.get(1).getId());
		assertEquals(WettkaempferFixtures.wettkaempferin2, gespeicherteBegegnungen.get(1).getWettkaempfer1().get());
	}

	@Test
	@Disabled
	public void testBerechnePlatzierungen() {
		/**
		 * SIEGERRUNDE
		 . RUNDE 1                                    RUNDE 2
		 . "Fox, Sweetie" vs "Reid, Riley" ------
		 .                                       |___ "Fox, Sweetie" vs "Jameson, Jenna"  (---> Sieger "Fox, Sweetie")
		 .                                       |
		 . "Jameson, Jenna" vs "Belle, Lexi" ----
		 .
		 .TROSTRUNDE
		 |
		 .                                        --- "Reid, Riley" vs "Belle, Lexi"  (---> Sieger "Belle, Lexi")
		 .
		 */
		Benutzer bewerter = new Benutzer(UUID.randomUUID(), "kr1", "Kampfrichter 1", List.of(), List.of(BenutzerRolle.KAMPFRICHTER));
		Wertung wertung1 = new Wertung(UUID.randomUUID(), WettkaempferFixtures.wettkaempferin1, Duration.ofMinutes(2), 1, 0, 0, 0, null, null, null, null, null, null, null, null, bewerter);
		Wertung wertung2 = new Wertung(UUID.randomUUID(), WettkaempferFixtures.wettkaempferin3, Duration.ofMinutes(2), 0, 0, 1, 0, null, null, null, null, null, null, null, null, bewerter);
		Wertung wertung3 = new Wertung(UUID.randomUUID(), WettkaempferFixtures.wettkaempferin1, Duration.ofMinutes(2), 1, 0, 0, 0, null, null, null, null, null, null, null, null, bewerter);
		Wertung wertung4 = new Wertung(UUID.randomUUID(), WettkaempferFixtures.wettkaempferin4, Duration.ofMinutes(2), 1, 0, 0, 0, null, null, null, null, null, null, null, null, bewerter);

		List<Begegnung> begegnungList = List.of(
			new Begegnung(UUID.randomUUID(), new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1), UUID.randomUUID(), 1, 1, 1, 1, Optional.of(WettkaempferFixtures.wettkaempferin1), Optional.of(WettkaempferFixtures.wettkaempferin2), List.of(wertung1), WettkampfgruppeFixture.gruppe1.gruppe(), turnierUUID),
			new Begegnung(UUID.randomUUID(), new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 2), UUID.randomUUID(), 1, 2, 2, 2, Optional.of(WettkaempferFixtures.wettkaempferin3), Optional.of(WettkaempferFixtures.wettkaempferin4), List.of(wertung2), WettkampfgruppeFixture.gruppe1.gruppe(), turnierUUID),
			new Begegnung(UUID.randomUUID(), new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 2, 3), UUID.randomUUID(), 1, 3, 3, 3, Optional.of(WettkaempferFixtures.wettkaempferin1), Optional.of(WettkaempferFixtures.wettkaempferin3), List.of(wertung3), WettkampfgruppeFixture.gruppe1.gruppe(), turnierUUID),
			new Begegnung(UUID.randomUUID(), new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 2, 4), UUID.randomUUID(), 1, 4, 4, 4, Optional.of(WettkaempferFixtures.wettkaempferin4), Optional.of(WettkaempferFixtures.wettkaempferin2), List.of(wertung4), WettkampfgruppeFixture.gruppe1.gruppe(), turnierUUID)
		);
		when(turnierRepository.ladeAlleBegegnungen(any())).thenReturn(begegnungList);

		var results = wertungService.berechnePlatzierungen(turnierUUID);

		logger.info("1 {}", WettkaempferFixtures.wettkaempferin1.name());
		logger.info("2 {}", WettkaempferFixtures.wettkaempferin2.name());
		logger.info("3 {}", WettkaempferFixtures.wettkaempferin3.name());
		logger.info("4 {}", WettkaempferFixtures.wettkaempferin4.name());
		for (Map.Entry<Wettkaempfer, Integer> result : results.entrySet()) {
			logger.info("Result {} - Siege: {}x", result.getKey().name(), result.getValue());
		}

		assertEquals(results.get(WettkaempferFixtures.wettkaempferin1), 1); // "Fox, Sweetie", Erster Platz
		assertEquals(results.get(WettkaempferFixtures.wettkaempferin2), 4); // "Reid, Riley", Letzter Platz
		assertEquals(results.get(WettkaempferFixtures.wettkaempferin3), 2); // "Jameson, Jenna", Zweiter Platz
		assertEquals(results.get(WettkaempferFixtures.wettkaempferin4), 3); // "Belle, Lexi", Dritter Platz
	}
}
