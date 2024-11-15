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
@Table(name = "verein")
public class VereinJpa extends AbstractEntity {
	private String name;
	@Column(name = "turnier_uuid", nullable = false)
	private String turnierUUID;

	public VereinJpa(String uuid, String name, String turnierUUID) {
		super(uuid);
		this.name = name;
		this.turnierUUID = turnierUUID;
	}
}
