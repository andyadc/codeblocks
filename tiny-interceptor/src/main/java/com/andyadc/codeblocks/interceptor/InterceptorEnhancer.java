package com.andyadc.codeblocks.interceptor;

import com.andyadc.codeblocks.common.util.ServiceLoaders;

/**
 * {@link Interceptor} Enhancer
 */
public interface InterceptorEnhancer {

	default <T> T enhance(T source) {
		return enhance(source, (Class<? super T>) source.getClass());
	}

	default <T> T enhance(T source, Class<? super T> type) {
		return enhance(source, type, ServiceLoaders.loadAsArray(AnnotatedInterceptor.class));
	}

	default <T> T enhance(T source, Object... interceptors) {
		return enhance(source, (Class<? super T>) source.getClass(), interceptors);
	}

	<T> T enhance(T source, Class<? super T> type, Object... interceptors);
}
