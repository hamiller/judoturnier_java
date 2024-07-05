package de.sinnix.judoturnier.adapter.secondary;

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
	String uuid;
	Long zeit;

	// turnier
	@OneToOne
	WettkaempferJpa sieger;
	Integer punkteWettkaempfer1;
	Integer strafenWettkaempfer1;
	Integer punkteWettkaempfer2;
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
