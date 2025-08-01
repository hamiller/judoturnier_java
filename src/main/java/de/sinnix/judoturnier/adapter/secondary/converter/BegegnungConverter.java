package de.sinnix.judoturnier.adapter.secondary.converter;

import de.sinnix.judoturnier.adapter.secondary.jpa.BegegnungJpa;
import de.sinnix.judoturnier.adapter.secondary.jpa.WettkampfGruppeJpa;
import de.sinnix.judoturnier.model.Begegnung;
import de.sinnix.judoturnier.model.WettkampfGruppe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class BegegnungConverter {

	@Autowired
	private WettkaempferConverter    wettkaempferConverter;
	@Autowired
	private WertungConverter         wertungConverter;
	@Autowired
	private WettkampfGruppeConverter wettkampfGruppeConverter;

	public Begegnung convertToBegegnung(BegegnungJpa jpa, List<WettkampfGruppeJpa> wettkampfGruppeJpaList) {
		Begegnung.BegegnungId begegnungId = null;
		if (jpa.getRundenTyp() != null && jpa.getRunde() != null && jpa.getPaarung() != null) {
			begegnungId = new Begegnung.BegegnungId(Begegnung.RundenTyp.fromValue(jpa.getRundenTyp()), jpa.getRunde(), jpa.getPaarung());
		}

		WettkampfGruppe wettkampfGruppe = null;
		if (!wettkampfGruppeJpaList.isEmpty()) {
			WettkampfGruppeJpa wettkampfGruppeJpa = wettkampfGruppeJpaList.stream().filter(wkg -> wkg.getId().equals(jpa.getWettkampfGruppeId())).findFirst().orElseThrow();
			wettkampfGruppe = wettkampfGruppeConverter.convertToWettkampfGruppe(wettkampfGruppeJpa);
		}
		return new Begegnung(jpa.getId(),
			begegnungId,
			UUID.fromString(jpa.getRundeUUID()),
			jpa.getMatteId(),
			jpa.getMattenRunde(),
			jpa.getGruppenRunde(),
			jpa.getGesamtBegegnung(),
			Optional.ofNullable(wettkaempferConverter.convertToWettkaempfer(jpa.getWettkaempfer1())),
			Optional.ofNullable(wettkaempferConverter.convertToWettkaempfer(jpa.getWettkaempfer2())),
			jpa.getWertungen().stream().map(wertung -> wertungConverter.convertToWertung(wertung)).collect(Collectors.toList()),
			wettkampfGruppe,
			jpa.getTurnierUUID()
			);
	}

	public Begegnung convertToBegegnung(BegegnungJpa jpa, Optional<WettkampfGruppeJpa> wettkampfGruppeJpa) {
		Begegnung.BegegnungId begegnungId = null;
		if (jpa.getRundenTyp() != null && jpa.getRunde() != null && jpa.getPaarung() != null) {
			begegnungId = new Begegnung.BegegnungId(Begegnung.RundenTyp.fromValue(jpa.getRundenTyp()), jpa.getRunde(), jpa.getPaarung());
		}

		WettkampfGruppe wettkampfGruppe = null;
		if (wettkampfGruppeJpa.isPresent()) {
			wettkampfGruppe = wettkampfGruppeConverter.convertToWettkampfGruppe(wettkampfGruppeJpa.get());
		}
		return new Begegnung(jpa.getId(),
			begegnungId,
			UUID.fromString(jpa.getRundeUUID()),
			jpa.getMatteId(),
			jpa.getMattenRunde(),
			jpa.getGruppenRunde(),
			jpa.getGesamtBegegnung(),
			Optional.ofNullable(wettkaempferConverter.convertToWettkaempfer(jpa.getWettkaempfer1())),
			Optional.ofNullable(wettkaempferConverter.convertToWettkaempfer(jpa.getWettkaempfer2())),
			jpa.getWertungen().stream().map(wertung -> wertungConverter.convertToWertung(wertung)).collect(Collectors.toList()),
			wettkampfGruppe,
			jpa.getTurnierUUID()
		);
	}

	public BegegnungJpa convertFromBegegnung(Begegnung begegnung) {
		BegegnungJpa jpa = new BegegnungJpa();
		jpa.setRunde(begegnung.getBegegnungId().getRundenNummerDesTyps());
		jpa.setRundenTyp(begegnung.getBegegnungId().getRundenTyp().getValue());
		jpa.setPaarung(begegnung.getBegegnungId().getPaarungNummer());
		jpa.setRundeUUID(fromUUID(begegnung.getRundeId()));
		jpa.setMatteId(begegnung.getMatteId());
		jpa.setMattenRunde(begegnung.getMattenRunde());
		jpa.setGruppenRunde(begegnung.getGruppenRunde());
		jpa.setGesamtBegegnung(begegnung.getGesamtBegegnung());
		jpa.setWettkaempfer1(wettkaempferConverter.convertFromWettkaempfer(begegnung.getWettkaempfer1().orElse(null)));
		jpa.setWettkaempfer2(wettkaempferConverter.convertFromWettkaempfer(begegnung.getWettkaempfer2().orElse(null)));
		jpa.setWertungen(begegnung.getWertungen().stream().map(wertung -> wertungConverter.convertFromWertung(wertung)).toList());
		begegnung.getWertungen().stream().map(wertung -> wertungConverter.convertFromWertung(wertung)).toList();
		if (begegnung.getWettkampfGruppe().id() != null) jpa.setWettkampfGruppeId(begegnung.getWettkampfGruppe().id());
		jpa.setTurnierUUID(begegnung.getTurnierUUID());
		return jpa;
	}

	private String fromUUID(UUID uuid) {
		if (uuid != null) {
			return uuid.toString();
		}

		return UUID.randomUUID().toString();
	}
}
