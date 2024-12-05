package de.sinnix.judoturnier.fixtures;

import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Begegnung;
import de.sinnix.judoturnier.model.BegegnungenJeRunde;
import de.sinnix.judoturnier.model.Farbe;
import de.sinnix.judoturnier.model.Geschlecht;
import de.sinnix.judoturnier.model.Verein;
import de.sinnix.judoturnier.model.Wettkaempfer;
import de.sinnix.judoturnier.model.WettkampfGruppe;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class WettkampfgruppeFixture {
	private static final UUID turnierUUID = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
	private static final UUID rundeUUID1  = UUID.randomUUID();
	private static final UUID v1UUID      = UUID.randomUUID();
	private static final UUID v2UUID      = UUID.randomUUID();
	private static final UUID v3UUID      = UUID.randomUUID();
	private static final UUID v4UUID      = UUID.randomUUID();
	private static final UUID v5UUID      = UUID.randomUUID();
	private static final UUID v6UUID      = UUID.randomUUID();
	private static final UUID v7UUID      = UUID.randomUUID();
	private static final UUID v8UUID      = UUID.randomUUID();
	private static final UUID v9UUID      = UUID.randomUUID();
	private static final UUID wk1UUID     = UUID.randomUUID();
	private static final UUID wk2UUID     = UUID.randomUUID();
	private static final UUID wk3UUID     = UUID.randomUUID();
	private static final UUID wk4UUID     = UUID.randomUUID();
	private static final UUID wk5UUID     = UUID.randomUUID();
	private static final UUID wk6UUID     = UUID.randomUUID();
	private static final UUID wk7UUID     = UUID.randomUUID();
	private static final UUID wk8UUID     = UUID.randomUUID();
	private static final UUID wk9UUID     = UUID.randomUUID();
	private static final UUID wk10UUID    = UUID.randomUUID();
	private static final UUID wk11UUID    = UUID.randomUUID();
	private static final UUID wk12UUID    = UUID.randomUUID();
	private static final UUID wk13UUID    = UUID.randomUUID();
	private static final UUID wk14UUID    = UUID.randomUUID();
	private static final UUID wk15UUID    = UUID.randomUUID();
	private static final UUID wk16UUID    = UUID.randomUUID();
	private static final UUID wk17UUID    = UUID.randomUUID();
	private static final UUID wk18UUID    = UUID.randomUUID();
	private static final UUID wk19UUID    = UUID.randomUUID();
	private static final UUID wk20UUID    = UUID.randomUUID();
	private static final UUID wk21UUID    = UUID.randomUUID();
	private static final UUID wk22UUID    = UUID.randomUUID();
	private static final UUID gwkgk1UUID  = UUID.randomUUID();
	private static final UUID gwkgk2UUID  = UUID.randomUUID();
	private static final UUID gwkgk3UUID  = UUID.randomUUID();
	private static final UUID gwkgk4UUID  = UUID.randomUUID();
	public static final  UUID b1UUID      = UUID.randomUUID();
	public static final  UUID b2UUID      = UUID.randomUUID();
	public static final  UUID b3UUID      = UUID.randomUUID();
	public static final  UUID b4UUID      = UUID.randomUUID();
	public static final  UUID b5UUID      = UUID.randomUUID();
	public static final  UUID b6UUID      = UUID.randomUUID();
	public static final  UUID b7UUID      = UUID.randomUUID();
	public static final  UUID b8UUID      = UUID.randomUUID();
	public static final  UUID b9UUID      = UUID.randomUUID();
	public static final  UUID b10UUID     = UUID.randomUUID();
	public static final  UUID b11UUID     = UUID.randomUUID();
	public static final  UUID b12UUID     = UUID.randomUUID();
	public static final  UUID b13UUID     = UUID.randomUUID();
	public static final  UUID b14UUID     = UUID.randomUUID();
	public static final  UUID b15UUID     = UUID.randomUUID();
	public static final  UUID b16UUID     = UUID.randomUUID();
	public static final  UUID b17UUID     = UUID.randomUUID();
	public static final  UUID b18UUID     = UUID.randomUUID();
	public static final  UUID b19UUID     = UUID.randomUUID();
	public static final  UUID b20UUID     = UUID.randomUUID();
	public static final  UUID b21UUID     = UUID.randomUUID();
	public static final  UUID b22UUID     = UUID.randomUUID();
	public static final  UUID b23UUID     = UUID.randomUUID();
	public static final  UUID b24UUID     = UUID.randomUUID();
	public static final  UUID b25UUID     = UUID.randomUUID();
	public static final  UUID b26UUID     = UUID.randomUUID();
	public static final  UUID b27UUID     = UUID.randomUUID();
	public static final  UUID b28UUID     = UUID.randomUUID();
	public static final  UUID b29UUID     = UUID.randomUUID();
	public static final  UUID b30UUID     = UUID.randomUUID();
	public static final  UUID b31UUID     = UUID.randomUUID();
	public static final  UUID b32UUID     = UUID.randomUUID();
	public static final  UUID b33UUID     = UUID.randomUUID();
	public static final  UUID b34UUID     = UUID.randomUUID();
	public static final  UUID b35UUID     = UUID.randomUUID();
	public static final  UUID b36UUID     = UUID.randomUUID();
	public static final  UUID b37UUID     = UUID.randomUUID();
	public static final  UUID b38UUID     = UUID.randomUUID();
	public static final  UUID b39UUID     = UUID.randomUUID();
	public static final  UUID b40UUID     = UUID.randomUUID();
	public static final  UUID b41UUID     = UUID.randomUUID();
	public static final  UUID b42UUID     = UUID.randomUUID();
	public static final  UUID b43UUID     = UUID.randomUUID();
	public static final  UUID b44UUID     = UUID.randomUUID();
	public static final  UUID b45UUID     = UUID.randomUUID();
	public static final  UUID b46UUID     = UUID.randomUUID();
	public static final  UUID b47UUID     = UUID.randomUUID();
	public static final  UUID b48UUID     = UUID.randomUUID();
	public static final  UUID b49UUID     = UUID.randomUUID();
	public static final  UUID b50UUID     = UUID.randomUUID();
	public static final  UUID b51UUID     = UUID.randomUUID();


    public static WettkampfGruppe gruppe1 = new WettkampfGruppe(gwkgk1UUID, "Antilope", "(Gewichtskl.1 U11)", Altersklasse.U11, Arrays.asList(
        new BegegnungenJeRunde(Arrays.asList(
            new Begegnung(b1UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 1,
                Optional.of(new Wettkaempfer(wk1UUID, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(v1UUID, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                Optional.of(new Wettkaempfer(wk6UUID, "Teilnehmer F", Geschlecht.m, Altersklasse.U11, new Verein(v6UUID, "Verein6", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
            new Begegnung(b2UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 2,
                Optional.of(new Wettkaempfer(wk2UUID, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(v2UUID, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                Optional.of(new Wettkaempfer(wk5UUID, "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new Verein(v5UUID, "Verein5", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
            new Begegnung(b3UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 3,
                Optional.of(new Wettkaempfer(wk3UUID, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(v3UUID, "Verein3", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                Optional.of(new Wettkaempfer(wk4UUID, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(v4UUID, "Verein4", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID)
        )),
        new BegegnungenJeRunde(Arrays.asList(
            new Begegnung(b4UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 4,
                Optional.of(new Wettkaempfer(wk6UUID, "Teilnehmer F", Geschlecht.m, Altersklasse.U11, new Verein(v6UUID, "Verein6", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                Optional.of(new Wettkaempfer(wk4UUID, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(v4UUID, "Verein4", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
            new Begegnung(b5UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1,1, 5,
                Optional.of(new Wettkaempfer(wk5UUID, "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new Verein(v5UUID, "Verein5", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                Optional.of(new Wettkaempfer(wk3UUID, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(v3UUID, "Verein3", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
            new Begegnung(b6UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1,1,6,
                Optional.of(new Wettkaempfer(wk1UUID, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(v1UUID, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                Optional.of(new Wettkaempfer(wk2UUID, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(v2UUID, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID)
        )),
        new BegegnungenJeRunde(Arrays.asList(
            new Begegnung(b7UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1,1,7,
                Optional.of(new Wettkaempfer(wk2UUID, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(v2UUID, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                Optional.of(new Wettkaempfer(wk6UUID, "Teilnehmer F", Geschlecht.m, Altersklasse.U11, new Verein(v6UUID, "Verein6", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
            new Begegnung(b8UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1,1,8,
                Optional.of(new Wettkaempfer(wk3UUID, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(v3UUID, "Verein3", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                Optional.of(new Wettkaempfer(wk1UUID, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(v1UUID, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
            new Begegnung(b9UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1,1,9,
                Optional.of(new Wettkaempfer(wk4UUID, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(v4UUID, "Verein4", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                Optional.of(new Wettkaempfer(wk5UUID, "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new Verein(v5UUID, "Verein5", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID)
        )),
        new BegegnungenJeRunde(Arrays.asList(
            new Begegnung(b10UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1,1,10,
                Optional.of(new Wettkaempfer(wk6UUID, "Teilnehmer F", Geschlecht.m, Altersklasse.U11, new Verein(v6UUID, "Verein6", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                Optional.of(new Wettkaempfer(wk5UUID, "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new Verein(v5UUID, "Verein5", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
            new Begegnung(b11UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1,1,11,
                Optional.of(new Wettkaempfer(wk1UUID, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(v1UUID, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                Optional.of(new Wettkaempfer(wk4UUID, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(v4UUID, "Verein4", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
            new Begegnung(b12UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1,1,12,
                Optional.of(new Wettkaempfer(wk2UUID, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(v2UUID, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                Optional.of(new Wettkaempfer(wk3UUID, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(v3UUID, "Verein3", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID)
        )),
        new BegegnungenJeRunde(Arrays.asList(
            new Begegnung(b13UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1,13,
                Optional.of(new Wettkaempfer(wk3UUID, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(v3UUID, "Verein3", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                Optional.of(new Wettkaempfer(wk6UUID, "Teilnehmer F", Geschlecht.m, Altersklasse.U11, new Verein(v6UUID, "Verein6", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
            new Begegnung(b14UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1,14,
                Optional.of(new Wettkaempfer(wk4UUID, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(v4UUID, "Verein4", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                Optional.of(new Wettkaempfer(wk2UUID, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(v2UUID, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
            new Begegnung(b15UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1,15,
                Optional.of(new Wettkaempfer(wk5UUID, "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new Verein(v5UUID, "Verein5", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                Optional.of(new Wettkaempfer(wk1UUID, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(v1UUID, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID)
        ))
    ), turnierUUID);

    public static List<WettkampfGruppe> wks_gerade = Arrays.asList(
            new WettkampfGruppe(gwkgk1UUID, "Antilope", "(Gewichtskl.1 U11)", Altersklasse.U11, Arrays.asList(
                new BegegnungenJeRunde(Arrays.asList(
                            new Begegnung(b1UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1,1,
                                    Optional.of(new Wettkaempfer(wk1UUID, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(v1UUID, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(wk6UUID, "Teilnehmer F", Geschlecht.m, Altersklasse.U11, new Verein(v6UUID, "Verein6", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
                            new Begegnung(b2UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1,2,
                                    Optional.of(new Wettkaempfer(wk2UUID, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(v2UUID, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(wk5UUID, "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new Verein(v5UUID, "Verein5", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
                            new Begegnung(b3UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1,3,
                                    Optional.of(new Wettkaempfer(wk3UUID, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(v3UUID, "Verein3", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(wk4UUID, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(v4UUID, "Verein4", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID))
                ),
                new BegegnungenJeRunde(Arrays.asList(
                            new Begegnung(b4UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1,4,
                                    Optional.of(new Wettkaempfer(wk6UUID, "Teilnehmer F", Geschlecht.m, Altersklasse.U11, new Verein(v6UUID, "Verein6", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(wk4UUID, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(v4UUID, "Verein4", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
                            new Begegnung(b5UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1,5,
                                    Optional.of(new Wettkaempfer(wk5UUID, "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new Verein(v5UUID, "Verein5", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(wk3UUID, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(v3UUID, "Verein3", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
                            new Begegnung(b6UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1,6,
                                    Optional.of(new Wettkaempfer(wk1UUID, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(v1UUID, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(wk2UUID, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(v2UUID, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID))
                ),
                new BegegnungenJeRunde(Arrays.asList(
                            new Begegnung(b7UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1,7,
                                    Optional.of(new Wettkaempfer(wk2UUID, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(v2UUID, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(wk6UUID, "Teilnehmer F", Geschlecht.m, Altersklasse.U11, new Verein(v6UUID, "Verein6", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
                            new Begegnung(b8UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1,8,
                                    Optional.of(new Wettkaempfer(wk3UUID, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(v3UUID, "Verein3", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(wk1UUID, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(v1UUID, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
                            new Begegnung(b9UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1,9,
                                    Optional.of(new Wettkaempfer(wk4UUID, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(v4UUID, "Verein4", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(wk5UUID, "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new Verein(v5UUID, "Verein5", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID))
                ),
                new BegegnungenJeRunde(Arrays.asList(
                            new Begegnung(b10UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1,10,
                                    Optional.of(new Wettkaempfer(wk6UUID, "Teilnehmer F", Geschlecht.m, Altersklasse.U11, new Verein(v6UUID, "Verein6", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(wk5UUID, "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new Verein(v5UUID, "Verein5", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
                            new Begegnung(b11UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1,11,
                                    Optional.of(new Wettkaempfer(wk1UUID, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(v1UUID, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(wk4UUID, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(v4UUID, "Verein4", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
                            new Begegnung(b12UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1,12,
                                    Optional.of(new Wettkaempfer(wk2UUID, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(v2UUID, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(wk3UUID, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(v3UUID, "Verein3", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID))
                ),
                new BegegnungenJeRunde(Arrays.asList(
                            new Begegnung(b13UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1,13,
                                    Optional.of(new Wettkaempfer(wk3UUID, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(v3UUID, "Verein3", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(wk6UUID, "Teilnehmer F", Geschlecht.m, Altersklasse.U11, new Verein(v6UUID, "Verein6", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
                            new Begegnung(b13UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1,14,
                                    Optional.of(new Wettkaempfer(wk4UUID, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(v4UUID, "Verein4", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(wk2UUID, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(v2UUID, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
                            new Begegnung(b15UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1,15,
                                    Optional.of(new Wettkaempfer(wk5UUID, "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new Verein(v5UUID, "Verein5", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(wk1UUID, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(v1UUID, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID))
                )
            ), turnierUUID),
            new WettkampfGruppe(gwkgk2UUID, "Eule", "(Gewichtskl.1 U11)", Altersklasse.U11, Arrays.asList(
                new BegegnungenJeRunde(Arrays.asList(
                            new Begegnung(b16UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1,16,
                                    Optional.of(new Wettkaempfer(wk7UUID, "Teilnehmer G", Geschlecht.m, Altersklasse.U11, new Verein(v7UUID, "Verein7", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(wk12UUID, "Teilnehmer L", Geschlecht.m, Altersklasse.U11, new Verein(v2UUID, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
                            new Begegnung(b17UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1,17,
                                    Optional.of(new Wettkaempfer(wk8UUID, "Teilnehmer H", Geschlecht.m, Altersklasse.U11, new Verein(v8UUID, "Verein8", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(wk11UUID, "Teilnehmer K", Geschlecht.m, Altersklasse.U11, new Verein(v1UUID, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
                            new Begegnung(b18UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 18,
                                    Optional.of(new Wettkaempfer(wk9UUID, "Teilnehmer I", Geschlecht.m, Altersklasse.U11, new Verein(v9UUID, "Verein9", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(wk10UUID, "Teilnehmer J", Geschlecht.m, Altersklasse.U11, new Verein(v1UUID, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID))
                ),
                new BegegnungenJeRunde(Arrays.asList(
                            new Begegnung(b19UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 19,
                                    Optional.of(new Wettkaempfer(wk12UUID, "Teilnehmer L", Geschlecht.m, Altersklasse.U11, new Verein(v2UUID, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(wk10UUID, "Teilnehmer J", Geschlecht.m, Altersklasse.U11, new Verein(v1UUID, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
                            new Begegnung(b20UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 20,
                                    Optional.of(new Wettkaempfer(wk11UUID, "Teilnehmer K", Geschlecht.m, Altersklasse.U11, new Verein(v1UUID, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(wk9UUID, "Teilnehmer I", Geschlecht.m, Altersklasse.U11, new Verein(v9UUID, "Verein9", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
                            new Begegnung(b21UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 21,
                                    Optional.of(new Wettkaempfer(wk7UUID, "Teilnehmer G", Geschlecht.m, Altersklasse.U11, new Verein(v7UUID, "Verein7", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(wk8UUID, "Teilnehmer H", Geschlecht.m, Altersklasse.U11, new Verein(v8UUID, "Verein8", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID))
                ),
                new BegegnungenJeRunde(Arrays.asList(
                            new Begegnung(b22UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 22,
                                    Optional.of(new Wettkaempfer(wk8UUID, "Teilnehmer H", Geschlecht.m, Altersklasse.U11, new Verein(v8UUID, "Verein8", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(wk12UUID, "Teilnehmer L", Geschlecht.m, Altersklasse.U11, new Verein(v2UUID, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
                            new Begegnung(b23UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 23,
                                    Optional.of(new Wettkaempfer(wk9UUID, "Teilnehmer I", Geschlecht.m, Altersklasse.U11, new Verein(v9UUID, "Verein9", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(wk7UUID, "Teilnehmer G", Geschlecht.m, Altersklasse.U11, new Verein(v7UUID, "Verein7", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
                            new Begegnung(b24UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 24,
                                    Optional.of(new Wettkaempfer(wk10UUID, "Teilnehmer J", Geschlecht.m, Altersklasse.U11, new Verein(v1UUID, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(wk11UUID, "Teilnehmer K", Geschlecht.m, Altersklasse.U11, new Verein(v1UUID, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID))
                ),
                new BegegnungenJeRunde(Arrays.asList(
                            new Begegnung(b25UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 25,
                                    Optional.of(new Wettkaempfer(wk12UUID, "Teilnehmer L", Geschlecht.m, Altersklasse.U11, new Verein(v2UUID, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(wk11UUID, "Teilnehmer K", Geschlecht.m, Altersklasse.U11, new Verein(v1UUID, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
                            new Begegnung(b26UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 26,
                                    Optional.of(new Wettkaempfer(wk7UUID, "Teilnehmer G", Geschlecht.m, Altersklasse.U11, new Verein(v7UUID, "Verein7", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(wk10UUID, "Teilnehmer J", Geschlecht.m, Altersklasse.U11, new Verein(v1UUID, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
                            new Begegnung(b27UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 27,
                                    Optional.of(new Wettkaempfer(wk8UUID, "Teilnehmer H", Geschlecht.m, Altersklasse.U11, new Verein(v8UUID, "Verein8", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(wk9UUID, "Teilnehmer I", Geschlecht.m, Altersklasse.U11, new Verein(v9UUID, "Verein9", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID))
                ),
                new BegegnungenJeRunde(Arrays.asList(
                            new Begegnung(b28UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 28,
                                    Optional.of(new Wettkaempfer(wk9UUID, "Teilnehmer I", Geschlecht.m, Altersklasse.U11, new Verein(v9UUID, "Verein9", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(wk12UUID, "Teilnehmer L", Geschlecht.m, Altersklasse.U11, new Verein(v2UUID, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
                            new Begegnung(b29UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 29,
                                    Optional.of(new Wettkaempfer(wk10UUID, "Teilnehmer J", Geschlecht.m, Altersklasse.U11, new Verein(v1UUID, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(wk8UUID, "Teilnehmer H", Geschlecht.m, Altersklasse.U11, new Verein(v8UUID, "Verein8", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
                            new Begegnung(b30UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 30,
                                    Optional.of(new Wettkaempfer(wk11UUID, "Teilnehmer K", Geschlecht.m, Altersklasse.U11, new Verein(v1UUID, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(wk7UUID, "Teilnehmer G", Geschlecht.m, Altersklasse.U11, new Verein(v7UUID, "Verein7", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID))
                )
            ), turnierUUID),
            new WettkampfGruppe(gwkgk3UUID, "Katze", "(Gewichtskl.1 U11)", Altersklasse.U11, Arrays.asList(
                new BegegnungenJeRunde(Arrays.asList(
                            new Begegnung(b31UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 31,
                                    Optional.of(new Wettkaempfer(wk13UUID, "Teilnehmer M", Geschlecht.m, Altersklasse.U11, new Verein(v3UUID, "Verein3", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(wk18UUID, "Teilnehmer R", Geschlecht.m, Altersklasse.U11, new Verein(v8UUID, "Verein8", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
                            new Begegnung(b32UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 32,
                                    Optional.of(new Wettkaempfer(wk14UUID, "Teilnehmer N", Geschlecht.m, Altersklasse.U11, new Verein(v4UUID, "Verein4", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(wk19UUID, "Teilnehmer Q", Geschlecht.m, Altersklasse.U11, new Verein(v9UUID, "Verein9", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
                            new Begegnung(b33UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 33,
                                    Optional.of(new Wettkaempfer(wk15UUID, "Teilnehmer O", Geschlecht.m, Altersklasse.U11, new Verein(v5UUID, "Verein5", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(wk16UUID, "Teilnehmer P", Geschlecht.m, Altersklasse.U11, new Verein(v6UUID, "Verein6", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID))
                ),
                new BegegnungenJeRunde(Arrays.asList(
                            new Begegnung(b34UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 34,
                                    Optional.of(new Wettkaempfer(wk18UUID, "Teilnehmer R", Geschlecht.m, Altersklasse.U11, new Verein(v8UUID, "Verein8", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(wk16UUID, "Teilnehmer P", Geschlecht.m, Altersklasse.U11, new Verein(v6UUID, "Verein6", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
                            new Begegnung(b35UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 35,
                                    Optional.of(new Wettkaempfer(wk19UUID, "Teilnehmer Q", Geschlecht.m, Altersklasse.U11, new Verein(v9UUID, "Verein9", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(wk15UUID, "Teilnehmer O", Geschlecht.m, Altersklasse.U11, new Verein(v5UUID, "Verein5", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
                            new Begegnung(b36UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 36,
                                    Optional.of(new Wettkaempfer(wk13UUID, "Teilnehmer M", Geschlecht.m, Altersklasse.U11, new Verein(v3UUID, "Verein3", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(wk14UUID, "Teilnehmer N", Geschlecht.m, Altersklasse.U11, new Verein(v4UUID, "Verein4", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID))
                ),
                new BegegnungenJeRunde(Arrays.asList(
                            new Begegnung(b37UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 37,
                                    Optional.of(new Wettkaempfer(wk14UUID, "Teilnehmer N", Geschlecht.m, Altersklasse.U11, new Verein(v4UUID, "Verein4", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(wk18UUID, "Teilnehmer R", Geschlecht.m, Altersklasse.U11, new Verein(v8UUID, "Verein8", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
                            new Begegnung(b38UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 38,
                                    Optional.of(new Wettkaempfer(wk15UUID, "Teilnehmer O", Geschlecht.m, Altersklasse.U11, new Verein(v5UUID, "Verein5", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(wk13UUID, "Teilnehmer M", Geschlecht.m, Altersklasse.U11, new Verein(v3UUID, "Verein3", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
                            new Begegnung(b39UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 39,
                                    Optional.of(new Wettkaempfer(wk16UUID, "Teilnehmer P", Geschlecht.m, Altersklasse.U11, new Verein(v6UUID, "Verein6", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(wk19UUID, "Teilnehmer Q", Geschlecht.m, Altersklasse.U11, new Verein(v9UUID, "Verein9", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID))
                ),
                new BegegnungenJeRunde(Arrays.asList(
                            new Begegnung(b40UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 40,
                                    Optional.of(new Wettkaempfer(wk18UUID, "Teilnehmer R", Geschlecht.m, Altersklasse.U11, new Verein(v8UUID, "Verein8", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(wk19UUID, "Teilnehmer Q", Geschlecht.m, Altersklasse.U11, new Verein(v9UUID, "Verein9", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
                            new Begegnung(b41UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 41,
                                    Optional.of(new Wettkaempfer(wk13UUID, "Teilnehmer M", Geschlecht.m, Altersklasse.U11, new Verein(v3UUID, "Verein3", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(wk16UUID, "Teilnehmer P", Geschlecht.m, Altersklasse.U11, new Verein(v6UUID, "Verein6", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
                            new Begegnung(b42UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 42,
                                    Optional.of(new Wettkaempfer(wk14UUID, "Teilnehmer N", Geschlecht.m, Altersklasse.U11, new Verein(v4UUID, "Verein4", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(wk15UUID, "Teilnehmer O", Geschlecht.m, Altersklasse.U11, new Verein(v5UUID, "Verein5", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID))
                ),
                new BegegnungenJeRunde(Arrays.asList(
                            new Begegnung(b43UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 43,
                                    Optional.of(new Wettkaempfer(wk15UUID, "Teilnehmer O", Geschlecht.m, Altersklasse.U11, new Verein(v5UUID, "Verein5", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(wk18UUID, "Teilnehmer R", Geschlecht.m, Altersklasse.U11, new Verein(v8UUID, "Verein8", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
                            new Begegnung(b44UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 44,
                                    Optional.of(new Wettkaempfer(wk16UUID, "Teilnehmer P", Geschlecht.m, Altersklasse.U11, new Verein(v6UUID, "Verein6", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(wk14UUID, "Teilnehmer N", Geschlecht.m, Altersklasse.U11, new Verein(v4UUID, "Verein4", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
                            new Begegnung(b45UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 45,
                                    Optional.of(new Wettkaempfer(wk19UUID, "Teilnehmer Q", Geschlecht.m, Altersklasse.U11, new Verein(v9UUID, "Verein9", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(wk13UUID, "Teilnehmer M", Geschlecht.m, Altersklasse.U11, new Verein(v3UUID, "Verein3", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID))
                )
            ), turnierUUID),
            new WettkampfGruppe(gwkgk4UUID, "Maus", "(Gewichtskl.1 U11)", Altersklasse.U11, Arrays.asList(
                new BegegnungenJeRunde(Arrays.asList(
                            new Begegnung(b46UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 46,
                                    Optional.of(new Wettkaempfer(wk19UUID, "Teilnehmer S", Geschlecht.m, Altersklasse.U11, new Verein(v9UUID, "Verein9", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(wk22UUID, "Teilnehmer V", Geschlecht.m, Altersklasse.U11, new Verein(v2UUID, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
                            new Begegnung(b47UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 47,
                                    Optional.of(new Wettkaempfer(wk20UUID, "Teilnehmer T", Geschlecht.m, Altersklasse.U11, new Verein(v2UUID, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(wk21UUID, "Teilnehmer U", Geschlecht.m, Altersklasse.U11, new Verein(v1UUID, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID))
                ),
                new BegegnungenJeRunde(Arrays.asList(
                            new Begegnung(b48UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 48,
                                    Optional.of(new Wettkaempfer(wk22UUID, "Teilnehmer V", Geschlecht.m, Altersklasse.U11, new Verein(v2UUID, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(wk21UUID, "Teilnehmer U", Geschlecht.m, Altersklasse.U11, new Verein(v1UUID, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
                            new Begegnung(b49UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 49,
                                    Optional.of(new Wettkaempfer(wk19UUID, "Teilnehmer S", Geschlecht.m, Altersklasse.U11, new Verein(v9UUID, "Verein9", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(wk20UUID, "Teilnehmer T", Geschlecht.m, Altersklasse.U11, new Verein(v2UUID, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID))
                ),
                new BegegnungenJeRunde(Arrays.asList(
                            new Begegnung(b50UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 50,
                                    Optional.of(new Wettkaempfer(wk20UUID, "Teilnehmer T", Geschlecht.m, Altersklasse.U11, new Verein(v2UUID, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(wk22UUID, "Teilnehmer V", Geschlecht.m, Altersklasse.U11, new Verein(v2UUID, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
                            new Begegnung(b51UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 51,
                                    Optional.of(new Wettkaempfer(wk21UUID, "Teilnehmer U", Geschlecht.m, Altersklasse.U11, new Verein(v1UUID, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(wk19UUID, "Teilnehmer S", Geschlecht.m, Altersklasse.U11, new Verein(v9UUID, "Verein9", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID))
                )
            ), turnierUUID)
    );



    public static List<WettkampfGruppe>  wks2 = Arrays.asList(
        new WettkampfGruppe(gwkgk1UUID, "Antilope", "(Gewichtskl.1 U11)", Altersklasse.U11, Arrays.asList(
            new BegegnungenJeRunde(Arrays.asList(
                new Begegnung(b1UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 1,
                        Optional.of(new Wettkaempfer(wk1UUID, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(v1UUID, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                        Optional.of(new Wettkaempfer(wk4UUID, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(v4UUID, "Verein4", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
                new Begegnung(b2UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 2,
                        Optional.of(new Wettkaempfer(wk2UUID, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(v2UUID, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                        Optional.of(new Wettkaempfer(wk3UUID, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(v3UUID, "Verein3", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID))
            ),
            new BegegnungenJeRunde(Arrays.asList(
                new Begegnung(b3UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 3,
                        Optional.of(new Wettkaempfer(wk4UUID, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(v4UUID, "Verein4", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                        Optional.of(new Wettkaempfer(wk3UUID, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(v3UUID, "Verein3", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
                new Begegnung(b4UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 4,
                        Optional.of(new Wettkaempfer(wk1UUID, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(v1UUID, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                        Optional.of(new Wettkaempfer(wk2UUID, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(v2UUID, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID))
            ),
            new BegegnungenJeRunde(Arrays.asList(
                new Begegnung(b5UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 5,
                        Optional.of(new Wettkaempfer(wk2UUID, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(v2UUID, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                        Optional.of(new Wettkaempfer(wk4UUID, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(v4UUID, "Verein4", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
                new Begegnung(b6UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 6,
                        Optional.of(new Wettkaempfer(wk3UUID, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(v3UUID, "Verein3", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                        Optional.of(new Wettkaempfer(wk1UUID, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(v1UUID, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID))
            )
        ), turnierUUID)
    );

	public static WettkampfGruppe wettkampfGruppeFrauen = new WettkampfGruppe(UUID.randomUUID(), "Ameise", "(58.0-62.0 Frauen)", Altersklasse.Frauen, Arrays.asList(
		new BegegnungenJeRunde(Arrays.asList(
			new Begegnung(b1UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1), null, null, null, null, null,
				Optional.of(WettkaempferFixtures.wettkaempferin1),
				Optional.empty(),
				null, null, turnierUUID),
			new Begegnung(b2UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 2),null, null, null, null, null,
				Optional.of(WettkaempferFixtures.wettkaempferin2),
				Optional.of(WettkaempferFixtures.wettkaempferin3),
				null, null, turnierUUID),
			new Begegnung(b3UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 3),null, null, null, null, null,
				Optional.of(WettkaempferFixtures.wettkaempferin4),
				Optional.of(WettkaempferFixtures.wettkaempferin5),
				null, null, turnierUUID),
			new Begegnung(b4UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 4),null, null, null, null, null,
				Optional.of(WettkaempferFixtures.wettkaempferin6),
				Optional.of(WettkaempferFixtures.wettkaempferin7),
				null, null, turnierUUID),
			new Begegnung(b5UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 1, 1),null, null, null, null, null,
				Optional.empty(),
				Optional.empty(),
				null, null, turnierUUID),
			new Begegnung(b6UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 1, 2),null, null, null, null, null,
				Optional.empty(),
				Optional.empty(),
				null, null, turnierUUID))),
		new BegegnungenJeRunde(Arrays.asList(
			new Begegnung(b7UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 2, 1), null, null, null, null, null,
				Optional.empty(),
				Optional.empty(),
				null, null, turnierUUID),
			new Begegnung(b8UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 2, 2),null, null, null, null, null,
				Optional.empty(),
				Optional.empty(),
				null, null, turnierUUID),
			new Begegnung(b9UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 2, 1),null, null, null, null, null,
				Optional.empty(),
				Optional.empty(),
				null, null, turnierUUID),
			new Begegnung(b10UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.TROSTRUNDE, 2, 2),null, null, null, null, null,
				Optional.empty(),
				Optional.empty(),
				null, null, turnierUUID))
		),
		new BegegnungenJeRunde(Arrays.asList(
			new Begegnung(b11UUID,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 3, 1), null, null, null, null, null,
				Optional.empty(),
				Optional.empty(),
				null, null, turnierUUID))
		)
	), turnierUUID);

	public static List<WettkampfGruppe> wettkampfGruppenListeFrauen = Arrays.asList(wettkampfGruppeFrauen);
}
