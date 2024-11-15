package de.sinnix.judoturnier.adapter.secondary;

import de.sinnix.judoturnier.model.BenutzerRolle;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@Table(name = "benutzer")
public class BenutzerJpa extends AbstractEntity {
	private String                 username;
	private String                 name;
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "benutzer_uuid", referencedColumnName = "uuid") // Join mit der Spalte `benutzer_uuid`
	private List<TurnierRollenJpa> turnierRollen;
	@Convert(converter = BenutzerRolleListConverter.class)
	private List<BenutzerRolle>    rollen;

	public BenutzerJpa(String uuid, String username, String name, List<TurnierRollenJpa> turnierRollen, List<BenutzerRolle> rollen) {
		super(uuid);
		this.username = username;
		this.name = name;
		this.turnierRollen = turnierRollen;
		this.rollen = rollen;
	}
}
