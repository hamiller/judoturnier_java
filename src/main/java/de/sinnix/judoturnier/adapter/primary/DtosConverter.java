package de.sinnix.judoturnier.adapter.primary;

import de.sinnix.judoturnier.model.Begegnung;
import de.sinnix.judoturnier.model.Benutzer;
import de.sinnix.judoturnier.model.Matte;
import de.sinnix.judoturnier.model.Runde;
import de.sinnix.judoturnier.model.Turnier;
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

	public static BegegnungDto convertFromBegegnung(Begegnung begegnung, UUID userid, Optional<UUID> vorherigeBegegnungId, Optional<UUID> nachfolgendeBegegnungId) {
		var begegnungId = begegnung.getId();
		var	wettkaempfer1 = begegnung.getWettkaempfer1().orElseGet(() -> Wettkaempfer.Freilos());
		var	wettkaempfer2 = begegnung.getWettkaempfer2().orElseGet(() -> Wettkaempfer.Freilos());
		var kampfrichterWertung = begegnung.getWertungen().stream().filter(w -> w.getBewerter().uuid().equals(userid)).findFirst().map(w -> new WertungDto(w.getSieger(),
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

	public static Long formatDuration(Duration duration) {
		if (duration == null) {
			return 0l;
		}

		return duration.toMillis();
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

	public static BenutzerDto convertFromBenutzer(Benutzer b, UUID turnierUuid) {
		return new BenutzerDto(b.uuid().toString(), b.username(), b.name(), b.istAdmin(), b.istKampfrichter(turnierUuid), b.istBeobachter(turnierUuid), b.istAnonym(), b.istTurnierZugeordnet(turnierUuid));
	}

	public static TurnierDto convertFromTurnier(Turnier turnier, List<BenutzerDto> benutzerList) {
		return new TurnierDto(turnier.uuid(), turnier.name(), turnier.ort(), turnier.datum(), benutzerList);
	}
}
