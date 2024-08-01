package de.sinnix.judoturnier.model;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public record Metadaten(List<Integer> alleRundenBegegnungIds, Optional<Integer> vorherigeBegegnungId, Optional<Integer> nachfolgendeBegegnungId, UUID rundeId) {
}
