package de.sinnix.judoturnier.adapter.secondary.converter;

import de.sinnix.judoturnier.adapter.secondary.jpa.WettkampfGruppeJpa;
import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.WettkampfGruppe;
import org.springframework.stereotype.Component;

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
