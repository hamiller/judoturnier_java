package de.sinnix.judoturnier.adapter.secondary;

import de.sinnix.judoturnier.model.Turnier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class TurnierConverterTest {

	@InjectMocks
	private TurnierConverter turnierConverter;

	@Test
	void testConvertToTurnier() {
		TurnierJpa jpa = new TurnierJpa();
		jpa.setUuid(UUID.randomUUID().toString());
		jpa.setName("Name");
		jpa.setOrt("Ort");
		jpa.setDatum(new java.sql.Date(System.currentTimeMillis()));

		Turnier result = turnierConverter.convertToTurnier(jpa);

		assertEquals(jpa.getUuid().toString(), result.uuid().toString());
		assertEquals(jpa.getName(), result.name());
		assertEquals(jpa.getOrt(), result.ort());
		assertEquals(jpa.getDatum(), result.datum());
	}

	@Test
	void testConvertFromTurnier() {
		Turnier turnier = new Turnier(UUID.randomUUID(),"Name", "Ort", new java.util.Date(System.currentTimeMillis()));

		TurnierJpa result = turnierConverter.convertFromTurnier(turnier);

		assertNotNull(result);
		assertEquals(turnier.uuid().toString(), result.getUuid().toString());
		assertEquals(turnier.name(), result.getName());
		assertEquals(turnier.ort(), result.getOrt());
		assertEquals(turnier.datum(), result.getDatum());
	}
}