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

    public static WettkampfGruppe gruppe1 = new WettkampfGruppe(100, "Antilope", "(Gewichtskl.1 U11)", Arrays.asList(
        new BegegnungenJeRunde(Arrays.asList(
            new Begegnung(1, rundeUUID1, 1, 1, 1, 1,
                new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                new Wettkaempfer(6, "Teilnehmer F", Geschlecht.m, Altersklasse.U11, new Verein(6, "Verein6", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID),
            new Begegnung(2, rundeUUID1, 1, 1, 1, 2,
                new Wettkaempfer(2, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                new Wettkaempfer(5, "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID),
            new Begegnung(3, rundeUUID1, 1, 1, 1, 3,
                new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                new Wettkaempfer(4, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID)
        )),
        new BegegnungenJeRunde(Arrays.asList(
            new Begegnung(4, rundeUUID1, 1, 1, 1, 4,
                new Wettkaempfer(6, "Teilnehmer F", Geschlecht.m, Altersklasse.U11, new Verein(6, "Verein6", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                new Wettkaempfer(4, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID),
            new Begegnung(5, rundeUUID1, 1, 1,1, 5,
                new Wettkaempfer(5, "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID),
            new Begegnung(6, rundeUUID1, 1, 1,1,6,
                new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                new Wettkaempfer(2, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID)
        )),
        new BegegnungenJeRunde(Arrays.asList(
            new Begegnung(7, rundeUUID1, 1, 1,1,7,
                new Wettkaempfer(2, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                new Wettkaempfer(6, "Teilnehmer F", Geschlecht.m, Altersklasse.U11, new Verein(6, "Verein6", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID),
            new Begegnung(8, rundeUUID1, 1, 1,1,8,
                new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID),
            new Begegnung(9, rundeUUID1, 1, 1,1,9,
                new Wettkaempfer(4, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                new Wettkaempfer(5, "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID)
        )),
        new BegegnungenJeRunde(Arrays.asList(
            new Begegnung(10, rundeUUID1, 1, 1,1,10,
                new Wettkaempfer(6, "Teilnehmer F", Geschlecht.m, Altersklasse.U11, new Verein(6, "Verein6", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                new Wettkaempfer(5, "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID),
            new Begegnung(11, rundeUUID1, 1, 1,1,11,
                new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                new Wettkaempfer(4, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID),
            new Begegnung(12, rundeUUID1, 1, 1,1,12,
                new Wettkaempfer(2, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID)
        )),
        new BegegnungenJeRunde(Arrays.asList(
            new Begegnung(13, rundeUUID1, 1, 1, 1,13,
                new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                new Wettkaempfer(6, "Teilnehmer F", Geschlecht.m, Altersklasse.U11, new Verein(6, "Verein6", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID),
            new Begegnung(14, rundeUUID1, 1, 1, 1,14,
                new Wettkaempfer(4, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                new Wettkaempfer(2, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID),
            new Begegnung(15, rundeUUID1, 1, 1, 1,15,
                new Wettkaempfer(5, "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID)
        ))
    ), turnierUUID);

    public static List<WettkampfGruppe> wks_gerade = Arrays.asList(
            new WettkampfGruppe(100, "Antilope", "(Gewichtskl.1 U11)", Arrays.asList(
                new BegegnungenJeRunde(Arrays.asList(
                            new Begegnung(1, rundeUUID1, 1, 1, 1,1,
                                    new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                                    new Wettkaempfer(6, "Teilnehmer F", Geschlecht.m, Altersklasse.U11, new Verein(6, "Verein6", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID),
                            new Begegnung(2, rundeUUID1, 1, 1, 1,2,
                                    new Wettkaempfer(2, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                                    new Wettkaempfer(5, "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID),
                            new Begegnung(3, rundeUUID1, 1, 1, 1,3,
                                    new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                                    new Wettkaempfer(4, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID))
                ),
                new BegegnungenJeRunde(Arrays.asList(
                            new Begegnung(4, rundeUUID1, 1, 1, 1,4,
                                    new Wettkaempfer(6, "Teilnehmer F", Geschlecht.m, Altersklasse.U11, new Verein(6, "Verein6", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                                    new Wettkaempfer(4, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID),
                            new Begegnung(5, rundeUUID1, 1, 1, 1,5,
                                    new Wettkaempfer(5, "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                                    new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID),
                            new Begegnung(6, rundeUUID1, 1, 1, 1,6,
                                    new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                                    new Wettkaempfer(2, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID))
                ),
                new BegegnungenJeRunde(Arrays.asList(
                            new Begegnung(7, rundeUUID1, 1, 1, 1,7,
                                    new Wettkaempfer(2, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                                    new Wettkaempfer(6, "Teilnehmer F", Geschlecht.m, Altersklasse.U11, new Verein(6, "Verein6", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID),
                            new Begegnung(8, rundeUUID1, 1, 1, 1,8,
                                    new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                                    new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID),
                            new Begegnung(9, rundeUUID1, 1, 1, 1,9,
                                    new Wettkaempfer(4, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                                    new Wettkaempfer(5, "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID))
                ),
                new BegegnungenJeRunde(Arrays.asList(
                            new Begegnung(10, rundeUUID1, 1, 1, 1,10,
                                    new Wettkaempfer(6, "Teilnehmer F", Geschlecht.m, Altersklasse.U11, new Verein(6, "Verein6", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                                    new Wettkaempfer(5, "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID),
                            new Begegnung(11, rundeUUID1, 1, 1, 1,11,
                                    new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                                    new Wettkaempfer(4, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID),
                            new Begegnung(12, rundeUUID1, 1, 1, 1,12,
                                    new Wettkaempfer(2, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                                    new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID))
                ),
                new BegegnungenJeRunde(Arrays.asList(
                            new Begegnung(13, rundeUUID1, 1, 1, 1,13,
                                    new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                                    new Wettkaempfer(6, "Teilnehmer F", Geschlecht.m, Altersklasse.U11, new Verein(6, "Verein6", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID),
                            new Begegnung(14, rundeUUID1, 1, 1, 1,14,
                                    new Wettkaempfer(4, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                                    new Wettkaempfer(2, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID),
                            new Begegnung(15, rundeUUID1, 1, 1, 1,15,
                                    new Wettkaempfer(5, "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                                    new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID))
                )
            ), turnierUUID),
            new WettkampfGruppe(200, "Eule", "(Gewichtskl.1 U11)", Arrays.asList(
                new BegegnungenJeRunde(Arrays.asList(
                            new Begegnung(16, rundeUUID1, 1, 1, 1,16,
                                    new Wettkaempfer(7, "Teilnehmer G", Geschlecht.m, Altersklasse.U11, new Verein(7, "Verein7", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                                    new Wettkaempfer(12, "Teilnehmer L", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID),
                            new Begegnung(17, rundeUUID1, 1, 1, 1,17,
                                    new Wettkaempfer(8, "Teilnehmer H", Geschlecht.m, Altersklasse.U11, new Verein(8, "Verein8", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                                    new Wettkaempfer(11, "Teilnehmer K", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID),
                            new Begegnung(18, rundeUUID1, 1, 1, 1, 18,
                                    new Wettkaempfer(9, "Teilnehmer I", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                                    new Wettkaempfer(10, "Teilnehmer J", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID))
                ),
                new BegegnungenJeRunde(Arrays.asList(
                            new Begegnung(19, rundeUUID1, 1, 1, 1, 19,
                                    new Wettkaempfer(12, "Teilnehmer L", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                                    new Wettkaempfer(10, "Teilnehmer J", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID),
                            new Begegnung(20, rundeUUID1, 1, 1, 1, 20,
                                    new Wettkaempfer(11, "Teilnehmer K", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                                    new Wettkaempfer(9, "Teilnehmer I", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID),
                            new Begegnung(21, rundeUUID1, 1, 1, 1, 21,
                                    new Wettkaempfer(7, "Teilnehmer G", Geschlecht.m, Altersklasse.U11, new Verein(7, "Verein7", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                                    new Wettkaempfer(8, "Teilnehmer H", Geschlecht.m, Altersklasse.U11, new Verein(8, "Verein8", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID))
                ),
                new BegegnungenJeRunde(Arrays.asList(
                            new Begegnung(22, rundeUUID1, 1, 1, 1, 22,
                                    new Wettkaempfer(8, "Teilnehmer H", Geschlecht.m, Altersklasse.U11, new Verein(8, "Verein8", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                                    new Wettkaempfer(12, "Teilnehmer L", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID),
                            new Begegnung(23, rundeUUID1, 1, 1, 1, 23,
                                    new Wettkaempfer(9, "Teilnehmer I", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                                    new Wettkaempfer(7, "Teilnehmer G", Geschlecht.m, Altersklasse.U11, new Verein(7, "Verein7", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID),
                            new Begegnung(24, rundeUUID1, 1, 1, 1, 24,
                                    new Wettkaempfer(10, "Teilnehmer J", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                                    new Wettkaempfer(11, "Teilnehmer K", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID))
                ),
                new BegegnungenJeRunde(Arrays.asList(
                            new Begegnung(25, rundeUUID1, 1, 1, 1, 25,
                                    new Wettkaempfer(12, "Teilnehmer L", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                                    new Wettkaempfer(11, "Teilnehmer K", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID),
                            new Begegnung(26, rundeUUID1, 1, 1, 1, 26,
                                    new Wettkaempfer(7, "Teilnehmer G", Geschlecht.m, Altersklasse.U11, new Verein(7, "Verein7", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                                    new Wettkaempfer(10, "Teilnehmer J", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID),
                            new Begegnung(27, rundeUUID1, 1, 1, 1, 27,
                                    new Wettkaempfer(8, "Teilnehmer H", Geschlecht.m, Altersklasse.U11, new Verein(8, "Verein8", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                                    new Wettkaempfer(9, "Teilnehmer I", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID))
                ),
                new BegegnungenJeRunde(Arrays.asList(
                            new Begegnung(28, rundeUUID1, 1, 1, 1, 28,
                                    new Wettkaempfer(9, "Teilnehmer I", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                                    new Wettkaempfer(12, "Teilnehmer L", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID),
                            new Begegnung(29, rundeUUID1, 1, 1, 1, 29,
                                    new Wettkaempfer(10, "Teilnehmer J", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                                    new Wettkaempfer(8, "Teilnehmer H", Geschlecht.m, Altersklasse.U11, new Verein(8, "Verein8", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID),
                            new Begegnung(30, rundeUUID1, 1, 1, 1, 30,
                                    new Wettkaempfer(11, "Teilnehmer K", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                                    new Wettkaempfer(7, "Teilnehmer G", Geschlecht.m, Altersklasse.U11, new Verein(7, "Verein7", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID))
                )
            ), turnierUUID),
            new WettkampfGruppe(300, "Katze", "(Gewichtskl.1 U11)", Arrays.asList(
                new BegegnungenJeRunde(Arrays.asList(
                            new Begegnung(31, rundeUUID1, 1, 1, 1, 31,
                                    new Wettkaempfer(13, "Teilnehmer M", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                                    new Wettkaempfer(18, "Teilnehmer R", Geschlecht.m, Altersklasse.U11, new Verein(8, "Verein8", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID),
                            new Begegnung(32, rundeUUID1, 1, 1, 1, 32,
                                    new Wettkaempfer(14, "Teilnehmer N", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                                    new Wettkaempfer(19, "Teilnehmer Q", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID),
                            new Begegnung(33, rundeUUID1, 1, 1, 1, 33,
                                    new Wettkaempfer(15, "Teilnehmer O", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                                    new Wettkaempfer(16, "Teilnehmer P", Geschlecht.m, Altersklasse.U11, new Verein(6, "Verein6", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID))
                ),
                new BegegnungenJeRunde(Arrays.asList(
                            new Begegnung(34, rundeUUID1, 1, 1, 1, 34,
                                    new Wettkaempfer(18, "Teilnehmer R", Geschlecht.m, Altersklasse.U11, new Verein(8, "Verein8", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                                    new Wettkaempfer(16, "Teilnehmer P", Geschlecht.m, Altersklasse.U11, new Verein(6, "Verein6", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID),
                            new Begegnung(35, rundeUUID1, 1, 1, 1, 35,
                                    new Wettkaempfer(19, "Teilnehmer Q", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                                    new Wettkaempfer(15, "Teilnehmer O", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID),
                            new Begegnung(36, rundeUUID1, 1, 1, 1, 36,
                                    new Wettkaempfer(13, "Teilnehmer M", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                                    new Wettkaempfer(14, "Teilnehmer N", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID))
                ),
                new BegegnungenJeRunde(Arrays.asList(
                            new Begegnung(37, rundeUUID1, 1, 1, 1, 37,
                                    new Wettkaempfer(14, "Teilnehmer N", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                                    new Wettkaempfer(18, "Teilnehmer R", Geschlecht.m, Altersklasse.U11, new Verein(8, "Verein8", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID),
                            new Begegnung(38, rundeUUID1, 1, 1, 1, 38,
                                    new Wettkaempfer(15, "Teilnehmer O", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                                    new Wettkaempfer(13, "Teilnehmer M", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID),
                            new Begegnung(39, rundeUUID1, 1, 1, 1, 39,
                                    new Wettkaempfer(16, "Teilnehmer P", Geschlecht.m, Altersklasse.U11, new Verein(6, "Verein6", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                                    new Wettkaempfer(19, "Teilnehmer Q", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID))
                ),
                new BegegnungenJeRunde(Arrays.asList(
                            new Begegnung(40, rundeUUID1, 1, 1, 1, 40,
                                    new Wettkaempfer(18, "Teilnehmer R", Geschlecht.m, Altersklasse.U11, new Verein(8, "Verein8", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                                    new Wettkaempfer(19, "Teilnehmer Q", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID),
                            new Begegnung(41, rundeUUID1, 1, 1, 1, 41,
                                    new Wettkaempfer(13, "Teilnehmer M", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                                    new Wettkaempfer(16, "Teilnehmer P", Geschlecht.m, Altersklasse.U11, new Verein(6, "Verein6", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID),
                            new Begegnung(42, rundeUUID1, 1, 1, 1, 42,
                                    new Wettkaempfer(14, "Teilnehmer N", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                                    new Wettkaempfer(15, "Teilnehmer O", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID))
                ),
                new BegegnungenJeRunde(Arrays.asList(
                            new Begegnung(43, rundeUUID1, 1, 1, 1, 43,
                                    new Wettkaempfer(15, "Teilnehmer O", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                                    new Wettkaempfer(18, "Teilnehmer R", Geschlecht.m, Altersklasse.U11, new Verein(8, "Verein8", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID),
                            new Begegnung(44, rundeUUID1, 1, 1, 1, 44,
                                    new Wettkaempfer(16, "Teilnehmer P", Geschlecht.m, Altersklasse.U11, new Verein(6, "Verein6", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                                    new Wettkaempfer(14, "Teilnehmer N", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID),
                            new Begegnung(45, rundeUUID1, 1, 1, 1, 45,
                                    new Wettkaempfer(19, "Teilnehmer Q", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                                    new Wettkaempfer(13, "Teilnehmer M", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID))
                )
            ), turnierUUID),
            new WettkampfGruppe(400, "Maus", "(Gewichtskl.1 U11)", Arrays.asList(
                new BegegnungenJeRunde(Arrays.asList(
                            new Begegnung(46, rundeUUID1, 1, 1, 1, 46,
                                    new Wettkaempfer(19, "Teilnehmer S", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                                    new Wettkaempfer(22, "Teilnehmer V", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID),
                            new Begegnung(47, rundeUUID1, 1, 1, 1, 47,
                                    new Wettkaempfer(20, "Teilnehmer T", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                                    new Wettkaempfer(21, "Teilnehmer U", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID))
                ),
                new BegegnungenJeRunde(Arrays.asList(
                            new Begegnung(48, rundeUUID1, 1, 1, 1, 48,
                                    new Wettkaempfer(22, "Teilnehmer V", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                                    new Wettkaempfer(21, "Teilnehmer U", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID),
                            new Begegnung(49, rundeUUID1, 1, 1, 1, 49,
                                    new Wettkaempfer(19, "Teilnehmer S", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                                    new Wettkaempfer(20, "Teilnehmer T", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID))
                ),
                new BegegnungenJeRunde(Arrays.asList(
                            new Begegnung(50, rundeUUID1, 1, 1, 1, 50,
                                    new Wettkaempfer(20, "Teilnehmer T", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                                    new Wettkaempfer(22, "Teilnehmer V", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID),
                            new Begegnung(51, rundeUUID1, 1, 1, 1, 51,
                                    new Wettkaempfer(21, "Teilnehmer U", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                                    new Wettkaempfer(19, "Teilnehmer S", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID))
                )
            ), turnierUUID)
    );



    public static List<WettkampfGruppe>  wks2 = Arrays.asList(
        new WettkampfGruppe(100, "Antilope", "(Gewichtskl.1 U11)", Arrays.asList(
            new BegegnungenJeRunde(Arrays.asList(
                new Begegnung(1, rundeUUID1, 1, 1, 1, 1,
                        new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                        new Wettkaempfer(4, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID),
                new Begegnung(2, rundeUUID1, 1, 1, 1, 2,
                        new Wettkaempfer(2, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                        new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID))
            ),
            new BegegnungenJeRunde(Arrays.asList(
                new Begegnung(3, rundeUUID1, 1, 1, 1, 3,
                        new Wettkaempfer(4, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                        new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID),
                new Begegnung(4, rundeUUID1, 1, 1, 1, 4,
                        new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                        new Wettkaempfer(2, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID))
            ),
            new BegegnungenJeRunde(Arrays.asList(
                new Begegnung(5, rundeUUID1, 1, 1, 1, 5,
                        new Wettkaempfer(2, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                        new Wettkaempfer(4, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID),
                new Begegnung(6, rundeUUID1, 1, 1, 1, 6,
                        new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                        new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.ORANGE), true, true, turnierUUID), null, null, turnierUUID))
            )
        ), turnierUUID)
    );
}
