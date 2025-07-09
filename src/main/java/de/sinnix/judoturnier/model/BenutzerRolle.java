package de.sinnix.judoturnier.model;

public enum BenutzerRolle {
	KAMPFRICHTER, ADMINISTRATOR, BEOBACHTER, TRAINER;

	public static BenutzerRolle fromString(String authority) {
		switch (authority) {
			case "ROLE_KAMPFRICHTER": return BenutzerRolle.KAMPFRICHTER;
			case "ROLE_ADMIN": return BenutzerRolle.ADMINISTRATOR;
			case "ROLE_ZUSCHAUER": return BenutzerRolle.BEOBACHTER;
			case "ROLE_TRAINER": return BenutzerRolle.TRAINER;
			default: return null;
		}
	}
}
