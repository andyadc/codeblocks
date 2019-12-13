package com.andyadc.codeblocks.test.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * andy.an
 */
public class ExecutorPoolSizeTest {

	private static final Logger logger = LoggerFactory.getLogger(ExecutorPoolSizeTest.class);

	private static ThreadPoolExecutor executor = new ThreadPoolExecutor(
		2,
		4,
		60, TimeUnit.SECONDS,
		new ArrayBlockingQueue<>(1000),
		new ThreadFactory() {
			private String prefix = "DefaultThreadPool";
			AtomicInteger i = new AtomicInteger(0);

			@Override
			public Thread newThread(Runnable r) {
				Thread thread = new Thread(r, prefix + "-" + i.incrementAndGet());
				thread.setUncaughtExceptionHandler((t, e) ->
					logger.error(prefix + " error occured! threadName: {}, error message: {}", t.getName(), e.getMessage(), e)
				);
				return thread;
			}
		},
		new ThreadPoolExecutor.CallerRunsPolicy()
	);

	public static void main(String[] args) throws Exception {
		int count = 1500;
		CountDownLatch latch = new CountDownLatch(count);

		AtomicInteger n = new AtomicInteger(0);
		for (int i = 0; i < count; i++) {
			executor.execute(() -> {
				try {
					logger.info(Thread.currentThread().getName());
					int num = n.incrementAndGet();
					if (num % 3 == 0) {
						num = num / 0;
					}
					Thread.sleep(num);
				} catch (Exception e) {
					logger.error("", e);
				} finally {
					latch.countDown();
				}

			});
		}

		latch.await();
		System.out.println(">>>> " + n.get());
		executor.shutdown();
	}
}
