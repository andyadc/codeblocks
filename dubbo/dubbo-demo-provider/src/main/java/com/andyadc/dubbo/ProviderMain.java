package com.andyadc.dubbo;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author andy.an
 * @since 2018/11/14
 */
public class ProviderMain {

    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"classpath:applicationContext.xml"});
        context.start();

        System.in.read();
    }
}
