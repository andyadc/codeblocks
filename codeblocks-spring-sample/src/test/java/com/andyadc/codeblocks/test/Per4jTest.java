package com.andyadc.codeblocks.test;

import com.andyadc.perf4j.StopWatch;
import com.andyadc.perf4j.slf4j.Slf4JStopWatch;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author andy.an
 * @since 2018/11/21
 */
public class Per4jTest {

    @Test
    public void test() throws Exception {
        for (int i = 0; i < 100; i++) {
            StopWatch stopWatch = new Slf4JStopWatch("tag1");
            TimeUnit.MILLISECONDS.sleep((long) Math.random() * 1000L);
            stopWatch.stop();
        }

    }
}
