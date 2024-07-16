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
	Integer id;
	Integer matteId;
	Integer mattenRunde;
	Integer gruppenRunde;
	Integer gesamtRunde;
	@OneToOne
	@JoinColumn(name = "wettkaempfer1")
	WettkaempferJpa wettkaempfer1;
	@OneToOne
	@JoinColumn(name = "wettkaempfer2")
	WettkaempferJpa  wettkaempfer2;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(
		name = "begegnung_wertung",
		joinColumns = {
			@JoinColumn(name = "begegnung_id", referencedColumnName = "id"),
			@JoinColumn(name = "turnier_uuid", referencedColumnName = "turnier_uuid")
		},
		inverseJoinColumns = @JoinColumn(name = "wertung_id")
	)
	List<WertungJpa> wertungen;
	Integer wettkampfGruppeId;
	@Column(name = "turnier_uuid")
	private String turnierUUID;
}
