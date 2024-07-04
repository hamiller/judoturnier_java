package de.sinnix.judoturnier.adapter.primary;

import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Runde;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HelperSourceTest {

	@Test
	void testVorherigesElement() {
		List<Runde> runden = List.of(new Runde(1, 1, 1, 1,1, Altersklasse.U11, null, List.of()),
			new Runde(2, 2, 2, 2,2, Altersklasse.U11, null, List.of()),
			new Runde(3, 3, 3, 3,3, Altersklasse.U11, null, List.of())
			);
		var newRunden = HelperSource.vorherigeRunde(runden, null);

		assertEquals(null, newRunden.get(0).getLeft());
		assertEquals(1, newRunden.get(0).getRight().id());
		assertEquals(1, newRunden.get(1).getLeft().id());
		assertEquals(2, newRunden.get(1).getRight().id());
		assertEquals(2, newRunden.get(2).getLeft().id());
		assertEquals(3, newRunden.get(2).getRight().id());
	}
}