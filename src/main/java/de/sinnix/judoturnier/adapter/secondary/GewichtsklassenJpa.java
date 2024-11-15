package de.sinnix.judoturnier.adapter.secondary;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
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
@Table(name = "gewichtsklassengruppen")
public class GewichtsklassenJpa extends AbstractEntity {
	private String                altersklasse;
	private String                gruppengeschlecht;
	@OneToMany(fetch = FetchType.EAGER)
	@JoinTable(
		name = "gewichtsklassengruppen_wettkaempfer",
		joinColumns = {
			@JoinColumn(name = "gewichtsklassengruppe_id", referencedColumnName = "uuid"),
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

	public GewichtsklassenJpa(String uuid, String altersklasse, String gruppengeschlecht, List<WettkaempferJpa> teilnehmer, String name, Double mingewicht, Double maxgewicht, String turnierUUID) {
		super(uuid);
		this.altersklasse = altersklasse;
		this.gruppengeschlecht = gruppengeschlecht;
		this.teilnehmer = teilnehmer;
		this.name = name;
		this.mingewicht = mingewicht;
		this.maxgewicht = maxgewicht;
		this.turnierUUID = turnierUUID;
	}
}
