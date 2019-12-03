package com.andyadc.codeblocks.framework.concurrent;

import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskDecorator;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.Map;
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
		executor.setThreadNamePrefix("Default-");
		executor.setCorePoolSize(2);
		executor.setMaxPoolSize(4);
		executor.setQueueCapacity(100);
		executor.setKeepAliveSeconds(60);
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		executor.setTaskDecorator(new MDCTaskDecorator());
		executor.initialize();
		return executor;
	}

	class MDCTaskDecorator implements TaskDecorator {

		@Override
		@NonNull
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
