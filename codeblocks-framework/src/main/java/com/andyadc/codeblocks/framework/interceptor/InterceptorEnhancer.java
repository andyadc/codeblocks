package com.andyadc.codeblocks.framework.interceptor;

import static com.andyadc.codeblocks.common.util.ServiceLoaders.loadAsArray;

/**
 * {@link Interceptor} Enhancer
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
public interface InterceptorEnhancer {

	default <T> T enhance(T source) {
		return enhance(source, (Class<? super T>) source.getClass());
	}

	default <T> T enhance(T source, Class<? super T> type) {
		return enhance(source, type, loadAsArray(AnnotatedInterceptor.class));
	}

	default <T> T enhance(T source, Object... interceptors) {
		return enhance(source, (Class<? super T>) source.getClass(), interceptors);
	}

	<T> T enhance(T source, Class<? super T> type, Object... interceptors);

}
