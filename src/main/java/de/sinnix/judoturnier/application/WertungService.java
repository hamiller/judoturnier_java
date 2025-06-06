package de.sinnix.judoturnier.application;

import de.sinnix.judoturnier.adapter.secondary.BenutzerRepository;
import de.sinnix.judoturnier.adapter.secondary.TurnierRepository;
import de.sinnix.judoturnier.adapter.secondary.WertungRepository;
import de.sinnix.judoturnier.model.Begegnung;
import de.sinnix.judoturnier.model.Benutzer;
import de.sinnix.judoturnier.model.Wertung;
import de.sinnix.judoturnier.model.Wettkaempfer;
import de.sinnix.judoturnier.model.WettkampfGruppe;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Transactional
@Service
public class WertungService {
	private static final Logger              logger = LogManager.getLogger(WertungService.class);
	@Autowired
	private              BenutzerRepository  benutzerRepository;
	@Autowired
	private              TurnierRepository   turnierRepository;
	@Autowired
	private              WertungRepository   wertungRepository;
	@Autowired
	private              WettkaempferService wettkaempferService;
	@Autowired
	private              WettkampfService    wettkampfService;

	public void speichereRandoriWertung(UUID begegnungId, int kampfgeist1, int technik1, int stil1, int vielfalt1, int kampfgeist2, int technik2, int stil2, int vielfalt2, UUID bewerterUUID) {
		logger.info("speichereRandoriWertung: {}", begegnungId);

		Benutzer benutzer = benutzerRepository.findBenutzer(bewerterUUID).orElseThrow(() -> new RuntimeException("Nutzer " + bewerterUUID + " konnte nicht gefunden werden"));
		Wertung wertung = new Wertung(null, null, null, null, null, null, null,
			kampfgeist1, technik1, stil1, vielfalt1,
			kampfgeist2, technik2, stil2, vielfalt2,
			benutzer
		);
		wertungRepository.speichereWertungInBegegnung(wertung, begegnungId);
	}

	public void speichereTurnierWertung(UUID begegnungId, int scoreWeiss, int scoreBlau, int penaltiesWeiss, int penaltiesBlau, String fightTime, UUID siegerUUID, UUID bewerterUUID) {
		logger.info("speichereTurnierWertung: {}", begegnungId);

		Wettkaempfer wettkaempferSiegerRunde = wettkaempferService.ladeKaempfer(siegerUUID).orElseThrow(() -> new RuntimeException("Wettkaempfer " + siegerUUID + " konnte nicht gefunden werden"));
		Benutzer benutzer = benutzerRepository.findBenutzer(bewerterUUID).orElseThrow(() -> new RuntimeException("Nutzer " + bewerterUUID + " konnte nicht gefunden werden"));
		Wertung wertung = new Wertung(null,
			wettkaempferSiegerRunde,
			parseDuration(fightTime),
			scoreWeiss,
			penaltiesWeiss,
			scoreBlau,
			penaltiesBlau,
			null, null, null, null, null, null, null, null,
			benutzer
		);
		wertungRepository.speichereWertungInBegegnung(wertung, begegnungId);

		aktualisierePaarungen(begegnungId, wettkaempferSiegerRunde);
	}

	// schreibe nachfolgende Begegnungen - falls noch welche kommen (Trostrunde und nächste Runde)
	private void aktualisierePaarungen(UUID begegnungId, Wettkaempfer wettkaempferSiegerRunde) {
		logger.info("updatePairings für Begegnung {}", begegnungId);
		Begegnung begegnung = wettkampfService.ladeBegegnung(begegnungId);

		Begegnung.BegegnungId aktuelleBegegnungId = begegnung.getBegegnungId();
		var wettkampfGruppe = begegnung.getWettkampfGruppe();
		var alleWettkampfgruppeRunden = turnierRepository.ladeWettkampfgruppeRunden(wettkampfGruppe.id(), begegnung.getTurnierUUID());
		Pair<Optional<Begegnung>, Optional<Begegnung>> nachfolger = Sortierer.nachfolgeBegegnungen(aktuelleBegegnungId, wettkampfGruppe, alleWettkampfgruppeRunden);
		Optional<Begegnung> nextGewinnerBegegnungOptional = nachfolger.getLeft();
		Optional<Begegnung> nextTrostBegegnungOptional = nachfolger.getRight();

		if (nextGewinnerBegegnungOptional.isPresent()) {
			Begegnung nextGewinnerRunde = nextGewinnerBegegnungOptional.get();
			logger.warn("nächste GewinnerRunde: {}", nextGewinnerRunde);
			if (nextGewinnerRunde.getWettkaempfer1().isEmpty()) nextGewinnerRunde.setWettkaempfer1(Optional.of(wettkaempferSiegerRunde));
			else if (nextGewinnerRunde.getWettkaempfer2().isEmpty()) nextGewinnerRunde.setWettkaempfer2(Optional.of(wettkaempferSiegerRunde));
			turnierRepository.speichereBegegnung(nextGewinnerRunde);
		}
		if (nextTrostBegegnungOptional.isPresent()) {
			Begegnung nextTrostRunde = nextTrostBegegnungOptional.get();
			logger.warn("nächste TrostRunde: {}", nextTrostRunde);
			Optional<Wettkaempfer> wettkaempferTrostRunde = findeVerlierer(begegnung, wettkaempferSiegerRunde);
			if (wettkaempferTrostRunde.isPresent()) {
				if (nextTrostRunde.getWettkaempfer1().isEmpty()) nextTrostRunde.setWettkaempfer1(Optional.of(wettkaempferTrostRunde.get()));
				else if (nextTrostRunde.getWettkaempfer2().isEmpty()) nextTrostRunde.setWettkaempfer2(Optional.of(wettkaempferTrostRunde.get()));
				turnierRepository.speichereBegegnung(nextTrostRunde);
			}
		}
	}

