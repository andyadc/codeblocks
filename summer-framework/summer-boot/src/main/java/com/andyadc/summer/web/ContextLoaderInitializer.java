package com.andyadc.summer.web;

import com.andyadc.summer.io.PropertyResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.Set;

public class ContextLoaderInitializer implements ServletContainerInitializer {

	final Logger logger = LoggerFactory.getLogger(getClass());

	final Class<?> configClass;
	final PropertyResolver propertyResolver;

	public ContextLoaderInitializer(Class<?> configClass, PropertyResolver propertyResolver) {
		this.configClass = configClass;
		this.propertyResolver = propertyResolver;
	}

	@Override
	public void onStartup(Set<Class<?>> set, ServletContext servletContext) throws ServletException {
		logger.info("Servlet container start. ServletContext = {}", servletContext);


	}

}
