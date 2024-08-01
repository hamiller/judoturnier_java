package de.sinnix.judoturnier.adapter.secondary;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
@Table(name = "begegnungen")
public class BegegnungJpa {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name = "runde_uuid")
	private String  rundeUUID;
	private Integer matteId;
	private Integer mattenRunde;
	private Integer gruppenRunde;
	private Integer gesamtRunde;
	@OneToOne
	@JoinColumn(name = "wettkaempfer1")
	private WettkaempferJpa wettkaempfer1;
	@OneToOne
	@JoinColumn(name = "wettkaempfer2")
	private WettkaempferJpa  wettkaempfer2;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(
		name = "begegnung_wertung",
		joinColumns = {
			@JoinColumn(name = "begegnung_id", referencedColumnName = "id"),
			@JoinColumn(name = "turnier_uuid", referencedColumnName = "turnier_uuid")
		},
		inverseJoinColumns = @JoinColumn(name = "wertung_id")
	)
	private List<WertungJpa> wertungen;
	private Integer wettkampfGruppeId;
	@Column(name = "turnier_uuid")
	private String turnierUUID;
}
