package de.sinnix.judoturnier.adapter.secondary;

import java.util.List;
import java.util.UUID;


import com.fasterxml.jackson.annotation.JsonIgnore;
import de.sinnix.judoturnier.model.BenutzerRolle;
import de.sinnix.judoturnier.model.TurnierRollen;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = "benutzer")
@ToString(callSuper = true, exclude = "benutzer")
@Data
@Entity
@Table(name = "turnier_rollen")
public class TurnierRollenJpa extends AbstractEntity {

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "benutzer_uuid", nullable = false)
	private BenutzerJpa         benutzer;
	@Convert(converter = BenutzerRolleListConverter.class)
	@Column(name = "rollen")
	private List<BenutzerRolle> rollen;
	@Column(name = "turnier_uuid")
	private UUID                turnierUuid;

	public TurnierRollenJpa(BenutzerJpa benutzer, List<BenutzerRolle> rollen, UUID turnierUuid) {
		super();
		this.benutzer = benutzer;
		this.rollen = rollen;
		this.turnierUuid = turnierUuid;
	}

	public void updateFrom(TurnierRollen turnierRollen) {
		this.rollen = turnierRollen.rollen();
	}
}
