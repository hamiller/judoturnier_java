package de.sinnix.judoturnier.config;

import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.AltersklasseGewicht;
import de.sinnix.judoturnier.model.Geschlecht;
import de.sinnix.judoturnier.model.GeschlechtAltersklasse;
import de.sinnix.judoturnier.model.Gewichtsklasse;

import java.util.List;

public class GewichtsklassenConfiguration {

	private static final Gewichtsklasse Bis27  = new Gewichtsklasse("Bis 27kg", 27d);
	private static final Gewichtsklasse Bis28  = new Gewichtsklasse("Bis 28kg", 28d);
	private static final Gewichtsklasse Bis30  = new Gewichtsklasse("Bis 30kg", 30d);
	private static final Gewichtsklasse Bis31  = new Gewichtsklasse("Bis 31kg", 31d);
	private static final Gewichtsklasse Bis33  = new Gewichtsklasse("Bis 33kg", 33d);
	private static final Gewichtsklasse Bis34  = new Gewichtsklasse("Bis 34kg", 34d);
	private static final Gewichtsklasse Bis36  = new Gewichtsklasse("Bis 36kg", 36d);
	private static final Gewichtsklasse Bis37  = new Gewichtsklasse("Bis 37kg", 37d);
	private static final Gewichtsklasse Bis40  = new Gewichtsklasse("Bis 40kg", 40d);
	private static final Gewichtsklasse Bis43  = new Gewichtsklasse("Bis 43kg", 43d);
	private static final Gewichtsklasse Bis44  = new Gewichtsklasse("Bis 44kg", 44d);
	private static final Gewichtsklasse Bis46  = new Gewichtsklasse("Bis 46kg", 46d);
	private static final Gewichtsklasse Bis48  = new Gewichtsklasse("Bis 48kg", 48d);
	private static final Gewichtsklasse Bis50  = new Gewichtsklasse("Bis 50kg", 50d);
	private static final Gewichtsklasse Bis52  = new Gewichtsklasse("Bis 52kg", 52d);
	private static final Gewichtsklasse Bis55  = new Gewichtsklasse("Bis 55kg", 55d);
	private static final Gewichtsklasse Bis57  = new Gewichtsklasse("Bis 57kg", 57d);
	private static final Gewichtsklasse Bis60  = new Gewichtsklasse("Bis 60kg", 60d);
	private static final Gewichtsklasse Bis63  = new Gewichtsklasse("Bis 63kg", 63d);
	private static final Gewichtsklasse Bis66  = new Gewichtsklasse("Bis 66kg", 66d);
	private static final Gewichtsklasse Bis70  = new Gewichtsklasse("Bis 70kg", 70d);
	private static final Gewichtsklasse Bis73  = new Gewichtsklasse("Bis 73kg", 73d);
	private static final Gewichtsklasse Bis78  = new Gewichtsklasse("Bis 78kg", 78d);
	private static final Gewichtsklasse Bis81  = new Gewichtsklasse("Bis 81kg", 81d);
	private static final Gewichtsklasse Bis90  = new Gewichtsklasse("Bis 90kg", 90d);
	private static final Gewichtsklasse Bis100 = new Gewichtsklasse("Bis 100kg", 100d);
	private static final Gewichtsklasse Mehr   = new Gewichtsklasse("Mehr", 1000d);

	private static final List<GeschlechtAltersklasse> CONFIG = List.of(
		new GeschlechtAltersklasse(
			Geschlecht.m,
			List.of(
				new AltersklasseGewicht(Altersklasse.U9, List.of(Mehr)),
				new AltersklasseGewicht(Altersklasse.U11, List.of(Mehr)),
				new AltersklasseGewicht(Altersklasse.U12, List.of(Mehr)),
				new AltersklasseGewicht(Altersklasse.U13, List.of(Bis28, Bis31, Bis34, Bis37, Bis40, Bis43, Bis46, Bis50, Bis55, Mehr)),
				new AltersklasseGewicht(Altersklasse.U15, List.of(Bis34, Bis37, Bis40, Bis43, Bis46, Bis50, Bis55, Bis60, Bis66, Mehr)),
				new AltersklasseGewicht(Altersklasse.U18, List.of(Bis46, Bis50, Bis55, Bis60, Bis66, Bis73, Bis81, Bis90, Mehr)),
				new AltersklasseGewicht(Altersklasse.U21, List.of(Bis60, Bis66, Bis73, Bis81, Bis90, Bis100, Mehr)),
				new AltersklasseGewicht(Altersklasse.Maenner, List.of(Bis60, Bis66, Bis73, Bis81, Bis90, Bis100, Mehr))
			)
		),
		new GeschlechtAltersklasse(
			Geschlecht.w,
			List.of(
				new AltersklasseGewicht(Altersklasse.U9, List.of(Mehr)),
				new AltersklasseGewicht(Altersklasse.U11, List.of(Mehr)),
				new AltersklasseGewicht(Altersklasse.U12, List.of(Mehr)),
				new AltersklasseGewicht(Altersklasse.U13, List.of(Bis27, Bis30, Bis33, Bis36, Bis40, Bis44, Bis48, Bis52, Bis57, Mehr)),
				new AltersklasseGewicht(Altersklasse.U15, List.of(Bis33, Bis36, Bis40, Bis44, Bis48, Bis52, Bis57, Bis63, Mehr)),
				new AltersklasseGewicht(Altersklasse.U18, List.of(Bis40, Bis44, Bis48, Bis52, Bis57, Bis63, Bis70, Bis78, Mehr)),
				new AltersklasseGewicht(Altersklasse.U21, List.of(Bis48, Bis52, Bis57, Bis63, Bis70, Bis78, Mehr)),
				new AltersklasseGewicht(Altersklasse.Frauen, List.of(Bis48, Bis52, Bis57, Bis63, Bis70, Bis78, Mehr))
			)
		)
	);

	public static List<Gewichtsklasse> getGewichtsklasse(Geschlecht geschlecht, Altersklasse alter) {
		return CONFIG.stream()
			.filter(c -> c.geschlecht().equals(geschlecht))
			.flatMap(c -> c.altersKlassenGewichte().stream())
			.filter(akg -> akg.altersklasse().equals(alter))
			.flatMap(ak -> ak.gewichtsKlassen().stream())
			.toList();
	}
}
