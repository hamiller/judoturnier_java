package de.sinnix.judoturnier.adapter.secondary;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
@Setter
@Entity
@Table(name = "begegnungen")
public class BegegnungJpa {
	@Id
	Integer         id;
	Integer         matteId;
	Integer         mattenRunde;
	Integer         gruppenRunde;
	@OneToOne
	@JoinColumn(name = "wettkaempfer1")
	WettkaempferJpa wettkaempfer1;
	@OneToOne
	@JoinColumn(name = "wettkaempfer2")
	WettkaempferJpa wettkaempfer2;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "wertung")
	WertungJpa      wertung;
}