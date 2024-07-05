package de.sinnix.judoturnier.application;

import de.sinnix.judoturnier.adapter.secondary.EinstellungJpa;
import de.sinnix.judoturnier.adapter.secondary.EinstellungJpaRepository;
import de.sinnix.judoturnier.model.Einstellungen;
import de.sinnix.judoturnier.model.MattenAnzahl;
import de.sinnix.judoturnier.model.RandoriGruppengroesse;
import de.sinnix.judoturnier.model.TurnierTyp;
import de.sinnix.judoturnier.model.VariablerGewichtsteil;
import de.sinnix.judoturnier.model.WettkampfReihenfolge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EinstellungenService {
	private static final Logger logger = LogManager.getLogger(EinstellungenService.class);

	@Autowired
	private EinstellungJpaRepository einstellungJpaRepository;

	private static final TurnierTyp            DEFAULT_TURNIERTYP             = TurnierTyp.RANDORI;
	private static final MattenAnzahl          DEFAULT_MATTENANZAHL           = new MattenAnzahl(2);
	private static final WettkampfReihenfolge  DEFAULT_WETTKAMPFREIHENFOLGE   = WettkampfReihenfolge.ABWECHSELND;
	private static final RandoriGruppengroesse DEFAULT_RANDORIGRUPPENGROESSE  = new RandoriGruppengroesse(6);
	private static final VariablerGewichtsteil DEFAULT_VARIABLER_GEWICHTSTEIL = new VariablerGewichtsteil(0.2);

	public Einstellungen ladeEinstellungen() {
		logger.info("EinstellungenService ladeEinstellungen()");
		List<EinstellungJpa> einstellungenList = einstellungJpaRepository.findAll();
		TurnierTyp turnierTyp = einstellungenList.stream().filter(e -> e.getArt().equalsIgnoreCase(TurnierTyp.TYP)).findFirst().map(t -> TurnierTyp.valueOf(t.getWert())).orElse(DEFAULT_TURNIERTYP);
		MattenAnzahl mattenAnzahl = einstellungenList.stream().filter(e -> e.getArt().equalsIgnoreCase(MattenAnzahl.TYP)).findFirst().map(t -> new MattenAnzahl(Integer.parseInt(t.getWert()))).orElseGet(() -> DEFAULT_MATTENANZAHL);
		WettkampfReihenfolge wettkampfReihenfolge = einstellungenList.stream().filter(e -> e.getArt().equalsIgnoreCase(WettkampfReihenfolge.TYP)).findFirst().map(t -> WettkampfReihenfolge.valueOf(t.getWert())).orElseGet(() -> DEFAULT_WETTKAMPFREIHENFOLGE);
		RandoriGruppengroesse randoriGruppengroesse = einstellungenList.stream().filter(e -> e.getArt().equalsIgnoreCase(RandoriGruppengroesse.TYP)).findFirst().map(r -> new RandoriGruppengroesse(Integer.parseInt(r.getWert()))).orElseGet(() -> DEFAULT_RANDORIGRUPPENGROESSE);
		VariablerGewichtsteil variablerGewichtsteil = einstellungenList.stream().filter(e -> e.getArt().equalsIgnoreCase(VariablerGewichtsteil.TYP)).findFirst().map(v -> new VariablerGewichtsteil(Double.parseDouble(v.getWert()))).orElseGet(() -> DEFAULT_VARIABLER_GEWICHTSTEIL);

		return new Einstellungen(turnierTyp, mattenAnzahl, wettkampfReihenfolge, randoriGruppengroesse, variablerGewichtsteil);
	}

	public Einstellungen speichereTurnierEinstellungen(Einstellungen einstellungen) {
		logger.info("EinstellungenService speichereTurnierEinstellungen()");
		List<EinstellungJpa> jpaList = List.of(
			new EinstellungJpa(einstellungen.turnierTyp().TYP, einstellungen.turnierTyp().name()),
			new EinstellungJpa(einstellungen.mattenAnzahl().TYP, einstellungen.mattenAnzahl().anzahl().toString()),
			new EinstellungJpa(einstellungen.wettkampfReihenfolge().TYP, einstellungen.wettkampfReihenfolge().name()),
			new EinstellungJpa(einstellungen.randoriGruppengroesse().TYP, einstellungen.randoriGruppengroesse().anzahl().toString()),
			new EinstellungJpa(einstellungen.variablerGewichtsteil().TYP, einstellungen.variablerGewichtsteil().variablerTeil().toString())
			);
		einstellungJpaRepository.saveAll(jpaList);
		return ladeEinstellungen();
	}

	public boolean isRandori() {
		TurnierTyp turnierTyp = einstellungJpaRepository.findById(TurnierTyp.TYP).map(typ -> TurnierTyp.valueOf(typ.getWert())).orElseThrow();
		return turnierTyp == TurnierTyp.RANDORI;
	}
}
