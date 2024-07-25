package de.sinnix.judoturnier.config;

import com.github.jknack.handlebars.springmvc.HandlebarsViewResolver;
import de.sinnix.judoturnier.adapter.primary.HelperSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ViewResolver;

@ComponentScan
@Configuration
public class WebConfiguration {

	@Bean
	public ViewResolver handlebarsViewResolver() {
		HandlebarsViewResolver viewResolver = new HandlebarsViewResolver();
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
}
