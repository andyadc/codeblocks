package com.andyadc.codeblocks.interceptor;

import com.andyadc.codeblocks.common.reflect.ClassLoaderUtils;
import com.andyadc.codeblocks.common.util.ServiceLoaders;
import com.andyadc.codeblocks.interceptor.util.InterceptorUtils;

import javax.annotation.Priority;
import javax.interceptor.InterceptorBinding;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * The Mananger of {@link Interceptor}
 */
public interface InterceptorManager {

	static InterceptorManager getInstance(ClassLoader classLoader) {
		return ServiceLoaders.loadSpi(InterceptorManager.class, classLoader);
	}

	static InterceptorManager getInstance() {
		return getInstance(ClassLoaderUtils.getClassLoader(InterceptorManager.class));
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
		registerInterceptors(ServiceLoaders.load(Interceptor.class));
	}

	/**
	 * Gets the {@linkplain InterceptorBinding interceptor bindings} of the interceptor.
	 *
	 * @return the instance of {@linkplain InterceptorBindings interceptor bindings}
	 * @throws IllegalStateException See exception details on {@link InterceptorUtils#isInterceptorClass(Class)}
	 */
	default InterceptorBindings getInterceptorBindings(Class<?> interceptorClass) throws IllegalStateException {
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
	 * Resolve the bindings of {@link javax.interceptor.Interceptor @Interceptor} instances upon
	 * the specified {@link Method}
	 *
	 * @param method              the intercepted of {@linkplain Method method}
	 * @param defaultInterceptors the default interceptors
	 * @return a non-null read-only {@link Priority priority} {@link List list} of
	 * {@link javax.interceptor.Interceptor @Interceptor} instances
	 */
	default List<Object> resolveInterceptors(Method method, Object... defaultInterceptors) {
		return resolveInterceptors((Executable) method, defaultInterceptors);
	}

	/**
	 * Resolve the bindings of {@link javax.interceptor.Interceptor @Interceptor} instances upon
	 * the specified {@link Constructor}
	 *
	 * @param constructor         the intercepted of {@linkplain Constructor constructor}
	 * @param defaultInterceptors the default interceptors
	 * @return a non-null read-only {@link Priority priority} {@link List list} of
	 * {@link javax.interceptor.Interceptor @Interceptor} instances
	 */
	default List<Object> resolveInterceptors(Constructor<?> constructor, Object... defaultInterceptors) {
		return resolveInterceptors((Executable) constructor, defaultInterceptors);
	}

	/**
	 * Resolve the bindings of {@link javax.interceptor.Interceptor @Interceptor} instances upon
	 * the specified {@linkplain Method method} or {@link Constructor}
	 * <p>
	 * See Specification:
	 * <p>
	 * 5.2 Interceptor Ordering Rules
	 * <p>
	 * 5.2.1 Use of the Priority Annotation in Ordering Interceptors
	 *
	 * @param executable          the intercepted of {@linkplain Method method} or {@linkplain Constructor constructor}
	 * @param defaultInterceptors the default interceptors
	 * @return a non-null read-only {@link Priority priority} {@link List list} of
	 * {@link javax.interceptor.Interceptor @Interceptor} instances
	 */
	List<Object> resolveInterceptors(Executable executable, Object... defaultInterceptors);

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
