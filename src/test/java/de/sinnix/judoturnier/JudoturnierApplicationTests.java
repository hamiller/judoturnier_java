package de.sinnix.judoturnier;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import de.sinnix.judoturnier.config.SecurityConfig;
import org.junit.jupiter.api.Test;

@SpringBootTest
@ActiveProfiles("test")
class JudoturnierApplicationTests {

	// prevent loading the security context
	@MockitoBean
	private SecurityConfig                securityConfig;
	@MockitoBean
	private ClientRegistrationRepository  clientRegistrationRepository;
	@MockitoBean
	private OAuth2AuthorizedClientManager authorizedClientManager;

	@Test
	void contextLoads() {
	}

}
