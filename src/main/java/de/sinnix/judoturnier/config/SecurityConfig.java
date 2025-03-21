package de.sinnix.judoturnier.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	private static final Logger logger = LogManager.getLogger(SecurityConfig.class);

	@Bean
	public KeycloakLogoutHandler keycloakLogoutHandlerBean() {;
		return new KeycloakLogoutHandler();
	}

	@Bean
	public GrantedAuthoritiesMapper userAuthoritiesMapperBean() {
		return new GrantedAuthoritiesMapperImpl();
	}

	@Bean
	public SecurityFilterChain configure(HttpSecurity http) throws Exception {
		http
			.csrf(AbstractHttpConfigurer::disable);

		http
			.authorizeRequests(request -> request
				.requestMatchers("/css/**", "/js/**", "/assets/**").permitAll() // Statische Dateien erlauben
				.requestMatchers("/").permitAll()
				.requestMatchers("/kontakt").permitAll()
				.requestMatchers("/**").hasAnyAuthority("ROLE_ADMIN")
				.requestMatchers("/turnier/**").hasAnyAuthority("ROLE_ZUSCHAUER", "ROLE_ADMIN", "ROLE_TRAINER", "ROLE_KAMPFRICHTER")
				.requestMatchers("/turnier/*/wettkaempfer/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_TRAINER", "ROLE_KAMPFRICHTER")
				.requestMatchers("/turnier/*/vereine/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_TRAINER", "ROLE_KAMPFRICHTER")
				.requestMatchers("/turnier/*/gewichtsklassen/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_TRAINER", "ROLE_KAMPFRICHTER")
				.requestMatchers("/turnier/*/begegnungen/**").hasAnyAuthority("ROLE_ZUSCHAUER", "ROLE_ADMIN", "ROLE_TRAINER", "ROLE_KAMPFRICHTER")
				.requestMatchers("/turnier/*/einstellungen/**").hasAnyAuthority("ROLE_ADMIN")
				.anyRequest().authenticated());

		http.exceptionHandling(exceptionHandling -> exceptionHandling
			.accessDeniedHandler((request, response, accessDeniedException) -> {
				logger.warn("Access denied for request to {} with user {}: {}",
					request.getRequestURI(),
					request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : "anonymous",
					accessDeniedException.getMessage());
			}));

		http
			.oauth2Login(Customizer.withDefaults())
			.logout(logout -> logout
				.addLogoutHandler(keycloakLogoutHandlerBean())
				.invalidateHttpSession(true)
				.clearAuthentication(true)
				.deleteCookies("JSESSIONID")
				.logoutSuccessUrl("/"));

		return http.build();
	}

	static class GrantedAuthoritiesMapperImpl implements GrantedAuthoritiesMapper {

		@Override
		public Collection<? extends GrantedAuthority> mapAuthorities(Collection<? extends GrantedAuthority> authorities) {
			Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
			authorities.forEach(authority -> {
				if (OidcUserAuthority.class.isInstance(authority)) {
					final var oidcUserAuthority = (OidcUserAuthority) authority;
					mappedAuthorities.addAll(convert(oidcUserAuthority.getIdToken().getClaims()));
				}
				else if (OAuth2UserAuthority.class.isInstance(authority)) {
					logger.warn("Received OAuth2UserAuthority user authorities for authority: {}", authority);
				}
			});

			return mappedAuthorities;
		};

		private Collection<GrantedAuthority> convert(Map<String, Object> claims) {
			if (Objects.nonNull(claims)) {
				List<String> roles = (List<String>) claims.get("groups");
				logger.debug("converting groups to roles: {}", roles);
				if (Objects.nonNull(roles)) {
					return roles.stream().map(rn -> new SimpleGrantedAuthority("ROLE_" + rn.toUpperCase())).collect(Collectors.toList());
				}
			}
			return List.of();
		}
	}

	static class KeycloakLogoutHandler implements LogoutHandler {
		private final RestTemplate restTemplate = new RestTemplate();;

		@Override
		public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
			if (authentication == null) {
				return;
			}

			var user = (OidcUser) authentication.getPrincipal();
			String endSessionEndpoint = user.getIssuer() + "/protocol/openid-connect/logout";
			logger.debug("Logging out endpoint {}", endSessionEndpoint);
			UriComponentsBuilder builder = UriComponentsBuilder
				.fromUriString(endSessionEndpoint)
				.queryParam("id_token_hint", user.getIdToken().getTokenValue());

			ResponseEntity<String> logoutResponse = restTemplate.getForEntity(builder.toUriString(), String.class);
			if (logoutResponse.getStatusCode().is2xxSuccessful()) {
				logger.info("Successfulley logged out from Keycloak");
			} else {
				logger.error("Could not propagate logout to Keycloak");
			}
		}
	}
}
