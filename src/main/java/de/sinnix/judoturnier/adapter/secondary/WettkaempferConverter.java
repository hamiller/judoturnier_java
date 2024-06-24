package de.sinnix.judoturnier.adapter.secondary;

import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Farbe;
import de.sinnix.judoturnier.model.Geschlecht;
import de.sinnix.judoturnier.model.Wettkaempfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class WettkaempferConverter {

	@Autowired
	private VereinConverter vereinConverter;

	public WettkaempferJpa convertFromWettkaempfer(Wettkaempfer wettkaempfer) {
		WettkaempferJpa jpa = new WettkaempferJpa();
		if (wettkaempfer.id() != null) jpa.setId(wettkaempfer.id());
		jpa.setName(wettkaempfer.name());
		jpa.setGeschlecht(wettkaempfer.geschlecht().name());
		jpa.setAltersklasse(wettkaempfer.altersklasse().name());
		jpa.setVerein(vereinConverter.convertFromVerein(wettkaempfer.verein()));
		jpa.setGewicht(wettkaempfer.gewicht());
		jpa.setFarbe(wettkaempfer.farbe().map(f -> f.name()).orElse(null));
		jpa.setChecked(wettkaempfer.checked());
		jpa.setPrinted(wettkaempfer.printed());
		return jpa;
	}

	public Wettkaempfer convertToWettkaempfer(WettkaempferJpa jpa) {
		Wettkaempfer wettkaempfer = new Wettkaempfer(
			jpa.getId(),
			jpa.getName(),
			getGeschlecht(jpa.getGeschlecht()),
			getAltersklasse(jpa.getAltersklasse()),
			vereinConverter.converToVerein(jpa.getVerein()),
			jpa.getGewicht(),
			getFarbe(jpa.getFarbe()),
			jpa.getChecked(),
			jpa.getPrinted()
		);
		return wettkaempfer;
	}

	public List<WettkaempferJpa> convertFromWettkaempferList(List<Wettkaempfer> teilnehmerList) {
		return teilnehmerList.stream().map(teilnehmer -> this.convertFromWettkaempfer(teilnehmer)).toList();
	}

	public List<Wettkaempfer> convertToWettkaempferList(List<WettkaempferJpa> teilnehmerJpaList) {
		return teilnehmerJpaList.stream().map(jpa -> this.convertToWettkaempfer(jpa)).toList();
	}

	private static Optional<Farbe> getFarbe(String farbe) {
		return (farbe != null && !farbe.isBlank()) ? Optional.of(Farbe.valueOf(farbe)) : Optional.empty();
	}

	private static Altersklasse getAltersklasse(String altersklasse) {
		return (altersklasse != null && !altersklasse.isBlank()) ? Altersklasse.valueOf(altersklasse) : null;
	}

	private static Geschlecht getGeschlecht(String geschlecht) {
		return (geschlecht != null && !geschlecht.isBlank()) ? Geschlecht.valueOf(geschlecht) : null;
	}

}
