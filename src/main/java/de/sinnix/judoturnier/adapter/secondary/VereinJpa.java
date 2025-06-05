package de.sinnix.judoturnier.adapter.secondary;

import java.util.UUID;


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
	private UUID   turnierUUID;

	public VereinJpa(String name, UUID turnierUUID) {
		super();
		this.name = name;
		this.turnierUUID = turnierUUID;
	}

	public void updateFrom(VereinJpa vereinJpa) {
		this.name = vereinJpa.getName();
	}
}
