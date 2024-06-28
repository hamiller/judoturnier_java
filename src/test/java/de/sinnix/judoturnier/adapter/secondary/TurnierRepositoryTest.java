package de.sinnix.judoturnier.adapter.secondary;

import de.sinnix.judoturnier.fixtures.WettkaempferFixtures;
import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Begegnung;
import de.sinnix.judoturnier.model.Farbe;
import de.sinnix.judoturnier.model.Geschlecht;
import de.sinnix.judoturnier.model.Matte;
import de.sinnix.judoturnier.model.Runde;
import de.sinnix.judoturnier.model.Verein;
import de.sinnix.judoturnier.model.Wertung;
import de.sinnix.judoturnier.model.Wettkaempfer;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
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
	private BegegnungConverter     begegnungConverter;
	@Mock
	private WettkaempferConverter  wettkaempferConverter;
	@InjectMocks
	private TurnierRepository      turnierRepository;

	@BeforeEach
	public void setUp() {
	}

	@Test
	void ladeWertung() {
		String wettkampfId = "1231231";
		WertungJpa wertungJpa = new WertungJpa();
		Wertung wertung = new Wertung(
			"uuid",
			WettkaempferFixtures.wettkaempfer1,
			Duration.of(3l, ChronoUnit.MINUTES),
			1,
			0,
			0,
			1,
			null, null, null, null, null, null, null, null);


		when(wertungJpaRepository.findById(wettkampfId)).thenReturn(Optional.of(wertungJpa));
		when(wertungConverter.convertToWertung(any(WertungJpa.class))).thenReturn(wertung);

		var result = turnierRepository.ladeWertung(wettkampfId);

		assertTrue(result != null);
		assertTrue(result.isPresent());
		assertTrue(result.get().uuid() != null);
	}

	@Test
	void speichereWertung() {
		WertungJpa wertungJpa = new WertungJpa();
		Wertung wertung = new Wertung(
			null,
			WettkaempferFixtures.wettkaempfer1,
			Duration.of(3l, ChronoUnit.MINUTES),
			1, 0, 0, 1,
			null, null, null, null, null, null, null, null);

		when(wertungConverter.convertFromWertung(any(Wertung.class))).thenReturn(wertungJpa);

		turnierRepository.speichereWertung(wertung);

		ArgumentCaptor<WertungJpa> argumentCaptor = ArgumentCaptor.forClass(WertungJpa.class);
		verify(wertungJpaRepository, times(1)).save(argumentCaptor.capture());
		WertungJpa jpa = argumentCaptor.getValue();
		assertTrue(jpa != null);
	}

	@Test
	public void testSpeichereMatten() {
		List<Begegnung> begegnungList = new ArrayList<>();
		begegnungList.add(new Begegnung(1, 2, 3, 4, WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2, null));
		List<Runde> rundenList = new ArrayList<>();
		rundenList.add(new Runde(1, 3, 4,4, 5, Altersklasse.U11, null, begegnungList));
		Matte matte = new Matte(2, rundenList, new ArrayList<>());
		List<Matte> mattenList = Arrays.asList(matte);

		List<BegegnungJpa> begegnungJpaList = new ArrayList<>();;
		begegnungJpaList.add(new BegegnungJpa(null, 2, 3, 4, WettkaempferFixtures.wettkaempferJpa1, WettkaempferFixtures.wettkaempferJpa2, null));

		when(wettkaempferConverter.convertFromWettkaempfer(WettkaempferFixtures.wettkaempfer1)).thenReturn(WettkaempferFixtures.wettkaempferJpa1);
		when(wettkaempferConverter.convertFromWettkaempfer(WettkaempferFixtures.wettkaempfer2)).thenReturn(WettkaempferFixtures.wettkaempferJpa2);

		turnierRepository.speichereMatten(mattenList);

		verify(begegnungJpaRepository, times(1)).saveAll(begegnungJpaList);
	}

	@Test
	public void testSpeichereMatte() {
		Begegnung begegnung = new Begegnung(1, 5, 2, 3, WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2, null);
		Runde runde = new Runde(1, 1, 1, 1, 1, Altersklasse.U12, null, Arrays.asList(begegnung));
		Matte matte = new Matte(1, Arrays.asList(runde), new ArrayList<>());

		when(wettkaempferConverter.convertFromWettkaempfer(begegnung.getWettkaempfer1())).thenReturn(new WettkaempferJpa());
		when(wettkaempferConverter.convertFromWettkaempfer(begegnung.getWettkaempfer2())).thenReturn(new WettkaempferJpa());

		turnierRepository.speichereMatte(matte);

		verify(begegnungJpaRepository, times(1)).saveAll(anyList());
	}
}