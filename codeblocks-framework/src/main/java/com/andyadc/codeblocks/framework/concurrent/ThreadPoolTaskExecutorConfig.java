package com.andyadc.codeblocks.framework.concurrent;

import com.andyadc.codeblocks.common.Constants;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskDecorator;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * andy.an
 * 2019/12/2
 */
@Component
public class ThreadPoolTaskExecutorConfig {

	@Bean(value = "defaultTaskExecutor")
	public ThreadPoolTaskExecutor defaultTaskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setThreadNamePrefix("DefaultThreadPool-");
		executor.setCorePoolSize(Constants.PROCESSOR_NUM);
		executor.setMaxPoolSize(Constants.PROCESSOR_NUM * 2);
		executor.setQueueCapacity(100);
		executor.setKeepAliveSeconds(60);
		executor.setRejectedExecutionHandler(rejectedExecutionHandler());
		executor.setTaskDecorator(new MDCTaskDecorator());
		return executor;
	}

	private RejectedExecutionHandler rejectedExecutionHandler() {
		return new ThreadPoolExecutor.CallerRunsPolicy();
	}

	static class MDCTaskDecorator implements TaskDecorator {

		@NonNull
		@Override
		public Runnable decorate(@NonNull Runnable runnable) {
			Map<String, String> contextMap = MDC.getCopyOfContextMap();
			return () -> {
				try {
					if (contextMap != null) {
						MDC.setContextMap(contextMap);
					}
					runnable.run();
				} finally {
					MDC.clear();
				}
			};
		}
	}
}
