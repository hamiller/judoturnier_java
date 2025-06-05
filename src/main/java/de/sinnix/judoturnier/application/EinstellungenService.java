package de.sinnix.judoturnier.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.sinnix.judoturnier.adapter.secondary.EinstellungJpa;
import de.sinnix.judoturnier.adapter.secondary.EinstellungJpaRepository;
import de.sinnix.judoturnier.adapter.secondary.TurnierConverter;
import de.sinnix.judoturnier.adapter.secondary.TurnierJpa;
import de.sinnix.judoturnier.adapter.secondary.TurnierJpaRepository;
import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Einstellungen;
import de.sinnix.judoturnier.model.Gruppengroessen;
import de.sinnix.judoturnier.model.MattenAnzahl;
import de.sinnix.judoturnier.model.SeparateAlterklassen;
import de.sinnix.judoturnier.model.Turnier;
import de.sinnix.judoturnier.model.TurnierTyp;
import de.sinnix.judoturnier.model.VariablerGewichtsteil;
import de.sinnix.judoturnier.model.WettkampfReihenfolge;
import de.sinnix.judoturnier.model.Wettkampfzeiten;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Transactional
@Service
public class EinstellungenService {
	private static final Logger logger = LogManager.getLogger(EinstellungenService.class);

	@Autowired
	private EinstellungJpaRepository einstellungJpaRepository;
	@Autowired
	private TurnierJpaRepository     turnierJpaRepository;
	@Autowired
	private TurnierConverter         turnierConverter;
	@Autowired
	private ObjectMapper             objectMapper;

	private static final TurnierTyp            DEFAULT_TURNIERTYP             = TurnierTyp.RANDORI;
	private static final MattenAnzahl          DEFAULT_MATTENANZAHL           = new MattenAnzahl(2);
	private static final WettkampfReihenfolge  DEFAULT_WETTKAMPFREIHENFOLGE   = WettkampfReihenfolge.ABWECHSELND;
	private static final Gruppengroessen       DEFAULT_GRUPPENGROESSEN        = new Gruppengroessen(Map.of(
		Altersklasse.U9, 6,
		Altersklasse.U11, 6,
		Altersklasse.U12, 6,
		Altersklasse.U13, 30,
		Altersklasse.U15, 30,
		Altersklasse.U18, 30,
		Altersklasse.U21, 30,
		Altersklasse.Frauen, 30,
		Altersklasse.Maenner, 30));
	private static final VariablerGewichtsteil DEFAULT_VARIABLER_GEWICHTSTEIL = new VariablerGewichtsteil(0.2);
	private static final SeparateAlterklassen  DEFAULT_SEPARATE_ALTERKLASSEN  = SeparateAlterklassen.GETRENNT;
	private static final Wettkampfzeiten       DEFAULT_WETTKAMPFZEITEN        = new Wettkampfzeiten(Map.of(
		Altersklasse.U9, 3 * 60,
		Altersklasse.U11, 3 * 60,
		Altersklasse.U12, 3 * 60,
		Altersklasse.U13, 3 * 60,
		Altersklasse.U15, 3 * 60,
		Altersklasse.U18, 3 * 60,
		Altersklasse.U21, 4 * 60,
		Altersklasse.Frauen, 4 * 60,
		Altersklasse.Maenner, 4 * 60));


