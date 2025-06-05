package de.sinnix.judoturnier.adapter.primary.dto;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public record TurnierDto(UUID uuid, String name, String ort, Date datum, List<BenutzerDto> benutzerDtoList) {
}
