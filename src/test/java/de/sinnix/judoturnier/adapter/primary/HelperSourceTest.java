package de.sinnix.judoturnier.adapter.primary;

import com.github.jknack.handlebars.Handlebars;
import de.sinnix.judoturnier.model.Altersklasse;
import de.sinnix.judoturnier.model.Runde;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HelperSourceTest {

	private Handlebars handlebars;

	@BeforeEach
	void setUp() {
		handlebars = new Handlebars();
		handlebars.registerHelpers(HelperSource.class);
	}

	@Test
	void testVorherigesElement() {
		var a = UUID.randomUUID();
		var b = UUID.randomUUID();
		var c = UUID.randomUUID();
		List<Runde> runden = List.of(new Runde(a, 1, 1, 1,1, Altersklasse.U11, null, List.of()),
			new Runde(b, 2, 2, 2,2, Altersklasse.U11, null, List.of()),
			new Runde(c, 3, 3, 3,3, Altersklasse.U11, null, List.of())
			);
		var newRunden = HelperSource.vorherigeRunde(runden, null);

		assertEquals(null, newRunden.get(0).getLeft());
		assertEquals(a, newRunden.get(0).getRight().rundeId());
		assertEquals(a, newRunden.get(1).getLeft().rundeId());
		assertEquals(b, newRunden.get(1).getRight().rundeId());
		assertEquals(b, newRunden.get(2).getLeft().rundeId());
		assertEquals(c, newRunden.get(2).getRight().rundeId());
	}

	@Test
	void testIstLeer() throws IOException {
		String templateSource = "{{#if (istLeer element1 element3)}}Ein leeres{{else}}Alle voll{{/if}}";
		com.github.jknack.handlebars.Template template = handlebars.compileInline(templateSource);

		// generell
		String result = template.apply(Map.of(
			"element1", "",
			"element3", ""
		));
		assertEquals("Ein leeres", result);
	}

	@Test
	void testIstLeer2() throws IOException {
		String templateSource = "{{#if (istLeer element1)}}Ein leeres{{else}}Alle voll{{/if}}";
		com.github.jknack.handlebars.Template template = handlebars.compileInline(templateSource);

		String result = template.apply(Map.of(
			"element1", ""
		));
		assertEquals("Ein leeres", result);
	}

	@Test
	void testIstLeer3() throws IOException {
		String templateSource = "{{#if (istLeer element1)}}Ein leeres{{else}}Alle voll{{/if}}";
		com.github.jknack.handlebars.Template template = handlebars.compileInline(templateSource);

		String result = template.apply(Map.of(
			"element1", "1"
		));
		assertEquals("Alle voll", result);
	}

	@Test
	void testIstLeer4() throws IOException {
		String templateSource = "{{#if (istLeer element1 element2)}}Ein leeres{{else}}Alle voll{{/if}}";
		com.github.jknack.handlebars.Template template = handlebars.compileInline(templateSource);

		String result = template.apply(Map.of(
			"element1", "",
			"element2", "1"
		));
		assertEquals("Ein leeres", result);
	}

	@Test
	void testIstLeer5() throws IOException {
		String templateSource = "{{#if (istLeer element1 element2 element3)}}Ein leeres{{else}}Alle voll{{/if}}";
		com.github.jknack.handlebars.Template template = handlebars.compileInline(templateSource);

		Map<String, Object> context = new HashMap<>();
		context.put("element1", ""); // Dynamische Elemente
		context.put("element2", null);
		context.put("element3", "");
		String result = template.apply(context);
		assertEquals("Ein leeres", result);
	}

}