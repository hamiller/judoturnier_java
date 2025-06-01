package de.sinnix.judoturnier.application;

import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.UUID;

@Service
public class CodeGeneratorService {
	private static final char[] ALPHANUM = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();

	public static String generateCode(UUID uuid) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(uuid.toString().getBytes(StandardCharsets.UTF_8));

			StringBuilder code = new StringBuilder();
			for (int i = 0; i < 4; i++) {
				int index = Byte.toUnsignedInt(hash[i]) % ALPHANUM.length;
				code.append(ALPHANUM[index]);
			}
			return code.toString();
		} catch (Exception e) {
			throw new RuntimeException("Fehler beim Generieren des Codes", e);
		}
	}
}
