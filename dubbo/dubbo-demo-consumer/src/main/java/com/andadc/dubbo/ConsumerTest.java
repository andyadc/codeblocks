package com.andadc.dubbo;

import com.andyadc.dubbo.api.DemoService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author andy.an
 * @since 2018/11/14
 */
public class ConsumerTest {

    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"classpath:spring.xml"});
        context.start();

        DemoService demoService = (DemoService) context.getBean("demoService");

        for (int i = 0; i < 100; i++) {
            System.out.println(demoService.hello("num=" + i));

            Thread.sleep(1000);
        }
    }
}
