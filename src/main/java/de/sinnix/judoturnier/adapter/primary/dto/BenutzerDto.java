package de.sinnix.judoturnier.adapter.primary.dto;

public record BenutzerDto(String userid, String username, String name, Boolean istAdmin, Boolean istKampfrichter, Boolean istBeobachter, Boolean istAnonym, Boolean turnierZugeordnet) {
}
