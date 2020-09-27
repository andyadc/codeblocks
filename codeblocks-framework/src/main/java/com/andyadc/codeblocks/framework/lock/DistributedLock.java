package com.andyadc.codeblocks.framework.lock;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public interface DistributedLock<T> {

	<T> T lock(String key, long waitTimes, TimeUnit unit, Supplier<T> success, Supplier<T> fail);
}
