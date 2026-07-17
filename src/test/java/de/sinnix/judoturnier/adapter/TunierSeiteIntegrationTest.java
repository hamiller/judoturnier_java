package de.sinnix.judoturnier.adapter;

import java.io.IOException;


import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TunierSeiteIntegrationTest extends AbstractIntegrationTest {


	@Test
	public void turnieruebersicht() throws IOException {
		try (BrowserContext context = login("kr1", "k1")) {
			Page page = context.newPage();
			page.navigate(url("/"));
			assertEquals("Sie sind angemeldet als kr1", page.getByTestId("loggedin_user").textContent());
		}
	}
}
