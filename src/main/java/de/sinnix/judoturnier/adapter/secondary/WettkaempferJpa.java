package de.sinnix.judoturnier.adapter.secondary;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "wettkaempfer")
public class WettkaempferJpa {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String    uuid;
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
}
