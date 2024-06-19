package de.sinnix.judoturnier.application;

import de.sinnix.judoturnier.adapter.secondary.VereinRepository;
import de.sinnix.judoturnier.model.Verein;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VereinService {
    private static final Logger logger = LogManager.getLogger(VereinService.class);

    @Autowired
    private VereinRepository vereinRepository;

    public List<Verein> holeAlleVereine() {
        return vereinRepository.findAll().stream().map(jpa -> new Verein(jpa.getId(), jpa.getName())).collect(Collectors.toUnmodifiableList());
    }

    public Verein holeVerein(Integer vereinsId) {
        return vereinRepository.findById(vereinsId).map(jpa -> new Verein(jpa.getId(), jpa.getName())).orElseThrow();
    }
}
