package de.sinnix.judoturnier.fixtures;

import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Begegnung;
import de.sinnix.judoturnier.model.Matte;
import de.sinnix.judoturnier.model.Runde;
import de.sinnix.judoturnier.model.WettkampfGruppe;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MatteFixtures {
	private static final UUID            turnierUUID    = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
	private static final WettkampfGruppe WKG            = new WettkampfGruppe(1, "name", "typ", List.of(), turnierUUID);
	private static final UUID rundeUUID1 = UUID.randomUUID();
	private static final UUID rundeUUID2 = UUID.randomUUID();
	private static final UUID rundeUUID3 = UUID.randomUUID();
	private static final UUID rundeUUID4 = UUID.randomUUID();
	private static final UUID rundeUUID5 = UUID.randomUUID();
	public static final Map<Integer, Matte> matteList = Map.of(1,
		new Matte(1, Arrays.asList(
			new Runde(rundeUUID1, 1, 1, 1, 1, Altersklasse.U11, WKG, Arrays.asList(
				new Begegnung(1, new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1), rundeUUID1,1, 1, 1, 1, WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2, null, WKG, turnierUUID),
				new Begegnung(2, new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1), rundeUUID1,1, 1, 1, 2, WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2, null, WKG, turnierUUID),
				new Begegnung(3, new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1), rundeUUID1,1, 1, 1, 3, WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2, null, WKG, turnierUUID)
			)),
			new Runde(rundeUUID2, 2, 2, 2, 1, Altersklasse.U11, WKG, Arrays.asList(
				new Begegnung(4, new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1), rundeUUID2,1, 2, 2, 4, WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2, null, WKG, turnierUUID),
				new Begegnung(5, new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1), rundeUUID2,1, 2, 2, 5, WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2, null, WKG, turnierUUID),
				new Begegnung(6, new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1), rundeUUID2,1, 2, 2, 6, WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2, null, WKG, turnierUUID)
			)),
			new Runde(rundeUUID3, 3, 3, 3, 1, Altersklasse.U11, WKG, Arrays.asList(
				new Begegnung(7, new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1), rundeUUID3, 1, 3, 3, 7, WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2, null, WKG, turnierUUID),
				new Begegnung(8, new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1), rundeUUID3, 1, 3, 3, 8, WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2, null, WKG, turnierUUID),
				new Begegnung(9, new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1), rundeUUID3, 1, 3, 3, 9, WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2, null, WKG, turnierUUID)
			)),
			new Runde(rundeUUID4, 4, 4, 4, 1, Altersklasse.U11, WKG, Arrays.asList(
				new Begegnung(10, new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1), rundeUUID4,1, 4, 4, 10, WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2, null, WKG, turnierUUID),
				new Begegnung(11, new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1), rundeUUID4,1, 4, 4, 11, WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2, null, WKG, turnierUUID),
				new Begegnung(12, new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1), rundeUUID4,1, 4, 4, 12, WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2, null, WKG, turnierUUID)
			)),
			new Runde(rundeUUID5, 5, 5, 5, 1, Altersklasse.U11, WKG, Arrays.asList(
				new Begegnung(13, new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1), rundeUUID5,1, 5, 5, 13, WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2, null, WKG, turnierUUID),
				new Begegnung(14, new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1), rundeUUID5,1, 5, 5, 14, WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2, null, WKG, turnierUUID),
				new Begegnung(15, new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1), rundeUUID5,1, 5, 5, 15, WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2, null, WKG, turnierUUID)
			))
		))
	);

}
