package com.andyadc.scaffold.showcase.common.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.util.Properties;

/**
 * 扩展标准的PropertyPlaceholderConfigurer把属性文件中的配置参数信息放入全局Map变量便于其他接口访问key-value配置数据
 *
 * @author andaicheng
 * @version 2017/5/9
 */
public class ExtPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

    private static final Logger LOG = LoggerFactory.getLogger(ExtPropertyPlaceholderConfigurer.class);

    // TODO
    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {
        super.processProperties(beanFactoryToProcess, props);

        for (Object key : props.keySet()) {
            LOG.info("key: {}, value: {}", key, props.getProperty(key.toString()));
        }
    }
}
