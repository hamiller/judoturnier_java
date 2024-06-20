package de.sinnix.judoturnier.application;

import de.sinnix.judoturnier.adapter.secondary.VereinJpaRepository;
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
    private VereinJpaRepository vereinJpaRepository;

    public List<Verein> holeAlleVereine() {
        return vereinJpaRepository.findAll().stream().map(jpa -> new Verein(jpa.getId(), jpa.getName())).collect(Collectors.toUnmodifiableList());
    }

    public Verein holeVerein(Integer vereinsId) {
        return vereinJpaRepository.findById(vereinsId).map(jpa -> new Verein(jpa.getId(), jpa.getName())).orElseThrow();
    }
}
