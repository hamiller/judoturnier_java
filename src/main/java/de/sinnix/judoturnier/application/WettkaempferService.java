package de.sinnix.judoturnier.application;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import de.sinnix.judoturnier.adapter.secondary.WettkaempferRepository;
import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Begegnung;
import de.sinnix.judoturnier.model.GesamtPlatzierung;
import de.sinnix.judoturnier.model.GesamtWertung;
import de.sinnix.judoturnier.model.Geschlecht;
import de.sinnix.judoturnier.model.Verein;
import de.sinnix.judoturnier.model.Wertung;
import de.sinnix.judoturnier.model.Wettkaempfer;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Stream;

@Transactional
@Service
public class WettkaempferService {
	private static final Logger logger = LogManager.getLogger(WettkaempferService.class);

	private DecimalFormat df = new DecimalFormat("0.00");

	@Autowired
	private WettkaempferRepository wettkaempferRepository;
	@Lazy
	@Autowired
	private VereinService          vereinService;
	@Lazy
	@Autowired
	private EinstellungenService   einstellungenService;
	@Autowired
	private WettkampfService       wettkampfService;

	public List<Wettkaempfer> alleKaempfer(UUID turnierUUID) {
		logger.info("lade alleKaempfer");
		return wettkaempferRepository.findAll(turnierUUID);
	}

	public void loescheKaempfer(UUID id) {
		logger.info("loesche Wettkaempfer");
		wettkaempferRepository.deleteById(id);
		logger.info("Wettkaempfer gelöscht");
	}

	public Optional<Wettkaempfer> ladeKaempfer(UUID id) {
		logger.info("lade Wettkaempfer");
		return wettkaempferRepository.findById(id);
	}

	public Wettkaempfer speichereKaempfer(Wettkaempfer wettkaempfer) {
		logger.info("speichere Kaempfer {}", wettkaempfer);
		return wettkaempferRepository.save(wettkaempfer);
	}

	public void speichereCSV(UUID turnierUUID, MultipartFile file) {
		logger.info("Speichere Wettkaempfer");
		List<String[]> records = new ArrayList<>();
		String[] headers = null;

		if (file.isEmpty()) {
			logger.error("Datei ist leer!");
		}

		// Überprüfen, ob die Datei nicht leer ist
		if (!file.isEmpty()) {
			try (InputStreamReader reader = new InputStreamReader(file.getInputStream());
				 CSVReader csvReader = new CSVReader(reader)) {

				// Erste Zeile als Header speichern
				headers = csvReader.readNext();
				// prüfen ob das Format des CSV korrekt ist:
				// name,gewicht,altersklasse,geschlecht,vereinsname
				if (headers.length != 5 || !headers[0].strip().equalsIgnoreCase("name")
					|| !headers[1].strip().equalsIgnoreCase("gewicht")
					|| !headers[2].strip().equalsIgnoreCase("altersklasse")
					|| !headers[3].strip().equalsIgnoreCase("geschlecht")
					|| !headers[4].strip().equalsIgnoreCase("vereinsname")) {
					throw new CsvValidationException("Das CSV hat das falsche Format: NAME,GEWICHT,ALTERSKLASSE,GESCHLECHT,VEREINSNAME");
				}

				String[] nextRecord;
				while ((nextRecord = csvReader.readNext()) != null) {
					records.add(nextRecord);
				}

				logger.info("Geparste Daten: {} mit {} Einträgen", headers, records.size());
				records.forEach(record -> {

					var name = record[0].strip();
					var gewicht = record[1].strip();
					var altersklasse = record[2].strip();
					var geschlecht = record[3].strip();
					var vereinname = record[4].strip();

					Optional<Verein> verein = vereinService.sucheVerein(vereinname, turnierUUID);

					Wettkaempfer wettkaempfer = new Wettkaempfer(null,
						name,
						geschlecht.length() > 0 ? Geschlecht.valueOf(geschlecht.toLowerCase()) : null,
						altersklasse.length() > 0 ? Altersklasse.valueOf(altersklasse.toUpperCase()) : null,
						verein.orElse(null),
						gewicht.length() > 0 ? Double.parseDouble(gewicht) : 0d,
						Optional.empty(),
						false,
						false,
						turnierUUID);

					wettkaempferRepository.save(wettkaempfer);
				});
			} catch (IOException | CsvValidationException e) {
				logger.error(e);
			}
		}
	}

