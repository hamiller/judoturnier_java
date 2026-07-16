package de.sinnix.judoturnier.adapter.primary;

import de.sinnix.judoturnier.application.EinstellungenService;
import de.sinnix.judoturnier.application.GewichtsklassenService;
import de.sinnix.judoturnier.application.WettkaempferService;
import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Einstellungen;
import de.sinnix.judoturnier.model.Geschlecht;
import de.sinnix.judoturnier.model.GewichtsklassenGruppe;
import de.sinnix.judoturnier.model.GewichtsklassenGruppen;
import de.sinnix.judoturnier.model.Gruppengroessen;
import de.sinnix.judoturnier.model.MattenAnzahl;
import de.sinnix.judoturnier.model.RandoriGruppenName;
import de.sinnix.judoturnier.model.SeparateAlterklassen;
import de.sinnix.judoturnier.model.TurnierTyp;
import de.sinnix.judoturnier.model.VariablerGewichtsteil;
import de.sinnix.judoturnier.model.Verein;
import de.sinnix.judoturnier.model.Wettkaempfer;
import de.sinnix.judoturnier.model.WettkampfReihenfolge;
import de.sinnix.judoturnier.model.Wettkampfzeiten;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GewichtsklassenControllerTest {

	@Mock
	private WettkaempferService    wettkaempferService;
	@Mock
	private GewichtsklassenService gewichtsklassenService;
	@Mock
	private EinstellungenService   einstellungenService;

	@InjectMocks
	private GewichtsklassenController gewichtsklassenController;

	@AfterEach
	void clearSecurityContext() {
		SecurityContextHolder.clearContext();
	}

	@Test
	@SuppressWarnings("unchecked")
	void ladeGewichtsklassenSortiertTeilnehmerInnerhalbDerGruppenNachGewicht() {
		UUID turnierUUID = UUID.randomUUID();
		Verein verein = new Verein(UUID.randomUUID(), "Verein", turnierUUID);
		Wettkaempfer schwer = new Wettkaempfer(UUID.randomUUID(), "Schwer", Geschlecht.m, Altersklasse.U11, verein, 42.0, Optional.empty(), true, false, turnierUUID);
		Wettkaempfer leicht = new Wettkaempfer(UUID.randomUUID(), "Leicht", Geschlecht.m, Altersklasse.U11, verein, 24.0, Optional.empty(), true, false, turnierUUID);
		Wettkaempfer mittel = new Wettkaempfer(UUID.randomUUID(), "Mittel", Geschlecht.m, Altersklasse.U11, verein, 31.0, Optional.empty(), true, false, turnierUUID);
		GewichtsklassenGruppe gruppe = new GewichtsklassenGruppe(
			UUID.randomUUID(),
			Altersklasse.U11,
			Optional.of(Geschlecht.m),
			List.of(schwer, leicht, mittel),
			Optional.of(RandoriGruppenName.Adler),
			24.0,
			42.0,
			turnierUUID
		);

		SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken("test", "test"));
		when(wettkaempferService.alleKaempfer(turnierUUID)).thenReturn(List.of(schwer, leicht, mittel));
		when(gewichtsklassenService.ladeGewichtsklassenGruppen(turnierUUID)).thenReturn(List.of(gruppe));
		when(einstellungenService.ladeEinstellungen(turnierUUID)).thenReturn(new Einstellungen(
			TurnierTyp.STANDARD,
			new MattenAnzahl(1),
			WettkampfReihenfolge.ABWECHSELND,
			new Gruppengroessen(Map.of(Altersklasse.U11, 6)),
			new VariablerGewichtsteil(0.2),
			SeparateAlterklassen.ZUSAMMEN,
			new Wettkampfzeiten(Map.of()),
			turnierUUID
		));

		ModelAndView modelAndView = gewichtsklassenController.ladeGewichtsklassen(turnierUUID.toString());

		List<GewichtsklassenGruppen> gruppenNachGeschlecht = (List<GewichtsklassenGruppen>) modelAndView.getModel().get("gewichtsklassengruppenMaennlich");
		List<Wettkaempfer> teilnehmer = gruppenNachGeschlecht.getFirst().gruppen().getFirst().teilnehmer();
		assertEquals(List.of(leicht, mittel, schwer), teilnehmer);
	}

	@Test
	void speichereGewichtsklassen() {
		UUID turnierUUID = UUID.randomUUID();
		UUID g1 = UUID.fromString("47111111-1111-1111-1111-111111111111");
		UUID g2 = UUID.fromString("56111111-1111-1111-1111-111111111111");
		UUID wk1 = UUID.fromString("11111111-1111-1111-1111-111111111174");
		UUID wk2 = UUID.fromString("11111111-1111-1111-1111-111111111181");
		UUID wk3 = UUID.fromString("11111111-1111-1111-1111-111111111188");
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		formData.add("gruppen_teilnehmer", "gruppe_" + g1.toString() + "_teilnehmer_" + wk1.toString());
		formData.add("gruppen_teilnehmer", "gruppe_" + g1.toString() + "_teilnehmer_" + wk2.toString());
		formData.add("gruppen_teilnehmer", "gruppe_" + g2.toString() + "_teilnehmer_" + wk3.toString());

		Map<UUID, List<UUID>> gruppenTeilnehmer = new HashMap<>();
		gruppenTeilnehmer.put(g1, List.of(wk1, wk2));
		gruppenTeilnehmer.put(g2, List.of(wk3));

		gewichtsklassenController.speichereGewichtsklassen(turnierUUID.toString(), formData);

		verify(gewichtsklassenService, times(1)).aktualisiere(gruppenTeilnehmer, turnierUUID);
	}

	@Test
	void testRegex() {
		String input = "gruppe_47111111-1111-1111-1111-111111111111_teilnehmer_11111111-1111-1111-1111-111111111174";

		Pattern pattern = Pattern.compile("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}");
		Matcher matcher = pattern.matcher(input);

		while (matcher.find()) {
			System.out.println(matcher.group());
		}
		System.out.println("Fertig");
	}
}
