package de.sinnix.judoturnier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


import de.sinnix.judoturnier.application.EinstellungenService;
import de.sinnix.judoturnier.application.GewichtsklassenService;
import de.sinnix.judoturnier.application.TurnierService;
import de.sinnix.judoturnier.application.VereinService;
import de.sinnix.judoturnier.application.WettkaempferService;
import de.sinnix.judoturnier.application.WettkampfService;
import de.sinnix.judoturnier.config.SecurityConfig;
import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Begegnung;
import de.sinnix.judoturnier.model.Einstellungen;
import de.sinnix.judoturnier.model.Gruppengroessen;
import de.sinnix.judoturnier.model.Matte;
import de.sinnix.judoturnier.model.MattenAnzahl;
import de.sinnix.judoturnier.model.Runde;
import de.sinnix.judoturnier.model.SeparateAlterklassen;
import de.sinnix.judoturnier.model.Turnier;
import de.sinnix.judoturnier.model.TurnierTyp;
import de.sinnix.judoturnier.model.VariablerGewichtsteil;
import de.sinnix.judoturnier.model.Wettkaempfer;
import de.sinnix.judoturnier.model.WettkampfGruppe;
import de.sinnix.judoturnier.model.WettkampfReihenfolge;
import de.sinnix.judoturnier.model.Wettkampfzeiten;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
public class NormalesTurnierTest {
	private static final Logger         logger = LogManager.getLogger(NormalesTurnierTest.class);

	// prevent loading the security context
	@MockitoBean
	private              SecurityConfig securityConfig;
	@MockitoBean
	private ClientRegistrationRepository  clientRegistrationRepository;
	@MockitoBean
	private OAuth2AuthorizedClientManager authorizedClientManager;
	@Autowired
	private TurnierService                turnierService;
	@Autowired
	private EinstellungenService          einstellungenService;
	@Autowired
	private VereinService                 vereinService;
	@Autowired
	private WettkaempferService           wettkaempferService;
	@Autowired
	private ResourceLoader                resourceLoader;
	@Autowired
	private GewichtsklassenService        gewichtsklassenService;
	@Autowired
	private WettkampfService              wettkampfService;
	private Turnier                       turnier;

	@BeforeEach
	public void setup(@Autowired Flyway flyway) throws IOException {
		// Lösche alle vorherigen Turniere
		flyway.clean();
		flyway.migrate();

		// erstelle Turnier
		this.turnier = turnierService.erstelleTurnier("Normales Turnier", "Ort des Turniers", "2023-10-01");

		// speichere Einstellungen
		einstellungenService.speichereTurnierEinstellungen(new Einstellungen(
			TurnierTyp.STANDARD,
			new MattenAnzahl(1),
			WettkampfReihenfolge.ABWECHSELND,
			new Gruppengroessen(Map.of(Altersklasse.FRAUEN, 50)),
			new VariablerGewichtsteil(0.2),
			SeparateAlterklassen.GETRENNT,
			new Wettkampfzeiten(Map.of(Altersklasse.FRAUEN, 3 * 60)),
			this.turnier.uuid()));

		// speichere Vereine
		InputStream vereineIS = resourceLoader.getResource("classpath:/vereine.csv").getInputStream();
		MultipartFile vereineFile = new MockMultipartFile("vereine", "vereine.csv", "text/csv", vereineIS);
		vereinService.speichereCSV(this.turnier.uuid(), vereineFile);

		// speichere Wettkaempfer
		InputStream wettkaempferIS = resourceLoader.getResource("classpath:/wettkaempfer.csv").getInputStream();
		MultipartFile wettkaempferFile = new MockMultipartFile("wettkaempfer", "wettkaempfer.csv", "text/csv", wettkaempferIS);
		wettkaempferService.speichereCSV(this.turnier.uuid(), wettkaempferFile);
	}

