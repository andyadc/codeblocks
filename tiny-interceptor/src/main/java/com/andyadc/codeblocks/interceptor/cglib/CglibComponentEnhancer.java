package com.andyadc.codeblocks.interceptor.cglib;

import com.andyadc.codeblocks.interceptor.ComponentEnhancer;
import net.sf.cglib.proxy.Enhancer;

import javax.interceptor.Interceptor;

/**
 * {@link Interceptor @Interceptor} enhancer by CGLIB
 */
public class CglibComponentEnhancer implements ComponentEnhancer {

	@Override
	public <T> T enhance(T source, Class<? super T> componentClass, Object... defaultInterceptors) {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(componentClass);
		enhancer.setCallback(new MethodInterceptorAdapter(source, defaultInterceptors));
		return (T) enhancer.create();
	}
}
