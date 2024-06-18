package de.sinnix.judoturnier.application;

import de.sinnix.judoturnier.model.Wettkaempfer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WiegenService {
    private static final Logger logger = LogManager.getLogger(WiegenService.class);

    public List<Wettkaempfer> alleKaempfer() {
        return List.of();
    }
}
