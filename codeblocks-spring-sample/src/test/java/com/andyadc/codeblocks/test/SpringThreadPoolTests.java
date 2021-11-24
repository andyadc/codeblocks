package com.andyadc.codeblocks.test;

import com.andyadc.codeblocks.kit.concurrent.ThreadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * TODO
 */
public class SpringThreadPoolTests {

	private static final Logger logger = LoggerFactory.getLogger(SpringThreadPoolTests.class);

	public static ThreadPoolTaskExecutor build() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setThreadNamePrefix("");
		executor.setCorePoolSize(2);
		executor.setMaxPoolSize(4);
		executor.setQueueCapacity(100);
		executor.setKeepAliveSeconds(60);
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		executor.initialize();
		return executor;
	}

	public static void main(String[] args) throws Exception {
		ThreadPoolTaskExecutor executor = build();

		int count = 1500;
		CountDownLatch latch = new CountDownLatch(count);
		AtomicInteger n = new AtomicInteger(0);
		for (int i = 0; i < count; i++) {
			executor.execute(() -> {
				try {
					int num = n.incrementAndGet();
					logger.info(Thread.currentThread().getName() + " >>> " + num + "\r\n" + executor);

					if (num % 3 == 0) {
						num = num / 0;
					}
					ThreadUtil.sleep(num / 10);
				} finally {
					latch.countDown();
				}
			});
		}

		executor.shutdown();

		latch.await();
		System.out.println("Finished >>>> " + n.get());
	}
}
