package de.sinnix.judoturnier.model;

import java.util.List;
import java.util.UUID;

public record TurnierRollen(UUID uuid, UUID turnierId, List<BenutzerRolle> rollen) {
}
