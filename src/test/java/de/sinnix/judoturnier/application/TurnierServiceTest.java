package de.sinnix.judoturnier.application;

import de.sinnix.judoturnier.adapter.secondary.TurnierRepository;
import de.sinnix.judoturnier.fixtures.MatteFixtures;
import de.sinnix.judoturnier.fixtures.WettkaempferFixtures;
import de.sinnix.judoturnier.fixtures.WettkampfgruppeFixture;
import de.sinnix.judoturnier.model.Begegnung;
import de.sinnix.judoturnier.model.Benutzer;
import de.sinnix.judoturnier.model.BenutzerRolle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TurnierServiceTest {

	private static final Logger logger = LogManager.getLogger(TurnierServiceTest.class);

	@Mock
	private TurnierRepository      turnierRepository;

	@InjectMocks
	private TurnierService turnierService;

	private final UUID     turnierUUID = MatteFixtures.turnierUUID;

	@BeforeEach
	void setUp() {
	}

	@Test
	void testLadeMetadaten() {
		UUID begegnungId = MatteFixtures.b2;

		when(turnierRepository.ladeMatten(turnierUUID)).thenReturn(MatteFixtures.matteList);

		var metadaten = turnierService.ladeMetadaten(begegnungId, turnierUUID);

		assertNotNull(metadaten);
		assertEquals(3, metadaten.alleRundenBegegnungIds().size());
		assertEquals(MatteFixtures.b1, metadaten.vorherigeBegegnungId().get());
		assertEquals(MatteFixtures.b3, metadaten.nachfolgendeBegegnungId().get());
	}

	@Test
	void testLadeMetadatenLetzteBegegnung() {
		UUID begegnungId = MatteFixtures.b15;

		when(turnierRepository.ladeMatten(turnierUUID)).thenReturn(MatteFixtures.matteList);

		var metadaten = turnierService.ladeMetadaten(begegnungId, turnierUUID);

		assertNotNull(metadaten);
		assertEquals(3, metadaten.alleRundenBegegnungIds().size());
		assertEquals(MatteFixtures.b14, metadaten.vorherigeBegegnungId().get());
		assertEquals(Optional.empty(), metadaten.nachfolgendeBegegnungId());
	}

	@Test
	void testLadeMetadatenErsteBegegnung() {
		UUID begegnungId = MatteFixtures.b1;

		when(turnierRepository.ladeMatten(turnierUUID)).thenReturn(MatteFixtures.matteList);

		var metadaten = turnierService.ladeMetadaten(begegnungId, turnierUUID);

		assertNotNull(metadaten);
		assertEquals(3, metadaten.alleRundenBegegnungIds().size());
		assertEquals(Optional.empty(), metadaten.vorherigeBegegnungId());
		assertEquals(MatteFixtures.b2, metadaten.nachfolgendeBegegnungId().get());
	}

	@Test
	void ladeMattenRunde() {

		when(turnierRepository.ladeAlleBegegnungen(any())).thenReturn(List.of(
			new Begegnung(UUID.randomUUID(), new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1), UUID.randomUUID(), 1, 1, 1, 1, WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2, List.of(), WettkampfgruppeFixture.gruppe1.gruppe(), turnierUUID),
			new Begegnung(UUID.randomUUID(), new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1), UUID.randomUUID(), 2, 1, 1, 1, WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2, List.of(), WettkampfgruppeFixture.gruppe1.gruppe(), turnierUUID),
			new Begegnung(UUID.randomUUID(), new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1), UUID.randomUUID(), 1, 2, 1, 2, WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2, List.of(), WettkampfgruppeFixture.gruppe1.gruppe(), turnierUUID),
			new Begegnung(UUID.randomUUID(), new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1), UUID.randomUUID(), 1, 2, 1, 2, WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2, List.of(), WettkampfgruppeFixture.gruppe1.gruppe(), turnierUUID)
		));

		List<Begegnung> begegnungList = turnierService.ladeMattenRunde(turnierUUID, 1, 2);

		assertEquals(2, begegnungList.size());
	}
}
