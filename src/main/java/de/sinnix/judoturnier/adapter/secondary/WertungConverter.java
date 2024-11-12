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
	@Autowired
	private BenutzerConverter     benutzerConverter;

	public Wertung convertToWertung(WertungJpa jpa) {
		if (jpa == null) {
			return null;
		}
		return new Wertung(UUID.fromString(jpa.getUuid()),
			wettkaempferConverter.convertToWettkaempfer(jpa.getSieger()),
			toDuration(jpa.getZeit()),
			jpa.getPunkteWettkaempfer1(),
			jpa.getStrafenWettkaempfer1(),
			jpa.getPunkteWettkaempfer2(),
			jpa.getStrafenWettkaempfer2(),
			jpa.getKampfgeistWettkaempfer1(),
			jpa.getTechnikWettkaempfer1(),
			jpa.getKampfstilWettkaempfer1(),
			jpa.getVielfaltWettkaempfer1(),
			jpa.getKampfgeistWettkaempfer2(),
			jpa.getTechnikWettkaempfer2(),
			jpa.getKampfstilWettkaempfer2(),
			jpa.getVielfaltWettkaempfer2(),
			benutzerConverter.convertToBenutzer(jpa.getBewerter()));
	}

	public WertungJpa convertFromWertung(Wertung wertung) {
		if (wertung == null) {
			return null;
		}
		WertungJpa jpa = new WertungJpa();
		if (wertung.getUuid() != null) jpa.setUuid(wertung.getUuid().toString());

		if (wertung.getSieger() != null) jpa.setSieger(wettkaempferConverter.convertFromWettkaempfer(wertung.getSieger()));

		// Turnier
		jpa.setZeit(fromDuration(wertung.getZeit()));
		jpa.setPunkteWettkaempfer1(wertung.getPunkteWettkaempferWeiss());
		jpa.setStrafenWettkaempfer1(wertung.getStrafenWettkaempferWeiss());
		jpa.setPunkteWettkaempfer2(wertung.getPunkteWettkaempferRot());
		jpa.setStrafenWettkaempfer2(wertung.getStrafenWettkaempferRot());

		// Randori
		jpa.setKampfgeistWettkaempfer1(wertung.getKampfgeistWettkaempfer1());
		jpa.setTechnikWettkaempfer1(wertung.getTechnikWettkaempfer1());
		jpa.setKampfstilWettkaempfer1(wertung.getKampfstilWettkaempfer1());
		jpa.setVielfaltWettkaempfer1(wertung.getVielfaltWettkaempfer1());
		jpa.setKampfgeistWettkaempfer2(wertung.getKampfgeistWettkaempfer2());
		jpa.setTechnikWettkaempfer2(wertung.getTechnikWettkaempfer2());
		jpa.setKampfstilWettkaempfer2(wertung.getKampfstilWettkaempfer2());
		jpa.setVielfaltWettkaempfer2(wertung.getVielfaltWettkaempfer2());

		jpa.setBewerter(benutzerConverter.convertFromBenutzer(wertung.getBewerter()));

		return jpa;
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
