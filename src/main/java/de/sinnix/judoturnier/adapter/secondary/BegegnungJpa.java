package de.sinnix.judoturnier.adapter.secondary;

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
	private Integer          gesamtRunde;
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
			@JoinColumn(name = "begegnung_id", referencedColumnName = "uuid"),
			@JoinColumn(name = "turnier_uuid", referencedColumnName = "turnier_uuid")
		},
		inverseJoinColumns = @JoinColumn(name = "wertung_id")
	)
	private List<WertungJpa> wertungen;
	private String           wettkampfGruppeId;
	@Column(name = "turnier_uuid", nullable = false)
	private String           turnierUUID;
	private Integer          runde;
	private Integer          rundenTyp;
	private Integer          paarung;

	public BegegnungJpa(String uuid, String rundeUUID, Integer matteId, Integer mattenRunde, Integer gruppenRunde, Integer gesamtRunde, WettkaempferJpa wettkaempfer1, WettkaempferJpa wettkaempfer2, List<WertungJpa> wertungen, String wettkampfGruppeId, String turnierUUID, Integer runde, Integer rundenTyp, Integer paarung) {
		super(uuid);
		this.rundeUUID = rundeUUID;
		this.matteId = matteId;
		this.mattenRunde = mattenRunde;
		this.gruppenRunde = gruppenRunde;
		this.gesamtRunde = gesamtRunde;
		this.wettkaempfer1 = wettkaempfer1;
		this.wettkaempfer2 = wettkaempfer2;
		this.wertungen = wertungen;
		this.wettkampfGruppeId = wettkampfGruppeId;
		this.turnierUUID = turnierUUID;
		this.runde = runde;
		this.rundenTyp = rundenTyp;
		this.paarung = paarung;
	}
}