	public ByteArrayResource erstelleWettkaempferCSV(UUID turnierUUID) {
		logger.info("erstelleWettkaempferCSV");
		List<Wettkaempfer> wettkaempferList = wettkaempferRepository.findAll(turnierUUID);
		Map<UUID, GesamtWertung> gesamtWertungen = berechneGesamtWertungen(turnierUUID);
		Map<UUID, GesamtPlatzierung> gesamtPlatzierungen = berechneGesamtPlatzierungen(turnierUUID);

		try {
			// CSV-Daten erstellen
			StringWriter writer = new StringWriter();
			CSVWriter csvWriter = new CSVWriter(writer);
			csvWriter.writeNext(new String[]{"Id", "Name", "Gewicht", "Altersklasse", "Geschlecht", "Vereinsname", "Platz", "Kampfgeist", "Technik", "Kampfstil", "Vielfalt"}); // Kopfzeile

			for (Wettkaempfer wettkaempfer : wettkaempferList) {
				GesamtWertung gesamtWertung = gesamtWertungen.get(wettkaempfer.id());
				GesamtPlatzierung gesamtPlatzierung = gesamtPlatzierungen.get(wettkaempfer.id());

				csvWriter.writeNext(new String[]{
					wettkaempfer.id().toString(),
					wettkaempfer.name(),
					df.format(wettkaempfer.gewicht()),
					wettkaempfer.altersklasse().toString(),
					wettkaempfer.geschlecht().getBezeichnung(),
					wettkaempfer.verein().name(),
					gesamtPlatzierung != null ? gesamtPlatzierung.platzierung().toString() : "",
					gesamtWertung != null ? df.format(gesamtWertung.kampfgeist()) : "",
					gesamtWertung != null ? df.format(gesamtWertung.technik()) : "",
					gesamtWertung != null ? df.format(gesamtWertung.kampfstil()) : "",
					gesamtWertung != null ? df.format(gesamtWertung.vielfalt()) : ""
				});
			}

			csvWriter.close();

			// CSV als Byte-Array
			byte[] csvData = writer.toString().getBytes();
			return new ByteArrayResource(csvData);
		} catch (IOException e) {
			logger.error(e);
			throw new RuntimeException(e);
		}
	}

	public Map<UUID, GesamtWertung> berechneGesamtWertungen(UUID turnierUUID) {
		logger.info("berechne alle GesamtWertungen für Turnier {}", turnierUUID);
		if (!einstellungenService.isRandori(turnierUUID)) {
			logger.info("Für normale Turniere wird keine Wertung erstellt!");
			return Map.of();
		}

		List<Wettkaempfer> wettkaempferList = wettkaempferRepository.findAll(turnierUUID);
		List<Begegnung> begegnungList = wettkampfService.ladeAlleBegegnungen(turnierUUID);

		Map<UUID, GesamtWertung> gesamtWertungen = new HashMap<>();
		for (Wettkaempfer wettkaempfer : wettkaempferList) {
			List<Integer> kampfgeistList = holeWertungen(begegnungList, wettkaempfer, Wertung::getKampfgeistWettkaempfer1, Wertung::getKampfgeistWettkaempfer2);
			double kampfgeist = kampfgeistList.stream().mapToInt(Integer::intValue).average().orElseGet(() -> 0d);

			List<Integer> technikList = holeWertungen(begegnungList, wettkaempfer, Wertung::getTechnikWettkaempfer1, Wertung::getTechnikWettkaempfer2);
			double technik = technikList.stream().mapToInt(Integer::intValue).average().orElseGet(() -> 0d);

			List<Integer> kampfstilList = holeWertungen(begegnungList, wettkaempfer, Wertung::getKampfstilWettkaempfer1, Wertung::getKampfstilWettkaempfer2);
			double kampfstil = kampfstilList.stream().mapToInt(Integer::intValue).average().orElseGet(() -> 0d);

			List<Integer> vielfaltList = holeWertungen(begegnungList, wettkaempfer, Wertung::getVielfaltWettkaempfer1, Wertung::getVielfaltWettkaempfer2);
			double vielfalt = vielfaltList.stream().mapToInt(Integer::intValue).average().orElseGet(() -> 0d);

			GesamtWertung gesamtWertung = new GesamtWertung(kampfgeist, kampfgeistList, technik, technikList, kampfstil, kampfstilList, vielfalt, vielfaltList);
			gesamtWertungen.put(wettkaempfer.id(), gesamtWertung);
		}

		return gesamtWertungen;
	}

	public Map<UUID, GesamtPlatzierung> berechneGesamtPlatzierungen(UUID turnierUUID) {
		if (einstellungenService.isRandori(turnierUUID)) {
			logger.info("Für Randor-Turniere wird keine Platzierung erstellt!");
			return Map.of();
		}
		return Map.of();
	}

	public static List<Integer> holeWertungen(List<Begegnung> begegnungList, Wettkaempfer wettkaempfer, Function<Wertung, Integer> wettkaempfer1Mapper, Function<Wertung, Integer> wettkaempfer2Mapper) {
		return begegnungList.stream()
			.filter(b -> (b.getWettkaempfer1().isPresent() && b.getWettkaempfer1().get().id().equals(wettkaempfer.id())) ||
				(b.getWettkaempfer2().isPresent() && b.getWettkaempfer2().get().id().equals(wettkaempfer.id())))
			.flatMap(b -> {
				if (b.getWettkaempfer1().isPresent() && b.getWettkaempfer1().get().id().equals(wettkaempfer.id())) {
					return b.getWertungen().stream().map(wettkaempfer1Mapper);
				} else if (b.getWettkaempfer2().isPresent() && b.getWettkaempfer2().get().id().equals(wettkaempfer.id())) {
					return b.getWertungen().stream().map(wettkaempfer2Mapper);
				}
				return Stream.of(0);
			})
			.toList();
	}
}
