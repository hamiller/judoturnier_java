package de.sinnix.judoturnier.application;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class QRCodeGeneratorService {

	private static final Logger logger = LogManager.getLogger(QRCodeGeneratorService.class);

	public byte[] generateQRCode(String text, int width, int height) {
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		try {
			BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

			BufferedImage qrCodeImage = getBufferedImage(width, height, bitMatrix);

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			ImageIO.write(qrCodeImage, "png", outputStream);
			return outputStream.toByteArray();
		} catch (WriterException | IOException e) {
			logger.error(e);
			throw new RuntimeException(e);
		}
	}

	private static BufferedImage getBufferedImage(int width, int height, BitMatrix bitMatrix) {
		BufferedImage qrCodeImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		qrCodeImage.createGraphics();

		Graphics2D graphics = (Graphics2D) qrCodeImage.getGraphics();
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, width, height);
		graphics.setColor(Color.BLACK);

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (bitMatrix.get(i, j)) {
					graphics.fillRect(i, j, 1, 1);
				}
			}
		}
		return qrCodeImage;
	}

}
