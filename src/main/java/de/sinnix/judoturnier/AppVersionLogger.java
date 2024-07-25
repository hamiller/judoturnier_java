package de.sinnix.judoturnier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Component;

@Component
public class AppVersionLogger implements CommandLineRunner {

	private static final Logger logger = LogManager.getLogger(AppVersionLogger.class);

	@Autowired
	private BuildProperties buildProperties;

	@Override
	public void run(String... args) throws Exception {
		logger.info("Application Version: {}", buildProperties.getVersion());
	}
}
