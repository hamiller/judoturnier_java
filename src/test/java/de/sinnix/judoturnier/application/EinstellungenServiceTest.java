package de.sinnix.judoturnier.application;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.sinnix.judoturnier.adapter.secondary.jpa.EinstellungJpa;
import de.sinnix.judoturnier.adapter.secondary.EinstellungJpaRepository;
import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Einstellungen;
import de.sinnix.judoturnier.model.Gruppengroessen;
import de.sinnix.judoturnier.model.MattenAnzahl;
import de.sinnix.judoturnier.model.SeparateAlterklassen;
import de.sinnix.judoturnier.model.TurnierTyp;
import de.sinnix.judoturnier.model.VariablerGewichtsteil;
import de.sinnix.judoturnier.model.WettkampfReihenfolge;
import de.sinnix.judoturnier.model.Wettkampfzeiten;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EinstellungenServiceTest {

	@Mock
	private EinstellungJpaRepository einstellungJpaRepository;
	@Spy
	private ObjectMapper             objectMapper = new ObjectMapper();
	@InjectMocks
	private EinstellungenService     einstellungenService;

	private UUID   turnierUUID;
	private String wettkampfzeitenString = """
		{"altersklasseKampfzeitSekunden":{
		   "U9": 180,
		   "U11": 180,
		   "U12": 180,
		   "U13": 180,
		   "U15": 180,
		   "U18": 180,
		   "U21": 240,
		   "FRAUEN": 240,
		   "MAENNER": 240
		 }}
		""";
	private String gruppengroessenString = """
		{"altersklasseGruppengroesse":{
		            "U9": 6,
		            "U11": 30
		      }}
		""";

	@BeforeEach
	void setUp() {
		turnierUUID = UUID.randomUUID();
	}

	@Test
	void testLadeEinstellungen() {
		EinstellungJpa turnierTypJpa = new EinstellungJpa(new EinstellungJpa.EinstellungId(TurnierTyp.TYP, turnierUUID), "RANDORI");
		EinstellungJpa mattenAnzahlJpa = new EinstellungJpa(new EinstellungJpa.EinstellungId(MattenAnzahl.TYP, turnierUUID), "4");
		EinstellungJpa wettkampfReihenfolgeJpa = new EinstellungJpa(new EinstellungJpa.EinstellungId(WettkampfReihenfolge.TYP, turnierUUID), "ABWECHSELND");
		EinstellungJpa wettkampfzeitenJpa = new EinstellungJpa(new EinstellungJpa.EinstellungId(Wettkampfzeiten.TYP, turnierUUID), wettkampfzeitenString);
		EinstellungJpa gruppengroessenJpa = new EinstellungJpa(new EinstellungJpa.EinstellungId(Gruppengroessen.TYP, turnierUUID), gruppengroessenString);

		Gruppengroessen gruppengroessen = new Gruppengroessen(Map.of(
			Altersklasse.U9, 6,
			Altersklasse.U11, 30));
		Wettkampfzeiten wettkampfzeiten = new Wettkampfzeiten(Map.of(
			Altersklasse.U9, 3 * 60,
			Altersklasse.U11, 3 * 60,
			Altersklasse.U12, 3 * 60,
			Altersklasse.U13, 3 * 60,
			Altersklasse.U15, 3 * 60,
			Altersklasse.U18, 3 * 60,
			Altersklasse.U21, 4 * 60,
			Altersklasse.FRAUEN, 4 * 60,
			Altersklasse.MAENNER, 4 * 60));

		when(einstellungJpaRepository.findAll()).thenReturn(List.of(turnierTypJpa, mattenAnzahlJpa, wettkampfReihenfolgeJpa, gruppengroessenJpa, wettkampfzeitenJpa));

		Einstellungen einstellungen = einstellungenService.ladeEinstellungen(turnierUUID);

		assertEquals(TurnierTyp.RANDORI, einstellungen.turnierTyp());
		assertEquals(4, einstellungen.mattenAnzahl().anzahl());
		assertEquals(WettkampfReihenfolge.ABWECHSELND, einstellungen.wettkampfReihenfolge());
		assertEquals(turnierUUID, einstellungen.turnierUUID());
		assertEquals(gruppengroessen, einstellungen.gruppengroessen());
		assertEquals(wettkampfzeiten, einstellungen.wettkampfzeiten());
	}

	@Test
	void testSpeichereTurnierEinstellungen() throws Exception {
		Einstellungen einstellungen = new Einstellungen(TurnierTyp.RANDORI, new MattenAnzahl(4), WettkampfReihenfolge.ABWECHSELND, new Gruppengroessen(Map.of(Altersklasse.U9, 6, Altersklasse.U11, 30)), new VariablerGewichtsteil(0.2d), SeparateAlterklassen.ZUSAMMEN, new Wettkampfzeiten(Map.of(
			Altersklasse.U9, 3 * 60,
			Altersklasse.U11, 3 * 60,
			Altersklasse.U12, 3 * 60,
			Altersklasse.U13, 3 * 60,
			Altersklasse.U15, 3 * 60,
			Altersklasse.U18, 3 * 60,
			Altersklasse.U21, 4 * 60,
			Altersklasse.FRAUEN, 4 * 60,
			Altersklasse.MAENNER, 4 * 60)), turnierUUID);

		when(einstellungJpaRepository.findAll()).thenReturn(List.of());

		einstellungenService.speichereTurnierEinstellungen(einstellungen);

		ArgumentCaptor<List<EinstellungJpa>> argumentCaptor = ArgumentCaptor.forClass(List.class);
		verify(einstellungJpaRepository, times(1)).saveAll(argumentCaptor.capture());

		List<EinstellungJpa> gespeicherteEinstellungen = argumentCaptor.getValue();
		assertEquals(7, gespeicherteEinstellungen.size());
		assertEquals(TurnierTyp.TYP, gespeicherteEinstellungen.get(0).getId().getArt());
		assertEquals(TurnierTyp.RANDORI.name(), gespeicherteEinstellungen.get(0).getWert());
		assertEquals(MattenAnzahl.TYP, gespeicherteEinstellungen.get(1).getId().getArt());
		assertEquals("4", gespeicherteEinstellungen.get(1).getWert());
		assertEquals(WettkampfReihenfolge.TYP, gespeicherteEinstellungen.get(2).getId().getArt());
		assertEquals(WettkampfReihenfolge.ABWECHSELND.name(), gespeicherteEinstellungen.get(2).getWert());
		assertEquals(Gruppengroessen.TYP, gespeicherteEinstellungen.get(3).getId().getArt());
		assertTrue(equalJson2String(gruppengroessenString, gespeicherteEinstellungen.get(3).getWert()));
		assertEquals(VariablerGewichtsteil.TYP, gespeicherteEinstellungen.get(4).getId().getArt());
		assertEquals("0.2", gespeicherteEinstellungen.get(4).getWert());
		assertEquals(SeparateAlterklassen.TYP, gespeicherteEinstellungen.get(5).getId().getArt());
		assertEquals(SeparateAlterklassen.ZUSAMMEN.name(), gespeicherteEinstellungen.get(5).getWert());
		assertEquals(Wettkampfzeiten.TYP, gespeicherteEinstellungen.get(6).getId().getArt());
		assertTrue(equalJson2String(wettkampfzeitenString, gespeicherteEinstellungen.get(6).getWert()));
	}

	@Test
	void testIsRandori() {
		EinstellungJpa turnierTypJpa = new EinstellungJpa(new EinstellungJpa.EinstellungId(TurnierTyp.TYP, turnierUUID), "RANDORI");

		when(einstellungJpaRepository.findById(new EinstellungJpa.EinstellungId(TurnierTyp.TYP, turnierUUID))).thenReturn(Optional.of(turnierTypJpa));

		boolean isRandori = einstellungenService.isRandori(turnierUUID);

		assertTrue(isRandori);
	}

	@Test
	void testIsNotRandori() {
		EinstellungJpa turnierTypJpa = new EinstellungJpa(new EinstellungJpa.EinstellungId(TurnierTyp.TYP, turnierUUID), "STANDARD");

		when(einstellungJpaRepository.findById(new EinstellungJpa.EinstellungId(TurnierTyp.TYP, turnierUUID))).thenReturn(Optional.of(turnierTypJpa));

		boolean isRandori = einstellungenService.isRandori(turnierUUID);

		assertFalse(isRandori);
	}

	public boolean equalJson2String(String json1, String json2) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();

		// JSON-Strings in JsonNode parsen
		JsonNode node1 = objectMapper.readTree(json1);
		JsonNode node2 = objectMapper.readTree(json2);

		System.out.println("node 1: " + node1);
		System.out.println("node 2: " + node2);
		// Vergleich
		return node1.equals(node2);
	}
}
