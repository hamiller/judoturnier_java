package de.sinnix.judoturnier.adapter.primary.dto;

import de.sinnix.judoturnier.model.Begegnung;
import de.sinnix.judoturnier.model.Wettkaempfer;

import java.util.List;
import java.util.Optional;

public record BegegnungDto(String begegnungId,
						   Begegnung.RundenTyp rundenTyp,
						   Integer runde,
						   Integer akuellePaarung,
						   Wettkaempfer wettkaempfer1,
						   Wettkaempfer wettkaempfer2,
						   Optional<WertungDto> kampfrichterWertung,
						   List<WertungDto> alleWertungen,
						   String rundeId,
						   String vorherigeBegegnungId,
						   String nachfolgerBegegnungId
) {
}
