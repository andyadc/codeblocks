package com.andyadc.bms.web.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.ServletRequestHandledEvent;

@Component
public class ReuquestTimingEventListener implements ApplicationListener<ServletRequestHandledEvent> {

	private static final Logger logger = LoggerFactory.getLogger(ReuquestTimingEventListener.class);

	@Override
	public void onApplicationEvent(ServletRequestHandledEvent event) {
		StringBuilder sb = new StringBuilder();
		sb.append("url=[").append(event.getRequestUrl()).append("]; ");
		sb.append("client=[").append(event.getClientAddress()).append("]; ");
		sb.append("time=[").append(event.getProcessingTimeMillis()).append("ms]; ");
		sb.append("status=[");
		if (!event.wasFailure()) {
			sb.append("OK");
		} else {
			sb.append("failed: ").append(event.getFailureCause());
		}
		sb.append(']');

		logger.info("RequestEvent {}", sb.toString());
	}

}
