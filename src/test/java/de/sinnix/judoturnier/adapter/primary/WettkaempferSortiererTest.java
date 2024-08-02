package de.sinnix.judoturnier.adapter.primary;

import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Geschlecht;
import de.sinnix.judoturnier.model.Verein;
import de.sinnix.judoturnier.model.Wettkaempfer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class WettkaempferSortiererTest {

	@BeforeEach
	void setUp() {
	}

	@Test
	void testSortiereNachName() {
		Verein v1 = new Verein(null, "v1", null);
		List<Wettkaempfer> wettkaempferList = new ArrayList<>();
		wettkaempferList.add(new Wettkaempfer(null, "a", Geschlecht.m, Altersklasse.U9, v1, 0.0, Optional.empty(), false, false, null));
		wettkaempferList.add(new Wettkaempfer(null, "c", Geschlecht.w, Altersklasse.U9, v1, 0.0, Optional.empty(), false, false, null));
		wettkaempferList.add(new Wettkaempfer(null, "d", Geschlecht.m, Altersklasse.U9, v1, 0.0, Optional.empty(), false, false, null));
		wettkaempferList.add(new Wettkaempfer(null, "b", Geschlecht.w, Altersklasse.U9, v1, 0.0, Optional.empty(), false, false, null));
		wettkaempferList.add(new Wettkaempfer(null, "e", Geschlecht.m, Altersklasse.U9, v1, 0.0, Optional.empty(), false, false, null));

		var sortierteListe = wettkaempferList.stream()
			.sorted(WettkaempferSortierer.sortiere("name"))
			.collect(Collectors.toUnmodifiableList());

		assertEquals("a", sortierteListe.get(0).name());
		assertEquals("b", sortierteListe.get(1).name());
		assertEquals("c", sortierteListe.get(2).name());
		assertEquals("d", sortierteListe.get(3).name());
		assertEquals("e", sortierteListe.get(4).name());
	}

	@Test
	void testSortiereNachVerein() {
		Verein v1 = new Verein(null, "v1", null);
		Verein v2 = new Verein(null, "v2", null);
		Verein v3 = new Verein(null, "v3", null);
		List<Wettkaempfer> wettkaempferList = new ArrayList<>();
		wettkaempferList.add(new Wettkaempfer(null, "a", Geschlecht.m, Altersklasse.U9, null, 0.0, Optional.empty(), false, false, null));
		wettkaempferList.add(new Wettkaempfer(null, "c", Geschlecht.w, Altersklasse.U9, v2, 0.0, Optional.empty(), false, false, null));
		wettkaempferList.add(new Wettkaempfer(null, "d", Geschlecht.m, Altersklasse.U9, v3, 0.0, Optional.empty(), false, false, null));
		wettkaempferList.add(new Wettkaempfer(null, "b", Geschlecht.w, Altersklasse.U9, null, 0.0, Optional.empty(), false, false, null));
		wettkaempferList.add(new Wettkaempfer(null, "e", Geschlecht.m, Altersklasse.U9, v1, 0.0, Optional.empty(), false, false, null));
		wettkaempferList.add(new Wettkaempfer(null, "f", Geschlecht.m, Altersklasse.U9, null, 0.0, Optional.empty(), false, false, null));

		var sortierteListe = wettkaempferList.stream()
			.sorted(WettkaempferSortierer.sortiere("verein"))
			.collect(Collectors.toUnmodifiableList());

		assertEquals("e", sortierteListe.get(0).name());
		assertEquals("c", sortierteListe.get(1).name());
		assertEquals("d", sortierteListe.get(2).name());
		assertEquals("a", sortierteListe.get(3).name());
		assertEquals("b", sortierteListe.get(4).name());
		assertEquals("f", sortierteListe.get(5).name());
	}

	@Test
	void testSortiereNachGeschlecht() {
		Verein v1 = new Verein(null, "v1", null);
		Verein v2 = new Verein(null, "v2", null);
		Verein v3 = new Verein(null, "v3", null);
		List<Wettkaempfer> wettkaempferList = new ArrayList<>();
		wettkaempferList.add(new Wettkaempfer(null, "a", Geschlecht.m, Altersklasse.U9, v2, 0.0, Optional.empty(), false, false, null));
		wettkaempferList.add(new Wettkaempfer(null, "c", Geschlecht.w, Altersklasse.U9, v1, 0.0, Optional.empty(), false, false, null));
		wettkaempferList.add(new Wettkaempfer(null, "d", Geschlecht.m, Altersklasse.U9, v3, 0.0, Optional.empty(), false, false, null));
		wettkaempferList.add(new Wettkaempfer(null, "b", Geschlecht.w, Altersklasse.U9, null, 0.0, Optional.empty(), false, false, null));
		wettkaempferList.add(new Wettkaempfer(null, "e", Geschlecht.m, Altersklasse.U9, v1, 0.0, Optional.empty(), false, false, null));

		var sortierteListe = wettkaempferList.stream()
			.sorted(WettkaempferSortierer.sortiere("geschlecht"))
			.collect(Collectors.toUnmodifiableList());

		assertEquals("a", sortierteListe.get(0).name());
		assertEquals("d", sortierteListe.get(1).name());
		assertEquals("e", sortierteListe.get(2).name());
		assertEquals("c", sortierteListe.get(3).name());
		assertEquals("b", sortierteListe.get(4).name());
	}
}