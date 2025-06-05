package de.sinnix.judoturnier.adapter.secondary;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;


import de.sinnix.judoturnier.adapter.secondary.converter.BegegnungConverter;
import de.sinnix.judoturnier.adapter.secondary.converter.WertungConverter;
import de.sinnix.judoturnier.adapter.secondary.converter.WettkampfGruppeConverter;
import de.sinnix.judoturnier.adapter.secondary.jpa.BegegnungJpa;
import de.sinnix.judoturnier.adapter.secondary.jpa.WertungJpa;
import de.sinnix.judoturnier.adapter.secondary.jpa.WettkampfGruppeJpa;
import de.sinnix.judoturnier.fixtures.WettkaempferFixtures;
import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Begegnung;
import de.sinnix.judoturnier.model.Benutzer;
import de.sinnix.judoturnier.model.BenutzerRolle;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
	private WertungJpaRepository   wertungJpaRepository;
	@Mock
	private WertungConverter       wertungConverter;
	@Mock
	private BegegnungJpaRepository begegnungJpaRepository;
	@Mock
	private BegegnungConverter           begegnungConverter;
	@Mock
	private WettkampfGruppeConverter     wettkampfGruppeConverter;
	@Mock
	private WettkampfGruppeJpaRepository wettkampfGruppeJpaRepository;
	@Mock
	private WettkaempferJpaRepository    wettkaempferJpaRepository;
	@InjectMocks
	private TurnierRepository            turnierRepository;

	private Benutzer benutzer;

	@BeforeEach
	public void setUp() {
		benutzer = new Benutzer(UUID.randomUUID(), "user1", "Name, Vorname", List.of(), List.of(BenutzerRolle.BEOBACHTER));
	}

	@Test
	void testLadeBegegnung() {
		UUID turnierUUID = UUID.randomUUID();
		UUID id = UUID.randomUUID();
		WettkampfGruppe wettkampfGruppe = new WettkampfGruppe(id, "Gruppe1", "typ1", Altersklasse.U11, turnierUUID);

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
		WettkampfGruppe wkg = new WettkampfGruppe(id, "name", "typ", Altersklasse.U11, turnierUUID);
		List<Begegnung> begegnungList = new ArrayList<>();
		Begegnung.BegegnungId begegnungId = new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1);
		begegnungList.add(new Begegnung(null, begegnungId, rundeUUID, 2, 3, 4, 4, WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2, List.of(), wkg, turnierUUID));
		List<Runde> rundenList = new ArrayList<>();
		rundenList.add(new Runde(null, 3, 4, 4, 5, Altersklasse.U11, wkg, begegnungList));
		Matte matte = new Matte(2, rundenList);
		List<Matte> mattenList = Arrays.asList(matte);

		WettkampfGruppeJpa wkgJpa = new WettkampfGruppeJpa("name", "typ", "U11", turnierUUID);
		wkgJpa.setId(id);
		List<BegegnungJpa> begegnungJpaList = new ArrayList<>();
		begegnungJpaList.add(new BegegnungJpa(rundeUUID.toString(), 2, 3, 4, 4, WettkaempferFixtures.wettkaempferJpa1, WettkaempferFixtures.wettkaempferJpa2, List.of(), wkgJpa.getId(), turnierUUID, 1, 1, 1));

		when(wettkampfGruppeJpaRepository.findById(any())).thenReturn(Optional.of(wkgJpa));
		when(wettkaempferJpaRepository.findById(eq(WettkaempferFixtures.wettkaempfer1.get().id()))).thenReturn(Optional.of(WettkaempferFixtures.wettkaempferJpa1));
		when(wettkaempferJpaRepository.findById(eq(WettkaempferFixtures.wettkaempfer2.get().id()))).thenReturn(Optional.of(WettkaempferFixtures.wettkaempferJpa2));

		turnierRepository.speichereMatten(mattenList);

		ArgumentCaptor<List> argumentCaptor = ArgumentCaptor.forClass(List.class);
		verify(begegnungJpaRepository, times(1)).saveAll(argumentCaptor.capture());
		verify(wettkampfGruppeJpaRepository, times(0)).save(any());

		List<BegegnungJpa> saved = argumentCaptor.getAllValues().get(0);
		assertEquals(1, saved.size());
		assertEquals(begegnungJpaList.get(0).getId(), saved.get(0).getId());
		assertEquals(begegnungJpaList.get(0).getPaarung(), saved.get(0).getPaarung());
		assertEquals(begegnungJpaList.get(0).getTurnierUUID(), saved.get(0).getTurnierUUID());
		assertEquals(begegnungJpaList.get(0).getMattenRunde(), saved.get(0).getMattenRunde());
		assertEquals(begegnungJpaList.get(0).getGruppenRunde(), saved.get(0).getGruppenRunde());
		assertEquals(begegnungJpaList.get(0).getRunde(), saved.get(0).getRunde());
		assertEquals(begegnungJpaList.get(0).getWettkampfGruppeId(), saved.get(0).getWettkampfGruppeId());
		assertEquals(begegnungJpaList.get(0).getWettkaempfer1(), saved.get(0).getWettkaempfer1());
		assertEquals(begegnungJpaList.get(0).getWettkaempfer2(), saved.get(0).getWettkaempfer2());
		assertEquals(begegnungJpaList.get(0).getWertungen(), saved.get(0).getWertungen());
		assertEquals(begegnungJpaList.get(0).getWettkampfGruppeId(), saved.get(0).getWettkampfGruppeId());
	}

	@Test
	public void testSpeichereMatteExistingWettkampfgruppe() {
		UUID turnierUUID = WettkaempferFixtures.turnierUUID;
		UUID runde1UUID = UUID.randomUUID();
		UUID runde2UUID = UUID.randomUUID();
		UUID id = UUID.randomUUID();
		WettkampfGruppe wkg = new WettkampfGruppe(id, "name", "typ", Altersklasse.U11, turnierUUID);
		Begegnung.BegegnungId begegnungId1 = new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1);
		Begegnung.BegegnungId begegnungId2 = new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1);
		Begegnung begegnung1 = new Begegnung(null, begegnungId1, runde1UUID, 5, 3, 4, 4, WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2, List.of(), wkg, turnierUUID);
		Begegnung begegnung2 = new Begegnung(null, begegnungId2, runde2UUID, 5, 3, 4, 4, WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2, List.of(), wkg, turnierUUID);
		Runde runde1 = new Runde(runde1UUID, 3, 4, 4, 5, Altersklasse.U12, wkg, Arrays.asList(begegnung1));
		Runde runde2 = new Runde(runde2UUID, 3, 4, 4, 5, Altersklasse.U12, wkg, Arrays.asList(begegnung2));
		Matte matte = new Matte(5, Arrays.asList(runde1, runde2));

		WettkampfGruppeJpa wkgJpa = new WettkampfGruppeJpa("name", "typ", "U11", turnierUUID);
		BegegnungJpa begegnungJpa1 = new BegegnungJpa(runde1UUID.toString(), 5, 3, 4, 4, WettkaempferFixtures.wettkaempferJpa1, WettkaempferFixtures.wettkaempferJpa2, List.of(), wkgJpa.getId(), turnierUUID, 1, 1, 1);
		BegegnungJpa begegnungJpa2 = new BegegnungJpa(runde2UUID.toString(), 5, 3, 4, 4, WettkaempferFixtures.wettkaempferJpa1, WettkaempferFixtures.wettkaempferJpa2, List.of(), wkgJpa.getId(), turnierUUID, 1, 1, 1);

		when(wettkampfGruppeJpaRepository.findById(any())).thenReturn(Optional.of(wkgJpa));
		when(wettkaempferJpaRepository.findById(WettkaempferFixtures.wettkaempfer1.get().id())).thenReturn(Optional.of(WettkaempferFixtures.wettkaempferJpa1));
		when(wettkaempferJpaRepository.findById(WettkaempferFixtures.wettkaempfer2.get().id())).thenReturn(Optional.of(WettkaempferFixtures.wettkaempferJpa2));

		turnierRepository.speichereMatte(matte);

		verify(begegnungJpaRepository, times(1)).saveAll(List.of(begegnungJpa1, begegnungJpa2));
	}

	@Test
	public void testSpeichereMatteMissingWettkampfgruppe() {
		UUID turnierUUID = WettkaempferFixtures.turnierUUID;
		UUID runde1UUID = UUID.randomUUID();
		UUID runde2UUID = UUID.randomUUID();
		UUID id = UUID.randomUUID();
		WettkampfGruppe wkg = new WettkampfGruppe(id, "name", "typ", Altersklasse.U11, turnierUUID);
		Begegnung.BegegnungId begegnungId1 = new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1);
		Begegnung.BegegnungId begegnungId2 = new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1);
		Begegnung begegnung1 = new Begegnung(null, begegnungId1, runde1UUID, 5, 3, 4, 4, WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2, List.of(), wkg, turnierUUID);
		Begegnung begegnung2 = new Begegnung(null, begegnungId2, runde2UUID, 5, 3, 4, 4, WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2, List.of(), wkg, turnierUUID);
		Runde runde1 = new Runde(runde1UUID, 3, 4, 4, 5, Altersklasse.U12, wkg, Arrays.asList(begegnung1));
		Runde runde2 = new Runde(runde2UUID, 3, 4, 4, 5, Altersklasse.U12, wkg, Arrays.asList(begegnung2));
		Matte matte = new Matte(5, Arrays.asList(runde1, runde2));

		WettkampfGruppeJpa wkgJpa = new WettkampfGruppeJpa("name", "typ", "U11", turnierUUID);

		when(wettkampfGruppeJpaRepository.findById(any())).thenReturn(Optional.empty(), Optional.of(wkgJpa));

		assertThrows(IllegalArgumentException.class, () -> {
			turnierRepository.speichereMatte(matte);
		});
	}

	@Test
	public void testSpeichereNeueWettkampfgruppen() {
		UUID turnierUUID = WettkaempferFixtures.turnierUUID;
		WettkampfGruppe wkg = new WettkampfGruppe(null, "name", "typ", Altersklasse.U11, turnierUUID);
		WettkampfGruppeJpa wkgJpa = new WettkampfGruppeJpa("name", "typ", "U11", turnierUUID);

		when(wettkampfGruppeConverter.convertFromWettkampfGruppe(wkg)).thenReturn(wkgJpa);

		turnierRepository.speichereGruppen(List.of(wkg));

		verify(wettkampfGruppeJpaRepository).saveAndFlush(wkgJpa);
	}

	@Test
	public void testSpeichereExistierendeWettkampfgruppen() {
		UUID turnierUUID = WettkaempferFixtures.turnierUUID;
		UUID id = UUID.randomUUID();
		WettkampfGruppe wkg = new WettkampfGruppe(id, "neuerame", "neuertyp", Altersklasse.U13, turnierUUID);
		WettkampfGruppeJpa wkgJpa = new WettkampfGruppeJpa("name", "typ", "U11", turnierUUID);
		wkgJpa.setId(id);
		WettkampfGruppeJpa expectedWkgJpa = new WettkampfGruppeJpa("neuerame", "neuertyp", "U13", turnierUUID);
		expectedWkgJpa.setId(id);

		when(wettkampfGruppeJpaRepository.findByIdAndTurnierUUID(id, turnierUUID)).thenReturn(Optional.of(wkgJpa));

		turnierRepository.speichereGruppen(List.of(wkg));

		verify(wettkampfGruppeJpaRepository).save(expectedWkgJpa);
	}

	@Test
	public void testLadeMattenRandori() {
		UUID turnierUUID = UUID.randomUUID();
		UUID rundeUUID = UUID.randomUUID();
		UUID wkgId = UUID.randomUUID();
		UUID jpa1Id = UUID.randomUUID();
		UUID jpa2Id = UUID.randomUUID();
		WettkampfGruppe wkg = new WettkampfGruppe(wkgId, "name", "typ", Altersklasse.U11, turnierUUID);
		BegegnungJpa jpa1 = new BegegnungJpa();
		jpa1.setId(jpa1Id);
		BegegnungJpa jpa2 = new BegegnungJpa();
		jpa2.setId(jpa2Id);
		Begegnung.BegegnungId begegnungId = new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1);
		Begegnung begegnung1 = new Begegnung(jpa1Id, begegnungId, rundeUUID, 1, 1, 1, 1, WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2, List.of(), wkg, turnierUUID);
		Begegnung begegnung2 = new Begegnung(jpa2Id, begegnungId, rundeUUID, 1, 1, 1, 1, WettkaempferFixtures.wettkaempfer3, WettkaempferFixtures.wettkaempfer4, List.of(), wkg, turnierUUID);

		when(begegnungJpaRepository.findAllByTurnierUUID(turnierUUID)).thenReturn(List.of(jpa1, jpa2));
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
		WettkampfGruppe wkg = new WettkampfGruppe(wkgId, "name", "typ", Altersklasse.U11, turnierUUID);
		BegegnungJpa jpa1 = new BegegnungJpa();
		jpa1.setId(jpa1Id);
		BegegnungJpa jpa2 = new BegegnungJpa();
		jpa2.setId(jpa2Id);
		BegegnungJpa jpa3 = new BegegnungJpa();
		jpa3.setId(jpa3Id);
		Begegnung begegnung1 = new Begegnung(jpa1Id, new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1), UUID.randomUUID(), 1, 1, 1, 1, WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2, List.of(), wkg, turnierUUID);
		Begegnung begegnung2 = new Begegnung(jpa2Id, new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 2), UUID.randomUUID(), 1, 2, 1, 2, WettkaempferFixtures.wettkaempfer3, WettkaempferFixtures.wettkaempfer4, List.of(), wkg, turnierUUID);
		Begegnung begegnung3 = new Begegnung(jpa3Id, new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 3), UUID.randomUUID(), 1, 3, 1, 3, WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer4, List.of(), wkg, turnierUUID);

		when(begegnungJpaRepository.findAllByTurnierUUID(turnierUUID)).thenReturn(List.of(jpa1, jpa2, jpa3));
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

	@Test
	void ladeWettkampfgruppeRunden() {
		UUID turnierUUID = UUID.randomUUID();
		UUID wkgId = UUID.randomUUID();
		UUID jpa1Id = UUID.randomUUID();
		UUID jpa2Id = UUID.randomUUID();
		UUID jpa3Id = UUID.randomUUID();
		WettkampfGruppe wkg = new WettkampfGruppe(wkgId, "name", "typ", Altersklasse.U11, turnierUUID);
		WettkampfGruppeJpa wkgJpa = new WettkampfGruppeJpa();
		wkgJpa.setId(wkgId);
		BegegnungJpa jpa1 = new BegegnungJpa();
		jpa1.setId(jpa1Id);
		jpa1.setWettkampfGruppeId(wkgId);
		BegegnungJpa jpa2 = new BegegnungJpa();
		jpa2.setId(jpa2Id);
		jpa2.setWettkampfGruppeId(wkgId);
		BegegnungJpa jpa3 = new BegegnungJpa();
		jpa3.setId(jpa3Id);
		jpa3.setWettkampfGruppeId(wkgId);
		BegegnungJpa jpa4 = new BegegnungJpa();
		jpa4.setId(UUID.randomUUID());
		jpa4.setWettkampfGruppeId(UUID.randomUUID());
		Begegnung begegnung1 = new Begegnung(jpa1Id, new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1), UUID.randomUUID(), 1, 1, 1, 1, WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2, List.of(), wkg, turnierUUID);
		Begegnung begegnung2 = new Begegnung(jpa2Id, new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 2), UUID.randomUUID(), 1, 2, 1, 2, WettkaempferFixtures.wettkaempfer3, WettkaempferFixtures.wettkaempfer4, List.of(), wkg, turnierUUID);
		Begegnung begegnung3 = new Begegnung(jpa3Id, new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 3), UUID.randomUUID(), 1, 3, 1, 3, WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer4, List.of(), wkg, turnierUUID);

		when(begegnungJpaRepository.findAllByTurnierUUID(turnierUUID)).thenReturn(List.of(jpa1, jpa2, jpa3));
		when(wettkampfGruppeJpaRepository.findByIdAndTurnierUUID(wkgId, turnierUUID)).thenReturn(Optional.of(wkgJpa));
		when(begegnungConverter.convertToBegegnung(eq(jpa1), eq(Optional.of(wkgJpa)))).thenReturn(begegnung1);
		when(begegnungConverter.convertToBegegnung(eq(jpa2), eq(Optional.of(wkgJpa)))).thenReturn(begegnung2);
		when(begegnungConverter.convertToBegegnung(eq(jpa3), eq(Optional.of(wkgJpa)))).thenReturn(begegnung3);

		List<Runde> wkgRunden = turnierRepository.ladeWettkampfgruppeRunden(wkgId, turnierUUID);

		assertEquals(3, wkgRunden.size());
		verify(begegnungConverter, times(3)).convertToBegegnung(any(), any(Optional.class));
	}

	//	@Test
	//	void testAktualisiereExistierendeRandoriWertung() {
	//		UUID turnierUUID = UUID.randomUUID();
	//		UUID rundeUUID = UUID.randomUUID();
	//		UUID id = UUID.randomUUID();
	//		List<Wertung> wertungList = new ArrayList<>();
	//		Wertung alteWertung = new Wertung(UUID.randomUUID(), null, null, null, null, null, null,
	//			1, 2, 3, 4, 4, 3, 2, 1,
	//			benutzer
	//		);
	//		wertungList.add(alteWertung);
	//		Begegnung.BegegnungId begegnungId = new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1);
	//		Begegnung begegnung = new Begegnung(id, begegnungId, rundeUUID,
	//			1, 2, 3, 3,
	//			WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2,
	//			wertungList,
	//			WettkampfgruppeFixture.gruppe1.gruppe(),
	//			turnierUUID
	//		);
	//
	//		List<Wertung> neueWertungList = new ArrayList<>();
	//		Wertung neueWertung = new Wertung(alteWertung.getUuid(), null, null, null, null, null, null,
	//			1, 2, 3, 4, 4, 3, 2, 1,
	//			benutzer
	//		);
	//		neueWertungList.add(neueWertung);
	//
	//		when(wettkampfService.ladeBegegnung(id)).thenReturn(begegnung);
	//		when(benutzerRepository.findBenutzer(benutzer.uuid())).thenReturn(Optional.of(benutzer));
	//
	//		wertungService.speichereRandoriWertung(id, 1, 2, 3, 4, 4, 3, 2, 1, benutzer.uuid());
	//
	//		// Erstelle einen Captor für jeden Parametertyp
	//		ArgumentCaptor<Wertung> wertungCaptor = ArgumentCaptor.forClass(Wertung.class);
	//		ArgumentCaptor<UUID> begegnungIdCaptor = ArgumentCaptor.forClass(UUID.class);
	//
	//		// Bei der Verifikation beide Captors verwenden
	//		verify(turnierRepository).speichereWertungInBegegnung(wertungCaptor.capture(), begegnungIdCaptor.capture());
	//
	//		// Zugriff auf die erfassten Werte
	//		Wertung erfassteWertung = wertungCaptor.getValue();
	//		UUID erfassteBegegnungId = begegnungIdCaptor.getValue();
	//		assertEquals(neueWertungList, result.getWertungen());
	//	}
	//
	//	@Test
	//	void testWeitereRandoriWertung() {
	//		Benutzer benutzerA = new Benutzer(UUID.randomUUID(), "username", "name", List.of(), List.of(BenutzerRolle.KAMPFRICHTER));
	//		Benutzer benutzerB = new Benutzer(UUID.randomUUID(), "username", "name", List.of(), List.of(BenutzerRolle.KAMPFRICHTER));
	//		UUID turnierUUID = UUID.randomUUID();
	//		UUID rundeUUID = UUID.randomUUID();
	//		UUID id = UUID.randomUUID();
	//		List<Wertung> wertungList = new ArrayList<>();
	//		Wertung alteWertung = new Wertung(UUID.randomUUID(), null, null, null, null, null, null,
	//			1, 2, 3, 4, 4, 3, 2, 1,
	//			benutzerA
	//		);
	//		wertungList.add(alteWertung);
	//		Begegnung.BegegnungId begegnungId = new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1);
	//		Begegnung begegnung = new Begegnung(id,
	//			begegnungId,
	//			rundeUUID,
	//			1, 2, 3, 3,
	//			WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2,
	//			wertungList,
	//			WettkampfgruppeFixture.gruppe1.gruppe(),
	//			turnierUUID
	//		);
	//
	//		List<Wertung> neueWertungList = new ArrayList<>();
	//		Wertung neueWertung = new Wertung(null, null, null, null, null, null, null,
	//			1, 2, 3, 4, 4, 3, 2, 1,
	//			benutzerB
	//		);
	//		neueWertungList.add(alteWertung);
	//		neueWertungList.add(neueWertung);
	//
	//		when(wettkampfService.ladeBegegnung(id)).thenReturn(begegnung);
	//		when(benutzerRepository.findBenutzer(benutzerB.uuid())).thenReturn(Optional.of(benutzerB));
	//
	//		wertungService.speichereRandoriWertung(id, 1, 2, 3, 4, 4, 3, 2, 1, benutzerB.uuid());
	//
	//		ArgumentCaptor<Begegnung> argumentCaptor = ArgumentCaptor.forClass(Begegnung.class);
	//		verify(turnierRepository).speichereWertungInBegegnung(argumentCaptor.capture());
	//		Begegnung result = argumentCaptor.getValue();
	//		assertEquals(2, result.getWertungen().size());
	//		assertEquals(benutzerA, result.getWertungen().getFirst().getBewerter());
	//		assertEquals(benutzerB, result.getWertungen().get(1).getBewerter());
	//	}
	//
	//	@Test
	//	void testSpeichereNeueRandoriWertung() {
	//		UUID turnierUUID = UUID.randomUUID();
	//		UUID rundeUUID = UUID.randomUUID();
	//		UUID id = UUID.randomUUID();
	//		Begegnung.BegegnungId begegnungId = new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1);
	//		Begegnung begegnung = new Begegnung(id, begegnungId, rundeUUID,
	//			1, 2, 3, 3,
	//			WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2,
	//			new ArrayList<>(),
	//			WettkampfgruppeFixture.gruppe1.gruppe(),
	//			turnierUUID
	//		);
	//
	//		when(wettkampfService.ladeBegegnung(id)).thenReturn(begegnung);
	//		when(benutzerRepository.findBenutzer(benutzer.uuid())).thenReturn(Optional.of(benutzer));
	//
	//		wertungService.speichereRandoriWertung(id, 1, 2, 3, 4, 4, 3, 2, 1, benutzer.uuid());
	//
	//		ArgumentCaptor<Begegnung> argumentCaptor = ArgumentCaptor.forClass(Begegnung.class);
	//		verify(turnierRepository).speichereWertungInBegegnung(argumentCaptor.capture());
	//		Begegnung result = argumentCaptor.getValue();
	//		assertEquals(1, result.getWertungen().size());
	//		assertEquals(benutzer, result.getWertungen().getFirst().getBewerter());
	//		assertEquals(1, result.getWertungen().getFirst().getKampfgeistWettkaempfer1());
	//		assertEquals(2, result.getWertungen().getFirst().getTechnikWettkaempfer1());
	//		assertEquals(3, result.getWertungen().getFirst().getKampfstilWettkaempfer1());
	//		assertEquals(4, result.getWertungen().getFirst().getVielfaltWettkaempfer1());
	//		assertEquals(4, result.getWertungen().getFirst().getKampfgeistWettkaempfer2());
	//		assertEquals(3, result.getWertungen().getFirst().getTechnikWettkaempfer2());
	//		assertEquals(2, result.getWertungen().getFirst().getKampfstilWettkaempfer2());
	//		assertEquals(1, result.getWertungen().getFirst().getVielfaltWettkaempfer2());
	//	}
	//
	//	@Test
	//	void speichereTurnierWertungErsterKampf() {
	//		int scoreWeiss = 1;
	//		int scoreBlau = 0;
	//		int penaltiesWeiss = 0;
	//		int penaltiesBlau = 0;
	//		String fighttime = "02:03.54"; // entspricht 123.540ms
	//		UUID siegerUuid = WettkaempferFixtures.wettkaempfer1.get().id();
	//
	//		int aktuelleMattenRunde = 2;
	//		int aktuellePaarung = 1;
	//		int aktuelleGruppenRunde = 1;
	//		int aktuelleGesamtRunde = 2;
	//		Begegnung.BegegnungId begegnungId = new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, aktuelleMattenRunde, aktuellePaarung);
	//		Begegnung aktuelleBegegnung = new Begegnung(UUID.randomUUID(), begegnungId, UUID.randomUUID(),
	//			1, aktuelleMattenRunde, aktuelleGruppenRunde, aktuelleGesamtRunde,
	//			WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2,
	//			new ArrayList<>(),
	//			WettkampfgruppeFixture.gruppe1.gruppe(),
	//			turnierUUID
	//		);
	//
	//		// Teil 1
	//		when(wettkampfService.ladeBegegnung(any())).thenReturn(aktuelleBegegnung);
	//		when(benutzerRepository.findBenutzer(eq(benutzer.uuid()))).thenReturn(Optional.of(benutzer));
	//		when(wettkaempferService.ladeKaempfer(eq(siegerUuid))).thenReturn(WettkaempferFixtures.wettkaempfer1);
	//		when(turnierRepository.ladeWettkampfgruppeRunden(eq(WettkampfgruppeFixture.gruppe1.gruppe().id()), eq(turnierUUID))).thenReturn(List.of());
	//
	//		wertungService.speichereTurnierWertung(aktuelleBegegnung.getId(), scoreWeiss, scoreBlau, penaltiesWeiss, penaltiesBlau, fighttime, siegerUuid, benutzer.uuid());
	//
	//
	//		ArgumentCaptor<Begegnung> argumentCaptor = ArgumentCaptor.forClass(Begegnung.class);
	//		verify(turnierRepository, times(1)).speichereWertungInBegegnung(argumentCaptor.capture());
	//
	//		Begegnung gespeichert = argumentCaptor.getAllValues().getFirst();
	//		assertEquals(begegnungId, gespeichert.getBegegnungId());
	//		assertEquals(1, gespeichert.getWertungen().size());
	//		assertEquals(siegerUuid, gespeichert.getWertungen().getFirst().getSieger().id());
	//		assertEquals(scoreWeiss, gespeichert.getWertungen().getFirst().getPunkteWettkaempferWeiss());
	//		assertEquals(scoreBlau, gespeichert.getWertungen().getFirst().getPunkteWettkaempferRot());
	//		assertEquals(penaltiesWeiss, gespeichert.getWertungen().getFirst().getStrafenWettkaempferWeiss());
	//		assertEquals(penaltiesBlau, gespeichert.getWertungen().getFirst().getStrafenWettkaempferRot());
	//		assertEquals(Duration.ofMillis(123540), gespeichert.getWertungen().getFirst().getZeit());
	//	}
	//
	//	@Test
	//	void speichereTurnierWertungZweiterKampf() {
	//		int scoreWeiss = 1;
	//		int scoreBlau = 0;
	//		int penaltiesWeiss = 0;
	//		int penaltiesBlau = 0;
	//		String fighttime = "02:03.54"; // entspricht 123.540ms
	//		UUID siegerUuid = WettkaempferFixtures.wettkaempferin2.id();
	//		var aktuelleBegegnungUUID = WettkampfgruppeFixture.b2UUID;
	//		var aktuelleBegegnungId = new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 2);
	//		var naechsteBegegnungUUID = WettkampfgruppeFixture.b7UUID;
	//		var naechsteBegegnungId = new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 2, 1);
	//
	//		WettkampfGruppe wkg = WettkampfgruppeFixture.wettkampfGruppeFrauen.gruppe();
	//		List<Begegnung> begegnungListRunde1 = new ArrayList<>();
	//		begegnungListRunde1.add(new Begegnung(WettkampfgruppeFixture.b1UUID, new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1), null, null, null, null, null,
	//			Optional.of(WettkaempferFixtures.wettkaempferin1),
	//			Optional.empty(),
	//			null, wkg, turnierUUID));
	//		begegnungListRunde1.add(new Begegnung(aktuelleBegegnungUUID, aktuelleBegegnungId, null, null, null, null, null,
	//			Optional.of(WettkaempferFixtures.wettkaempferin2),
	//			Optional.of(WettkaempferFixtures.wettkaempferin3),
	//			new ArrayList<>(), wkg, turnierUUID));
	//		begegnungListRunde1.add(new Begegnung(WettkampfgruppeFixture.b3UUID, new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 3), null, null, null, null, null,
	//			Optional.of(WettkaempferFixtures.wettkaempferin4),
	//			Optional.of(WettkaempferFixtures.wettkaempferin5),
	//			null, wkg, turnierUUID));
	//		begegnungListRunde1.add(new Begegnung(WettkampfgruppeFixture.b4UUID, new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 4), null, null, null, null, null,
	//			Optional.of(WettkaempferFixtures.wettkaempferin6),
	//			Optional.of(WettkaempferFixtures.wettkaempferin7),
	//			null, wkg, turnierUUID));
	//		begegnungListRunde1.add(new Begegnung(WettkampfgruppeFixture.b5UUID, new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 1, 1), null, null, null, null, null,
	//			Optional.empty(),
	//			Optional.empty(),
	//			null, wkg, turnierUUID));
	//		begegnungListRunde1.add(new Begegnung(WettkampfgruppeFixture.b6UUID, new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 1, 2), null, null, null, null, null,
	//			Optional.empty(),
	//			Optional.empty(),
	//			null, wkg, turnierUUID));
	//		List<Begegnung> begegnungListRunde2 = new ArrayList<>();
	//		begegnungListRunde2.add(new Begegnung(WettkampfgruppeFixture.b7UUID, new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 2, 1), null, null, null, null, null,
	//			Optional.empty(),
	//			Optional.empty(),
	//			null, wkg, turnierUUID));
	//		begegnungListRunde2.add(new Begegnung(naechsteBegegnungUUID, naechsteBegegnungId, null, null, null, null, null,
	//			Optional.empty(),
	//			Optional.empty(),
	//			null, wkg, turnierUUID));
	//		begegnungListRunde2.add(new Begegnung(WettkampfgruppeFixture.b9UUID, new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 2, 1), null, null, null, null, null,
	//			Optional.empty(),
	//			Optional.empty(),
	//			null, wkg, turnierUUID));
	//		begegnungListRunde2.add(new Begegnung(WettkampfgruppeFixture.b10UUID, new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 2, 2), null, null, null, null, null,
	//			Optional.empty(),
	//			Optional.empty(),
	//			null, wkg, turnierUUID));
	//		List<Begegnung> begegnungListRunde3 = new ArrayList<>();
	//		begegnungListRunde3.add(new Begegnung(WettkampfgruppeFixture.b11UUID, new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 3, 1), null, null, null, null, null,
	//			Optional.empty(),
	//			Optional.empty(),
	//			null, wkg, turnierUUID));
	//
	//		List<Runde> rundenList = new ArrayList<>();
	//		UUID rundeUUID1 = UUID.randomUUID();
	//		UUID rundeUUID2 = UUID.randomUUID();
	//		UUID rundeUUID3 = UUID.randomUUID();
	//		rundenList.add(new Runde(rundeUUID1, 1, 1, 1, 1, Altersklasse.Frauen, WettkampfgruppeFixture.wettkampfGruppeFrauen.gruppe(), begegnungListRunde1));
	//		rundenList.add(new Runde(rundeUUID2, 2, 2, 2, 1, Altersklasse.Frauen, WettkampfgruppeFixture.wettkampfGruppeFrauen.gruppe(), begegnungListRunde2));
	//		rundenList.add(new Runde(rundeUUID3, 3, 3, 3, 1, Altersklasse.Frauen, WettkampfgruppeFixture.wettkampfGruppeFrauen.gruppe(), begegnungListRunde3));
	//
	//		// Teil 1
	//		when(wettkampfService.ladeBegegnung(any())).thenReturn(begegnungListRunde1.get(1));
	//		when(benutzerRepository.findBenutzer(eq(benutzer.uuid()))).thenReturn(Optional.of(benutzer));
	//		when(wettkaempferService.ladeKaempfer(eq(siegerUuid))).thenReturn(Optional.ofNullable(WettkaempferFixtures.wettkaempferin2));
	//
	//		// Teil 2
	//		when(turnierRepository.ladeWettkampfgruppeRunden(wkg.id(), turnierUUID)).thenReturn(rundenList);
	//
	//
	//		wertungService.speichereTurnierWertung(aktuelleBegegnungUUID, scoreWeiss, scoreBlau, penaltiesWeiss, penaltiesBlau, fighttime, siegerUuid, benutzer.uuid());
	//
	//
	//		// Verify Teil 1
	//		ArgumentCaptor<Begegnung> argumentCaptor = ArgumentCaptor.forClass(Begegnung.class);
	//		verify(turnierRepository, times(3)).speichereWertungInBegegnung(argumentCaptor.capture());
	//
	//		Begegnung gespeichert = argumentCaptor.getAllValues().getFirst();
	//		assertEquals(aktuelleBegegnungId, gespeichert.getBegegnungId());
	//		assertEquals(aktuelleBegegnungUUID, gespeichert.getId());
	//		assertEquals(1, gespeichert.getWertungen().size());
	//		assertEquals(siegerUuid, gespeichert.getWertungen().getFirst().getSieger().id());
	//		assertEquals(scoreWeiss, gespeichert.getWertungen().getFirst().getPunkteWettkaempferWeiss());
	//		assertEquals(scoreBlau, gespeichert.getWertungen().getFirst().getPunkteWettkaempferRot());
	//		assertEquals(penaltiesWeiss, gespeichert.getWertungen().getFirst().getStrafenWettkaempferWeiss());
	//		assertEquals(penaltiesBlau, gespeichert.getWertungen().getFirst().getStrafenWettkaempferRot());
	//		assertEquals(Duration.ofMillis(123540), gespeichert.getWertungen().getFirst().getZeit());
	//
	//		// Verify Teil 2
	//		Begegnung gespeichertNext = argumentCaptor.getAllValues().get(1);
	//		assertEquals(naechsteBegegnungId, gespeichertNext.getBegegnungId());
	//		assertEquals(naechsteBegegnungUUID, gespeichertNext.getId());
	//	}
	//
	//	@Test
	//	@Disabled
	//	public void testBerechnePlatzierungen() {
	//		/**
	//		 * SIEGERRUNDE
	//		 . RUNDE 1                                    RUNDE 2
	//		 . "Fox, Sweetie" vs "Reid, Riley" ------
	//		 .                                       |___ "Fox, Sweetie" vs "Jameson, Jenna"  (---> Sieger "Fox, Sweetie")
	//		 .                                       |
	//		 . "Jameson, Jenna" vs "Belle, Lexi" ----
	//		 .
	//		 .TROSTRUNDE
	//		 |
	//		 .                                        --- "Reid, Riley" vs "Belle, Lexi"  (---> Sieger "Belle, Lexi")
	//		 .
	//		 */
	//		Benutzer bewerter = new Benutzer(UUID.randomUUID(), "kr1", "Kampfrichter 1", List.of(), List.of(BenutzerRolle.KAMPFRICHTER));
	//		Wertung wertung1 = new Wertung(UUID.randomUUID(), WettkaempferFixtures.wettkaempferin1, Duration.ofMinutes(2), 1, 0, 0, 0, null, null, null, null, null, null, null, null, bewerter);
	//		Wertung wertung2 = new Wertung(UUID.randomUUID(), WettkaempferFixtures.wettkaempferin3, Duration.ofMinutes(2), 0, 0, 1, 0, null, null, null, null, null, null, null, null, bewerter);
	//		Wertung wertung3 = new Wertung(UUID.randomUUID(), WettkaempferFixtures.wettkaempferin1, Duration.ofMinutes(2), 1, 0, 0, 0, null, null, null, null, null, null, null, null, bewerter);
	//		Wertung wertung4 = new Wertung(UUID.randomUUID(), WettkaempferFixtures.wettkaempferin4, Duration.ofMinutes(2), 1, 0, 0, 0, null, null, null, null, null, null, null, null, bewerter);
	//
	//		List<Begegnung> begegnungList = List.of(
	//			new Begegnung(UUID.randomUUID(), new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1), UUID.randomUUID(), 1, 1, 1, 1, Optional.of(WettkaempferFixtures.wettkaempferin1), Optional.of(WettkaempferFixtures.wettkaempferin2), List.of(wertung1), WettkampfgruppeFixture.gruppe1.gruppe(), turnierUUID),
	//			new Begegnung(UUID.randomUUID(), new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 2), UUID.randomUUID(), 1, 2, 2, 2, Optional.of(WettkaempferFixtures.wettkaempferin3), Optional.of(WettkaempferFixtures.wettkaempferin4), List.of(wertung2), WettkampfgruppeFixture.gruppe1.gruppe(), turnierUUID),
	//			new Begegnung(UUID.randomUUID(), new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 2, 3), UUID.randomUUID(), 1, 3, 3, 3, Optional.of(WettkaempferFixtures.wettkaempferin1), Optional.of(WettkaempferFixtures.wettkaempferin3), List.of(wertung3), WettkampfgruppeFixture.gruppe1.gruppe(), turnierUUID),
	//			new Begegnung(UUID.randomUUID(), new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 2, 4), UUID.randomUUID(), 1, 4, 4, 4, Optional.of(WettkaempferFixtures.wettkaempferin4), Optional.of(WettkaempferFixtures.wettkaempferin2), List.of(wertung4), WettkampfgruppeFixture.gruppe1.gruppe(), turnierUUID)
	//		);
	//		when(turnierRepository.ladeAlleBegegnungen(any())).thenReturn(begegnungList);
	//
	//		var results = wertungService.berechnePlatzierungen(turnierUUID);
	//
	//		logger.info("1 {}", WettkaempferFixtures.wettkaempferin1.name());
	//		logger.info("2 {}", WettkaempferFixtures.wettkaempferin2.name());
	//		logger.info("3 {}", WettkaempferFixtures.wettkaempferin3.name());
	//		logger.info("4 {}", WettkaempferFixtures.wettkaempferin4.name());
	//		for (Map.Entry<Wettkaempfer, Integer> result : results.entrySet()) {
	//			logger.info("Result {} - Siege: {}x", result.getKey().name(), result.getValue());
	//		}
	//
	//		assertEquals(results.get(WettkaempferFixtures.wettkaempferin1), 1); // "Fox, Sweetie", Erster Platz
	//		assertEquals(results.get(WettkaempferFixtures.wettkaempferin2), 4); // "Reid, Riley", Letzter Platz
	//		assertEquals(results.get(WettkaempferFixtures.wettkaempferin3), 2); // "Jameson, Jenna", Zweiter Platz
	//		assertEquals(results.get(WettkaempferFixtures.wettkaempferin4), 3); // "Belle, Lexi", Dritter Platz
	//	}
}
