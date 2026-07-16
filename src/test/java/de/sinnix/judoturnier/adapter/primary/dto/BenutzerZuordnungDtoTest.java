package de.sinnix.judoturnier.adapter.primary.dto;

import org.junit.jupiter.api.Test;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class BenutzerZuordnungDtoTest {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void jsonMapperBehandeltFehlendeCheckboxWerteAlsNichtZugeordnet() throws Exception {
		String json = """
			{
			  "c79f4203-6c72-4771-b641-17eb80e4cfaa": {
			    "userid": "c79f4203-6c72-4771-b641-17eb80e4cfaa"
			  },
			  "659b104d-6267-4fe0-b70e-641c455b6030": {
			    "userid": "659b104d-6267-4fe0-b70e-641c455b6030"
			  },
			  "898a7fcf-2fad-4ec9-8b4f-5513188af291": {
			    "userid": "898a7fcf-2fad-4ec9-8b4f-5513188af291"
			  },
			  "355d3ac2-991b-45ed-8a2a-1bc1ad32eb15": {
			    "userid": "355d3ac2-991b-45ed-8a2a-1bc1ad32eb15",
			    "zugeordnetZuTurnier": "true"
			  }
			}
			""";

		Map<String, BenutzerZuordnungDto> benutzerZuordnungen = objectMapper.readValue(json, new TypeReference<>() {
		});

		assertThat(benutzerZuordnungen).hasSize(4);
		assertThat(benutzerZuordnungen.get("c79f4203-6c72-4771-b641-17eb80e4cfaa").istZugeordnetZuTurnier()).isFalse();
		assertThat(benutzerZuordnungen.get("659b104d-6267-4fe0-b70e-641c455b6030").istZugeordnetZuTurnier()).isFalse();
		assertThat(benutzerZuordnungen.get("898a7fcf-2fad-4ec9-8b4f-5513188af291").istZugeordnetZuTurnier()).isFalse();
		assertThat(benutzerZuordnungen.get("355d3ac2-991b-45ed-8a2a-1bc1ad32eb15").istZugeordnetZuTurnier()).isTrue();
	}
}
