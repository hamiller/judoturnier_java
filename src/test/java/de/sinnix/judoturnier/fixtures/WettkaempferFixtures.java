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
import java.util.UUID;

public class WettkaempferFixtures {
    public static final UUID turnierUUID = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
    public static final UUID v1UUID  = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    public static final UUID v2UUID  = UUID.fromString("123e4567-e89b-12d3-a456-426614174001");
    public static final UUID v3UUID  = UUID.fromString("123e4567-e89b-12d3-a456-426614174002");
    public static final UUID v4UUID  = UUID.fromString("123e4567-e89b-12d3-a456-426614174003");
    public static final UUID v5UUID  = UUID.fromString("123e4567-e89b-12d3-a456-426614174004");
    public static final UUID w1      = UUID.fromString("123e4567-e89b-12d3-a456-426614174005");
    public static final UUID w2      = UUID.fromString("123e4567-e89b-12d3-a456-426614174006");
    public static final UUID w3      = UUID.fromString("123e4567-e89b-12d3-a456-426614174007");
    public static final UUID w4      = UUID.fromString("123e4567-e89b-12d3-a456-426614174008");
    public static final UUID w5      = UUID.fromString("123e4567-e89b-12d3-a456-426614174009");
    public static final UUID w6      = UUID.fromString("123e4567-e89b-12d3-a456-426614174010");
    public static final UUID w7      = UUID.fromString("123e4567-e89b-12d3-a456-426614174011");
    public static final UUID w8      = UUID.fromString("123e4567-e89b-12d3-a456-426614174012");
    public static final UUID w9      = UUID.fromString("123e4567-e89b-12d3-a456-426614174013");
    public static final UUID w10     = UUID.fromString("123e4567-e89b-12d3-a456-426614174014");
    public static final UUID w11     = UUID.fromString("123e4567-e89b-12d3-a456-426614174015");
    public static final UUID w12     = UUID.fromString("123e4567-e89b-12d3-a456-426614174016");
    public static final UUID w13     = UUID.fromString("123e4567-e89b-12d3-a456-426614174017");
    public static final UUID w14     = UUID.fromString("123e4567-e89b-12d3-a456-426614174018");
    public static final UUID w15     = UUID.fromString("123e4567-e89b-12d3-a456-426614174019");
    public static final UUID w16     = UUID.fromString("123e4567-e89b-12d3-a456-426614174020");
    public static final UUID w17     = UUID.fromString("123e4567-e89b-12d3-a456-426614174021");
    public static final UUID w18     = UUID.fromString("123e4567-e89b-12d3-a456-426614174022");
    public static final UUID w19     = UUID.fromString("123e4567-e89b-12d3-a456-426614174023");
    public static final UUID w20     = UUID.fromString("123e4567-e89b-12d3-a456-426614174024");

    public static WettkaempferJpa wettkaempferJpa1 = new WettkaempferJpa("Melanie", "w", "U11", new VereinJpa( "Verein1", turnierUUID), 55d, null, false, false, turnierUUID);
    public static WettkaempferJpa wettkaempferJpa2 = new WettkaempferJpa("Lea", "w", "U11", new VereinJpa( "Verein1", turnierUUID), 55d, null, false, false, turnierUUID);

    public static Optional<Wettkaempfer> wettkaempfer1 = Optional.of(new Wettkaempfer(w1, "Melanie", Geschlecht.w, Altersklasse.U11, new Verein(v1UUID, "Verein1", turnierUUID), 25.0, Optional.of(Farbe.ORANGE), true, false, turnierUUID));
    public static Optional<Wettkaempfer> wettkaempfer2 = Optional.of(new Wettkaempfer(w2, "Lea A", Geschlecht.w, Altersklasse.U11, new Verein(v1UUID, "Verein1", turnierUUID), 25.0, Optional.of(Farbe.ORANGE), true, false, turnierUUID));
    public static Optional<Wettkaempfer> wettkaempfer3 = Optional.of(new Wettkaempfer(w3, "Simone S", Geschlecht.w, Altersklasse.U11, new Verein(v1UUID, "Verein1", turnierUUID), 25.0, Optional.of(Farbe.ORANGE), true, false, turnierUUID));
    public static Optional<Wettkaempfer> wettkaempfer4 = Optional.of(new Wettkaempfer(w4, "Beatrice X", Geschlecht.w, Altersklasse.U11, new Verein(v1UUID, "Verein1", turnierUUID), 25.0, Optional.of(Farbe.ORANGE), true, false, turnierUUID));

