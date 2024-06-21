package de.sinnix.judoturnier.model;

import java.util.List;

public class GewichtsklasseConfig {
	List<GeschlechtAltersklasse> geschlechter;

	GewichtsklasseConfig(List<GeschlechtAltersklasse> geschlechter) {
		this.geschlechter = geschlechter;
	}
}
