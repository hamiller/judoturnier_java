package de.sinnix.judoturnier.adapter.secondary;

import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.WettkampfGruppe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class WettkampfGruppeConverterTest {

	@InjectMocks
	private WettkampfGruppeConverter converter;

	@BeforeEach
	void setUp() {
	}

	@Test
	void testConvertToWettkampfGruppe() {
		WettkampfGruppeJpa wettkampfGruppeJpa = new WettkampfGruppeJpa();
		wettkampfGruppeJpa.setId(UUID.randomUUID());
		wettkampfGruppeJpa.setName("Gruppe A");
		wettkampfGruppeJpa.setTyp("Team");
		wettkampfGruppeJpa.setAltersklasse("U11");
		wettkampfGruppeJpa.setTurnierUUID(UUID.randomUUID());

		WettkampfGruppe wettkampfGruppe = converter.convertToWettkampfGruppe(wettkampfGruppeJpa);
		assertNotNull(wettkampfGruppe);
		assertEquals(wettkampfGruppeJpa.getId(), wettkampfGruppe.id());
		assertEquals(wettkampfGruppeJpa.getName(), wettkampfGruppe.name());
		assertEquals(wettkampfGruppeJpa.getTyp(), wettkampfGruppe.typ());
		assertEquals(wettkampfGruppeJpa.getAltersklasse(), wettkampfGruppe.altersklasse().name());
		assertEquals(wettkampfGruppeJpa.getTurnierUUID(), wettkampfGruppe.turnierUUID());
	}

	@Test
	void convertFromWettkampfGruppe() {
		WettkampfGruppe wettkampfGruppe = new WettkampfGruppe(
			UUID.randomUUID(),
			"Gruppe A",
			"Team",
			Altersklasse.U11,
			UUID.randomUUID()
		);

		// Act
		WettkampfGruppeJpa jpa = converter.convertFromWettkampfGruppe(wettkampfGruppe);

		// Assert
		assertNotNull(jpa);
		assertEquals(null, jpa.getId());
		assertEquals(wettkampfGruppe.name(), jpa.getName());
		assertEquals(wettkampfGruppe.typ(), jpa.getTyp());
		assertEquals(wettkampfGruppe.altersklasse().name(), jpa.getAltersklasse());
		assertEquals(wettkampfGruppe.turnierUUID(), jpa.getTurnierUUID());
	}
}
