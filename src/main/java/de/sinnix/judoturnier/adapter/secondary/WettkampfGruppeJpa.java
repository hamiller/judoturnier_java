package de.sinnix.judoturnier.adapter.secondary;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "wettkampfgruppe", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"id", "turnier_uuid"})
})
public class WettkampfGruppeJpa {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;
	String name;
	String typ;
	String altersklasse;
	@Column(name = "turnier_uuid", nullable = false)
	private String turnierUUID;
}
