package de.sinnix.judoturnier.adapter;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Response;
import com.microsoft.playwright.options.WaitUntilState;
import de.sinnix.judoturnier.application.EinstellungenService;
import de.sinnix.judoturnier.application.GewichtsklassenService;
import de.sinnix.judoturnier.application.TurnierService;
import de.sinnix.judoturnier.application.VereinService;
import de.sinnix.judoturnier.application.WettkaempferService;
import de.sinnix.judoturnier.application.WettkampfService;
import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Begegnung;
import de.sinnix.judoturnier.model.Einstellungen;
import de.sinnix.judoturnier.model.Farbe;
import de.sinnix.judoturnier.model.Geschlecht;
import de.sinnix.judoturnier.model.GewichtsklassenGruppe;
import de.sinnix.judoturnier.model.Turnier;
import de.sinnix.judoturnier.model.TurnierTyp;
import de.sinnix.judoturnier.model.Verein;
import de.sinnix.judoturnier.model.Wettkaempfer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AnsichtenIntegrationTest extends AbstractIntegrationTest {

	private static SeedData seedData;

	@Autowired
	private PlatformTransactionManager transactionManager;
	@Autowired
	private TurnierService             turnierService;
	@Autowired
	private VereinService              vereinService;
	@Autowired
	private WettkaempferService        wettkaempferService;
	@Autowired
	private GewichtsklassenService     gewichtsklassenService;
	@Autowired
	private WettkampfService           wettkampfService;
	@Autowired
	private EinstellungenService       einstellungenService;

	@Test
	void startseiteAnonym() {
		try (BrowserContext context = browser.newContext(new Browser.NewContextOptions())) {
			assertViewContains(context, "/", "Software Information");
		}
	}

	@Test
	void startseiteEingeloggt() {
		try (BrowserContext context = login("kr1", "k1")) {
			assertViewContains(context, "/", "Sie sind angemeldet als kr1");
		}
	}

	@Test
	void kontakt() {
		try (BrowserContext context = browser.newContext(new Browser.NewContextOptions())) {
			assertViewContains(context, "/kontakt", "Kontaktiere mich");
		}
	}

	@Test
	void impressum() {
		try (BrowserContext context = browser.newContext(new Browser.NewContextOptions())) {
			assertViewContains(context, "/impressum", "Impressum");
		}
	}

	@Test
	void datenschutz() {
		try (BrowserContext context = browser.newContext(new Browser.NewContextOptions())) {
			assertViewContains(context, "/datenschutz", "Datenschutz");
		}
	}

	@Test
	void turnierverwaltung() {
		seedData();
		try (BrowserContext context = login("admin1", "s3cr3t")) {
			assertViewContains(context, "/turnierverwaltung", "Turniere Verwaltung");
		}
	}

	@Test
	void turnieruebersicht() {
		SeedData data = seedData();
		try (BrowserContext context = login("admin1", "s3cr3t")) {
			assertViewContains(context, "/turnier/" + data.randoriTurnierId(), "Turnierdaten");
		}
	}

	@Test
	void vereineliste() {
		SeedData data = seedData();
		try (BrowserContext context = login("admin1", "s3cr3t")) {
			assertViewContains(context, "/turnier/" + data.randoriTurnierId() + "/vereine", "Eingetragene Vereine");
		}
	}

	@Test
	void vereinNeu() {
		SeedData data = seedData();
		try (BrowserContext context = login("admin1", "s3cr3t")) {
			assertViewContains(context, "/turnier/" + data.randoriTurnierId() + "/vereine/verein-neu", "Neuer Verein");
		}
	}

	@Test
	void vereinBearbeiten() {
		SeedData data = seedData();
		try (BrowserContext context = login("admin1", "s3cr3t")) {
			assertViewContains(context, "/turnier/" + data.randoriTurnierId() + "/vereine/verein/" + data.randoriVereinId(), "Editiere Verein");
		}
	}

	@Test
	void wettkaempferliste() {
		SeedData data = seedData();
		try (BrowserContext context = login("admin1", "s3cr3t")) {
			assertViewContains(context, "/turnier/" + data.randoriTurnierId() + "/wettkaempfer", "Gemeldete Wettkämpfer");
		}
	}

	@Test
	void wettkaempferNeu() {
		SeedData data = seedData();
		try (BrowserContext context = login("admin1", "s3cr3t")) {
			assertViewContains(context, "/turnier/" + data.randoriTurnierId() + "/wettkaempfer/wettkaempfer-neu", "Neuer Wettkämpfer");
		}
	}

	@Test
	void wettkaempferBearbeiten() {
		SeedData data = seedData();
		try (BrowserContext context = login("admin1", "s3cr3t")) {
			assertViewContains(context, "/turnier/" + data.randoriTurnierId() + "/wettkaempfer/wettkaempfer/" + data.randoriWettkaempferId(), "Editiere Wettkämpfer");
		}
	}

	@Test
	void gewichtsklassen() {
		SeedData data = seedData();
		try (BrowserContext context = login("admin1", "s3cr3t")) {
			assertViewContains(context, "/turnier/" + data.randoriTurnierId() + "/gewichtsklassen", "Gewichtsklassen");
		}
	}

	@Test
	void einstellungen() {
		SeedData data = seedData();
		try (BrowserContext context = login("admin1", "s3cr3t")) {
			assertViewContains(context, "/turnier/" + data.randoriTurnierId() + "/einstellungen", "Einstellungen des Turnieres");
		}
	}

	@Test
	void begegnungenRandori() {
		SeedData data = seedData();
		try (BrowserContext context = login("admin1", "s3cr3t")) {
			assertViewContains(context, "/turnier/" + data.randoriTurnierId() + "/begegnungen/randori", "Matte 1");
		}
	}

	@Test
	void begegnungenNormal() {
		SeedData data = seedData();
		try (BrowserContext context = login("admin1", "s3cr3t")) {
			assertViewContains(context, "/turnier/" + data.standardTurnierId() + "/begegnungen/normal", "Matte 1");
		}
	}

	@Test
	void wettkampfRandori() {
		SeedData data = seedData();
		try (BrowserContext context = login("admin1", "s3cr3t")) {
			assertViewContains(context, "/turnier/" + data.randoriTurnierId() + "/begegnungen/randori/" + data.randoriBegegnungId(), "Aktuelle Begegnung");
		}
	}

	@Test
	void wettkampfNormal() {
		SeedData data = seedData();
		try (BrowserContext context = login("admin1", "s3cr3t")) {
			assertViewContains(context, "/turnier/" + data.standardTurnierId() + "/begegnungen/normal/" + data.standardBegegnungId(), "Aktuelle Begegnung");
		}
	}

	@Test
	void mattenanzeigeRandori() {
		SeedData data = seedData();
		try (BrowserContext context = login("admin1", "s3cr3t")) {
			assertViewContains(context, "/turnier/" + data.randoriTurnierId() + "/mattenanzeige/randori/" + data.randoriMatte() + "/" + data.randoriMattenrunde(), "Matte " + data.randoriMatte());
		}
	}

	@Test
	void mattenanzeigeNormal() {
		SeedData data = seedData();
		try (BrowserContext context = login("admin1", "s3cr3t")) {
			assertViewContains(context, "/turnier/" + data.standardTurnierId() + "/mattenanzeige/normal/" + data.standardBegegnungId(), "Matte " + data.standardMatte());
		}
	}

	@Test
	void mattenzeitRandori() {
		SeedData data = seedData();
		try (BrowserContext context = login("admin1", "s3cr3t")) {
			assertViewContains(context, "/turnier/" + data.randoriTurnierId() + "/mattenanzeige/randori/" + data.randoriMatte() + "/" + data.randoriMattenrunde() + "/zeit", "Matte " + data.randoriMatte());
		}
	}

	@Test
	void mattenzeitNormal() {
		SeedData data = seedData();
		try (BrowserContext context = login("admin1", "s3cr3t")) {
			assertViewContains(context, "/turnier/" + data.standardTurnierId() + "/mattenanzeige/normal/" + data.standardBegegnungId() + "/zeit", "Matte " + data.standardMatte());
		}
	}

	@Test
	void qrcode() {
		SeedData data = seedData();
		try (BrowserContext context = login("admin1", "s3cr3t")) {
			assertViewContains(context, "/turnier/" + data.randoriTurnierId() + "/qrcodes/U11", "QR-Codes Randori");
		}
	}

	@Test
	void druckansichtBegegnungenRandori() {
		SeedData data = seedData();
		try (BrowserContext context = browser.newContext(new Browser.NewContextOptions())) {
			assertViewContains(context, "/turnier/" + data.randoriTurnierId() + "/druck/randori_printview_matches/U11", "Matte 1");
		}
	}

	@Test
	void druckansichtBegegnungenRandoriDateneingabe() {
		SeedData data = seedData();
		try (BrowserContext context = login("admin1", "s3cr3t")) {
			assertViewContains(context, "/turnier/" + data.randoriTurnierId() + "/druck/randori_printview_matches_inserting_data/U11", "Matte 1");
		}
	}

	@Test
	void druckansichtGruppenRandori() {
		SeedData data = seedData();
		try (BrowserContext context = browser.newContext(new Browser.NewContextOptions())) {
			assertViewContains(context, "/turnier/" + data.randoriTurnierId() + "/druck/randori_printview_groups/U11", "U11");
		}
	}

	@Test
	void druckansichtBegegnungenTurnier() {
		SeedData data = seedData();
		try (BrowserContext context = browser.newContext(new Browser.NewContextOptions())) {
			assertViewContains(context, "/turnier/" + data.standardTurnierId() + "/druck/turnier_printview_matches/m/U11", "Matte 1");
		}
	}

	@Test
	void druckansichtGruppenTurnier() {
		SeedData data = seedData();
		try (BrowserContext context = browser.newContext(new Browser.NewContextOptions())) {
			assertViewContains(context, "/turnier/" + data.standardTurnierId() + "/druck/turnier_printview_groups/m/U11", "U11");
		}
	}

	private Page assertViewContains(BrowserContext context, String path, String expectedText) {
		Page page = context.newPage();
		Response response = page.navigate(url(path), new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
		assertNotNull(response);
		String bodyText = page.locator("body").textContent();
		String responseText = response.text();
		assertEquals(200, response.status(), "Unerwarteter Status für " + path + " mit Body: " + bodyText);
		assertTrue(bodyText.contains(expectedText) || responseText.contains(expectedText), "Ansicht enthält nicht '" + expectedText + "' für " + path + ". Body: " + bodyText + ", Response: " + responseText);
		return page;
	}

	private SeedData seedData() {
		if (seedData != null) {
			return seedData;
		}

		TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
		transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		SeedData createdSeedData = transactionTemplate.execute(status -> createSeedData());
		if (createdSeedData == null) {
			throw new IllegalStateException("Testdaten konnten nicht angelegt werden.");
		}
		seedData = createdSeedData;
		return seedData;
	}

	private SeedData createSeedData() {
		String suffix = UUID.randomUUID().toString().substring(0, 8);
		Turnier randoriTurnier = turnierService.erstelleTurnier("Integration Randori " + suffix, "Testort", "2026-01-01");
		Verein randoriVerein = vereinService.speichereVerein(new Verein(null, "Integration Verein Randori " + suffix, randoriTurnier.uuid()));
		List<Wettkaempfer> randoriWettkaempfer = speichereWettkaempfer(randoriTurnier.uuid(), randoriVerein, "Randori " + suffix, List.of(Geschlecht.m, Geschlecht.w, Geschlecht.m, Geschlecht.w));
		erstelleGewichtsklassenUndBegegnungen(randoriTurnier.uuid());
		Begegnung randoriBegegnung = ersteBegegnung(randoriTurnier.uuid());

		Turnier standardTurnier = turnierService.erstelleTurnier("Integration Standard " + suffix, "Testort", "2026-01-02");
		Einstellungen defaultEinstellungen = einstellungenService.ladeEinstellungen(standardTurnier.uuid());
		einstellungenService.speichereTurnierEinstellungen(new Einstellungen(
			TurnierTyp.STANDARD,
			defaultEinstellungen.mattenAnzahl(),
			defaultEinstellungen.wettkampfReihenfolge(),
			defaultEinstellungen.gruppengroessen(),
			defaultEinstellungen.variablerGewichtsteil(),
			defaultEinstellungen.separateAlterklassen(),
			defaultEinstellungen.wettkampfzeiten(),
			standardTurnier.uuid()
		));
		Verein standardVerein = vereinService.speichereVerein(new Verein(null, "Integration Verein Standard " + suffix, standardTurnier.uuid()));
		speichereWettkaempfer(standardTurnier.uuid(), standardVerein, "Standard " + suffix, List.of(Geschlecht.m, Geschlecht.m, Geschlecht.m, Geschlecht.m));
		erstelleGewichtsklassenUndBegegnungen(standardTurnier.uuid());
		Begegnung standardBegegnung = ersteBegegnung(standardTurnier.uuid());

		return new SeedData(
			randoriTurnier.uuid(),
			standardTurnier.uuid(),
			randoriVerein.id(),
			randoriWettkaempfer.getFirst().id(),
			randoriBegegnung.getId(),
			standardBegegnung.getId(),
			randoriBegegnung.getMatteId(),
			randoriBegegnung.getMattenRunde(),
			standardBegegnung.getMatteId(),
			standardBegegnung.getMattenRunde()
		);
	}

	private List<Wettkaempfer> speichereWettkaempfer(UUID turnierId, Verein verein, String prefix, List<Geschlecht> geschlechter) {
		return IntStream.range(0, geschlechter.size())
			.mapToObj(index -> wettkaempferService.speichereKaempfer(new Wettkaempfer(
				null,
				prefix + " Teilnehmer " + (index + 1),
				geschlechter.get(index),
				Altersklasse.U11,
				verein,
				25.0 + index,
				Optional.of(Farbe.WEISS),
				true,
				false,
				turnierId
			)))
			.toList();
	}

	private void erstelleGewichtsklassenUndBegegnungen(UUID turnierId) {
		List<GewichtsklassenGruppe> gewichtsklassenGruppen;
		try {
			gewichtsklassenGruppen = gewichtsklassenService.teileInGewichtsklassen(wettkaempferService.alleKaempfer(turnierId), turnierId).stream()
				.filter(gruppe -> !gruppe.teilnehmer().isEmpty())
				.toList();
		} catch (Exception e) {
			throw new IllegalStateException("Gewichtsklassen fuer Integrationstest konnten nicht erstellt werden.", e);
		}
		gewichtsklassenService.speichere(gewichtsklassenGruppen);
		wettkampfService.erstelleWettkampfreihenfolge(turnierId);
	}

	private Begegnung ersteBegegnung(UUID turnierId) {
		return wettkampfService.ladeAlleBegegnungen(turnierId).stream()
			.filter(begegnung -> begegnung.getWettkaempfer1().isPresent())
			.findFirst()
			.orElseThrow();
	}

	private record SeedData(
		UUID randoriTurnierId,
		UUID standardTurnierId,
		UUID randoriVereinId,
		UUID randoriWettkaempferId,
		UUID randoriBegegnungId,
		UUID standardBegegnungId,
		Integer randoriMatte,
		Integer randoriMattenrunde,
		Integer standardMatte,
		Integer standardMattenrunde) {
	}
}
