package com.andyadc.codeblocks.interceptor;

import com.andyadc.codeblocks.common.reflect.ClassLoaderUtils;
import com.andyadc.codeblocks.common.util.ServiceLoaders;
import com.andyadc.codeblocks.interceptor.util.InterceptorUtils;

import javax.interceptor.InterceptorBinding;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.ServiceLoader;
import java.util.Set;

/**
 * The registry of {@link Interceptor}
 */
public interface InterceptorRegistry {

	static InterceptorRegistry getInstance(ClassLoader classLoader) {
		return ServiceLoaders.loadSpi(InterceptorRegistry.class, classLoader);
	}

	static InterceptorRegistry getInstance() {
		return getInstance(ClassLoaderUtils.getClassLoader(InterceptorRegistry.class));
	}

	void registerInterceptorClass(Class<?> interceptorClass);

	default void registerInterceptorClasses(Class<?> interceptorClass, Class<?>... otherInterceptorClasses) {
		registerInterceptorClass(interceptorClass);
		registerInterceptorClasses(otherInterceptorClasses);
	}

	default void registerInterceptorClasses(Class<?>[] interceptorClasses) {
		registerInterceptorClasses(Arrays.asList(interceptorClasses));
	}

	default void registerInterceptorClasses(Iterable<Class<?>> interceptorClasses) {
		interceptorClasses.forEach(this::registerInterceptorClass);
	}

	void registerInterceptor(Object interceptor);

	default void registerInterceptors(Object interceptor, Object... otherInterceptors) {
		registerInterceptor(interceptor);
		registerInterceptors(otherInterceptors);
	}

	default void registerInterceptors(Object[] interceptors) {
		registerInterceptors(Arrays.asList(interceptors));
	}

	default void registerInterceptors(Iterable<?> interceptors) {
		interceptors.forEach(this::registerInterceptor);
	}

	default void registerDiscoveredInterceptors() {
		registerInterceptors(ServiceLoader.load(Interceptor.class));
	}

	/**
	 * Gets the {@linkplain InterceptorBinding interceptor bindings} of the interceptor.
	 *
	 * @return the set of {@linkplain InterceptorBinding interceptor bindings}
	 * @throws IllegalStateException See exception details on {@link InterceptorUtils#isInterceptorClass(Class)}
	 */
	default Set<Annotation> getInterceptorBindings(Class<?> interceptorClass) throws IllegalStateException {
		return getInterceptorInfo(interceptorClass).getInterceptorBindings();
	}

	/**
	 * Get the instance of {@link InterceptorInfo} from the given interceptor class
	 *
	 * @param interceptorClass the given interceptor class
	 * @return non-null if <code>interceptorClass</code> is a valid interceptor class
	 * @throws IllegalStateException See exception details on {@link InterceptorUtils#isInterceptorClass(Class)}
	 */
	InterceptorInfo getInterceptorInfo(Class<?> interceptorClass) throws IllegalStateException;

	/**
	 * Gets the sorted {@link List list} of {@link javax.interceptor.Interceptor @Interceptor} instances
	 *
	 * @param interceptorBindingType the annotation type of {@linkplain InterceptorBinding interceptor binding}
	 * @return a non-null read-only sorted {@link List list}
	 */
	List<Object> getInterceptors(Class<? extends Annotation> interceptorBindingType);

	/**
	 * <p>
	 * Declares an annotation type as an {@linkplain javax.interceptor.Interceptor @Interceptor} binding type if you
	 * wish to make an annotation an interceptor binding type without adding {@link InterceptorBinding} to it.
	 * </p>
	 *
	 * @param interceptorBindingType interceptorBindingType
	 */
	void registerInterceptorBindingType(Class<? extends Annotation> interceptorBindingType);

	default boolean isInterceptorBinding(Annotation annotation) {
		return isInterceptorBindingType(annotation.annotationType());
	}

	boolean isInterceptorBindingType(Class<? extends Annotation> annotationType);
}
