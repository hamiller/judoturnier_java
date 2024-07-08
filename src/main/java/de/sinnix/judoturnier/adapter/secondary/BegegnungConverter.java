package de.sinnix.judoturnier.adapter.secondary;

import de.sinnix.judoturnier.model.Begegnung;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

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
			Optional.ofNullable(wertungConverter.convertToWertung(jpa.getWertung())),
			wettkampfGruppeConverter.convertToWettkampfGruppe(jpa.getWettkampfGruppeId(), wettkampfGruppeJpaList)
			);
	}

	public BegegnungJpa convertFromBegegnung(Begegnung begegnung) {
		BegegnungJpa jpa = new BegegnungJpa();
		jpa.setId(begegnung.getBegegnungId());
		jpa.setMatteId(begegnung.getMatteId());
		jpa.setMattenRunde(begegnung.getMattenRunde());
		jpa.setGruppenRunde(begegnung.getGruppenRunde());
		jpa.setWettkaempfer1(wettkaempferConverter.convertFromWettkaempfer(begegnung.getWettkaempfer1()));
		jpa.setWettkaempfer2(wettkaempferConverter.convertFromWettkaempfer(begegnung.getWettkaempfer2()));
		if (begegnung.getWertung().isPresent()) {
			jpa.setWertung(wertungConverter.convertFromWertung(begegnung.getWertung().get()));
		}
		jpa.setWettkampfGruppeId(begegnung.getWettkampfGruppe().id());
		return jpa;
	}
}
