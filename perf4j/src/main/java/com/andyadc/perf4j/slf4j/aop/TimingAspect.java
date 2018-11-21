package com.andyadc.perf4j.slf4j.aop;

import com.andyadc.perf4j.aop.ProfiledTimingAspect;
import com.andyadc.perf4j.slf4j.Slf4JStopWatch;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.LoggerFactory;

/**
 * This TimingAspect implementation uses a SLF4J Logger instance to persist StopWatch log messages.
 *
 * @author Alex Devine
 */
@Aspect
public class TimingAspect extends ProfiledTimingAspect {

    protected Slf4JStopWatch newStopWatch(String loggerName, String levelName) {
        int levelInt = Slf4JStopWatch.mapLevelName(levelName);
        return new Slf4JStopWatch(LoggerFactory.getLogger(loggerName), levelInt, levelInt);
    }

}