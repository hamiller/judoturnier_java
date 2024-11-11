package de.sinnix.judoturnier.model;

import java.util.UUID;

public record Verein(UUID id, String name, UUID turnierUUID) {
}
