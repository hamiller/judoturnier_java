package de.sinnix.judoturnier.adapter.secondary;

import de.sinnix.judoturnier.model.Begegnung;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BegegnungConverter {

	@Autowired
	private WettkaempferConverter wettkaempferConverter;
	@Autowired
	private WertungConverter wertungConverter;
	@Autowired
	private WettkampfGruppeConverter wettkampfGruppeConverter;

	public Begegnung convertToBegegnung(BegegnungJpa jpa, List<WettkampfGruppeJpa> wettkampfGruppeJpaList) {
		return new Begegnung(jpa.getId(),
			jpa.getMatteId(),
			jpa.getMattenRunde(),
			jpa.getGruppenRunde(),
			wettkaempferConverter.convertToWettkaempfer(jpa.getWettkaempfer1()),
			wettkaempferConverter.convertToWettkaempfer(jpa.getWettkaempfer2()),
			wertungConverter.convertToWertung(jpa.getWertung()),
			wettkampfGruppeConverter.convertToWettkampfGruppe(jpa.getWettkampfGruppeId(), wettkampfGruppeJpaList)
			);
	}
}
