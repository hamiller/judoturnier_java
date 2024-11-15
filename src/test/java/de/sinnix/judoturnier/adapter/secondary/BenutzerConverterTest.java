package de.sinnix.judoturnier.adapter.secondary;

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
	private TurnierConverter turnierConverter;
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
		List<TurnierJpa> turniereJpa = List.of(
			new TurnierJpa(UUID.randomUUID().toString(), "t1", "o1", date),
			new TurnierJpa(UUID.randomUUID().toString(), "t2", "o2", date));
		List<TurnierRollenJpa> turnierRollenJpa = List.of(
			new TurnierRollenJpa(new TurnierRollenJpa.TurnierRollenId(uuid.toString(), turniereJpa.get(0).getUuid().toString()), List.of(BenutzerRolle.BEOBACHTER)),
			new TurnierRollenJpa(new TurnierRollenJpa.TurnierRollenId(uuid.toString(), turniereJpa.get(1).getUuid().toString()), List.of(BenutzerRolle.KAMPFRICHTER)));
		BenutzerJpa jpa = new BenutzerJpa();
		jpa.setUuid(uuid.toString());
		jpa.setUsername("username");
		jpa.setName("name");
		jpa.setTurnierRollen(turnierRollenJpa);
		jpa.setRollen(List.of(BenutzerRolle.ADMINISTRATOR));


		Benutzer result = converter.convertToBenutzer(jpa);

		assertNotNull(result);
		assertEquals(result.uuid().toString(), jpa.getUuid().toString());
		assertEquals(result.username(), jpa.getUsername());
		assertEquals(result.name(), jpa.getName());
		assertTrue(result.istAdmin());
		assertTrue(result.istKampfrichter(UUID.fromString(turniereJpa.get(1).getUuid())));
		assertFalse(result.istKampfrichter(UUID.fromString(turniereJpa.get(0).getUuid())));
		assertEquals(1, result.benutzerRollen().size());
		assertTrue(result.benutzerRollen().containsAll(List.of(BenutzerRolle.ADMINISTRATOR)));
		assertEquals(2, result.turnierRollen().size());
		assertEquals(turniereJpa.get(0).getUuid(), result.turnierRollen().get(0).turnierId().toString());
		assertEquals(1, result.turnierRollen().get(0).rollen().size());
		assertEquals(BenutzerRolle.BEOBACHTER, result.turnierRollen().get(0).rollen().get(0));
		assertEquals(turniereJpa.get(1).getUuid(), result.turnierRollen().get(1).turnierId().toString());
		assertEquals(1, result.turnierRollen().get(1).rollen().size());
		assertEquals(BenutzerRolle.KAMPFRICHTER, result.turnierRollen().get(1).rollen().get(0));
	}

	@Test
	void convertFromBenutzer() {
		UUID turnierId1 = UUID.randomUUID();
		UUID turnierId2 = UUID.randomUUID();
		List<TurnierRollen> turnierRollen = List.of(
			new TurnierRollen(turnierId1, List.of(BenutzerRolle.BEOBACHTER)),
			new TurnierRollen(turnierId2, List.of(BenutzerRolle.KAMPFRICHTER)));

		Benutzer benutzer = new Benutzer(
			null, "username", "name", turnierRollen, List.of(BenutzerRolle.ADMINISTRATOR)
		);

		BenutzerJpa result = converter.convertFromBenutzer(benutzer);

		assertNotNull(result);
		assertNull(result.getUuid());
		assertEquals(benutzer.name(), result.getName());
		assertEquals(benutzer.username(), result.getUsername());
		assertEquals(1, result.getRollen().size());
		assertEquals(benutzer.benutzerRollen().get(0), result.getRollen().get(0));
		assertEquals(2, result.getTurnierRollen().size());

		assertEquals(1, result.getTurnierRollen().get(0).getRollen().size());
		assertEquals(benutzer.turnierRollen().get(0).rollen().get(0), result.getTurnierRollen().get(0).getRollen().get(0));
		assertEquals(turnierId1.toString(), result.getTurnierRollen().get(0).getId().getTurnierUuid());
		assertEquals(benutzer.uuid(), result.getTurnierRollen().get(0).getId().getBenutzerUuid());

		assertEquals(1, result.getTurnierRollen().get(1).getRollen().size());
		assertEquals(benutzer.turnierRollen().get(1).rollen().get(0), result.getTurnierRollen().get(1).getRollen().get(0));
		assertEquals(turnierId2.toString(), result.getTurnierRollen().get(1).getId().getTurnierUuid());
		assertEquals(benutzer.uuid(), result.getTurnierRollen().get(1).getId().getBenutzerUuid());


	}
}