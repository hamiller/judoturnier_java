package de.sinnix.judoturnier.adapter.secondary;

import de.sinnix.judoturnier.model.Begegnung;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
@Table(name = "begegnungen")
public class BegegnungJpa extends AbstractEntity {
	@Column(name = "runde_uuid")
	private String           rundeUUID;
	private Integer          matteId;
	private Integer          mattenRunde;
	private Integer          gruppenRunde;
	private Integer          gesamtBegegnung;
	@OneToOne
	@JoinColumn(name = "wettkaempfer1")
	private WettkaempferJpa  wettkaempfer1;
	@OneToOne
	@JoinColumn(name = "wettkaempfer2")
	private WettkaempferJpa  wettkaempfer2;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(
		name = "begegnung_wertung",
		joinColumns = {
			@JoinColumn(name = "begegnung_id", referencedColumnName = "id", nullable = false),
			@JoinColumn(name = "turnier_uuid", referencedColumnName = "turnier_uuid", nullable = false)
		},
		inverseJoinColumns = @JoinColumn(name = "wertung_id")
	)
	private List<WertungJpa> wertungen;
	private UUID             wettkampfGruppeId;
	@Column(name = "turnier_uuid", nullable = false)
	private UUID             turnierUUID;
	private Integer          runde;
	private Integer          rundenTyp;
	private Integer          paarung;

	public BegegnungJpa(String rundeUUID, Integer matteId, Integer mattenRunde, Integer gruppenRunde, Integer gesamtBegegnung, WettkaempferJpa wettkaempfer1, WettkaempferJpa wettkaempfer2, List<WertungJpa> wertungen, UUID wettkampfGruppeId, UUID turnierUUID, Integer runde, Integer rundenTyp, Integer paarung) {
		super();
		this.rundeUUID = rundeUUID;
		this.matteId = matteId;
		this.mattenRunde = mattenRunde;
		this.gruppenRunde = gruppenRunde;
		this.gesamtBegegnung = gesamtBegegnung;
		this.wettkaempfer1 = wettkaempfer1;
		this.wettkaempfer2 = wettkaempfer2;
		this.wertungen = wertungen;
		this.wettkampfGruppeId = wettkampfGruppeId;
		this.turnierUUID = turnierUUID;
		this.runde = runde;
		this.rundenTyp = rundenTyp;
		this.paarung = paarung;
	}

	public void updateFrom(BegegnungJpa jpa, WettkaempferJpa wettkaempfer1, WettkaempferJpa wettkaempfer2) {
		this.wettkaempfer1 = wettkaempfer1;
		this.wettkaempfer2 = wettkaempfer2;
	}
}
