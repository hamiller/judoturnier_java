package de.sinnix.judoturnier.adapter.primary;

import com.github.jknack.handlebars.Options;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.NumberFormat;
import java.util.Locale;

public class HelperSource {
    private static final Logger logger = LogManager.getLogger(HelperSource.class);

    private static boolean istGleich(Object o1, Object o2) {
        return o1 == null ? o2 == null : o1.equals(o2);
    }

    public static String janein(Boolean value, Options options) {
        if (value) {
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

    public static String istLeer(Object[] params, Options options) {
        boolean istLeer = false;
        for (Object param : params) {
            if (param == null || "".equals(param)) {
                istLeer = true;
                break;
            }
        }
        return istLeer ? "leer" : "";
    }

//        hbs.registerHelper("wertungVorhanden", (Helper<Wertung>) (wertung, options) -> {
//            boolean hatWertung = wertung != null && (wertung.getKampfgeistWettkaempfer1() != null || wertung.getSieger() != null);
//            return hatWertung ? "vorhanden" : "";
//        });
//        hbs.registerHelper("vorherigesElement", (Helper<Object[]>) (items, options) -> {
//            for (int i = 0; i < items.length; i++) {
//                if (i > 0) {
//                    options.context.combine("previous", items[i - 1]);
//                } else {
//                    options.context.combine("previous", null);
//                }
//            }
//            return items;
//        });
//        hbs.registerHelper("gleichesElement", (element1, options) -> {
//            Object element2 = options.param(0);
//            return element1.equals(element2);
//        };

    public static String concat(Object[] args, Options options) {
        StringBuilder sb = new StringBuilder();
        for (Object arg : args) {
            sb.append(arg);
        }
        return sb.toString();
    }

}
