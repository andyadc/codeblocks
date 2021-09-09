package com.andyadc.codeblocks.interceptor.jdk;

import com.andyadc.codeblocks.common.reflect.ClassLoaderUtils;
import com.andyadc.codeblocks.interceptor.ComponentEnhancer;

import java.lang.reflect.Proxy;

/**
 * {@link ComponentEnhancer} based on JDK Dynamic Proxy
 */
public class DynamicProxyComponentEnhancer implements ComponentEnhancer {

	@Override
	public <T> T enhance(T source, Class<? super T> componentClass, Object... defaultInterceptors) {
		ClassLoader classLoader = ClassLoaderUtils.getClassLoader(componentClass);
		return (T) Proxy.newProxyInstance(
			classLoader,
			new Class[]{componentClass},
			new InvocationHandlerAdapter(source, defaultInterceptors)
		);
	}
}
