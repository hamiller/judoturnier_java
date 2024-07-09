package de.sinnix.judoturnier.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	private static final Logger logger = LogManager.getLogger(SecurityConfiguration.class);

	@Bean
	public SecurityFilterChain configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests(request -> request
				.requestMatchers("/turnier/**").permitAll()//hasRole("zuschauer")
				.requestMatchers("/").permitAll()
//				.requestMatchers("/test").hasRole("zuschauer")
//				.requestMatchers("/test2").hasRole("ZUSCHAUER2")
				.anyRequest().authenticated());

		http
			.oauth2Login(Customizer.withDefaults());
//			.logout(logout -> logout
//				.addLogoutHandler(keycloakLogoutHandler)
//				.logoutSuccessUrl("/"));

		return http.build();
	}

}
