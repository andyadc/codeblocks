package com.andyadc.codeblocks.framework.http;

import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.pool.PoolStats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class IdleConnectionEvictorThread extends Thread {

	private final Logger logger = LoggerFactory.getLogger(IdleConnectionEvictorThread.class);
	private final PoolingHttpClientConnectionManager connectionManager;
	private volatile boolean shutdown;

	public IdleConnectionEvictorThread(PoolingHttpClientConnectionManager connectionManager) {
		super("Connection evictor");
		this.connectionManager = connectionManager;
		this.setDaemon(true);
	}

	@Override
	public void run() {
		try {
			while (!shutdown) {
				synchronized (this) {
					wait(30000); // Run every 30 seconds
					// Close expired connections
					connectionManager.closeExpiredConnections();
					// Close connections that have been idle for longer than 60 sec
					connectionManager.closeIdleConnections(60, TimeUnit.SECONDS);

					// Log pool statistics
					PoolStats stats = connectionManager.getTotalStats();
					logger.debug("Connection pool stats - Available: {}, Leased: {}, Pending: {}, Max: {}",
						stats.getAvailable(), stats.getLeased(), stats.getPending(), stats.getMax());
				}
			}
		} catch (InterruptedException ex) {
			// Restore the interrupted status
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