	public static Wettkaempfer wettkaempferin1 = new Wettkaempfer(UUID.fromString("dae575e5-d78c-461c-89a3-0275002f317f"), "Fox, Sweetie", Geschlecht.w, Altersklasse.Frauen, new Verein(v1UUID, "Verein1", turnierUUID), 60.0, Optional.of(Farbe.WEISS), true, false, turnierUUID);
	public static Wettkaempfer wettkaempferin2 = new Wettkaempfer(UUID.fromString("fed166f9-8ba2-482d-9ad0-ff4e35fe28dd"), "Reid, Riley", Geschlecht.w, Altersklasse.Frauen, new Verein(v1UUID, "Verein1", turnierUUID), 60.0, Optional.of(Farbe.BRAUN), true, false, turnierUUID);
	public static Wettkaempfer wettkaempferin3 = new Wettkaempfer(UUID.fromString("ace600da-6b83-408e-8d57-790694c1ec5b"), "Jameson, Jenna", Geschlecht.w, Altersklasse.Frauen, new Verein(v1UUID, "Verein1", turnierUUID), 61.0, Optional.of(Farbe.GELB), true, false, turnierUUID);
	public static Wettkaempfer wettkaempferin4 = new Wettkaempfer(UUID.fromString("3fa2397f-641e-48ac-873f-31f10fd6afba"), "Belle, Lexi", Geschlecht.w, Altersklasse.Frauen, new Verein(v1UUID, "Verein1", turnierUUID), 62.0, Optional.of(Farbe.ORANGE), true, false, turnierUUID);
	public static Wettkaempfer wettkaempferin5 = new Wettkaempfer(UUID.fromString("9c22cdb3-a102-4f87-9586-ce68e40489e0"), "Grey, Sasha", Geschlecht.w, Altersklasse.Frauen, new Verein(v1UUID, "Verein1", turnierUUID), 62.0, Optional.empty(), true, false, turnierUUID);
	public static Wettkaempfer wettkaempferin6 = new Wettkaempfer(UUID.fromString("2ce00751-47ba-4698-a39e-2f0853bcd661"), "Crystal, Alexis", Geschlecht.w, Altersklasse.Frauen, new Verein(v1UUID, "Verein1", turnierUUID), 58.0, Optional.empty(), true, false, turnierUUID);
	public static Wettkaempfer wettkaempferin7 = new Wettkaempfer(UUID.fromString("db2311c4-40cc-4c8a-adf0-6294c2bbc68c"), "Lore, Lexi", Geschlecht.w, Altersklasse.Frauen, new Verein(v1UUID, "Verein1", turnierUUID), 58.0, Optional.empty(), true, false, turnierUUID);

	public static Wettkaempfer wettkaempferin8 = new Wettkaempfer(UUID.fromString("53f3464d-f356-4cae-b2a9-900b875da911"), "Caprice, Little", Geschlecht.w, Altersklasse.Frauen, new Verein(v1UUID, "Verein1", turnierUUID), 47.0, Optional.empty(), true, false, turnierUUID);
	public static Wettkaempfer wettkaempferin9 = new Wettkaempfer(UUID.fromString("8572c7e7-b50b-4964-b661-e4d25e0950b4"), "Maire, Ariana", Geschlecht.w, Altersklasse.Frauen, new Verein(v1UUID, "Verein1", turnierUUID), 47.6, Optional.empty(), true, false, turnierUUID);

	public static Wettkaempfer wettkaempferin10 = new Wettkaempfer(UUID.fromString("8015045a-6490-4b26-83c2-0d46b8fbd397"), "Di, Foxi", Geschlecht.w, Altersklasse.Frauen, new Verein(v1UUID, "Verein1", turnierUUID), 53.0, Optional.empty(), true, false, turnierUUID);
	public static Wettkaempfer wettkaempferin11 = new Wettkaempfer(UUID.fromString("9aa77e17-d3d3-48e9-9fae-8ab9c0b952c5"), "Dee, Sophie", Geschlecht.w, Altersklasse.Frauen, new Verein(v1UUID, "Verein1", turnierUUID), 54.0, Optional.empty(), true, false, turnierUUID);

