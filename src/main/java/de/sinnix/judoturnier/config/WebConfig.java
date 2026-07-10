package de.sinnix.judoturnier.config;

import com.github.jknack.handlebars.springmvc.HandlebarsViewResolver;
import de.sinnix.judoturnier.adapter.primary.HelperSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import java.util.Locale;

@ComponentScan
@Configuration
public class WebConfig {

	@Bean
	public ViewResolver handlebarsViewResolver() {
		HandlebarsViewResolver viewResolver = new HtmlOnlyHandlebarsViewResolver();
		viewResolver.setOrder(1);
		viewResolver.setPrefix("classpath:templates/");
		viewResolver.setSuffix(".hbs");
		viewResolver.setCache(false);
		viewResolver.registerHelpers(HelperSource.class);
		return viewResolver;
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	static final class HtmlOnlyHandlebarsViewResolver extends HandlebarsViewResolver {
		@Override
		protected boolean canHandle(String viewName, Locale locale) {
			if (viewName.startsWith(UrlBasedViewResolver.REDIRECT_URL_PREFIX)
				|| viewName.startsWith(UrlBasedViewResolver.FORWARD_URL_PREFIX)) {
				return super.canHandle(viewName, locale);
			}
			return !viewName.contains(".") && super.canHandle(viewName, locale);
		}
	}
}
