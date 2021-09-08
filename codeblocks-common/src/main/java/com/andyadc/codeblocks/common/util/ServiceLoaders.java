package com.andyadc.codeblocks.common.util;

import com.andyadc.codeblocks.common.function.Streams;
import com.andyadc.codeblocks.common.reflect.ClassLoaderUtils;

import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * {@link ServiceLoader} Utilities Class
 */
public abstract class ServiceLoaders {

	private static final Map<ClassLoader, Map<Class<?>, ServiceLoader<?>>> serviceLoadersCache = new ConcurrentHashMap<>();

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
		return ArrayUtils.asArray(load(serviceClass, classLoader), serviceClass);
	}

	public static <T> ServiceLoader<T> load(Class<T> serviceClass) {
		return load(serviceClass, ClassLoaderUtils.getClassLoader(serviceClass));
	}

	public static <T> ServiceLoader<T> load(Class<T> serviceClass, ClassLoader classLoader) {
		Map<Class<?>, ServiceLoader<?>> serviceLoadersMap = serviceLoadersCache.computeIfAbsent(classLoader, cl -> new ConcurrentHashMap<>());
		ServiceLoader<T> serviceLoader = (ServiceLoader<T>) serviceLoadersMap.computeIfAbsent(serviceClass,
			service -> ServiceLoader.load(service, classLoader));
		return serviceLoader;
	}
}
