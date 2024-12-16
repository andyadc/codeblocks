package com.andyadc.codeblocks.test.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 存在共享变量问题: [taskUndone]
 */
public class HatSettleTaskExecutor extends ThreadPoolExecutor {

	private final static Logger logger = LoggerFactory.getLogger(HatSettleTaskExecutor.class);

	private final AtomicLong taskUndone = new AtomicLong(0); //未完成的任务计数

	public HatSettleTaskExecutor(int corePoolSize, int maximumPoolSize, int queueCapacity) {
		super(corePoolSize, maximumPoolSize, 300, TimeUnit.SECONDS, new LinkedBlockingQueue<>(queueCapacity));
		this.setRejectedExecutionHandler(new RejectedExecutionHandler() {

			@Override
			public void rejectedExecution(Runnable task, ThreadPoolExecutor executor) {
				try {
					if (!executor.isShutdown()) {
						executor.getQueue().put(task);
					}
				} catch (InterruptedException e) {
					throw new RejectedExecutionException(
						"Executor was interrupted while the task was waiting to put on work queue", e);
				}
			}
		});

	}

	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		try {
			super.afterExecute(r, t);
		} finally {
			taskUndone.decrementAndGet();
		}
	}

	@Override
	public void execute(Runnable command) {
		super.execute(command);
		taskUndone.incrementAndGet();
	}

	public void join() {
		long lastLog = System.currentTimeMillis();
		logger.info("Waiting settle task executor to finish ..., queueSize={}, taskUndone={}", getQueue().size(),
			taskUndone.get());
		while (!this.getQueue().isEmpty() || taskUndone.get() != 0) {
			Thread.yield();
			long now = System.currentTimeMillis();
			if (now - lastLog > 10000) {
				lastLog = now;
				logger.info("Waiting settle task executor to finish ..., queueSize={}, taskUndone={}",
					getQueue().size(), taskUndone.get());
			}
		}
		logger.info("Settle task executor finished, queueSize={}, taskUndone={}", getQueue().size(), taskUndone.get());
	}

}
