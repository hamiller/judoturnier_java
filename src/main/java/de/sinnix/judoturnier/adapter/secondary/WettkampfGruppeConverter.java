package de.sinnix.judoturnier.adapter.secondary;

import de.sinnix.judoturnier.model.WettkampfGruppe;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WettkampfGruppeConverter {

	public WettkampfGruppe convertToWettkampfGruppe(WettkampfGruppeJpa jpa) {
		return new WettkampfGruppe(jpa.getId(), jpa.getName(), jpa.getTyp(), List.of());
	}

	public WettkampfGruppeJpa convertFromWettkampfGruppe(WettkampfGruppe wettkampfGruppe) {
		WettkampfGruppeJpa jpa = new WettkampfGruppeJpa();
		jpa.setId(wettkampfGruppe.id());
		jpa.setName(wettkampfGruppe.name());
		jpa.setTyp(wettkampfGruppe.typ());
		return jpa;
	}
}