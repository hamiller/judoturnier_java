package de.sinnix.judoturnier.adapter.primary;

import de.sinnix.judoturnier.fixtures.WettkaempferFixtures;
import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Begegnung;
import de.sinnix.judoturnier.model.Verein;
import de.sinnix.judoturnier.model.Wettkaempfer;
import de.sinnix.judoturnier.model.WettkampfGruppe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class TurnierControllerTest {
	@InjectMocks
	private TurnierController turnierController;

	private UUID                  turnierUuid;
	private Verein                verein;
	private WettkampfGruppe       gruppeA;
	private WettkampfGruppe       gruppeB;
	private List<GruppenRundeDto> gruppenRunden;

	@BeforeEach
	void setUp() {
		turnierUuid = UUID.fromString("18ebc967-8605-43de-9a15-89f26cb005cb");
		verein = new Verein(
			UUID.fromString("cd299fb0-757b-4a42-9c59-5cc45a21ec32"),
			"1. JC", UUID.fromString("18ebc967-8605-43de-9a15-89f26cb005cb")
		);
		gruppeA = new WettkampfGruppe(UUID.fromString("68be793d-f8ac-4b97-a40f-637e99da3cc4"), "Ameise", "(58.0-62.0 Frauen)", Altersklasse.Frauen, List.of(), turnierUuid);
		gruppeB = new WettkampfGruppe(UUID.fromString("c8ca628a-4de5-410a-9a31-81acf806b600"), "Giraffe", "(0.0-200.0 Frauen)", Altersklasse.Frauen, List.of(), turnierUuid);

		gruppenRunden = new ArrayList<>();

		GruppenRundeDto gruppenRundeDto1 = new GruppenRundeDto(List.of(
			new RundeDto(
				UUID.fromString("3473c2e8-3a51-4b0b-a7cb-9ffe45ecc806"), 1, 1, 3, 2, Altersklasse.Frauen, gruppeA, List.of(
				new BegegnungDto(
					"a7e4caf0-a14f-49ac-b152-8b9b74bc50ae",
					Begegnung.RundenTyp.GEWINNERRUNDE, 1, 1,
					WettkaempferFixtures.wettkaempferin1,
					randomEmtpyWk(),
					Optional.empty(), List.of(), null, null
				),
				new BegegnungDto(
					"1504e18a-56b5-48ff-9f9c-001dee76eb10",
					Begegnung.RundenTyp.GEWINNERRUNDE, 1, 2,
					WettkaempferFixtures.wettkaempferin2,
					WettkaempferFixtures.wettkaempferin3,
					Optional.empty(), List.of(), null, null
				),
				new BegegnungDto(
					"969c5f1d-5bbc-4e1e-bd9d-3d8ad521e8cf",
					Begegnung.RundenTyp.GEWINNERRUNDE, 1, 3,
					WettkaempferFixtures.wettkaempferin4,
					WettkaempferFixtures.wettkaempferin5,
					Optional.empty(), List.of(), null, null
				),
				new BegegnungDto(
					"4d455f47-4575-4de6-bb34-d34faf6d175c",
					Begegnung.RundenTyp.GEWINNERRUNDE, 1, 4,
					WettkaempferFixtures.wettkaempferin6,
					WettkaempferFixtures.wettkaempferin7,
					Optional.empty(), List.of(), null, null
				),
				new BegegnungDto(
					"13b0dde9-0806-42e8-99bd-e455f58b2f11",
					Begegnung.RundenTyp.TROSTRUNDE, 1, 1,
					randomEmtpyWk(),
					randomEmtpyWk(),
					Optional.empty(), List.of(), null, null
				),
				new BegegnungDto(
					"13b0dde9-0806-42e8-99bd-e455f58b2f11",
					Begegnung.RundenTyp.TROSTRUNDE, 1, 2,
					randomEmtpyWk(),
					randomEmtpyWk(),
					Optional.empty(), List.of(), null, null
				)
			)
			)
		));
		GruppenRundeDto gruppenRundeDto2 = new GruppenRundeDto(List.of(
			new RundeDto(UUID.fromString("0580b988-6d0f-4e62-b585-455f09334c44"), 2, 1, 4, 2, Altersklasse.PAUSE, gruppeB, List.of(
				new BegegnungDto(
					"9d163f1f-016a-4981-9450-7801f765ad32",
					Begegnung.RundenTyp.GEWINNERRUNDE, 2, 1,
					WettkaempferFixtures.wettkaempferin8,
					WettkaempferFixtures.wettkaempferin9,
					Optional.empty(), List.of(), null, null
				)
			)
			)
		));
		GruppenRundeDto gruppenRundeDto3 = new GruppenRundeDto(List.of(
			new RundeDto(UUID.fromString("85b46c3d-7223-4fe5-8e58-dd48dc770b9d"), 3, 2, 5, 2, Altersklasse.PAUSE, gruppeA, List.of(
				new BegegnungDto(
					"26af7aa5-ea24-4af4-a4b6-b6327f1171ee",
					Begegnung.RundenTyp.GEWINNERRUNDE, 2, 1,
					randomEmtpyWk(),
					randomEmtpyWk(),
					Optional.empty(), List.of(), null, null
				),
				new BegegnungDto(
					"d98af51f-c92d-4c9d-ab5c-3b1f50243805",
					Begegnung.RundenTyp.GEWINNERRUNDE, 2, 2,
					randomEmtpyWk(),
					randomEmtpyWk(),
					Optional.empty(), List.of(), null, null
				),
				new BegegnungDto(
					"726a7899-8ed5-4d35-b103-ad0c9788b3da",
					Begegnung.RundenTyp.TROSTRUNDE, 2, 1,
					randomEmtpyWk(),
					randomEmtpyWk(),
					Optional.empty(), List.of(), null, null
				),
				new BegegnungDto(
					"7222e0cf-a8e7-461d-aea6-c3fbae355d1d",
					Begegnung.RundenTyp.TROSTRUNDE, 2, 2,
					randomEmtpyWk(),
					randomEmtpyWk(),
					Optional.empty(), List.of(), null, null
				)
			)
			)
		));
		GruppenRundeDto gruppenRundeDto4 = new GruppenRundeDto(List.of(
			new RundeDto(UUID.fromString("0f9fe23a-ce15-4a73-a06b-e57e0ef54b9e"), 4, 2, 6, 2, Altersklasse.PAUSE, gruppeB, List.of(
				new BegegnungDto(
					"ef7f769c-c955-4d5b-b753-69172ddf0e64",
					Begegnung.RundenTyp.GEWINNERRUNDE, 4, 2,
					randomEmtpyWk(),
					randomEmtpyWk(),
					Optional.empty(), List.of(), null, null
				)
			)
			)
		));
		GruppenRundeDto gruppenRundeDto5 = new GruppenRundeDto(List.of(
			new RundeDto(UUID.fromString("0c6f082d-cf34-45c1-bed9-1fc17f8e66a7"), 5, 3, 7, 2, Altersklasse.PAUSE, gruppeA, List.of(
				new BegegnungDto(
					"95241ca3-4e8c-4316-95c7-44ecf525d209",
					Begegnung.RundenTyp.GEWINNERRUNDE, 3, 1,
					randomEmtpyWk(),
					randomEmtpyWk(),
					Optional.empty(), List.of(), null, null
				)
			)
			)
		));

		gruppenRunden.add(gruppenRundeDto1);
		gruppenRunden.add(gruppenRundeDto2);
		gruppenRunden.add(gruppenRundeDto3);
		gruppenRunden.add(gruppenRundeDto4);
		gruppenRunden.add(gruppenRundeDto5);
	}

	@Test
	void reorganize() {
		var reorganizedGruppenRunden = TurnierController.reorganize(gruppenRunden);


		for (GruppenRundeDto gr : gruppenRunden) {
			System.out.println("GruppenRunde: ");
			for (RundeDto r : gr.runde()) {
				System.out.println(" - Gruppe: " + r.gruppe().name() + ", Runde: " + r.gruppenRunde());
				for (BegegnungDto b : r.begegnungen()) {
					System.out.println("    - " + b.rundenTyp() + " Begegnung: " + b.wettkaempfer1().name() + " vs " + b.wettkaempfer2().name());
				}
			}
		}

		System.out.println("#############################\nREORGANISIERT:");
		for (GruppeTypenRundenDto gruppeTypenRundenDto : reorganizedGruppenRunden) {
			System.out.println("GruppenRunde: " + gruppeTypenRundenDto.gruppenId());

			System.out.println("  Gewinnerrunden" );
			for (RundeDto r : gruppeTypenRundenDto.gewinnerrundeDtoList()) {
				System.out.println("  - Runde: " + r.gruppenRunde());

				for (BegegnungDto b : r.begegnungen()) {
					System.out.println("     -  Begegnung: " + b.wettkaempfer1().name() + " vs " + b.wettkaempfer2().name());
				}
			}

			System.out.println("  Trostrunden" );
			for (RundeDto r : gruppeTypenRundenDto.trostrundeDtoList()) {
				System.out.println("  - Runde: " + r.gruppenRunde());

				for (BegegnungDto b : r.begegnungen()) {
					System.out.println("     -  Begegnung: " + b.wettkaempfer1().name() + " vs " + b.wettkaempfer2().name());
				}
			}
		}

		assertEquals(5, gruppenRunden.size());
		assertEquals(2, reorganizedGruppenRunden.size());            						// zwei verschiede Gruppen
		assertEquals(2, reorganizedGruppenRunden.get(0).gewinnerrundeDtoList().size());	// in jeder Gruppe gibt jeweils eine Liste mit Gewinnerrunden, eine mit Trostrunden
		assertEquals(0, reorganizedGruppenRunden.get(0).trostrundeDtoList().size());    	// in jeder Gruppe gibt jeweils eine Liste mit Gewinnerrunden, eine mit Trostrunden
		assertEquals(3, reorganizedGruppenRunden.get(1).gewinnerrundeDtoList().size());
		assertEquals(2, reorganizedGruppenRunden.get(1).trostrundeDtoList().size());

	}


	private static Wettkaempfer randomEmtpyWk() {
		return new Wettkaempfer(UUID.randomUUID(), "*", null, null, null, null, Optional.empty(), false, false, null);
	}
}
