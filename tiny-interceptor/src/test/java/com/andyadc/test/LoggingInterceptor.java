package com.andyadc.test;


import com.andyadc.codeblocks.interceptor.AnnotatedInterceptor;

import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.lang.reflect.Method;
import java.util.logging.Logger;

@Logging
@Interceptor
public class LoggingInterceptor extends AnnotatedInterceptor<Logging> {

	@Override
	protected Object intercept(InvocationContext context, Logging logging) throws Throwable {
		Logger logger = Logger.getLogger(logging.name());
		logger.info((String) context.getParameters()[0]);
		return context.proceed();
	}

	@Override
	protected void beforePostConstruct(Object target, Method method) throws Throwable {
		Logger logger = Logger.getLogger(getClass().getName());
		logger.info("target : " + target.getClass().getName());
		logger.info("method : " + method.getName());
	}
}
