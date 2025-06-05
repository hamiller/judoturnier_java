package de.sinnix.judoturnier.adapter.secondary;

import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.WettkampfGruppe;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class WettkampfGruppeConverter {

	public WettkampfGruppe convertToWettkampfGruppe(WettkampfGruppeJpa jpa) {
		return new WettkampfGruppe(jpa.getId(), jpa.getName(), jpa.getTyp(), Altersklasse.valueOf(jpa.getAltersklasse()), jpa.getTurnierUUID());
	}

	public WettkampfGruppeJpa convertFromWettkampfGruppe(WettkampfGruppe wettkampfGruppe) {
		WettkampfGruppeJpa jpa = new WettkampfGruppeJpa();
		jpa.setName(wettkampfGruppe.name());
		jpa.setTyp(wettkampfGruppe.typ());
		jpa.setTurnierUUID(wettkampfGruppe.turnierUUID());
		jpa.setAltersklasse(wettkampfGruppe.altersklasse().name());
		return jpa;
	}
}
