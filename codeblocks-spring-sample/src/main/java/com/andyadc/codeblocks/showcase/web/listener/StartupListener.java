package com.andyadc.codeblocks.showcase.web.listener;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;

/**
 * @author andy.an
 * @since 2018/6/7
 */
@Component
public class StartupListener implements ApplicationContextAware, ServletContextAware, InitializingBean, ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        System.out.println(">>> contextRefreshedEvent: " + contextRefreshedEvent);
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println(">>> afterPropertiesSet");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println(">>> applicationContext:" + applicationContext);
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        System.out.println(">>> servletContext:" + servletContext);
    }
}
