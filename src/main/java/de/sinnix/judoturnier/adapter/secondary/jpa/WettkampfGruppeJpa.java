package de.sinnix.judoturnier.adapter.secondary.jpa;

import java.util.UUID;


import de.sinnix.judoturnier.model.WettkampfGruppe;
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
	private UUID   turnierUUID;

	public WettkampfGruppeJpa(String name, String typ, String altersklasse, UUID turnierUUID) {
		super();
		this.name = name;
		this.typ = typ;
		this.altersklasse = altersklasse;
		this.turnierUUID = turnierUUID;
	}

	public void updateFrom(WettkampfGruppe wettkampfGruppe) {
		this.setName(wettkampfGruppe.name());
		this.setTyp(wettkampfGruppe.typ());
		this.setAltersklasse(wettkampfGruppe.altersklasse().name());
	}
}
