package de.sinnix.judoturnier.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
@Setter
public class Begegnung {
	private Integer         begegnungId;
	private UUID			rundeId;
	private Integer         matteId;
	private Integer         mattenRunde;
	private Integer         gruppenRunde;
	private Integer         gesamtRunde;
	private Wettkaempfer    wettkaempfer1;
	private Wettkaempfer    wettkaempfer2;
	private List<Wertung>   wertungen;
	private WettkampfGruppe wettkampfGruppe;
	private UUID            turnierUUID;
}
