package com.andyadc.codeblocks.framework.lock;

import java.util.concurrent.TimeUnit;

/**
 * @author andy.an
 * @since 2019/1/15
 */
public interface DLock {

	boolean tryLock();

	boolean tryLock(long time, TimeUnit unit) throws InterruptedException;

	void unlock();

	String lockType();
}
