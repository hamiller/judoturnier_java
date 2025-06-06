package de.sinnix.judoturnier.adapter.secondary.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Date;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@Table(name = "turnier")
public class TurnierJpa extends AbstractEntity {
	private String name;
	private String ort;
	private Date   datum;

	public TurnierJpa(String name, String ort, Date datum) {
		super();
		this.name = name;
		this.ort = ort;
		this.datum = datum;
	}
}
