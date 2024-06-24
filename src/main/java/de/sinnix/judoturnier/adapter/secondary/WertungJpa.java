package de.sinnix.judoturnier.adapter.secondary;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.Duration;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
@Setter
@Entity
@Table(name = "wertung")
public class WertungJpa {
	@Id
	Integer id;

	@OneToOne
	WettkaempferJpa wettkaempfer1;
	@OneToOne
	WettkaempferJpa wettkaempfer2;

	// turnier
	@OneToOne
	WettkaempferJpa sieger;

	Long zeit;
	@Column(name = "punktewettkaempfer1")
	Integer punkteWettkaempfer1;
	@Column(name = "strafenwettkaempfer1")
	Integer strafenWettkaempfer1;
	@Column(name = "punktewettkaempfer2")
	Integer punkteWettkaempfer2;
	@Column(name = "strafenwettkaempfer2")
	Integer strafenWettkaempfer2;

	// randori
	Integer kampfgeistWettkaempfer1;
	Integer technikWettkaempfer1;
	Integer kampfstilWettkaempfer1;
	Integer fairnessWettkaempfer1;

	Integer kampfgeistWettkaempfer2;
	Integer technikWettkaempfer2;
	Integer kampfstilWettkaempfer2;
	Integer fairnessWettkaempfer2;
}
