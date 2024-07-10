package de.sinnix.judoturnier.model;

import java.util.Date;
import java.util.UUID;

public record Turnier(UUID uuid, String name, String ort, Date datum) {
}
