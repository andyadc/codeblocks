package com.andyadc.codeblocks.test.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * @author andy.an
 * @since 2018/5/24
 */
public class ThreadRun {

    public static void main(String[] args) {
        new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                System.out.println(Thread.currentThread().getName() + '-' + i);
            }
        }).start();

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 100; i++) {
            executorService.execute(() ->
                    System.out.println(Thread.currentThread().getName())
            );
        }
        executorService.shutdown();

        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        threadFactory.newThread(() -> {
            for (int i = 0; i < 100; i++) {
                System.out.println(Thread.currentThread().getName() + '-' + i);
            }
        }).start();

        System.out.println("main");
    }
}
