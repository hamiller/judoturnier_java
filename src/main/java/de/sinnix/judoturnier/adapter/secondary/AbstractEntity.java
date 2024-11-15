package de.sinnix.judoturnier.adapter.secondary;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@NoArgsConstructor
@AllArgsConstructor
@Data
@MappedSuperclass
public abstract class AbstractEntity {
	@Id
	@GeneratedValue(generator = "uuid-generator")
	@GenericGenerator(name = "uuid-generator", strategy = "de.sinnix.judoturnier.adapter.secondary.UuidGenerator")
	private String uuid;
}
