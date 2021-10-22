package com.andyadc.test.redis;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Pipeline;

/**
 * @author andy.an
 * @since 2018/12/4
 */
public class RedisTest {

	private static final String HOST = "192.168.55.9";
	private static final int PORT = 6379;
//    private static final String HOST = "www.qq-server.com";
//    private static final int PORT = 6377;

	private static final JedisPool pool;
	private static Jedis jedis;

	static {
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(30);
		config.setMaxIdle(10);
		config.setTestOnBorrow(true);
		config.setTestOnReturn(true);

//        pool = new JedisPool(config, HOST, PORT, 3000, "andyadc");
		pool = new JedisPool(config, HOST, PORT, 3000);
	}

	@BeforeAll
    public static void before() {
        jedis = pool.getResource();
    }

	@AfterAll
    public static void after() {
        jedis.close();
    }

    @Test
    public void ping() {
        System.out.println(jedis.ping());
    }

    @Test
    public void testSet() {
        jedis.flushDB();

        long start = System.currentTimeMillis();

        for (int i = 0; i < 1000; i++) {
            jedis.set("key" + i, "v" + i);
        }
		System.out.println("Elapsed time: " + (System.currentTimeMillis() - start));
    }

    @Test
    public void testPipeline() {
        jedis.flushDB();

        Pipeline pipeline = jedis.pipelined();
        long start = System.currentTimeMillis();

        for (int i = 0; i < 1000; i++) {
            pipeline.set("key" + i, "v" + i);

            if (i % 100 == 0) {
                pipeline.sync();
            }
        }

		System.out.println("Elapsed time: " + (System.currentTimeMillis() - start));
    }

    @Test
    public void testKey() {
        System.out.println("flush db: " + jedis.flushDB());

        System.out.println("exist: " + jedis.exists("username"));
        System.out.println("set" + jedis.set("username", "andyadc"));
        System.out.println(jedis.get("username"));

        jedis.expire("username", 10);
        jedis.ttl("username");
        jedis.persist("username");

    }
}
