package de.sinnix.judoturnier.adapter.secondary;

import de.sinnix.judoturnier.model.WettkampfGruppe;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class WettkampfGruppeConverter {

	public WettkampfGruppe convertToWettkampfGruppe(Integer id, List<WettkampfGruppeJpa> jpaList) {
		WettkampfGruppeJpa jpa = jpaList.stream().filter(wkg -> wkg.getId().equals(id)).findFirst().orElseThrow();
		return new WettkampfGruppe(jpa.getId(), jpa.getName(), jpa.getTyp(), List.of(), UUID.fromString(jpa.getTurnierUUID()));
	}

	public WettkampfGruppeJpa convertFromWettkampfGruppe(WettkampfGruppe wettkampfGruppe) {
		WettkampfGruppeJpa jpa = new WettkampfGruppeJpa();
		jpa.setId(wettkampfGruppe.id());
		jpa.setName(wettkampfGruppe.name());
		jpa.setTyp(wettkampfGruppe.typ());
		jpa.setTurnierUUID(wettkampfGruppe.turnierUUID().toString());
		return jpa;
	}
}
