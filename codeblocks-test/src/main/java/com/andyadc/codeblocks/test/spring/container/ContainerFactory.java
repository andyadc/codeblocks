package com.andyadc.codeblocks.test.spring.container;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

@Service
public class ContainerFactory implements BeanFactoryAware, ApplicationContextAware, ApplicationListener<ContextRefreshedEvent> {

	private ApplicationContext context;
	private ApplicationContext applicationContext;
	private ApplicationContext eventContext;
	private BeanFactory beanFactory;

	@Autowired
	public void setContext(ApplicationContext context) {
		this.context = context;
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		this.eventContext = event.getApplicationContext();
	}
}
