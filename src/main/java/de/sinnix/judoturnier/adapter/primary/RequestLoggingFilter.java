package de.sinnix.judoturnier.adapter.primary;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.annotation.WebFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;


@WebFilter(urlPatterns = "/*")
public class RequestLoggingFilter implements Filter {

	private static final Logger logger = LogManager.getLogger(GewichtsklassenController.class);


	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// Initialisierung, falls erforderlich
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
		throws IOException, ServletException {
		if (request instanceof HttpServletRequest) {
			HttpServletRequest httpServletRequest = (HttpServletRequest) request;
			String requestURL = httpServletRequest.getRequestURL().toString();
			logger.debug("Requested URL: {}", requestURL);
		}

		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		// Aufräumarbeiten, falls erforderlich
	}
}
