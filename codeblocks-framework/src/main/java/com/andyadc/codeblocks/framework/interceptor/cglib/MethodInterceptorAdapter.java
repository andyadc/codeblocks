package com.andyadc.codeblocks.framework.interceptor.cglib;

import com.andyadc.codeblocks.framework.interceptor.ChainableInvocationContext;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import javax.interceptor.InvocationContext;
import java.lang.reflect.Method;

public class MethodInterceptorAdapter implements MethodInterceptor {

	private final Object target;

	private final Object[] interceptors;

	public MethodInterceptorAdapter(Object target, Object[] interceptors) {
		this.target = target;
		this.interceptors = interceptors;
	}

	@Override
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		InvocationContext delegateContext = new CglibMethodInvocationContext(obj, method, proxy, args);
		ChainableInvocationContext context = new ChainableInvocationContext(delegateContext, interceptors);
		return context.proceed();
	}
}
