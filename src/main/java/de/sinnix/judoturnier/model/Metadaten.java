package de.sinnix.judoturnier.model;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public record Metadaten(List<UUID> alleRundenBegegnungIds, Optional<UUID> vorherigeBegegnungId, Optional<UUID> nachfolgendeBegegnungId, UUID rundeId) {
}
