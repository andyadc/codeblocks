package com.andyadc.codeblocks.lock.test;

import org.apache.zookeeper.ZooKeeper;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author andy.an
 * @since 2018/4/27
 */
public class SimpleZookeeperLockTest {

    private static final String ZK_SERVER = "www.jd-server.com:2181";

    static int num = 0;
    static AtomicInteger ai = new AtomicInteger(0);

    @Rule
    public ContiPerfRule rule = new ContiPerfRule();

    private static ZooKeeper newZookeeper() throws IOException {
        System.out.printf("%s: Creating new ZooKeeper%n", Thread.currentThread().getName());
        return new ZooKeeper(ZK_SERVER, 3000, (event) -> {
        });
    }

    private static void closeZooKeeper(ZooKeeper zk) throws InterruptedException {
        System.out.printf("%s: Closing ZooKeeper%n", Thread.currentThread().getName());
        zk.close();
    }

    //@PerfTest(threads = 10, invocations = 100)
    @Test
    public void testLock() throws Exception {
        CountDownLatch latch = new CountDownLatch(100000);

        ExecutorService executor = Executors.newFixedThreadPool(100);
        for (int i = 0; i < 100000; i++) {
            executor.execute(new Locker(latch));
        }
        executor.shutdown();

        latch.await();
        System.out.println(num);
        System.out.println(ai);
    }

    static class Locker implements Runnable {

        private CountDownLatch latch;
        private String lockName;

        public Locker(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public void run() {
            try {
                ZooKeeper zooKeeper = newZookeeper();

            } catch (Exception e) {

            }

            num++;
            ai.incrementAndGet();
            latch.countDown();
        }
    }
}
