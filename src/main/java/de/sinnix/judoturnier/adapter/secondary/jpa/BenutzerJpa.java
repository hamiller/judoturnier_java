package de.sinnix.judoturnier.adapter.secondary.jpa;

import de.sinnix.judoturnier.adapter.secondary.converter.BenutzerRolleListConverter;
import de.sinnix.judoturnier.model.BenutzerRolle;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@Data
@EqualsAndHashCode
@ToString(callSuper = true)
@Entity
@Table(
	name = "benutzer",
	uniqueConstraints = @UniqueConstraint(columnNames = "username")
)
// keine automatische UUID-Generierung, da die UUID vom Auth-System kommt
public class BenutzerJpa {
	@Id
	private UUID id;
	@Version
	private Long version;
	private String                 username;
	private String                 name;
	@OneToMany(mappedBy = "benutzer", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<TurnierRollenJpa> turnierRollen = new ArrayList<>();
	@Convert(converter = BenutzerRolleListConverter.class)
	private List<BenutzerRolle>    rollen        = new ArrayList<>();

	public BenutzerJpa(String username, String name, List<TurnierRollenJpa> turnierRollen, List<BenutzerRolle> rollen) {
		super();
		this.username = username;
		this.name = name;
		this.turnierRollen = turnierRollen;
		this.rollen = rollen;
	}

	public void updateFrom(BenutzerJpa benutzer) {
		if (rollen != null) {
			rollen.clear();
			rollen.addAll(benutzer.getRollen());
		}
		if (turnierRollen != null) {
			turnierRollen.clear();
			turnierRollen.addAll(benutzer.getTurnierRollen());
		}
	}
}
