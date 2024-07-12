package de.sinnix.judoturnier.adapter.primary;

import com.github.jknack.handlebars.Options;
import de.sinnix.judoturnier.model.Runde;
import de.sinnix.judoturnier.model.Wertung;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class HelperSource {
	private static final Logger logger = LogManager.getLogger(HelperSource.class);

	public static boolean istGleich(Object o1, Object o2) {
		logger.trace("istGleich {} == {}", o1, o2);
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

	public static String wertungVorhanden(List<Wertung> wertung, Options options) {
		return (wertung != null && wertung.size() > 0 && (wertung.get(0).getKampfgeistWettkaempfer1() != null || wertung.get(0).getSieger() != null)) ? "vorhanden" : "";
	}

	public static List<ImmutablePair<Runde, Runde>> vorherigeRunde(List<Runde> runden, Options options) {
		List<ImmutablePair<Runde, Runde>> result = new ArrayList<>();
		for (int i = 0; i < runden.size(); i++) {
			Runde vorherigeRunde = i > 0 ? runden.get(i - 1) : null;
			Runde runde = runden.get(i);
			result.add(new ImmutablePair(vorherigeRunde, runde));
		}
		return result;
	}

	public static String combineString(Object arg1, Object arg2, Options options) {
		if (arg1 == null && arg2 == null) {
			logger.trace("combineString args: {} {}, options: {}", arg1, arg2, options.params);
			return "";
		}
		if (arg1 == null) {
			return arg2.toString();
		}
		if (arg2 == null) {
			return arg1.toString();
		}

		logger.trace("combineString args: {} {}, options: {}", arg1, arg2, options.params);
		StringBuilder sb = new StringBuilder();
		sb.append(arg1);
		sb.append(arg2);
		return sb.toString();
	}

	public static Object optional(Optional<?> optional, Options options) throws IOException {
		logger.trace("Checking for optional... {}", optional);
		if (optional.isPresent()) {
			return options.fn(optional.get());
		} else {
			return options.inverse();
		}
	}

	public static String disable(Boolean isEditable, Options options) {
		logger.trace("isEditable {}", isEditable);
		return isEditable ? "" : "disabled";
	}

	public static String truncate(String text, Integer size, Options options) {
		if (StringUtils.isBlank(text)) {
			return "";
		}

		return text.substring(0, size);
	}
}
