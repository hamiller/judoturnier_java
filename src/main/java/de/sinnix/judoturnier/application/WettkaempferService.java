package de.sinnix.judoturnier.application;

import de.sinnix.judoturnier.adapter.secondary.VereinConverter;
import de.sinnix.judoturnier.adapter.secondary.WettkaempferConverter;
import de.sinnix.judoturnier.adapter.secondary.WettkaempferJpa;
import de.sinnix.judoturnier.adapter.secondary.WettkaempferRepository;
import de.sinnix.judoturnier.model.Wettkaempfer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WettkaempferService {
    private static final Logger logger = LogManager.getLogger(WettkaempferService.class);

    @Autowired
    private WettkaempferRepository wettkaempferRepository;
    @Autowired
    private WettkaempferConverter wettkaempferConverter;
    @Autowired
    private VereinConverter vereinConverter;

    public List<Wettkaempfer> alleKaempfer() {
        logger.info("lade alleKaempfer");
        return wettkaempferRepository.findAll().stream().map(jpa -> wettkaempferConverter.convertToWettkaempfer(jpa)).collect(Collectors.toUnmodifiableList());
    }

    public void loescheKaempfer(Integer id) {
        logger.info("loesche Wettkaempfer");
        var optionalWettkaempfer = wettkaempferRepository.findById(id);
        if (optionalWettkaempfer.isEmpty()) {
            logger.warn("Wettkaempfer nicht gefunden");
            return;
        }

        wettkaempferRepository.deleteById(id);
        logger.info("Wettkaempfer gelöscht");
    }

    public Wettkaempfer ladeKaempfer(Integer id) {
        logger.info("lade Wettkaempfer");
        return wettkaempferRepository.findById(id).map(jpa -> wettkaempferConverter.convertToWettkaempfer(jpa)).orElseGet(() -> null);
    }

    public Wettkaempfer speichereKaempfer(Wettkaempfer wettkaempfer) {
        logger.info("speichere Kaempfer {}", wettkaempfer);
        if (wettkaempfer.id() != null) {
            var optionalWettkaempfer = wettkaempferRepository.findById(wettkaempfer.id());
            if (optionalWettkaempfer.isPresent()) {
                logger.info("Aktualisiere Wettkaempfer");
                WettkaempferJpa wk = optionalWettkaempfer.get();
                wk.setName(wettkaempfer.name());
                wk.setGeschlecht(wettkaempfer.geschlecht());
                wk.setAltersklasse(wettkaempfer.altersklasse());
                wk.setVerein(vereinConverter.convertFromVerein(wettkaempfer.verein()));
                wk.setGewicht(wettkaempfer.gewicht());
                wk.setFarbe(wettkaempfer.farbe());
                wk.setChecked(wettkaempfer.checked());
                wk.setPrinted(wettkaempfer.printed());
                wk = wettkaempferRepository.save(wk);
                logger.info("Aktualisiertes jpa: {}", wk);
                return wettkaempferConverter.convertToWettkaempfer(wk);
            }
        }

        var wk = wettkaempferConverter.convertFromWettkaempfer(wettkaempfer);
        logger.debug("Speichere jpa: {}", wk);
        wk = wettkaempferRepository.save(wk);
        logger.debug("Gespeichertes jpa: {}", wk);
        return wettkaempferConverter.convertToWettkaempfer(wk);
    }
}
