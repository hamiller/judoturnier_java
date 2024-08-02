package de.sinnix.judoturnier.adapter.secondary;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "einstellungen", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"art", "turnier_uuid"})
})
public class EinstellungJpa {
	@Id
	private String art;
	private String wert;
	@Column(name = "turnier_uuid", nullable = false)
	private String turnierUUID;
}
