package com.andyadc.codeblocks.framework.concurrent;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.common.util.concurrent.Uninterruptibles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * andy.an
 */
public final class ThreadPoolCreator {

	private static final Logger logger = LoggerFactory.getLogger(ThreadPoolCreator.class);

	private static final String THREAD_POOL_NAME_PREFIX = "StatisticsTask";
	private static final int DEFAULT_QUEUE_SIZE = 10;
	private static final int DEFAULT_CORE_POOL_SIZE = 10;
	private static final int DEFAULT_MAX_POOL_SIZE = 10;

	private static final ThreadLocal<Instant> start = new ThreadLocal<>();

	public synchronized static ThreadPoolExecutor create() {
		BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(DEFAULT_QUEUE_SIZE);
		// ignore/reset
		ThreadPoolExecutor executor = new ThreadPoolExecutor(
			DEFAULT_CORE_POOL_SIZE,
			DEFAULT_MAX_POOL_SIZE,
			60,
			TimeUnit.SECONDS,
			queue,
			new ThreadFactoryBuilder().setThreadFactory(new ThreadFactory() {
				private int count = 0;
				private final String prefix = THREAD_POOL_NAME_PREFIX;

				@Override
				public Thread newThread(Runnable r) {
					return new Thread(r, prefix + "-" + count++);
				}
			}).setUncaughtExceptionHandler((t, e) -> {
				String threadName = t.getName();
				logger.error("ThreadPool error occurred! threadName: {}, error message: {}", threadName, e.getMessage(), e);
			}).build(),
			(r, e) -> {
				if (!e.isShutdown()) {
					logger.warn("ThreadPool is too busy! Waiting to insert task to queue! ");
					Uninterruptibles.putUninterruptibly(e.getQueue(), r);
				}
			}
		) {
			@Override
			protected void beforeExecute(Thread t, Runnable r) {
				super.beforeExecute(t, r);
				start.set(Instant.now());
			}

			@Override
			protected void afterExecute(Runnable r, Throwable t) {
				super.afterExecute(r, t);
				if (t == null && r instanceof Future<?>) {
					try {
						Future<?> future = (Future<?>) r;
						future.get();
					} catch (CancellationException ce) {
						t = ce;
					} catch (ExecutionException ee) {
						t = ee.getCause();
					} catch (InterruptedException ie) {
						Thread.currentThread().interrupt(); // ignore/reset
					}
				}
				if (t != null) {
					logger.error("ThreadPool error message: {}", t.getMessage(), t);
				}
				logger.warn("Timing: {}ms", Duration.between(Instant.now(), start.get()).toMillis());
			}

		};
		executor.prestartAllCoreThreads();

		return executor;
	}

	public static void close(ThreadPoolExecutor executor) {
		executor.shutdown();
		try {
			if (!executor.awaitTermination(10L, TimeUnit.SECONDS)) {
				logger.warn("Wait 10 seconds then force shutdown thread pool");
				executor.shutdownNow();
			}
		} catch (InterruptedException e) {
			executor.shutdownNow();
			logger.error("ThreadPoolExecutor close error.", e);
		}
	}
}
