package com.andyadc.codeblocks.framework.concurrent;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.common.util.concurrent.Uninterruptibles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public final class ThreadPoolCreator {

	private static final Logger logger = LoggerFactory.getLogger(ThreadPoolCreator.class);

	private static final String THREAD_POOL_NAME_PREFIX = "StatisticsTask";
	private static final int DEFAULT_QUEUE_SIZE = 10;
	private static final int DEFAULT_CORE_POOL_SIZE = 10;
	private static final int DEFAULT_MAX_POOL_SIZE = 10;

	private static final ThreadLocal<Instant> start = new ThreadLocal<>();

	private String poolName;

	public synchronized static ThreadPoolExecutor create() {
		return create(THREAD_POOL_NAME_PREFIX);
	}

	public synchronized static ThreadPoolExecutor create(String poolName) {
		return create(poolName, DEFAULT_CORE_POOL_SIZE, DEFAULT_MAX_POOL_SIZE, DEFAULT_QUEUE_SIZE);
	}

	public synchronized static ThreadPoolExecutor create(String poolName, int corePoolSize, int maxPoolSize, int queueSize) {
		BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(queueSize);
		// ignore/reset
		ThreadPoolExecutor executor = new ThreadPoolExecutor(
			corePoolSize,
			maxPoolSize,
			60,
			TimeUnit.SECONDS,
			queue,
			new ThreadFactoryBuilder().setThreadFactory(new ThreadFactory() {
				private final String prefix = poolName;
				private int count = 0;

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
				logger.warn("Elapsed time: {}ms", Duration.between(start.get(), Instant.now()).toMillis());
			}

			@Override
			public void shutdown() {
				logger.info("{}\r\n activeCount: {}, poolSize: {}, corePoolSize: {}, maximumPoolSize: {}, completedTaskCount: {}, taskCount: {}, largestPoolSize: {}", poolName,
					this.getActiveCount(), // 获取正在执行任务的线程数据
					this.getPoolSize(), // 获取当前线程池中线程数量的大小
					this.getCorePoolSize(), // 线程池的核心线程数量
					this.getMaximumPoolSize(), // 线程池中最大可创建的线程数
					this.getCompletedTaskCount(), // 已经执行完成的任务数量
					this.getTaskCount(), // 计划执行的任务总数
					this.getLargestPoolSize() // 线程池里曾经创建过的最大的线程数量
				);
				super.shutdown();
			}

			@Override
			public List<Runnable> shutdownNow() {
				return super.shutdownNow();
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
