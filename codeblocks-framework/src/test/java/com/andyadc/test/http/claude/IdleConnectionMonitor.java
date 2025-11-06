package com.andyadc.test.http.claude;

import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class IdleConnectionMonitor extends Thread {

	private static final Logger logger = LoggerFactory.getLogger(IdleConnectionMonitor.class);

	private final PoolingHttpClientConnectionManager connectionManager;
	private volatile boolean shutdown;

	public IdleConnectionMonitor(PoolingHttpClientConnectionManager connectionManager) {
		super("IdleConnectionMonitor");
		this.connectionManager = connectionManager;
		this.setDaemon(true);
	}

	@Override
	public void run() {
		try {
			while (!shutdown) {
				synchronized (this) {
					wait(5000); // Check every 5 seconds

					// Close expired connections
					connectionManager.closeExpiredConnections();

					// Close connections idle for more than 30 seconds
					connectionManager.closeIdleConnections(30, TimeUnit.SECONDS);

					logger.debug("Connection pool stats: {}",
						connectionManager.getTotalStats());
				}
			}
		} catch (InterruptedException e) {
			logger.warn("Idle connection monitor interrupted", e);
			Thread.currentThread().interrupt();
		}
	}

	public void shutdown() {
		shutdown = true;
		synchronized (this) {
			notifyAll();
		}
	}

}
