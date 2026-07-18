package de.sinnix.judoturnier;

import de.sinnix.judoturnier.application.EinstellungenService;
import de.sinnix.judoturnier.application.GewichtsklassenService;
import de.sinnix.judoturnier.application.TurnierService;
import de.sinnix.judoturnier.application.VereinService;
import de.sinnix.judoturnier.application.WettkaempferService;
import de.sinnix.judoturnier.application.WettkampfService;
import de.sinnix.judoturnier.config.SecurityConfig;
import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Begegnung;
import de.sinnix.judoturnier.model.Farbe;
import de.sinnix.judoturnier.model.Geschlecht;
import de.sinnix.judoturnier.model.GewichtsklassenGruppe;
import de.sinnix.judoturnier.model.Gruppengroessen;
import de.sinnix.judoturnier.model.Matte;
import de.sinnix.judoturnier.model.MattenAnzahl;
import de.sinnix.judoturnier.model.Runde;
import de.sinnix.judoturnier.model.SeparateAlterklassen;
import de.sinnix.judoturnier.model.Turnier;
import de.sinnix.judoturnier.model.TurnierTyp;
import de.sinnix.judoturnier.model.VariablerGewichtsteil;
import de.sinnix.judoturnier.model.Verein;
import de.sinnix.judoturnier.model.Wettkaempfer;
import de.sinnix.judoturnier.model.WettkampfReihenfolge;
import de.sinnix.judoturnier.model.Wettkampfzeiten;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
class KOSystemMitDoppelterTrostrundeIntegrationTest {

	@MockitoBean
	private SecurityConfig securityConfig;
	@MockitoBean
	private ClientRegistrationRepository clientRegistrationRepository;
	@MockitoBean
	private OAuth2AuthorizedClientManager authorizedClientManager;

	@Autowired
	private TurnierService turnierService;
	@Autowired
	private EinstellungenService einstellungenService;
	@Autowired
	private VereinService vereinService;
	@Autowired
	private WettkaempferService wettkaempferService;
	@Autowired
	private GewichtsklassenService gewichtsklassenService;
	@Autowired
	private WettkampfService wettkampfService;

	private Turnier turnier;

	@BeforeEach
	void setup(@Autowired Flyway flyway) {
		flyway.clean();
		flyway.migrate();

		turnier = turnierService.erstelleTurnier("KO mit doppelter Trostrunde", "Testort", "2026-07-17");
		einstellungenService.speichereTurnierEinstellungen(new de.sinnix.judoturnier.model.Einstellungen(
			TurnierTyp.STANDARD,
			new MattenAnzahl(1),
			WettkampfReihenfolge.ABWECHSELND,
			new Gruppengroessen(Map.of(Altersklasse.MAENNER, 64)),
			new VariablerGewichtsteil(0.2),
			SeparateAlterklassen.GETRENNT,
			new Wettkampfzeiten(Map.of(Altersklasse.MAENNER, 4 * 60)),
			turnier.uuid()
		));
	}

	@Test
	void erstelltPersistierte64erListeFuer33Erwachsene() {
		Verein verein = vereinService.speichereVerein(new Verein(null, "Integration Verein", turnier.uuid()));
		List<Wettkaempfer> teilnehmer = IntStream.rangeClosed(1, 33)
			.mapToObj(nr -> wettkaempferService.speichereKaempfer(new Wettkaempfer(
				null,
				"Teilnehmer %02d".formatted(nr),
				Geschlecht.m,
				Altersklasse.MAENNER,
				verein,
				60.0 + nr / 100.0,
				Optional.of(Farbe.WEISS),
				true,
				false,
				turnier.uuid()
			)))
			.toList();
		gewichtsklassenService.speichere(List.of(new GewichtsklassenGruppe(UUID.randomUUID(), Altersklasse.MAENNER, Optional.of(Geschlecht.m), teilnehmer, Optional.empty(), 60.0, 66.0, turnier.uuid())));

		wettkampfService.erstelleWettkampfreihenfolge(turnier.uuid());

		List<Matte> matten = turnierService.ladeWettkampfreihenfolge(turnier.uuid());
		assertEquals(1, matten.size());

		List<Begegnung> begegnungen = matten.getFirst().runden().stream()
			.filter(runde -> !runde.istPause())
			.flatMap(runde -> runde.begegnungen().stream())
			.toList();
		assertEquals(79, begegnungen.size());
		assertTrue(begegnungen.stream().anyMatch(begegnung -> begegnung.getBegegnungId().equals(new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 6, 1))));
		assertTrue(begegnungen.stream().anyMatch(begegnung -> begegnung.getBegegnungId().equals(new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 4, 1))));
		assertTrue(begegnungen.stream().anyMatch(begegnung -> begegnung.getBegegnungId().equals(new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 8, 2))));

		List<Runde> kampfRunden = matten.getFirst().runden().stream().filter(runde -> !runde.istPause()).toList();
		assertEquals(11, kampfRunden.size());
		assertEquals(Begegnung.RundenTyp.GEWINNERRUNDE, kampfRunden.getFirst().begegnungen().getFirst().getBegegnungId().rundenTyp);
		assertEquals(Begegnung.RundenTyp.GEWINNERRUNDE, kampfRunden.getLast().begegnungen().getFirst().getBegegnungId().rundenTyp);
	}
}
