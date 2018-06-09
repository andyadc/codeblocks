package com.andyadc.codeblocks.framework.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * @author andy.an
 * @since 2017/8/25
 */
public class LogAspect {

    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    public Object log(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long from = System.currentTimeMillis();
        String signature = proceedingJoinPoint.getSignature().toShortString();
        logger.info("Invoking {} with request {}", signature, Arrays.toString(proceedingJoinPoint.getArgs()));
        Object result = null;
        try {
            result = proceedingJoinPoint.proceed();
        } finally {
            long to = System.currentTimeMillis();
            logger.info("Invoked {}, request {}, response {}, cost={}", signature,
                    Arrays.toString(proceedingJoinPoint.getArgs()), result, (to - from));
        }
        return result;
    }
}
