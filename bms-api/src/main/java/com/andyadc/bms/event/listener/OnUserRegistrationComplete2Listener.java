package com.andyadc.bms.event.listener;

import com.andyadc.bms.event.OnUserRegistrationCompleteEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Order(2)
@Component
public class OnUserRegistrationComplete2Listener implements ApplicationListener<OnUserRegistrationCompleteEvent> {

	private static final Logger logger = LoggerFactory.getLogger(OnUserRegistrationComplete2Listener.class);

	@Async
	@Override
	public void onApplicationEvent(OnUserRegistrationCompleteEvent event) {
		logger.info("OnUserRegistrationComplete2Listener");
	}
}
