package de.sinnix.judoturnier.adapter.secondary;

import de.sinnix.judoturnier.fixtures.WettkaempferFixtures;
import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Begegnung;
import de.sinnix.judoturnier.model.Benutzer;
import de.sinnix.judoturnier.model.Matte;
import de.sinnix.judoturnier.model.Runde;
import de.sinnix.judoturnier.model.Wertung;
import de.sinnix.judoturnier.model.WettkampfGruppe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TurnierRepositoryTest {
	@Mock
	private WertungJpaRepository         wertungJpaRepository;
	@Mock
	private WertungConverter             wertungConverter;
	@Mock
	private BegegnungJpaRepository       begegnungJpaRepository;
	@Mock
	private BegegnungConverter           begegnungConverter;
	@Mock
	private WettkaempferConverter        wettkaempferConverter;
	@Mock
	private WettkampfGruppeConverter     wettkampfGruppeConverter;
	@Mock
	private WettkampfGruppeJpaRepository wettkampfGruppeJpaRepository;
	@InjectMocks
	private TurnierRepository            turnierRepository;

	private Benutzer benutzer;

	@BeforeEach
	public void setUp() {
		benutzer = new Benutzer(UUID.randomUUID().toString(), "user1", "Name, Vorname", List.of("ROLE_ZUSCHAUER"));
	}

	@Test
	void testLadeBegegnung() {
		UUID turnierUUID = UUID.randomUUID();
		UUID id = UUID.randomUUID();
		WettkampfGruppe wettkampfGruppe = new WettkampfGruppe(id, "Gruppe1", "typ1", Altersklasse.U11, List.of(), turnierUUID);
		Benutzer benutzer = new Benutzer(UUID.randomUUID().toString(), "user1", "Name, Vorname", List.of("ROLE_ZUSCHAUER"));
		Wertung wertung = new Wertung(
			UUID.randomUUID(),
			WettkaempferFixtures.wettkaempfer1.get(),
			Duration.of(3l, ChronoUnit.MINUTES),
			1,
			0,
			0,
			1,
			null, null, null, null, null, null, null, null,
			benutzer);
		Begegnung.BegegnungId begegnungId = new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1);
		Begegnung begegnung = new Begegnung(id, begegnungId, UUID.randomUUID(), 2, 123, 22, 123, WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2, List.of(wertung), wettkampfGruppe, turnierUUID);

		when(begegnungJpaRepository.findById(any())).thenReturn(Optional.of(new BegegnungJpa()));
		when(wettkampfGruppeJpaRepository.findAll()).thenReturn(List.of());
		when(begegnungConverter.convertToBegegnung(any(), anyList())).thenReturn(begegnung);

		Begegnung result = turnierRepository.ladeBegegnung(id);

		assertTrue(result != null);
		assertEquals(1, result.getWertungen().size());
		assertTrue(result.getWertungen().get(0).getUuid() != null);
	}

	@Test
	void testSpeichereWertung() {
		WertungJpa wertungJpa = new WertungJpa();
		Wertung wertung = new Wertung(
			null,
			WettkaempferFixtures.wettkaempfer1.get(),
			Duration.of(3l, ChronoUnit.MINUTES),
			1, 0, 0, 1,
			null, null, null, null, null, null, null, null,
			benutzer);

		when(wertungConverter.convertFromWertung(any(Wertung.class))).thenReturn(wertungJpa);

		turnierRepository.speichereWertung(wertung);

		ArgumentCaptor<WertungJpa> argumentCaptor = ArgumentCaptor.forClass(WertungJpa.class);
		verify(wertungJpaRepository, times(1)).save(argumentCaptor.capture());
		WertungJpa jpa = argumentCaptor.getValue();
		assertTrue(jpa != null);
	}

	@Test
	public void testSpeichereMatten() {
		UUID turnierUUID = WettkaempferFixtures.turnierUUID;
		UUID rundeUUID = UUID.randomUUID();
		UUID id = UUID.randomUUID();
		WettkampfGruppe wkg = new WettkampfGruppe(id, "name", "typ", Altersklasse.U11, List.of(), turnierUUID);
		List<Begegnung> begegnungList = new ArrayList<>();
		Begegnung.BegegnungId begegnungId = new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1);
		begegnungList.add(new Begegnung(null, begegnungId, rundeUUID, 2, 3, 4, 4, WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2, List.of(), wkg, turnierUUID));
		List<Runde> rundenList = new ArrayList<>();
		rundenList.add(new Runde(rundeUUID, 3, 4, 4, 5, Altersklasse.U11, wkg, begegnungList));
		Matte matte = new Matte(2, rundenList);
		List<Matte> mattenList = Arrays.asList(matte);

		WettkampfGruppeJpa wkgJpa = new WettkampfGruppeJpa(id.toString(), "name", "typ", "U11", turnierUUID.toString());
		List<BegegnungJpa> begegnungJpaList = new ArrayList<>();
		begegnungJpaList.add(new BegegnungJpa(null, rundeUUID.toString(), 2, 3, 4, 4, WettkaempferFixtures.wettkaempferJpa1, WettkaempferFixtures.wettkaempferJpa2, List.of(), wkgJpa.getUuid(), turnierUUID.toString(), 1, 1, 1));

		when(wettkampfGruppeJpaRepository.findById(any())).thenReturn(Optional.of(wkgJpa));
		when(wettkampfGruppeConverter.convertToWettkampfGruppe(any())).thenReturn(wkg);
		when(begegnungConverter.convertFromBegegnung(begegnungList.get(0))).thenReturn(begegnungJpaList.get(0));

		turnierRepository.speichereMatten(mattenList);

		verify(begegnungJpaRepository, times(1)).saveAll(begegnungJpaList);
	}

	@Test
	public void testSpeichereMatteExistingWettkampfgruppe() {
		UUID turnierUUID = WettkaempferFixtures.turnierUUID;
		UUID runde1UUID = UUID.randomUUID();
		UUID runde2UUID = UUID.randomUUID();
		UUID id = UUID.randomUUID();
		WettkampfGruppe wkg = new WettkampfGruppe(id, "name", "typ", Altersklasse.U11, List.of(), turnierUUID);
		Begegnung.BegegnungId begegnungId1 = new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1);
		Begegnung.BegegnungId begegnungId2 = new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1);
		Begegnung begegnung1 = new Begegnung(null, begegnungId1, runde1UUID, 5, 3, 4, 4, WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2, List.of(), wkg, turnierUUID);
		Begegnung begegnung2 = new Begegnung(null, begegnungId2, runde2UUID, 5, 3, 4, 4, WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2, List.of(), wkg, turnierUUID);
		Runde runde1 = new Runde(runde1UUID, 3, 4, 4, 5, Altersklasse.U12, wkg, Arrays.asList(begegnung1));
		Runde runde2 = new Runde(runde2UUID, 3, 4, 4, 5, Altersklasse.U12, wkg, Arrays.asList(begegnung2));
		Matte matte = new Matte(5, Arrays.asList(runde1, runde2));

		WettkampfGruppeJpa wkgJpa = new WettkampfGruppeJpa(id.toString(), "name", "typ", "U11", turnierUUID.toString());
		BegegnungJpa begegnungJpa1 = new BegegnungJpa(null, runde1UUID.toString(), 5, 3, 4, 4, WettkaempferFixtures.wettkaempferJpa1, WettkaempferFixtures.wettkaempferJpa2, List.of(), wkgJpa.getUuid(), turnierUUID.toString(), 1, 1, 1);
		BegegnungJpa begegnungJpa2 = new BegegnungJpa(null, runde2UUID.toString(), 5, 3, 4, 4, WettkaempferFixtures.wettkaempferJpa1, WettkaempferFixtures.wettkaempferJpa2, List.of(), wkgJpa.getUuid(), turnierUUID.toString(), 1, 1, 1);

		when(wettkampfGruppeJpaRepository.findById(any())).thenReturn(Optional.of(wkgJpa));
		when(wettkampfGruppeConverter.convertToWettkampfGruppe(any())).thenReturn(wkg);
		when(begegnungConverter.convertFromBegegnung(begegnung1)).thenReturn(begegnungJpa1);
		when(begegnungConverter.convertFromBegegnung(begegnung2)).thenReturn(begegnungJpa2);

		turnierRepository.speichereMatte(matte);

		verify(begegnungJpaRepository, times(1)).saveAll(List.of(begegnungJpa1, begegnungJpa2));
	}

	@Test
	public void testSpeichereMatteMissingWettkampfgruppe() {
		UUID turnierUUID = WettkaempferFixtures.turnierUUID;
		UUID runde1UUID = UUID.randomUUID();
		UUID runde2UUID = UUID.randomUUID();
		UUID id = UUID.randomUUID();
		WettkampfGruppe wkg = new WettkampfGruppe(id, "name", "typ", Altersklasse.U11, List.of(), turnierUUID);
		Begegnung.BegegnungId begegnungId1 = new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1);
		Begegnung.BegegnungId begegnungId2 = new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1);
		Begegnung begegnung1 = new Begegnung(null, begegnungId1, runde1UUID, 5, 3, 4, 4, WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2, List.of(), wkg, turnierUUID);
		Begegnung begegnung2 = new Begegnung(null, begegnungId2, runde2UUID, 5, 3, 4, 4, WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2, List.of(), wkg, turnierUUID);
		Runde runde1 = new Runde(runde1UUID, 3, 4, 4, 5, Altersklasse.U12, wkg, Arrays.asList(begegnung1));
		Runde runde2 = new Runde(runde2UUID, 3, 4, 4, 5, Altersklasse.U12, wkg, Arrays.asList(begegnung2));
		Matte matte = new Matte(5, Arrays.asList(runde1, runde2));

		WettkampfGruppeJpa wkgJpa = new WettkampfGruppeJpa(id.toString(), "name", "typ", "U11", turnierUUID.toString());
		BegegnungJpa begegnungJpa1 = new BegegnungJpa(null, runde1UUID.toString(), 5, 3, 4, 4, WettkaempferFixtures.wettkaempferJpa1, WettkaempferFixtures.wettkaempferJpa2, List.of(), wkgJpa.getUuid(), turnierUUID.toString(), 1, 1, 1);
		BegegnungJpa begegnungJpa2 = new BegegnungJpa(null, runde2UUID.toString(), 5, 3, 4, 4, WettkaempferFixtures.wettkaempferJpa1, WettkaempferFixtures.wettkaempferJpa2, List.of(), wkgJpa.getUuid(), turnierUUID.toString(), 1, 1, 1);

		when(wettkampfGruppeJpaRepository.findById(any())).thenReturn(Optional.empty(), Optional.of(wkgJpa));
		when(wettkampfGruppeJpaRepository.save(any())).thenReturn(wkgJpa);
		when(wettkampfGruppeConverter.convertToWettkampfGruppe(any())).thenReturn(wkg);
		when(begegnungConverter.convertFromBegegnung(begegnung1)).thenReturn(begegnungJpa1);
		when(begegnungConverter.convertFromBegegnung(begegnung2)).thenReturn(begegnungJpa2);

		turnierRepository.speichereMatte(matte);

		verify(begegnungJpaRepository, times(1)).saveAll(List.of(begegnungJpa1, begegnungJpa2));
		verify(wettkampfGruppeJpaRepository, times(1)).save(any());
	}

	@Test
	public void testLadeMattenRandori() {
		UUID turnierUUID = UUID.randomUUID();
		UUID rundeUUID = UUID.randomUUID();
		UUID wkgId = UUID.randomUUID();
		UUID jpa1Id = UUID.randomUUID();
		UUID jpa2Id = UUID.randomUUID();
		WettkampfGruppe wkg = new WettkampfGruppe(wkgId, "name", "typ", Altersklasse.U11, List.of(), turnierUUID);
		BegegnungJpa jpa1 = new BegegnungJpa();
		jpa1.setUuid(jpa1Id.toString());
		BegegnungJpa jpa2 = new BegegnungJpa();
		jpa2.setUuid(jpa2Id.toString());
		Begegnung.BegegnungId begegnungId = new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1);
		Begegnung begegnung1 = new Begegnung(jpa1Id, begegnungId, rundeUUID, 1, 1, 1, 1, WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2, List.of(), wkg, turnierUUID);
		Begegnung begegnung2 = new Begegnung(jpa2Id, begegnungId, rundeUUID, 1, 1, 1, 1, WettkaempferFixtures.wettkaempfer3, WettkaempferFixtures.wettkaempfer4, List.of(), wkg, turnierUUID);

		when(begegnungJpaRepository.findAllByTurnierUUID(turnierUUID.toString())).thenReturn(List.of(jpa1, jpa2));
		when(begegnungConverter.convertToBegegnung(eq(jpa1), anyList())).thenReturn(begegnung1);
		when(begegnungConverter.convertToBegegnung(eq(jpa2), anyList())).thenReturn(begegnung2);

		Map<Integer, Matte> matten = turnierRepository.ladeMatten(turnierUUID);

		assertEquals(1, matten.size());
		var matte = matten.get(1);
		assertEquals(begegnung1.getMatteId(), matte.id());
		assertEquals(begegnung2.getMatteId(), matte.id());
		assertTrue(matte.runden() != null);
		assertEquals(1, matte.runden().size());
		var runde = matte.runden().get(0);
		assertEquals(1, runde.mattenRunde());
		assertEquals(1, runde.gruppenRunde());
		assertEquals(1, runde.rundeGesamt());
		assertEquals(rundeUUID, runde.rundeId());
		assertEquals(Altersklasse.U11, runde.altersklasse());
		assertEquals(wkg, runde.gruppe());
		assertTrue(runde.begegnungen() != null);
		assertEquals(2, runde.begegnungen().size());
		var begegnungA = runde.begegnungen().get(0);
		assertEquals(matte.id(), begegnungA.getMatteId());
		assertEquals(jpa1Id, begegnungA.getId());
		assertEquals(1, begegnungA.getGruppenRunde());
		assertEquals(rundeUUID, begegnungA.getRundeId());
		assertEquals(WettkaempferFixtures.wettkaempfer1, begegnungA.getWettkaempfer1());
		assertEquals(WettkaempferFixtures.wettkaempfer2, begegnungA.getWettkaempfer2());
		assertEquals(begegnungId, begegnungA.getBegegnungId());

		var begegnungB = runde.begegnungen().get(1);
		assertEquals(matte.id(), begegnungB.getMatteId());
		assertEquals(jpa2Id, begegnungB.getId());
		assertEquals(1, begegnungB.getGruppenRunde());
		assertEquals(rundeUUID, begegnungB.getRundeId());
		assertEquals(WettkaempferFixtures.wettkaempfer3, begegnungB.getWettkaempfer1());
		assertEquals(WettkaempferFixtures.wettkaempfer4, begegnungB.getWettkaempfer2());
		assertEquals(begegnungId, begegnungB.getBegegnungId());
	}

	@Test
	public void testLadeMattenNormal() {
		UUID turnierUUID = UUID.randomUUID();
		UUID wkgId = UUID.randomUUID();
		UUID jpa1Id = UUID.randomUUID();
		UUID jpa2Id = UUID.randomUUID();
		UUID jpa3Id = UUID.randomUUID();
		WettkampfGruppe wkg = new WettkampfGruppe(wkgId, "name", "typ", Altersklasse.U11, List.of(), turnierUUID);
		BegegnungJpa jpa1 = new BegegnungJpa();
		jpa1.setUuid(jpa1Id.toString());
		BegegnungJpa jpa2 = new BegegnungJpa();
		jpa2.setUuid(jpa2Id.toString());
		BegegnungJpa jpa3 = new BegegnungJpa();
		jpa3.setUuid(jpa3Id.toString());
		Begegnung begegnung1 = new Begegnung(jpa1Id, new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1), UUID.randomUUID(), 1, 1, 1, 1, WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2, List.of(), wkg, turnierUUID);
		Begegnung begegnung2 = new Begegnung(jpa2Id, new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 2), UUID.randomUUID(), 1, 2, 1, 2, WettkaempferFixtures.wettkaempfer3, WettkaempferFixtures.wettkaempfer4, List.of(), wkg, turnierUUID);
		Begegnung begegnung3 = new Begegnung(jpa3Id, new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 3), UUID.randomUUID(), 1, 3, 1, 3, WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer4, List.of(), wkg, turnierUUID);

		when(begegnungJpaRepository.findAllByTurnierUUID(turnierUUID.toString())).thenReturn(List.of(jpa1, jpa2, jpa3));
		when(begegnungConverter.convertToBegegnung(eq(jpa1), anyList())).thenReturn(begegnung1);
		when(begegnungConverter.convertToBegegnung(eq(jpa2), anyList())).thenReturn(begegnung2);
		when(begegnungConverter.convertToBegegnung(eq(jpa3), anyList())).thenReturn(begegnung3);

		Map<Integer, Matte> matten = turnierRepository.ladeMatten(turnierUUID);

		assertEquals(1, matten.size());
		var matte = matten.get(1);
		assertTrue(matte.runden() != null);
		assertEquals(3, matte.runden().size());
		for (int i = 0; i < 3; i++) {
			var runde = matte.runden().get(i);
			assertEquals(i + 1, runde.mattenRunde());
			assertEquals(1, runde.gruppenRunde());
			assertEquals(i + 1, runde.rundeGesamt());
			assertEquals(1, runde.begegnungen().size());
			var bid = new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, i + 1);
			assertEquals(bid, runde.begegnungen().get(0).getBegegnungId());
		}
	}
}