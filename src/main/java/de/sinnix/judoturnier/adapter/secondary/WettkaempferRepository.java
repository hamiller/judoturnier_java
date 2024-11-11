package de.sinnix.judoturnier.adapter.secondary;

import de.sinnix.judoturnier.model.Wettkaempfer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class WettkaempferRepository {

	private static final Logger logger = LogManager.getLogger(WettkaempferRepository.class);

	@Autowired
	private WettkaempferJpaRepository wettkaempferJpaRepository;
	@Autowired
	private WettkaempferConverter     wettkaempferConverter;
	@Autowired
	private VereinConverter           vereinConverter;

	public List<Wettkaempfer> findAll(UUID turnierUUID) {
		return wettkaempferJpaRepository.findAllByTurnierUUID(turnierUUID.toString()).stream().map(jpa -> wettkaempferConverter.convertToWettkaempfer(jpa)).collect(Collectors.toUnmodifiableList());
	}

	public void deleteById(UUID id) {
		var optionalWettkaempfer = wettkaempferJpaRepository.findById(id.toString());
		if (optionalWettkaempfer.isEmpty()) {
			return;
		}

		wettkaempferJpaRepository.deleteById(id.toString());
	}

	public Optional<Wettkaempfer> findById(UUID id) {
		return wettkaempferJpaRepository.findById(id.toString()).map(jpa -> wettkaempferConverter.convertToWettkaempfer(jpa));
	}

	public Wettkaempfer save(Wettkaempfer wettkaempfer) {
		logger.debug("Speichere wettkaempfer in db: {}", wettkaempfer);
		if (wettkaempfer.id() != null) {
			var optionalWettkaempfer = wettkaempferJpaRepository.findById(wettkaempfer.id().toString());
			if (optionalWettkaempfer.isPresent()) {
				WettkaempferJpa wk = optionalWettkaempfer.get();
				wk.setName(wettkaempfer.name());
				wk.setGeschlecht(wettkaempfer.geschlecht().name());
				wk.setAltersklasse(wettkaempfer.altersklasse().name());
				wk.setVerein(vereinConverter.convertFromVerein(wettkaempfer.verein()));
				wk.setGewicht(wettkaempfer.gewicht());
				wk.setFarbe(wettkaempfer.farbe().map(f -> f.name()).orElse(null));
				wk.setChecked(wettkaempfer.checked());
				wk.setPrinted(wettkaempfer.printed());
				wk = wettkaempferJpaRepository.save(wk);
				return wettkaempferConverter.convertToWettkaempfer(wk);
			}
		}

		var wk = wettkaempferConverter.convertFromWettkaempfer(wettkaempfer);
		wk = wettkaempferJpaRepository.save(wk);
		return wettkaempferConverter.convertToWettkaempfer(wk);
	}
}
