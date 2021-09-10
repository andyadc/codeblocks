package com.andyadc.test;

import javax.annotation.PostConstruct;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 * External {@link Interceptor @Interceptor}
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
@Interceptor
public class ExternalInterceptor {

	private final Logger logger = Logger.getLogger(getClass().getName());

	private final Set<String> methodNames = new LinkedHashSet<>();

	@AroundInvoke
	public Object intercept(InvocationContext context) throws Throwable {
		String methodName = context.getMethod().getName();
		methodNames.add(methodName);
		logger.info("Interception Method : " + methodName);
		return context.proceed();
	}

	@PostConstruct
	public void postConstruct(InvocationContext context) throws Exception {
		String methodName = context.getMethod().getName();
		methodNames.add(methodName);
		logger.info("Post Construct : " + context.getMethod().getName());
		context.proceed();
	}

	public Set<String> getMethodNames() {
		return methodNames;
	}
}
