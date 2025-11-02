package com.andyadc.test.http.claude;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.io.Closeable;
import java.util.concurrent.TimeUnit;

/**
 * Manages HttpClient lifecycle in production environments
 */
public class HttpClientManager implements Closeable {

	private final CloseableHttpClient httpClient;
	private final PoolingHttpClientConnectionManager connectionManager;
	private final IdleConnectionMonitor idleConnectionMonitor;

	public HttpClientManager() {
		this.connectionManager = createConnectionManager();
		this.httpClient = createHttpClient(connectionManager);
		this.idleConnectionMonitor = new IdleConnectionMonitor(connectionManager);
		this.idleConnectionMonitor.start();
	}

	private PoolingHttpClientConnectionManager createConnectionManager() {
		PoolingHttpClientConnectionManager cm =
			new PoolingHttpClientConnectionManager();
		cm.setMaxTotal(200);
		cm.setDefaultMaxPerRoute(20);
		return cm;
	}

	private CloseableHttpClient createHttpClient(
		PoolingHttpClientConnectionManager cm) {
		return HttpClients.custom()
			.setConnectionManager(cm)
			.build();
	}

	public CloseableHttpClient getHttpClient() {
		return httpClient;
	}

	@Override
	public void close() {
		try {
			idleConnectionMonitor.shutdown();
			httpClient.close();
			connectionManager.close();
		} catch (Exception e) {
			// Log error
		}
	}

	/**
	 * Background thread to clean up idle connections
	 */
	private static class IdleConnectionMonitor extends Thread {
		private final PoolingHttpClientConnectionManager cm;
		private volatile boolean shutdown;

		public IdleConnectionMonitor(PoolingHttpClientConnectionManager cm) {
			super("IdleConnectionMonitor");
			this.cm = cm;
			setDaemon(true);
		}

		@Override
		public void run() {
			while (!shutdown) {
				synchronized (this) {
					try {
						wait(5000);
						cm.closeExpiredConnections();
						cm.closeIdleConnections(30, TimeUnit.SECONDS);
					} catch (InterruptedException e) {
						shutdown = true;
					}
				}
			}
		}

		public void shutdown() {
			shutdown = true;
			synchronized (this) {
				notifyAll();
			}
		}
	}
}
