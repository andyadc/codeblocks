package com.andyadc.codeblocks.interceptor;

import com.andyadc.codeblocks.common.util.ServiceLoaders;

/**
 * The tagging interface that all {@link javax.interceptor.Interceptor @Interceptor} class should implement.
 */
public interface Interceptor {

	/**
	 * Load the sorted instances of {@link Interceptor} via Java Standard SPI.
	 *
	 * @return non-null
	 */
	static Interceptor[] loadInterceptors() {
		return ServiceLoaders.loadAsArray(Interceptor.class);
	}
}