	public Einstellungen ladeEinstellungen(UUID turnierUUID) {
		logger.info("EinstellungenService ladeEinstellungen()");
		List<EinstellungJpa> einstellungenList = einstellungJpaRepository.findAll().stream().filter(e -> e.getId().getTurnierUUID().equals(turnierUUID)).toList();
		TurnierTyp turnierTyp = einstellungenList.stream().filter(e -> e.getId().getArt().equalsIgnoreCase(TurnierTyp.TYP)).findFirst().map(t -> TurnierTyp.valueOf(t.getWert())).orElse(DEFAULT_TURNIERTYP);
		MattenAnzahl mattenAnzahl = einstellungenList.stream().filter(e -> e.getId().getArt().equalsIgnoreCase(MattenAnzahl.TYP)).findFirst().map(t -> new MattenAnzahl(Integer.parseInt(t.getWert()))).orElseGet(() -> DEFAULT_MATTENANZAHL);
		WettkampfReihenfolge wettkampfReihenfolge = einstellungenList.stream().filter(e -> e.getId().getArt().equalsIgnoreCase(WettkampfReihenfolge.TYP)).findFirst().map(t -> WettkampfReihenfolge.valueOf(t.getWert())).orElseGet(() -> DEFAULT_WETTKAMPFREIHENFOLGE);
		Gruppengroessen gruppengroessen = einstellungenList.stream().filter(e -> e.getId().getArt().equalsIgnoreCase(Gruppengroessen.TYP)).findFirst().map(wkz -> convertToObject(wkz.getWert(), Gruppengroessen.class)).orElseGet(() -> DEFAULT_GRUPPENGROESSEN);
		VariablerGewichtsteil variablerGewichtsteil = einstellungenList.stream().filter(e -> e.getId().getArt().equalsIgnoreCase(VariablerGewichtsteil.TYP)).findFirst().map(v -> new VariablerGewichtsteil(Double.parseDouble(v.getWert()))).orElseGet(() -> DEFAULT_VARIABLER_GEWICHTSTEIL);
		SeparateAlterklassen separateAlterklassen = einstellungenList.stream().filter(e -> e.getId().getArt().equalsIgnoreCase(SeparateAlterklassen.TYP)).findFirst().map(t -> SeparateAlterklassen.valueOf(t.getWert())).orElse(DEFAULT_SEPARATE_ALTERKLASSEN);
		Wettkampfzeiten wettkampfzeiten = einstellungenList.stream().filter(e -> e.getId().getArt().equalsIgnoreCase(Wettkampfzeiten.TYP)).findFirst().map(wkz -> convertToObject(wkz.getWert(), Wettkampfzeiten.class)).orElseGet(() -> DEFAULT_WETTKAMPFZEITEN);
		return new Einstellungen(turnierTyp, mattenAnzahl, wettkampfReihenfolge, gruppengroessen, variablerGewichtsteil, separateAlterklassen, wettkampfzeiten, turnierUUID);
	}

	public Einstellungen speichereTurnierEinstellungen(Einstellungen einstellungen) {
		logger.info("EinstellungenService speichereTurnierEinstellungen()");
		List<EinstellungJpa> jpaList = List.of(
			new EinstellungJpa(new EinstellungJpa.EinstellungId(einstellungen.turnierTyp().TYP, einstellungen.turnierUUID()), einstellungen.turnierTyp().name()),
			new EinstellungJpa(new EinstellungJpa.EinstellungId(einstellungen.mattenAnzahl().TYP, einstellungen.turnierUUID()), einstellungen.mattenAnzahl().anzahl().toString()),
			new EinstellungJpa(new EinstellungJpa.EinstellungId(einstellungen.wettkampfReihenfolge().TYP, einstellungen.turnierUUID()), einstellungen.wettkampfReihenfolge().name()),
			new EinstellungJpa(new EinstellungJpa.EinstellungId(einstellungen.gruppengroessen().TYP, einstellungen.turnierUUID()), convertFromObject(einstellungen.gruppengroessen())),
			new EinstellungJpa(new EinstellungJpa.EinstellungId(einstellungen.variablerGewichtsteil().TYP, einstellungen.turnierUUID()), einstellungen.variablerGewichtsteil().variablerTeil().toString()),
			new EinstellungJpa(new EinstellungJpa.EinstellungId(einstellungen.separateAlterklassen().TYP, einstellungen.turnierUUID()), einstellungen.separateAlterklassen().name()),
			new EinstellungJpa(new EinstellungJpa.EinstellungId(einstellungen.wettkampfzeiten().TYP, einstellungen.turnierUUID()), convertFromObject(einstellungen.wettkampfzeiten()))
		);
		logger.info("speichere {}", jpaList);
		einstellungJpaRepository.saveAll(jpaList);
		return ladeEinstellungen(einstellungen.turnierUUID());
	}

