package de.sinnix.judoturnier.adapter.primary;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

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
	@ExceptionHandler(NoHandlerFoundException.class)
	public ModelAndView handleNotFound(NoHandlerFoundException ex) {
		logger.error("Die Seite konnte nicht gefunden werden", ex);
		ModelAndView mav = new ModelAndView("error");
		mav.addObject("fehlerart", "Unbekannte Seite");
		return mav;
	}

	// 403 Fehlerbehandlung
	@ExceptionHandler(AccessDeniedException.class)
	public ModelAndView handleAccessDenied(AccessDeniedException ex) {
		logger.error("Keine Berechtigung", ex);
		ModelAndView mav = new ModelAndView("error");
		mav.addObject("fehlerart", "Zugriff verweigert");
		return mav;
	}

}
