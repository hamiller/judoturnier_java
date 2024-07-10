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

    public static List<GewichtsklassenGruppe> gewichtsklassenGruppen = Arrays.asList(
            new GewichtsklassenGruppe(1, Altersklasse.U11, Optional.of(Geschlecht.m), Arrays.asList(
                    new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 24.0, Optional.of(Farbe.BLAU), true, true, turnierUUID),
                    new Wettkaempfer(2, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2", turnierUUID), 24.5, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                    new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(3, "Verein3", turnierUUID), 23.8, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
                    new Wettkaempfer(4, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 24.2, Optional.of(Farbe.GELB), true, true, turnierUUID),
                    new Wettkaempfer(5, "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5", turnierUUID), 24.4, Optional.of(Farbe.SCHWARZ), true, true, turnierUUID),
                    new Wettkaempfer(6, "Teilnehmer F", Geschlecht.w, Altersklasse.U11, new Verein(2, "Verein2", turnierUUID), 24.3, Optional.of(Farbe.WEISS), true, true, turnierUUID)
            ), Optional.of(RandoriGruppenName.Antilope), 23.8, 24.5, turnierUUID),
            new GewichtsklassenGruppe(2, Altersklasse.U11, Optional.of(Geschlecht.m), Arrays.asList(
                    new Wettkaempfer(7, "Teilnehmer G", Geschlecht.w, Altersklasse.U11, new Verein(3, "Verein3", turnierUUID), 25.0, Optional.of(Farbe.BLAU), true, true, turnierUUID),
                    new Wettkaempfer(8, "Teilnehmer H", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 25.2, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                    new Wettkaempfer(9, "Teilnehmer I", Geschlecht.w, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 24.9, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
                    new Wettkaempfer(10, "Teilnehmer J", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5", turnierUUID), 25.1, Optional.of(Farbe.GELB), true, true, turnierUUID),
                    new Wettkaempfer(11, "Teilnehmer K", Geschlecht.w, Altersklasse.U11, new Verein(3, "Verein3", turnierUUID), 25.3, Optional.of(Farbe.SCHWARZ), true, true, turnierUUID),
                    new Wettkaempfer(12, "Teilnehmer L", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2", turnierUUID), 25.4, Optional.of(Farbe.WEISS), true, true, turnierUUID)
            ), Optional.of(RandoriGruppenName.Eule), 25.8, 24.8, turnierUUID),
            new GewichtsklassenGruppe(3, Altersklasse.U11, Optional.of(Geschlecht.m), Arrays.asList(
                    new Wettkaempfer(13, "Teilnehmer M", Geschlecht.w, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 26.0, Optional.of(Farbe.BLAU), true, true, turnierUUID),
                    new Wettkaempfer(14, "Teilnehmer N", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5", turnierUUID), 26.2, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                    new Wettkaempfer(15, "Teilnehmer O", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 26.1, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
                    new Wettkaempfer(16, "Teilnehmer P", Geschlecht.w, Altersklasse.U11, new Verein(3, "Verein3", turnierUUID), 26.4, Optional.of(Farbe.GELB), true, true, turnierUUID),
                    new Wettkaempfer(17, "Teilnehmer Q", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2", turnierUUID), 26.3, Optional.of(Farbe.SCHWARZ), true, true, turnierUUID),
                    new Wettkaempfer(18, "Teilnehmer R", Geschlecht.w, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 26.5, Optional.of(Farbe.WEISS), true, true, turnierUUID)
            ), Optional.of(RandoriGruppenName.Katze), 26.8, 25.8, turnierUUID),
            new GewichtsklassenGruppe(4, Altersklasse.U11, Optional.of(Geschlecht.m), Arrays.asList(
                    new Wettkaempfer(19, "Teilnehmer S", Geschlecht.m, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 27.0, Optional.of(Farbe.BLAU), true, true, turnierUUID),
                    new Wettkaempfer(20, "Teilnehmer T", Geschlecht.w, Altersklasse.U11, new Verein(5, "Verein5", turnierUUID), 27.2, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                    new Wettkaempfer(21, "Teilnehmer U", Geschlecht.m, Altersklasse.U11, new Verein(2, "Verein2", turnierUUID), 27.1, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
                    new Wettkaempfer(22, "Teilnehmer V", Geschlecht.w, Altersklasse.U11, new Verein(3, "Verein3", turnierUUID), 27.3, Optional.of(Farbe.GELB), true, true, turnierUUID)
            ), Optional.of(RandoriGruppenName.Maus), 27.8, 26.8, turnierUUID),
            new GewichtsklassenGruppe(5, Altersklasse.U11, Optional.of(Geschlecht.m), Arrays.asList(
                    new Wettkaempfer(23, "Teilnehmer W", Geschlecht.m, Altersklasse.U11, new Verein(1, "Verein1", turnierUUID), 28.0, Optional.of(Farbe.BLAU), true, true, turnierUUID),
                    new Wettkaempfer(24, "Teilnehmer X", Geschlecht.w, Altersklasse.U11, new Verein(4, "Verein4", turnierUUID), 28.2, Optional.of(Farbe.ORANGE), true, true, turnierUUID),
                    new Wettkaempfer(25, "Teilnehmer Y", Geschlecht.m, Altersklasse.U11, new Verein(5, "Verein5", turnierUUID), 28.1, Optional.of(Farbe.GRUEN), true, true, turnierUUID)
            ), Optional.of(RandoriGruppenName.Tiger), 28.0, 28.2, turnierUUID)
    );

    public static List<GewichtsklassenJpa> gewichtsklassenGruppenJpa = Arrays.asList(
            new GewichtsklassenJpa(1, "U11", "m", Arrays.asList(
                    new WettkaempferJpa(1, "Teilnehmer A", "m", "U11", new VereinJpa(1, "Verein1", turnierUUID.toString()), 24.0, "BLAU", true, true, turnierUUID.toString()),
                    new WettkaempferJpa(2, "Teilnehmer B", "m", "U11", new VereinJpa(2, "Verein2", turnierUUID.toString()), 24.5, "ORANGE", true, true, turnierUUID.toString()),
                    new WettkaempferJpa(3, "Teilnehmer C", "m", "U11", new VereinJpa(3, "Verein3", turnierUUID.toString()), 23.8, "GRUEN", true, true, turnierUUID.toString()),
                    new WettkaempferJpa(4, "Teilnehmer D", "m", "U11", new VereinJpa(4, "Verein4", turnierUUID.toString()), 24.2, "GELB", true, true, turnierUUID.toString()),
                    new WettkaempferJpa(5, "Teilnehmer E", "m", "U11", new VereinJpa(5, "Verein5", turnierUUID.toString()), 24.4, "SCHWARZ", true, true, turnierUUID.toString()),
                    new WettkaempferJpa(6, "Teilnehmer F", "m", "U11", new VereinJpa(2, "Verein2", turnierUUID.toString()), 24.3, "WEISS", true, true, turnierUUID.toString())
            ), "Antilope", 24.8, 23.8, turnierUUID.toString()),
            new GewichtsklassenJpa(2, "U11", "m", Arrays.asList(
                    new WettkaempferJpa(7, "Teilnehmer G", "m", "U11", new VereinJpa(3, "Verein3", turnierUUID.toString()), 25.0, "BLAU", true, true, turnierUUID.toString()),
                    new WettkaempferJpa(8, "Teilnehmer H", "m", "U11", new VereinJpa(4, "Verein4", turnierUUID.toString()), 25.2, "ORANGE", true, true, turnierUUID.toString()),
                    new WettkaempferJpa(9, "Teilnehmer I", "m", "U11", new VereinJpa(1, "Verein1", turnierUUID.toString()), 24.9, "GRUEN", true, true, turnierUUID.toString()),
                    new WettkaempferJpa(10, "Teilnehmer J", "m", "U11", new VereinJpa(5, "Verein5", turnierUUID.toString()), 25.1, "GELB", true, true, turnierUUID.toString()),
                    new WettkaempferJpa(11, "Teilnehmer K", "m", "U11", new VereinJpa(3, "Verein3", turnierUUID.toString()), 25.3, "SCHWARZ", true, true, turnierUUID.toString()),
                    new WettkaempferJpa(12, "Teilnehmer L", "m", "U11", new VereinJpa(2, "Verein2", turnierUUID.toString()), 25.4, "WEISS", true, true, turnierUUID.toString())
            ), "Eule", 25.8, 24.8, turnierUUID.toString()),
            new GewichtsklassenJpa(3, "U11", "m", Arrays.asList(
                    new WettkaempferJpa(13, "Teilnehmer M", "m", "U11", new VereinJpa(4, "Verein4", turnierUUID.toString()), 26.0, "BLAU", true, true, turnierUUID.toString()),
                    new WettkaempferJpa(14, "Teilnehmer N", "m", "U11", new VereinJpa(5, "Verein5", turnierUUID.toString()), 26.2, "ORANGE", true, true, turnierUUID.toString()),
                    new WettkaempferJpa(15, "Teilnehmer O", "m", "U11", new VereinJpa(1, "Verein1", turnierUUID.toString()), 26.1, "GRUEN", true, true, turnierUUID.toString()),
                    new WettkaempferJpa(16, "Teilnehmer P", "m", "U11", new VereinJpa(3, "Verein3", turnierUUID.toString()), 26.4, "GELB", true, true, turnierUUID.toString()),
                    new WettkaempferJpa(17, "Teilnehmer Q", "m", "U11", new VereinJpa(2, "Verein2", turnierUUID.toString()), 26.3, "SCHWARZ", true, true, turnierUUID.toString()),
                    new WettkaempferJpa(18, "Teilnehmer R", "m", "U11", new VereinJpa(1, "Verein1", turnierUUID.toString()), 26.5, "WEISS", true, true, turnierUUID.toString())
            ), "Katze", 26.8, 25.8, turnierUUID.toString()),
            new GewichtsklassenJpa(4, "U11", "m", Arrays.asList(
                    new WettkaempferJpa(19, "Teilnehmer S", "m", "U11", new VereinJpa(4, "Verein4", turnierUUID.toString()), 27.0, "BLAU", true, true, turnierUUID.toString()),
                    new WettkaempferJpa(20, "Teilnehmer T", "m", "U11", new VereinJpa(5, "Verein5", turnierUUID.toString()), 27.2, "ORANGE", true, true, turnierUUID.toString()),
                    new WettkaempferJpa(21, "Teilnehmer U", "m", "U11", new VereinJpa(2, "Verein2", turnierUUID.toString()), 27.1, "GRUEN", true, true, turnierUUID.toString()),
                    new WettkaempferJpa(22, "Teilnehmer V", "m", "U11", new VereinJpa(3, "Verein3", turnierUUID.toString()), 27.3, "GELB", true, true, turnierUUID.toString())
            ), "Maus", 27.8, 26.8, turnierUUID.toString()),
            new GewichtsklassenJpa(5, "U11", "m", Arrays.asList(
                    new WettkaempferJpa(23, "Teilnehmer W", "m", "U11", new VereinJpa(1, "Verein1", turnierUUID.toString()), 28.0, "BLAU", true, true, turnierUUID.toString()),
                    new WettkaempferJpa(24, "Teilnehmer X", "m", "U11", new VereinJpa(4, "Verein4", turnierUUID.toString()), 28.2, "ORANGE", true, true, turnierUUID.toString()),
                    new WettkaempferJpa(25, "Teilnehmer Y", "m", "U11", new VereinJpa(5, "Verein5", turnierUUID.toString()), 28.1, "GRUEN", true, true, turnierUUID.toString())
            ), "Tiger", 28.8, 27.8, turnierUUID.toString())
    );

    public static List<GewichtsklassenGruppe> gwks2 = Arrays.asList(
            new GewichtsklassenGruppe(1, Altersklasse.U11, Optional.of(Geschlecht.m), Arrays.asList(
                    new Wettkaempfer(1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(1,"Verein1", turnierUUID), 26.0, Optional.of(Farbe.BLAU),true, true, turnierUUID),
                    new Wettkaempfer(2, "Teilnehmer B", Geschlecht.m, Altersklasse.U11, new Verein(2,"Verein2", turnierUUID), 26.2, Optional.of(Farbe.ORANGE),true, true, turnierUUID),
                    new Wettkaempfer(3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(3,"Verein3", turnierUUID), 26.3, Optional.of(Farbe.GRUEN),true, true, turnierUUID),
                    new Wettkaempfer(4, "Teilnehmer D", Geschlecht.m, Altersklasse.U11, new Verein(4,"Verein4", turnierUUID), 26.5, Optional.of(Farbe.GELB),true, true, turnierUUID)
            ), Optional.of(RandoriGruppenName.Antilope), 26.8,24.8, turnierUUID)
    );

}
