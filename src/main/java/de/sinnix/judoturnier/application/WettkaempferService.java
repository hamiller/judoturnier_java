package de.sinnix.judoturnier.application;

import de.sinnix.judoturnier.adapter.secondary.WettkaempferRepository;
import de.sinnix.judoturnier.model.Wettkaempfer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WettkaempferService {
    private static final Logger logger = LogManager.getLogger(WettkaempferService.class);

    @Autowired
    private WettkaempferRepository wettkaempferRepository;

    public List<Wettkaempfer> alleKaempfer() {
        logger.info("lade alleKaempfer");
        return wettkaempferRepository.findAll();
    }

    public void loescheKaempfer(Integer id) {
        logger.info("loesche Wettkaempfer");
        wettkaempferRepository.deleteById(id);
        logger.info("Wettkaempfer gelöscht");
    }

    public Optional<Wettkaempfer> ladeKaempfer(Integer id) {
        logger.info("lade Wettkaempfer");
        return wettkaempferRepository.findById(id);
    }

    public Wettkaempfer speichereKaempfer(Wettkaempfer wettkaempfer) {
        logger.info("speichere Kaempfer {}", wettkaempfer);
        return wettkaempferRepository.save(wettkaempfer);
    }
}
