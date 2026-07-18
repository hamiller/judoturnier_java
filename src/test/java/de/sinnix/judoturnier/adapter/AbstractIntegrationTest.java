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
import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;


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
	private static final double PLAYWRIGHT_ACTION_TIMEOUT_MS     = 15_000;
	private static final double PLAYWRIGHT_NAVIGATION_TIMEOUT_MS = 45_000;
	private static final Map<String, String> AUTHENTICATED_STORAGE_STATES = new ConcurrentHashMap<>();
	private static final AtomicBoolean SHUTDOWN_HOOK_REGISTERED = new AtomicBoolean(false);

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
	static synchronized void beforeAll() {
		long startedAt = System.nanoTime();
		startContainer("PostgreSQL", postgres);
		startContainer("Keycloak", keycloak);
		if (playwright == null) {
			playwright = timed("Playwright.create", Playwright::create);
		}
		if (browser == null || !browser.isConnected()) {
			browser = timed("Firefox launch", () -> playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(true)));
		}
		registerShutdownHook();
		logDuration("Integration test setup", startedAt);
	}

	@AfterAll
	static void afterAll() {
		// Spring caches the application context across subclasses. The shared
		// containers and browser stay alive until the test JVM exits.
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

			navigateToLogin(page, username);
			page.locator("#username").fill(username);
			page.locator("#password").fill(password);
			page.locator("#kc-login").click(new Locator.ClickOptions().setTimeout(PLAYWRIGHT_ACTION_TIMEOUT_MS));

			page.waitForURL(url("/**"), new Page.WaitForURLOptions()
				.setWaitUntil(WaitUntilState.DOMCONTENTLOADED)
				.setTimeout(PLAYWRIGHT_NAVIGATION_TIMEOUT_MS));

			String bodyText = page.locator("body").textContent();
			if (!bodyText.contains("Sie sind angemeldet als " + username)) {
				throw new IllegalStateException("Login fuer " + username + " fehlgeschlagen. Body: " + bodyText);
			}
			return context.storageState();
		}
	}

	private void configureTimeouts(BrowserContext context) {
		context.setDefaultTimeout(PLAYWRIGHT_ACTION_TIMEOUT_MS);
		context.setDefaultNavigationTimeout(PLAYWRIGHT_NAVIGATION_TIMEOUT_MS);
	}

	private void navigateToLogin(Page page, String username) {
		RuntimeException lastFailure = null;
		for (int attempt = 1; attempt <= 2; attempt++) {
			try {
				page.navigate(url("/oauth2/authorization/keycloak"), new Page.NavigateOptions()
					.setWaitUntil(WaitUntilState.DOMCONTENTLOADED)
					.setTimeout(PLAYWRIGHT_NAVIGATION_TIMEOUT_MS));
				return;
			} catch (RuntimeException e) {
				lastFailure = e;
				System.out.println("Login navigation for " + username + " failed on attempt " + attempt + ": " + e.getMessage());
				page.waitForTimeout(1_000);
			}
		}
		throw lastFailure;
	}

	private static void startContainer(String name, org.testcontainers.containers.GenericContainer<?> container) {
		if (container.isRunning()) {
			System.out.println(Instant.now() + " " + name + " container already running.");
			return;
		}
		System.out.println(Instant.now() + " Starting " + name + " container.");
		timed(name + " container start", () -> {
			container.start();
			return null;
		});
	}

	private static <T> T timed(String label, java.util.function.Supplier<T> action) {
		long startedAt = System.nanoTime();
		try {
			return action.get();
		} finally {
			logDuration(label, startedAt);
		}
	}

	private static void logDuration(String label, long startedAt) {
		long millis = Duration.ofNanos(System.nanoTime() - startedAt).toMillis();
		System.out.println(label + " took " + millis + " ms.");
	}

	private static void registerShutdownHook() {
		if (SHUTDOWN_HOOK_REGISTERED.compareAndSet(false, true)) {
			Runtime.getRuntime().addShutdownHook(new Thread(() -> {
				if (browser != null && browser.isConnected()) {
					browser.close();
				}
				if (playwright != null) {
					playwright.close();
				}
			}));
		}
	}
}
