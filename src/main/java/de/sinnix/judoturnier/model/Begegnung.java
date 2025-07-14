package de.sinnix.judoturnier.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
@Setter
public class Begegnung {
	private UUID                   id;
	private BegegnungId            begegnungId;
	private UUID                   rundeId;
	private Integer                matteId;
	private Integer                mattenRunde;
	private Integer                gruppenRunde;
	private Integer                gesamtBegegnung;
	private Optional<Wettkaempfer> wettkaempfer1;
	private Optional<Wettkaempfer> wettkaempfer2;
	private List<Wertung>          wertungen;
	private WettkampfGruppe        wettkampfGruppe;
	private UUID                   turnierUUID;

	@Data
	public static class BegegnungId {
		public RundenTyp rundenTyp;		// der Typ der Runde (Gewinner- oder Verlierer-Runde)
		public int rundenNummerDesTyps; // die Nummer der Runde des Typs (Gewinner- oder Verlierer-Runde)
		public int paarungNummer;  		// die Nummer der Begegnung in der Runde

		public BegegnungId(RundenTyp rundenTyp, int rundenNummerDesTyps, int paarungNummer) {
			this.rundenTyp = rundenTyp;
			this.rundenNummerDesTyps = rundenNummerDesTyps;
			this.paarungNummer = paarungNummer;
		}

		@Override
		public String toString() {
			return (this.rundenTyp == RundenTyp.GEWINNERRUNDE ? "GewinnerRunde" : "VerliererRunde") + " TypRunde" + this.rundenNummerDesTyps + " Begegnung" + this.paarungNummer;
		}
	}

	public enum RundenTyp {
		GEWINNERRUNDE(1),
		TROSTRUNDE(2);

		private final int value;

		RundenTyp(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		public static RundenTyp fromValue(int value) {
			for (RundenTyp rundenTyp : RundenTyp.values()) {
				if (rundenTyp.getValue() == value) {
					return rundenTyp;
				}
			}
			throw new IllegalArgumentException("Unbekannter Wert: " + value);
		}
	}
}
