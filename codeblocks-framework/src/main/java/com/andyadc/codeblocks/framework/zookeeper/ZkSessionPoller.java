package com.andyadc.codeblocks.framework.zookeeper;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author andy.an
 * @since 2019/2/13
 */
public final class ZkSessionPoller {

	private static final Logger logger = LoggerFactory.getLogger(ZkSessionPoller.class);

	/*Poll interval in milliseconds*/
	private final long pollIntervalMs;

	/*The zookeeper instance to check*/
	private final ZooKeeper zk;

	private final ConnectionListener pollListener;

	private final Object disconnectTimeLock = "Lock";

	/*executor to poll*/
	private final ScheduledExecutorService poller = Executors.newScheduledThreadPool(1,
		(runnable) -> {
			Thread t = new Thread(runnable);
			t.setName("ZkSessionPoller");
			return t;
		});

	private Long startDisconnectTime;

	public ZkSessionPoller(ZooKeeper zk, long pollIntervalMs, ConnectionListener pollListener) {
		this.zk = zk;
		this.pollIntervalMs = pollIntervalMs;
		this.pollListener = pollListener;
	}

	public void startPolling() {
		poller.scheduleWithFixedDelay(new SessionPoller(), 0L, pollIntervalMs, TimeUnit.MILLISECONDS);
	}

	public void stopPolling() {
		poller.shutdownNow();
	}

	private void expire() {
		//session expired!
		logger.info("Session has expired, notifying listenerand shutting down poller");
		ZkSessionPoller.this.stopPolling();
		pollListener.expired();
	}

	private class SessionPoller implements Runnable {

		private final int sessionTimeoutPeriod;

		public SessionPoller() {
			sessionTimeoutPeriod = zk.getSessionTimeout();
		}

		@Override
		public void run() {
			if (Thread.currentThread().isInterrupted()) {
				return;
			}

			if (logger.isTraceEnabled()) {
				logger.trace("Current state of ZooKeeper object: " + zk.getState());
			}

			try {
				zk.exists("/", false);
				synchronized (disconnectTimeLock) {
					startDisconnectTime = null;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (KeeperException e) {
				if (e.code() == KeeperException.Code.SESSIONEXPIRED) {
					expire();
				} else if (e.code() == KeeperException.Code.CONNECTIONLOSS) {
					logger.debug("Received a ConnectionLoss Exception, determining if our session has expired");
					long currentTime = System.currentTimeMillis();
					boolean shouldExpire = false;
					synchronized (disconnectTimeLock) {
						if (startDisconnectTime == null) {
							startDisconnectTime = currentTime;
						} else if ((currentTime - startDisconnectTime) > sessionTimeoutPeriod) {
							shouldExpire = true;
						}
					}
					if (shouldExpire)
						expire();
				} else {
					e.printStackTrace();
				}
			}
		}
	}
}
