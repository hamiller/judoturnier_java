package de.sinnix.judoturnier.adapter.secondary;

import de.sinnix.judoturnier.model.BenutzerRolle;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "turnier_rollen")
public class TurnierRollenJpa extends AbstractEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "benutzer_uuid", nullable = false)
	private BenutzerJpa         benutzer;
	@Convert(converter = BenutzerRolleListConverter.class)
	@Column(name = "rollen")
	private List<BenutzerRolle> rollen;
	@Column(name = "turnier_uuid")
	private String              turnierUuid;

	public TurnierRollenJpa(String uuid, BenutzerJpa benutzer, List<BenutzerRolle> rollen, String turnierUuid) {
		super(uuid);
		this.benutzer = benutzer;
		this.rollen = rollen;
		this.turnierUuid = turnierUuid;
	}
}
