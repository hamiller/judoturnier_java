package de.sinnix.judoturnier.adapter.secondary;

import de.sinnix.judoturnier.model.Bewerter;
import org.springframework.stereotype.Component;

@Component
public class BewerterConverter {
	public Bewerter convertToBewerter(BewerterJpa jpa) {
		if (jpa == null) {
			return null;
		}
		return new Bewerter(jpa.getUuid(), jpa.getUsername(), jpa.getName());
	}

	public BewerterJpa convertFromBewerter(Bewerter bewerter) {
		if (bewerter == null) {
			return null;
		}
		BewerterJpa jpa = new BewerterJpa();
		jpa.setUuid(bewerter.id());
		jpa.setUsername(bewerter.username());
		jpa.setName(bewerter.name());
		return jpa;
	}
}
