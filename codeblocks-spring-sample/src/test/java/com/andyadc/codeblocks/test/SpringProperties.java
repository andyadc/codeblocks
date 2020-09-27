package com.andyadc.codeblocks.test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;
import java.util.Map;

/**
 * @author andy.an
 * @since 2018/12/7
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath:/applicationContext.xml"})
public class SpringProperties {

    @Value("${redis.pool.maxIdle}")
    public String value;
    @Autowired
    private ConfigurableEnvironment configurableEnvironment;

    @Test
    public void test() {
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        System.out.println(value + "\r\n");

        System.out.println(configurableEnvironment + "\r\n");
        System.out.println(configurableEnvironment.getPropertySources() + "\r\n");
        System.out.println(configurableEnvironment.getSystemEnvironment() + "\r\n");
        System.out.println(configurableEnvironment.getSystemProperties() + "\r\n");
        System.out.println(configurableEnvironment.getProperty("redis.pool.maxIdle") + "\r\n");

        Map<String, Object> source = new HashMap<>();
        source.put("redis.pool.maxIdle", "45");
        MyPropertySource myPropertySource = new MyPropertySource("adc", source);
        configurableEnvironment.getPropertySources().addFirst(myPropertySource);

        System.out.println(configurableEnvironment.getPropertySources() + "\r\n");
        System.out.println(configurableEnvironment.getProperty("redis.pool.maxIdle") + "\r\n");

        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }

    static class MyPropertySource extends MapPropertySource {

        public MyPropertySource(String name, Map<String, Object> source) {
            super(name, source);
        }

    }
}
