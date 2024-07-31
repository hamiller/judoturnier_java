package de.sinnix.judoturnier.adapter.primary;

import de.sinnix.judoturnier.model.Wertung;
import de.sinnix.judoturnier.model.Wettkaempfer;

import java.util.List;
import java.util.Optional;

public record BegegnungDto(Integer begegnungId,
						   Wettkaempfer wettkaempfer1,
						   Wettkaempfer wettkaempfer2,
						   Optional<Wertung> kampfrichterWertung,
						   List<Wertung> alleWertungen,
						   String vorherigeBegegnungId,
						   String nachfolgerBegegnungId
) {
}
