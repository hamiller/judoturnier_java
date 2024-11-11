package de.sinnix.judoturnier.application;

import de.sinnix.judoturnier.adapter.secondary.VereinConverter;
import de.sinnix.judoturnier.adapter.secondary.VereinJpa;
import de.sinnix.judoturnier.adapter.secondary.VereinJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class VereinServiceTest {

	@Mock
	private VereinJpaRepository vereinJpaRepository;

	@Spy
	private VereinConverter converter;

	@InjectMocks
	private VereinService vereinService;

	@BeforeEach
	void setUp() {
	}

	@Test
	void testSpeichereCSV() {
		UUID turnierUUID = UUID.randomUUID();
		String csvContent = "NAME\nDantai Wölfersheim\nJCN Lindenfels\n1. JC Großkrotzenburg";
		MockMultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv", csvContent.getBytes());

		vereinService.speichereCSV(turnierUUID, file);

		ArgumentCaptor<VereinJpa> argumentCaptor = ArgumentCaptor.forClass(VereinJpa.class);
		verify(vereinJpaRepository, times(3)).save(argumentCaptor.capture());
		List<VereinJpa> capturedArguments = argumentCaptor.getAllValues();

		assertEquals(null, capturedArguments.get(0).getUuid());
		assertEquals("Dantai Wölfersheim", capturedArguments.get(0).getName());
		assertEquals(turnierUUID.toString(), capturedArguments.get(0).getTurnierUUID());

		assertEquals(null, capturedArguments.get(1).getUuid());
		assertEquals("JCN Lindenfels", capturedArguments.get(1).getName());
		assertEquals(turnierUUID.toString(), capturedArguments.get(1).getTurnierUUID());

		assertEquals(null, capturedArguments.get(2).getUuid());
		assertEquals("1. JC Großkrotzenburg", capturedArguments.get(2).getName());
		assertEquals(turnierUUID.toString(), capturedArguments.get(2).getTurnierUUID());
	}
}