package de.sinnix.judoturnier.adapter.primary;

import de.sinnix.judoturnier.model.Begegnung;
import de.sinnix.judoturnier.model.Matte;
import de.sinnix.judoturnier.model.Runde;
import de.sinnix.judoturnier.model.Wettkaempfer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class DtosConverter {

	public static MatteDto convertFromMatte(Matte mat) {
		List<GruppenRundeDto> gruppenRunden = new ArrayList<>();
		gruppenRunden.add(new GruppenRundeDto(new ArrayList<>()));
		int gruppenRundenNummer = 0;

		for (int i = 0; i < mat.runden().size(); i++) {
			UUID aktuelleGruppe = mat.runden().get(i).gruppe().id();
			UUID vorherigeGruppe = i > 0 ? mat.runden().get(i - 1).gruppe().id() : aktuelleGruppe;

			if (!aktuelleGruppe.equals(vorherigeGruppe)) {
				gruppenRunden.add(new GruppenRundeDto(new ArrayList<>()));
				gruppenRundenNummer++;
			}
			RundeDto rundeDto = DtosConverter.convertFromRunde(mat.runden().get(i));
			gruppenRunden.get(gruppenRundenNummer).runde().add(rundeDto);
		}

		return new MatteDto(mat.id(), gruppenRunden);
	}

	public static BegegnungDto convertFromBegegnung(Begegnung begegnung, String userid, Optional<UUID> vorherigeBegegnungId, Optional<UUID> nachfolgendeBegegnungId) {
		var begegnungId = begegnung.getId();
		var	wettkaempfer1 = begegnung.getWettkaempfer1().orElseGet(() -> Wettkaempfer.Freilos());
		var	wettkaempfer2 = begegnung.getWettkaempfer2().orElseGet(() -> Wettkaempfer.Freilos());
		var kampfrichterWertung = begegnung.getWertungen().stream().filter(w -> w.getBewerter().id().equals(userid)).findFirst().map(w -> new WertungDto(w.getSieger(),
			formatDuration(w.getZeit()),
			w.getPunkteWettkaempferWeiss(),
			w.getStrafenWettkaempferWeiss(),
			w.getPunkteWettkaempferRot(),
			w.getStrafenWettkaempferRot(),
			w.getKampfgeistWettkaempfer1(),
			w.getTechnikWettkaempfer1(),
			w.getKampfstilWettkaempfer1(),
			w.getVielfaltWettkaempfer1(),
			w.getKampfgeistWettkaempfer2(),
			w.getTechnikWettkaempfer2(),
			w.getKampfstilWettkaempfer2(),
			w.getVielfaltWettkaempfer2(),
			w.getBewerter()));
		var vorher = vorherigeBegegnungId.map(id -> String.valueOf(id)).orElseGet(() -> "");
		var nachher = nachfolgendeBegegnungId.map(id -> String.valueOf(id)).orElseGet(() -> "");
		return new BegegnungDto(begegnungId.toString(), begegnung.getBegegnungId().getRundenTyp(), begegnung.getBegegnungId().runde, begegnung.getBegegnungId().akuellePaarung, wettkaempfer1, wettkaempfer2, kampfrichterWertung, begegnung.getWertungen(), vorher, nachher);
	}

	public static BegegnungDto convertFromBegegnung(Begegnung begegnung) {
		var begegnungId = begegnung.getId();
		var	wettkaempfer1 = begegnung.getWettkaempfer1().orElseGet(() -> Wettkaempfer.Freilos());
		var	wettkaempfer2 = begegnung.getWettkaempfer2().orElseGet(() -> Wettkaempfer.Freilos());
		return new BegegnungDto(begegnungId.toString(), begegnung.getBegegnungId().getRundenTyp(), begegnung.getBegegnungId().runde, begegnung.getBegegnungId().akuellePaarung, wettkaempfer1, wettkaempfer2, null, begegnung.getWertungen(), null, null);
	}

	public static String formatDuration(Duration duration) {
		if (duration == null) {
			return "";
		}

		long totalMillis = duration.toMillis();

		// Extrahiere Minuten, Sekunden und Millisekunden
		long minutes = totalMillis / (60 * 1000);
		long seconds = (totalMillis % (60 * 1000)) / 1000;
		long millis = (totalMillis % 1000) / 10; // Wir verwenden nur zwei Stellen für Millisekunden

		// Formatiere den String als "mm:ss.SS"
		return String.format("%02d:%02d.%02d", minutes, seconds, millis);
	}

	public static RundeDto convertFromRunde(Runde runde) {
		var begegnungDtoList = convertFromBegegnungen(runde.begegnungen());
		return new RundeDto(runde.rundeId(), runde.mattenRunde(), runde.gruppenRunde(), runde.rundeGesamt(), runde.matteId(), runde.altersklasse(), runde.gruppe(), begegnungDtoList);
	}

	public static List<BegegnungDto> convertFromBegegnungen(List<Begegnung> begegnungen) {
		return begegnungen.stream()
			.map(b -> convertFromBegegnung(b))
			.collect(Collectors.toList());
	}
}