    public static List<WettkaempferJpa> wettkaempferJpaList = Arrays.asList(
            new WettkaempferJpa("Teilnehmer A", "m", "U11", new VereinJpa( "Verein1", turnierUUID), 25.0, "ORANGE", true, false, turnierUUID),
            new WettkaempferJpa("Teilnehmer B", "w", "U11", new VereinJpa( "Verein2", turnierUUID), 26.0, "BLAU", false, true, turnierUUID),
            new WettkaempferJpa("Teilnehmer C", "m", "U11", new VereinJpa( "Verein3", turnierUUID), 27.0, "GRUEN", true, true, turnierUUID),
            new WettkaempferJpa("Teilnehmer D", "w", "U11", new VereinJpa( "Verein4", turnierUUID), 28.0, "GELB", false, false, turnierUUID),
            new WettkaempferJpa("Teilnehmer E", "m", "U11", new VereinJpa( "Verein5", turnierUUID), 29.0, "ORANGE", true, false, turnierUUID),
            new WettkaempferJpa("Teilnehmer F", "w", "U11", new VereinJpa( "Verein1", turnierUUID), 30.0, "BLAU", false, true, turnierUUID),
            new WettkaempferJpa("Teilnehmer G", "m", "U11", new VereinJpa( "Verein2", turnierUUID), 26.1, "GRUEN", true, true, turnierUUID),
            new WettkaempferJpa("Teilnehmer H", "w", "U11", new VereinJpa( "Verein3", turnierUUID), 32.0, "GELB", false, false, turnierUUID),
            new WettkaempferJpa("Teilnehmer I", "m", "U11", new VereinJpa( "Verein4", turnierUUID), 33.0, "ORANGE", true, false, turnierUUID),
            new WettkaempferJpa("Teilnehmer J", "w", "U11", new VereinJpa( "Verein5", turnierUUID), 34.0, "BLAU", false, true, turnierUUID),
            new WettkaempferJpa("Teilnehmer K", "m", "U11", new VereinJpa( "Verein1", turnierUUID), 35.0, "GRUEN", true, true, turnierUUID),
            new WettkaempferJpa("Teilnehmer L", "w", "U11", new VereinJpa( "Verein2", turnierUUID), 36.0, "GELB", false, false, turnierUUID),
            new WettkaempferJpa("Teilnehmer M", "m", "U11", new VereinJpa( "Verein3", turnierUUID), 37.0, "ORANGE", true, false, turnierUUID),
            new WettkaempferJpa("Teilnehmer N", "w", "U11", new VereinJpa( "Verein4", turnierUUID), 38.0, "BLAU", false, true, turnierUUID),
            new WettkaempferJpa("Teilnehmer O", "m", "U11", new VereinJpa( "Verein5", turnierUUID), 39.0, "GRUEN", true, true, turnierUUID),
            new WettkaempferJpa("Teilnehmer P", "w", "U11", new VereinJpa( "Verein1", turnierUUID), 40.0, "GELB", false, false, turnierUUID),
            new WettkaempferJpa("Teilnehmer Q", "m", "U11", new VereinJpa( "Verein2", turnierUUID), 41.0, "ORANGE", true, false, turnierUUID),
            new WettkaempferJpa("Teilnehmer R", "w", "U11", new VereinJpa( "Verein3", turnierUUID), 42.0, "BLAU", false, true, turnierUUID),
            new WettkaempferJpa("Teilnehmer S", "m", "U11", new VereinJpa( "Verein4", turnierUUID), 43.0, "GRUEN", true, true, turnierUUID),
            new WettkaempferJpa("Teilnehmer T", "w", "U11", new VereinJpa( "Verein5", turnierUUID), 44.0, "GELB", false, false, turnierUUID)
    );

