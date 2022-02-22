package com.andyadc.codeblocks.kit.concurrent;

import org.slf4j.MDC;

import java.util.Map;

/**
 * Slf4j 异步线程设置 MDC
 *
 */
public class MDCRunnable implements Runnable {

	private final Runnable runnable;
	private transient final Map<String, String> map;

	public MDCRunnable(Runnable runnable) {
		this.runnable = runnable;
		// 保存当前线程的MDC值
		this.map = MDC.getCopyOfContextMap();
	}

	@Override
	public void run() {
		if (map != null) {
			MDC.setContextMap(map);
		}
		try {
			runnable.run();
		} finally {
			MDC.clear();
		}
	}
}
