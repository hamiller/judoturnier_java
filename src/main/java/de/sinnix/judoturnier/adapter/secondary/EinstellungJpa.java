package de.sinnix.judoturnier.adapter.secondary;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "einstellungen")
public class EinstellungJpa {
	@EmbeddedId
	private EinstellungId id;
	private String wert;

	@AllArgsConstructor
	@NoArgsConstructor
	@Data
	@Embeddable
	public static class EinstellungId implements Serializable {
		private String art;
		@Column(name = "turnier_uuid", nullable = false)
		private UUID   turnierUUID;

	}
}
