package de.sinnix.judoturnier.adapter.secondary;

import de.sinnix.judoturnier.model.Benutzer;
import de.sinnix.judoturnier.model.BenutzerRolle;
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

	}
}