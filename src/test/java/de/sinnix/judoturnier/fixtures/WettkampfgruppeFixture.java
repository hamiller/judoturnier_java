package de.sinnix.judoturnier.fixtures;

import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Begegnung;
import de.sinnix.judoturnier.model.Farbe;
import de.sinnix.judoturnier.model.Geschlecht;
import de.sinnix.judoturnier.model.Verein;
import de.sinnix.judoturnier.model.Wettkaempfer;
import de.sinnix.judoturnier.model.WettkampfGruppe;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class WettkampfgruppeFixture {

    public static List<WettkampfGruppe> wks = Arrays.asList(new WettkampfGruppe(100, "Antilope", "(Gewichtskl.1 U11)", Arrays.asList(
                Arrays.asList(
                    new Begegnung(1, 1, 1,1,
                            new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                            new Wettkaempfer(6, "Teilnehmer F", Geschlecht.m, Altersklasse.U11, new Verein(6, "Verein6"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                    new Begegnung(2,1, 1,1,
                        new Wettkaempfer(2, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                            new Wettkaempfer(5, "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                    new Begegnung(3,1, 1,1,
                        new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                            new Wettkaempfer(4, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4"), 24.0, Optional.of(Farbe.ORANGE), true, true), null)
                ),
                Arrays.asList(
                    new Begegnung(4,1, 1,1,
                        new Wettkaempfer(6, "Teilnehmer F", Geschlecht.m, Altersklasse.U11, new Verein(6, "Verein6"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                            new Wettkaempfer(4, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                    new Begegnung(5,1, 1,1,
                        new Wettkaempfer(5, "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                            new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                    new Begegnung(6,1, 1,1,
                        new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                            new Wettkaempfer(2, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true), null)
                ),
                Arrays.asList(
                    new Begegnung(7,1, 1,1,
                        new Wettkaempfer(2, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                            new Wettkaempfer(6, "Teilnehmer F", Geschlecht.m, Altersklasse.U11, new Verein(6, "Verein6"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                    new Begegnung(8,1, 1,1,
                        new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                            new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                    new Begegnung(9,1, 1,1,
                        new Wettkaempfer(4, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                            new Wettkaempfer(5, "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5"), 24.0, Optional.of(Farbe.ORANGE), true, true), null
                    )
                ),
                Arrays.asList(
                    new Begegnung(10,1, 1,1,
                        new Wettkaempfer(6, "Teilnehmer F", Geschlecht.m, Altersklasse.U11, new Verein(6, "Verein6"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                            new Wettkaempfer(5, "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                    new Begegnung(11,1, 1,1,
                        new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                            new Wettkaempfer(4, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                    new Begegnung(12,1, 1,1,
                        new Wettkaempfer(2, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                            new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3"), 24.0, Optional.of(Farbe.ORANGE), true, true), null
                    )
                ),
                Arrays.asList(
                    new Begegnung(13, 1, 1, 1,
                        new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                            new Wettkaempfer(6, "Teilnehmer F", Geschlecht.m, Altersklasse.U11, new Verein(6, "Verein6"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                    new Begegnung(14, 1, 1, 1,
                            new Wettkaempfer(4, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                            new Wettkaempfer(2, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                    new Begegnung(15, 1, 1, 1,
                            new Wettkaempfer(5, "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                            new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true), null
                    )
                )
            )),
            new WettkampfGruppe(200, "Eule", "(Gewichtskl.1 U11)", Arrays.asList(
                    Arrays.asList(
                            new Begegnung(16, 1, 1, 1,
                                    new Wettkaempfer(7, "Teilnehmer G", Geschlecht.m, Altersklasse.U11, new Verein(7, "Verein7"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(12, "Teilnehmer L", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(17, 1, 1, 1,
                                    new Wettkaempfer(8, "Teilnehmer H", Geschlecht.m, Altersklasse.U11, new Verein(8, "Verein8"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(11, "Teilnehmer K", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(18, 1, 1, 1,
                                    new Wettkaempfer(9, "Teilnehmer I", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(10, "Teilnehmer J", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true), null)),
                    Arrays.asList(
                            new Begegnung(19, 1, 1, 1,
                                    new Wettkaempfer(12, "Teilnehmer L", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(10, "Teilnehmer J", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(20, 1, 1, 1,
                                    new Wettkaempfer(11, "Teilnehmer K", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(9, "Teilnehmer I", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(21, 1, 1, 1,
                                    new Wettkaempfer(7, "Teilnehmer G", Geschlecht.m, Altersklasse.U11, new Verein(7, "Verein7"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(8, "Teilnehmer H", Geschlecht.m, Altersklasse.U11, new Verein(8, "Verein8"), 24.0, Optional.of(Farbe.ORANGE), true, true), null)),
                    Arrays.asList(
                            new Begegnung(22, 1, 1, 1,
                                    new Wettkaempfer(8, "Teilnehmer H", Geschlecht.m, Altersklasse.U11, new Verein(8, "Verein8"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(12, "Teilnehmer L", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(23, 1, 1, 1,
                                    new Wettkaempfer(9, "Teilnehmer I", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(7, "Teilnehmer G", Geschlecht.m, Altersklasse.U11, new Verein(7, "Verein7"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(24, 1, 1, 1,
                                    new Wettkaempfer(10, "Teilnehmer J", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(11, "Teilnehmer K", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true), null)),
                    Arrays.asList(
                            new Begegnung(25, 1, 1, 1,
                                    new Wettkaempfer(12, "Teilnehmer L", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(11, "Teilnehmer K", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(26, 1, 1, 1,
                                    new Wettkaempfer(7, "Teilnehmer G", Geschlecht.m, Altersklasse.U11, new Verein(7, "Verein7"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(10, "Teilnehmer J", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(27, 1, 1, 1,
                                    new Wettkaempfer(8, "Teilnehmer H", Geschlecht.m, Altersklasse.U11, new Verein(8, "Verein8"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(9, "Teilnehmer I", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9"), 24.0, Optional.of(Farbe.ORANGE), true, true), null)),
                    Arrays.asList(
                            new Begegnung(28, 1, 1, 1,
                                    new Wettkaempfer(9, "Teilnehmer I", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(12, "Teilnehmer L", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(29, 1, 1, 1,
                                    new Wettkaempfer(10, "Teilnehmer J", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(8, "Teilnehmer H", Geschlecht.m, Altersklasse.U11, new Verein(8, "Verein8"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(30, 1, 1, 1,
                                    new Wettkaempfer(11, "Teilnehmer K", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(7, "Teilnehmer G", Geschlecht.m, Altersklasse.U11, new Verein(7, "Verein7"), 24.0, Optional.of(Farbe.ORANGE), true, true), null
                            )
                    )
            )),
            new WettkampfGruppe(300, "Katze", "(Gewichtskl.1 U11)", Arrays.asList(
                    Arrays.asList(
                            new Begegnung(31, 1, 1, 1,
                                    new Wettkaempfer(13, "Teilnehmer M", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(18, "Teilnehmer R", Geschlecht.m, Altersklasse.U11, new Verein(8, "Verein8"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(32, 1, 1, 1,
                                    new Wettkaempfer(14, "Teilnehmer N", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(19, "Teilnehmer Q", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(33, 1, 1, 1,
                                    new Wettkaempfer(15, "Teilnehmer O", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(16, "Teilnehmer P", Geschlecht.m, Altersklasse.U11, new Verein(6, "Verein6"), 24.0, Optional.of(Farbe.ORANGE), true, true), null)),
                    Arrays.asList(
                            new Begegnung(34, 1, 1, 1,
                                    new Wettkaempfer(18, "Teilnehmer R", Geschlecht.m, Altersklasse.U11, new Verein(8, "Verein8"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(16, "Teilnehmer P", Geschlecht.m, Altersklasse.U11, new Verein(6, "Verein6"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(35, 1, 1, 1,
                                    new Wettkaempfer(19, "Teilnehmer Q", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(15, "Teilnehmer O", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(36, 1, 1, 1,
                                    new Wettkaempfer(13, "Teilnehmer M", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(14, "Teilnehmer N", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4"), 24.0, Optional.of(Farbe.ORANGE), true, true), null)),
                    Arrays.asList(
                            new Begegnung(37, 1, 1, 1,
                                    new Wettkaempfer(14, "Teilnehmer N", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(18, "Teilnehmer R", Geschlecht.m, Altersklasse.U11, new Verein(8, "Verein8"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(38, 1, 1, 1,
                                    new Wettkaempfer(15, "Teilnehmer O", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(13, "Teilnehmer M", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(39, 1, 1, 1,
                                    new Wettkaempfer(16, "Teilnehmer P", Geschlecht.m, Altersklasse.U11, new Verein(6, "Verein6"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(19, "Teilnehmer Q", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9"), 24.0, Optional.of(Farbe.ORANGE), true, true), null)),
                    Arrays.asList(
                            new Begegnung(40, 1, 1, 1,
                                    new Wettkaempfer(18, "Teilnehmer R", Geschlecht.m, Altersklasse.U11, new Verein(8, "Verein8"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(19, "Teilnehmer Q", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(41, 1, 1, 1,
                                    new Wettkaempfer(13, "Teilnehmer M", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(16, "Teilnehmer P", Geschlecht.m, Altersklasse.U11, new Verein(6, "Verein6"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(42, 1, 1, 1,
                                    new Wettkaempfer(14, "Teilnehmer N", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(15, "Teilnehmer O", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5"), 24.0, Optional.of(Farbe.ORANGE), true, true), null)),
                    Arrays.asList(
                            new Begegnung(43, 1, 1, 1,
                                    new Wettkaempfer(15, "Teilnehmer O", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(18, "Teilnehmer R", Geschlecht.m, Altersklasse.U11, new Verein(8, "Verein8"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(44, 1, 1, 1,
                                    new Wettkaempfer(16, "Teilnehmer P", Geschlecht.m, Altersklasse.U11, new Verein(6, "Verein6"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(14, "Teilnehmer N", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(45, 1, 1, 1,
                                    new Wettkaempfer(19, "Teilnehmer Q", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(13, "Teilnehmer M", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3"), 24.0, Optional.of(Farbe.ORANGE), true, true), null
                            )
                    ))),
            new WettkampfGruppe(400, "Maus", "(Gewichtskl.1 U11)", Arrays.asList(
                    Arrays.asList(
                            new Begegnung(46, 1, 1, 1,
                                    new Wettkaempfer(19, "Teilnehmer S", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(22, "Teilnehmer V", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(47, 1, 1, 1,
                                    new Wettkaempfer(20, "Teilnehmer T", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(21, "Teilnehmer U", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true), null)),
                    Arrays.asList(
                            new Begegnung(48, 1, 1, 1,
                                    new Wettkaempfer(22, "Teilnehmer V", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(21, "Teilnehmer U", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(49, 1, 1, 1,
                                    new Wettkaempfer(19, "Teilnehmer S", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(20, "Teilnehmer T", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true), null)),
                    Arrays.asList(
                            new Begegnung(50, 1, 1, 1,
                                    new Wettkaempfer(20, "Teilnehmer T", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(22, "Teilnehmer V", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(51, 1, 1, 1,
                                    new Wettkaempfer(21, "Teilnehmer U", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(19, "Teilnehmer S", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9"), 24.0, Optional.of(Farbe.ORANGE), true, true), null
                            )
                    )
            )),
            new WettkampfGruppe(500, "Tiger", "(Gewichtskl.1 U11)", Arrays.asList(
                    Arrays.asList(
                            new Begegnung(52, 1, 1, 1,
                                    new Wettkaempfer(24, "Teilnehmer X", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(25, "Teilnehmer Y", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5"), 24.0, Optional.of(Farbe.ORANGE), true, true), null)),
                    Arrays.asList(
                            new Begegnung(53, 1, 1, 1,
                                    new Wettkaempfer(23, "Teilnehmer W", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(24, "Teilnehmer X", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4"), 24.0, Optional.of(Farbe.ORANGE), true, true), null)),
                    Arrays.asList(
                            new Begegnung(54, 1, 1, 1,
                                    new Wettkaempfer(25, "Teilnehmer Y", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(23, "Teilnehmer W", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3"), 24.0, Optional.of(Farbe.ORANGE), true, true), null
                            )
                    )
            ))
    );

    public static List<WettkampfGruppe> wks_gerade = Arrays.asList(
            new WettkampfGruppe(100, "Antilope", "(Gewichtskl.1 U11)", Arrays.asList(
                    Arrays.asList(
                            new Begegnung(1, 1, 1, 1,
                                    new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(6, "Teilnehmer F", Geschlecht.m, Altersklasse.U11, new Verein(6, "Verein6"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(2, 1, 1, 1,
                                    new Wettkaempfer(2, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(5, "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(3, 1, 1, 1,
                                    new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(4, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4"), 24.0, Optional.of(Farbe.ORANGE), true, true), null)),
                    Arrays.asList(
                            new Begegnung(4, 1, 1, 1,
                                    new Wettkaempfer(6, "Teilnehmer F", Geschlecht.m, Altersklasse.U11, new Verein(6, "Verein6"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(4, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(5, 1, 1, 1,
                                    new Wettkaempfer(5, "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(6, 1, 1, 1,
                                    new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(2, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true), null)),
                    Arrays.asList(
                            new Begegnung(7, 1, 1, 1,
                                    new Wettkaempfer(2, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(6, "Teilnehmer F", Geschlecht.m, Altersklasse.U11, new Verein(6, "Verein6"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(8, 1, 1, 1,
                                    new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(9, 1, 1, 1,
                                    new Wettkaempfer(4, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(5, "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5"), 24.0, Optional.of(Farbe.ORANGE), true, true), null)),
                    Arrays.asList(
                            new Begegnung(10, 1, 1, 1,
                                    new Wettkaempfer(6, "Teilnehmer F", Geschlecht.m, Altersklasse.U11, new Verein(6, "Verein6"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(5, "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(11, 1, 1, 1,
                                    new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(4, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(12, 1, 1, 1,
                                    new Wettkaempfer(2, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3"), 24.0, Optional.of(Farbe.ORANGE), true, true), null)),
                    Arrays.asList(
                            new Begegnung(13, 1, 1, 1,
                                    new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(6, "Teilnehmer F", Geschlecht.m, Altersklasse.U11, new Verein(6, "Verein6"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(14, 1, 1, 1,
                                    new Wettkaempfer(4, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(2, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(15, 1, 1, 1,
                                    new Wettkaempfer(5, "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true), null
                            )
                    )
            )),
            new WettkampfGruppe(200, "Eule", "(Gewichtskl.1 U11)", Arrays.asList(
                    Arrays.asList(
                            new Begegnung(16, 1, 1, 1,
                                    new Wettkaempfer(7, "Teilnehmer G", Geschlecht.m, Altersklasse.U11, new Verein(7, "Verein7"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(12, "Teilnehmer L", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(17, 1, 1, 1,
                                    new Wettkaempfer(8, "Teilnehmer H", Geschlecht.m, Altersklasse.U11, new Verein(8, "Verein8"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(11, "Teilnehmer K", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(18, 1, 1, 1,
                                    new Wettkaempfer(9, "Teilnehmer I", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(10, "Teilnehmer J", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true), null)),
                    Arrays.asList(
                            new Begegnung(19, 1, 1, 1,
                                    new Wettkaempfer(12, "Teilnehmer L", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(10, "Teilnehmer J", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(20, 1, 1, 1,
                                    new Wettkaempfer(11, "Teilnehmer K", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(9, "Teilnehmer I", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(21, 1, 1, 1,
                                    new Wettkaempfer(7, "Teilnehmer G", Geschlecht.m, Altersklasse.U11, new Verein(7, "Verein7"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(8, "Teilnehmer H", Geschlecht.m, Altersklasse.U11, new Verein(8, "Verein8"), 24.0, Optional.of(Farbe.ORANGE), true, true), null)),
                    Arrays.asList(
                            new Begegnung(22, 1, 1, 1,
                                    new Wettkaempfer(8, "Teilnehmer H", Geschlecht.m, Altersklasse.U11, new Verein(8, "Verein8"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(12, "Teilnehmer L", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(23, 1, 1, 1,
                                    new Wettkaempfer(9, "Teilnehmer I", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(7, "Teilnehmer G", Geschlecht.m, Altersklasse.U11, new Verein(7, "Verein7"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(24, 1, 1, 1,
                                    new Wettkaempfer(10, "Teilnehmer J", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(11, "Teilnehmer K", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true), null)),
                    Arrays.asList(
                            new Begegnung(25, 1, 1, 1,
                                    new Wettkaempfer(12, "Teilnehmer L", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(11, "Teilnehmer K", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(26, 1, 1, 1,
                                    new Wettkaempfer(7, "Teilnehmer G", Geschlecht.m, Altersklasse.U11, new Verein(7, "Verein7"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(10, "Teilnehmer J", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(27, 1, 1, 1,
                                    new Wettkaempfer(8, "Teilnehmer H", Geschlecht.m, Altersklasse.U11, new Verein(8, "Verein8"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(9, "Teilnehmer I", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9"), 24.0, Optional.of(Farbe.ORANGE), true, true), null)),
                    Arrays.asList(
                            new Begegnung(28, 1, 1, 1,
                                    new Wettkaempfer(9, "Teilnehmer I", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(12, "Teilnehmer L", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(29, 1, 1, 1,
                                    new Wettkaempfer(10, "Teilnehmer J", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(8, "Teilnehmer H", Geschlecht.m, Altersklasse.U11, new Verein(8, "Verein8"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(30, 1, 1, 1,
                                    new Wettkaempfer(11, "Teilnehmer K", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(7, "Teilnehmer G", Geschlecht.m, Altersklasse.U11, new Verein(7, "Verein7"), 24.0, Optional.of(Farbe.ORANGE), true, true), null
                            )
                    )
            )),
            new WettkampfGruppe(300, "Katze", "(Gewichtskl.1 U11)", Arrays.asList(
                    Arrays.asList(
                            new Begegnung(31, 1, 1, 1,
                                    new Wettkaempfer(13, "Teilnehmer M", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(18, "Teilnehmer R", Geschlecht.m, Altersklasse.U11, new Verein(8, "Verein8"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(32, 1, 1, 1,
                                    new Wettkaempfer(14, "Teilnehmer N", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(19, "Teilnehmer Q", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(33, 1, 1, 1,
                                    new Wettkaempfer(15, "Teilnehmer O", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(16, "Teilnehmer P", Geschlecht.m, Altersklasse.U11, new Verein(6, "Verein6"), 24.0, Optional.of(Farbe.ORANGE), true, true), null)),
                    Arrays.asList(
                            new Begegnung(34, 1, 1, 1,
                                    new Wettkaempfer(18, "Teilnehmer R", Geschlecht.m, Altersklasse.U11, new Verein(8, "Verein8"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(16, "Teilnehmer P", Geschlecht.m, Altersklasse.U11, new Verein(6, "Verein6"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(35, 1, 1, 1,
                                    new Wettkaempfer(19, "Teilnehmer Q", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(15, "Teilnehmer O", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(36, 1, 1, 1,
                                    new Wettkaempfer(13, "Teilnehmer M", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(14, "Teilnehmer N", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4"), 24.0, Optional.of(Farbe.ORANGE), true, true), null)),
                    Arrays.asList(
                            new Begegnung(37, 1, 1, 1,
                                    new Wettkaempfer(14, "Teilnehmer N", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(18, "Teilnehmer R", Geschlecht.m, Altersklasse.U11, new Verein(8, "Verein8"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(38, 1, 1, 1,
                                    new Wettkaempfer(15, "Teilnehmer O", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(13, "Teilnehmer M", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(39, 1, 1, 1,
                                    new Wettkaempfer(16, "Teilnehmer P", Geschlecht.m, Altersklasse.U11, new Verein(6, "Verein6"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(19, "Teilnehmer Q", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9"), 24.0, Optional.of(Farbe.ORANGE), true, true), null)),
                    Arrays.asList(
                            new Begegnung(40, 1, 1, 1,
                                    new Wettkaempfer(18, "Teilnehmer R", Geschlecht.m, Altersklasse.U11, new Verein(8, "Verein8"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(19, "Teilnehmer Q", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(41, 1, 1, 1,
                                    new Wettkaempfer(13, "Teilnehmer M", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(16, "Teilnehmer P", Geschlecht.m, Altersklasse.U11, new Verein(6, "Verein6"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(42, 1, 1, 1,
                                    new Wettkaempfer(14, "Teilnehmer N", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(15, "Teilnehmer O", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5"), 24.0, Optional.of(Farbe.ORANGE), true, true), null)),
                    Arrays.asList(
                            new Begegnung(43, 1, 1, 1,
                                    new Wettkaempfer(15, "Teilnehmer O", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(18, "Teilnehmer R", Geschlecht.m, Altersklasse.U11, new Verein(8, "Verein8"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(44, 1, 1, 1,
                                    new Wettkaempfer(16, "Teilnehmer P", Geschlecht.m, Altersklasse.U11, new Verein(6, "Verein6"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(14, "Teilnehmer N", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(45, 1, 1, 1,
                                    new Wettkaempfer(19, "Teilnehmer Q", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(13, "Teilnehmer M", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3"), 24.0, Optional.of(Farbe.ORANGE), true, true), null
                            )
                    )
            )),
            new WettkampfGruppe(400, "Maus", "(Gewichtskl.1 U11)", Arrays.asList(
                    Arrays.asList(
                            new Begegnung(46, 1, 1, 1,
                                    new Wettkaempfer(19, "Teilnehmer S", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(22, "Teilnehmer V", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(47, 1, 1, 1,
                                    new Wettkaempfer(20, "Teilnehmer T", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(21, "Teilnehmer U", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true), null)),
                    Arrays.asList(
                            new Begegnung(48, 1, 1, 1,
                                    new Wettkaempfer(22, "Teilnehmer V", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(21, "Teilnehmer U", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(49, 1, 1, 1,
                                    new Wettkaempfer(19, "Teilnehmer S", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(20, "Teilnehmer T", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true), null)),
                    Arrays.asList(
                            new Begegnung(50, 1, 1, 1,
                                    new Wettkaempfer(20, "Teilnehmer T", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(22, "Teilnehmer V", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(51, 1, 1, 1,
                                    new Wettkaempfer(21, "Teilnehmer U", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(19, "Teilnehmer S", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9"), 24.0, Optional.of(Farbe.ORANGE), true, true), null
                            )
                    )
            ))
    );

    public static List<WettkampfGruppe> wks_gerade_unterschiedlich = Arrays.asList(
            new WettkampfGruppe(100, "Antilope", "(Gewichtskl.1 U11)", Arrays.asList(
                    Arrays.asList(
                            new Begegnung(1, 1, 1, 1,
                                    new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(6, "Teilnehmer F", Geschlecht.m, Altersklasse.U11, new Verein(6, "Verein6"), 24.0, Optional.of(Farbe.ORANGE), true, true), null
                            )
                    )
            )),
            new WettkampfGruppe(200, "Eule", "(Gewichtskl.1 U11)", Arrays.asList(
                    Arrays.asList(
                            new Begegnung(2, 1, 1, 1,
                                    new Wettkaempfer(7, "Teilnehmer G", Geschlecht.m, Altersklasse.U11, new Verein(7, "Verein7"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(12, "Teilnehmer L", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(3, 1, 1, 1,
                                    new Wettkaempfer(8, "Teilnehmer H", Geschlecht.m, Altersklasse.U11, new Verein(8, "Verein8"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(11, "Teilnehmer K", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(4, 1, 1, 1,
                                    new Wettkaempfer(9, "Teilnehmer I", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(10, "Teilnehmer J", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true), null)),
                    Arrays.asList(
                            new Begegnung(5, 1, 1, 1,
                                    new Wettkaempfer(12, "Teilnehmer L", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(10, "Teilnehmer J", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(6, 1, 1, 1,
                                    new Wettkaempfer(11, "Teilnehmer K", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(9, "Teilnehmer I", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(7, 1, 1, 1,
                                    new Wettkaempfer(7, "Teilnehmer G", Geschlecht.m, Altersklasse.U11, new Verein(7, "Verein7"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(8, "Teilnehmer H", Geschlecht.m, Altersklasse.U11, new Verein(8, "Verein8"), 24.0, Optional.of(Farbe.ORANGE), true, true), null)),
                    Arrays.asList(
                            new Begegnung(8, 1, 1, 1,
                                    new Wettkaempfer(8, "Teilnehmer H", Geschlecht.m, Altersklasse.U11, new Verein(8, "Verein8"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(12, "Teilnehmer L", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(9, 1, 1, 1,
                                    new Wettkaempfer(9, "Teilnehmer I", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(7, "Teilnehmer G", Geschlecht.m, Altersklasse.U11, new Verein(7, "Verein7"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(10, 1, 1, 1,
                                    new Wettkaempfer(10, "Teilnehmer J", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(11, "Teilnehmer K", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true), null)),
                    Arrays.asList(
                            new Begegnung(11, 1, 1, 1,
                                    new Wettkaempfer(12, "Teilnehmer L", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(11, "Teilnehmer K", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(12, 1, 1, 1,
                                    new Wettkaempfer(7, "Teilnehmer G", Geschlecht.m, Altersklasse.U11, new Verein(7, "Verein7"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(10, "Teilnehmer J", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(13, 1, 1, 1,
                                    new Wettkaempfer(8, "Teilnehmer H", Geschlecht.m, Altersklasse.U11, new Verein(8, "Verein8"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(9, "Teilnehmer I", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9"), 24.0, Optional.of(Farbe.ORANGE), true, true), null)),
                    Arrays.asList(
                            new Begegnung(14, 1, 1, 1,
                                    new Wettkaempfer(9, "Teilnehmer I", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(12, "Teilnehmer L", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(15, 1, 1, 1,
                                    new Wettkaempfer(10, "Teilnehmer J", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(8, "Teilnehmer H", Geschlecht.m, Altersklasse.U11, new Verein(8, "Verein8"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(16, 1, 1, 1,
                                    new Wettkaempfer(11, "Teilnehmer K", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(7, "Teilnehmer G", Geschlecht.m, Altersklasse.U11, new Verein(7, "Verein7"), 24.0, Optional.of(Farbe.ORANGE), true, true), null
                            )
                    )
            ))
    );

    public static List<WettkampfGruppe>  wks2 = Arrays.asList(
        new WettkampfGruppe(100, "Antilope", "(Gewichtskl.1 U11)", Arrays.asList(
            Arrays.asList(
                new Begegnung(1, 1, 1, 1,
                        new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                        new Wettkaempfer(4, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                new Begegnung(2, 1, 1, 1,
                        new Wettkaempfer(2, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                        new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3"), 24.0, Optional.of(Farbe.ORANGE), true, true), null)),
            Arrays.asList(
                new Begegnung(3, 1, 1, 1,
                        new Wettkaempfer(4, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                        new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                new Begegnung(4, 1, 1, 1,
                        new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                        new Wettkaempfer(2, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true), null)),
            Arrays.asList(
                new Begegnung(5, 1, 1, 1,
                        new Wettkaempfer(2, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                        new Wettkaempfer(4, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                new Begegnung(6, 1, 1, 1,
                        new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                        new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true), null))
        ))
    );
}
