package de.sinnix.judoturnier.adapter;

import java.io.IOException;


import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TunierSeiteIntegrationTest extends AbstractIntegrationTest {


	@Test
	public void turnieruebersicht() throws IOException {
		BrowserContext context = login();
        Page page = context.newPage();
		page.navigate("http://localhost:" + port + "/");
		assertEquals("Sie sind angemeldet als kr1", page.getByTestId("loggedin_user").textContent());
	}

    private BrowserContext login() {
        BrowserContext context = browser.newContext(new Browser.NewContextOptions());
        Page page = context.newPage();
        
        page.navigate("http://localhost:" + port + "/oauth2/authorization/keycloak");
        page.locator("#username").fill("kr1");
        page.locator("#password").fill("k1");
        page.locator("#kc-login").click();

        page.waitForURL("http://localhost:" + port + "/**");

        assertTrue(page.locator("body").textContent().contains("Sie sind angemeldet als kr1"));
        return context;
    }
}
