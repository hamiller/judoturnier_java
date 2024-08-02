package de.sinnix.judoturnier.adapter.primary;

import de.sinnix.judoturnier.model.Wettkaempfer;
import de.sinnix.judoturnier.model.Verein;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Comparator;
import java.util.function.Function;

public class WettkaempferSortierer {
	private static final Logger logger = LogManager.getLogger(WettkaempferSortierer.class);

	public static Comparator<Wettkaempfer> sortiere(String sortingHeader) {
		logger.debug("Sortiere nach {}", sortingHeader);
		if (sortingHeader != null) {
			return switch (sortingHeader.toLowerCase()) {
				case "name" -> Comparator.comparing(Wettkaempfer::name);
				case "verein" -> compareNullable(Wettkaempfer::verein, Verein::name);
				case "geschlecht" -> compareNullable(Wettkaempfer::geschlecht);
				case "altersklasse" -> compareNullable(Wettkaempfer::altersklasse);
				case "gewicht" -> compareNullable(Wettkaempfer::gewicht);
				case "geprueft" -> compareNullable(Wettkaempfer::checked);
				case "gedruckt" -> compareNullable(Wettkaempfer::printed);
				default -> Comparator.comparing(Wettkaempfer::name);
			};
		}
		return Comparator.comparing(Wettkaempfer::name);
	}

	private static <T, U extends Comparable<U>> Comparator<T> compareNullable(Function<T, U> getter) {
		return (o1, o2) -> {
			U value1 = getter.apply(o1);
			U value2 = getter.apply(o2);
			if (value1 == null && value2 == null) {
				return 0;
			} else if (value1 == null) {
				return 1;  // value1 nach value2 sortieren, wenn value1 null ist
			} else if (value2 == null) {
				return -1; // value1 vor value2 sortieren, wenn value2 null ist
			} else {
				return value1.compareTo(value2);
			}
		};
	}

	private static <T, R, U extends Comparable<U>> Comparator<T> compareNullable(Function<T, R> getter, Function<R, U> subGetter) {
		return (o1, o2) -> {
			R value1 = getter.apply(o1);
			R value2 = getter.apply(o2);
			if (value1 == null && value2 == null) {
				return 0;
			} else if (value1 == null) {
				return 1;
			} else if (value2 == null) {
				return -1;
			} else {
				U subValue1 = subGetter.apply(value1);
				U subValue2 = subGetter.apply(value2);
				return subValue1.compareTo(subValue2);
			}
		};
	}
}
