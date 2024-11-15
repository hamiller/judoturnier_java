package de.sinnix.judoturnier.adapter.secondary;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "wettkampfgruppe")
public class WettkampfGruppeJpa extends AbstractEntity {
	private String name;
	private String typ;
	private String altersklasse;
	@Column(name = "turnier_uuid", nullable = false)
	private String turnierUUID;

	public WettkampfGruppeJpa(String uuid, String name, String typ, String altersklasse, String turnierUUID) {
		super(uuid);
		this.name = name;
		this.typ = typ;
		this.altersklasse = altersklasse;
		this.turnierUUID = turnierUUID;
	}
}
