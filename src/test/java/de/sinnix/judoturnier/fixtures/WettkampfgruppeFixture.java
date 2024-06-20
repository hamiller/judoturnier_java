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
                    new Begegnung(1,
                            new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                            new Wettkaempfer(6, "Teilnehmer F", Geschlecht.m, Altersklasse.U11, new Verein(6, "Verein6"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                    new Begegnung(2,
                            new Wettkaempfer(2, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                            new Wettkaempfer(5, "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                    new Begegnung(3,
                            new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                            new Wettkaempfer(4, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4"), 24.0, Optional.of(Farbe.ORANGE), true, true), null)
                ),
                Arrays.asList(
                    new Begegnung(4,
                            new Wettkaempfer(6, "Teilnehmer F", Geschlecht.m, Altersklasse.U11, new Verein(6, "Verein6"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                            new Wettkaempfer(4, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                    new Begegnung(5,
                            new Wettkaempfer(5, "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                            new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                    new Begegnung(6,
                            new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                            new Wettkaempfer(2, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true), null)
                ),
                Arrays.asList(
                    new Begegnung(7,
                            new Wettkaempfer(2, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                            new Wettkaempfer(6, "Teilnehmer F", Geschlecht.m, Altersklasse.U11, new Verein(6, "Verein6"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                    new Begegnung(8,
                            new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                            new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                    new Begegnung(9,
                            new Wettkaempfer(4, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                            new Wettkaempfer(5, "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5"), 24.0, Optional.of(Farbe.ORANGE), true, true), null
                    )
                ),
                Arrays.asList(
                    new Begegnung(10,
                            new Wettkaempfer(6, "Teilnehmer F", Geschlecht.m, Altersklasse.U11, new Verein(6, "Verein6"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                            new Wettkaempfer(5, "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                    new Begegnung(11,
                            new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                            new Wettkaempfer(4, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                    new Begegnung(12,
                            new Wettkaempfer(2, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                            new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3"), 24.0, Optional.of(Farbe.ORANGE), true, true), null
                    )
                ),
                Arrays.asList(
                    new Begegnung(13,
                            new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                            new Wettkaempfer(6, "Teilnehmer F", Geschlecht.m, Altersklasse.U11, new Verein(6, "Verein6"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                    new Begegnung(14,
                            new Wettkaempfer(4, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                            new Wettkaempfer(2, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                    new Begegnung(15,
                            new Wettkaempfer(5, "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                            new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true), null
                    )
                )
            )),
            new WettkampfGruppe(200, "Eule", "(Gewichtskl.1 U11)", Arrays.asList(
                    Arrays.asList(
                            new Begegnung(16,
                                    new Wettkaempfer(7, "Teilnehmer G", Geschlecht.m, Altersklasse.U11, new Verein(7, "Verein7"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(12, "Teilnehmer L", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(17,
                                    new Wettkaempfer(8, "Teilnehmer H", Geschlecht.m, Altersklasse.U11, new Verein(8, "Verein8"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(11, "Teilnehmer K", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(18,
                                    new Wettkaempfer(9, "Teilnehmer I", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(10, "Teilnehmer J", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true), null)),
                    Arrays.asList(
                            new Begegnung(19,
                                    new Wettkaempfer(12, "Teilnehmer L", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(10, "Teilnehmer J", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(20,
                                    new Wettkaempfer(11, "Teilnehmer K", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(9, "Teilnehmer I", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(21,
                                    new Wettkaempfer(7, "Teilnehmer G", Geschlecht.m, Altersklasse.U11, new Verein(7, "Verein7"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(8, "Teilnehmer H", Geschlecht.m, Altersklasse.U11, new Verein(8, "Verein8"), 24.0, Optional.of(Farbe.ORANGE), true, true), null)),
                    Arrays.asList(
                            new Begegnung(22,
                                    new Wettkaempfer(8, "Teilnehmer H", Geschlecht.m, Altersklasse.U11, new Verein(8, "Verein8"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(12, "Teilnehmer L", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(23,
                                    new Wettkaempfer(9, "Teilnehmer I", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(7, "Teilnehmer G", Geschlecht.m, Altersklasse.U11, new Verein(7, "Verein7"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(24,
                                    new Wettkaempfer(10, "Teilnehmer J", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(11, "Teilnehmer K", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true), null)),
                    Arrays.asList(
                            new Begegnung(25,
                                    new Wettkaempfer(12, "Teilnehmer L", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(11, "Teilnehmer K", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(26,
                                    new Wettkaempfer(7, "Teilnehmer G", Geschlecht.m, Altersklasse.U11, new Verein(7, "Verein7"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(10, "Teilnehmer J", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(27,
                                    new Wettkaempfer(8, "Teilnehmer H", Geschlecht.m, Altersklasse.U11, new Verein(8, "Verein8"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(9, "Teilnehmer I", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9"), 24.0, Optional.of(Farbe.ORANGE), true, true), null)),
                    Arrays.asList(
                            new Begegnung(28,
                                    new Wettkaempfer(9, "Teilnehmer I", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(12, "Teilnehmer L", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(29,
                                    new Wettkaempfer(10, "Teilnehmer J", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(8, "Teilnehmer H", Geschlecht.m, Altersklasse.U11, new Verein(8, "Verein8"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(30,
                                    new Wettkaempfer(11, "Teilnehmer K", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(7, "Teilnehmer G", Geschlecht.m, Altersklasse.U11, new Verein(7, "Verein7"), 24.0, Optional.of(Farbe.ORANGE), true, true), null
                            )
                    )
            )),
            new WettkampfGruppe(300, "Katze", "(Gewichtskl.1 U11)", Arrays.asList(
                    Arrays.asList(
                            new Begegnung(31,
                                    new Wettkaempfer(13, "Teilnehmer M", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(18, "Teilnehmer R", Geschlecht.m, Altersklasse.U11, new Verein(8, "Verein8"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(32,
                                    new Wettkaempfer(14, "Teilnehmer N", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(19, "Teilnehmer Q", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(33,
                                    new Wettkaempfer(15, "Teilnehmer O", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(16, "Teilnehmer P", Geschlecht.m, Altersklasse.U11, new Verein(6, "Verein6"), 24.0, Optional.of(Farbe.ORANGE), true, true), null)),
                    Arrays.asList(
                            new Begegnung(34,
                                    new Wettkaempfer(18, "Teilnehmer R", Geschlecht.m, Altersklasse.U11, new Verein(8, "Verein8"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(16, "Teilnehmer P", Geschlecht.m, Altersklasse.U11, new Verein(6, "Verein6"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(35,
                                    new Wettkaempfer(19, "Teilnehmer Q", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(15, "Teilnehmer O", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(36,
                                    new Wettkaempfer(13, "Teilnehmer M", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(14, "Teilnehmer N", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4"), 24.0, Optional.of(Farbe.ORANGE), true, true), null)),
                    Arrays.asList(
                            new Begegnung(37,
                                    new Wettkaempfer(14, "Teilnehmer N", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(18, "Teilnehmer R", Geschlecht.m, Altersklasse.U11, new Verein(8, "Verein8"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(38,
                                    new Wettkaempfer(15, "Teilnehmer O", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(13, "Teilnehmer M", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(39,
                                    new Wettkaempfer(16, "Teilnehmer P", Geschlecht.m, Altersklasse.U11, new Verein(6, "Verein6"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(19, "Teilnehmer Q", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9"), 24.0, Optional.of(Farbe.ORANGE), true, true), null)),
                    Arrays.asList(
                            new Begegnung(40,
                                    new Wettkaempfer(18, "Teilnehmer R", Geschlecht.m, Altersklasse.U11, new Verein(8, "Verein8"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(19, "Teilnehmer Q", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(41,
                                    new Wettkaempfer(13, "Teilnehmer M", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(16, "Teilnehmer P", Geschlecht.m, Altersklasse.U11, new Verein(6, "Verein6"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(42,
                                    new Wettkaempfer(14, "Teilnehmer N", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(15, "Teilnehmer O", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5"), 24.0, Optional.of(Farbe.ORANGE), true, true), null)),
                    Arrays.asList(
                            new Begegnung(43,
                                    new Wettkaempfer(15, "Teilnehmer O", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(18, "Teilnehmer R", Geschlecht.m, Altersklasse.U11, new Verein(8, "Verein8"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(44,
                                    new Wettkaempfer(16, "Teilnehmer P", Geschlecht.m, Altersklasse.U11, new Verein(6, "Verein6"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(14, "Teilnehmer N", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(45,
                                    new Wettkaempfer(19, "Teilnehmer Q", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(13, "Teilnehmer M", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3"), 24.0, Optional.of(Farbe.ORANGE), true, true), null
                            )
                    ))),
            new WettkampfGruppe(400, "Maus", "(Gewichtskl.1 U11)", Arrays.asList(
                    Arrays.asList(
                            new Begegnung(46,
                                    new Wettkaempfer(19, "Teilnehmer S", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(22, "Teilnehmer V", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(47,
                                    new Wettkaempfer(20, "Teilnehmer T", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(21, "Teilnehmer U", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true), null)),
                    Arrays.asList(
                            new Begegnung(48,
                                    new Wettkaempfer(22, "Teilnehmer V", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(21, "Teilnehmer U", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(49,
                                    new Wettkaempfer(19, "Teilnehmer S", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(20, "Teilnehmer T", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true), null)),
                    Arrays.asList(
                            new Begegnung(50,
                                    new Wettkaempfer(20, "Teilnehmer T", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(22, "Teilnehmer V", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(51,
                                    new Wettkaempfer(21, "Teilnehmer U", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(19, "Teilnehmer S", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9"), 24.0, Optional.of(Farbe.ORANGE), true, true), null
                            )
                    )
            )),
            new WettkampfGruppe(500, "Tiger", "(Gewichtskl.1 U11)", Arrays.asList(
                    Arrays.asList(
                            new Begegnung(52,
                                    new Wettkaempfer(24, "Teilnehmer X", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(25, "Teilnehmer Y", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5"), 24.0, Optional.of(Farbe.ORANGE), true, true), null)),
                    Arrays.asList(
                            new Begegnung(53,
                                    new Wettkaempfer(23, "Teilnehmer W", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(24, "Teilnehmer X", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4"), 24.0, Optional.of(Farbe.ORANGE), true, true), null)),
                    Arrays.asList(
                            new Begegnung(54,
                                    new Wettkaempfer(25, "Teilnehmer Y", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(23, "Teilnehmer W", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3"), 24.0, Optional.of(Farbe.ORANGE), true, true), null
                            )
                    )
            ))
    );

    public static List<WettkampfGruppe> wks_gerade = Arrays.asList(
            new WettkampfGruppe(100, "Antilope", "(Gewichtskl.1 U11)", Arrays.asList(
                    Arrays.asList(
                            new Begegnung(1,
                                    new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(6, "Teilnehmer F", Geschlecht.m, Altersklasse.U11, new Verein(6, "Verein6"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(2,
                                    new Wettkaempfer(2, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(5, "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(3,
                                    new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(4, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4"), 24.0, Optional.of(Farbe.ORANGE), true, true), null)),
                    Arrays.asList(
                            new Begegnung(4,
                                    new Wettkaempfer(6, "Teilnehmer F", Geschlecht.m, Altersklasse.U11, new Verein(6, "Verein6"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(4, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(5,
                                    new Wettkaempfer(5, "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(6,
                                    new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(2, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true), null)),
                    Arrays.asList(
                            new Begegnung(7,
                                    new Wettkaempfer(2, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(6, "Teilnehmer F", Geschlecht.m, Altersklasse.U11, new Verein(6, "Verein6"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(8,
                                    new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(9,
                                    new Wettkaempfer(4, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(5, "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5"), 24.0, Optional.of(Farbe.ORANGE), true, true), null)),
                    Arrays.asList(
                            new Begegnung(10,
                                    new Wettkaempfer(6, "Teilnehmer F", Geschlecht.m, Altersklasse.U11, new Verein(6, "Verein6"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(5, "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(11,
                                    new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(4, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(12,
                                    new Wettkaempfer(2, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3"), 24.0, Optional.of(Farbe.ORANGE), true, true), null)),
                    Arrays.asList(
                            new Begegnung(13,
                                    new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(6, "Teilnehmer F", Geschlecht.m, Altersklasse.U11, new Verein(6, "Verein6"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(14,
                                    new Wettkaempfer(4, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(2, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(15,
                                    new Wettkaempfer(5, "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true), null
                            )
                    )
            )),
            new WettkampfGruppe(200, "Eule", "(Gewichtskl.1 U11)", Arrays.asList(
                    Arrays.asList(
                            new Begegnung(16,
                                    new Wettkaempfer(7, "Teilnehmer G", Geschlecht.m, Altersklasse.U11, new Verein(7, "Verein7"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(12, "Teilnehmer L", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(17,
                                    new Wettkaempfer(8, "Teilnehmer H", Geschlecht.m, Altersklasse.U11, new Verein(8, "Verein8"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(11, "Teilnehmer K", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(18,
                                    new Wettkaempfer(9, "Teilnehmer I", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(10, "Teilnehmer J", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true), null)),
                    Arrays.asList(
                            new Begegnung(19,
                                    new Wettkaempfer(12, "Teilnehmer L", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(10, "Teilnehmer J", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(20,
                                    new Wettkaempfer(11, "Teilnehmer K", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(9, "Teilnehmer I", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(21,
                                    new Wettkaempfer(7, "Teilnehmer G", Geschlecht.m, Altersklasse.U11, new Verein(7, "Verein7"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(8, "Teilnehmer H", Geschlecht.m, Altersklasse.U11, new Verein(8, "Verein8"), 24.0, Optional.of(Farbe.ORANGE), true, true), null)),
                    Arrays.asList(
                            new Begegnung(22,
                                    new Wettkaempfer(8, "Teilnehmer H", Geschlecht.m, Altersklasse.U11, new Verein(8, "Verein8"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(12, "Teilnehmer L", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(23,
                                    new Wettkaempfer(9, "Teilnehmer I", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(7, "Teilnehmer G", Geschlecht.m, Altersklasse.U11, new Verein(7, "Verein7"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(24,
                                    new Wettkaempfer(10, "Teilnehmer J", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(11, "Teilnehmer K", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true), null)),
                    Arrays.asList(
                            new Begegnung(25,
                                    new Wettkaempfer(12, "Teilnehmer L", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(11, "Teilnehmer K", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(26,
                                    new Wettkaempfer(7, "Teilnehmer G", Geschlecht.m, Altersklasse.U11, new Verein(7, "Verein7"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(10, "Teilnehmer J", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(27,
                                    new Wettkaempfer(8, "Teilnehmer H", Geschlecht.m, Altersklasse.U11, new Verein(8, "Verein8"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(9, "Teilnehmer I", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9"), 24.0, Optional.of(Farbe.ORANGE), true, true), null)),
                    Arrays.asList(
                            new Begegnung(28,
                                    new Wettkaempfer(9, "Teilnehmer I", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(12, "Teilnehmer L", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(29,
                                    new Wettkaempfer(10, "Teilnehmer J", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(8, "Teilnehmer H", Geschlecht.m, Altersklasse.U11, new Verein(8, "Verein8"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(30,
                                    new Wettkaempfer(11, "Teilnehmer K", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(7, "Teilnehmer G", Geschlecht.m, Altersklasse.U11, new Verein(7, "Verein7"), 24.0, Optional.of(Farbe.ORANGE), true, true), null
                            )
                    )
            )),
            new WettkampfGruppe(300, "Katze", "(Gewichtskl.1 U11)", Arrays.asList(
                    Arrays.asList(
                            new Begegnung(31,
                                    new Wettkaempfer(13, "Teilnehmer M", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(18, "Teilnehmer R", Geschlecht.m, Altersklasse.U11, new Verein(8, "Verein8"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(32,
                                    new Wettkaempfer(14, "Teilnehmer N", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(19, "Teilnehmer Q", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(33,
                                    new Wettkaempfer(15, "Teilnehmer O", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(16, "Teilnehmer P", Geschlecht.m, Altersklasse.U11, new Verein(6, "Verein6"), 24.0, Optional.of(Farbe.ORANGE), true, true), null)),
                    Arrays.asList(
                            new Begegnung(34,
                                    new Wettkaempfer(18, "Teilnehmer R", Geschlecht.m, Altersklasse.U11, new Verein(8, "Verein8"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(16, "Teilnehmer P", Geschlecht.m, Altersklasse.U11, new Verein(6, "Verein6"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(35,
                                    new Wettkaempfer(19, "Teilnehmer Q", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(15, "Teilnehmer O", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(36,
                                    new Wettkaempfer(13, "Teilnehmer M", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(14, "Teilnehmer N", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4"), 24.0, Optional.of(Farbe.ORANGE), true, true), null)),
                    Arrays.asList(
                            new Begegnung(37,
                                    new Wettkaempfer(14, "Teilnehmer N", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(18, "Teilnehmer R", Geschlecht.m, Altersklasse.U11, new Verein(8, "Verein8"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(38,
                                    new Wettkaempfer(15, "Teilnehmer O", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(13, "Teilnehmer M", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(39,
                                    new Wettkaempfer(16, "Teilnehmer P", Geschlecht.m, Altersklasse.U11, new Verein(6, "Verein6"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(19, "Teilnehmer Q", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9"), 24.0, Optional.of(Farbe.ORANGE), true, true), null)),
                    Arrays.asList(
                            new Begegnung(40,
                                    new Wettkaempfer(18, "Teilnehmer R", Geschlecht.m, Altersklasse.U11, new Verein(8, "Verein8"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(19, "Teilnehmer Q", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(41,
                                    new Wettkaempfer(13, "Teilnehmer M", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(16, "Teilnehmer P", Geschlecht.m, Altersklasse.U11, new Verein(6, "Verein6"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(42,
                                    new Wettkaempfer(14, "Teilnehmer N", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(15, "Teilnehmer O", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5"), 24.0, Optional.of(Farbe.ORANGE), true, true), null)),
                    Arrays.asList(
                            new Begegnung(43,
                                    new Wettkaempfer(15, "Teilnehmer O", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(18, "Teilnehmer R", Geschlecht.m, Altersklasse.U11, new Verein(8, "Verein8"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(44,
                                    new Wettkaempfer(16, "Teilnehmer P", Geschlecht.m, Altersklasse.U11, new Verein(6, "Verein6"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(14, "Teilnehmer N", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(45,
                                    new Wettkaempfer(19, "Teilnehmer Q", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(13, "Teilnehmer M", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3"), 24.0, Optional.of(Farbe.ORANGE), true, true), null
                            )
                    )
            )),
            new WettkampfGruppe(400, "Maus", "(Gewichtskl.1 U11)", Arrays.asList(
                    Arrays.asList(
                            new Begegnung(46,
                                    new Wettkaempfer(19, "Teilnehmer S", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(22, "Teilnehmer V", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(47,
                                    new Wettkaempfer(20, "Teilnehmer T", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(21, "Teilnehmer U", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true), null)),
                    Arrays.asList(
                            new Begegnung(48,
                                    new Wettkaempfer(22, "Teilnehmer V", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(21, "Teilnehmer U", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(49,
                                    new Wettkaempfer(19, "Teilnehmer S", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(20, "Teilnehmer T", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true), null)),
                    Arrays.asList(
                            new Begegnung(50,
                                    new Wettkaempfer(20, "Teilnehmer T", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(22, "Teilnehmer V", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(51,
                                    new Wettkaempfer(21, "Teilnehmer U", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(19, "Teilnehmer S", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9"), 24.0, Optional.of(Farbe.ORANGE), true, true), null
                            )
                    )
            ))
    );

    public static List<WettkampfGruppe> wks_gerade_unterschiedlich = Arrays.asList(
            new WettkampfGruppe(100, "Antilope", "(Gewichtskl.1 U11)", Arrays.asList(
                    Arrays.asList(
                            new Begegnung(1,
                                    new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(6, "Teilnehmer F", Geschlecht.m, Altersklasse.U11, new Verein(6, "Verein6"), 24.0, Optional.of(Farbe.ORANGE), true, true), null
                            )
                    )
            )),
            new WettkampfGruppe(200, "Eule", "(Gewichtskl.1 U11)", Arrays.asList(
                    Arrays.asList(
                            new Begegnung(2,
                                    new Wettkaempfer(7, "Teilnehmer G", Geschlecht.m, Altersklasse.U11, new Verein(7, "Verein7"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(12, "Teilnehmer L", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(3,
                                    new Wettkaempfer(8, "Teilnehmer H", Geschlecht.m, Altersklasse.U11, new Verein(8, "Verein8"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(11, "Teilnehmer K", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(4,
                                    new Wettkaempfer(9, "Teilnehmer I", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(10, "Teilnehmer J", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true), null)),
                    Arrays.asList(
                            new Begegnung(5,
                                    new Wettkaempfer(12, "Teilnehmer L", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(10, "Teilnehmer J", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(6,
                                    new Wettkaempfer(11, "Teilnehmer K", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(9, "Teilnehmer I", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(7,
                                    new Wettkaempfer(7, "Teilnehmer G", Geschlecht.m, Altersklasse.U11, new Verein(7, "Verein7"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(8, "Teilnehmer H", Geschlecht.m, Altersklasse.U11, new Verein(8, "Verein8"), 24.0, Optional.of(Farbe.ORANGE), true, true), null)),
                    Arrays.asList(
                            new Begegnung(8,
                                    new Wettkaempfer(8, "Teilnehmer H", Geschlecht.m, Altersklasse.U11, new Verein(8, "Verein8"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(12, "Teilnehmer L", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(9,
                                    new Wettkaempfer(9, "Teilnehmer I", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(7, "Teilnehmer G", Geschlecht.m, Altersklasse.U11, new Verein(7, "Verein7"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(10,
                                    new Wettkaempfer(10, "Teilnehmer J", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(11, "Teilnehmer K", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true), null)),
                    Arrays.asList(
                            new Begegnung(11,
                                    new Wettkaempfer(12, "Teilnehmer L", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(11, "Teilnehmer K", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(12,
                                    new Wettkaempfer(7, "Teilnehmer G", Geschlecht.m, Altersklasse.U11, new Verein(7, "Verein7"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(10, "Teilnehmer J", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(13,
                                    new Wettkaempfer(8, "Teilnehmer H", Geschlecht.m, Altersklasse.U11, new Verein(8, "Verein8"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(9, "Teilnehmer I", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9"), 24.0, Optional.of(Farbe.ORANGE), true, true), null)),
                    Arrays.asList(
                            new Begegnung(14,
                                    new Wettkaempfer(9, "Teilnehmer I", Geschlecht.m, Altersklasse.U11, new Verein(9, "Verein9"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(12, "Teilnehmer L", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(15,
                                    new Wettkaempfer(10, "Teilnehmer J", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(8, "Teilnehmer H", Geschlecht.m, Altersklasse.U11, new Verein(8, "Verein8"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                            new Begegnung(16,
                                    new Wettkaempfer(11, "Teilnehmer K", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                                    new Wettkaempfer(7, "Teilnehmer G", Geschlecht.m, Altersklasse.U11, new Verein(7, "Verein7"), 24.0, Optional.of(Farbe.ORANGE), true, true), null
                            )
                    )
            ))
    );

    public static List<WettkampfGruppe>  wks2 = Arrays.asList(
        new WettkampfGruppe(100, "Antilope", "(Gewichtskl.1 U11)", Arrays.asList(
            Arrays.asList(
                new Begegnung(1,
                        new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                        new Wettkaempfer(4, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                new Begegnung(2,
                        new Wettkaempfer(2, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                        new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3"), 24.0, Optional.of(Farbe.ORANGE), true, true), null)),
            Arrays.asList(
                new Begegnung(3,
                        new Wettkaempfer(4, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                        new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                new Begegnung(4,
                        new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                        new Wettkaempfer(2, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true), null)),
            Arrays.asList(
                new Begegnung(5,
                        new Wettkaempfer(2, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                        new Wettkaempfer(4, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4"), 24.0, Optional.of(Farbe.ORANGE), true, true), null),
                new Begegnung(6,
                        new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3"), 24.0, Optional.of(Farbe.ORANGE), true, true),
                        new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Optional.of(Farbe.ORANGE), true, true), null))
        ))
    );
}
