package de.sinnix.judoturnier.adapter.primary;

import java.util.List;
import java.util.stream.Collectors;


import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Matte;
import org.apache.commons.lang3.StringUtils;

public class MattenFilterer {
	public static List<Matte> filtereMatten(String altersklasse, List<Matte> wettkampfreihenfolgeJeMatte) {
		if (!StringUtils.isBlank(altersklasse)) {
			var altersKlasse = Altersklasse.valueOf(altersklasse);
			// Filtern der Liste nach Altersklasse "U11" und Erstellen der neuen Liste von Matten
			return wettkampfreihenfolgeJeMatte.stream()
				.map(matte -> new Matte(
					matte.id(),
					matte.runden().stream()
						.filter(runde -> altersKlasse.equals(runde.altersklasse()))
						.collect(Collectors.toList())
				))
				.filter(matte -> !matte.runden().isEmpty())
				.collect(Collectors.toList());
		}
		return wettkampfreihenfolgeJeMatte;
	}
}
