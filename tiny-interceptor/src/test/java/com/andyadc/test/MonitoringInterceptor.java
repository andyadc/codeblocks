package com.andyadc.test;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

/**
 * {@link MonitoringInterceptor} from Java Interceptor Specification
 */
@Monitored
@javax.interceptor.Interceptor
public class MonitoringInterceptor {

	@AroundInvoke
	public Object monitorInvocation(InvocationContext context) throws Exception {
		return context.proceed();
	}
}
