package de.sinnix.judoturnier.model;

import lombok.AllArgsConstructor;
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
	private BegegnungId            begegnungId;
	private UUID                   rundeId;
	private Integer                matteId;
	private Integer                mattenRunde;
	private Integer                gruppenRunde;
	private Integer                gesamtRunde;
	private Optional<Wettkaempfer> wettkaempfer1;
	private Optional<Wettkaempfer> wettkaempfer2;
	private List<Wertung>          wertungen;
	private WettkampfGruppe        wettkampfGruppe;
	private UUID                   turnierUUID;

	public static class BegegnungId {
		public RundenTyp rundenTyp;
		public int runde;
		public int akuellePaarung;

		public BegegnungId(RundenTyp rundenTyp, int runde, int akuellePaarung) {
			this.rundenTyp = rundenTyp;
			this.runde = runde;
			this.akuellePaarung = akuellePaarung;
		}

		@Override
		public String toString() {
			return (this.rundenTyp == RundenTyp.GEWINNER_RUNDE ? "GewinnerRunde" : "VerliererRunde") + " Runde" + this.runde + " Begegnung" + this.akuellePaarung;
		}
	}

	public enum RundenTyp {
		GEWINNER_RUNDE(1),
		VERLIERER_RUNDE(2);

		private final int value;

		RundenTyp(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}
}
