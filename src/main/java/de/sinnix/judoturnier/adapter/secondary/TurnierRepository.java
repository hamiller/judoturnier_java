package de.sinnix.judoturnier.adapter.secondary;

import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Begegnung;
import de.sinnix.judoturnier.model.Matte;
import de.sinnix.judoturnier.model.Wertung;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TurnierRepository {

	private static final Logger logger = LogManager.getLogger(TurnierRepository.class);

	@Autowired
	private WertungJpaRepository wertungJpaRepository;
	@Autowired
	private WertungConverter wertungConverter;

	public Begegnung ladeBegegnung(String wettkampfId) {
		return null;
	}

	public Optional<Wertung> ladeWertung(String wettkampfId) {
		return wertungJpaRepository.findById(wettkampfId).map(jpa -> wertungConverter.convertToWertung(jpa));
	}

	public void speichereWertung(Wertung wertung) {

	}

	public List<Matte> ladeMatten() {
		return List.of();
	}

	public void speichereMatten(List<Matte> mattenList) {

	}

	public void speichereMatte(Matte matte) {

	}

	public void loescheAlleMatten() {

	}

	public void loescheWettkaempfeMitAltersklasse(Altersklasse altersklasse) {

	}
}
