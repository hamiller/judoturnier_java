package de.sinnix.judoturnier.config;

import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class SecurityConfigTest {

	@Test
	void keycloakRealmAccessRollenWerdenAlsAuthoritiesGemappt() {
		SecurityConfig.GrantedAuthoritiesMapperImpl mapper = new SecurityConfig.GrantedAuthoritiesMapperImpl();
		OidcIdToken idToken = new OidcIdToken(
			"token",
			Instant.now(),
			Instant.now().plusSeconds(60),
			Map.of(
				"sub", "24b37104-ba6d-48f1-9953-d577b759812a",
				"realm_access", Map.of("roles", List.of("kampfrichter", "admin"))
			)
		);

		var authorities = mapper.mapAuthorities(List.of(new OidcUserAuthority(idToken)));

		assertThat(authorities)
			.extracting("authority")
			.contains("ROLE_KAMPFRICHTER", "ROLE_ADMIN");
	}
}
