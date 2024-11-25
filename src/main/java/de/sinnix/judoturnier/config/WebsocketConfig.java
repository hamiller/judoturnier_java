package de.sinnix.judoturnier.config;

import de.sinnix.judoturnier.adapter.primary.MattenAnzeigeWebsocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebsocketConfig implements WebSocketConfigurer {

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(new MattenAnzeigeWebsocketHandler(), "/mattenanzeigews")
			.setAllowedOrigins("*");
	}
}