package com.andyadc.codeblocks.lock.test;

import com.andyadc.codeblocks.lock.redis.SimpleRedisLock;
import org.databene.contiperf.PerfTest;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author andaicheng
 * @since 2018/4/22
 */
public class SimpleRedisLockTest {

    private static final String QQ_SERVER = "www.qq-server.com";
    private static final int REDIS_PORT = 6377;

    private static JedisPoolConfig config;

    static {
        config = new JedisPoolConfig();
        config.setMaxTotal(30);
        config.setMaxIdle(10);
    }

    private JedisPool jedisPool;

    @Before
    public void setup() {
        jedisPool = new JedisPool(config, QQ_SERVER, REDIS_PORT, 30000, "andyadc");
    }

    @PerfTest(threads = 10, invocations = 100)
    @Test
    public void ping() {
        Jedis jedis = jedisPool.getResource();
        System.out.println(jedis.ping());
    }

    //@PerfTest(threads = 10, invocations = 100)
    @Test
    public void lock() throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        for (int i = 0; i < 20; i++) {
            executorService.execute(new Locker(jedisPool));
        }
        executorService.shutdown();

        Thread.currentThread().join();
    }

    @Test
    public void lock2() {
        SimpleRedisLock lock = new SimpleRedisLock(jedisPool);
        System.out.println(lock.lock("adc", 100000, "home-pc"));
    }

    static class Locker implements Runnable {

        private JedisPool jedisPool;

        public Locker(JedisPool jedisPool) {
            this.jedisPool = jedisPool;
        }

        @Override
        public void run() {
            SimpleRedisLock lock = new SimpleRedisLock(jedisPool);
            System.out.println(lock.lock("adc", 100000, "home-pc"));
        }
    }
}
