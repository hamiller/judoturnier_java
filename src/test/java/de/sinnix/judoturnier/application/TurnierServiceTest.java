package de.sinnix.judoturnier.application;

import de.sinnix.judoturnier.adapter.secondary.TurnierRepository;
import de.sinnix.judoturnier.fixtures.GewichtsklassenGruppeFixture;
import de.sinnix.judoturnier.fixtures.WettkampfgruppeFixture;
import de.sinnix.judoturnier.model.Einstellungen;
import de.sinnix.judoturnier.model.GewichtsklassenGruppe;
import de.sinnix.judoturnier.model.Matte;
import de.sinnix.judoturnier.model.MattenAnzahl;
import de.sinnix.judoturnier.model.RandoriGruppengroesse;
import de.sinnix.judoturnier.model.Runde;
import de.sinnix.judoturnier.model.TurnierTyp;
import de.sinnix.judoturnier.model.VariablerGewichtsteil;
import de.sinnix.judoturnier.model.WettkampfGruppe;
import de.sinnix.judoturnier.model.WettkampfReihenfolge;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TurnierServiceTest {
	@Mock
	private TurnierRepository      turnierRepository;
	@Mock
	private EinstellungenService   einstellungenService;
	@Mock
	private GewichtsklassenService gewichtsklassenService;

	@Spy
	private Sortierer              sortierer = new Sortierer();

	@InjectMocks
	private TurnierService turnierService;

	@Test
	void erstelleWettkampfreihenfolgeAltersklasse() {
		List<GewichtsklassenGruppe> gewichtsklassenGruppen = GewichtsklassenGruppeFixture.gewichtsklassenGruppen;
		Einstellungen einstellungen = new Einstellungen(TurnierTyp.RANDORI, new MattenAnzahl(2), WettkampfReihenfolge.ABWECHSELND, new RandoriGruppengroesse(6), new VariablerGewichtsteil(0.2));

		when(einstellungenService.ladeEinstellungen()).thenReturn(einstellungen);
		when(gewichtsklassenService.ladeGewichtsklassenGruppen()).thenReturn(gewichtsklassenGruppen);

		turnierService.erstelleWettkampfreihenfolgeAltersklasse(Optional.empty());

		assertEquals(5, gewichtsklassenGruppen.size());
		assertEquals(25, gewichtsklassenGruppen.stream().mapToInt(g -> g.teilnehmer().size()).sum()); // Anzahl Teilnehmer insgesamt
		// ArgumentCaptor verwenden
		ArgumentCaptor<List<Matte>> argumentCaptor = ArgumentCaptor.forClass(List.class);
		verify(turnierRepository, times(1)).speichereMatten(argumentCaptor.capture());
		// Verifizieren
		List<Matte> matten = argumentCaptor.getValue();
		assertEquals(2, matten.size());
		int anzahlBegegnungen = matten.stream().mapToInt(m -> m.runden().stream().mapToInt(r -> r.begegnungen().size()).sum()).sum();
		int berechneteBegegnungen = 0;
		// da Jeder-gegen-Jeden nur je Gruppe gilt, muss die Anzahl für jede Gruppe separat geprüft werden
		for (GewichtsklassenGruppe gruppe: gewichtsklassenGruppen) {
			var n = gruppe.teilnehmer().size();
			var N = (n * (n - 1)) / 2; // Berechnete Begegnungen in dieser Gruppe
			berechneteBegegnungen += N;
		}
		assertEquals(berechneteBegegnungen, anzahlBegegnungen);
	}
}