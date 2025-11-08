package com.andyadc.test.http.claude;

import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.pool.PoolStats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ConnectionPoolMetrics {

	private static final Logger logger = LoggerFactory.getLogger(ConnectionPoolMetrics.class);

	private final PoolingHttpClientConnectionManager connectionManager;
	private final ScheduledExecutorService scheduler;

	public ConnectionPoolMetrics(PoolingHttpClientConnectionManager connectionManager) {
		this.connectionManager = connectionManager;
		this.scheduler = Executors.newScheduledThreadPool(1);
	}

	public void startMonitoring() {
		scheduler.scheduleAtFixedRate(this::logMetrics, 0, 30, TimeUnit.SECONDS);
	}

	public void stopMonitoring() {
		scheduler.shutdown();
	}

	private void logMetrics() {
		PoolStats totalStats = connectionManager.getTotalStats();

		logger.info("=== Connection Pool Stats === \r\n" +
				"Total: Available={}, Leased={}, Pending={}, Max={}",
			totalStats.getAvailable(),
			totalStats.getLeased(),
			totalStats.getPending(),
			totalStats.getMax());

		// Log per-route stats if needed
		for (HttpRoute route : connectionManager.getRoutes()) {
			PoolStats routeStats = connectionManager.getStats(route);
			logger.debug("Route {}: Available={}, Leased={}, Pending={}",
				route,
				routeStats.getAvailable(),
				routeStats.getLeased(),
				routeStats.getPending());
		}
	}

}
