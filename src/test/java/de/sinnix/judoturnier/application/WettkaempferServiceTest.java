package de.sinnix.judoturnier.application;

import de.sinnix.judoturnier.adapter.secondary.WettkaempferRepository;
import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Farbe;
import de.sinnix.judoturnier.model.Geschlecht;
import de.sinnix.judoturnier.model.Verein;
import de.sinnix.judoturnier.model.Wettkaempfer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;
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
	@InjectMocks
	private WettkaempferService    wettkaempferService;

	private Wettkaempfer wettkaempfer;
	private UUID         turnierUUID;

	@BeforeEach
	void setUp() {
		turnierUUID = UUID.randomUUID();
		wettkaempfer = new Wettkaempfer(UUID.randomUUID(), "Max", Geschlecht.m, Altersklasse.U18, new Verein(UUID.randomUUID(), "Verein1", turnierUUID), 70d, Optional.of(Farbe.BLAU), true, false, turnierUUID);
	}

	@Test
	void testAlleKaempfer() {
		when(wettkaempferRepository.findAll(turnierUUID)).thenReturn(List.of(wettkaempfer));

		List<Wettkaempfer> wettkaempferList = wettkaempferService.alleKaempfer(turnierUUID);

		assertEquals(1, wettkaempferList.size());
		assertEquals(wettkaempfer, wettkaempferList.get(0));
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

		assertEquals(null, capturedArguments.get(0).id());
		assertEquals("ABC123", capturedArguments.get(0).name());
		assertEquals(turnierUUID, capturedArguments.get(0).turnierUUID());
		assertEquals(Altersklasse.U9, capturedArguments.get(0).altersklasse());
	}
}