package com.andyadc.codeblocks.kit.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * A {@link RejectedExecutionHandler} that blocks the caller until
 * the executor has room in its queue, or a timeout occurs (in which
 * case a {@link RejectedExecutionException} is thrown.
 *
 */
public class CallerBlocksPolicy implements RejectedExecutionHandler {

    private static final Logger logger = LoggerFactory.getLogger(CallerBlocksPolicy.class);

    private final long maxWait;

    /**
     * @param maxWait The maximum time to wait for a queue slot to be
     *                available, in milliseconds.
     */
    public CallerBlocksPolicy(long maxWait) {
        this.maxWait = maxWait;
    }

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        if (!executor.isShutdown()) {
            try {
                BlockingQueue<Runnable> queue = executor.getQueue();
                if (logger.isDebugEnabled()) {
                    logger.debug("Attempting to queue task execution for " + this.maxWait + " milliseconds");
                }
                if (!queue.offer(r, this.maxWait, TimeUnit.MILLISECONDS)) {
                    throw new RejectedExecutionException("Max wait time expired to queue task");
                }
                if (logger.isDebugEnabled()) {
                    logger.debug("Task execution queued");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RejectedExecutionException("Interrupted", e);
            }
        } else {
            throw new RejectedExecutionException("Executor has been shut down");
        }
    }

}
