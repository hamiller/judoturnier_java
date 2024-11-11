package de.sinnix.judoturnier.adapter.secondary;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "wertung")
public class WertungJpa {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String uuid;
	private Long   zeit;

	// turnier
	@OneToOne
	private WettkaempferJpa sieger;
	private Integer         punkteWettkaempfer1;
	private Integer         strafenWettkaempfer1;
	private Integer         punkteWettkaempfer2;
	private Integer         strafenWettkaempfer2;

	// randori
	private Integer kampfgeistWettkaempfer1;
	private Integer technikWettkaempfer1;
	private Integer kampfstilWettkaempfer1;
	private Integer fairnessWettkaempfer1;

	private Integer kampfgeistWettkaempfer2;
	private Integer technikWettkaempfer2;
	private Integer kampfstilWettkaempfer2;
	private Integer fairnessWettkaempfer2;

	private String bewerter;
}
