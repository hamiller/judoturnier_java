package de.sinnix.judoturnier.adapter.secondary;

import de.sinnix.judoturnier.adapter.secondary.converter.BenutzerConverter;
import de.sinnix.judoturnier.adapter.secondary.converter.TurnierConverter;
import de.sinnix.judoturnier.adapter.secondary.jpa.BenutzerJpa;
import de.sinnix.judoturnier.adapter.secondary.jpa.TurnierJpa;
import de.sinnix.judoturnier.adapter.secondary.jpa.TurnierRollenJpa;
import de.sinnix.judoturnier.model.Benutzer;
import de.sinnix.judoturnier.model.BenutzerRolle;
import de.sinnix.judoturnier.model.TurnierRollen;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class BenutzerConverterTest {
	@Mock
	private TurnierConverter  turnierConverter;
	@InjectMocks
	private BenutzerConverter converter;

	private Date date;

	@BeforeEach
	void setUp() {
		date = new java.sql.Date(System.currentTimeMillis());
	}

	@Test
	void convertToBenutzer() {
		UUID uuid = UUID.randomUUID();
		BenutzerJpa jpa = new BenutzerJpa();
		List<TurnierJpa> turniereJpa = List.of(
			new TurnierJpa("t1", "o1", date),
			new TurnierJpa("t2", "o2", date));
		turniereJpa.get(0).setId(UUID.randomUUID());
		turniereJpa.get(1).setId(UUID.randomUUID());
		List<TurnierRollenJpa> turnierRollenJpa = List.of(
			new TurnierRollenJpa(jpa, List.of(BenutzerRolle.BEOBACHTER), turniereJpa.get(0).getId()),
			new TurnierRollenJpa(jpa, List.of(BenutzerRolle.KAMPFRICHTER), turniereJpa.get(1).getId()));
		turnierRollenJpa.get(0).setId(UUID.randomUUID());
		turnierRollenJpa.get(1).setId(UUID.randomUUID());
		jpa.setId(uuid);
		jpa.setUsername("username");
		jpa.setName("name");
		jpa.setTurnierRollen(turnierRollenJpa);
		jpa.setRollen(List.of(BenutzerRolle.ADMINISTRATOR));


		Benutzer result = converter.convertToBenutzer(jpa);

		assertNotNull(result);
		assertEquals(result.uuid(), jpa.getId());
		assertEquals(result.username(), jpa.getUsername());
		assertEquals(result.name(), jpa.getName());
		assertTrue(result.istAdmin());
		assertTrue(result.istKampfrichter(turniereJpa.get(1).getId()));
		assertFalse(result.istKampfrichter(turniereJpa.get(0).getId()));
		assertEquals(1, result.benutzerRollen().size());
		assertTrue(result.benutzerRollen().containsAll(List.of(BenutzerRolle.ADMINISTRATOR)));
		assertEquals(2, result.turnierRollen().size());
		assertEquals(turniereJpa.get(0).getId(), result.turnierRollen().get(0).turnierId());
		assertEquals(1, result.turnierRollen().get(0).rollen().size());
		assertEquals(BenutzerRolle.BEOBACHTER, result.turnierRollen().get(0).rollen().get(0));
		assertEquals(turniereJpa.get(1).getId(), result.turnierRollen().get(1).turnierId());
		assertEquals(1, result.turnierRollen().get(1).rollen().size());
		assertEquals(BenutzerRolle.KAMPFRICHTER, result.turnierRollen().get(1).rollen().get(0));
	}

	@Test
	void convertFromBenutzer() {
		UUID turnierId1 = UUID.randomUUID();
		UUID turnierId2 = UUID.randomUUID();
		List<TurnierRollen> turnierRollen = List.of(
			new TurnierRollen(null, turnierId1, List.of(BenutzerRolle.BEOBACHTER)),
			new TurnierRollen(null, turnierId2, List.of(BenutzerRolle.KAMPFRICHTER)));

		Benutzer benutzer = new Benutzer(
			null, "username", "name", turnierRollen, List.of(BenutzerRolle.ADMINISTRATOR)
		);

		BenutzerJpa result = converter.convertFromBenutzer(benutzer);

		assertNotNull(result);
		assertNull(result.getId());
		assertEquals(benutzer.name(), result.getName());
		assertEquals(benutzer.username(), result.getUsername());
		assertEquals(1, result.getRollen().size());
		assertEquals(benutzer.benutzerRollen().get(0), result.getRollen().get(0));
		assertEquals(2, result.getTurnierRollen().size());

		assertEquals(1, result.getTurnierRollen().get(0).getRollen().size());
		assertEquals(benutzer.turnierRollen().get(0).rollen().get(0), result.getTurnierRollen().get(0).getRollen().get(0));
		assertEquals(turnierId1, result.getTurnierRollen().get(0).getTurnierUuid());
		assertEquals(benutzer.uuid(), result.getTurnierRollen().get(0).getBenutzer().getId());

		assertEquals(1, result.getTurnierRollen().get(1).getRollen().size());
		assertEquals(benutzer.turnierRollen().get(1).rollen().get(0), result.getTurnierRollen().get(1).getRollen().get(0));
		assertEquals(turnierId2, result.getTurnierRollen().get(1).getTurnierUuid());
		assertEquals(benutzer.uuid(), result.getTurnierRollen().get(1).getBenutzer().getId());


	}
}
