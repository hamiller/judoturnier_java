package de.sinnix.judoturnier.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
@Setter
public class Begegnung {
	private Integer           begegnungId;
	private Integer           matteId;
	private Integer           mattenRunde;
	private Integer           gruppenRunde;
	private Wettkaempfer      wettkaempfer1;
	private Wettkaempfer      wettkaempfer2;
	private Optional<Wertung> wertung;
	private WettkampfGruppe   wettkampfGruppe;
	private UUID              turnierUUID;
}
