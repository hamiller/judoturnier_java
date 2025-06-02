package de.sinnix.judoturnier.adapter.primary;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.net.URI;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class MattenAnzeigeWebsocketHandler extends TextWebSocketHandler {
	private static final Logger                      logger        = LogManager.getLogger(MattenAnzeigeWebsocketHandler.class);
	// Speicherung der Sessions basierend auf "matte"
	private final Map<String, Set<WebSocketSession>> matteSessions = new ConcurrentHashMap<>();

	@Override
	public void afterConnectionEstablished(WebSocketSession session) {
		String matte = getMatteFromQuery(session);
		if (matte == null) {
			logger.warn("WebSocket connection established without 'matte' query parameter. Closing session: {}", session.getId());
			return;
		};

		matteSessions.computeIfAbsent(matte, k -> ConcurrentHashMap.newKeySet()).add(session);
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		String matte = getMatteFromQuery(session);
		if (matte == null) {
			logger.warn("WebSocket connection established without 'matte' query parameter. Closing session: {}", session.getId());
			return;
		};

		for (WebSocketSession s : matteSessions.getOrDefault(matte, Set.of())) {
			if (s.isOpen()) {
				s.sendMessage(message);
			}
		}
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
		String matte = getMatteFromQuery(session);
		if (matte == null) {
			logger.warn("WebSocket connection established without 'matte' query parameter. Closing session: {}", session.getId());
			return;
		};

		Set<WebSocketSession> sessions = matteSessions.get(matte);
		if (sessions != null) {
			sessions.remove(session);
			if (sessions.isEmpty()) matteSessions.remove(matte);
		}
	}

	private String getMatteFromQuery(WebSocketSession session) {
		URI uri = session.getUri();
		if (uri == null) {
			logger.warn("URI ist null {}", session.getId());
			return null;
		};

		String query = uri.getQuery(); // e.g., "matte=1"
		if (query == null) {
			logger.warn("query ist null");
			return null;
		};

		for (String param : query.split("&")) {
			String[] kv = param.split("=");
			if (kv.length == 2 && kv[0].equals("matte")) {
				return kv[1];
			}
		}
		logger.warn("matte nicht gefunden in der Abfrage: {}", query);
		return null;
	}
}
