package de.sinnix.judoturnier.adapter.primary;

import de.sinnix.judoturnier.model.OidcBenutzer;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class FehlerseiteHandler {
	private static final Logger logger = LogManager.getLogger(FehlerseiteHandler.class);

	// Allgemeine Ausnahmebehandlung
	@ExceptionHandler(Exception.class)
	public ModelAndView handleException(Exception ex) {
		logger.error("Ein Fehler ist aufgetreten", ex);
		ModelAndView mav = new ModelAndView("error");
		mav.addObject("fehlerart", "Allgemeiner Fehler");
		return mav;
	}

	// 404 Fehlerbehandlung
	@ExceptionHandler({NoHandlerFoundException.class, NoResourceFoundException.class})
	public ModelAndView handleNotFound(Exception ex, HttpServletRequest request) {
		String requestedUrl = request.getRequestURL().toString();
		logger.error("Die Seite konnte nicht gefunden werden: {} {}", requestedUrl, ex.getMessage());
		ModelAndView mav = new ModelAndView("error");
		mav.addObject("fehlerart", "Unbekannte Seite");
		return mav;
	}

	// 403 Fehlerbehandlung
	@ExceptionHandler(AccessDeniedException.class)
	public ModelAndView handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
		OidcBenutzer benutzer = HelperSource.extractOidcBenutzer(SecurityContextHolder.getContext().getAuthentication());
		String requestedUrl = request.getRequestURL().toString();

		logger.error("Keine Berechtigung für Seite {}, principal {} {}", requestedUrl, benutzer, ex.getMessage());
		ModelAndView mav = new ModelAndView("error");
		mav.addObject("fehlerart", "Zugriff verweigert");
		return mav;
	}

}
