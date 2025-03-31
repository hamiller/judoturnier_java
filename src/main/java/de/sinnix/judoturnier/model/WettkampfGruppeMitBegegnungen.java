package de.sinnix.judoturnier.model;

import java.util.List;

public record WettkampfGruppeMitBegegnungen(WettkampfGruppe gruppe, List<BegegnungenJeRunde> alleRundenBegegnungen) {
}
