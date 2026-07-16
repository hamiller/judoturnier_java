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
	private Integer         ipponWettkaempfer1;
	private Integer         wazariWettkaempfer1;
	private Integer         yukoWettkaempfer1;
	private Integer         shidoWettkaempfer1;
	private Boolean         hansokuMakeWettkaempfer1;
	private Integer         ipponWettkaempfer2;
	private Integer         wazariWettkaempfer2;
	private Integer         yukoWettkaempfer2;
	private Integer         shidoWettkaempfer2;
	private Boolean         hansokuMakeWettkaempfer2;

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

	public WertungJpa(Long zeit, WettkaempferJpa sieger, Integer ipponWettkaempfer1, Integer wazariWettkaempfer1, Integer yukoWettkaempfer1, Integer shidoWettkaempfer1, Boolean hansokuMakeWettkaempfer1, Integer ipponWettkaempfer2, Integer wazariWettkaempfer2, Integer yukoWettkaempfer2, Integer shidoWettkaempfer2, Boolean hansokuMakeWettkaempfer2, Integer kampfgeistWettkaempfer1, Integer technikWettkaempfer1, Integer kampfstilWettkaempfer1, Integer vielfaltWettkaempfer1, Integer kampfgeistWettkaempfer2, Integer technikWettkaempfer2, Integer kampfstilWettkaempfer2, Integer vielfaltWettkaempfer2, BenutzerJpa benutzer) {
		super();
		this.zeit = zeit;
		this.sieger = sieger;
		this.ipponWettkaempfer1 = ipponWettkaempfer1;
		this.wazariWettkaempfer1 = wazariWettkaempfer1;
		this.yukoWettkaempfer1 = yukoWettkaempfer1;
		this.shidoWettkaempfer1 = shidoWettkaempfer1;
		this.hansokuMakeWettkaempfer1 = hansokuMakeWettkaempfer1;
		this.ipponWettkaempfer2 = ipponWettkaempfer2;
		this.wazariWettkaempfer2 = wazariWettkaempfer2;
		this.yukoWettkaempfer2 = yukoWettkaempfer2;
		this.shidoWettkaempfer2 = shidoWettkaempfer2;
		this.hansokuMakeWettkaempfer2 = hansokuMakeWettkaempfer2;
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
		this.ipponWettkaempfer1 = jpa.getIpponWettkaempfer1();
		this.wazariWettkaempfer1 = jpa.getWazariWettkaempfer1();
		this.yukoWettkaempfer1 = jpa.getYukoWettkaempfer1();
		this.shidoWettkaempfer1 = jpa.getShidoWettkaempfer1();
		this.hansokuMakeWettkaempfer1 = jpa.getHansokuMakeWettkaempfer1();
		this.ipponWettkaempfer2 = jpa.getIpponWettkaempfer2();
		this.wazariWettkaempfer2 = jpa.getWazariWettkaempfer2();
		this.yukoWettkaempfer2 = jpa.getYukoWettkaempfer2();
		this.shidoWettkaempfer2 = jpa.getShidoWettkaempfer2();
		this.hansokuMakeWettkaempfer2 = jpa.getHansokuMakeWettkaempfer2();

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
