package com.andyadc.codeblocks.test.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SettleTaskExecutorTests {

	static final SettleTaskExecutor taskExecutor = new SettleTaskExecutor(5, 10, 50);
	private static final Logger logger = LoggerFactory.getLogger(SettleTaskExecutorTests.class);

	public static void main(String[] args) {

		for (int i = 0; i < 10; i++) {
			taskExecutor.execute(() -> {
				logger.info("execute task1");
			});
		}

		System.out.println("exec1");

		for (int i = 0; i < 10; i++) {
			taskExecutor.execute(() -> {
				logger.info("execute task2");
			});
		}

		System.out.println("exec2");

		taskExecutor.join();

		System.out.println("done");

	}
}
