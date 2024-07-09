package de.sinnix.judoturnier.config;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
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
public class SecurityConfiguration {

	private static final Logger logger = LogManager.getLogger(SecurityConfiguration.class);

	@Bean
	public SecurityFilterChain configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests(request -> request
				.requestMatchers("/turnier/**").permitAll()//hasRole("zuschauer")
				.requestMatchers("/").permitAll()
				.anyRequest().authenticated());

		http
			.oauth2Login(Customizer.withDefaults());
//			.logout(logout -> logout
//				.logoutSuccessUrl("/"));

		return http.build();
	}

	@Component
	@RequiredArgsConstructor
	static class GrantedAuthoritiesMapperImpl implements GrantedAuthoritiesMapper {

		@Override
		public Collection<? extends GrantedAuthority> mapAuthorities(Collection<? extends GrantedAuthority> authorities) {
			Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
			authorities.forEach(authority -> {
				if (OidcUserAuthority.class.isInstance(authority)) {
					final var oidcUserAuthority = (OidcUserAuthority) authority;
					mappedAuthorities.addAll(convert(oidcUserAuthority.getIdToken().getClaims()));
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
}
