package com.andyadc.codeblocks.test.singleton;

/**
 * 饿汉式
 *
 * @author andy.an
 * @since 2018/6/4
 */
public class Singleton1 {

    private static final Singleton1 INSTANCE = new Singleton1();

    private Singleton1() {
    }

    public static Singleton1 getInstance() {
        return INSTANCE;
    }
}
