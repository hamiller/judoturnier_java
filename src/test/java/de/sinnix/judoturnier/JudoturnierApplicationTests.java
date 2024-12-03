package de.sinnix.judoturnier;

import de.sinnix.judoturnier.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class JudoturnierApplicationTests {

	// prevent loading the security context
	@MockBean
	private SecurityConfig               securityConfig;
	@MockBean
	private ClientRegistrationRepository clientRegistrationRepository;
	@MockBean
	private OAuth2AuthorizedClientManager authorizedClientManager;

	@Test
	void contextLoads() {
	}

}
