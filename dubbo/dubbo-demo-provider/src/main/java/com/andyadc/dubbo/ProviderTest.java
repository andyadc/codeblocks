package com.andyadc.dubbo;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * TEST
 *
 * @author andy.an
 * @since 2018/11/14
 */
public class ProviderTest {

    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"classpath:spring.xml"});
        context.start();

        System.in.read();
    }
}
