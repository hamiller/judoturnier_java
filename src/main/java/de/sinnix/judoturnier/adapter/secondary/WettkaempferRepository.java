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
	private VereinConverter     vereinConverter;
	@Autowired
	private VereinJpaRepository vereinJpaRepository;

	public List<Wettkaempfer> findAll(UUID turnierUUID) {
		return wettkaempferJpaRepository.findAllByTurnierUUID(turnierUUID).stream().map(jpa -> wettkaempferConverter.convertToWettkaempfer(jpa)).collect(Collectors.toUnmodifiableList());
	}

	public void deleteById(UUID id) {
		var optionalWettkaempfer = wettkaempferJpaRepository.findById(id);
		if (optionalWettkaempfer.isEmpty()) {
			return;
		}

		wettkaempferJpaRepository.deleteById(id);
	}

	public Optional<Wettkaempfer> findById(UUID id) {
		return wettkaempferJpaRepository.findById(id).map(jpa -> wettkaempferConverter.convertToWettkaempfer(jpa));
	}

	public Wettkaempfer save(Wettkaempfer wettkaempfer) {
		logger.info("Speichere wettkaempfer");

		VereinJpa vereinJpa = vereinJpaRepository.findById(wettkaempfer.verein().id()).orElseThrow();
		if (wettkaempfer.id() != null) {
			var optionalWettkaempfer = wettkaempferJpaRepository.findById(wettkaempfer.id());
			if (optionalWettkaempfer.isPresent()) {
				logger.debug("Wettkämpfer existiert bereits, aktualisiere {}", wettkaempfer);
				WettkaempferJpa jpa = optionalWettkaempfer.get();
				jpa.updateFrom(wettkaempferConverter.convertFromWettkaempfer(wettkaempfer), vereinJpa);
				jpa = wettkaempferJpaRepository.save(jpa);
				return wettkaempferConverter.convertToWettkaempfer(jpa);
			}
		}

		logger.debug("Wettkämpfer wird neu angelegt {}", wettkaempfer);
		WettkaempferJpa jpa = wettkaempferConverter.convertFromWettkaempfer(wettkaempfer);
		jpa.setVerein(vereinJpa);
		jpa = wettkaempferJpaRepository.save(jpa);
		return wettkaempferConverter.convertToWettkaempfer(jpa);
	}
}
