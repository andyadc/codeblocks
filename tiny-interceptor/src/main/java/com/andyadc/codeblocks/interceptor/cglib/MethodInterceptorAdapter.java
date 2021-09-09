package com.andyadc.codeblocks.interceptor.cglib;

import com.andyadc.codeblocks.interceptor.ChainableInvocationContext;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import javax.interceptor.InvocationContext;
import java.lang.reflect.Method;

public class MethodInterceptorAdapter implements MethodInterceptor {

	private final Object target;

	private final Object[] additionalInterceptors;

	public MethodInterceptorAdapter(Object target, Object... additionalInterceptors) {
		this.target = target;
		this.additionalInterceptors = additionalInterceptors;
	}

	@Override
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		InvocationContext delegateContext = new CglibMethodInvocationContext(obj, method, proxy, args);
		ChainableInvocationContext context = new ChainableInvocationContext(delegateContext, additionalInterceptors);
		return context.proceed();
	}
}
