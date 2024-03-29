package com.andyadc.codeblocks.framework.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;

/**
 * 性能切面
 *
 */
public class PerformanceMonitor {

	private static final Logger logger = LoggerFactory.getLogger(PerformanceMonitor.class);

	public Object doAround(ProceedingJoinPoint point) throws Throwable {
		Instant begin = Instant.now();
		Object ret = point.proceed();
		logger.info("{}, method: {}, elapsed time: {} ms",
			point.getTarget().getClass(),
			point.getSignature().getName(),
			Duration.between(begin, Instant.now()).toMillis()
		);
		return ret;
	}
}
