package com.andyadc.bms.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TokensPurgeTask {

	private static final Logger logger = LoggerFactory.getLogger(TokensPurgeTask.class);

	@Async
	@Scheduled(initialDelay = 10000L, fixedRate = 60000L)
	public void purgeExpired() {
		logger.info("purge expired tokens!");
	}
}
