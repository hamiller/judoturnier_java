package de.sinnix.judoturnier.adapter.secondary;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "gewichtsklassengruppen", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"id", "turnier_uuid"})
})
public class GewichtsklassenJpa {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer               id;
	private String                altersklasse;
	private String                gruppengeschlecht;
	@OneToMany(fetch = FetchType.EAGER)
	@JoinTable(
		name = "gewichtsklassengruppen_wettkaempfer",
		joinColumns = {
			@JoinColumn(name = "gewichtsklassengruppe_id", referencedColumnName = "id"),
			@JoinColumn(name = "turnier_uuid", referencedColumnName = "turnier_uuid")
		},
		inverseJoinColumns = @JoinColumn(name = "wettkaempfer_id")
	)
	private List<WettkaempferJpa> teilnehmer;
	private String                name;
	private Double                mingewicht;
	private Double                maxgewicht;
	@Column(name = "turnier_uuid", nullable = false)
	private String                turnierUUID;
}
