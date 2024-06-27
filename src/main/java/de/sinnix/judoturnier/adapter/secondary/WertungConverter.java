package de.sinnix.judoturnier.adapter.secondary;

import de.sinnix.judoturnier.model.Wertung;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Component
public class WertungConverter {
	@Autowired
	private WettkaempferConverter wettkaempferConverter;

	public Wertung convertToWertung(WertungJpa jpa) {
		if (jpa == null) {
			return null;
		}
		return new Wertung(jpa.getUuid().toString(),
			wettkaempferConverter.convertToWettkaempfer(jpa.getSieger()),
			toDuration(jpa.getZeit()),
			jpa.getPunkteWettkaempfer1(),
			jpa.getStrafenWettkaempfer1(),
			jpa.getPunkteWettkaempfer2(),
			jpa.getStrafenWettkaempfer2(),
			jpa.getKampfgeistWettkaempfer1(),
			jpa.getTechnikWettkaempfer1(),
			jpa.getKampfstilWettkaempfer1(),
			jpa.getFairnessWettkaempfer1(),
			jpa.getKampfgeistWettkaempfer2(),
			jpa.getTechnikWettkaempfer2(),
			jpa.getKampfstilWettkaempfer2(),
			jpa.getFairnessWettkaempfer2());
	}

	public WertungJpa convertFromWertung(Wertung wertung) {
		if (wertung == null) {
			return null;
		}
		WertungJpa jpa = new WertungJpa();
		jpa.setUuid(toUUID(wertung.uuid()));

		if (wertung.sieger() != null) jpa.setSieger(wettkaempferConverter.convertFromWettkaempfer(wertung.sieger()));

		// Turnier
		jpa.setZeit(fromDuration(wertung.zeit()));
		jpa.setPunkteWettkaempfer1(wertung.punkteWettkaempferWeiss());
		jpa.setStrafenWettkaempfer1(wertung.strafenWettkaempferWeiss());
		jpa.setPunkteWettkaempfer2(wertung.punkteWettkaempferRot());
		jpa.setStrafenWettkaempfer2(wertung.strafenWettkaempferRot());

		// Randori
		jpa.setKampfgeistWettkaempfer1(wertung.kampfgeistWettkaempfer1());
		jpa.setTechnikWettkaempfer1(wertung.technikWettkaempfer1());
		jpa.setKampfstilWettkaempfer1(wertung.kampfstilWettkaempfer1());
		jpa.setFairnessWettkaempfer1(wertung.fairnessWettkaempfer1());
		jpa.setKampfgeistWettkaempfer2(wertung.kampfgeistWettkaempfer2());
		jpa.setTechnikWettkaempfer2(wertung.technikWettkaempfer2());
		jpa.setKampfstilWettkaempfer2(wertung.kampfstilWettkaempfer2());
		jpa.setFairnessWettkaempfer2(wertung.fairnessWettkaempfer2());
		return jpa;
	}

	private UUID toUUID(String uuid) {
		if (uuid != null && !uuid.isBlank()) {
			return UUID.fromString(uuid);
		}

		return UUID.randomUUID();
	}

	private Duration toDuration(Long duration) {
		if (duration != null) {
			return Duration.of(duration, ChronoUnit.NANOS);
		}
		return null;
	}

	private Long fromDuration(Duration duration) {
		if (duration == null) return 0l;

		return duration.toNanos();
	}
}
