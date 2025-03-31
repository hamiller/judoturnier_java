package de.sinnix.judoturnier.fixtures;

import de.sinnix.judoturnier.adapter.secondary.GewichtsklassenJpa;
import de.sinnix.judoturnier.adapter.secondary.VereinJpa;
import de.sinnix.judoturnier.adapter.secondary.WettkaempferJpa;
import de.sinnix.judoturnier.model.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class GewichtsklassenGruppeFixture {
    private static final UUID turnierUUID = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
    private static final UUID v1 = UUID.fromString("323e4567-e89b-12d3-a456-426614174000");
    private static final UUID v2 = UUID.fromString("323e4567-e89b-12d3-a456-426614174001");
    private static final UUID v3 = UUID.fromString("323e4567-e89b-12d3-a456-426614174002");
    private static final UUID v4 = UUID.fromString("323e4567-e89b-12d3-a456-426614174003");
    private static final UUID v5 = UUID.fromString("323e4567-e89b-12d3-a456-426614174004");

    private static final UUID w1 = UUID.fromString("323e4567-e89b-12d3-a456-426614174005");
    private static final UUID w2 = UUID.fromString("323e4567-e89b-12d3-a456-426614174006");
    private static final UUID w3 = UUID.fromString("323e4567-e89b-12d3-a456-426614174007");
    private static final UUID w4 = UUID.fromString("323e4567-e89b-12d3-a456-426614174008");
    private static final UUID w5 = UUID.fromString("323e4567-e89b-12d3-a456-426614174009");
    private static final UUID w6 = UUID.fromString("323e4567-e89b-12d3-a456-426614174010");
    private static final UUID w7 = UUID.fromString("323e4567-e89b-12d3-a456-426614174011");
    private static final UUID w8 = UUID.fromString("323e4567-e89b-12d3-a456-426614174012");
    private static final UUID w9 = UUID.fromString("323e4567-e89b-12d3-a456-426614174013");
    private static final UUID w10 = UUID.fromString("323e4567-e89b-12d3-a456-426614174014");
    private static final UUID w11 = UUID.fromString("323e4567-e89b-12d3-a456-426614174015");
    private static final UUID w12 = UUID.fromString("323e4567-e89b-12d3-a456-426614174016");
    private static final UUID w13 = UUID.fromString("323e4567-e89b-12d3-a456-426614174017");
    private static final UUID w14 = UUID.fromString("323e4567-e89b-12d3-a456-426614174018");
    private static final UUID w15 = UUID.fromString("323e4567-e89b-12d3-a456-426614174019");
    private static final UUID w16 = UUID.fromString("323e4567-e89b-12d3-a456-426614174020");
    private static final UUID w17 = UUID.fromString("323e4567-e89b-12d3-a456-426614174021");
    private static final UUID w18 = UUID.fromString("323e4567-e89b-12d3-a456-426614174022");
    private static final UUID w19 = UUID.fromString("323e4567-e89b-12d3-a456-426614174023");
    private static final UUID w20 = UUID.fromString("323e4567-e89b-12d3-a456-426614174024");
    private static final UUID w21 = UUID.fromString("323e4567-e89b-12d3-a456-426614174025");
    private static final UUID w22 = UUID.fromString("323e4567-e89b-12d3-a456-426614174026");
    private static final UUID w23 = UUID.fromString("323e4567-e89b-12d3-a456-426614174027");
    private static final UUID w24 = UUID.fromString("323e4567-e89b-12d3-a456-426614174028");
    private static final UUID w25 = UUID.fromString("323e4567-e89b-12d3-a456-426614174029");

	public static final  UUID gwkg1 = UUID.fromString("323e4567-e89b-12d3-a456-426614174030");
	private static final UUID gwkg2 = UUID.fromString("323e4567-e89b-12d3-a456-426614174031");
    private static final UUID gwkg3 = UUID.fromString("323e4567-e89b-12d3-a456-426614174032");
    private static final UUID gwkg4 = UUID.fromString("323e4567-e89b-12d3-a456-426614174033");
    private static final UUID gwkg5 = UUID.fromString("323e4567-e89b-12d3-a456-426614174034");





    public static List<GewichtsklassenGruppe> gewichtsklassenGruppen = Arrays.asList(
            new GewichtsklassenGruppe(gwkg1, Altersklasse.U11, Optional.of(Geschlecht.m), Arrays.asList(
                    new Wettkaempfer(w1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(v1, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.BLAU), true, true, turnierUUID),
                    new Wettkaempfer(w2, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(v2, "Verein2", turnierUUID), 24.5, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                    new Wettkaempfer(w3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(v3, "Verein3", turnierUUID), 23.8, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
                    new Wettkaempfer(w4, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(v4, "Verein4", turnierUUID), 24.2, Optional.of(Farbe.GELB), true, true, turnierUUID),
                    new Wettkaempfer(w5, "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new Verein(v5, "Verein5", turnierUUID), 24.4, Optional.of(Farbe.SCHWARZ), true, true, turnierUUID),
                    new Wettkaempfer(w6, "Teilnehmer F", Geschlecht.w, Altersklasse.U11, new Verein(v2, "Verein2", turnierUUID), 24.3, Optional.of(Farbe.WEISS), true, true, turnierUUID)
            ), Optional.of(RandoriGruppenName.Antilope), 23.8, 24.5, turnierUUID),
            new GewichtsklassenGruppe(gwkg2, Altersklasse.U11, Optional.of(Geschlecht.m), Arrays.asList(
                    new Wettkaempfer(w7, "Teilnehmer G", Geschlecht.w, Altersklasse.U11, new Verein(v3, "Verein3", turnierUUID), 25.0, Optional.of(Farbe.BLAU), true, true, turnierUUID),
                    new Wettkaempfer(w8, "Teilnehmer H", Geschlecht.m, Altersklasse.U11, new Verein(v4, "Verein4", turnierUUID), 25.2, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                    new Wettkaempfer(w9, "Teilnehmer I", Geschlecht.w, Altersklasse.U11, new Verein(v1, "Verein1", turnierUUID), 24.9, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
                    new Wettkaempfer(w10, "Teilnehmer J", Geschlecht.m, Altersklasse.U11, new Verein(v5, "Verein5", turnierUUID), 25.1, Optional.of(Farbe.GELB), true, true, turnierUUID),
                    new Wettkaempfer(w11, "Teilnehmer K", Geschlecht.w, Altersklasse.U11, new Verein(v3, "Verein3", turnierUUID), 25.3, Optional.of(Farbe.SCHWARZ), true, true, turnierUUID),
                    new Wettkaempfer(w12, "Teilnehmer L", Geschlecht.m, Altersklasse.U11, new Verein(v2, "Verein2", turnierUUID), 25.4, Optional.of(Farbe.WEISS), true, true, turnierUUID)
            ), Optional.of(RandoriGruppenName.Eule), 25.8, 24.8, turnierUUID),
            new GewichtsklassenGruppe(gwkg3, Altersklasse.U11, Optional.of(Geschlecht.m), Arrays.asList(
                    new Wettkaempfer(w13, "Teilnehmer M", Geschlecht.w, Altersklasse.U11, new Verein(v4, "Verein4", turnierUUID), 26.0, Optional.of(Farbe.BLAU), true, true, turnierUUID),
                    new Wettkaempfer(w14, "Teilnehmer N", Geschlecht.m, Altersklasse.U11, new Verein(v5, "Verein5", turnierUUID), 26.2, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                    new Wettkaempfer(w15, "Teilnehmer O", Geschlecht.m, Altersklasse.U11, new Verein(v1, "Verein1", turnierUUID), 26.1, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
                    new Wettkaempfer(w16, "Teilnehmer P", Geschlecht.w, Altersklasse.U11, new Verein(v3, "Verein3", turnierUUID), 26.4, Optional.of(Farbe.GELB), true, true, turnierUUID),
                    new Wettkaempfer(w17, "Teilnehmer Q", Geschlecht.m, Altersklasse.U11, new Verein(v2, "Verein2", turnierUUID), 26.3, Optional.of(Farbe.SCHWARZ), true, true, turnierUUID),
                    new Wettkaempfer(w18, "Teilnehmer R", Geschlecht.w, Altersklasse.U11, new Verein(v1, "Verein1", turnierUUID), 26.5, Optional.of(Farbe.WEISS), true, true, turnierUUID)
            ), Optional.of(RandoriGruppenName.Katze), 26.8, 25.8, turnierUUID),
            new GewichtsklassenGruppe(gwkg4, Altersklasse.U11, Optional.of(Geschlecht.m), Arrays.asList(
                    new Wettkaempfer(w19, "Teilnehmer S", Geschlecht.m, Altersklasse.U11, new Verein(v4, "Verein4", turnierUUID), 27.0, Optional.of(Farbe.BLAU), true, true, turnierUUID),
                    new Wettkaempfer(w20, "Teilnehmer T", Geschlecht.w, Altersklasse.U11, new Verein(v5, "Verein5", turnierUUID), 27.2, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                    new Wettkaempfer(w21, "Teilnehmer U", Geschlecht.m, Altersklasse.U11, new Verein(v2, "Verein2", turnierUUID), 27.1, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
                    new Wettkaempfer(w22, "Teilnehmer V", Geschlecht.w, Altersklasse.U11, new Verein(v3, "Verein3", turnierUUID), 27.3, Optional.of(Farbe.GELB), true, true, turnierUUID)
            ), Optional.of(RandoriGruppenName.Maus), 27.8, 26.8, turnierUUID),
            new GewichtsklassenGruppe(gwkg5, Altersklasse.U11, Optional.of(Geschlecht.m), Arrays.asList(
                    new Wettkaempfer(w23, "Teilnehmer W", Geschlecht.m, Altersklasse.U11, new Verein(v1, "Verein1", turnierUUID), 28.0, Optional.of(Farbe.BLAU), true, true, turnierUUID),
                    new Wettkaempfer(w24, "Teilnehmer X", Geschlecht.w, Altersklasse.U11, new Verein(v4, "Verein4", turnierUUID), 28.2, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                    new Wettkaempfer(w25, "Teilnehmer Y", Geschlecht.m, Altersklasse.U11, new Verein(v5, "Verein5", turnierUUID), 28.1, Optional.of(Farbe.GRUEN), true, true, turnierUUID)
            ), Optional.of(RandoriGruppenName.Tiger), 28.0, 28.2, turnierUUID)
    );

	public static List<GewichtsklassenGruppe> gewichtsklassenGruppenFrauen3 = Arrays.asList(
		new GewichtsklassenGruppe(gwkg1, Altersklasse.Frauen, Optional.of(Geschlecht.w), Arrays.asList(
			WettkaempferFixtures.wettkaempferin1,
			WettkaempferFixtures.wettkaempferin2,
			WettkaempferFixtures.wettkaempferin3,
			WettkaempferFixtures.wettkaempferin4,
			WettkaempferFixtures.wettkaempferin5,
			WettkaempferFixtures.wettkaempferin6,
			WettkaempferFixtures.wettkaempferin7
		), Optional.of(RandoriGruppenName.Antilope), 58.0, 62.0, turnierUUID),
		new GewichtsklassenGruppe(gwkg2, Altersklasse.Frauen, Optional.of(Geschlecht.w), Arrays.asList(
			WettkaempferFixtures.wettkaempferin8,
			WettkaempferFixtures.wettkaempferin9
		), Optional.of(RandoriGruppenName.Tiger), 47.0, 47.6, turnierUUID),
		new GewichtsklassenGruppe(gwkg3, Altersklasse.Frauen, Optional.of(Geschlecht.w), Arrays.asList(
			WettkaempferFixtures.wettkaempferin10,
			WettkaempferFixtures.wettkaempferin11
		), Optional.of(RandoriGruppenName.Katze), 53.0, 54.0, turnierUUID)
	);

    public static List<GewichtsklassenGruppe> gewichtsklassenGruppenZweiAltersklassen = Arrays.asList(
        new GewichtsklassenGruppe(gwkg1, Altersklasse.U11, Optional.of(Geschlecht.m), Arrays.asList(
            new Wettkaempfer(w1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(v1, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.BLAU), true, true, turnierUUID),
            new Wettkaempfer(w2, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(v2, "Verein2", turnierUUID), 24.5, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
            new Wettkaempfer(w3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(v3, "Verein3", turnierUUID), 23.8, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
            new Wettkaempfer(w4, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(v4, "Verein4", turnierUUID), 24.2, Optional.of(Farbe.GELB), true, true, turnierUUID),
            new Wettkaempfer(w5, "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new Verein(v5, "Verein5", turnierUUID), 24.4, Optional.of(Farbe.SCHWARZ), true, true, turnierUUID),
            new Wettkaempfer(w6, "Teilnehmer F", Geschlecht.w, Altersklasse.U11, new Verein(v2, "Verein2", turnierUUID), 24.3, Optional.of(Farbe.WEISS), true, true, turnierUUID)
        ), Optional.of(RandoriGruppenName.Antilope), 23.8, 24.5, turnierUUID),
        new GewichtsklassenGruppe(gwkg2, Altersklasse.U11, Optional.of(Geschlecht.m), Arrays.asList(
            new Wettkaempfer(w7, "Teilnehmer G", Geschlecht.w, Altersklasse.U11, new Verein(v3, "Verein3", turnierUUID), 25.0, Optional.of(Farbe.BLAU), true, true, turnierUUID),
            new Wettkaempfer(w8, "Teilnehmer H", Geschlecht.m, Altersklasse.U11, new Verein(v4, "Verein4", turnierUUID), 25.2, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
            new Wettkaempfer(w9, "Teilnehmer I", Geschlecht.w, Altersklasse.U11, new Verein(v1, "Verein1", turnierUUID), 24.9, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
            new Wettkaempfer(w10, "Teilnehmer J", Geschlecht.m, Altersklasse.U11, new Verein(v5, "Verein5", turnierUUID), 25.1, Optional.of(Farbe.GELB), true, true, turnierUUID),
            new Wettkaempfer(w11, "Teilnehmer K", Geschlecht.w, Altersklasse.U11, new Verein(v3, "Verein3", turnierUUID), 25.3, Optional.of(Farbe.SCHWARZ), true, true, turnierUUID),
            new Wettkaempfer(w12, "Teilnehmer L", Geschlecht.m, Altersklasse.U11, new Verein(v2, "Verein2", turnierUUID), 25.4, Optional.of(Farbe.WEISS), true, true, turnierUUID)
        ), Optional.of(RandoriGruppenName.Eule), 25.8, 24.8, turnierUUID),
        new GewichtsklassenGruppe(gwkg3, Altersklasse.U11, Optional.of(Geschlecht.m), Arrays.asList(
            new Wettkaempfer(w13, "Teilnehmer M", Geschlecht.w, Altersklasse.U11, new Verein(v4, "Verein4", turnierUUID), 26.0, Optional.of(Farbe.BLAU), true, true, turnierUUID),
            new Wettkaempfer(w14, "Teilnehmer N", Geschlecht.m, Altersklasse.U11, new Verein(v5, "Verein5", turnierUUID), 26.2, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
            new Wettkaempfer(w15, "Teilnehmer O", Geschlecht.m, Altersklasse.U11, new Verein(v1, "Verein1", turnierUUID), 26.1, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
            new Wettkaempfer(w16, "Teilnehmer P", Geschlecht.w, Altersklasse.U11, new Verein(v3, "Verein3", turnierUUID), 26.4, Optional.of(Farbe.GELB), true, true, turnierUUID),
            new Wettkaempfer(w17, "Teilnehmer Q", Geschlecht.m, Altersklasse.U11, new Verein(v2, "Verein2", turnierUUID), 26.3, Optional.of(Farbe.SCHWARZ), true, true, turnierUUID),
            new Wettkaempfer(w18, "Teilnehmer R", Geschlecht.w, Altersklasse.U11, new Verein(v1, "Verein1", turnierUUID), 26.5, Optional.of(Farbe.WEISS), true, true, turnierUUID)
        ), Optional.of(RandoriGruppenName.Katze), 26.8, 25.8, turnierUUID),
        new GewichtsklassenGruppe(gwkg4, Altersklasse.U13, Optional.of(Geschlecht.m), Arrays.asList(
            new Wettkaempfer(w19, "Teilnehmer S", Geschlecht.m, Altersklasse.U13, new Verein(v4, "Verein4", turnierUUID), 27.0, Optional.of(Farbe.BLAU), true, true, turnierUUID),
            new Wettkaempfer(w20, "Teilnehmer T", Geschlecht.w, Altersklasse.U13, new Verein(v5, "Verein5", turnierUUID), 27.2, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
            new Wettkaempfer(w21, "Teilnehmer U", Geschlecht.m, Altersklasse.U13, new Verein(v2, "Verein2", turnierUUID), 27.1, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
            new Wettkaempfer(w22, "Teilnehmer V", Geschlecht.w, Altersklasse.U13, new Verein(v3, "Verein3", turnierUUID), 27.3, Optional.of(Farbe.GELB), true, true, turnierUUID)
        ), Optional.of(RandoriGruppenName.Maus), 27.8, 26.8, turnierUUID),
        new GewichtsklassenGruppe(gwkg5, Altersklasse.U13, Optional.of(Geschlecht.m), Arrays.asList(
            new Wettkaempfer(w23, "Teilnehmer W", Geschlecht.m, Altersklasse.U13, new Verein(v1, "Verein1", turnierUUID), 28.0, Optional.of(Farbe.BLAU), true, true, turnierUUID),
            new Wettkaempfer(w24, "Teilnehmer X", Geschlecht.w, Altersklasse.U13, new Verein(v4, "Verein4", turnierUUID), 28.2, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
            new Wettkaempfer(w25, "Teilnehmer Y", Geschlecht.m, Altersklasse.U13, new Verein(v5, "Verein5", turnierUUID), 28.1, Optional.of(Farbe.GRUEN), true, true, turnierUUID)
        ), Optional.of(RandoriGruppenName.Tiger), 28.0, 28.2, turnierUUID)
    );

    public static List<GewichtsklassenJpa> gewichtsklassenGruppenJpa = Arrays.asList(
            new GewichtsklassenJpa(gwkg1.toString(), "U11", "m", Arrays.asList(
                    new WettkaempferJpa(w1.toString(), "Teilnehmer A", "m", "U11", new VereinJpa(v1.toString(), "Verein1", turnierUUID.toString()), 24.0, "BLAU", true, true, turnierUUID.toString()),
                    new WettkaempferJpa(w2.toString(), "Teilnehmer B", "m", "U11", new VereinJpa(v2.toString(), "Verein2", turnierUUID.toString()), 24.5, "ORANGE", true, true, turnierUUID.toString()),
                    new WettkaempferJpa(w3.toString(), "Teilnehmer C", "m", "U11", new VereinJpa(v3.toString(), "Verein3", turnierUUID.toString()), 23.8, "GRUEN", true, true, turnierUUID.toString()),
                    new WettkaempferJpa(w4.toString(), "Teilnehmer D", "m", "U11", new VereinJpa(v4.toString(), "Verein4", turnierUUID.toString()), 24.2, "GELB", true, true, turnierUUID.toString()),
                    new WettkaempferJpa(w5.toString(), "Teilnehmer E", "m", "U11", new VereinJpa(v5.toString(), "Verein5", turnierUUID.toString()), 24.4, "SCHWARZ", true, true, turnierUUID.toString()),
                    new WettkaempferJpa(w6.toString(), "Teilnehmer F", "m", "U11", new VereinJpa(v2.toString(), "Verein2", turnierUUID.toString()), 24.3, "WEISS", true, true, turnierUUID.toString())
            ), "Antilope", 24.8, 23.8, turnierUUID.toString()),
            new GewichtsklassenJpa(gwkg2.toString(), "U11", "m", Arrays.asList(
                    new WettkaempferJpa(w7.toString(), "Teilnehmer G", "m", "U11", new VereinJpa(v3.toString(), "Verein3", turnierUUID.toString()), 25.0, "BLAU", true, true, turnierUUID.toString()),
                    new WettkaempferJpa(w8.toString(), "Teilnehmer H", "m", "U11", new VereinJpa(v4.toString(), "Verein4", turnierUUID.toString()), 25.2, "ORANGE", true, true, turnierUUID.toString()),
                    new WettkaempferJpa(w9.toString(), "Teilnehmer I", "m", "U11", new VereinJpa(v1.toString(), "Verein1", turnierUUID.toString()), 24.9, "GRUEN", true, true, turnierUUID.toString()),
                    new WettkaempferJpa(w10.toString(), "Teilnehmer J", "m", "U11", new VereinJpa(v5.toString(), "Verein5", turnierUUID.toString()), 25.1, "GELB", true, true, turnierUUID.toString()),
                    new WettkaempferJpa(w11.toString(), "Teilnehmer K", "m", "U11", new VereinJpa(v3.toString(), "Verein3", turnierUUID.toString()), 25.3, "SCHWARZ", true, true, turnierUUID.toString()),
                    new WettkaempferJpa(w12.toString(), "Teilnehmer L", "m", "U11", new VereinJpa(v2.toString(), "Verein2", turnierUUID.toString()), 25.4, "WEISS", true, true, turnierUUID.toString())
            ), "Eule", 25.8, 24.8, turnierUUID.toString()),
            new GewichtsklassenJpa(gwkg3.toString(), "U11", "m", Arrays.asList(
                    new WettkaempferJpa(w13.toString(), "Teilnehmer M", "m", "U11", new VereinJpa(v4.toString(), "Verein4", turnierUUID.toString()), 26.0, "BLAU", true, true, turnierUUID.toString()),
                    new WettkaempferJpa(w14.toString(), "Teilnehmer N", "m", "U11", new VereinJpa(v5.toString(), "Verein5", turnierUUID.toString()), 26.2, "ORANGE", true, true, turnierUUID.toString()),
                    new WettkaempferJpa(w15.toString(), "Teilnehmer O", "m", "U11", new VereinJpa(v1.toString(), "Verein1", turnierUUID.toString()), 26.1, "GRUEN", true, true, turnierUUID.toString()),
                    new WettkaempferJpa(w16.toString(), "Teilnehmer P", "m", "U11", new VereinJpa(v3.toString(), "Verein3", turnierUUID.toString()), 26.4, "GELB", true, true, turnierUUID.toString()),
                    new WettkaempferJpa(w17.toString(), "Teilnehmer Q", "m", "U11", new VereinJpa(v2.toString(), "Verein2", turnierUUID.toString()), 26.3, "SCHWARZ", true, true, turnierUUID.toString()),
                    new WettkaempferJpa(w18.toString(), "Teilnehmer R", "m", "U11", new VereinJpa(v1.toString(), "Verein1", turnierUUID.toString()), 26.5, "WEISS", true, true, turnierUUID.toString())
            ), "Katze", 26.8, 25.8, turnierUUID.toString()),
            new GewichtsklassenJpa(gwkg4.toString(), "U11", "m", Arrays.asList(
                    new WettkaempferJpa(w19.toString(), "Teilnehmer S", "m", "U11", new VereinJpa(v4.toString(), "Verein4", turnierUUID.toString()), 27.0, "BLAU", true, true, turnierUUID.toString()),
                    new WettkaempferJpa(w20.toString(), "Teilnehmer T", "m", "U11", new VereinJpa(v5.toString(), "Verein5", turnierUUID.toString()), 27.2, "ORANGE", true, true, turnierUUID.toString()),
                    new WettkaempferJpa(w21.toString(), "Teilnehmer U", "m", "U11", new VereinJpa(v2.toString(), "Verein2", turnierUUID.toString()), 27.1, "GRUEN", true, true, turnierUUID.toString()),
                    new WettkaempferJpa(w22.toString(), "Teilnehmer V", "m", "U11", new VereinJpa(v3.toString(), "Verein3", turnierUUID.toString()), 27.3, "GELB", true, true, turnierUUID.toString())
            ), "Maus", 27.8, 26.8, turnierUUID.toString()),
            new GewichtsklassenJpa(gwkg5.toString(), "U11", "m", Arrays.asList(
                    new WettkaempferJpa(w23.toString(), "Teilnehmer W", "m", "U11", new VereinJpa(v1.toString(), "Verein1", turnierUUID.toString()), 28.0, "BLAU", true, true, turnierUUID.toString()),
                    new WettkaempferJpa(w24.toString(), "Teilnehmer X", "m", "U11", new VereinJpa(v4.toString(), "Verein4", turnierUUID.toString()), 28.2, "ORANGE", true, true, turnierUUID.toString()),
                    new WettkaempferJpa(w25.toString(), "Teilnehmer Y", "m", "U11", new VereinJpa(v5.toString(), "Verein5", turnierUUID.toString()), 28.1, "GRUEN", true, true, turnierUUID.toString())
            ), "Tiger", 28.8, 27.8, turnierUUID.toString())
    );

    public static List<GewichtsklassenGruppe> gwks2 = Arrays.asList(
            new GewichtsklassenGruppe(gwkg1, Altersklasse.U11, Optional.of(Geschlecht.m), Arrays.asList(
                    new Wettkaempfer(w1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(v1,"Verein1", turnierUUID), 26.0, Optional.of(Farbe.BLAU),true, true, turnierUUID),
                    new Wettkaempfer(w2, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(v2,"Verein2", turnierUUID), 26.2, Optional.of(Farbe.ORANGE),true, true, turnierUUID),
                    new Wettkaempfer(w3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(v3,"Verein3", turnierUUID), 26.3, Optional.of(Farbe.GRUEN),true, true, turnierUUID),
                    new Wettkaempfer(w4, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(v4,"Verein4", turnierUUID), 26.5, Optional.of(Farbe.GELB),true, true, turnierUUID)
            ), Optional.of(RandoriGruppenName.Antilope), 26.8,24.8, turnierUUID)
    );

}
