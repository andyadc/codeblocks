package com.andyadc.codeblocks.interceptor;

import com.andyadc.codeblocks.common.reflect.ClassLoaderUtils;
import com.andyadc.codeblocks.interceptor.cglib.CglibInterceptorEnhancer;
import com.andyadc.codeblocks.interceptor.jdk.DynamicProxyInterceptorEnhancer;

/**
 * Default {@link InterceptorEnhancer}
 */
public class DefaultInterceptorEnhancer implements InterceptorEnhancer {

	private final InterceptorEnhancer jdkProxyInterceptorEnhancer;

	private final InterceptorEnhancer cglibInterceptorEnhancer;

	private final InterceptorRegistry interceptorRegistry;

	public DefaultInterceptorEnhancer() {
		this.jdkProxyInterceptorEnhancer = new DynamicProxyInterceptorEnhancer();
		this.cglibInterceptorEnhancer = new CglibInterceptorEnhancer();
		this.interceptorRegistry = InterceptorRegistry.getInstance(ClassLoaderUtils.getClassLoader(this.getClass()));
		this.interceptorRegistry.registerDiscoveredInterceptors();
	}

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