	public Map<Wettkaempfer, Integer> berechnePlatzierungen(UUID turnierUUID) {
		logger.info("Berechnung platzierung in Turnier {}", turnierUUID);
		Map<Wettkaempfer, Integer> results = new HashMap<>();
		Map<WettkampfGruppe, List<Begegnung>> begegnungenDerWettkampfgruppen = turnierRepository.ladeAlleBegegnungen(turnierUUID).stream()
			.collect(Collectors.groupingBy(Begegnung::getWettkampfGruppe));

		for (Map.Entry<WettkampfGruppe, List<Begegnung>> begegnungenSet : begegnungenDerWettkampfgruppen.entrySet()) {
			logger.debug("berechne Platzierungen in Gruppe {}", begegnungenSet.getKey().id());
			var begegnungen = begegnungenSet.getValue();

			// 1. Siege zählen
			Map<Wettkaempfer, Integer> siege = new LinkedHashMap<>();
			for (Begegnung begegnung : begegnungen) {
				if (begegnung.getWettkaempfer1().isPresent()) siege.putIfAbsent(begegnung.getWettkaempfer1().get(), 0);
				if (begegnung.getWettkaempfer2().isPresent()) siege.putIfAbsent(begegnung.getWettkaempfer2().get(), 0);

				for (Wertung wertung : begegnung.getWertungen()) {
					siege.put(wertung.getSieger(), siege.getOrDefault(wertung.getSieger(), 0) + 1);
				}
			}

			// 2. Platzierungen berechnen
			List<Wettkaempfer> platzierungen = new ArrayList<>(siege.keySet());
			platzierungen.sort((w1, w2) -> siege.get(w2) - siege.get(w1));

			// 3. Platzierungen in die Map eintragen
			for (int i = 0; i < platzierungen.size(); i++) {
				Wettkaempfer wettkaempfer = platzierungen.get(i);
				int platzierung = i + 1;
				results.put(wettkaempfer, platzierung);
			}
		}

		return results;
	}

	private static Duration parseDuration(String input) {
		// Regex für das Parsen von "mm:ss.SS"
		Pattern pattern = Pattern.compile("(\\d{2}):(\\d{2})\\.(\\d{2})");
		Matcher matcher = pattern.matcher(input);

		if (matcher.matches()) {
			int minutes = Integer.parseInt(matcher.group(1));
			int seconds = Integer.parseInt(matcher.group(2));
			int millis = Integer.parseInt(matcher.group(3)) * 10; // 36 bedeutet 360 Millisekunden

			long totalMillis = (long) minutes * 60 * 1000 + seconds * 1000 + millis;
			return Duration.ofMillis(totalMillis);
		} else {
			throw new IllegalArgumentException("Invalid duration format: " + input);
		}
	}

	private Optional<Wettkaempfer> findeVerlierer(Begegnung begegnung, Wettkaempfer sieger) {
		if (begegnung.getWettkaempfer1().isPresent() && sieger.equals(begegnung.getWettkaempfer1())) {
			if (begegnung.getWettkaempfer2().isPresent()) {
				return begegnung.getWettkaempfer2();
			}
			return Optional.empty();
		}

		return begegnung.getWettkaempfer2();
	}
}
