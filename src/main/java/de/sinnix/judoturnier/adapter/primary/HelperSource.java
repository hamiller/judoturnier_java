package de.sinnix.judoturnier.adapter.primary;

import com.github.jknack.handlebars.Options;
import de.sinnix.judoturnier.model.Benutzer;
import de.sinnix.judoturnier.model.Runde;
import de.sinnix.judoturnier.model.Wertung;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class HelperSource {
	private static final Logger logger = LogManager.getLogger(HelperSource.class);

	public static boolean istGleich(Object o1, Object o2) {
		logger.trace("istGleich {} == {}", o1, o2);
		return o1 == null ? o2 == null : o1.equals(o2);
	}

	public static String ifCond(Object v1, Object v2, String trueResult, String falseResult) {
		if (v1 == null && v2 == null) {
			return trueResult;
		}
		if (v1 != null && v1.equals(v2)) {
			return trueResult;
		}
		return falseResult;
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

	public static String istLeer(Object firstObject, Options options) {
		List<Object> allObjects = new ArrayList<>();
		allObjects.add(firstObject);
		allObjects.addAll(Arrays.asList(options.params));
		for (Object param : allObjects) {
			if (param == null || "".equals(param)) {
				return "leer";
			}

			if (param instanceof Double) {
				Double d = (Double) param;
				if (d == 0.0) {
					return "leer_zahl";
				}
			}
		}
		return "";
	}

	public static String wertungVorhanden(List<Wertung> wertung, Options options) {
		return (wertung != null && wertung.size() > 0 && (wertung.get(0).getKampfgeistWettkaempfer1() != null || wertung.get(0).getSieger() != null)) ? "vorhanden" : "";
	}

	public static List<ImmutablePair<RundeDto, RundeDto>> vorherigeRunde(List<RundeDto> runden, Options options) {
		List<ImmutablePair<RundeDto, RundeDto>> result = new ArrayList<>();
		if (runden != null) {
			for (int i = 0; i < runden.size(); i++) {
				RundeDto vorherigeRunde = i > 0 ? runden.get(i - 1) : null;
				RundeDto runde = runden.get(i);
				result.add(new ImmutablePair(vorherigeRunde, runde));
			}
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
		if (optional == null || !optional.isPresent()) {
			return "";
		}
		return optional.get();
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

	public static Benutzer extractBewerter(Authentication authentication) {
		try {
			if (authentication instanceof AnonymousAuthenticationToken) {
				AnonymousAuthenticationToken token = (AnonymousAuthenticationToken) authentication;
				var principal = (String) token.getPrincipal();
				Collection<? extends GrantedAuthority> authorities = token.getAuthorities();
				List<String> rollen = new ArrayList<>();
				for (GrantedAuthority authority : authorities) {
					rollen.add(authority.getAuthority());
				}
				return new Benutzer(principal, "", "", rollen);
			}

			if (authentication.getPrincipal() instanceof DefaultOidcUser) {
				var principal = (DefaultOidcUser) authentication.getPrincipal();
				var userid = principal.getUserInfo().getSubject();
				var username = principal.getUserInfo().getPreferredUsername();
				var fullname = principal.getUserInfo().getFullName();
				var rollen = authentication.getAuthorities().stream().map(a -> a.getAuthority()).toList();

				Benutzer result = new Benutzer(userid, username, fullname, rollen);
				logger.info("Eingeloggter User {}", result);
				return result;
			}

			logger.warn("Konnte keinen Bewerter aus dem Authentication parsen, erstelle Dummy");
			return new Benutzer("dummy", "", "", List.of());
		}
		catch (Exception e) {
			logger.info("Nutzer konnte nicht geparsed werden! {}", authentication, e);
			throw e;
		}
	}
}
