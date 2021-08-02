package com.andyadc.codeblocks.framework.interceptor.cglib;

import com.andyadc.codeblocks.framework.interceptor.ReflectiveMethodInvocationContext;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class CglibMethodInvocationContext extends ReflectiveMethodInvocationContext {

	private final MethodProxy proxy;

	public CglibMethodInvocationContext(Object target, Method method, MethodProxy proxy, Object... parameters) {
		super(target, method, parameters);
		this.proxy = proxy;
	}

	@Override
	public Object proceed() throws Exception {
		try {
			return proxy.invokeSuper(getTarget(), getParameters());
		} catch (Throwable throwable) {
			throw new Exception(throwable);
		}
	}
}
