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

    public static WettkampfGruppe gruppe1 = new WettkampfGruppe(100, "Antilope", "(Gewichtskl.1 U11)", Altersklasse.U11, Arrays.asList(
        new BegegnungenJeRunde(Arrays.asList(
            new Begegnung(1,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 1,
                Optional.of(new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                Optional.of(new Wettkaempfer(6, "Teilnehmer F", Geschlecht.m, Altersklasse.U11, new Verein(6, "Verein6", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
            new Begegnung(2,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 2,
                Optional.of(new Wettkaempfer(2, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                Optional.of(new Wettkaempfer(5, "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
            new Begegnung(3,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 3,
                Optional.of(new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                Optional.of(new Wettkaempfer(4, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID)
        )),
        new BegegnungenJeRunde(Arrays.asList(
            new Begegnung(4,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 4,
                Optional.of(new Wettkaempfer(6, "Teilnehmer F", Geschlecht.m, Altersklasse.U11, new Verein(6, "Verein6", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                Optional.of(new Wettkaempfer(4, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
            new Begegnung(5,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1,1, 5,
                Optional.of(new Wettkaempfer(5, "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                Optional.of(new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
            new Begegnung(6,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1,1,6,
                Optional.of(new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                Optional.of(new Wettkaempfer(2, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID)
        )),
        new BegegnungenJeRunde(Arrays.asList(
            new Begegnung(7,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1,1,7,
                Optional.of(new Wettkaempfer(2, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                Optional.of(new Wettkaempfer(6, "Teilnehmer F", Geschlecht.m, Altersklasse.U11, new Verein(6, "Verein6", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
            new Begegnung(8,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1,1,8,
                Optional.of(new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                Optional.of(new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
            new Begegnung(9,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1,1,9,
                Optional.of(new Wettkaempfer(4, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                Optional.of(new Wettkaempfer(5, "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID)
        )),
        new BegegnungenJeRunde(Arrays.asList(
            new Begegnung(10,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1,1,10,
                Optional.of(new Wettkaempfer(6, "Teilnehmer F", Geschlecht.m, Altersklasse.U11, new Verein(6, "Verein6", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                Optional.of(new Wettkaempfer(5, "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
            new Begegnung(11,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1,1,11,
                Optional.of(new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                Optional.of(new Wettkaempfer(4, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
            new Begegnung(12,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1,1,12,
                Optional.of(new Wettkaempfer(2, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                Optional.of(new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID)
        )),
        new BegegnungenJeRunde(Arrays.asList(
            new Begegnung(13,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1,13,
                Optional.of(new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                Optional.of(new Wettkaempfer(6, "Teilnehmer F", Geschlecht.m, Altersklasse.U11, new Verein(6, "Verein6", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
            new Begegnung(14,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1,14,
                Optional.of(new Wettkaempfer(4, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                Optional.of(new Wettkaempfer(2, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
            new Begegnung(15,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1,15,
                Optional.of(new Wettkaempfer(5, "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                Optional.of(new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID)
        ))
    ), turnierUUID);

    public static List<WettkampfGruppe> wks_gerade = Arrays.asList(
            new WettkampfGruppe(100, "Antilope", "(Gewichtskl.1 U11)", Altersklasse.U11, Arrays.asList(
                new BegegnungenJeRunde(Arrays.asList(
                            new Begegnung(1,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1,1,
                                    Optional.of(new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(6, "Teilnehmer F", Geschlecht.m, Altersklasse.U11, new Verein(6, "Verein6", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
                            new Begegnung(2,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1,2,
                                    Optional.of(new Wettkaempfer(2, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(5, "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
                            new Begegnung(3,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1,3,
                                    Optional.of(new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(4, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID))
                ),
                new BegegnungenJeRunde(Arrays.asList(
                            new Begegnung(4,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1,4,
                                    Optional.of(new Wettkaempfer(6, "Teilnehmer F", Geschlecht.m, Altersklasse.U11, new Verein(6, "Verein6", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(4, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
                            new Begegnung(5,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1,5,
                                    Optional.of(new Wettkaempfer(5, "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
                            new Begegnung(6,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1,6,
                                    Optional.of(new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(2, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID))
                ),
                new BegegnungenJeRunde(Arrays.asList(
                            new Begegnung(7,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1,7,
                                    Optional.of(new Wettkaempfer(2, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(6, "Teilnehmer F", Geschlecht.m, Altersklasse.U11, new Verein(6, "Verein6", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
                            new Begegnung(8,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1,8,
                                    Optional.of(new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
                            new Begegnung(9,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1,9,
                                    Optional.of(new Wettkaempfer(4, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(5, "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID))
                ),
                new BegegnungenJeRunde(Arrays.asList(
                            new Begegnung(10,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1,10,
                                    Optional.of(new Wettkaempfer(6, "Teilnehmer F", Geschlecht.m, Altersklasse.U11, new Verein(6, "Verein6", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(5, "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
                            new Begegnung(11,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1,11,
                                    Optional.of(new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(4, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
                            new Begegnung(12,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1,12,
                                    Optional.of(new Wettkaempfer(2, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID))
                ),
                new BegegnungenJeRunde(Arrays.asList(
                            new Begegnung(13,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1,13,
                                    Optional.of(new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(6, "Teilnehmer F", Geschlecht.m, Altersklasse.U11, new Verein(6, "Verein6", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
                            new Begegnung(14,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1,14,
                                    Optional.of(new Wettkaempfer(4, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(2, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
                            new Begegnung(15,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1,15,
                                    Optional.of(new Wettkaempfer(5, "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID))
                )
            ), turnierUUID),
            new WettkampfGruppe(200, "Eule", "(Gewichtskl.1 U11)", Altersklasse.U11, Arrays.asList(
                new BegegnungenJeRunde(Arrays.asList(
                            new Begegnung(16,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1,16,
                                    Optional.of(new Wettkaempfer(7, "Teilnehmer G", Geschlecht.m, Altersklasse.U11, new Verein(7, "Verein7", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(12, "Teilnehmer L", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
                            new Begegnung(17,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1,17,
                                    Optional.of(new Wettkaempfer(8, "Teilnehmer H", Geschlecht.m, Altersklasse.U11, new Verein(8, "Verein8", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(11, "Teilnehmer K", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
                            new Begegnung(18,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 18,
                                    Optional.of(new Wettkaempfer(9, "Teilnehmer I", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(10, "Teilnehmer J", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID))
                ),
                new BegegnungenJeRunde(Arrays.asList(
                            new Begegnung(19,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 19,
                                    Optional.of(new Wettkaempfer(12, "Teilnehmer L", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(10, "Teilnehmer J", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
                            new Begegnung(20,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 20,
                                    Optional.of(new Wettkaempfer(11, "Teilnehmer K", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(9, "Teilnehmer I", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
                            new Begegnung(21,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 21,
                                    Optional.of(new Wettkaempfer(7, "Teilnehmer G", Geschlecht.m, Altersklasse.U11, new Verein(7, "Verein7", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(8, "Teilnehmer H", Geschlecht.m, Altersklasse.U11, new Verein(8, "Verein8", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID))
                ),
                new BegegnungenJeRunde(Arrays.asList(
                            new Begegnung(22,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 22,
                                    Optional.of(new Wettkaempfer(8, "Teilnehmer H", Geschlecht.m, Altersklasse.U11, new Verein(8, "Verein8", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(12, "Teilnehmer L", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
                            new Begegnung(23,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 23,
                                    Optional.of(new Wettkaempfer(9, "Teilnehmer I", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(7, "Teilnehmer G", Geschlecht.m, Altersklasse.U11, new Verein(7, "Verein7", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
                            new Begegnung(24,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 24,
                                    Optional.of(new Wettkaempfer(10, "Teilnehmer J", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(11, "Teilnehmer K", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID))
                ),
                new BegegnungenJeRunde(Arrays.asList(
                            new Begegnung(25,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 25,
                                    Optional.of(new Wettkaempfer(12, "Teilnehmer L", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(11, "Teilnehmer K", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
                            new Begegnung(26,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 26,
                                    Optional.of(new Wettkaempfer(7, "Teilnehmer G", Geschlecht.m, Altersklasse.U11, new Verein(7, "Verein7", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(10, "Teilnehmer J", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
                            new Begegnung(27,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 27,
                                    Optional.of(new Wettkaempfer(8, "Teilnehmer H", Geschlecht.m, Altersklasse.U11, new Verein(8, "Verein8", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(9, "Teilnehmer I", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID))
                ),
                new BegegnungenJeRunde(Arrays.asList(
                            new Begegnung(28,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 28,
                                    Optional.of(new Wettkaempfer(9, "Teilnehmer I", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(12, "Teilnehmer L", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
                            new Begegnung(29,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 29,
                                    Optional.of(new Wettkaempfer(10, "Teilnehmer J", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(8, "Teilnehmer H", Geschlecht.m, Altersklasse.U11, new Verein(8, "Verein8", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
                            new Begegnung(30,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 30,
                                    Optional.of(new Wettkaempfer(11, "Teilnehmer K", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(7, "Teilnehmer G", Geschlecht.m, Altersklasse.U11, new Verein(7, "Verein7", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID))
                )
            ), turnierUUID),
            new WettkampfGruppe(300, "Katze", "(Gewichtskl.1 U11)", Altersklasse.U11, Arrays.asList(
                new BegegnungenJeRunde(Arrays.asList(
                            new Begegnung(31,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 31,
                                    Optional.of(new Wettkaempfer(13, "Teilnehmer M", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(18, "Teilnehmer R", Geschlecht.m, Altersklasse.U11, new Verein(8, "Verein8", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
                            new Begegnung(32,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 32,
                                    Optional.of(new Wettkaempfer(14, "Teilnehmer N", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(19, "Teilnehmer Q", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
                            new Begegnung(33,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 33,
                                    Optional.of(new Wettkaempfer(15, "Teilnehmer O", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(16, "Teilnehmer P", Geschlecht.m, Altersklasse.U11, new Verein(6, "Verein6", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID))
                ),
                new BegegnungenJeRunde(Arrays.asList(
                            new Begegnung(34,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 34,
                                    Optional.of(new Wettkaempfer(18, "Teilnehmer R", Geschlecht.m, Altersklasse.U11, new Verein(8, "Verein8", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(16, "Teilnehmer P", Geschlecht.m, Altersklasse.U11, new Verein(6, "Verein6", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
                            new Begegnung(35,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 35,
                                    Optional.of(new Wettkaempfer(19, "Teilnehmer Q", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(15, "Teilnehmer O", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
                            new Begegnung(36,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 36,
                                    Optional.of(new Wettkaempfer(13, "Teilnehmer M", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(14, "Teilnehmer N", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID))
                ),
                new BegegnungenJeRunde(Arrays.asList(
                            new Begegnung(37,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 37,
                                    Optional.of(new Wettkaempfer(14, "Teilnehmer N", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(18, "Teilnehmer R", Geschlecht.m, Altersklasse.U11, new Verein(8, "Verein8", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
                            new Begegnung(38,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 38,
                                    Optional.of(new Wettkaempfer(15, "Teilnehmer O", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(13, "Teilnehmer M", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
                            new Begegnung(39,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 39,
                                    Optional.of(new Wettkaempfer(16, "Teilnehmer P", Geschlecht.m, Altersklasse.U11, new Verein(6, "Verein6", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(19, "Teilnehmer Q", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID))
                ),
                new BegegnungenJeRunde(Arrays.asList(
                            new Begegnung(40,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 40,
                                    Optional.of(new Wettkaempfer(18, "Teilnehmer R", Geschlecht.m, Altersklasse.U11, new Verein(8, "Verein8", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(19, "Teilnehmer Q", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
                            new Begegnung(41,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 41,
                                    Optional.of(new Wettkaempfer(13, "Teilnehmer M", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(16, "Teilnehmer P", Geschlecht.m, Altersklasse.U11, new Verein(6, "Verein6", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
                            new Begegnung(42,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 42,
                                    Optional.of(new Wettkaempfer(14, "Teilnehmer N", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(15, "Teilnehmer O", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID))
                ),
                new BegegnungenJeRunde(Arrays.asList(
                            new Begegnung(43,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 43,
                                    Optional.of(new Wettkaempfer(15, "Teilnehmer O", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(18, "Teilnehmer R", Geschlecht.m, Altersklasse.U11, new Verein(8, "Verein8", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
                            new Begegnung(44,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 44,
                                    Optional.of(new Wettkaempfer(16, "Teilnehmer P", Geschlecht.m, Altersklasse.U11, new Verein(6, "Verein6", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(14, "Teilnehmer N", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
                            new Begegnung(45,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 45,
                                    Optional.of(new Wettkaempfer(19, "Teilnehmer Q", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(13, "Teilnehmer M", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID))
                )
            ), turnierUUID),
            new WettkampfGruppe(400, "Maus", "(Gewichtskl.1 U11)", Altersklasse.U11, Arrays.asList(
                new BegegnungenJeRunde(Arrays.asList(
                            new Begegnung(46,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 46,
                                    Optional.of(new Wettkaempfer(19, "Teilnehmer S", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(22, "Teilnehmer V", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
                            new Begegnung(47,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 47,
                                    Optional.of(new Wettkaempfer(20, "Teilnehmer T", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(21, "Teilnehmer U", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID))
                ),
                new BegegnungenJeRunde(Arrays.asList(
                            new Begegnung(48,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 48,
                                    Optional.of(new Wettkaempfer(22, "Teilnehmer V", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(21, "Teilnehmer U", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
                            new Begegnung(49,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 49,
                                    Optional.of(new Wettkaempfer(19, "Teilnehmer S", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(20, "Teilnehmer T", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID))
                ),
                new BegegnungenJeRunde(Arrays.asList(
                            new Begegnung(50,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 50,
                                    Optional.of(new Wettkaempfer(20, "Teilnehmer T", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(22, "Teilnehmer V", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
                            new Begegnung(51,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 51,
                                    Optional.of(new Wettkaempfer(21, "Teilnehmer U", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                                    Optional.of(new Wettkaempfer(19, "Teilnehmer S", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID))
                )
            ), turnierUUID)
    );



    public static List<WettkampfGruppe>  wks2 = Arrays.asList(
        new WettkampfGruppe(100, "Antilope", "(Gewichtskl.1 U11)", Altersklasse.U11, Arrays.asList(
            new BegegnungenJeRunde(Arrays.asList(
                new Begegnung(1,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 1,
                        Optional.of(new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                        Optional.of(new Wettkaempfer(4, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
                new Begegnung(2,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 2,
                        Optional.of(new Wettkaempfer(2, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                        Optional.of(new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID))
            ),
            new BegegnungenJeRunde(Arrays.asList(
                new Begegnung(3,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 3,
                        Optional.of(new Wettkaempfer(4, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                        Optional.of(new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
                new Begegnung(4,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 4,
                        Optional.of(new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                        Optional.of(new Wettkaempfer(2, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID))
            ),
            new BegegnungenJeRunde(Arrays.asList(
                new Begegnung(5,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 5,
                        Optional.of(new Wettkaempfer(2, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                        Optional.of(new Wettkaempfer(4, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID),
                new Begegnung(6,   new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1),rundeUUID1, 1, 1, 1, 6,
                        Optional.of(new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)),
                        Optional.of(new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID)), null, null, turnierUUID))
            )
        ), turnierUUID)
    );
}
