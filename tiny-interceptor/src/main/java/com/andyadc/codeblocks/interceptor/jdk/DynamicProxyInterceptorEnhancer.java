package com.andyadc.codeblocks.interceptor.jdk;

import com.andyadc.codeblocks.common.reflect.ClassLoaderUtils;
import com.andyadc.codeblocks.interceptor.InterceptorEnhancer;

import java.lang.reflect.Proxy;

/**
 * {@link InterceptorEnhancer} based on JDK Dynamic Proxy
 */
@Deprecated
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
