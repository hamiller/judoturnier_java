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
import java.util.UUID;

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
	private UUID                  turnierUUID;

	public GewichtsklassenJpa(String altersklasse, String gruppengeschlecht, List<WettkaempferJpa> teilnehmer, String name, Double mingewicht, Double maxgewicht, UUID turnierUUID) {
		super();
		this.altersklasse = altersklasse;
		this.gruppengeschlecht = gruppengeschlecht;
		this.teilnehmer = teilnehmer;
		this.name = name;
		this.mingewicht = mingewicht;
		this.maxgewicht = maxgewicht;
		this.turnierUUID = turnierUUID;
	}

	public void updateFrom(GewichtsklassenJpa gewichtsklassenJpa, List<WettkaempferJpa> wettkaempferJpaList) {
		this.altersklasse = gewichtsklassenJpa.getAltersklasse();
		this.gruppengeschlecht = gewichtsklassenJpa.getGruppengeschlecht();
		this.teilnehmer = wettkaempferJpaList;
		this.name = gewichtsklassenJpa.getName();
		this.mingewicht = gewichtsklassenJpa.getMingewicht();
		this.maxgewicht = gewichtsklassenJpa.getMaxgewicht();
	}
}
