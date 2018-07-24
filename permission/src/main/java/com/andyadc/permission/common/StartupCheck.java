package com.andyadc.permission.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author andy.an
 * @since 2018/6/7
 */
@Component
public class StartupCheck implements ApplicationListener<ContextRefreshedEvent> {

    private RedisTemplate redisTemplate;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        System.out.println(">>> contextRefreshedEvent: " + event);

        Object val = redisTemplate.execute((RedisConnection connection) -> connection.ping());

        System.out.println(val);

    }

    @Autowired
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
}
