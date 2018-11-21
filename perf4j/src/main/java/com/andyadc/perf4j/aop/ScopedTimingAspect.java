package com.andyadc.perf4j.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public abstract class ScopedTimingAspect extends AbstractTimingAspect {

    @Pointcut
    public abstract void scope();

    @Around("scope()")
    public Object doPerfLoggingNotProfiled(final ProceedingJoinPoint pjp) throws Throwable {
        return runProfiledMethod(pjp, DefaultProfiled.INSTANCE);
    }

}
