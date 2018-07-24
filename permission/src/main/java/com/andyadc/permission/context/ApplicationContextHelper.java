package com.andyadc.permission.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author andy.an
 * @since 2018/7/23
 */
@Component
public class ApplicationContextHelper implements ApplicationContextAware {

    private static ApplicationContext context;

    public static <T> T popBean(Class<T> clazz) {
        if (context == null) {
            return null;
        }
        return context.getBean(clazz);
    }

    public static <T> T popBean(Class<T> clazz, String name) {
        if (context == null) {
            return null;
        }
        return context.getBean(clazz, name);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}
