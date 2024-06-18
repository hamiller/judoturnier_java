package de.sinnix.judoturnier.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public record Verein(@Id @GeneratedValue(strategy = GenerationType.AUTO) String id, String name) {
}
