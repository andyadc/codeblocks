package com.andyadc.bms.management.bean;

import org.apache.catalina.connector.Connector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class GracefulShutdown implements TomcatConnectorCustomizer, ApplicationListener<ContextClosedEvent> {

	private static final Logger logger = LoggerFactory.getLogger(GracefulShutdown.class);
	private static final long TIMEOUT = 30L;
	private volatile Connector connector;

	@Override
	public void customize(Connector connector) {
		this.connector = connector;
	}

	@Override
	public void onApplicationEvent(ContextClosedEvent event) {
		logger.info("Protocol handler is shutting down");

		if (this.connector == null) {
			return;
		}

		this.connector.pause();
		Executor executor = this.connector.getProtocolHandler().getExecutor();
		if (executor instanceof ThreadPoolExecutor) {
			try {
				ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) executor;
				threadPoolExecutor.shutdown();

				if (!threadPoolExecutor.awaitTermination(TIMEOUT, TimeUnit.SECONDS)) {
					logger.warn("Tomcat thread pool did not shutdown gracefully within 30 seconds. Proceeding with forceful shutdown");
					threadPoolExecutor.shutdownNow();

					if (!threadPoolExecutor.awaitTermination(TIMEOUT, TimeUnit.SECONDS)) {
						logger.error("Tomcat thread pool did not terminate");
					}
				} else {
					logger.info("Protocol handler shutdown");
				}
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}
}
