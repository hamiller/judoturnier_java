package de.sinnix.judoturnier.adapter.secondary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


import de.sinnix.judoturnier.model.Wertung;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Repository
public class WertungRepository {
	private static final Logger logger = LogManager.getLogger(WertungRepository.class);

	@Autowired
	private BegegnungJpaRepository    begegnungJpaRepository;
	@Autowired
	private WertungConverter          wertungConverter;
	@Autowired
	private BenutzerJpaRepository     benutzerJpaRepository;
	@Autowired
	private WettkaempferJpaRepository wettkaempferJpaRepository;
	@Autowired
	private WertungJpaRepository      wertungJpaRepository;

	@Transactional
	public void speichereWertungInBegegnung(Wertung wertung, UUID begegnungId) {
		logger.info("Speichere Wertung in Begegnung");

		BegegnungJpa begegnungJpa = begegnungJpaRepository.findById(begegnungId).orElseThrow();
		BenutzerJpa benutzerJpa = benutzerJpaRepository.findById(wertung.getBewerter().uuid()).orElseThrow();

		// hole Sieger, falls normales Turnier
		Optional<WettkaempferJpa> siegerJpaOptional = wertung.getSieger() != null
			? wettkaempferJpaRepository.findById(wertung.getSieger().id())
			: Optional.empty();
		List<WertungJpa> wertungenJpaList = begegnungJpa.getWertungen();

		// Suche nach bestehender Wertung des Kampfrichters
		Optional<WertungJpa> wertungJpaOptional = wertungenJpaList.stream().filter(wJpa -> wJpa.getBenutzer().getId().equals(benutzerJpa.getId())).findFirst();
		if (wertungJpaOptional.isPresent()) {
			logger.debug("Wertung existiert bereits, aktualisiere {}", wertung);
			WertungJpa jpa = wertungJpaOptional.get();
			jpa.updateFrom(wertungConverter.convertFromWertung(wertung), siegerJpaOptional);
			begegnungJpaRepository.save(begegnungJpa);
			return;
		}

		logger.debug("Wertung wird neu angelegt {}", wertung);
		WertungJpa jpa = wertungConverter.convertFromWertung(wertung);
		jpa.setBenutzer(benutzerJpa);
		// Normale Turnierwertung
		if (siegerJpaOptional.isPresent()) {
			jpa.setSieger(siegerJpaOptional.get());
			// bei normalen Turnieren gibt es immer nur eine Wertung, ersetze also bisherige Wertungen anderer Kampfrichter
			begegnungJpa.setWertungen(new ArrayList<>());
		}
		begegnungJpa.getWertungen().add(jpa);

		begegnungJpaRepository.save(begegnungJpa);
	}
}
