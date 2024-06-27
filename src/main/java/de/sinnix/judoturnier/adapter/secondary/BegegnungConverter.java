package de.sinnix.judoturnier.adapter.secondary;

import de.sinnix.judoturnier.model.Begegnung;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BegegnungConverter {

	@Autowired
	private WettkaempferConverter wettkaempferConverter;
	@Autowired
	private WertungConverter wertungConverter;

	public Begegnung convertToBegegnung(BegegnungJpa jpa) {
		return new Begegnung(jpa.getId(),
			jpa.getMatteId(),
			jpa.getMattenRunde(),
			jpa.getGruppenRunde(),
			wettkaempferConverter.convertToWettkaempfer(jpa.getWettkaempfer1()),
			wettkaempferConverter.convertToWettkaempfer(jpa.getWettkaempfer2()),
			wertungConverter.convertToWertung(jpa.getWertung())
			);
	}
}
