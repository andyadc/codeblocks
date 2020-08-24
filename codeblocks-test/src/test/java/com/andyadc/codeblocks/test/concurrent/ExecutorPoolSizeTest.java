package com.andyadc.codeblocks.test.concurrent;

import com.andyadc.codeblocks.kit.concurrent.ThreadUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
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

	private static final ThreadPoolExecutor executor = new ThreadPoolExecutor(
		2,
		4,
		60, TimeUnit.SECONDS,
		new ArrayBlockingQueue<>(100),
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

	@Test
	public void testAdd() throws Exception {
		AtomicInteger c = new AtomicInteger(0);
		int times = 1000;
		CountDownLatch latch = new CountDownLatch(times);
		for (int i = 0; i < times; i++) {
			executor.execute(() -> {
				c.incrementAndGet();
				latch.countDown();
			});
		}

		latch.await();
		System.out.println(c);
	}

	@Test
	public void testCountDownLatch() throws Exception {
		int times = 1000;
		CountDownLatch latch = new CountDownLatch(times);

		Instant begin = Instant.now();
		for (int i = 0; i < times; i++) {
			executor.execute(() -> {
				try {
					System.out.println(Thread.currentThread().getName() + "");
					ThreadUtil.sleep(100);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					latch.countDown();
				}
			});
		}

		System.out.println("-------------------");
		latch.await();
		System.out.println("Done " + Duration.between(begin, Instant.now()).toMillis() / 1000);
		executor.shutdown();
	}

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
