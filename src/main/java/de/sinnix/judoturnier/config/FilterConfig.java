package de.sinnix.judoturnier.config;


import de.sinnix.judoturnier.adapter.primary.RequestLoggingFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

	@Bean
	public FilterRegistrationBean<RequestLoggingFilter> loggingFilter(){
		FilterRegistrationBean<RequestLoggingFilter> registrationBean = new FilterRegistrationBean<>();

		registrationBean.setFilter(new RequestLoggingFilter());
		registrationBean.addUrlPatterns("/*");

		return registrationBean;
	}
}