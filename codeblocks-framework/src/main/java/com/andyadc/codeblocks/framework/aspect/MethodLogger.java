package com.andyadc.codeblocks.framework.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

/**
 * <ur>https://www.yegor256.com/2014/06/01/aop-aspectj-java-method-logging.html</ur>
 * MethodSignature.class.cast(point.getSignature()).getMethod().getName()
 *
 * @author andy.an
 * @since 2018/10/17
 */
@Aspect
public final class MethodLogger {

	private static final Logger logger = LoggerFactory.getLogger(MethodLogger.class);

	@Around("execution(* *(..)) && @annotation(com.andyadc.codeblocks.framework.aspect.Loggable)")
	public Object around(ProceedingJoinPoint point) throws Throwable {
		Instant start = Instant.now();
		String signature = point.getSignature().toShortString();
		Object result = null;
		try {
			result = point.proceed();
		} finally {
			logger.info(
				"Invoked {}, request={}, response={}, timing={}",
				signature,
				Arrays.toString(point.getArgs()),
				result,
				Duration.between(start, Instant.now()).toMillis()
			);
		}
		return result;
	}
}
