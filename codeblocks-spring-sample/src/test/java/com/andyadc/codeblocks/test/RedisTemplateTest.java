package com.andyadc.codeblocks.test;

import com.andyadc.codeblocks.showcase.auth.entity.AuthUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author andy.an
 * @since 2018/5/28
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath:/applicationContext.xml"})
public class RedisTemplateTest {

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@Test
	public void testPing() {
		String ping = redisTemplate.execute(RedisConnection::ping);
		System.out.println("PING > " + ping);
	}

	@Test
	public void testIncrement() {
		Long num = redisTemplate.opsForValue().increment("adc-vote-num", 1);
		System.out.println(Thread.currentThread().getName() + ", vote no: " + num);
	}

	@Test
	public void testRedisAtomicLong() {
		RedisAtomicLong counter = new RedisAtomicLong("adc-vote-no", redisTemplate.getConnectionFactory());
		System.out.println(Thread.currentThread().getName() + ", vote no: " + counter.getAndIncrement());
	}

	@Test
	public void testStringSet() {
		AuthUser authUser = new AuthUser();
		authUser.setId(1L);
		redisTemplate.opsForValue().set("authUser", authUser);
	}

	//    @PerfTest(threads = 10, invocations = 1000)
	@Test
	public void testStringGet() {
		Object o = redisTemplate.opsForValue().get("authUser");
		System.out.println(o);
	}
}
