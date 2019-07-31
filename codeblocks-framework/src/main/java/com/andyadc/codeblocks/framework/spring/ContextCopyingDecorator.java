package com.andyadc.codeblocks.framework.spring;

import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Map;

/**
 * andy.an
 *
 * <url>https://stackoverflow.com/questions/23732089/how-to-enable-request-scope-in-async-task-executor</url>
 *
 * <code>
 * public Executor getAsyncExecutor() {
 * ThreadPoolTaskExecutor poolExecutor = new ThreadPoolTaskExecutor();
 * poolExecutor.setTaskDecorator(new ContextCopyingDecorator());
 * poolExecutor.initialize();
 * return poolExecutor;
 * }
 * </code>
 */
public class ContextCopyingDecorator implements TaskDecorator {

	@Override
	public Runnable decorate(Runnable runnable) {
		RequestAttributes attributes =
			RequestContextHolder.currentRequestAttributes();
		Map<String, String> contextMap = MDC.getCopyOfContextMap();

		return () -> {
			try {
				RequestContextHolder.setRequestAttributes(attributes);
				MDC.setContextMap(contextMap);
				runnable.run();
			} finally {
				RequestContextHolder.resetRequestAttributes();
				MDC.clear();
			}
		};
	}
}
