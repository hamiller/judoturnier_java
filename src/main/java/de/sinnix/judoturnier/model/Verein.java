package de.sinnix.judoturnier.model;

import java.util.UUID;

public record Verein(Integer id, String name, UUID turnierUUID) {
}
