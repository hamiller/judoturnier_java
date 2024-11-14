package de.sinnix.judoturnier.adapter.secondary;

import de.sinnix.judoturnier.model.BenutzerRolle;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "turnier_rollen")
public class TurnierRollenJpa {
	@EmbeddedId
	private TurnierRollenId id;

	@Convert(converter = BenutzerRolleListConverter.class)
	@Column(name="rollen")
	private List<BenutzerRolle> rollen;

	@AllArgsConstructor
	@NoArgsConstructor
	@Data
	@Embeddable
	public static class TurnierRollenId implements Serializable {
		@Column(name="benutzer_uuid")
		private String benutzerUuid;
		@Column(name="turnier_uuid")
		private String turnierUuid;
	}
}
