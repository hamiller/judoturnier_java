package de.sinnix.judoturnier.adapter.secondary.converter;

import de.sinnix.judoturnier.adapter.secondary.jpa.WertungJpa;
import de.sinnix.judoturnier.model.Wertung;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

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
		return new Wertung(jpa.getId(),
			wettkaempferConverter.convertToWettkaempfer(jpa.getSieger()),
			toDuration(jpa.getZeit()),
			jpa.getIpponWettkaempfer1(),
			jpa.getWazariWettkaempfer1(),
			jpa.getYukoWettkaempfer1(),
			jpa.getShidoWettkaempfer1(),
			jpa.getHansokuMakeWettkaempfer1(),
			jpa.getIpponWettkaempfer2(),
			jpa.getWazariWettkaempfer2(),
			jpa.getYukoWettkaempfer2(),
			jpa.getShidoWettkaempfer2(),
			jpa.getHansokuMakeWettkaempfer2(),
			jpa.getKampfgeistWettkaempfer1(),
			jpa.getTechnikWettkaempfer1(),
			jpa.getKampfstilWettkaempfer1(),
			jpa.getVielfaltWettkaempfer1(),
			jpa.getKampfgeistWettkaempfer2(),
			jpa.getTechnikWettkaempfer2(),
			jpa.getKampfstilWettkaempfer2(),
			jpa.getVielfaltWettkaempfer2(),
			benutzerConverter.convertToBenutzer(jpa.getBenutzer()));
	}

	public WertungJpa convertFromWertung(Wertung wertung) {
		if (wertung == null) {
			return null;
		}
		WertungJpa jpa = new WertungJpa();

		if (wertung.getSieger() != null) jpa.setSieger(wettkaempferConverter.convertFromWettkaempfer(wertung.getSieger()));

		// Turnier
		jpa.setZeit(fromDuration(wertung.getZeit()));
		jpa.setIpponWettkaempfer1(wertung.getIpponWettkaempferWeiss());
		jpa.setWazariWettkaempfer1(wertung.getWazariWettkaempferWeiss());
		jpa.setYukoWettkaempfer1(wertung.getYukoWettkaempferWeiss());
		jpa.setShidoWettkaempfer1(wertung.getShidoWettkaempferWeiss());
		jpa.setHansokuMakeWettkaempfer1(wertung.getHansokuMakeWettkaempferWeiss());
		jpa.setIpponWettkaempfer2(wertung.getIpponWettkaempferRot());
		jpa.setWazariWettkaempfer2(wertung.getWazariWettkaempferRot());
		jpa.setYukoWettkaempfer2(wertung.getYukoWettkaempferRot());
		jpa.setShidoWettkaempfer2(wertung.getShidoWettkaempferRot());
		jpa.setHansokuMakeWettkaempfer2(wertung.getHansokuMakeWettkaempferRot());

		// Randori
		jpa.setKampfgeistWettkaempfer1(wertung.getKampfgeistWettkaempfer1());
		jpa.setTechnikWettkaempfer1(wertung.getTechnikWettkaempfer1());
		jpa.setKampfstilWettkaempfer1(wertung.getKampfstilWettkaempfer1());
		jpa.setVielfaltWettkaempfer1(wertung.getVielfaltWettkaempfer1());
		jpa.setKampfgeistWettkaempfer2(wertung.getKampfgeistWettkaempfer2());
		jpa.setTechnikWettkaempfer2(wertung.getTechnikWettkaempfer2());
		jpa.setKampfstilWettkaempfer2(wertung.getKampfstilWettkaempfer2());
		jpa.setVielfaltWettkaempfer2(wertung.getVielfaltWettkaempfer2());

		jpa.setBenutzer(benutzerConverter.convertFromBenutzer(wertung.getBewerter()));

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
