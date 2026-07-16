package de.sinnix.judoturnier.application.algorithm;

import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Einstellungen;
import de.sinnix.judoturnier.model.GewichtsklassenGruppe;
import de.sinnix.judoturnier.model.TurnierTyp;
import de.sinnix.judoturnier.model.Wettkampfsystem;

public class WettkampfsystemResolver {

	public Wettkampfsystem resolve(Einstellungen einstellungen, GewichtsklassenGruppe gewichtsklassenGruppe) {
		return resolve(
			einstellungen.turnierTyp(),
			gewichtsklassenGruppe.altersKlasse(),
			gewichtsklassenGruppe.teilnehmer().size()
		);
	}

	public Wettkampfsystem resolve(TurnierTyp turnierTyp, Altersklasse altersklasse, int teilnehmerAnzahl) {
		if (teilnehmerAnzahl <= 1) {
			return Wettkampfsystem.KEIN_WETTKAMPF;
		}

		if (teilnehmerAnzahl == 2) {
			return Wettkampfsystem.BESTER_AUS_DREI;
		}

		if (turnierTyp == TurnierTyp.RANDORI) {
			return Wettkampfsystem.JEDER_GEGEN_JEDEN;
		}

		if (altersklasse == Altersklasse.U11) {
			return resolveU11(teilnehmerAnzahl);
		}

		if (istJugendbereichBisU18(altersklasse)) {
			return resolveJugendbereich(teilnehmerAnzahl);
		}

		if (istErwachsenenbereichAbU21(altersklasse)) {
			return resolveErwachsenenbereich(teilnehmerAnzahl);
		}

		throw new IllegalArgumentException("Keine Wettkampfsystem-Regel für Altersklasse " + altersklasse);
	}

	private Wettkampfsystem resolveU11(int teilnehmerAnzahl) {
		if (teilnehmerAnzahl <= 5) {
			return Wettkampfsystem.JEDER_GEGEN_JEDEN;
		}
		throw new IllegalArgumentException("U11-Gruppen dürfen höchstens 5 Teilnehmer enthalten. Teilnehmer: " + teilnehmerAnzahl);
	}

	private Wettkampfsystem resolveJugendbereich(int teilnehmerAnzahl) {
		if (teilnehmerAnzahl <= 5) {
			return Wettkampfsystem.JEDER_GEGEN_JEDEN;
		}
		if (teilnehmerAnzahl <= 8) {
			return Wettkampfsystem.ZWEI_POOLS_JEDER_GEGEN_JEDEN;
		}
		return Wettkampfsystem.DOPPELTES_KO_SYSTEM_MIT_TROSTRUNDE;
	}

	private Wettkampfsystem resolveErwachsenenbereich(int teilnehmerAnzahl) {
		if (teilnehmerAnzahl <= 5) {
			return Wettkampfsystem.POOLSYSTEM_JEDER_GEGEN_JEDEN;
		}
		return Wettkampfsystem.DOPPELTES_KO_SYSTEM_MIT_TROSTRUNDE;
	}

	private boolean istJugendbereichBisU18(Altersklasse altersklasse) {
		return altersklasse == Altersklasse.U9
			|| altersklasse == Altersklasse.U12
			|| altersklasse == Altersklasse.U13
			|| altersklasse == Altersklasse.U15
			|| altersklasse == Altersklasse.U18;
	}

	private boolean istErwachsenenbereichAbU21(Altersklasse altersklasse) {
		return altersklasse == Altersklasse.U21
			|| altersklasse == Altersklasse.MAENNER
			|| altersklasse == Altersklasse.FRAUEN;
	}
}
