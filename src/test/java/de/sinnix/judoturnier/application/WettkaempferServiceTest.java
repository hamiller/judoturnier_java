package de.sinnix.judoturnier.application;

import de.sinnix.judoturnier.adapter.secondary.WettkaempferRepository;
import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Begegnung;
import de.sinnix.judoturnier.model.Benutzer;
import de.sinnix.judoturnier.model.BenutzerRolle;
import de.sinnix.judoturnier.model.Farbe;
import de.sinnix.judoturnier.model.GesamtPlatzierung;
import de.sinnix.judoturnier.model.GesamtWertung;
import de.sinnix.judoturnier.model.Geschlecht;
import de.sinnix.judoturnier.model.Verein;
import de.sinnix.judoturnier.model.Wertung;
import de.sinnix.judoturnier.model.Wettkaempfer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WettkaempferServiceTest {

	@Mock
	private WettkaempferRepository wettkaempferRepository;
	@Mock
	private VereinService          vereinService;
	@Mock
	private WettkampfService       wettkampfService;
	@Mock
	private EinstellungenService   einstellungenService;
	@InjectMocks
	private WettkaempferService    wettkaempferService;

	private Wettkaempfer wettkaempfer;

	private static final UUID     turnierUUID  = UUID.fromString("7c858e76-60b9-4097-ad19-1ebe6891b9f9");
	private static final Benutzer kampfrichter = new Benutzer(UUID.fromString("898a7fcf-2fad-4ec9-8b4f-5513188af291"), "user1", "Name, Vorname", List.of(), List.of(BenutzerRolle.KAMPFRICHTER));

	public static Verein       verein1       = new Verein(UUID.fromString("61d1a191-5df1-40f5-a941-da18e06cebad"), "Kimchi Wiesbaden", turnierUUID);
	public static Verein       verein2       = new Verein(UUID.fromString("9d4e210a-8099-4402-94d7-0b0fbaa67885"), "1. JCG", turnierUUID);
	public static Wettkaempfer wettkaempfer1 = new Wettkaempfer(UUID.fromString("c4effdff-1990-4803-a737-5463b16da89b"), "Jameson, Jenna", Geschlecht.w, Altersklasse.U21, verein1, 60.0, Optional.of(Farbe.WEISS), false, false, turnierUUID);
	public static Wettkaempfer wettkaempfer2 = new Wettkaempfer(UUID.fromString("db4d4ff2-8fe2-4f4e-a08c-697846fb6390"), "Fox, Sweetie", Geschlecht.w, Altersklasse.U21, verein2, 61.0, Optional.of(Farbe.BRAUN), false, false, turnierUUID);
	public static Wettkaempfer wettkaempfer3 = new Wettkaempfer(UUID.fromString("eaa236dc-cc3e-4df0-92c7-f9877159d8dc"), "Reid, Riley", Geschlecht.w, Altersklasse.U21, verein1, 62.0, Optional.of(Farbe.GELB), false, false, turnierUUID);
	public static Wettkaempfer wettkaempfer4 = new Wettkaempfer(UUID.fromString("1f245095-199c-4f5a-b109-cfabb09efab3"), "Belle, Lexi", Geschlecht.w, Altersklasse.U21, verein2, 63.0, Optional.of(Farbe.ORANGE), false, false, turnierUUID);

	public static Wertung wertung1a = new Wertung(UUID.fromString("b1521c63-19de-4ef3-bdfa-96d5d50c604f"), null, Duration.ZERO, 0, 0, 0, 0,
		6, 5, 2, 2, 1, 2, 1, 1, kampfrichter);
	public static Wertung wertung1b = new Wertung(UUID.fromString("68a5c197-848d-48c3-ba1f-54d3d82c0bfa"), null, Duration.ZERO, 0, 0, 0, 0,
		4, 4, 4, 4, 1, 1, 1, 1, kampfrichter);
	public static Wertung wertung2a = new Wertung(UUID.fromString("16485cd0-f45a-4fbc-895b-8d13ad7e960d"), null, Duration.ZERO, 0, 0, 0, 0,
		6, 5, 3, 3, 1, 1, 1, 1, kampfrichter);
	public static Wertung wertung2b = new Wertung(UUID.fromString("9c148628-13eb-487f-91bb-2d4f2b4876f1"), null, Duration.ZERO, 0, 0, 0, 0,
		1, 6, 6, 4, 3, 2, 1, 1, kampfrichter);
	public static Wertung wertung3a = new Wertung(UUID.fromString("6c70e666-8441-4218-9df2-32a5ee191b23"), null, Duration.ZERO, 0, 0, 0, 0,
		6, 1, 1, 1, 1, 3, 3, 1, kampfrichter);
	public static Wertung wertung3b = new Wertung(UUID.fromString("5bba6c54-e8f2-4c62-b016-dc55b9f7ed44"), null, Duration.ZERO, 0, 0, 0, 0,
		1, 1, 1, 1, 3, 2, 1, 4, kampfrichter);

	// Runde 1
	public static Begegnung     begegnung1a = new Begegnung(UUID.fromString("dc896258-6e73-45cc-8b7e-47c695957d78"), new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1), UUID.fromString("f3002df6-5172-4048-b715-b860fffe0c66"), 1, 1, 1, 1,
		Optional.of(wettkaempfer1), Optional.of(wettkaempfer2), List.of(wertung1a), null, turnierUUID);
	public static Begegnung     begegnung1b = new Begegnung(UUID.fromString("e6e5a7be-cae1-44de-a462-b1efbcab1d1d"), new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 1, 2), UUID.fromString("f3002df6-5172-4048-b715-b860fffe0c66"), 1, 1, 1, 1,
		Optional.of(wettkaempfer3), Optional.of(wettkaempfer4), List.of(wertung1b), null, turnierUUID);
	public static Begegnung     begegnung2a = new Begegnung(UUID.fromString("5f951bc0-7025-4bed-bb7a-488c99dc5d69"), new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 2, 1), UUID.fromString("28698372-788b-4c2b-9f16-2ce1bf27543d"), 1, 2, 2, 1,
		Optional.of(wettkaempfer1), Optional.of(wettkaempfer4), List.of(wertung2a), null, turnierUUID);
	public static Begegnung     begegnung2b = new Begegnung(UUID.fromString("5bb79609-567a-445b-ad5e-31c3f5b1fe2e"), new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 2, 2), UUID.fromString("28698372-788b-4c2b-9f16-2ce1bf27543d"), 1, 2, 2, 1,
		Optional.of(wettkaempfer3), Optional.of(wettkaempfer2), List.of(wertung2b), null, turnierUUID);
	public static Begegnung     begegnung3a = new Begegnung(UUID.fromString("06066382-5667-48db-afd6-0030151e3820"), new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 3, 1), UUID.fromString("2bce5622-04c8-4aab-b5e1-3dc5ef8ff391"), 1, 3, 3, 1,
		Optional.of(wettkaempfer1), Optional.of(wettkaempfer3), List.of(wertung3a), null, turnierUUID);
	public static Begegnung     begegnung3b = new Begegnung(UUID.fromString("44be7ae3-973c-41bb-bc3f-8f773985e11e"), new Begegnung.BegegnungId(Begegnung.RundenTyp.GEWINNERRUNDE, 3, 2), UUID.fromString("2bce5622-04c8-4aab-b5e1-3dc5ef8ff391"), 1, 3, 3, 1,
		Optional.of(wettkaempfer2), Optional.of(wettkaempfer4), List.of(wertung3b), null, turnierUUID);

	@BeforeEach
	void setUp() {
		wettkaempfer = new Wettkaempfer(UUID.randomUUID(), "Max", Geschlecht.m, Altersklasse.U18, new Verein(UUID.randomUUID(), "Verein1", turnierUUID), 70d, Optional.of(Farbe.BLAU), true, false, turnierUUID);
	}

	@Test
	void testAlleKaempfer() {
		when(wettkaempferRepository.findAll(turnierUUID)).thenReturn(List.of(wettkaempfer));

		List<Wettkaempfer> wettkaempferList = wettkaempferService.alleKaempfer(turnierUUID);

		assertEquals(1, wettkaempferList.size());
		assertEquals(wettkaempfer, wettkaempferList.getFirst());
	}

	@Test
	void testLoescheKaempfer() {
		wettkaempferService.loescheKaempfer(wettkaempfer.id());

		verify(wettkaempferRepository, times(1)).deleteById(wettkaempfer.id());
	}

	@Test
	void testLadeKaempfer() {
		when(wettkaempferRepository.findById(any())).thenReturn(Optional.of(wettkaempfer));

		Optional<Wettkaempfer> result = wettkaempferService.ladeKaempfer(wettkaempfer.id());

		assertEquals(wettkaempfer, result.get());
	}

	@Test
	void testLadeKaempferNotFound() {
		when(wettkaempferRepository.findById(any())).thenReturn(Optional.empty());

		Optional<Wettkaempfer> result = wettkaempferService.ladeKaempfer(wettkaempfer.id());

		assertTrue(result.isEmpty());
	}

	@Test
	void testSpeichereKaempfer() {
		when(wettkaempferRepository.save(any(Wettkaempfer.class))).thenReturn(wettkaempfer);

		Wettkaempfer result = wettkaempferService.speichereKaempfer(wettkaempfer);

		assertEquals(wettkaempfer, result);
	}

	@Test
	void testSpeichereCSV() {
		String csvContent = """
			    name,gewicht,altersklasse,geschlecht,vereinsname
			    "ABC123",19,U9,m,"HTG Bad Homburg"
			    "DEF456",20,U9,w,"JCN Lindenfels"
			    "GHI789",20,U9,m,"JCN Lindenfels"
			    "JKL012",21,U9,,"1. JC Großkrotzenburg"
			    "MNO345",21,U9,w,"1. JC Großkrotzenburg"
			    "PQR678",21,U9,m,"1. JC Großkrotzenburg"
			    "STU901",22,U9,w,"JCN Lindenfels"
			    "VWX234",22,U9,m,"JCN Lindenfels"
			    "YZA567",22,U9,w,"1. JC Großkrotzenburg"
			    "BCD890",22,U9,m,"HTG Bad Homburg"
			    "EFG123",23,,m,"1. JC Großkrotzenburg"
			    "HIJ456",23,U9,m,"JCN Lindenfels"
			    "KLM789",23,U9,m,"Dantai Wölfersheim"
			    "NOP012",23,U9,w,"1. JC Großkrotzenburg"
			""";
		MockMultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv", csvContent.getBytes());

		when(vereinService.sucheVerein(any(), eq(turnierUUID))).thenReturn(Optional.empty());

		wettkaempferService.speichereCSV(turnierUUID, file);

		ArgumentCaptor<Wettkaempfer> argumentCaptor = ArgumentCaptor.forClass(Wettkaempfer.class);
		verify(wettkaempferRepository, times(14)).save(argumentCaptor.capture());
		List<Wettkaempfer> capturedArguments = argumentCaptor.getAllValues();

		assertEquals(null, capturedArguments.getFirst().id());
		assertEquals("ABC123", capturedArguments.getFirst().name());
		assertEquals(turnierUUID, capturedArguments.getFirst().turnierUUID());
		assertEquals(Altersklasse.U9, capturedArguments.getFirst().altersklasse());
	}

	@Test
	void berechneGesamtWertung() {
		when(einstellungenService.isRandori(eq(turnierUUID))).thenReturn(true);
		when(wettkaempferRepository.findAll(eq(turnierUUID))).thenReturn(List.of(wettkaempfer1, wettkaempfer2, wettkaempfer3, wettkaempfer4));
		when(wettkampfService.ladeAlleBegegnungen(eq(turnierUUID))).thenReturn(List.of(begegnung1a, begegnung1b, begegnung2a, begegnung2b, begegnung3a, begegnung3b));

		Map<UUID, GesamtWertung> wertungMap = wettkaempferService.berechneGesamtWertungen(turnierUUID);

		System.out.println(wertungMap);
		assertEquals(4, wertungMap.size());

		GesamtWertung wertungWettkaempfer1 = wertungMap.get(wettkaempfer1.id());
		assertEquals(3, wertungWettkaempfer1.kampfgeistListe().size());
		assertEquals(3, wertungWettkaempfer1.technikListe().size());
		assertEquals(3, wertungWettkaempfer1.kampfstilListe().size());
		assertEquals(3, wertungWettkaempfer1.vielfaltListe().size());
		assertEquals(6, wertungWettkaempfer1.kampfgeist());
		assertEquals(3.7, wertungWettkaempfer1.technik(), 0.1);
		assertEquals(2, wertungWettkaempfer1.kampfstil());
		assertEquals(2, wertungWettkaempfer1.vielfalt());

		GesamtWertung wertungWettkaempfer2 = wertungMap.get(wettkaempfer2.id());
		assertEquals(3, wertungWettkaempfer2.kampfgeistListe().size());
		assertEquals(3, wertungWettkaempfer2.technikListe().size());
		assertEquals(3, wertungWettkaempfer2.kampfstilListe().size());
		assertEquals(3, wertungWettkaempfer2.vielfaltListe().size());
		assertEquals(1.7, wertungWettkaempfer2.kampfgeist(), 0.1);
		assertEquals(1.7, wertungWettkaempfer2.technik(), 0.1);
		assertEquals(1, wertungWettkaempfer2.kampfstil());
		assertEquals(1, wertungWettkaempfer2.vielfalt());

		GesamtWertung wertungWettkaempfer3 = wertungMap.get(wettkaempfer3.id());
		assertEquals(3, wertungWettkaempfer3.kampfgeistListe().size());
		assertEquals(3, wertungWettkaempfer3.technikListe().size());
		assertEquals(3, wertungWettkaempfer3.kampfstilListe().size());
		assertEquals(3, wertungWettkaempfer3.vielfaltListe().size());
		assertEquals(2, wertungWettkaempfer3.kampfgeist());
		assertEquals(4.3, wertungWettkaempfer3.technik(), 0.1);
		assertEquals(4.3, wertungWettkaempfer3.kampfstil(), 0.1);
		assertEquals(3, wertungWettkaempfer3.vielfalt());

		GesamtWertung wertungWettkaempfer4 = wertungMap.get(wettkaempfer4.id());
		assertEquals(3, wertungWettkaempfer4.kampfgeistListe().size());
		assertEquals(3, wertungWettkaempfer4.technikListe().size());
		assertEquals(3, wertungWettkaempfer4.kampfstilListe().size());
		assertEquals(3, wertungWettkaempfer4.vielfaltListe().size());
		assertEquals(1.7, wertungWettkaempfer4.kampfgeist(), 0.1);
		assertEquals(1.3, wertungWettkaempfer4.technik(), 0.1);
		assertEquals(1, wertungWettkaempfer4.kampfstil());
		assertEquals(2, wertungWettkaempfer4.vielfalt());
	}

	@Test
	@Disabled
	void berechneGesamtPlatzierung() {
		when(einstellungenService.isRandori(eq(turnierUUID))).thenReturn(false);
		//		when(wettkaempferRepository.findAll(eq(turnierUUID))).thenReturn(List.of(wettkaempfer1, wettkaempfer2, wettkaempfer3, wettkaempfer4));
		//		when(turnierService.ladeAlleBegegnungen(eq(turnierUUID))).thenReturn(List.of(begegnung1, begegnung2, begegnung3, begegnung4, begegnung5, begegnung6));

		Map<UUID, GesamtPlatzierung> platzierungMap = wettkaempferService.berechneGesamtPlatzierungen(turnierUUID);

		System.out.println(platzierungMap);

	}
}
