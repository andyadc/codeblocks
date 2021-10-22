package com.andyadc.test.kafka;

import org.apache.kafka.common.TopicPartition;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

public class ConsumerPool {
	private final PollThread pollThread;
	private final AtomicLong requestId = new AtomicLong(0);
	private final Map<TopicPartition, ThreadPoolExecutor> executorServices = new HashMap<>();
	private final ReentrantLock threadLock = new ReentrantLock();
	private final AtomicInteger threadNum = new AtomicInteger(0);
	private final Integer maxQueueInMsgs = 1000;

	public ConsumerPool(PollThread pollThread) {
		this.pollThread = pollThread;
	}

	/**
	 * 该方法只被 PoolThread 方法调用，线程安全
	 *
	 * @param consumerTask
	 * @return
	 */
	public void submitConsumerTask(ConsumerTask consumerTask) {
		ThreadPoolExecutor executor = get(consumerTask.topicPartition);
		if (executor.getQueue().size() > maxQueueInMsgs) {
			executor.submit(consumerTask);
			// 消息消费积压，需要暂停消息拉取
			pollThread.addNeedPause(consumerTask.topicPartition);
		}
	}

	private ThreadPoolExecutor get(TopicPartition topicPartition) {
		try {
			threadLock.lock();
			ThreadPoolExecutor executor = executorServices.get(topicPartition);
			if (executor == null) {
				executor = newThread();
				executorServices.put(topicPartition, executor);
			}
			return executor;

		} finally {
			threadLock.unlock();
		}
	}

	private ThreadPoolExecutor newThread() {
		return new ThreadPoolExecutor(1, 1, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(2000), new ThreadFactory() {

			@Override
			public Thread newThread(Runnable r) {
				Thread t = new Thread(r);
				t.setName("ConsumerMessageThread-" + threadNum.incrementAndGet());
				return t;
			}
		});
	}

}
