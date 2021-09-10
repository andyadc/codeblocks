package com.andyadc.test;

import javax.annotation.PostConstruct;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.util.logging.Logger;

@Logging
@Interceptor
public class ExtLoggingInterceptor extends LoggingInterceptor {

	@PostConstruct
	public void postConstruct(InvocationContext context) throws Exception {
		Logger logger = Logger.getLogger(getClass().getName());
		logger.info("postConstruct");
	}
}
