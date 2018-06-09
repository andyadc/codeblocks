package com.andyadc.codeblocks.framework.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;

/**
 * 性能切面
 *
 * @author andaicheng
 */
public class PerformanceMonitor {

    private static final Logger logger = LoggerFactory.getLogger(PerformanceMonitor.class);

    public Object doAround(ProceedingJoinPoint point) throws Throwable {
        Instant begin = Instant.now();
        Object ret = point.proceed();
        Instant end = Instant.now();
        logger.info("{}, method: {}, elapsed time: {}ms", point.getTarget().getClass(), point.getSignature().getName(),
                Duration.between(begin, end).toMillis());
        return ret;
    }
}
