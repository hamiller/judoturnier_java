package de.sinnix.judoturnier.adapter;

import org.junit.jupiter.api.Test;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AccessTokenAuthorizationProviderIT extends AbstractIntegrationTest {

    @Test
    public void authorization_is_provided() throws Exception {

        String token = getKampfrichterToken();
        String payload = token.split("\\.")[1];


        String json = new String(Base64.getDecoder().decode(payload), StandardCharsets.UTF_8);
        assertTrue(json.contains("kampfrichter"));
    }
}