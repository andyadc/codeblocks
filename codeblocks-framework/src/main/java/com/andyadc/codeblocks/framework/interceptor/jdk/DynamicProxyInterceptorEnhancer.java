package com.andyadc.codeblocks.framework.interceptor.jdk;

import com.andyadc.codeblocks.common.reflect.ClassLoaderUtils;
import com.andyadc.codeblocks.framework.interceptor.InterceptorEnhancer;

import java.lang.reflect.Proxy;

/**
 * {@link InterceptorEnhancer} based on JDK Dynamic Proxy
 */
public class DynamicProxyInterceptorEnhancer implements InterceptorEnhancer {

	@Override
	public <T> T enhance(T source, Class<? super T> type, Object... interceptors) {
		ClassLoader classLoader = ClassLoaderUtils.getClassLoader(type);
		return (T) Proxy.newProxyInstance(
			classLoader,
			new Class[]{type},
			new InvocationHandlerAdapter(source, interceptors));
	}
}
