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
import java.util.Collections;


import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import jakarta.transaction.Transactional;
import org.apache.http.client.utils.URIBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("componenttest")
@Transactional
public abstract class AbstractIntegrationTest {

	@LocalServerPort
	int port;
	static               Playwright             playwright;
	static               Browser                browser;
	static               PostgreSQLContainer<?> postgres      = new PostgreSQLContainer<>("postgres:17").withReuse(true);
	static               KeycloakContainer      keycloak      = new KeycloakContainer().withRealmImportFile("/judoturnier-realm.json").withReuse(true);
	private static final String                 CLIENT_SECRET = "the-client-secret";


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
}
