package de.sinnix.judoturnier.adapter.primary;

import com.github.jknack.handlebars.Options;
import de.sinnix.judoturnier.model.Wertung;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class HelperSource {
	private static final Logger logger = LogManager.getLogger(HelperSource.class);

	public static boolean istGleich(Object o1, Object o2) {
		return o1 == null ? o2 == null : o1.equals(o2);
	}

	public static String janein(Boolean value, Options options) {
		if (value != null && value) {
			return "Ja";
		}
		return "Nein";

	}

	public static String setChecked(Object value, Options options) {
		if (istGleich(value, options.param(0))) {
			return "checked";
		}
		return "";

	}

	public static String istWarnung(Boolean isWarning, Options options) {
		if (isWarning) {
			return "warnung";
		}
		return "";
	}

	public static String setSelected(Object value, Options options) {
		if (istGleich(value, options.param(0))) {
			return "selected";
		}
		return "";
	}

	public static String formatNumber(Object value, Options options) {
		if (value == null) {
			return null;
		}

		try {
			double number = Double.parseDouble(value.toString());
			Locale locale = Locale.getDefault(); // or obtain the locale from some other source
			NumberFormat numberFormat = NumberFormat.getNumberInstance(locale);
			numberFormat.setMinimumFractionDigits(2);
			return numberFormat.format(number);
		} catch (NumberFormatException e) {
			return value.toString(); // or handle the error as you see fit
		}
	}

	public static String istLeer(Options options) {
		boolean istLeer = false;
		logger.trace("options: {}", options.params);
		for (Object param : options.params) {
			logger.trace("param: {}", param);
			if (param == null || "".equals(param)) {
				istLeer = true;
				break;
			}
		}
		return istLeer ? "leer" : "";
	}

	public static String wertungVorhanden(Wertung wertung, Options options) {
		return (wertung != null && (wertung.kampfgeistWettkaempfer1() != null || wertung.sieger() != null)) ? "vorhanden" : "";
	}

	public static List vorherigesElement(List items, Options options) {
		for (int i = 0; i < items.size(); i++) {
			if (i > 0) {
				options.context.combine("previous", items.get(i - 1));
			} else {
				options.context.combine("previous", null);
			}
		}
		return items;
	}

	public static String concat(Object[] args, Options options) {
		if (args == null) {
			logger.warn("args: {}, options: {}", args, options.params);
			return "";
		}
		logger.warn("args: {}, options: {}", args, options.params);
		StringBuilder sb = new StringBuilder();
		for (Object arg : args) {
			sb.append(arg);
		}
		return sb.toString();
	}

}
