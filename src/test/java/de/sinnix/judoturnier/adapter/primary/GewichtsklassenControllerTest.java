package de.sinnix.judoturnier.adapter.primary;

import de.sinnix.judoturnier.application.EinstellungenService;
import de.sinnix.judoturnier.application.GewichtsklassenService;
import de.sinnix.judoturnier.application.WettkaempferService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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