package de.sinnix.judoturnier.adapter.secondary;

import de.sinnix.judoturnier.model.Begegnung;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
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
		Begegnung.BegegnungId begegnungId = null;
		if (jpa.getRundenTyp() != null && jpa.getRunde() != null && jpa.getPaarung() != null) {
			begegnungId = new Begegnung.BegegnungId(Begegnung.RundenTyp.fromValue(jpa.getRundenTyp()), jpa.getRunde(), jpa.getPaarung());
		}

		WettkampfGruppeJpa wettkampfGruppeJpa = wettkampfGruppeJpaList.stream().filter(wkg -> wkg.getUuid().equals(jpa.getWettkampfGruppeId())).findFirst().orElseThrow();
		return new Begegnung(UUID.fromString(jpa.getUuid()),
			begegnungId,
			UUID.fromString(jpa.getRundeUUID()),
			jpa.getMatteId(),
			jpa.getMattenRunde(),
			jpa.getGruppenRunde(),
			jpa.getGesamtRunde(),
			Optional.ofNullable(wettkaempferConverter.convertToWettkaempfer(jpa.getWettkaempfer1())),
			Optional.ofNullable(wettkaempferConverter.convertToWettkaempfer(jpa.getWettkaempfer2())),
			jpa.getWertungen().stream().map(wertung -> wertungConverter.convertToWertung(wertung)).collect(Collectors.toList()),
			wettkampfGruppeConverter.convertToWettkampfGruppe(wettkampfGruppeJpa),
			UUID.fromString(jpa.getTurnierUUID())
			);
	}

	public BegegnungJpa convertFromBegegnung(Begegnung begegnung) {
		BegegnungJpa jpa = new BegegnungJpa();
		if (begegnung.getId() != null) jpa.setUuid(begegnung.getId().toString());
		jpa.setRunde(begegnung.getBegegnungId().getRunde());
		jpa.setRundenTyp(begegnung.getBegegnungId().getRundenTyp().getValue());
		jpa.setPaarung(begegnung.getBegegnungId().getAkuellePaarung());
		jpa.setRundeUUID(fromUUID(begegnung.getRundeId()));
		jpa.setMatteId(begegnung.getMatteId());
		jpa.setMattenRunde(begegnung.getMattenRunde());
		jpa.setGruppenRunde(begegnung.getGruppenRunde());
		jpa.setGesamtRunde(begegnung.getGesamtRunde());
		jpa.setWettkaempfer1(wettkaempferConverter.convertFromWettkaempfer(begegnung.getWettkaempfer1().orElse(null)));
		jpa.setWettkaempfer2(wettkaempferConverter.convertFromWettkaempfer(begegnung.getWettkaempfer2().orElse(null)));
		jpa.setWertungen(begegnung.getWertungen().stream().map(wertung -> wertungConverter.convertFromWertung(wertung)).toList());
		begegnung.getWertungen().stream().map(wertung -> wertungConverter.convertFromWertung(wertung)).toList();
		if (begegnung.getWettkampfGruppe().id() != null) jpa.setWettkampfGruppeId(begegnung.getWettkampfGruppe().id().toString());
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
