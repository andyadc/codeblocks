package com.andyadc.codeblocks.test;

import com.andyadc.scaffold.showcase.auth.entity.AuthUser;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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
