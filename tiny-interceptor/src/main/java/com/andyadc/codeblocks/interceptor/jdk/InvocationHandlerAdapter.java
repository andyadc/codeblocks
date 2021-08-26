package com.andyadc.codeblocks.interceptor.jdk;

import com.andyadc.codeblocks.interceptor.ChainableInvocationContext;
import com.andyadc.codeblocks.interceptor.ReflectiveMethodInvocationContext;

import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * {@link InvocationHandler} Adapter based on {@link Interceptor @Interceptor} class
 */
public class InvocationHandlerAdapter implements InvocationHandler {

	private final Object source;

	private final Object[] interceptors;

	public InvocationHandlerAdapter(Object source, Object... interceptors) {
		this.source = source;
		this.interceptors = interceptors;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		InvocationContext delegateContext = new ReflectiveMethodInvocationContext(source, method, args);
		ChainableInvocationContext context = new ChainableInvocationContext(delegateContext, interceptors);
		return context.proceed();
	}
}
