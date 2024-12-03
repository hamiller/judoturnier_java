package de.sinnix.judoturnier.adapter.primary;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.Map;

public class MattenAnzeigeWebsocketHandler extends TextWebSocketHandler {
	private static final Logger                        logger   = LogManager.getLogger(MattenAnzeigeWebsocketHandler.class);
	// Speicherung der Sessions basierend auf "matte"
	private final        Map<String, WebSocketSession> sessions = new HashMap<>();

	@Override
	public void afterConnectionEstablished(WebSocketSession session) {
		String matte = getMatteFromQuery(session);
		if (matte != null) {
			sessions.put(matte, session);
			logger.info("Verbindung für Matte {} hergestellt.", matte);
		}
	}

	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		String command = message.getPayload();
		String matte = getMatteFromQuery(session);
		logger.debug("Nachricht von Matte {}: {}", matte, command);
		sessions.get(matte).sendMessage(new TextMessage(command));
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) {
		String matte = getMatteFromQuery(session);
		if (matte != null) {
			sessions.remove(matte);
			logger.info("Verbindung für Matte {} geschlossen.", matte);
		}
	}

	private String getMatteFromQuery(WebSocketSession session) {
		String query = session.getUri().getQuery(); // Beispiel: "matte=1"
		if (query != null && query.startsWith("matte=")) {
			return query.substring(6); // Rückgabe des Parameters "1"
		}
		return null;
	}
}
