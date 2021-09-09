package com.andyadc.codeblocks.interceptor;

import com.andyadc.codeblocks.common.reflect.ClassLoaderUtils;
import com.andyadc.codeblocks.interceptor.cglib.CglibComponentEnhancer;
import com.andyadc.codeblocks.interceptor.jdk.DynamicProxyComponentEnhancer;

/**
 * Default {@link ComponentEnhancer}
 */
public class DefaultComponentEnhancer implements ComponentEnhancer {

	private final ComponentEnhancer jdkProxyInterceptorEnhancer;

	private final ComponentEnhancer cglibInterceptorEnhancer;

	private final InterceptorManager interceptorManager;

	public DefaultComponentEnhancer() {
		this.jdkProxyInterceptorEnhancer = new DynamicProxyComponentEnhancer();
		this.cglibInterceptorEnhancer = new CglibComponentEnhancer();
		this.interceptorManager = InterceptorManager.getInstance(ClassLoaderUtils.getClassLoader(this.getClass()));
		this.interceptorManager.registerDiscoveredInterceptors();
	}

	@Override
	public <T> T enhance(T source, Class<? super T> componentClass, Object... defaultInterceptors) {
		assertType(componentClass);
		if (componentClass.isInterface()) {
			return jdkProxyInterceptorEnhancer.enhance(source, componentClass, defaultInterceptors);
		} else {
			return cglibInterceptorEnhancer.enhance(source, componentClass, defaultInterceptors);
		}
	}

	private <T> void assertType(Class<? super T> type) {
		if (type.isAnnotation() || type.isEnum() || type.isPrimitive() || type.isArray()) {
			throw new IllegalArgumentException("The type must be an interface or a class!");
		}
	}
}
