package com.andyadc.codeblocks.test;

import com.andyadc.scaffold.showcase.auth.entity.AuthUser;
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

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    public void testOpsForValue() {
        AuthUser authUser = new AuthUser();
        authUser.setId(1L);
//        redisTemplate.opsForValue().set("date", authUser);
        Object o = redisTemplate.opsForValue().get("date");
        System.out.println(o);
    }
}
