package de.sinnix.judoturnier.adapter.secondary;

import de.sinnix.judoturnier.model.Begegnung;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
			jpa.getRundeUUID() != null ? UUID.fromString(jpa.getRundeUUID()) : null,
			jpa.getMatteId(),
			jpa.getMattenRunde(),
			jpa.getGruppenRunde(),
			jpa.getGesamtRunde(),
			wettkaempferConverter.convertToWettkaempfer(jpa.getWettkaempfer1()),
			wettkaempferConverter.convertToWettkaempfer(jpa.getWettkaempfer2()),
			jpa.getWertungen().stream().map(wertung -> wertungConverter.convertToWertung(wertung)).collect(Collectors.toList()),
			wettkampfGruppeConverter.convertToWettkampfGruppe(jpa.getWettkampfGruppeId(), wettkampfGruppeJpaList),
			UUID.fromString(jpa.getTurnierUUID())
			);
	}

	public BegegnungJpa convertFromBegegnung(Begegnung begegnung) {
		BegegnungJpa jpa = new BegegnungJpa();
		jpa.setId(begegnung.getBegegnungId());
		jpa.setRundeUUID(fromUUID(begegnung.getRundeId()));
		jpa.setMatteId(begegnung.getMatteId());
		jpa.setMattenRunde(begegnung.getMattenRunde());
		jpa.setGruppenRunde(begegnung.getGruppenRunde());
		jpa.setGesamtRunde(begegnung.getGesamtRunde());
		jpa.setWettkaempfer1(wettkaempferConverter.convertFromWettkaempfer(begegnung.getWettkaempfer1()));
		jpa.setWettkaempfer2(wettkaempferConverter.convertFromWettkaempfer(begegnung.getWettkaempfer2()));
		jpa.setWertungen(begegnung.getWertungen().stream().map(wertung -> wertungConverter.convertFromWertung(wertung)).toList());
		begegnung.getWertungen().stream().map(wertung -> wertungConverter.convertFromWertung(wertung)).toList();
		jpa.setWettkampfGruppeId(begegnung.getWettkampfGruppe().id());
		jpa.setTurnierUUID(begegnung.getTurnierUUID().toString());
		return jpa;
	}

	private String fromUUID(UUID uuid) {
		if (uuid != null) {
			return uuid.toString();
		}

		return UUID.randomUUID().toString();
	}
}
