package com.andyadc.codeblocks.test.singleton;

import org.databene.contiperf.PerfTest;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * https://www.cnblogs.com/rjzheng/p/8946889.html
 *
 * @author andy.an
 * @since 2018/6/4
 */
public class SingletonTest {

    @Rule
    public ContiPerfRule rule = new ContiPerfRule();

    @PerfTest(threads = 10, invocations = 100)
    @Test
    public void testSingleton1() {
        System.out.println(Thread.currentThread().getName() + ": " + Singleton1.getInstance());
    }

    @PerfTest(threads = 10, invocations = 100)
    @Test
    public void testLazySingleton1() {
        System.out.println(Thread.currentThread().getName() + ": " + LazySingleton1.getInstance3());
    }

    @PerfTest(threads = 10, invocations = 100)
    @Test
    public void testLazySingleton2() {
        System.out.println(Thread.currentThread().getName() + ": " + LazySingleton2.getInstance());
    }
}
