package de.sinnix.judoturnier.adapter.secondary;

import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Geschlecht;
import de.sinnix.judoturnier.model.GewichtsklassenGruppe;
import de.sinnix.judoturnier.model.RandoriGruppenName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GewichtsklassenConverter {

	@Autowired
	private WettkaempferConverter wettkaempferConverter;

	public GewichtsklassenGruppe convertToGewichtsklassen(GewichtsklassenJpa jpa) {
		return new GewichtsklassenGruppe(
			jpa.getId(),
			getAltersklasse(jpa.getAltersklasse()),
			getGeschlecht(jpa.getGruppengeschlecht()),
			wettkaempferConverter.convertToWettkaempferList(jpa.getTeilnehmer()),
			getRandoriGruppenName(jpa.getName()),
			jpa.getMingewicht(),
			jpa.getMaxgewicht());
	}

	public GewichtsklassenJpa convertFromGewichtsklassen(GewichtsklassenGruppe gwk) {
		GewichtsklassenJpa jpa = new GewichtsklassenJpa();
		if (gwk.id() != null) jpa.setId(gwk.id());
		jpa.setName(gwk.name().map(n -> n.name()).orElse(""));
		jpa.setAltersklasse(gwk.altersKlasse().name());
		jpa.setMingewicht(gwk.minGewicht());
		jpa.setMaxgewicht(gwk.maxGewicht());
		jpa.setGruppengeschlecht(gwk.gruppenGeschlecht().map(g -> g.name()).orElse(null));
		jpa.setTeilnehmer(wettkaempferConverter.convertFromWettkaempferList(gwk.teilnehmer()));
		return jpa;
	}

	private static Altersklasse getAltersklasse(String altersklasse) {
		return (altersklasse != null && !altersklasse.isBlank()) ? Altersklasse.valueOf(altersklasse) : null;
	}

	private static Optional<Geschlecht> getGeschlecht(String geschlecht) {
		return (geschlecht != null && !geschlecht.isBlank()) ? Optional.of(Geschlecht.valueOf(geschlecht)) : Optional.empty();
	}

	private static Optional<RandoriGruppenName> getRandoriGruppenName(String name) {
		return (name != null && !name.isBlank()) ? Optional.of(RandoriGruppenName.valueOf(name)) : Optional.empty();
	}
}