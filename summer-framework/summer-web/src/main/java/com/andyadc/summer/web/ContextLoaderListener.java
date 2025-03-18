package com.andyadc.summer.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ContextLoaderListener implements ServletContextListener {

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		logger.info("init {}.", getClass().getName());
		ServletContext servletContext = sce.getServletContext();
		WebMvcConfiguration.setServletContext(servletContext);


	}

}
