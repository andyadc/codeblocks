package com.andyadc.bms.management.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(1)
@Component
public class StartupRunner implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(StartupRunner.class);

	@Override
	public void run(String... args) throws Exception {
		logger.info(">>> StartupRunner <<<");
	}
}
