package com.andyadc.codeblocks.interceptor;

import com.andyadc.codeblocks.interceptor.cglib.CglibInterceptorEnhancer;
import com.andyadc.codeblocks.interceptor.jdk.DynamicProxyInterceptorEnhancer;

/**
 * Default {@link InterceptorEnhancer}
 */
public class DefaultInterceptorEnhancer implements InterceptorEnhancer {

	private final InterceptorEnhancer jdkProxyInterceptorEnhancer = new DynamicProxyInterceptorEnhancer();

	private final InterceptorEnhancer cglibInterceptorEnhancer = new CglibInterceptorEnhancer();

	@Override
	public <T> T enhance(T source, Class<? super T> type, Object... interceptors) {
		assertType(type);
		if (type.isInterface()) {
			return jdkProxyInterceptorEnhancer.enhance(source, type, interceptors);
		} else {
			return cglibInterceptorEnhancer.enhance(source, type, interceptors);
		}
	}

	private <T> void assertType(Class<? super T> type) {
		if (type.isAnnotation() || type.isEnum() || type.isPrimitive() || type.isArray()) {
			throw new IllegalArgumentException("The type must be an interface or a class!");
		}
	}
}
