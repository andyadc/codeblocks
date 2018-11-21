package com.andyadc.perf4j.slf4j.aop;

import com.andyadc.perf4j.slf4j.Slf4JStopWatch;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.LoggerFactory;

/**
 * @author Brett Randall
 */
@Aspect
public abstract class ScopedTimingAspect extends com.andyadc.perf4j.aop.ScopedTimingAspect {

    protected Slf4JStopWatch newStopWatch(String loggerName, String levelName) {
        int levelInt = Slf4JStopWatch.mapLevelName(levelName);
        return new Slf4JStopWatch(LoggerFactory.getLogger(loggerName), levelInt, levelInt);
    }

}
