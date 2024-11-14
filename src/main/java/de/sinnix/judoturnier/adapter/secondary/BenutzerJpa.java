package de.sinnix.judoturnier.adapter.secondary;

import de.sinnix.judoturnier.model.BenutzerRolle;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "benutzer")
public class BenutzerJpa {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String                 uuid;
	private String                 username;
	private String                 name;
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "benutzer_uuid", referencedColumnName = "uuid") // Join mit der Spalte `benutzer_uuid`
	private List<TurnierRollenJpa> turnierRollen;
	@Convert(converter = BenutzerRolleListConverter.class)
	private List<BenutzerRolle>    rollen;
}
