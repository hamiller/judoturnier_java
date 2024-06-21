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

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GewichtsklassenControllerTest {

	@Mock
	private WettkaempferService    wettkaempferService;
	@Mock
	private GewichtsklassenService gewichtsklassenService;
	@Mock
	private EinstellungenService einstellungenService;

	@InjectMocks
	private GewichtsklassenController gewichtsklassenController;

	@Test
	void speichereGewichtsklassen() {
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		formData.add("gruppen_teilnehmer","gruppe_47_teilnehmer_74");
		formData.add("gruppen_teilnehmer","gruppe_47_teilnehmer_81");
		formData.add("gruppen_teilnehmer","gruppe_56_teilnehmer_88");

		Map<Integer, List<Integer>> gruppenTeilnehmer = new HashMap<>();
		gruppenTeilnehmer.put(47, List.of(74, 81));
		gruppenTeilnehmer.put(56, List.of(88));

		gewichtsklassenController.speichereGewichtsklassen(formData);

		verify(gewichtsklassenService, times(1)).aktualisiere(gruppenTeilnehmer);
	}
}