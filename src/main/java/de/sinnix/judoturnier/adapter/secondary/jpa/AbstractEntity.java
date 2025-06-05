package de.sinnix.judoturnier.adapter.secondary.jpa;

import java.util.UUID;


import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.Data;

@Data
@MappedSuperclass
public abstract class AbstractEntity {
	@Id
	@GeneratedValue (strategy = GenerationType.UUID)
	private UUID id;

	@Version
	private Long version = 0L;
}
