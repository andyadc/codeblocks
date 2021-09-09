package com.andyadc.codeblocks.interceptor.cglib;

import com.andyadc.codeblocks.interceptor.InterceptorEnhancer;
import net.sf.cglib.proxy.Enhancer;

import javax.interceptor.Interceptor;

/**
 * {@link Interceptor @Interceptor} enhancer by CGLIB
 */
@Deprecated
public class CglibInterceptorEnhancer implements InterceptorEnhancer {

	@Override
	public <T> T enhance(T source, Class<? super T> type, Object... interceptors) {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(type);
		enhancer.setCallback(new MethodInterceptorAdapter(source, interceptors));
		return (T) enhancer.create();
	}
}
