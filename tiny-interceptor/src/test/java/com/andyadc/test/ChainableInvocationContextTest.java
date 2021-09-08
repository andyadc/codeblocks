package com.andyadc.test;

import com.andyadc.codeblocks.interceptor.ChainableInvocationContext;
import com.andyadc.codeblocks.interceptor.Interceptor;
import com.andyadc.codeblocks.interceptor.ReflectiveMethodInvocationContext;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

/**
 * {@link ChainableInvocationContext} Test
 */
public class ChainableInvocationContextTest {

	@Test
	public void test() throws Exception {
		EchoService echoService = new EchoService();
		Method method = EchoService.class.getMethod("echo", String.class);
		ReflectiveMethodInvocationContext delegateContext = new ReflectiveMethodInvocationContext
			(echoService, method, "Hello,World");

		ChainableInvocationContext context =
			new ChainableInvocationContext(delegateContext, Interceptor.loadInterceptors());

		context.proceed();
	}
}
