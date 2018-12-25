package com.andyadc.codeblocks.showcase.prop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;

import java.beans.PropertyDescriptor;

/**
 * @author andy.an
 * @since 2018/12/24
 */
//@Component
public class ConfigFactory extends InstantiationAwareBeanPostProcessorAdapter
        implements InitializingBean, DisposableBean, BeanNameAware, BeanFactoryAware {

    private static final Logger logger = LoggerFactory.getLogger(ConfigFactory.class);

    private String beanName;
    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }

    @Override
    public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        System.out.println("beanName=" + beanName + ", bean=" + bean);
        PropertyValue[] pva = pvs.getPropertyValues();
        for (PropertyValue pv : pva) {
            System.out.println("name=" + pv.getName() + ", value=" + pv.getValue());
        }

        return super.postProcessProperties(pvs, bean, beanName);
    }

    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName) throws BeansException {

        return super.postProcessPropertyValues(pvs, pds, bean, beanName);
    }

    @Override
    public void afterPropertiesSet() {

    }

    @Override
    public void destroy() {

    }
}
