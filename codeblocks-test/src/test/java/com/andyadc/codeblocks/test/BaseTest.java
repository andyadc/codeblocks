package com.andyadc.codeblocks.test;

import org.databene.contiperf.PerfTest;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author andy.an
 * @since 2018/4/12
 */
public class BaseTest {

    @Rule
    public ContiPerfRule rule = new ContiPerfRule();

    @PerfTest(threads = 10, invocations = 100) // threads并发线程数量，invocations总调用次数
    @Test
    public void numRange() {
        System.out.println(Long.MAX_VALUE);
        System.out.println(Long.MIN_VALUE);
        System.out.println(Integer.MAX_VALUE);
        System.out.println(Integer.MIN_VALUE);
    }
}
