package de.sinnix.judoturnier.adapter.secondary;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "wettkampfgruppe")
public class WettkampfGruppeJpa {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String uuid;
	private String name;
	private String typ;
	private String altersklasse;
	@Column(name = "turnier_uuid", nullable = false)
	private String turnierUUID;
}
