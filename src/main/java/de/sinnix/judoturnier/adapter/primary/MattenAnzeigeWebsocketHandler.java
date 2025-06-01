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
		String matteTyp = getMatteAndTypeFromQuery(session);
		if (matteTyp != null) {
			sessions.put(matteTyp, session);
			logger.info("Verbindung für Matte {} hergestellt.", matteTyp);
		}
	}

	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		String command = message.getPayload();
		String matteTyp = getMatteAndTypeFromQuery(session);
		String matte = matteTyp.split("_")[0]; // Extrahiere nur die Mattennummer

		logger.debug("Nachricht von Matte {}: {}", matteTyp, command);

		// An alle Sessions für diese Matte senden (sequentiell, mit Statusprüfung)
		for (Map.Entry<String, WebSocketSession> entry : sessions.entrySet()) {
			if (entry.getKey().startsWith(matte + "_")) {
				WebSocketSession targetSession = entry.getValue();
				try {
					// Prüfen, ob die Session offen ist, bevor gesendet wird
					if (targetSession.isOpen()) {
						synchronized (targetSession) {
							targetSession.sendMessage(new TextMessage(command));
						}
						// Kurze Pause zwischen den Sendevorgängen
						Thread.sleep(10);
					}
				} catch (Exception e) {
					logger.error("Fehler beim Senden an {}: {}", entry.getKey(), e.getMessage());
				}
			}
		}
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) {
		String matte = getMatteAndTypeFromQuery(session);
		if (matte != null) {
			sessions.remove(matte);
			logger.info("Verbindung für Matte {} geschlossen.", matte);
		}
	}

	private String getMatteAndTypeFromQuery(WebSocketSession session) {
		String query = session.getUri().getQuery(); // Beispiel: "matte=1&type=zeit"
		if (query == null) {
			return null;
		}

		String matte = null;
		String type = null;

		String[] params = query.split("&");
		for (String param : params) {
			String[] keyValue = param.split("=");
			if (keyValue.length == 2) {
				if ("matte".equals(keyValue[0])) {
					matte = keyValue[1];
				} else if ("type".equals(keyValue[0])) {
					type = keyValue[1];
				}
			}
		}

		if (matte != null) {
			if (type != null) {
				return matte + "_" + type; // Rückgabe eines kombinierten Schlüssels "1_zeit"
			} else {
				return matte; // Abwärtskompatibilität für ältere URLs ohne type
			}
		}

		return null;
	}
}
