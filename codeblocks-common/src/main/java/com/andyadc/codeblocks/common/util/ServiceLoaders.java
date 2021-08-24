package com.andyadc.codeblocks.common.util;

import com.andyadc.codeblocks.common.function.Streams;
import com.andyadc.codeblocks.common.reflect.ClassLoaderUtils;

import java.util.ServiceLoader;
import java.util.stream.Stream;

import static java.util.ServiceLoader.load;

/**
 * {@link ServiceLoader} Utilities Class
 */
public abstract class ServiceLoaders {

	public static <T> Stream<T> loadAsStream(Class<T> serviceClass) {
		return loadAsStream(serviceClass, ClassLoaderUtils.getClassLoader(serviceClass));
	}

	public static <T> Stream<T> loadAsStream(Class<T> serviceClass, ClassLoader classLoader) {
		return Streams.stream(load(serviceClass, classLoader));
	}

	public static <T> T loadSpi(Class<T> serviceClass) {
		return loadSpi(serviceClass, ClassLoaderUtils.getClassLoader(serviceClass));
	}

	public static <T> T loadSpi(Class<T> serviceClass, ClassLoader classLoader) {
		return load(serviceClass, classLoader).iterator().next();
	}

	public static <T> T[] loadAsArray(Class<T> serviceClass) {
		return loadAsArray(serviceClass, ClassLoaderUtils.getClassLoader(serviceClass));
	}

	public static <T> T[] loadAsArray(Class<T> serviceClass, ClassLoader classLoader) {
		return (T[]) loadAsStream(serviceClass, classLoader).toArray();
	}
}
