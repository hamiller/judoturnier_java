package de.sinnix.judoturnier.application;

import de.sinnix.judoturnier.adapter.secondary.EinstellungJpa;
import de.sinnix.judoturnier.adapter.secondary.EinstellungJpaRepository;
import de.sinnix.judoturnier.model.Einstellungen;
import de.sinnix.judoturnier.model.MattenAnzahl;
import de.sinnix.judoturnier.model.Gruppengroesse;
import de.sinnix.judoturnier.model.SeparateAlterklassen;
import de.sinnix.judoturnier.model.TurnierTyp;
import de.sinnix.judoturnier.model.VariablerGewichtsteil;
import de.sinnix.judoturnier.model.WettkampfReihenfolge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EinstellungenService {
	private static final Logger logger = LogManager.getLogger(EinstellungenService.class);

	@Autowired
	private EinstellungJpaRepository einstellungJpaRepository;

	private static final TurnierTyp            DEFAULT_TURNIERTYP             = TurnierTyp.RANDORI;
	private static final MattenAnzahl          DEFAULT_MATTENANZAHL           = new MattenAnzahl(2);
	private static final WettkampfReihenfolge  DEFAULT_WETTKAMPFREIHENFOLGE   = WettkampfReihenfolge.ABWECHSELND;
	private static final Gruppengroesse        DEFAULT_GRUPPENGROESSE         = new Gruppengroesse(6);
	private static final VariablerGewichtsteil DEFAULT_VARIABLER_GEWICHTSTEIL = new VariablerGewichtsteil(0.2);
	private static final SeparateAlterklassen  DEFAULT_SEPARATE_ALTERKLASSEN  = SeparateAlterklassen.GETRENNT;


	public Einstellungen ladeEinstellungen(UUID turnierUUID) {
		logger.info("EinstellungenService ladeEinstellungen()");
		List<EinstellungJpa> einstellungenList = einstellungJpaRepository.findAll().stream().filter(e -> e.getId().getTurnierUUID().equalsIgnoreCase(turnierUUID.toString())).toList();
		TurnierTyp turnierTyp = einstellungenList.stream().filter(e -> e.getId().getArt().equalsIgnoreCase(TurnierTyp.TYP)).findFirst().map(t -> TurnierTyp.valueOf(t.getWert())).orElse(DEFAULT_TURNIERTYP);
		MattenAnzahl mattenAnzahl = einstellungenList.stream().filter(e -> e.getId().getArt().equalsIgnoreCase(MattenAnzahl.TYP)).findFirst().map(t -> new MattenAnzahl(Integer.parseInt(t.getWert()))).orElseGet(() -> DEFAULT_MATTENANZAHL);
		WettkampfReihenfolge wettkampfReihenfolge = einstellungenList.stream().filter(e -> e.getId().getArt().equalsIgnoreCase(WettkampfReihenfolge.TYP)).findFirst().map(t -> WettkampfReihenfolge.valueOf(t.getWert())).orElseGet(() -> DEFAULT_WETTKAMPFREIHENFOLGE);
		Gruppengroesse gruppengroesse = einstellungenList.stream().filter(e -> e.getId().getArt().equalsIgnoreCase(Gruppengroesse.TYP)).findFirst().map(r -> new Gruppengroesse(Integer.parseInt(r.getWert()))).orElseGet(() -> DEFAULT_GRUPPENGROESSE);
		VariablerGewichtsteil variablerGewichtsteil = einstellungenList.stream().filter(e -> e.getId().getArt().equalsIgnoreCase(VariablerGewichtsteil.TYP)).findFirst().map(v -> new VariablerGewichtsteil(Double.parseDouble(v.getWert()))).orElseGet(() -> DEFAULT_VARIABLER_GEWICHTSTEIL);
		SeparateAlterklassen separateAlterklassen = einstellungenList.stream().filter(e -> e.getId().getArt().equalsIgnoreCase(SeparateAlterklassen.TYP)).findFirst().map(t -> SeparateAlterklassen.valueOf(t.getWert())).orElse(DEFAULT_SEPARATE_ALTERKLASSEN);
		return new Einstellungen(turnierTyp, mattenAnzahl, wettkampfReihenfolge, gruppengroesse, variablerGewichtsteil, separateAlterklassen, turnierUUID);
	}

	public Einstellungen speichereTurnierEinstellungen(Einstellungen einstellungen) {
		logger.info("EinstellungenService speichereTurnierEinstellungen()");
		List<EinstellungJpa> jpaList = List.of(
			new EinstellungJpa(new EinstellungJpa.EinstellungId(einstellungen.turnierTyp().TYP, einstellungen.turnierUUID().toString()), einstellungen.turnierTyp().name()),
			new EinstellungJpa(new EinstellungJpa.EinstellungId(einstellungen.mattenAnzahl().TYP, einstellungen.turnierUUID().toString()), einstellungen.mattenAnzahl().anzahl().toString()),
			new EinstellungJpa(new EinstellungJpa.EinstellungId(einstellungen.wettkampfReihenfolge().TYP, einstellungen.turnierUUID().toString()), einstellungen.wettkampfReihenfolge().name()),
			new EinstellungJpa(new EinstellungJpa.EinstellungId(einstellungen.gruppengroesse().TYP, einstellungen.turnierUUID().toString()), einstellungen.gruppengroesse().anzahl().toString()),
			new EinstellungJpa(new EinstellungJpa.EinstellungId(einstellungen.variablerGewichtsteil().TYP, einstellungen.turnierUUID().toString()), einstellungen.variablerGewichtsteil().variablerTeil().toString()),
			new EinstellungJpa(new EinstellungJpa.EinstellungId(einstellungen.separateAlterklassen().TYP, einstellungen.turnierUUID().toString()), einstellungen.separateAlterklassen().name())
		);
		einstellungJpaRepository.saveAll(jpaList);
		return ladeEinstellungen(einstellungen.turnierUUID());
	}

	public boolean isRandori(UUID turnierUUID) {
		EinstellungJpa.EinstellungId einstellungId = new EinstellungJpa.EinstellungId(TurnierTyp.TYP, turnierUUID.toString());
		TurnierTyp turnierTyp = einstellungJpaRepository.findById(einstellungId).map(typ -> TurnierTyp.valueOf(typ.getWert())).orElseThrow();
		return turnierTyp == TurnierTyp.RANDORI;
	}
}
