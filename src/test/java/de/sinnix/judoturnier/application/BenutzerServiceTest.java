package de.sinnix.judoturnier.application;

import de.sinnix.judoturnier.adapter.secondary.BenutzerRepository;
import de.sinnix.judoturnier.model.Benutzer;
import de.sinnix.judoturnier.model.BenutzerRolle;
import de.sinnix.judoturnier.model.OidcBenutzer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
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

		when(benutzerRepository.findBenutzer(benutzer.uuid())).thenReturn(Optional.of(benutzer));

		Benutzer result = service.holeBenutzer(oidcBenutzer);

		assertEquals(benutzer, result);
	}

	@Test
	void benutzerNichtAnlegenWennExistent() {

	}

	@Test
	@Disabled
	void darfBenutzerTurnierSehen() {

	}

}