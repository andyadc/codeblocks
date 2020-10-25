package com.andyadc.codeblocks.test.singleton;

import org.junit.jupiter.api.Test;

/**
 * https://www.cnblogs.com/rjzheng/p/8946889.html
 *
 * @author andy.an
 * @since 2018/6/4
 */
public class SingletonTest {

    @Test
    public void testSingleton1() {
        System.out.println(Thread.currentThread().getName() + ": " + Singleton1.getInstance());
    }

    @Test
    public void testLazySingleton1() {
        System.out.println(Thread.currentThread().getName() + ": " + LazySingleton1.getInstance3());
    }

    @Test
    public void testLazySingleton2() {
        System.out.println(Thread.currentThread().getName() + ": " + LazySingleton2.getInstance());
    }
}
