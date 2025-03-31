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
	public static final UUID            turnierUUID = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
	public static final UUID            wkgUUID     = UUID.fromString("550e8401-e29b-41d4-a716-446655440001");
	public static final WettkampfGruppe WKG         = new WettkampfGruppe(wkgUUID, "name", "typ", Altersklasse.U11, turnierUUID);
	public static final UUID            rundeUUID1  = UUID.fromString("223e4567-e89b-12d3-a456-426614174000");
	public static final UUID            rundeUUID2  = UUID.fromString("223e4567-e89b-12d3-a456-426614174001");
	public static final UUID            rundeUUID3  = UUID.fromString("223e4567-e89b-12d3-a456-426614174002");
	public static final UUID            rundeUUID4  = UUID.fromString("223e4567-e89b-12d3-a456-426614174003");
	public static final UUID            rundeUUID5  = UUID.fromString("223e4567-e89b-12d3-a456-426614174004");
	public static final UUID            b1          = UUID.fromString("423e4567-e89b-12d3-a456-426614174036");
	public static final UUID            b2          = UUID.fromString("423e4567-e89b-12d3-a456-426614174037");
	public static final UUID            b3          = UUID.fromString("423e4567-e89b-12d3-a456-426614174038");
	public static final UUID            b4          = UUID.fromString("423e4567-e89b-12d3-a456-426614174039");
	public static final UUID            b5          = UUID.fromString("423e4567-e89b-12d3-a456-426614174040");
	public static final UUID            b6          = UUID.fromString("423e4567-e89b-12d3-a456-426614174041");
	public static final UUID            b7          = UUID.fromString("423e4567-e89b-12d3-a456-426614174042");
	public static final UUID            b8          = UUID.fromString("423e4567-e89b-12d3-a456-426614174043");
	public static final UUID            b9          = UUID.fromString("423e4567-e89b-12d3-a456-426614174044");
	public static final UUID            b10         = UUID.fromString("423e4567-e89b-12d3-a456-426614174045");
	public static final UUID            b11         = UUID.fromString("423e4567-e89b-12d3-a456-426614174046");
	public static final UUID            b12         = UUID.fromString("423e4567-e89b-12d3-a456-426614174047");
	public static final UUID            b13         = UUID.fromString("423e4567-e89b-12d3-a456-426614174048");
	public static final UUID            b14         = UUID.fromString("423e4567-e89b-12d3-a456-426614174049");
	public static final UUID            b15         = UUID.fromString("423e4567-e89b-12d3-a456-426614174050");


	public static final Map<Integer, Matte> matteList = Map.of(1,
		new Matte(1, Arrays.asList(
			new Runde(rundeUUID1, 1, 1, 1, 1, Altersklasse.U11, WKG, Arrays.asList(
				new Begegnung(b1, new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1), rundeUUID1, 1, 1, 1, 1, WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2, null, WKG, turnierUUID),
				new Begegnung(b2, new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1), rundeUUID1, 1, 1, 1, 2, WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2, null, WKG, turnierUUID),
				new Begegnung(b3, new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1), rundeUUID1, 1, 1, 1, 3, WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2, null, WKG, turnierUUID)
			)),
			new Runde(rundeUUID2, 2, 2, 2, 1, Altersklasse.U11, WKG, Arrays.asList(
				new Begegnung(b4, new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1), rundeUUID2, 1, 2, 2, 4, WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2, null, WKG, turnierUUID),
				new Begegnung(b5, new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1), rundeUUID2, 1, 2, 2, 5, WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2, null, WKG, turnierUUID),
				new Begegnung(b6, new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1), rundeUUID2, 1, 2, 2, 6, WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2, null, WKG, turnierUUID)
			)),
			new Runde(rundeUUID3, 3, 3, 3, 1, Altersklasse.U11, WKG, Arrays.asList(
				new Begegnung(b7, new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1), rundeUUID3, 1, 3, 3, 7, WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2, null, WKG, turnierUUID),
				new Begegnung(b8, new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1), rundeUUID3, 1, 3, 3, 8, WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2, null, WKG, turnierUUID),
				new Begegnung(b9, new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1), rundeUUID3, 1, 3, 3, 9, WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2, null, WKG, turnierUUID)
			)),
			new Runde(rundeUUID4, 4, 4, 4, 1, Altersklasse.U11, WKG, Arrays.asList(
				new Begegnung(b10, new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1), rundeUUID4, 1, 4, 4, 10, WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2, null, WKG, turnierUUID),
				new Begegnung(b11, new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1), rundeUUID4, 1, 4, 4, 11, WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2, null, WKG, turnierUUID),
				new Begegnung(b12, new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1), rundeUUID4, 1, 4, 4, 12, WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2, null, WKG, turnierUUID)
			)),
			new Runde(rundeUUID5, 5, 5, 5, 1, Altersklasse.U11, WKG, Arrays.asList(
				new Begegnung(b13, new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1), rundeUUID5, 1, 5, 5, 13, WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2, null, WKG, turnierUUID),
				new Begegnung(b14, new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1), rundeUUID5, 1, 5, 5, 14, WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2, null, WKG, turnierUUID),
				new Begegnung(b15, new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1), rundeUUID5, 1, 5, 5, 15, WettkaempferFixtures.wettkaempfer1, WettkaempferFixtures.wettkaempfer2, null, WKG, turnierUUID)
			))
		))
	);

}
