package de.sinnix.judoturnier.application;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import de.sinnix.judoturnier.adapter.secondary.VereinConverter;
import de.sinnix.judoturnier.adapter.secondary.VereinJpa;
import de.sinnix.judoturnier.adapter.secondary.VereinJpaRepository;
import de.sinnix.judoturnier.model.Verein;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Transactional
@Service
public class VereinService {
	private static final Logger logger = LogManager.getLogger(VereinService.class);

	@Autowired
	private VereinJpaRepository vereinJpaRepository;

	@Autowired
	private VereinConverter vereinConverter;

	public List<Verein> holeAlleVereine(UUID turnierUUID) {
		return vereinJpaRepository.findAllByTurnierUUID(turnierUUID).stream().map(jpa -> vereinConverter.converToVerein(jpa)).collect(Collectors.toUnmodifiableList());
	}

	public Verein holeVerein(UUID vereinsId, UUID turnierUUID) {
		return vereinJpaRepository.findById(vereinsId).map(jpa -> vereinConverter.converToVerein(jpa)).orElseThrow();
	}

	public Verein speichereVerein(Verein verein) {
		logger.info("Speichere Verein");

		if (verein.id() != null) {
			var optionalVerein = vereinJpaRepository.findById(verein.id());
			if (optionalVerein.isPresent()) {
				logger.debug("Verein {} existiert bereits, aktualisiere", verein);
				VereinJpa jpa = optionalVerein.get();
				jpa.updateFrom(vereinConverter.convertFromVerein(verein));
				jpa = vereinJpaRepository.save(jpa);
				return vereinConverter.converToVerein(jpa);
			}
		}

		logger.debug("Verein {} wird neu angelegt", verein);
		VereinJpa jpa = vereinJpaRepository.save(vereinConverter.convertFromVerein(verein));
		return vereinConverter.converToVerein(jpa);
	}

	public void speichereCSV(UUID turnierUUID, MultipartFile file) {
		logger.info("Speichere Vereine");
		List<String[]> records = new ArrayList<>();
		String[] headers = null;

		// Überprüfen, ob die Datei nicht leer ist
		if (!file.isEmpty()) {
			try (InputStreamReader reader = new InputStreamReader(file.getInputStream());
				 CSVReader csvReader = new CSVReader(reader)) {

				// Erste Zeile als Header speichern
				headers = csvReader.readNext();
				// prüfen ob das Format des CSV korrekt ist:
				// name
				if (headers.length != 1 || !headers[0].strip().equalsIgnoreCase("name")) {
					throw new CsvValidationException("Das CSV hat das falsche Format: NAME");
				}

				String[] nextRecord;
				while ((nextRecord = csvReader.readNext()) != null) {
					records.add(nextRecord);
				}

				logger.info("Geparste Daten: {} mit {} Einträgen", headers, records.size());
				records.forEach(record -> {
					var name = record[0].strip();
					Verein verein = new Verein(null, name, turnierUUID);
					vereinJpaRepository.save(vereinConverter.convertFromVerein(verein));
				});
			} catch (IOException | CsvValidationException e) {
				logger.error(e);
				throw new Error(e);
			}
		}
	}

	public Optional<Verein> sucheVerein(String vereinname, UUID turnierUUID) {
		return vereinJpaRepository.findAllByTurnierUUID(turnierUUID).stream().filter(jpa -> jpa.getName().equalsIgnoreCase(vereinname)).findFirst().map(jpa -> vereinConverter.converToVerein(jpa));
	}

	public void loescheVerein(UUID id, UUID turnierUUID) {
		logger.info("Loesche Verein {}", id);
		vereinJpaRepository.deleteById(id);
		logger.info("Verein gelöscht");
	}
}
