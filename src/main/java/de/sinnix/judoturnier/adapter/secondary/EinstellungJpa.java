package de.sinnix.judoturnier.adapter.secondary;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity(name = "einstellungen")
public class EinstellungJpa {
    @Id String art;
    String wert;

    public String art() { return art;}
    public String wert() { return wert;}
}
