package com.andyadc.codeblocks.test.concurrent;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author andy.an
 * @since 2018/10/30
 */
public class ThreadPoolExceptionTest {

    public static void main(String[] args) {

        System.out.println(Runtime.getRuntime().availableProcessors());
        System.out.println();

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                2,
                Integer.MAX_VALUE,
                0L,
                TimeUnit.MILLISECONDS,
                new SynchronousQueue<>());

        for (int i = 0; i < 5; i++) {
            executor.execute(new ExceptionTask(100, i));
        }

        executor.shutdown();
    }

    static class ExceptionTask implements Runnable {

        int a, b;

        public ExceptionTask(int a, int b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public void run() {
            System.out.println(a / b);
        }
    }
}
