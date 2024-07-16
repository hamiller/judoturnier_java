package de.sinnix.judoturnier.adapter.secondary;

import de.sinnix.judoturnier.fixtures.WettkaempferFixtures;
import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Begegnung;
import de.sinnix.judoturnier.model.Bewerter;
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

	private Bewerter bewerter;

	@BeforeEach
	public void setUp() {
		bewerter = new Bewerter(UUID.randomUUID().toString(), "user1", "Name, Vorname", List.of("ROLE_ZUSCHAUER"));
	}

	@Test
	void testLadeBegegnung() {
		UUID turnierUUID = UUID.randomUUID();
		WettkampfGruppe wettkampfGruppe = new WettkampfGruppe(1, "Gruppe1", "typ1", List.of(), turnierUUID);
		Bewerter bewerter = new Bewerter(UUID.randomUUID().toString(), "user1", "Name, Vorname", List.of("ROLE_ZUSCHAUER"));
		Wertung wertung = new Wertung(
			UUID.randomUUID(),
			WettkaempferFixtures.wettkaempfer1,
			Duration.of(3l, ChronoUnit.MINUTES),
			1,
			0,
			0,
			1,
			null, null, null, null, null, null, null, null,
			bewerter);
		Begegnung begegnung = new Begegnung(1, 2, 123, 22, WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2, List.of(wertung), wettkampfGruppe, turnierUUID);

		when(begegnungJpaRepository.findById(any())).thenReturn(Optional.of(new BegegnungJpa()));
		when(wettkampfGruppeJpaRepository.findAll()).thenReturn(List.of());
		when(begegnungConverter.convertToBegegnung(any(), anyList())).thenReturn(begegnung);

		Begegnung result = turnierRepository.ladeBegegnung(1);

		assertTrue(result != null);
		assertEquals(1, result.getWertungen().size());
		assertTrue(result.getWertungen().get(0).getUuid() != null);
	}

	@Test
	void testSpeichereWertung() {
		WertungJpa wertungJpa = new WertungJpa();
		Wertung wertung = new Wertung(
			null,
			WettkaempferFixtures.wettkaempfer1,
			Duration.of(3l, ChronoUnit.MINUTES),
			1, 0, 0, 1,
			null, null, null, null, null, null, null, null,
			bewerter);

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
		WettkampfGruppe wkg = new WettkampfGruppe(1, "name", "typ", List.of(), turnierUUID);
		List<Begegnung> begegnungList = new ArrayList<>();
		begegnungList.add(new Begegnung(1, 2, 3, 4, WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2, null, wkg, turnierUUID));
		List<Runde> rundenList = new ArrayList<>();
		rundenList.add(new Runde(1, 3, 4, 4, 5, Altersklasse.U11, wkg, begegnungList));
		Matte matte = new Matte(2, rundenList);
		List<Matte> mattenList = Arrays.asList(matte);

		WettkampfGruppeJpa wkgJpa = new WettkampfGruppeJpa(1, "name", "typ", turnierUUID.toString());
		List<BegegnungJpa> begegnungJpaList = new ArrayList<>();
		begegnungJpaList.add(new BegegnungJpa(null, 2, 3, 4, WettkaempferFixtures.wettkaempferJpa1, WettkaempferFixtures.wettkaempferJpa2, null, wkgJpa.getId(), turnierUUID.toString()));

		when(wettkaempferConverter.convertFromWettkaempfer(WettkaempferFixtures.wettkaempfer1)).thenReturn(WettkaempferFixtures.wettkaempferJpa1);
		when(wettkaempferConverter.convertFromWettkaempfer(WettkaempferFixtures.wettkaempfer2)).thenReturn(WettkaempferFixtures.wettkaempferJpa2);
		when(wettkampfGruppeJpaRepository.findById(any())).thenReturn(Optional.of(wkgJpa));

		turnierRepository.speichereMatten(mattenList);

		verify(begegnungJpaRepository, times(1)).saveAll(begegnungJpaList);
	}

	@Test
	public void testSpeichereMatte() {
		UUID turnierUUID = WettkaempferFixtures.turnierUUID;
		WettkampfGruppe wkg = new WettkampfGruppe(1, "name", "typ", List.of(), turnierUUID);
		Begegnung begegnung = new Begegnung(1, 5, 3, 4, WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2, null, wkg, turnierUUID);
		Runde runde = new Runde(1, 3, 4, 1, 5, Altersklasse.U12, wkg, Arrays.asList(begegnung));
		Matte matte = new Matte(5, Arrays.asList(runde));

		WettkampfGruppeJpa wkgJpa = new WettkampfGruppeJpa(1, "name", "typ", turnierUUID.toString());
		BegegnungJpa begegnungJpa = new BegegnungJpa(null, 5, 3, 4, WettkaempferFixtures.wettkaempferJpa1, WettkaempferFixtures.wettkaempferJpa2, null, wkgJpa.getId(), turnierUUID.toString());

		when(wettkaempferConverter.convertFromWettkaempfer(begegnung.getWettkaempfer1())).thenReturn(WettkaempferFixtures.wettkaempferJpa1);
		when(wettkaempferConverter.convertFromWettkaempfer(begegnung.getWettkaempfer2())).thenReturn(WettkaempferFixtures.wettkaempferJpa2);
		when(wettkampfGruppeJpaRepository.findById(any())).thenReturn(Optional.of(wkgJpa));

		turnierRepository.speichereMatte(matte);

		verify(begegnungJpaRepository, times(1)).saveAll(List.of(begegnungJpa));
	}

	@Test
	public void testLadeMatten() {
		UUID turnierUUID = UUID.randomUUID();
		WettkampfGruppe wkg = new WettkampfGruppe(1, "name", "typ", List.of(), turnierUUID);
		Wertung wertung = new Wertung(UUID.randomUUID(), null, Duration.of(3, ChronoUnit.MINUTES), null, null, null, null, 1, 2, 3, 4, 5, 6, 7, 8, bewerter);
		Begegnung begegnung = new Begegnung(2, 1, 3, 4, WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2, List.of(wertung), wkg, turnierUUID);

		when(begegnungJpaRepository.findAllByTurnierUUID(turnierUUID.toString())).thenReturn(List.of(new BegegnungJpa()));
		when(begegnungConverter.convertToBegegnung(any(BegegnungJpa.class), anyList())).thenReturn(begegnung);

		Map<Integer, Matte> matten = turnierRepository.ladeMatten(turnierUUID);

		assertEquals(1, matten.size());
		assertTrue(matten.containsKey(begegnung.getMatteId()));
		var matte = matten.get(1);
		assertEquals(begegnung.getMatteId(), matte.id());
		assertTrue(matte.runden() != null);
		assertEquals(1, matte.runden().size());
		var runde = matte.runden().get(0);
		assertEquals(matte.id(), runde.matteId());
		//		assertEquals(1, runde.rundeGesamt());
		assertEquals(3, runde.mattenRunde());
		assertEquals(4, runde.gruppenRunde());
		assertEquals(2, runde.id());
		assertEquals(Altersklasse.U11, runde.altersklasse());
		assertEquals(wkg, runde.gruppe());
		assertTrue(runde.begegnungen() != null);
		assertEquals(1, runde.begegnungen().size());
		var begegnung1 = runde.begegnungen().get(0);
		assertEquals(matte.id(), begegnung1.getMatteId());
		assertEquals(2, begegnung1.getBegegnungId());
		assertEquals(4, begegnung1.getGruppenRunde());
		assertEquals(WettkaempferFixtures.wettkaempfer1, begegnung1.getWettkaempfer1());
		assertEquals(WettkaempferFixtures.wettkaempfer2, begegnung1.getWettkaempfer2());

		//		assertTrue(matte.gruppenRunden() != null);
		//		assertEquals(1, matte.gruppenRunden().size());
		//		var gruppen = matte.gruppenRunden().get(0);
		//		assertEquals(1, gruppen);
	}
}