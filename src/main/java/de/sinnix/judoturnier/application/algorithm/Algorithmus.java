package de.sinnix.judoturnier.application.algorithm;

import de.sinnix.judoturnier.model.GewichtsklassenGruppe;
import de.sinnix.judoturnier.model.WettkampfGruppe;

import java.util.List;
import java.util.UUID;

public interface Algorithmus {

	List<WettkampfGruppe> erstelleWettkampfGruppen(Integer gruppenid, GewichtsklassenGruppe gewichtsklassenGruppe, Integer mattenAnzahl, UUID turnierUUID);
}
