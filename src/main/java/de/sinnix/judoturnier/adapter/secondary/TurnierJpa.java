package de.sinnix.judoturnier.adapter.secondary;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "turnier")
public class TurnierJpa {
	@Id
	private String uuid;
	private String name;
	private String ort;
	private Date   datum;
}
