package de.sinnix.judoturnier.adapter.secondary.jpa;

import java.util.Optional;


import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
@Table(name = "wertung")
public class WertungJpa extends AbstractEntity {
	private Long   zeit;

	// turnier
	@OneToOne
	@JoinColumn(name = "sieger_uuid")
	private WettkaempferJpa sieger;
	private Integer         punkteWettkaempfer1;
	private Integer         strafenWettkaempfer1;
	private Integer         punkteWettkaempfer2;
	private Integer         strafenWettkaempfer2;

	// randori
	private Integer kampfgeistWettkaempfer1;
	private Integer technikWettkaempfer1;
	private Integer kampfstilWettkaempfer1;
	private Integer vielfaltWettkaempfer1;

	private Integer kampfgeistWettkaempfer2;
	private Integer technikWettkaempfer2;
	private Integer kampfstilWettkaempfer2;
	private Integer vielfaltWettkaempfer2;

	@ManyToOne
	@JoinColumn(name = "bewerter")
	private BenutzerJpa benutzer;

	public WertungJpa(Long zeit, WettkaempferJpa sieger, Integer punkteWettkaempfer1, Integer strafenWettkaempfer1, Integer punkteWettkaempfer2, Integer strafenWettkaempfer2, Integer kampfgeistWettkaempfer1, Integer technikWettkaempfer1, Integer kampfstilWettkaempfer1, Integer vielfaltWettkaempfer1, Integer kampfgeistWettkaempfer2, Integer technikWettkaempfer2, Integer kampfstilWettkaempfer2, Integer vielfaltWettkaempfer2, BenutzerJpa benutzer) {
		super();
		this.zeit = zeit;
		this.sieger = sieger;
		this.punkteWettkaempfer1 = punkteWettkaempfer1;
		this.strafenWettkaempfer1 = strafenWettkaempfer1;
		this.punkteWettkaempfer2 = punkteWettkaempfer2;
		this.strafenWettkaempfer2 = strafenWettkaempfer2;
		this.kampfgeistWettkaempfer1 = kampfgeistWettkaempfer1;
		this.technikWettkaempfer1 = technikWettkaempfer1;
		this.kampfstilWettkaempfer1 = kampfstilWettkaempfer1;
		this.vielfaltWettkaempfer1 = vielfaltWettkaempfer1;
		this.kampfgeistWettkaempfer2 = kampfgeistWettkaempfer2;
		this.technikWettkaempfer2 = technikWettkaempfer2;
		this.kampfstilWettkaempfer2 = kampfstilWettkaempfer2;
		this.vielfaltWettkaempfer2 = vielfaltWettkaempfer2;
		this.benutzer = benutzer;
	}

	public void updateFrom(WertungJpa jpa, Optional<WettkaempferJpa> siegerOptionalJpa) {
		this.zeit = jpa.getZeit();
		this.sieger = siegerOptionalJpa.isPresent() ? siegerOptionalJpa.get() : jpa.getSieger();
		this.punkteWettkaempfer1 = jpa.getPunkteWettkaempfer1();
		this.strafenWettkaempfer1 = jpa.getStrafenWettkaempfer1();
		this.punkteWettkaempfer2 = jpa.getPunkteWettkaempfer2();
		this.strafenWettkaempfer2 = jpa.getStrafenWettkaempfer2();

		this.kampfgeistWettkaempfer1 = jpa.getKampfgeistWettkaempfer1();
		this.technikWettkaempfer1 = jpa.getTechnikWettkaempfer1();
		this.kampfstilWettkaempfer1 = jpa.getKampfstilWettkaempfer1();
		this.vielfaltWettkaempfer1 = jpa.getVielfaltWettkaempfer1();
		this.kampfgeistWettkaempfer2 = jpa.getKampfgeistWettkaempfer2();
		this.technikWettkaempfer2 = jpa.getTechnikWettkaempfer2();
		this.kampfstilWettkaempfer2 = jpa.getKampfstilWettkaempfer2();
		this.vielfaltWettkaempfer2 = jpa.getVielfaltWettkaempfer2();
	}
}
