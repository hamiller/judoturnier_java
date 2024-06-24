package de.sinnix.judoturnier.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum RandoriGruppenName {
	Igel,
	Tiger,
	Elefant,
	Löwe,
	Giraffe,
	Leopard,
	Gepard,
	Marder,
	Eichhörnchen,
	Bär,
	Affe,
	Wal,
	Wildkatze,
	Wolf,
	Ameise,
	Biene,
	Delfin,
	Hai,
	Iltis,
	Antilope,
	Hummel,
	Wiesel,
	Hase,
	Kaninchen,
	Adler,
	Falke,
	Bussard,
	Panther,
	Leguan,
	Eule,
	Maus,
	Dachs,
	Kiwi,
	Känguruh,
	Reh,
	Spatz,
	Ente,
	Schnabeltier,
	Katze;

	public static List<RandoriGruppenName> alleGruppenNamen() {
		List<RandoriGruppenName> gruppenNamen = new ArrayList<>();
		Collections.addAll(gruppenNamen, RandoriGruppenName.values());
		return gruppenNamen;
	}

	public static List<RandoriGruppenName> randomRandoriGruppenNamen(int count) {
		List<RandoriGruppenName> enumValues = new ArrayList<>(List.of(RandoriGruppenName.values()));
		List<RandoriGruppenName> result = new ArrayList<>();
		Random random = new Random();

		while (result.size() < count && !enumValues.isEmpty()) {
			int randomIndex = random.nextInt(enumValues.size());
			RandoriGruppenName randomEntry = enumValues.remove(randomIndex);
			result.add(randomEntry);
		}

		return result;
	}

	public static void main(String[] args) {
		// Teste alleGruppenNamen
		System.out.println("Alle Gruppen Namen: " + RandoriGruppenName.alleGruppenNamen());

		// Teste randomRandoriGruppenNamen
		System.out.println("Zufällige Gruppen Namen: " + RandoriGruppenName.randomRandoriGruppenNamen(5));
	}
}