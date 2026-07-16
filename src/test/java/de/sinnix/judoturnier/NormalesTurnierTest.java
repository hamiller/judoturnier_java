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
	public void testNormalesTurnier_ErwachseneBisFuenfKaempfenImPoolsystem() throws Exception {
		// erstelle Gewichtsklassen
		List<Wettkaempfer> wettkaempferList = wettkaempferService.alleKaempfer(this.turnier.uuid());
		assertEquals(8, wettkaempferList.size(), "Es sollten 8 Wettkämpfer existieren");

		var gwks = gewichtsklassenService.teileInGewichtsklassen(wettkaempferList, this.turnier.uuid());
		assertEquals(2 + 1, gwks.size(), "Es sollte 2 gefüllte Gewichtsklassen + 1 Dummy zum Verschieben der Wettkämpfer geben");

		gewichtsklassenService.speichere(gwks);
		assertEquals(gewichtsklassenService.ladeGewichtsklassenGruppe(Altersklasse.FRAUEN, this.turnier.uuid()).size(), 2 + 1, "Es sollten 2 gefüllte Gewichtsklassen + 1 Dummy zum Verschieben der Wettkämpfer in der Datenbank gefunden werden");


		// erstelle Begegnungen
		wettkampfService.erstelleWettkampfreihenfolge(this.turnier.uuid());


		// Prüfe, dass die kleinen Erwachsenengruppen als Pools ohne Freilose angelegt werden.
		List<Matte> wettkampfreihenfolgeJeMatte = turnierService.ladeWettkampfreihenfolge(this.turnier.uuid());
		assertEquals(1, wettkampfreihenfolgeJeMatte.size(), "Es sollte nur eine Matte existieren");

		Matte matte = wettkampfreihenfolgeJeMatte.getFirst();
		var gruppenIds = matte.runden().stream().map(Runde::gruppe).map(WettkampfGruppe::id).collect(Collectors.toSet());
		assertEquals(2, gruppenIds.size(), "Es sollten genau 2 verschiedene Gruppen sein");

		var erwarteteBegegnungen = gwks.stream()
			.filter(gwk -> !gwk.teilnehmer().isEmpty())
			.mapToInt(gwk -> gwk.teilnehmer().size() * (gwk.teilnehmer().size() - 1) / 2)
			.sum();
		var echteBegegnungen = matte.runden().stream()
			.flatMap(runde -> runde.begegnungen().stream())
			.filter(begegnung -> begegnung.getWettkaempfer1().isPresent() && begegnung.getWettkaempfer2().isPresent())
			.toList();
		assertEquals(erwarteteBegegnungen, echteBegegnungen.size(), "Poolsysteme müssen n*(n-1)/2 echte Begegnungen erzeugen");

		var begegnungenProGruppe = echteBegegnungen.stream()
			.map(Begegnung::getWettkampfGruppe)
			.map(WettkampfGruppe::id)
			.collect(Collectors.groupingBy(gid -> gid, Collectors.counting()));
		assertEquals(2, begegnungenProGruppe.size(), "Es sollten genau zwei Gruppen existieren");
		assertTrue(begegnungenProGruppe.containsValue(3L), "Die Dreiergruppe sollte 3 echte Begegnungen haben");
		assertTrue(begegnungenProGruppe.containsValue(10L), "Die Fünfergruppe sollte 10 echte Begegnungen haben");
		assertTrue(matte.runden().stream()
			.flatMap(runde -> runde.begegnungen().stream())
			.allMatch(begegnung -> begegnung.getBegegnungId().rundenTyp == Begegnung.RundenTyp.GEWINNERRUNDE), "Poolsysteme haben nur Gewinnerrunden");
		assertTrue(matte.runden().stream()
			.flatMap(runde -> runde.begegnungen().stream())
			.noneMatch(begegnung -> begegnung.getWettkaempfer1().isEmpty() ^ begegnung.getWettkaempfer2().isEmpty()), "Poolsysteme dürfen keine Freilose enthalten");
	}

}
