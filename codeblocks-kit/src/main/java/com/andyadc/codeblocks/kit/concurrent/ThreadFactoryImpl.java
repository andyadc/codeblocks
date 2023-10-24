package com.andyadc.codeblocks.kit.concurrent;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

public class ThreadFactoryImpl implements ThreadFactory {

	private final AtomicLong threadIndex = new AtomicLong(0);
	private final String threadNamePrefix;
	private final boolean daemon;

	public ThreadFactoryImpl(final String threadNamePrefix) {
		this(threadNamePrefix, false);
	}

	public ThreadFactoryImpl(final String threadNamePrefix, boolean daemon) {
		this.threadNamePrefix = threadNamePrefix;
		this.daemon = daemon;
	}

	@Override
	public Thread newThread(Runnable r) {
		Thread thread = new Thread(r, this.threadNamePrefix + this.threadIndex.incrementAndGet());
		thread.setDaemon(this.daemon);
		return thread;
	}
}