	@Test
	public void testNormalesTurnier_FreiloseWerdenKorrektGesetzt() {
		// erstelle Gewichtsklassen
		List<Wettkaempfer> wettkaempferList = wettkaempferService.alleKaempfer(this.turnier.uuid());
		assertEquals(8, wettkaempferList.size(), "Es sollten 8 Wettkämpfer existieren");

		var gwks = gewichtsklassenService.teileInGewichtsklassen(wettkaempferList, this.turnier.uuid());
		assertEquals(2 + 1, gwks.size(), "Es sollte 2 gefüllte Gewichtsklassen + 1 Dummy zum Verschieben der Wettkämpfer geben");

		gewichtsklassenService.speichere(gwks);
		assertEquals(gewichtsklassenService.ladeGewichtsklassenGruppe(Altersklasse.FRAUEN, this.turnier.uuid()).size(), 2 + 1, "Es sollten 2 gefüllte Gewichtsklassen + 1 Dummy zum Verschieben der Wettkämpfer in der Datenbank gefunden werden");


		// erstelle Begegnungen
		wettkampfService.erstelleWettkampfreihenfolge(this.turnier.uuid());


		// PRÜFE ob Freilose richtig gesetzt sind
		List<Matte> wettkampfreihenfolgeJeMatte = turnierService.ladeWettkampfreihenfolge(this.turnier.uuid());
		assertEquals(1, wettkampfreihenfolgeJeMatte.size(), "Es sollte nur eine Matte existieren");

		Matte matte = wettkampfreihenfolgeJeMatte.getFirst();
		Set<UUID> gruppenIds = matte.runden().stream().map(Runde::gruppe).map(WettkampfGruppe::id).collect(Collectors.toSet());
		assertEquals(2, gruppenIds.size(), "Es sollten genau 2 verschiedene Gruppen sein");

		Map<UUID, Long> begegnungenProGruppe = matte.runden().stream()
			.flatMap(r -> r.begegnungen().stream().map(Begegnung::getWettkampfGruppe))
			.map(WettkampfGruppe::id)
			.collect(Collectors.groupingBy(gid -> gid, Collectors.counting()));
		assertEquals(2, begegnungenProGruppe.size(), "Es sollten genau zwei Gruppen existieren");
		assertTrue(begegnungenProGruppe.containsValue(3L), "Eine Gruppe sollte 3 Begegnungen haben");
		assertTrue(begegnungenProGruppe.containsValue(11L), "Eine Gruppe sollte 11 Begegnungen haben");

		UUID grosseWettkampfGruppe = begegnungenProGruppe.entrySet().stream()
			.filter(entry -> entry.getValue().equals(11L))
			.map(Map.Entry::getKey)
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("Keine Gruppe mit 11 Begegnungen gefunden"));
		UUID kleineWettkampfGruppe = begegnungenProGruppe.entrySet().stream()
			.filter(entry -> entry.getValue().equals(3L))
			.map(Map.Entry::getKey)
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("Keine Gruppe mit 3 Begegnungen gefunden"));

		UUID startGruppeId = null;
		for (int mattenRunde = 1; mattenRunde <= matte.runden().size(); mattenRunde++) {
			var runde = matte.runden().get(mattenRunde - 1);
			logger.debug("Runde der Matte: {}", mattenRunde);
			logger.debug("Runde der Gruppe '{}': {}", runde.gruppe().id(), runde.gruppenRunde());

			assertTrue(kleineWettkampfGruppe == runde.gruppe().id() || grosseWettkampfGruppe == runde.gruppe().id(), "Die Runde sollte entweder zur kleinen oder großen Wettkampfgruppe gehören");
			assertNotEquals(runde.gruppe().id(), startGruppeId, "Die Gruppen sollten abwechselnd kämpfen");
			startGruppeId = runde.gruppe().id();

			for (Begegnung begegnung : runde.begegnungen()) {
				logger.debug("  Begegnung: {} - {} vs {}", begegnung.getBegegnungId(), begegnung.getWettkaempfer1().map(Wettkaempfer::name), begegnung.getWettkaempfer2().map(Wettkaempfer::name));
			}

			if (kleineWettkampfGruppe == runde.gruppe().id()) {
				assertEquals(1, runde.begegnungen().size(), "Die kleine Gruppe sollte immer nur eine Begegnung haben, wegen Jeder-Gegen-Jeden");
				assertTrue(runde.begegnungen().get(0).getWettkaempfer1().isPresent(), "Die Begegnung sollte einen Wettkämpfer 1 haben");
				assertTrue(runde.begegnungen().get(0).getWettkaempfer2().isPresent(), "Die Begegnung sollte einen Wettkämpfer 2 haben");
				assertEquals(Begegnung.RundenTyp.GEWINNERRUNDE, runde.begegnungen().get(0).getBegegnungId().rundenTyp, "Die Begegnung der kleinen Gruppe sollte eine Gewinnerrunde sein");
			}

			if (grosseWettkampfGruppe == runde.gruppe().id()) {
				assertTrue(runde.gruppenRunde() == 1 || runde.gruppenRunde() == 2 || runde.gruppenRunde() == 3, "Die große Gruppe sollte in einer von 3 Runden sein");

				if (runde.gruppenRunde() == 1) {
					assertEquals(6, runde.begegnungen().size(), "Die erste Runde der großen Gruppe sollte 6 Begegnungen haben");
					assertEquals(Begegnung.RundenTyp.GEWINNERRUNDE, runde.begegnungen().get(0).getBegegnungId().rundenTyp, "Die Begegnung der großen Gruppe sollte eine Gewinnerrunde sein");
					assertEquals(Begegnung.RundenTyp.GEWINNERRUNDE, runde.begegnungen().get(1).getBegegnungId().rundenTyp, "Die Begegnung der großen Gruppe sollte eine Gewinnerrunde sein");
					assertEquals(Begegnung.RundenTyp.GEWINNERRUNDE, runde.begegnungen().get(2).getBegegnungId().rundenTyp, "Die Begegnung der großen Gruppe sollte eine Gewinnerrunde sein");
					assertEquals(Begegnung.RundenTyp.GEWINNERRUNDE, runde.begegnungen().get(3).getBegegnungId().rundenTyp, "Die Begegnung der großen Gruppe sollte eine Gewinnerrunde sein");
					assertEquals(Begegnung.RundenTyp.TROSTRUNDE, runde.begegnungen().get(4).getBegegnungId().rundenTyp, "Die Begegnung der großen Gruppe sollte eine Trostrunde sein");
					assertEquals(Begegnung.RundenTyp.TROSTRUNDE, runde.begegnungen().get(5).getBegegnungId().rundenTyp, "Die Begegnung der großen Gruppe sollte eine Trostrunde sein");

					assertTrue(runde.begegnungen().get(0).getWettkaempfer1().isPresent(), "Wettkämpfer 1 der ersten Begegnung sollte vorhanden sein");
					assertTrue(runde.begegnungen().get(0).getWettkaempfer2().isEmpty(), "Die erste Begegnung der großen Gruppe sollte ein Freilos haben");
					assertTrue(runde.begegnungen().get(1).getWettkaempfer1().isPresent(), "Wettkämpfer 1 der zweiten Begegnung sollte vorhanden sein");
					assertTrue(runde.begegnungen().get(1).getWettkaempfer2().isEmpty(), "Die zweite Begegnung der großen Gruppe sollte ein Freilos haben");
					assertTrue(runde.begegnungen().get(2).getWettkaempfer1().isPresent(), "Wettkämpfer 1 der dritten Begegnung sollte vorhanden sein");
					assertTrue(runde.begegnungen().get(2).getWettkaempfer2().isEmpty(), "Die dritte Begegnung der großen Gruppe sollte ein Freilos haben");
					assertTrue(runde.begegnungen().get(3).getWettkaempfer1().isPresent(), "Wettkämpfer 1 der vierten Begegnung sollte vorhanden sein");
					assertTrue(runde.begegnungen().get(3).getWettkaempfer2().isPresent(), "Wettkämpfer 2 der vierten Begegnung sollte vorhanden sein");
				}
				if (runde.gruppenRunde() == 2) {
					assertEquals(4, runde.begegnungen().size(), "Die zweite Runde der großen Gruppe sollte 4 Begegnungen haben");
					assertEquals(Begegnung.RundenTyp.GEWINNERRUNDE, runde.begegnungen().get(0).getBegegnungId().rundenTyp, "Die Begegnung der großen Gruppe sollte eine Gewinnerrunde sein");
					assertEquals(Begegnung.RundenTyp.GEWINNERRUNDE, runde.begegnungen().get(1).getBegegnungId().rundenTyp, "Die Begegnung der großen Gruppe sollte eine Gewinnerrunde sein");
					assertEquals(Begegnung.RundenTyp.TROSTRUNDE, runde.begegnungen().get(2).getBegegnungId().rundenTyp, "Die Begegnung der großen Gruppe sollte eine Trostrunde sein");
					assertEquals(Begegnung.RundenTyp.TROSTRUNDE, runde.begegnungen().get(3).getBegegnungId().rundenTyp, "Die Begegnung der großen Gruppe sollte eine Trostrunde sein");

					assertTrue(runde.begegnungen().get(0).getWettkaempfer1().isPresent(), "Wettkämpfer 1 der ersten Begegnung sollte vorhanden sein");
					assertTrue(runde.begegnungen().get(0).getWettkaempfer2().isPresent(), "Wettkämpfer 2 der ersten Begegnung sollte vorhanden sein");
					assertTrue(runde.begegnungen().get(1).getWettkaempfer1().isPresent(), "Wettkämpfer 1 der zweiten Begegnung sollte vorhanden sein");
					assertTrue(runde.begegnungen().get(1).getWettkaempfer2().isEmpty(), "Die zweite Begegnung der großen Gruppe sollte noch offen sein");
				}
				if (runde.gruppenRunde() == 3) {
					assertEquals(1, runde.begegnungen().size(), "Die dritte Runde der großen Gruppe sollte 1 Begegnung haben");
					assertEquals(Begegnung.RundenTyp.GEWINNERRUNDE, runde.begegnungen().get(0).getBegegnungId().rundenTyp, "Die Begegnung der großen Gruppe sollte eine Gewinnerrunde sein");
					assertTrue(runde.begegnungen().get(0).getWettkaempfer1().isEmpty(), "Die erste Begegnung der großen Gruppe sollte noch offen sein");
					assertTrue(runde.begegnungen().get(0).getWettkaempfer2().isEmpty(), "Die erste Begegnung der großen Gruppe sollte noch offen sein");
				}
			}

		}
	}

}
