package de.sinnix.judoturnier.adapter.secondary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


import de.sinnix.judoturnier.adapter.secondary.converter.WertungConverter;
import de.sinnix.judoturnier.adapter.secondary.jpa.BegegnungJpa;
import de.sinnix.judoturnier.adapter.secondary.jpa.BenutzerJpa;
import de.sinnix.judoturnier.adapter.secondary.jpa.TurnierRollenJpa;
import de.sinnix.judoturnier.adapter.secondary.jpa.WertungJpa;
import de.sinnix.judoturnier.model.Benutzer;
import de.sinnix.judoturnier.model.BenutzerRolle;
import de.sinnix.judoturnier.model.TurnierRollen;
import de.sinnix.judoturnier.model.Wertung;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WertungRepositoryTest {

	@Mock
	private BegegnungJpaRepository begegnungJpaRepository;
	@Mock
	private BenutzerJpaRepository benutzerJpaRepository;
	@Mock
	private WertungConverter      wertungConverter;
	@InjectMocks
	private WertungRepository     wertungRepository;

	@Test
	void neueRandoriWertung() {
		UUID turnierUUID = UUID.randomUUID();
		UUID begegnungId = UUID.randomUUID();
		List<BenutzerRolle> rollen = List.of(BenutzerRolle.KAMPFRICHTER);
		Benutzer kampfrichter = new Benutzer(UUID.randomUUID(), "user1", "Name, Vorname", List.of(new TurnierRollen(UUID.randomUUID(), turnierUUID, rollen)), rollen);
		BenutzerJpa kampfrichterJpa = new BenutzerJpa(kampfrichter.username(), kampfrichter.name(), List.of(new TurnierRollenJpa(null, rollen, turnierUUID)), rollen);
		Wertung wertung = new Wertung(null, null, null, null, null, null, null,
			1, 2, 3, 4, 4, 3, 2, 1,
			kampfrichter
		);
		WertungJpa wertungJpa = new WertungJpa();
		wertungJpa.setKampfgeistWettkaempfer1(wertung.getKampfgeistWettkaempfer1());
		wertungJpa.setTechnikWettkaempfer1(wertung.getTechnikWettkaempfer1());
		wertungJpa.setKampfstilWettkaempfer1(wertung.getKampfstilWettkaempfer1());
		wertungJpa.setVielfaltWettkaempfer1(wertung.getVielfaltWettkaempfer1());
		wertungJpa.setKampfgeistWettkaempfer2(wertung.getKampfgeistWettkaempfer2());
		wertungJpa.setTechnikWettkaempfer2(wertung.getTechnikWettkaempfer2());
		wertungJpa.setKampfstilWettkaempfer2(wertung.getKampfstilWettkaempfer2());
		wertungJpa.setVielfaltWettkaempfer2(wertung.getVielfaltWettkaempfer2());
		BegegnungJpa begegnungJpa = new BegegnungJpa();
		begegnungJpa.setWertungen(new ArrayList<>());

		when(begegnungJpaRepository.findById(begegnungId)).thenReturn(Optional.of(begegnungJpa));
		when(benutzerJpaRepository.findById(wertung.getBewerter().uuid())).thenReturn(Optional.of(kampfrichterJpa));
		when(wertungConverter.convertFromWertung(wertung)).thenReturn(wertungJpa);


		wertungRepository.speichereWertungInBegegnung(wertung, begegnungId);

		BegegnungJpa neueJpa = new BegegnungJpa();
		neueJpa.setWertungen(Arrays.asList(wertungJpa));
		verify(begegnungJpaRepository, times(1)).save(neueJpa);

	}

	@Test
	void aendereRandoriWertung() {
		UUID turnierUUID = UUID.randomUUID();
		UUID begegnungId = UUID.randomUUID();
		List<BenutzerRolle> rollen = List.of(BenutzerRolle.KAMPFRICHTER);
		Benutzer kampfrichter = new Benutzer(UUID.randomUUID(), "user1", "Name, Vorname", List.of(new TurnierRollen(UUID.randomUUID(), turnierUUID, rollen)), rollen);
		BenutzerJpa kampfrichterJpa = new BenutzerJpa(kampfrichter.username(), kampfrichter.name(), List.of(new TurnierRollenJpa(null, rollen, turnierUUID)), rollen);
		kampfrichterJpa.setId(UUID.randomUUID());

		WertungJpa wertungJpa = new WertungJpa();
		wertungJpa.setKampfgeistWettkaempfer1(1);
		wertungJpa.setTechnikWettkaempfer1(1);
		wertungJpa.setKampfstilWettkaempfer1(1);
		wertungJpa.setVielfaltWettkaempfer1(1);
		wertungJpa.setKampfgeistWettkaempfer2(1);
		wertungJpa.setTechnikWettkaempfer2(1);
		wertungJpa.setKampfstilWettkaempfer2(1);
		wertungJpa.setVielfaltWettkaempfer2(1);
		wertungJpa.setBenutzer(kampfrichterJpa);
		BegegnungJpa begegnungJpa = new BegegnungJpa();
		begegnungJpa.setWertungen(Arrays.asList(wertungJpa));

		Wertung geaenderteWertung = new Wertung(null, null, null, null, null, null, null,
			1, 2, 3, 4, 4, 3, 2, 1,
			kampfrichter
		);
		WertungJpa geanderteWertungJpa = new WertungJpa();
		geanderteWertungJpa.setBenutzer(kampfrichterJpa);
		geanderteWertungJpa.setKampfgeistWettkaempfer1(geaenderteWertung.getKampfgeistWettkaempfer1());
		geanderteWertungJpa.setTechnikWettkaempfer1(geaenderteWertung.getTechnikWettkaempfer1());
		geanderteWertungJpa.setKampfstilWettkaempfer1(geaenderteWertung.getKampfstilWettkaempfer1());
		geanderteWertungJpa.setVielfaltWettkaempfer1(geaenderteWertung.getVielfaltWettkaempfer1());
		geanderteWertungJpa.setKampfgeistWettkaempfer2(geaenderteWertung.getKampfgeistWettkaempfer2());
		geanderteWertungJpa.setTechnikWettkaempfer2(1);
		geanderteWertungJpa.setKampfstilWettkaempfer2(1);
		geanderteWertungJpa.setVielfaltWettkaempfer2(1);

		when(begegnungJpaRepository.findById(begegnungId)).thenReturn(Optional.of(begegnungJpa));
		when(benutzerJpaRepository.findById(geaenderteWertung.getBewerter().uuid())).thenReturn(Optional.of(kampfrichterJpa));
		when(wertungConverter.convertFromWertung(geaenderteWertung)).thenReturn(geanderteWertungJpa);


		wertungRepository.speichereWertungInBegegnung(geaenderteWertung, begegnungId);

		BegegnungJpa neueJpa = new BegegnungJpa();
		neueJpa.setWertungen(Arrays.asList(geanderteWertungJpa));
		verify(begegnungJpaRepository, times(1)).save(neueJpa);
	}

	@Test
	void weitereRandoriWertung() {
		UUID turnierUUID = UUID.randomUUID();
		UUID begegnungId = UUID.randomUUID();
		List<BenutzerRolle> rollen = List.of(BenutzerRolle.KAMPFRICHTER);
		Benutzer kampfrichter = new Benutzer(UUID.randomUUID(), "user1", "Name, Vorname", List.of(new TurnierRollen(UUID.randomUUID(), turnierUUID, rollen)), rollen);
		BenutzerJpa kampfrichterJpa = new BenutzerJpa(kampfrichter.username(), kampfrichter.name(), List.of(new TurnierRollenJpa(null, rollen, turnierUUID)), rollen);
		kampfrichterJpa.setId(UUID.randomUUID());
		WertungJpa wertungJpa = new WertungJpa();
		wertungJpa.setKampfgeistWettkaempfer1(1);
		wertungJpa.setTechnikWettkaempfer1(1);
		wertungJpa.setKampfstilWettkaempfer1(1);
		wertungJpa.setVielfaltWettkaempfer1(1);
		wertungJpa.setKampfgeistWettkaempfer2(1);
		wertungJpa.setTechnikWettkaempfer2(1);
		wertungJpa.setKampfstilWettkaempfer2(1);
		wertungJpa.setVielfaltWettkaempfer2(1);
		wertungJpa.setBenutzer(kampfrichterJpa);
		BegegnungJpa begegnungJpa = new BegegnungJpa();
		begegnungJpa.setWertungen(new ArrayList<>(Arrays.asList(wertungJpa)));

		Benutzer kampfrichter2 = new Benutzer(UUID.randomUUID(), "user2", "Name, Vorname", List.of(new TurnierRollen(UUID.randomUUID(), turnierUUID, rollen)), rollen);
		BenutzerJpa kampfrichterJpa2 = new BenutzerJpa(kampfrichter2.username(), kampfrichter2.name(), List.of(new TurnierRollenJpa(null, rollen, turnierUUID)), rollen);
		kampfrichterJpa2.setId(UUID.randomUUID());
		Wertung weitereWertung = new Wertung(null, null, null, null, null, null, null,
			1, 2, 3, 4, 4, 3, 2, 1,
			kampfrichter2
		);
		WertungJpa weitereWertungJpa = new WertungJpa();
		weitereWertungJpa.setKampfgeistWettkaempfer1(weitereWertung.getKampfgeistWettkaempfer1());
		weitereWertungJpa.setTechnikWettkaempfer1(weitereWertung.getTechnikWettkaempfer1());
		weitereWertungJpa.setKampfstilWettkaempfer1(weitereWertung.getKampfstilWettkaempfer1());
		weitereWertungJpa.setVielfaltWettkaempfer1(weitereWertung.getVielfaltWettkaempfer1());
		weitereWertungJpa.setKampfgeistWettkaempfer2(weitereWertung.getKampfgeistWettkaempfer2());
		weitereWertungJpa.setTechnikWettkaempfer2(1);
		weitereWertungJpa.setKampfstilWettkaempfer2(1);
		weitereWertungJpa.setVielfaltWettkaempfer2(1);
		weitereWertungJpa.setBenutzer(kampfrichterJpa2);

		when(begegnungJpaRepository.findById(begegnungId)).thenReturn(Optional.of(begegnungJpa));
		when(benutzerJpaRepository.findById(weitereWertung.getBewerter().uuid())).thenReturn(Optional.of(kampfrichterJpa2));
		when(wertungConverter.convertFromWertung(weitereWertung)).thenReturn(weitereWertungJpa);


		wertungRepository.speichereWertungInBegegnung(weitereWertung, begegnungId);

		BegegnungJpa neueJpa = new BegegnungJpa();
		neueJpa.setWertungen(Arrays.asList(wertungJpa, weitereWertungJpa));
		verify(begegnungJpaRepository, times(1)).save(neueJpa);
	}

	@Test
	@Disabled
	void neueturnierWertung() {

	}

	@Test
	@Disabled
	void aendereTurnierWertung() {

	}

	@Test
	@Disabled
	void weitereTurnierWertung() {
		// muss bisherige Turnierwertung ersetzen

	}
}
