package de.sinnix.judoturnier.adapter.primary;

import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Runde;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HelperSourceTest {

	@Test
	void testVorherigesElement() {
		var a = UUID.randomUUID();
		var b = UUID.randomUUID();
		var c = UUID.randomUUID();
		List<Runde> runden = List.of(new Runde(a, 1, 1, 1,1, Altersklasse.U11, null, List.of()),
			new Runde(b, 2, 2, 2,2, Altersklasse.U11, null, List.of()),
			new Runde(c, 3, 3, 3,3, Altersklasse.U11, null, List.of())
			);
		var newRunden = HelperSource.vorherigeRunde(runden, null);

		assertEquals(null, newRunden.get(0).getLeft());
		assertEquals(a, newRunden.get(0).getRight().rundeId());
		assertEquals(a, newRunden.get(1).getLeft().rundeId());
		assertEquals(b, newRunden.get(1).getRight().rundeId());
		assertEquals(b, newRunden.get(2).getLeft().rundeId());
		assertEquals(c, newRunden.get(2).getRight().rundeId());
	}
}