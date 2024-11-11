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

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "verein")
public class VereinJpa {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String uuid;
	private String name;
	@Column(name = "turnier_uuid", nullable = false)
	private String turnierUUID;
}
