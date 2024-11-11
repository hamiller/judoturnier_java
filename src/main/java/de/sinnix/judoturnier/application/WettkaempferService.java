package de.sinnix.judoturnier.application;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import de.sinnix.judoturnier.adapter.secondary.WettkaempferRepository;
import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Farbe;
import de.sinnix.judoturnier.model.Geschlecht;
import de.sinnix.judoturnier.model.Verein;
import de.sinnix.judoturnier.model.Wettkaempfer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class WettkaempferService {
	private static final Logger logger = LogManager.getLogger(WettkaempferService.class);

	@Autowired
	private WettkaempferRepository wettkaempferRepository;
	@Lazy
	@Autowired
	private VereinService          vereinService;

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
}