	public boolean isRandori(UUID turnierUUID) {
		logger.info("Prüfe Turnierart für Turnier {}", turnierUUID);
		EinstellungJpa.EinstellungId einstellungId = new EinstellungJpa.EinstellungId(TurnierTyp.TYP, turnierUUID);
		TurnierTyp turnierTyp = einstellungJpaRepository.findById(einstellungId).map(typ -> TurnierTyp.valueOf(typ.getWert())).orElseThrow();
		return turnierTyp == TurnierTyp.RANDORI;
	}

	public Integer kampfZeit(UUID turnierUUID, Altersklasse altersklasse) {
		logger.info("Hole Kampfzeit für Turnier {} und Altersklasse {}", turnierUUID, altersklasse);
		EinstellungJpa.EinstellungId einstellungId = new EinstellungJpa.EinstellungId(Wettkampfzeiten.TYP, turnierUUID);
		Wettkampfzeiten wettkampfzeiten = einstellungJpaRepository.findById(einstellungId).map(jpa -> convertToObject(jpa.getWert(), Wettkampfzeiten.class)).orElseGet(() -> DEFAULT_WETTKAMPFZEITEN);
		return wettkampfzeiten.altersklasseKampfzeitSekunden().get(altersklasse);
	}

	public Turnier ladeTurnierDaten(UUID turnierUUID) {
		logger.info("Lade Turnierdaten für Turnier {}", turnierUUID);
		TurnierJpa jpa = turnierJpaRepository.findById(turnierUUID).orElseThrow();
		return turnierConverter.convertToTurnier(jpa);
	}

	public Einstellungen speichereDefaultEinstellungen(UUID turnierUUID) {
		logger.info("Erstelle Default-Einstellungen für Turnier {}", turnierUUID);
		List<EinstellungJpa> jpaList = List.of(
			new EinstellungJpa(new EinstellungJpa.EinstellungId(TurnierTyp.TYP, turnierUUID), DEFAULT_TURNIERTYP.name()),
			new EinstellungJpa(new EinstellungJpa.EinstellungId(MattenAnzahl.TYP, turnierUUID), DEFAULT_MATTENANZAHL.anzahl().toString()),
			new EinstellungJpa(new EinstellungJpa.EinstellungId(WettkampfReihenfolge.TYP, turnierUUID), DEFAULT_WETTKAMPFREIHENFOLGE.name()),
			new EinstellungJpa(new EinstellungJpa.EinstellungId(Gruppengroessen.TYP, turnierUUID), convertFromObject(DEFAULT_GRUPPENGROESSEN)),
			new EinstellungJpa(new EinstellungJpa.EinstellungId(VariablerGewichtsteil.TYP, turnierUUID), DEFAULT_VARIABLER_GEWICHTSTEIL.variablerTeil().toString()),
			new EinstellungJpa(new EinstellungJpa.EinstellungId(SeparateAlterklassen.TYP, turnierUUID), DEFAULT_SEPARATE_ALTERKLASSEN.name()),
			new EinstellungJpa(new EinstellungJpa.EinstellungId(Wettkampfzeiten.TYP, turnierUUID), convertFromObject(DEFAULT_WETTKAMPFZEITEN))
		);
		einstellungJpaRepository.saveAll(jpaList);
		return ladeEinstellungen(turnierUUID);
	}

	private <T> String convertFromObject(T object) {
		try {
			if (object == null) {
				throw new IllegalArgumentException("Das zu konvertierende Objekt darf nicht null sein.");
			}
			return objectMapper.writeValueAsString(object);
		} catch (Exception e) {
			throw new RuntimeException("Fehler beim Konvertieren eines Objekts in JSON", e);
		}
	}

	private <T> T convertToObject(String json, Class<T> type) {
		try {
			if (json == null || json.isEmpty()) {
				throw new IllegalArgumentException("JSON-String darf nicht null oder leer sein.");
			}
			return objectMapper.readValue(json, type);
		} catch (Exception e) {
			throw new RuntimeException("Fehler beim Konvertieren von JSON in Objekt", e);
		}
	}
}
