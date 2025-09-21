package de.sinnix.judoturnier.adapter.primary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import java.util.Base64;


import de.sinnix.judoturnier.application.QRCodeGeneratorService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RestController
@RequestMapping(QRCodeController.CONTROLLER_URI)
public class QRCodeController {
	public static final String CONTROLLER_URI           = "/turnier/{turnierid}/qrcodes";
	public static final String RANDORI_ALTERSKLASSE_URI = "/{altersklasse}";

	private static final Logger logger = LogManager.getLogger(QRCodeController.class);

	@Autowired
	private QRCodeGeneratorService qrCodeGeneratorService;

	@GetMapping(RANDORI_ALTERSKLASSE_URI)
	public ModelAndView ladeQRCodeGruppenRandori(@PathVariable String turnierid, @PathVariable("altersklasse") String altersklasse) {
		logger.info("lade QR-Code Randori-Gruppen für " + altersklasse);
		UriComponentsBuilder builder = ServletUriComponentsBuilder
			.fromCurrentRequest()
			.replaceQueryParam("continue");
		String base64ImageGruppen = getBase64ImageGruppen(DruckController.CONTROLLER_URI + DruckController.DRUCK_RANDORI_GEWICHTSKLASSE_URI, turnierid, altersklasse, builder);
		String base64ImageBegegnungen = getBase64ImageGruppen(DruckController.CONTROLLER_URI + DruckController.DRUCK_RANDORI_BEGEGNUNGEN_URI, turnierid, altersklasse, builder);

		String title = "QR-Codes Randori";

		ModelAndView mav = new ModelAndView("qrcode");
		mav.addObject("titel", title);
		mav.addObject("altersklasse", altersklasse);
		mav.addObject("imageGruppen", base64ImageGruppen);
		mav.addObject("imageBegegnungen", base64ImageBegegnungen);
		return mav;
	}

	private String getBase64ImageGruppen(String path, String turnierid, String altersklasse, UriComponentsBuilder builder) {
		logger.debug("QR-Code Path: {} mit turnier {} und alter {}", path, turnierid, altersklasse);
		URI uri = builder
			.replacePath(path)
			.buildAndExpand(turnierid, altersklasse)
			.toUri();
		logger.debug("Erstellte URI: {}", uri.toASCIIString());
		byte[] pngQRCode = qrCodeGeneratorService.generateQRCode(uri.toASCIIString(), 400, 400);
		String base64Image = Base64.getEncoder().encodeToString(pngQRCode);
		return base64Image;
	}

}
