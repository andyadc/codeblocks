package com.andyadc.bms.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

	private static final Logger logger = LoggerFactory.getLogger(SetupDataLoader.class);

	private volatile boolean alreadySetup = false;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (alreadySetup) {
			return;
		}

		logger.info("<<<<<<<<<<<>>>>>>>>>>>");
		alreadySetup = true;
	}
}