    public static List<Wettkaempfer> wettkaempferList = Arrays.asList(
            new Wettkaempfer(w1, "Teilnehmer A", Geschlecht.m, Altersklasse.U11, new Verein(v1UUID, "Verein1", turnierUUID), 25.0, Optional.of(Farbe.ORANGE), true, false, turnierUUID),
            new Wettkaempfer(w2, "Teilnehmer B", Geschlecht.w, Altersklasse.U11, new Verein(v2UUID, "Verein2", turnierUUID), 26.0, Optional.of(Farbe.BLAU), false, true, turnierUUID),
            new Wettkaempfer(w3, "Teilnehmer C", Geschlecht.m, Altersklasse.U11, new Verein(v3UUID, "Verein3", turnierUUID), 27.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
            new Wettkaempfer(w4, "Teilnehmer D", Geschlecht.w, Altersklasse.U11, new Verein(v4UUID, "Verein4", turnierUUID), 28.0, Optional.of(Farbe.GELB), false, false, turnierUUID),
            new Wettkaempfer(w5, "Teilnehmer E", Geschlecht.m, Altersklasse.U11, new Verein(v5UUID, "Verein5", turnierUUID), 29.0, Optional.of(Farbe.ORANGE), true, false, turnierUUID),
            new Wettkaempfer(w6, "Teilnehmer F", Geschlecht.w, Altersklasse.U11, new Verein(v1UUID, "Verein1", turnierUUID), 30.0, Optional.of(Farbe.BLAU), false, true, turnierUUID),
            new Wettkaempfer(w7, "Teilnehmer G", Geschlecht.m, Altersklasse.U11, new Verein(v2UUID, "Verein2", turnierUUID), 26.1, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
            new Wettkaempfer(w8, "Teilnehmer H", Geschlecht.w, Altersklasse.U11, new Verein(v3UUID, "Verein3", turnierUUID), 32.0, Optional.of(Farbe.GELB), false, false, turnierUUID),
            new Wettkaempfer(w9, "Teilnehmer I", Geschlecht.m, Altersklasse.U11, new Verein(v4UUID, "Verein4", turnierUUID), 33.0, Optional.of(Farbe.ORANGE), true, false, turnierUUID),
            new Wettkaempfer(w10, "Teilnehmer J", Geschlecht.w, Altersklasse.U11, new Verein(v5UUID, "Verein5", turnierUUID), 34.0, Optional.of(Farbe.BLAU), false, true, turnierUUID),
            new Wettkaempfer(w11, "Teilnehmer K", Geschlecht.m, Altersklasse.U11, new Verein(v1UUID, "Verein1", turnierUUID), 35.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
            new Wettkaempfer(w12, "Teilnehmer L", Geschlecht.w, Altersklasse.U11, new Verein(v2UUID, "Verein2", turnierUUID), 36.0, Optional.of(Farbe.GELB), false, false, turnierUUID),
            new Wettkaempfer(w13, "Teilnehmer M", Geschlecht.m, Altersklasse.U11, new Verein(v3UUID, "Verein3", turnierUUID), 37.0, Optional.of(Farbe.ORANGE), true, false, turnierUUID),
            new Wettkaempfer(w14, "Teilnehmer N", Geschlecht.w, Altersklasse.U11, new Verein(v4UUID, "Verein4", turnierUUID), 38.0, Optional.of(Farbe.BLAU), false, true, turnierUUID),
            new Wettkaempfer(w15, "Teilnehmer O", Geschlecht.m, Altersklasse.U11, new Verein(v5UUID, "Verein5", turnierUUID), 39.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
            new Wettkaempfer(w16, "Teilnehmer P", Geschlecht.w, Altersklasse.U11, new Verein(v1UUID, "Verein1", turnierUUID), 40.0, Optional.of(Farbe.GELB), false, false, turnierUUID),
            new Wettkaempfer(w17, "Teilnehmer Q", Geschlecht.m, Altersklasse.U11, new Verein(v2UUID, "Verein2", turnierUUID), 41.0, Optional.of(Farbe.ORANGE), true, false, turnierUUID),
            new Wettkaempfer(w18, "Teilnehmer R", Geschlecht.w, Altersklasse.U11, new Verein(v3UUID, "Verein3", turnierUUID), 42.0, Optional.of(Farbe.BLAU), false, true, turnierUUID),
            new Wettkaempfer(w19, "Teilnehmer S", Geschlecht.m, Altersklasse.U11, new Verein(v4UUID, "Verein4", turnierUUID), 43.0, Optional.of(Farbe.GRUEN), true, true, turnierUUID),
            new Wettkaempfer(w20, "Teilnehmer T", Geschlecht.w, Altersklasse.U11, new Verein(v5UUID, "Verein5", turnierUUID), 44.0, Optional.of(Farbe.GELB), false, false, turnierUUID)
    );
}
