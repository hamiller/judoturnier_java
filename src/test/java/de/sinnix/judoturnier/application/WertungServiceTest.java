package de.sinnix.judoturnier.application;

import de.sinnix.judoturnier.adapter.secondary.BenutzerRepository;
import de.sinnix.judoturnier.adapter.secondary.TurnierRepository;
import de.sinnix.judoturnier.fixtures.MatteFixtures;
import de.sinnix.judoturnier.fixtures.WettkaempferFixtures;
import de.sinnix.judoturnier.fixtures.WettkampfgruppeFixture;
import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Begegnung;
import de.sinnix.judoturnier.model.Benutzer;
import de.sinnix.judoturnier.model.BenutzerRolle;
import de.sinnix.judoturnier.model.Runde;
import de.sinnix.judoturnier.model.Wertung;
import de.sinnix.judoturnier.model.WettkampfGruppe;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
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
	private       Benutzer benutzer;

	@BeforeEach
	void setUp() {
		benutzer = new Benutzer(UUID.fromString("898a7fcf-2fad-4ec9-8b4f-5513188af291"), "user1", "Name, Vorname", List.of(), List.of(BenutzerRolle.KAMPFRICHTER));
	}

	@Test
	void testAktualisiereExistierendeRandoriWertung() {
		UUID turnierUUID = UUID.randomUUID();
		UUID rundeUUID = UUID.randomUUID();
		UUID id = UUID.randomUUID();
		List<Wertung> wertungList = new ArrayList<>();
		Wertung alteWertung = new Wertung(UUID.randomUUID(), null, null, null, null, null, null,
			1, 2, 3, 4, 4, 3, 2, 1,
			benutzer
		);
		wertungList.add(alteWertung);
		Begegnung.BegegnungId begegnungId = new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1);
		Begegnung begegnung = new Begegnung(id, begegnungId, rundeUUID,
			1, 2, 3, 3,
			WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2,
			wertungList,
			WettkampfgruppeFixture.gruppe1,
			turnierUUID
		);

		List<Wertung> neueWertungList = new ArrayList<>();
		Wertung neueWertung = new Wertung(alteWertung.getUuid(), null, null, null, null, null, null,
			1, 2, 3, 4, 4, 3, 2, 1,
			benutzer
		);
		neueWertungList.add(neueWertung);

		when(wettkampfService.ladeBegegnung(id)).thenReturn(begegnung);
		when(benutzerRepository.findBenutzer(benutzer.uuid())).thenReturn(Optional.of(benutzer));

		wertungService.speichereRandoriWertung(id, 1, 2, 3, 4, 4, 3, 2, 1, benutzer.uuid());

		ArgumentCaptor<Begegnung> argumentCaptor = ArgumentCaptor.forClass(Begegnung.class);
		verify(turnierRepository).speichereBegegnung(argumentCaptor.capture());
		Begegnung result = argumentCaptor.getValue();
		assertEquals(neueWertungList, result.getWertungen());
	}

	@Test
	void testWeitereRandoriWertung() {
		Benutzer benutzerA = new Benutzer(UUID.randomUUID(), "username", "name", List.of(), List.of(BenutzerRolle.KAMPFRICHTER));
		Benutzer benutzerB = new Benutzer(UUID.randomUUID(), "username", "name", List.of(), List.of(BenutzerRolle.KAMPFRICHTER));
		UUID turnierUUID = UUID.randomUUID();
		UUID rundeUUID = UUID.randomUUID();
		UUID id = UUID.randomUUID();
		List<Wertung> wertungList = new ArrayList<>();
		Wertung alteWertung = new Wertung(UUID.randomUUID(), null, null, null, null, null, null,
			1, 2, 3, 4, 4, 3, 2, 1,
			benutzerA
		);
		wertungList.add(alteWertung);
		Begegnung.BegegnungId begegnungId = new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1);
		Begegnung begegnung = new Begegnung(id,
			begegnungId,
			rundeUUID,
			1, 2, 3, 3,
			WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2,
			wertungList,
			WettkampfgruppeFixture.gruppe1,
			turnierUUID
		);

		List<Wertung> neueWertungList = new ArrayList<>();
		Wertung neueWertung = new Wertung(null, null, null, null, null, null, null,
			1, 2, 3, 4, 4, 3, 2, 1,
			benutzerB
		);
		neueWertungList.add(alteWertung);
		neueWertungList.add(neueWertung);

		when(wettkampfService.ladeBegegnung(id)).thenReturn(begegnung);
		when(benutzerRepository.findBenutzer(benutzerB.uuid())).thenReturn(Optional.of(benutzerB));

		wertungService.speichereRandoriWertung(id, 1, 2, 3, 4, 4, 3, 2, 1, benutzerB.uuid());

		ArgumentCaptor<Begegnung> argumentCaptor = ArgumentCaptor.forClass(Begegnung.class);
		verify(turnierRepository).speichereBegegnung(argumentCaptor.capture());
		Begegnung result = argumentCaptor.getValue();
		assertEquals(2, result.getWertungen().size());
		assertEquals(benutzerA, result.getWertungen().getFirst().getBewerter());
		assertEquals(benutzerB, result.getWertungen().get(1).getBewerter());
	}

	@Test
	void testSpeichereNeueRandoriWertung() {
		UUID turnierUUID = UUID.randomUUID();
		UUID rundeUUID = UUID.randomUUID();
		UUID id = UUID.randomUUID();
		Begegnung.BegegnungId begegnungId = new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1);
		Begegnung begegnung = new Begegnung(id, begegnungId, rundeUUID,
			1, 2, 3, 3,
			WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2,
			new ArrayList<>(),
			WettkampfgruppeFixture.gruppe1,
			turnierUUID
		);

		when(wettkampfService.ladeBegegnung(id)).thenReturn(begegnung);
		when(benutzerRepository.findBenutzer(benutzer.uuid())).thenReturn(Optional.of(benutzer));

		wertungService.speichereRandoriWertung(id, 1, 2, 3, 4, 4, 3, 2, 1, benutzer.uuid());

		ArgumentCaptor<Begegnung> argumentCaptor = ArgumentCaptor.forClass(Begegnung.class);
		verify(turnierRepository).speichereBegegnung(argumentCaptor.capture());
		Begegnung result = argumentCaptor.getValue();
		assertEquals(1, result.getWertungen().size());
		assertEquals(benutzer, result.getWertungen().getFirst().getBewerter());
		assertEquals(1, result.getWertungen().getFirst().getKampfgeistWettkaempfer1());
		assertEquals(2, result.getWertungen().getFirst().getTechnikWettkaempfer1());
		assertEquals(3, result.getWertungen().getFirst().getKampfstilWettkaempfer1());
		assertEquals(4, result.getWertungen().getFirst().getVielfaltWettkaempfer1());
		assertEquals(4, result.getWertungen().getFirst().getKampfgeistWettkaempfer2());
		assertEquals(3, result.getWertungen().getFirst().getTechnikWettkaempfer2());
		assertEquals(2, result.getWertungen().getFirst().getKampfstilWettkaempfer2());
		assertEquals(1, result.getWertungen().getFirst().getVielfaltWettkaempfer2());
	}

	@Test
	void speichereTurnierWertungErsterKampf() {
		int scoreWeiss = 1;
		int scoreBlau = 0;
		int penaltiesWeiss = 0;
		int penaltiesBlau = 0;
		String fighttime = "02:03.54"; // entspricht 123.540ms
		UUID siegerUuid = WettkaempferFixtures.wettkaempfer1.get().id();

		int aktuelleMattenRunde = 2;
		int aktuellePaarung = 1;
		int aktuelleGruppenRunde = 1;
		int aktuelleGesamtRunde = 2;
		Begegnung.BegegnungId begegnungId = new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, aktuelleMattenRunde, aktuellePaarung);
		Begegnung aktuelleBegegnung = new Begegnung(UUID.randomUUID(), begegnungId, UUID.randomUUID(),
			1, aktuelleMattenRunde, aktuelleGruppenRunde, aktuelleGesamtRunde,
			WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2,
			new ArrayList<>(),
			WettkampfgruppeFixture.gruppe1,
			turnierUUID
		);

		// Teil 1
		when(wettkampfService.ladeBegegnung(any())).thenReturn(aktuelleBegegnung);
		when(benutzerRepository.findBenutzer(eq(benutzer.uuid()))).thenReturn(Optional.of(benutzer));
		when(wettkaempferService.ladeKaempfer(eq(siegerUuid))).thenReturn(WettkaempferFixtures.wettkaempfer1);
		when(turnierRepository.ladeWettkampfgruppeRunden(eq(WettkampfgruppeFixture.gruppe1.id()), eq(turnierUUID))).thenReturn(List.of());

		wertungService.speichereTurnierWertung(aktuelleBegegnung.getId(), scoreWeiss, scoreBlau, penaltiesWeiss, penaltiesBlau, fighttime, siegerUuid, benutzer.uuid());


		ArgumentCaptor<Begegnung> argumentCaptor = ArgumentCaptor.forClass(Begegnung.class);
		verify(turnierRepository, times(1)).speichereBegegnung(argumentCaptor.capture());

		Begegnung gespeichert = argumentCaptor.getAllValues().getFirst();
		assertEquals(begegnungId, gespeichert.getBegegnungId());
		assertEquals(1, gespeichert.getWertungen().size());
		assertEquals(siegerUuid, gespeichert.getWertungen().getFirst().getSieger().id());
		assertEquals(scoreWeiss, gespeichert.getWertungen().getFirst().getPunkteWettkaempferWeiss());
		assertEquals(scoreBlau, gespeichert.getWertungen().getFirst().getPunkteWettkaempferRot());
		assertEquals(penaltiesWeiss, gespeichert.getWertungen().getFirst().getStrafenWettkaempferWeiss());
		assertEquals(penaltiesBlau, gespeichert.getWertungen().getFirst().getStrafenWettkaempferRot());
		assertEquals(Duration.ofMillis(123540), gespeichert.getWertungen().getFirst().getZeit());
	}

	@Test
	void speichereTurnierWertungZweiterKampf() {
		int scoreWeiss = 1;
		int scoreBlau = 0;
		int penaltiesWeiss = 0;
		int penaltiesBlau = 0;
		String fighttime = "02:03.54"; // entspricht 123.540ms
		UUID siegerUuid = WettkaempferFixtures.wettkaempferin2.id();
		var aktuelleBegegnungUUID = WettkampfgruppeFixture.b2UUID;
		var aktuelleBegegnungId = new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 2);
		var naechsteBegegnungUUID = WettkampfgruppeFixture.b7UUID;
		var naechsteBegegnungId = new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 2, 1);

		WettkampfGruppe wkg = WettkampfgruppeFixture.wettkampfGruppeFrauen;
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
		rundenList.add(new Runde(rundeUUID1, 1, 1, 1, 1, Altersklasse.Frauen, WettkampfgruppeFixture.wettkampfGruppeFrauen, begegnungListRunde1));
		rundenList.add(new Runde(rundeUUID2, 2, 2, 2, 1, Altersklasse.Frauen, WettkampfgruppeFixture.wettkampfGruppeFrauen, begegnungListRunde2));
		rundenList.add(new Runde(rundeUUID3, 3, 3, 3, 1, Altersklasse.Frauen, WettkampfgruppeFixture.wettkampfGruppeFrauen, begegnungListRunde3));

		// Teil 1
		when(wettkampfService.ladeBegegnung(any())).thenReturn(begegnungListRunde1.get(1));
		when(benutzerRepository.findBenutzer(eq(benutzer.uuid()))).thenReturn(Optional.of(benutzer));
		when(wettkaempferService.ladeKaempfer(eq(siegerUuid))).thenReturn(Optional.ofNullable(WettkaempferFixtures.wettkaempferin2));

		// Teil 2
		when(turnierRepository.ladeWettkampfgruppeRunden(wkg.id(), turnierUUID)).thenReturn(rundenList);


		wertungService.speichereTurnierWertung(aktuelleBegegnungUUID, scoreWeiss, scoreBlau, penaltiesWeiss, penaltiesBlau, fighttime, siegerUuid, benutzer.uuid());


		// Verify Teil 1
		ArgumentCaptor<Begegnung> argumentCaptor = ArgumentCaptor.forClass(Begegnung.class);
		verify(turnierRepository, times(3)).speichereBegegnung(argumentCaptor.capture());

		Begegnung gespeichert = argumentCaptor.getAllValues().getFirst();
		assertEquals(aktuelleBegegnungId, gespeichert.getBegegnungId());
		assertEquals(aktuelleBegegnungUUID, gespeichert.getId());
		assertEquals(1, gespeichert.getWertungen().size());
		assertEquals(siegerUuid, gespeichert.getWertungen().getFirst().getSieger().id());
		assertEquals(scoreWeiss, gespeichert.getWertungen().getFirst().getPunkteWettkaempferWeiss());
		assertEquals(scoreBlau, gespeichert.getWertungen().getFirst().getPunkteWettkaempferRot());
		assertEquals(penaltiesWeiss, gespeichert.getWertungen().getFirst().getStrafenWettkaempferWeiss());
		assertEquals(penaltiesBlau, gespeichert.getWertungen().getFirst().getStrafenWettkaempferRot());
		assertEquals(Duration.ofMillis(123540), gespeichert.getWertungen().getFirst().getZeit());

		// Verify Teil 2
		Begegnung gespeichertNext = argumentCaptor.getAllValues().get(1);
		assertEquals(naechsteBegegnungId, gespeichertNext.getBegegnungId());
		assertEquals(naechsteBegegnungUUID, gespeichertNext.getId());
	}

}
