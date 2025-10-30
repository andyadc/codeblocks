package com.andyadc.test.http.claude;

import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

public class IdleConnectionMonitorThread extends Thread {

	private final PoolingHttpClientConnectionManager connMgr;
	private volatile boolean shutdown;

	public IdleConnectionMonitorThread(PoolingHttpClientConnectionManager connMgr) {
		super("IdleConnectionMonitor");
		this.connMgr = connMgr;
		setDaemon(true);
	}

	@Override
	public void run() {
		try {
			while (!shutdown) {
				synchronized (this) {
					wait(5000);
					// Close expired connections
					connMgr.closeExpiredConnections();
					// Close connections idle for 30 seconds
					connMgr.closeIdleConnections(30, java.util.concurrent.TimeUnit.SECONDS);
				}
			}
		} catch (InterruptedException ex) {
			// Terminate
		}
	}

	public void shutdown() {
		shutdown = true;
		synchronized (this) {
			notifyAll();
		}
	}

}
