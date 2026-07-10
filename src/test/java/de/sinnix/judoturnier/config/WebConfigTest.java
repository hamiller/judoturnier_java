package de.sinnix.judoturnier.config;

import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

class WebConfigTest {

	@Test
	void htmlOnlyHandlebarsViewResolverRejectsContentNegotiationExtensions() {
		WebConfig.HtmlOnlyHandlebarsViewResolver viewResolver = new WebConfig.HtmlOnlyHandlebarsViewResolver();

		assertThat(viewResolver.canHandle("startseite", Locale.GERMANY)).isTrue();
		assertThat(viewResolver.canHandle("startseite.xml", Locale.GERMANY)).isFalse();
	}

	@Test
	void htmlOnlyHandlebarsViewResolverKeepsRedirectsAndForwardsAvailable() {
		WebConfig.HtmlOnlyHandlebarsViewResolver viewResolver = new WebConfig.HtmlOnlyHandlebarsViewResolver();

		assertThat(viewResolver.canHandle("redirect:/turnier/1.2.3", Locale.GERMANY)).isTrue();
		assertThat(viewResolver.canHandle("forward:/turnier/1.2.3", Locale.GERMANY)).isTrue();
	}
}
