package com.andyadc.codeblocks.test.singleton;

/**
 * 1. 加载一个类时，其内部类不会同时被加载。
 * 2. 一个类被加载，当且仅当其某个静态成员（静态域、构造器、静态方法等）被调用时发生。。
 *
 * @author andy.an
 * @since 2018/6/4
 */
public class InnerClassTest {

    static {
        System.out.println("load outer class...");
    }

    public static void main(String[] args) {
        InnerClassTest test = new InnerClassTest();
        System.out.println("\r\n -----------------------------");
        InnerClassTest.StaticInner.staticInnerMethod();
    }

    static class StaticInner {
        static {
            System.out.println("load static inner class...");
        }

        static void staticInnerMethod() {
            System.out.println("static inner method...");
        }
    }
}
