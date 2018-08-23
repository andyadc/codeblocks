package com.andyadc.codeblocks.test;

/**
 * @author andy.an
 * @since 2018/8/21
 */
public class Foo {
    public static void main(String[] args) {
        Foo foo = new Foo();
    }

    public static synchronized void staticSync() {
        System.out.println("static synchronized");
    }

    public synchronized void sync() {
        System.out.println("synchronized");
    }

    public void blockSync() {
        synchronized (this) {
            System.out.println("block synchronized");
        }
    }
}
