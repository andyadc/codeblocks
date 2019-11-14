package com.andyadc.codeblocks.test.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * andy.an
 */
public class ExecutorPoolSizeTest {

	private static final Logger logger = LoggerFactory.getLogger(ExecutorPoolSizeTest.class);

	private static ExecutorService executor = new ThreadPoolExecutor(
		2,
		4,
		60, TimeUnit.SECONDS,
		new ArrayBlockingQueue<>(10),
		new ThreadFactory() {
			AtomicInteger i = new AtomicInteger(0);

			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r, "TestExecutorPool-" + i.incrementAndGet());
			}
		},
		new ThreadPoolExecutor.CallerRunsPolicy()
	);

	public static void main(String[] args) throws Exception {
		int count = 100;
		CountDownLatch latch = new CountDownLatch(count);

		AtomicInteger n = new AtomicInteger(0);
		for (int i = 0; i < count; i++) {
			executor.execute(() -> {
				logger.info(Thread.currentThread().getName());
				n.incrementAndGet();
				latch.countDown();
			});
		}

		latch.await();
		System.out.println(">>>> " + n.get());
		executor.shutdown();
	}
}
