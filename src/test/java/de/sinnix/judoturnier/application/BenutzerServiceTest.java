package de.sinnix.judoturnier.application;

import de.sinnix.judoturnier.adapter.secondary.BenutzerRepository;
import de.sinnix.judoturnier.model.Benutzer;
import de.sinnix.judoturnier.model.BenutzerRolle;
import de.sinnix.judoturnier.model.OidcBenutzer;
import de.sinnix.judoturnier.model.TurnierRollen;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BenutzerServiceTest {

	@Mock
	private BenutzerRepository benutzerRepository;
	@InjectMocks
	private BenutzerService    service;

	private UUID turnierUUID;

	@BeforeEach
	void setUp() {
		turnierUUID = UUID.randomUUID();
	}

	@Test
	void benutzerAnlegenWennFehlend() {
		UUID userId = UUID.randomUUID();
		OidcBenutzer oidcBenutzer = new OidcBenutzer(userId, "username", "name", List.of(BenutzerRolle.BEOBACHTER));
		Benutzer benutzer = new Benutzer(userId, "username", "name", List.of(), List.of(BenutzerRolle.BEOBACHTER));

		when(benutzerRepository.findBenutzerByUsername(any())).thenReturn(Optional.empty());
		when(benutzerRepository.save(any())).thenReturn(benutzer);

		Benutzer result = service.holeBenutzer(oidcBenutzer);

		assertEquals(benutzer, result);
		verify(benutzerRepository, times(1)).save(any());
	}

	@Test
	void benutzerNichtAnlegenWennExistent() {
		UUID userId = UUID.randomUUID();
		OidcBenutzer oidcBenutzer = new OidcBenutzer(userId, "username", "name", List.of(BenutzerRolle.BEOBACHTER));
		Benutzer benutzer = new Benutzer(userId, "username", "name", List.of(), List.of(BenutzerRolle.BEOBACHTER));

		when(benutzerRepository.findBenutzerByUsername(benutzer.username())).thenReturn(Optional.of(benutzer));

		Benutzer result = service.holeBenutzer(oidcBenutzer);

		assertEquals(benutzer, result);
	}


	@Test
	void ordneBenutzerZuTurnier() {
		UUID turnier1UUID = UUID.randomUUID();
		UUID userId1 = UUID.randomUUID();
		UUID userId2 = UUID.randomUUID();
		Benutzer benutzer1 = new Benutzer(userId1,
			"username1",
			"name1",
			List.of(new TurnierRollen(null, turnier1UUID, List.of(BenutzerRolle.BEOBACHTER))),
			List.of(BenutzerRolle.BEOBACHTER));
		Benutzer benutzer2 = new Benutzer(userId2,
			"username2",
			"nam2",
			List.of(new TurnierRollen(null, turnier1UUID, List.of(BenutzerRolle.ADMINISTRATOR, BenutzerRolle.BEOBACHTER))),
			List.of(BenutzerRolle.ADMINISTRATOR, BenutzerRolle.BEOBACHTER));
		List<UUID> benutzerList = List.of(userId1, userId2);

		TurnierRollen turnierRollen1 = new TurnierRollen(null, turnier1UUID, benutzer1.benutzerRollen());
		TurnierRollen turnierRollen2 = new TurnierRollen(null, turnier1UUID, benutzer2.benutzerRollen());


		when(benutzerRepository.findBenutzer(any())).thenAnswer(invocation -> {
			UUID userId = invocation.getArgument(0);
			if (userId.equals(userId1)) return Optional.of(benutzer1);
			if (userId.equals(userId2)) return Optional.of(benutzer2);
			return Optional.empty();
		});

		service.ordneBenutzerZuTurnier(benutzerList, turnier1UUID);

		verify(benutzerRepository, times(2)).findBenutzer(any());

		ArgumentCaptor<UUID> argument1Captor = ArgumentCaptor.forClass(UUID.class);
		ArgumentCaptor<TurnierRollen> argument2Captor = ArgumentCaptor.forClass(TurnierRollen.class);
		ArgumentCaptor<UUID> argument3Captor = ArgumentCaptor.forClass(UUID.class);
		verify(benutzerRepository, times(2)).addTurnierRollen(argument1Captor.capture(), argument2Captor.capture(), argument3Captor.capture());

		List<UUID> capturedBenutzerIds = argument1Captor.getAllValues();
		assertEquals(2, capturedBenutzerIds.size());
		assertEquals(userId1, capturedBenutzerIds.get(0));
		assertEquals(userId2, capturedBenutzerIds.get(1));

		List<TurnierRollen> capturedTurnierRollen = argument2Captor.getAllValues();
		assertEquals(2, capturedTurnierRollen.size());
		assertEquals(turnierRollen1, capturedTurnierRollen.get(0));
		assertEquals(turnierRollen2, capturedTurnierRollen.get(1));

		List<UUID> capturedTurnierIds = argument3Captor.getAllValues();
		assertEquals(2, capturedTurnierIds.size());
		assertEquals(turnier1UUID, capturedTurnierIds.get(0));
		assertEquals(turnier1UUID, capturedTurnierIds.get(1));
	}

	@Test
	void entferneBenutzerVonTurnier() {
		UUID turnier1UUID = UUID.randomUUID();
		UUID turnier2UUID = UUID.randomUUID();
		UUID userId1 = UUID.randomUUID();
		UUID userId2 = UUID.randomUUID();
		Benutzer benutzer1 = new Benutzer(userId1,
			"username1",
			"name1",
			List.of(new TurnierRollen(null, turnier1UUID, List.of(BenutzerRolle.BEOBACHTER)), new TurnierRollen(null, turnier2UUID, List.of(BenutzerRolle.BEOBACHTER))),
			List.of(BenutzerRolle.BEOBACHTER));
		Benutzer benutzer2 = new Benutzer(userId2,
			"username2",
			"name2",
			List.of(new TurnierRollen(null, turnier1UUID, List.of(BenutzerRolle.ADMINISTRATOR, BenutzerRolle.BEOBACHTER)), new TurnierRollen(null, turnier2UUID, List.of(BenutzerRolle.ADMINISTRATOR, BenutzerRolle.BEOBACHTER))),
			List.of(BenutzerRolle.ADMINISTRATOR, BenutzerRolle.BEOBACHTER));
		List<UUID> benutzerList = List.of(userId1, userId2);

		Benutzer benutzer1Neu = new Benutzer(
			userId1,
			benutzer1.username(),
			benutzer1.name(),
			List.of(new TurnierRollen(null, turnier2UUID, benutzer1.benutzerRollen())),
			benutzer1.benutzerRollen());
		Benutzer benutzer2Neu = new Benutzer(
			benutzer2.uuid(),
			benutzer2.username(),
			benutzer2.name(),
			List.of(new TurnierRollen(null, turnier2UUID, benutzer2.benutzerRollen())),
			benutzer2.benutzerRollen());


		when(benutzerRepository.findBenutzer(any())).thenAnswer(invocation -> {
			UUID userId = invocation.getArgument(0);
			if (userId.equals(userId1)) return Optional.of(benutzer1);
			if (userId.equals(userId2)) return Optional.of(benutzer2);
			return Optional.empty();
		});

		service.entferneBenutzerVonTurnier(benutzerList, turnier1UUID);

		verify(benutzerRepository, times(2)).findBenutzer(any());
		ArgumentCaptor<Benutzer> argumentCaptor = ArgumentCaptor.forClass(Benutzer.class);
		verify(benutzerRepository, times(2)).save(argumentCaptor.capture());
		List<Benutzer> capturedBenutzer = argumentCaptor.getAllValues();
		assertEquals(2, capturedBenutzer.size());
		assertEquals(benutzer1Neu, capturedBenutzer.get(0));
		assertEquals(benutzer2Neu, capturedBenutzer.get(1));
	}
}
