package de.sinnix.judoturnier.adapter.primary;

import de.sinnix.judoturnier.model.Begegnung;
import de.sinnix.judoturnier.model.Wertung;
import de.sinnix.judoturnier.model.Wettkaempfer;

import java.util.List;
import java.util.Optional;

public record BegegnungDto(Integer begegnungId,
						   Begegnung.RundenTyp rundenTyp,
						   Integer runde,
						   Integer akuellePaarung,
						   Wettkaempfer wettkaempfer1,
						   Wettkaempfer wettkaempfer2,
						   Optional<WertungDto> kampfrichterWertung,
						   List<Wertung> alleWertungen,
						   String vorherigeBegegnungId,
						   String nachfolgerBegegnungId
) {
}
