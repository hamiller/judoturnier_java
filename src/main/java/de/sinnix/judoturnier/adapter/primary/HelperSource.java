package de.sinnix.judoturnier.adapter.primary;

import com.github.jknack.handlebars.Options;
import de.sinnix.judoturnier.adapter.primary.dto.BegegnungDto;
import de.sinnix.judoturnier.adapter.primary.dto.GruppeTypenRundenDto;
import de.sinnix.judoturnier.adapter.primary.dto.RundeDto;
import de.sinnix.judoturnier.adapter.primary.dto.WertungDto;
import de.sinnix.judoturnier.application.CodeGeneratorService;
import de.sinnix.judoturnier.model.Benutzer;
import de.sinnix.judoturnier.model.BenutzerRolle;
import de.sinnix.judoturnier.model.OidcBenutzer;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class HelperSource {
	private static final Logger           logger     = LogManager.getLogger(HelperSource.class);
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

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
		logger.trace("setChecked {}", options.params);
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

	public static String formatDatum(Object value, Options options) {
		if (value == null) {
			return null;
		}

		try {
			Date date = (Date) value;
			return dateFormat.format(date);
		} catch (Exception e) {
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

	public static String wertungVorhanden(List<WertungDto> wertung, Options options) {
		return (wertung != null && wertung.size() > 0 && (wertung.get(0).kampfgeistWettkaempfer1() != null || wertung.get(0).sieger() != null)) ? "vorhanden" : "";
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
		logger.trace("return " + optional.get());
		return optional.get();
	}

	public static Object optionalFn(Optional<?> optional, Options options) throws IOException {
		logger.trace("Checking for optional... {}", optional);
		if (optional == null || !optional.isPresent()) {
			return "";
		}
		logger.trace("return " + optional.get());
		return options.fn(optional.get());
	}

	public static String disable(Boolean isEditable, Options options) {
		logger.trace("isEditable {}", isEditable);
		return isEditable ? "" : "disabled";
	}

	public static String truncate(String text, Integer size, Options options) {
		if (StringUtils.isBlank(text)) {
			return "";
		}

		return text.length() <= size ? text : text.substring(0, size);
	}

	public static OidcBenutzer extractOidcBenutzer(Authentication authentication) {
		try {
			if (authentication.getPrincipal() instanceof DefaultOidcUser) {
				var principal = (DefaultOidcUser) authentication.getPrincipal();
				var userid = principal.getUserInfo().getSubject();
				var username = principal.getUserInfo().getPreferredUsername();
				var fullname = principal.getUserInfo().getFullName();
				Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
				List<BenutzerRolle> rollen = new ArrayList<>();
				for (GrantedAuthority authority : authorities) {
					var r = BenutzerRolle.fromString(authority.getAuthority());
					if (r != null) rollen.add(r);
				}

				OidcBenutzer result = new OidcBenutzer(UUID.fromString(userid), username, fullname, rollen);
				logger.info("Eingeloggter User {}", result);
				return result;
			}

			logger.warn("Konnte keinen Nutzer aus dem Authentication parsen, erstelle Dummy");
			return new OidcBenutzer(UUID.randomUUID(), Benutzer.ANONYMOUS_USERNAME, "", List.of(BenutzerRolle.BEOBACHTER));
		} catch (Exception e) {
			logger.info("Nutzer konnte nicht geparsed werden! {}", authentication, e);
			throw e;
		}
	}

	public static boolean isValidUUID(String str) {
		try {
			UUID.fromString(str);
			return true; // Wenn kein Fehler, ist es eine gültige UUID
		} catch (IllegalArgumentException e) {
			return false; // Wenn ein Fehler auftritt, ist es keine UUID
		}
	}

	public static String lowerCase(String value) {
		return value.toLowerCase();
	}

	public static Integer zeit(BegegnungDto begegnungDto) {
		logger.trace("BegegnungDto? {}", begegnungDto);
		return begegnungDto.kampfrichterWertung().isPresent() ? begegnungDto.kampfrichterWertung().get().zeit().intValue() : -1;
	}

	public static List<GruppeTypenRundenDto> gruppeTypenRundenDtoListFromMatte(Map<Integer, List<GruppeTypenRundenDto>> gruppenListMitTypen, Options options) {
		Integer matte = options.param(0);
		var r = gruppenListMitTypen.get(matte);
		return r;
	}

	public static String calculateRundenCode(UUID rundenId, Options options) {
		return CodeGeneratorService.generateCode(rundenId);
	}
}
