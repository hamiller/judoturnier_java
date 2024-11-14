package de.sinnix.judoturnier.model;

public enum BenutzerRolle {
	KAMPFRICHTER, ADMINISTRATOR, BEOBACHTER;

	public static BenutzerRolle fromString(String authority) {
		switch (authority) {
			case "ROLE_KAMPFRICHTER": return BenutzerRolle.KAMPFRICHTER;
			case "ROLE_ADMIN": return BenutzerRolle.ADMINISTRATOR;
			case "ROLE_ZUSCHAUER": return BenutzerRolle.BEOBACHTER;
			default: return null;
		}
	}
}
