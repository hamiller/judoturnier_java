package de.sinnix.judoturnier.adapter.secondary;

import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Farbe;
import de.sinnix.judoturnier.model.Geschlecht;
import de.sinnix.judoturnier.model.Wettkaempfer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class WettkaempferConverter {

	private static final Logger          logger = LogManager.getLogger(WettkaempferConverter.class);

	@Autowired
	private              VereinConverter vereinConverter;

	public WettkaempferJpa convertFromWettkaempfer(Wettkaempfer wettkaempfer) {
		if (wettkaempfer == null) {
			logger.trace("Kein Wettkaempfer zum convertieren erhalten!");
			return null;
		}
		WettkaempferJpa jpa = new WettkaempferJpa();
		if (wettkaempfer.id() != null) jpa.setUuid(wettkaempfer.id().toString());
		jpa.setName(wettkaempfer.name());
		if (wettkaempfer.geschlecht() != null) jpa.setGeschlecht(wettkaempfer.geschlecht().name());
		if (wettkaempfer.altersklasse() != null) jpa.setAltersklasse(wettkaempfer.altersklasse().name());
		jpa.setVerein(vereinConverter.convertFromVerein(wettkaempfer.verein()));
		jpa.setGewicht(wettkaempfer.gewicht());
		jpa.setFarbe(wettkaempfer.farbe().map(f -> f.name()).orElse(null));
		jpa.setChecked(wettkaempfer.checked());
		jpa.setPrinted(wettkaempfer.printed());
		jpa.setTurnierUUID(wettkaempfer.turnierUUID().toString());
		return jpa;
	}

	public Wettkaempfer convertToWettkaempfer(WettkaempferJpa jpa) {
		if (jpa == null) {
			logger.trace("Kein WettkaempferJpa zum convertieren erhalten!");
			return null;
		}
		Wettkaempfer wettkaempfer = new Wettkaempfer(
			UUID.fromString(jpa.getUuid()),
			jpa.getName(),
			getGeschlecht(jpa.getGeschlecht()),
			getAltersklasse(jpa.getAltersklasse()),
			vereinConverter.converToVerein(jpa.getVerein()),
			jpa.getGewicht(),
			getFarbe(jpa.getFarbe()),
			jpa.getChecked(),
			jpa.getPrinted(),
			UUID.fromString(jpa.getTurnierUUID())
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
