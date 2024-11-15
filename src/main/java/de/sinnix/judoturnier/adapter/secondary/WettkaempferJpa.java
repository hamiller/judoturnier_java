package de.sinnix.judoturnier.adapter.secondary;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@Table(name = "wettkaempfer")
public class WettkaempferJpa extends AbstractEntity {
	private String    name;
	private String    geschlecht;
	private String    altersklasse;
	@ManyToOne
	@JoinColumn(name = "verein")
	private VereinJpa verein;
	private Double    gewicht;
	private String    farbe;
	private Boolean   checked;
	private Boolean   printed;
	@Column(name = "turnier_uuid", nullable = false)
	private String    turnierUUID;

	public WettkaempferJpa(String uuid, String name, String geschlecht, String altersklasse, VereinJpa verein, Double gewicht, String farbe, Boolean checked, Boolean printed, String turnierUUID) {
		super(uuid);
		this.name = name;
		this.geschlecht = geschlecht;
		this.altersklasse = altersklasse;
		this.verein = verein;
		this.gewicht = gewicht;
		this.farbe = farbe;
		this.checked = checked;
		this.printed = printed;
		this.turnierUUID = turnierUUID;
	}
}
