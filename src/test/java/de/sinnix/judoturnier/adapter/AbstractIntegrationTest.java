package de.sinnix.judoturnier.adapter;


import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.WaitUntilState;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import jakarta.transaction.Transactional;
import org.apache.http.client.utils.URIBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("componenttest")
@Transactional
public abstract class AbstractIntegrationTest {

	private static final String KEYCLOAK_IMAGE        = "quay.io/keycloak/keycloak:26.7";
	private static final long   KEYCLOAK_MEMORY_LIMIT = 768L * 1024 * 1024;
	private static final String CLIENT_SECRET         = "the-client-secret";
	private static final double PLAYWRIGHT_TIMEOUT_MS = 120_000;
	private static final Map<String, String> AUTHENTICATED_STORAGE_STATES = new ConcurrentHashMap<>();

	@LocalServerPort
	int port;
	static               Playwright             playwright;
	static               Browser                browser;
	static               PostgreSQLContainer<?> postgres      = new PostgreSQLContainer<>("postgres:17").withReuse(reuseContainers());
	static               KeycloakContainer      keycloak      = new KeycloakContainer(KEYCLOAK_IMAGE)
		.withRealmImportFile("/judoturnier-realm.json")
		.withRamPercentage(50, 70)
		.withCreateContainerCmdModifier(cmd -> cmd.getHostConfig().withMemory(KEYCLOAK_MEMORY_LIMIT))
		.waitingFor(Wait.forHttp("/realms/judoturnier/.well-known/openid-configuration")
			.forPort(8080)
			.forStatusCode(200)
			.withStartupTimeout(Duration.ofMinutes(5)))
		.withReuse(reuseContainers());

	private static boolean reuseContainers() {
		return !Boolean.parseBoolean(System.getenv("CI"));
	}


	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", postgres::getJdbcUrl);
		registry.add("spring.datasource.username", postgres::getUsername);
		registry.add("spring.datasource.password", postgres::getPassword);
		registry.add("spring.security.oauth2.client.provider.keycloak.authorization-uri", () -> keycloak.getAuthServerUrl() + "/realms/judoturnier/protocol/openid-connect/auth");
		registry.add("spring.security.oauth2.client.provider.keycloak.issuer-uri", () -> keycloak.getAuthServerUrl() + "/realms/judoturnier");
		registry.add("spring.security.oauth2.client.registration.keycloak.client-secret", () -> CLIENT_SECRET);
	}

	@BeforeAll
	static void beforeAll() {
		postgres.start();
		keycloak.start();

		playwright = Playwright.create();
		browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(true));
	}

	@AfterAll
	static void afterAll() {
		// Spring caches the application context across subclasses. Stopping these
		// containers here would leave the cached DataSource pointing to a dead port.

		if (browser != null) {
			browser.close();
		}
		if (playwright != null) {
			playwright.close();
		}
	}

	public String getKampfrichterToken() {
		URI authorizationURI = null;
		try {
			authorizationURI = new URIBuilder(keycloak.getAuthServerUrl() + "/realms/judoturnier/protocol/openid-connect/token").build();
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
		WebClient webclient = WebClient.builder().build();
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		formData.put("client_id", Collections.singletonList("judoturnier-client"));
		formData.put("username", Collections.singletonList("kr1"));
		formData.put("password", Collections.singletonList("k1"));
		formData.put("grant_type", Collections.singletonList("password"));
		formData.put("scope", Collections.singletonList("openid"));
		formData.put("client_secret", Collections.singletonList(CLIENT_SECRET));

		String result = webclient.post()
			.uri(authorizationURI)
			.contentType(MediaType.APPLICATION_FORM_URLENCODED)
			.body(BodyInserters.fromFormData(formData))
			.retrieve()
			.bodyToMono(String.class)
			.block();

		JacksonJsonParser jsonParser = new JacksonJsonParser();
		return jsonParser.parseMap(result)
			.get("access_token")
			.toString();
	}

	protected BrowserContext login(String username, String password) {
		String storageState = AUTHENTICATED_STORAGE_STATES.computeIfAbsent(port + ":" + username, key -> createAuthenticatedStorageState(username, password));
		BrowserContext context = browser.newContext(new Browser.NewContextOptions().setStorageState(storageState));
		configureTimeouts(context);
		return context;
	}

	protected String url(String path) {
		return "http://localhost:" + port + path;
	}

	private String createAuthenticatedStorageState(String username, String password) {
		try (BrowserContext context = browser.newContext(new Browser.NewContextOptions())) {
			configureTimeouts(context);
			Page page = context.newPage();

			page.navigate(url("/oauth2/authorization/keycloak"), new Page.NavigateOptions()
				.setWaitUntil(WaitUntilState.DOMCONTENTLOADED)
				.setTimeout(PLAYWRIGHT_TIMEOUT_MS));
			page.locator("#username").fill(username);
			page.locator("#password").fill(password);
			page.locator("#kc-login").click(new Locator.ClickOptions().setTimeout(PLAYWRIGHT_TIMEOUT_MS));

			page.waitForURL(url("/**"), new Page.WaitForURLOptions()
				.setWaitUntil(WaitUntilState.DOMCONTENTLOADED)
				.setTimeout(PLAYWRIGHT_TIMEOUT_MS));

			String bodyText = page.locator("body").textContent();
			if (!bodyText.contains("Sie sind angemeldet als " + username)) {
				throw new IllegalStateException("Login fuer " + username + " fehlgeschlagen. Body: " + bodyText);
			}
			return context.storageState();
		}
	}

	private void configureTimeouts(BrowserContext context) {
		context.setDefaultTimeout(PLAYWRIGHT_TIMEOUT_MS);
		context.setDefaultNavigationTimeout(PLAYWRIGHT_TIMEOUT_MS);
	}
}
