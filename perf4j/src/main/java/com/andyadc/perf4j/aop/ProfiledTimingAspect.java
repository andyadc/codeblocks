package com.andyadc.perf4j.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public abstract class ProfiledTimingAspect extends AbstractTimingAspect {

    /**
     * This advice is used to add the StopWatch logging statements around method executions that have been tagged
     * with the Profiled annotation.
     *
     * @param pjp      The ProceedingJoinPoint encapulates the method around which this aspect advice runs.
     * @param profiled The profiled annotation that was attached to the method.
     * @return The return value from the method that was executed.
     * @throws Throwable Any exceptions thrown by the underlying method.
     */
    @Around("execution(* *(..)) && @annotation(profiled)")
    public Object doPerfLogging(final ProceedingJoinPoint pjp, Profiled profiled) throws Throwable {

        if (profiled == null) {
            profiled = DefaultProfiled.INSTANCE;
        }

        return runProfiledMethod(pjp, profiled);
    }

}
