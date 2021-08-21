package com.andyadc.codeblocks.common.event;

import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;

/**
 * Parallel {@link EventDispatcher} implementation uses {@link ForkJoinPool#commonPool() JDK common thread pool}
 *
 * @see ForkJoinPool#commonPool()
 * @since 1.0.0
 */
public class ParallelEventDispatcher extends AbstractEventDispatcher {
	public static final String NAME = "parallel";

	/**
	 * Constructor with an instance of {@link Executor}
	 *
	 * @param executor {@link Executor}
	 * @throws NullPointerException <code>executor</code> is <code>null</code>
	 */
	protected ParallelEventDispatcher(Executor executor) {
		super(executor);
	}

	public ParallelEventDispatcher() {
		this(ForkJoinPool.commonPool());
	}
}
