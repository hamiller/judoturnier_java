package de.sinnix.judoturnier.fixtures;

import de.sinnix.judoturnier.adapter.secondary.GewichtsklassenJpa;
import de.sinnix.judoturnier.adapter.secondary.VereinJpa;
import de.sinnix.judoturnier.adapter.secondary.WettkaempferJpa;
import de.sinnix.judoturnier.model.*;

import java.util.Arrays;
import java.util.List;

public class GewichtsklassenGruppeFixture {

    public static List<GewichtsklassenGruppe> gewichtsklassenGruppen = Arrays.asList(
            new GewichtsklassenGruppe(1, Altersklasse.U11, Geschlecht.m, Arrays.asList(
                    new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 24.0, Farbe.BLAU, true, true),
                    new Wettkaempfer(2, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 24.5, Farbe.ORANGE, true, true),
                    new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3"), 23.8, Farbe.GRUEN, true, true),
                    new Wettkaempfer(4, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4"), 24.2, Farbe.GELB, true, true),
                    new Wettkaempfer(5, "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5"), 24.4, Farbe.SCHWARZ, true, true),
                    new Wettkaempfer(6, "Teilnehmer F", Geschlecht.w, Altersklasse.U11, new Verein(2, "Verein2"), 24.3, Farbe.WEISS, true, true)
            ), "Antilope", 24.8, 23.8),
            new GewichtsklassenGruppe(2, Altersklasse.U11, Geschlecht.m, Arrays.asList(
                    new Wettkaempfer(7, "Teilnehmer G", Geschlecht.w, Altersklasse.U11, new Verein(3, "Verein3"), 25.0, Farbe.BLAU, true, true),
                    new Wettkaempfer(8, "Teilnehmer H", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4"), 25.2, Farbe.ORANGE, true, true),
                    new Wettkaempfer(9, "Teilnehmer I", Geschlecht.w, Altersklasse.U11, new Verein(1, "Verein1"), 24.9, Farbe.GRUEN, true, true),
                    new Wettkaempfer(10, "Teilnehmer J", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5"), 25.1, Farbe.GELB, true, true),
                    new Wettkaempfer(11, "Teilnehmer K", Geschlecht.w, Altersklasse.U11, new Verein(3, "Verein3"), 25.3, Farbe.SCHWARZ, true, true),
                    new Wettkaempfer(12, "Teilnehmer L", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 25.4, Farbe.WEISS, true, true)
            ), "Eule", 25.8, 24.8),
            new GewichtsklassenGruppe(3, Altersklasse.U11, Geschlecht.m, Arrays.asList(
                    new Wettkaempfer(13, "Teilnehmer M", Geschlecht.w, Altersklasse.U11, new Verein(4, "Verein4"), 26.0, Farbe.BLAU, true, true),
                    new Wettkaempfer(14, "Teilnehmer N", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5"), 26.2, Farbe.ORANGE, true, true),
                    new Wettkaempfer(15, "Teilnehmer O", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 26.1, Farbe.GRUEN, true, true),
                    new Wettkaempfer(16, "Teilnehmer P", Geschlecht.w, Altersklasse.U11, new Verein(3, "Verein3"), 26.4, Farbe.GELB, true, true),
                    new Wettkaempfer(17, "Teilnehmer Q", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 26.3, Farbe.SCHWARZ, true, true),
                    new Wettkaempfer(18, "Teilnehmer R", Geschlecht.w, Altersklasse.U11, new Verein(1, "Verein1"), 26.5, Farbe.WEISS, true, true)
            ), "Katze", 26.8, 25.8),
            new GewichtsklassenGruppe(4, Altersklasse.U11, Geschlecht.m, Arrays.asList(
                    new Wettkaempfer(19, "Teilnehmer S", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4"), 27.0, Farbe.BLAU, true, true),
                    new Wettkaempfer(20, "Teilnehmer T", Geschlecht.w, Altersklasse.U11, new Verein(5, "Verein5"), 27.2, Farbe.ORANGE, true, true),
                    new Wettkaempfer(21, "Teilnehmer U", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2"), 27.1, Farbe.GRUEN, true, true),
                    new Wettkaempfer(22, "Teilnehmer V", Geschlecht.w, Altersklasse.U11, new Verein(3, "Verein3"), 27.3, Farbe.GELB, true, true)
            ), "Maus", 27.8, 26.8),
            new GewichtsklassenGruppe(5, Altersklasse.U11, Geschlecht.m, Arrays.asList(
                    new Wettkaempfer(23, "Teilnehmer W", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1"), 28.0, Farbe.BLAU, true, true),
                    new Wettkaempfer(24, "Teilnehmer X", Geschlecht.w, Altersklasse.U11, new Verein(4, "Verein4"), 28.2, Farbe.ORANGE, true, true),
                    new Wettkaempfer(25, "Teilnehmer Y", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5"), 28.1, Farbe.GRUEN, true, true)
            ), "Tiger", 28.8, 27.8)
    );

    public static List<GewichtsklassenJpa> gewichtsklassenGruppenJpa = Arrays.asList(
            new GewichtsklassenJpa(1, Altersklasse.U11, Geschlecht.m, Arrays.asList(
                    new WettkaempferJpa(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new VereinJpa(1, "Verein1"), 24.0, Farbe.BLAU, true, true),
                    new WettkaempferJpa(2, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new VereinJpa(2, "Verein2"), 24.5, Farbe.ORANGE, true, true),
                    new WettkaempferJpa(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new VereinJpa(3, "Verein3"), 23.8, Farbe.GRUEN, true, true),
                    new WettkaempferJpa(4, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new VereinJpa(4, "Verein4"), 24.2, Farbe.GELB, true, true),
                    new WettkaempferJpa(5, "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new VereinJpa(5, "Verein5"), 24.4, Farbe.SCHWARZ, true, true),
                    new WettkaempferJpa(6, "Teilnehmer F", Geschlecht.w, Altersklasse.U11, new VereinJpa(2, "Verein2"), 24.3, Farbe.WEISS, true, true)
            ), "Antilope", 24.8, 23.8),
            new GewichtsklassenJpa(2, Altersklasse.U11, Geschlecht.m, Arrays.asList(
                    new WettkaempferJpa(7, "Teilnehmer G", Geschlecht.w, Altersklasse.U11, new VereinJpa(3, "Verein3"), 25.0, Farbe.BLAU, true, true),
                    new WettkaempferJpa(8, "Teilnehmer H", Geschlecht.m, Altersklasse.U11, new VereinJpa(4, "Verein4"), 25.2, Farbe.ORANGE, true, true),
                    new WettkaempferJpa(9, "Teilnehmer I", Geschlecht.w, Altersklasse.U11, new VereinJpa(1, "Verein1"), 24.9, Farbe.GRUEN, true, true),
                    new WettkaempferJpa(10, "Teilnehmer J", Geschlecht.m, Altersklasse.U11, new VereinJpa(5, "Verein5"), 25.1, Farbe.GELB, true, true),
                    new WettkaempferJpa(11, "Teilnehmer K", Geschlecht.w, Altersklasse.U11, new VereinJpa(3, "Verein3"), 25.3, Farbe.SCHWARZ, true, true),
                    new WettkaempferJpa(12, "Teilnehmer L", Geschlecht.m, Altersklasse.U11, new VereinJpa(2, "Verein2"), 25.4, Farbe.WEISS, true, true)
            ), "Eule", 25.8, 24.8),
            new GewichtsklassenJpa(3, Altersklasse.U11, Geschlecht.m, Arrays.asList(
                    new WettkaempferJpa(13, "Teilnehmer M", Geschlecht.w, Altersklasse.U11, new VereinJpa(4, "Verein4"), 26.0, Farbe.BLAU, true, true),
                    new WettkaempferJpa(14, "Teilnehmer N", Geschlecht.m, Altersklasse.U11, new VereinJpa(5, "Verein5"), 26.2, Farbe.ORANGE, true, true),
                    new WettkaempferJpa(15, "Teilnehmer O", Geschlecht.m, Altersklasse.U11, new VereinJpa(1, "Verein1"), 26.1, Farbe.GRUEN, true, true),
                    new WettkaempferJpa(16, "Teilnehmer P", Geschlecht.w, Altersklasse.U11, new VereinJpa(3, "Verein3"), 26.4, Farbe.GELB, true, true),
                    new WettkaempferJpa(17, "Teilnehmer Q", Geschlecht.m, Altersklasse.U11, new VereinJpa(2, "Verein2"), 26.3, Farbe.SCHWARZ, true, true),
                    new WettkaempferJpa(18, "Teilnehmer R", Geschlecht.w, Altersklasse.U11, new VereinJpa(1, "Verein1"), 26.5, Farbe.WEISS, true, true)
            ), "Katze", 26.8, 25.8),
            new GewichtsklassenJpa(4, Altersklasse.U11, Geschlecht.m, Arrays.asList(
                    new WettkaempferJpa(19, "Teilnehmer S", Geschlecht.m, Altersklasse.U11, new VereinJpa(4, "Verein4"), 27.0, Farbe.BLAU, true, true),
                    new WettkaempferJpa(20, "Teilnehmer T", Geschlecht.w, Altersklasse.U11, new VereinJpa(5, "Verein5"), 27.2, Farbe.ORANGE, true, true),
                    new WettkaempferJpa(21, "Teilnehmer U", Geschlecht.m, Altersklasse.U11, new VereinJpa(2, "Verein2"), 27.1, Farbe.GRUEN, true, true),
                    new WettkaempferJpa(22, "Teilnehmer V", Geschlecht.w, Altersklasse.U11, new VereinJpa(3, "Verein3"), 27.3, Farbe.GELB, true, true)
            ), "Maus", 27.8, 26.8),
            new GewichtsklassenJpa(5, Altersklasse.U11, Geschlecht.m, Arrays.asList(
                    new WettkaempferJpa(23, "Teilnehmer W", Geschlecht.m, Altersklasse.U11, new VereinJpa(1, "Verein1"), 28.0, Farbe.BLAU, true, true),
                    new WettkaempferJpa(24, "Teilnehmer X", Geschlecht.w, Altersklasse.U11, new VereinJpa(4, "Verein4"), 28.2, Farbe.ORANGE, true, true),
                    new WettkaempferJpa(25, "Teilnehmer Y", Geschlecht.m, Altersklasse.U11, new VereinJpa(5, "Verein5"), 28.1, Farbe.GRUEN, true, true)
            ), "Tiger", 28.8, 27.8)
    );

    public static List<GewichtsklassenGruppe> gwks2 = Arrays.asList(
            new GewichtsklassenGruppe(1, Altersklasse.U11, Geschlecht.m, Arrays.asList(
                    new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1,"Verein1"), 26.0, Farbe.BLAU,true, true),
                    new Wettkaempfer(2, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(2,"Verein2"), 26.2, Farbe.ORANGE,true, true),
                    new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(3,"Verein3"), 26.3, Farbe.GRUEN,true, true),
                    new Wettkaempfer(4, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(4,"Verein4"), 26.5, Farbe.GELB,true, true)
            ), "Antilope", 26.8,24.8)
    );

}
