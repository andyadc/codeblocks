package com.andyadc.codeblocks.test;

import com.andyadc.scaffold.showcase.auth.entity.AuthUser;
import org.databene.contiperf.PerfTest;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author andy.an
 * @since 2018/5/28
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/applicationContext.xml"})
public class RedisTemplateTest {

    @Rule
    public ContiPerfRule rule = new ContiPerfRule();

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @PerfTest(threads = 10, invocations = 1000)
    @Test
    public void testIncrement() {
        Long num = redisTemplate.opsForValue().increment("adc-vote-num", 1);
        System.out.println(Thread.currentThread().getName() + ", vote no: " + num);
    }

    @PerfTest(threads = 10, invocations = 1000)
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
