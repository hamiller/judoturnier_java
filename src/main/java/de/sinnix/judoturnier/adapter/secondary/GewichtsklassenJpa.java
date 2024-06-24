package de.sinnix.judoturnier.adapter.secondary;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
@Setter
@Entity
@Table(name = "gewichtsklassengruppen")
public class GewichtsklassenJpa {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer               id;
	private String                altersklasse;
	private String                gruppengeschlecht;
	@OneToMany(fetch = FetchType.EAGER)
	@JoinTable(
		name = "gewichtsklassengruppen_wettkaempfer",
		joinColumns = @JoinColumn(name = "gewichtsklassengruppe_id"),
		inverseJoinColumns = @JoinColumn(name = "wettkaempfer_id")
	)
	private List<WettkaempferJpa> teilnehmer;
	private String                name;
	private Double                mingewicht;
	private Double                maxgewicht;
}
