package com.andyadc.codeblocks.framework.log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <ur>https://www.yegor256.com/2014/06/01/aop-aspectj-java-method-logging.html</ur>
 *
 * @author andy.an
 * @since 2018/10/17
 */
@Aspect
public class MethodLogger {

    private static final Logger logger = LoggerFactory.getLogger(MethodLogger.class);

    @Around("execution(* *(..)) && @annotation(Loggable)")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = point.proceed();

        System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^");

        logger.info(
                "#%s(%s): %s in %[msec]s",
                MethodSignature.class.cast(point.getSignature()).getMethod().getName(),
                point.getArgs(),
                result,
                System.currentTimeMillis() - start
        );
        return result;
    }
}
