package com.andyadc.scan.proxy;

import com.andyadc.summer.annotation.Component;
import com.andyadc.summer.annotation.Order;
import com.andyadc.summer.context.BeanPostProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@Order(100)
@Component
public class FirstProxyBeanPostProcessor implements BeanPostProcessor {

	final Logger logger = LoggerFactory.getLogger(getClass());

	Map<String, Object> originBeans = new HashMap<>();

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) {
		if (OriginBean.class.isAssignableFrom(bean.getClass())) {
			logger.debug("create first proxy for bean '{}': {}", beanName, bean);
			FirstProxyBean proxy = new FirstProxyBean((OriginBean) bean);
			originBeans.put(beanName, bean);
			return proxy;
		}
		return bean;
	}

	@Override
	public Object postProcessOnSetProperty(Object bean, String beanName) {
		Object origin = originBeans.get(beanName);
		if (origin != null) {
			logger.debug("auto set property for {} from first proxy {} to origin bean: {}", beanName, bean, origin);
			return origin;
		}
		return bean;
	}
}
