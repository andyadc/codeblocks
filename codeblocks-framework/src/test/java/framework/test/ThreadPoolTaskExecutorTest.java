package framework.test;

import com.andyadc.codeblocks.framework.concurrent.ThreadPoolTaskExecutorConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * andy.an
 * 2019/12/2
 */
public class ThreadPoolTaskExecutorTest {

	private static final Logger logger = LoggerFactory.getLogger(ThreadPoolTaskExecutorTest.class);

	public static void main(String[] args) throws Exception {
		int count = 100;
		CountDownLatch latch = new CountDownLatch(count);
		AtomicInteger n = new AtomicInteger(0);

		ThreadPoolTaskExecutorConfig config = new ThreadPoolTaskExecutorConfig();
		ThreadPoolTaskExecutor executor = config.defaultTaskExecutor();

		for (int i = 0; i < count; i++) {
			executor.execute(() -> {
					int num = n.incrementAndGet();
					MDC.put("traceId", num + "");
					logger.info(Thread.currentThread().getName());
					logger.info(Thread.currentThread().getName());
					latch.countDown();
				}
			);
		}

		latch.await();
		System.out.println(">>>> " + n.get());
		executor.shutdown();
	}
}
