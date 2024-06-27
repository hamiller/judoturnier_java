package de.sinnix.judoturnier.fixtures;

import de.sinnix.judoturnier.adapter.secondary.VereinJpa;
import de.sinnix.judoturnier.adapter.secondary.WettkaempferJpa;
import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Farbe;
import de.sinnix.judoturnier.model.Geschlecht;
import de.sinnix.judoturnier.model.Verein;
import de.sinnix.judoturnier.model.Wettkaempfer;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class WettkaempferFixtures {

    public static WettkaempferJpa wettkaempferJpa1 = new WettkaempferJpa(1, "Melanie", "w", "U11", new VereinJpa(1, "Verein1"), 55d, null, false, false);
    public static WettkaempferJpa wettkaempferJpa2 = new WettkaempferJpa(2, "Lea", "w", "U11", new VereinJpa(1, "Verein1"), 55d, null, false, false);

    public static Wettkaempfer wettkaempfer1 = new Wettkaempfer(1, "Melanie", Geschlecht.w, Altersklasse.U11, new Verein(1, "Verein1"), 25.0, Optional.of(Farbe.ORANGE), true, false);
    public static Wettkaempfer wettkaempfer2 = new Wettkaempfer(2, "Lea A", Geschlecht.w, Altersklasse.U11, new Verein(1, "Verein1"), 25.0, Optional.of(Farbe.ORANGE), true, false);

    public static List<WettkaempferJpa> wettkaempferJpaList = Arrays.asList(
            new WettkaempferJpa(1, "Teilnehmer A", "m", "U11", new VereinJpa(1, "Verein1"), 25.0, "ORANGE", true, false),
            new WettkaempferJpa(2, "Teilnehmer B", "w", "U11", new VereinJpa(2, "Verein2"), 26.0, "BLAU", false, true),
            new WettkaempferJpa(3, "Teilnehmer C", "m", "U11", new VereinJpa(3, "Verein3"), 27.0, "GRUEN", true, true),
            new WettkaempferJpa(4, "Teilnehmer D", "w", "U11", new VereinJpa(4, "Verein4"), 28.0, "GELB", false, false),
            new WettkaempferJpa(5, "Teilnehmer E", "m", "U11", new VereinJpa(5, "Verein5"), 29.0, "ORANGE", true, false),
            new WettkaempferJpa(6, "Teilnehmer F", "w", "U11", new VereinJpa(1, "Verein1"), 30.0, "BLAU", false, true),
            new WettkaempferJpa(7, "Teilnehmer G", "m", "U11", new VereinJpa(2, "Verein2"), 26.1, "GRUEN", true, true),
            new WettkaempferJpa(8, "Teilnehmer H", "w", "U11", new VereinJpa(3, "Verein3"), 32.0, "GELB", false, false),
            new WettkaempferJpa(9, "Teilnehmer I", "m", "U11", new VereinJpa(4, "Verein4"), 33.0, "ORANGE", true, false),
            new WettkaempferJpa(10, "Teilnehmer J", "w", "U11", new VereinJpa(5, "Verein5"), 34.0, "BLAU", false, true),
            new WettkaempferJpa(11, "Teilnehmer K", "m", "U11", new VereinJpa(1, "Verein1"), 35.0, "GRUEN", true, true),
            new WettkaempferJpa(12, "Teilnehmer L", "w", "U11", new VereinJpa(2, "Verein2"), 36.0, "GELB", false, false),
            new WettkaempferJpa(13, "Teilnehmer M", "m", "U11", new VereinJpa(3, "Verein3"), 37.0, "ORANGE", true, false),
            new WettkaempferJpa(14, "Teilnehmer N", "w", "U11", new VereinJpa(4, "Verein4"), 38.0, "BLAU", false, true),
            new WettkaempferJpa(15, "Teilnehmer O", "m", "U11", new VereinJpa(5, "Verein5"), 39.0, "GRUEN", true, true),
            new WettkaempferJpa(16, "Teilnehmer P", "w", "U11", new VereinJpa(1, "Verein1"), 40.0, "GELB", false, false),
            new WettkaempferJpa(17, "Teilnehmer Q", "m", "U11", new VereinJpa(2, "Verein2"), 41.0, "ORANGE", true, false),
            new WettkaempferJpa(18, "Teilnehmer R", "w", "U11", new VereinJpa(3, "Verein3"), 42.0, "BLAU", false, true),
            new WettkaempferJpa(19, "Teilnehmer S", "m", "U11", new VereinJpa(4, "Verein4"), 43.0, "GRUEN", true, true),
            new WettkaempferJpa(20, "Teilnehmer T", "w", "U11", new VereinJpa(5, "Verein5"), 44.0, "GELB", false, false)
    );

    public static List<Wettkaempfer> wettkaempferList = Arrays.asList(
            new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 25.0, Optional.of(Farbe.ORANGE), true, false),
            new Wettkaempfer(2, "Teilnehmer B", Geschlecht.w, Altersklasse.U11, new Verein(2, "Verein2"), 26.0, Optional.of(Farbe.BLAU), false, true),
            new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3"), 27.0, Optional.of(Farbe.GRUEN), true, true),
            new Wettkaempfer(4, "Teilnehmer D", Geschlecht.w, Altersklasse.U11, new Verein(4, "Verein4"), 28.0, Optional.of(Farbe.GELB), false, false),
            new Wettkaempfer(5, "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5"), 29.0, Optional.of(Farbe.ORANGE), true, false),
            new Wettkaempfer(6, "Teilnehmer F", Geschlecht.w, Altersklasse.U11, new Verein(1, "Verein1"), 30.0, Optional.of(Farbe.BLAU), false, true),
            new Wettkaempfer(7, "Teilnehmer G", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 26.1, Optional.of(Farbe.GRUEN), true, true),
            new Wettkaempfer(8, "Teilnehmer H", Geschlecht.w, Altersklasse.U11, new Verein(3, "Verein3"), 32.0, Optional.of(Farbe.GELB), false, false),
            new Wettkaempfer(9, "Teilnehmer I", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4"), 33.0, Optional.of(Farbe.ORANGE), true, false),
            new Wettkaempfer(10, "Teilnehmer J", Geschlecht.w, Altersklasse.U11, new Verein(5, "Verein5"), 34.0, Optional.of(Farbe.BLAU), false, true),
            new Wettkaempfer(11, "Teilnehmer K", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 35.0, Optional.of(Farbe.GRUEN), true, true),
            new Wettkaempfer(12, "Teilnehmer L", Geschlecht.w, Altersklasse.U11, new Verein(2, "Verein2"), 36.0, Optional.of(Farbe.GELB), false, false),
            new Wettkaempfer(13, "Teilnehmer M", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3"), 37.0, Optional.of(Farbe.ORANGE), true, false),
            new Wettkaempfer(14, "Teilnehmer N", Geschlecht.w, Altersklasse.U11, new Verein(4, "Verein4"), 38.0, Optional.of(Farbe.BLAU), false, true),
            new Wettkaempfer(15, "Teilnehmer O", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5"), 39.0, Optional.of(Farbe.GRUEN), true, true),
            new Wettkaempfer(16, "Teilnehmer P", Geschlecht.w, Altersklasse.U11, new Verein(1, "Verein1"), 40.0, Optional.of(Farbe.GELB), false, false),
            new Wettkaempfer(17, "Teilnehmer Q", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 41.0, Optional.of(Farbe.ORANGE), true, false),
            new Wettkaempfer(18, "Teilnehmer R", Geschlecht.w, Altersklasse.U11, new Verein(3, "Verein3"), 42.0, Optional.of(Farbe.BLAU), false, true),
            new Wettkaempfer(19, "Teilnehmer S", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4"), 43.0, Optional.of(Farbe.GRUEN), true, true),
            new Wettkaempfer(20, "Teilnehmer T", Geschlecht.w, Altersklasse.U11, new Verein(5, "Verein5"), 44.0, Optional.of(Farbe.GELB), false, false)
    );
}
