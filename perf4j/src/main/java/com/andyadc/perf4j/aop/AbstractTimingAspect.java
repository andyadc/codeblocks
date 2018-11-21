package com.andyadc.perf4j.aop;

import com.andyadc.perf4j.LoggingStopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;

/**
 * This is the base class for TimingAspects that use the AspectJ framework (a better name for this class work probably
 * be AspectJTimingAspect, but for backwards compatibility reasons it keeps the AbstractTimingAspect name).
 * Subclasses just need to implement the {@link #newStopWatch} method to use their logging framework of choice
 * (e.g. log4j or java.logging) to persist the StopWatch log message.
 *
 * @author Alex Devine
 */
@Aspect
public abstract class AbstractTimingAspect extends AgnosticTimingAspect {

    protected Object runProfiledMethod(final ProceedingJoinPoint pjp, Profiled profiled) throws Throwable {
        //We just delegate to the super class, wrapping the AspectJ-specific ProceedingJoinPoint as an AbstractJoinPoint
        return runProfiledMethod(
                new AbstractJoinPoint() {
                    public Object proceed() throws Throwable {
                        return pjp.proceed();
                    }

                    public Object getExecutingObject() {
                        return pjp.getThis();
                    }

                    public Object[] getParameters() {
                        return pjp.getArgs();
                    }

                    public String getMethodName() {
                        return pjp.getSignature().getName();
                    }

                    public Class<?> getDeclaringClass() {
                        return pjp.getSignature().getDeclaringType();
                    }
                },
                profiled,
                newStopWatch(profiled.logger() + "", profiled.level())
        );
    }

    /**
     * Subclasses should implement this method to return a LoggingStopWatch that should be used to time the wrapped
     * code block.
     *
     * @param loggerName The name of the logger to use for persisting StopWatch messages.
     * @param levelName  The level at which the message should be logged.
     * @return The new LoggingStopWatch.
     */
    protected abstract LoggingStopWatch newStopWatch(String loggerName, String levelName);
}
