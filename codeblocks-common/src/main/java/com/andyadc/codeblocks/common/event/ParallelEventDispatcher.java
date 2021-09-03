package com.andyadc.codeblocks.common.event;

import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;

/**
 * Parallel {@link EventDispatcher} implementation uses {@link ForkJoinPool#commonPool() JDK common thread pool}
 *
 * @see ForkJoinPool#commonPool()
 */
public class ParallelEventDispatcher extends AbstractEventDispatcher {

	public static final String NAME = "parallel";

	public ParallelEventDispatcher(Executor executor) {
		super(executor);
	}

	public ParallelEventDispatcher() {
		this(ForkJoinPool.commonPool());
	}
}
