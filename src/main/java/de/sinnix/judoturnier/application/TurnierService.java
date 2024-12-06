package de.sinnix.judoturnier.application;

import de.sinnix.judoturnier.adapter.secondary.TurnierRepository;
import de.sinnix.judoturnier.model.Begegnung;
import de.sinnix.judoturnier.model.Matte;
import de.sinnix.judoturnier.model.Metadaten;
import de.sinnix.judoturnier.model.Turnier;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional
@Service
public class TurnierService {
	private static final Logger           logger     = LogManager.getLogger(TurnierService.class);
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	@Lazy
	@Autowired
	private EinstellungenService   einstellungenService;
	@Autowired
	private TurnierRepository      turnierRepository;

	public List<Turnier> ladeAlleTurniere() {
		logger.info("ladeTurniere");
		return turnierRepository.ladeAlleTurniere();
	}

	public List<Turnier> ladeTurniere(List<UUID> turnierUUIDs) {
		logger.info("lade alle Turniere {}", turnierUUIDs);
		return turnierUUIDs.stream()
			.map(uuid -> turnierRepository.ladeTurnier(uuid))
			.toList();
	}

	public List<Matte> ladeWettkampfreihenfolge(UUID turnierUUID) {
		logger.info("ladeWettkampfreihenfolge");
		return turnierRepository.ladeMatten(turnierUUID).values().stream().toList();
	}

	public void loescheWettkampfreihenfolge(UUID turnierUUID) {
		logger.info("loescheWettkampfreihenfolge");
		turnierRepository.loescheAlleMatten(turnierUUID);
	}

	public Turnier erstelleTurnier(String name, String ort, String datum) {
		logger.info("erstelle neues Turnier");
		try {
			// Parsen des Strings in ein java.util.Date
			Date parsedDate = dateFormat.parse(datum);
			Turnier neuesTurnier = new Turnier(UUID.randomUUID(), name, ort, parsedDate);
			Turnier turnier = turnierRepository.speichereTurnier(neuesTurnier);



			einstellungenService.speichereDefaultEinstellungen(turnier.uuid());

			return turnier;
		} catch (ParseException e) {
			logger.warn("Datum konnte nicht geparsed werden: {}", datum, e);
			throw new IllegalArgumentException("Datum");
		}
	}

	public Turnier ladeTurnier(UUID turnierid) {
		return turnierRepository.ladeTurnier(turnierid);
	}

	public Metadaten ladeMetadaten(UUID id, UUID turnierUUID) {
		logger.info("Lade Metadaten für Begegnung {}", id);
		var matten = turnierRepository.ladeMatten(turnierUUID);

		var aktuelleRunde = matten.values().stream()
			.flatMap(matte -> matte.runden().stream())
			.filter(runde -> runde.begegnungen().stream()
				.anyMatch(begegnung -> begegnung.getId().equals(id)))
			.findFirst();

		if (aktuelleRunde.isEmpty()) {
			throw new IllegalArgumentException("Es konnten keine Daten zu dieser Begegnung gefunden werden.");
		}

		var alleRundeBegegnungIds = aktuelleRunde.get().begegnungen().stream().map(Begegnung::getId).toList();

		UUID vorgaenger = null;
		UUID nachfolger = null;
		int index = alleRundeBegegnungIds.indexOf(id);
		if (index > 0) {
			vorgaenger = alleRundeBegegnungIds.get(index - 1);
		}
		if (index < alleRundeBegegnungIds.size() - 1) {
			nachfolger = alleRundeBegegnungIds.get(index + 1);
		}

		return new Metadaten(alleRundeBegegnungIds, Optional.ofNullable(vorgaenger), Optional.ofNullable(nachfolger), aktuelleRunde.get().rundeId());
	}

	public List<Begegnung> ladeMattenRunde(UUID turnierUUID, Integer matte, Integer mattenrunde) {
		logger.info("Lade Matten Runde");

		return turnierRepository.ladeAlleBegegnungen(turnierUUID).stream()
			.filter(b -> b.getMatteId().equals(matte))
			.filter(b -> b.getMattenRunde().equals(mattenrunde))
			.toList();
	}

}
