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
import java.util.Set;

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

	/**
	 * @param interceptorClass The class of {@link javax.interceptor.Interceptor @Interceptor}
	 * @throws NullPointerException  If <code>interceptorClass</code> is <code>null</code>
	 * @throws IllegalStateException If an interceptor class does not annotate @Interceptor or
	 *                               is abstract or have not a public no-arg constructor
	 */
	void registerInterceptorClass(Class<?> interceptorClass) throws NullPointerException, IllegalStateException;

	/**
	 * @param interceptorClass        The class of {@link javax.interceptor.Interceptor @Interceptor}
	 * @param otherInterceptorClasses the other classes of {@link javax.interceptor.Interceptor @Interceptor}
	 * @throws NullPointerException  If <code>interceptorClass</code> is <code>null</code>
	 * @throws IllegalStateException If an interceptor class does not annotate @Interceptor or
	 *                               is abstract or have not a public no-arg constructor
	 */
	default void registerInterceptorClasses(Class<?> interceptorClass, Class<?>... otherInterceptorClasses)
		throws NullPointerException, IllegalStateException {
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
	 * Resolve the bindings of {@link javax.interceptor.Interceptor @Interceptor} {@link Class classes} upon
	 * the specified {@linkplain Method method} or {@link Constructor}
	 * <p>
	 * See Specification:
	 * <p>
	 * 5.2 Interceptor Ordering Rules
	 * <p>
	 * 5.2.1 Use of the Priority Annotation in Ordering Interceptors
	 *
	 * @param executable                the intercepted of {@linkplain Method method} or {@linkplain Constructor constructor}
	 * @param defaultInterceptorClasses the default interceptors {@link Class classes}
	 * @return a non-null read-only {@link Priority priority} {@link List list} of
	 * {@link javax.interceptor.Interceptor @Interceptor} {@link Class classes}
	 */
	List<Class<?>> resolveInterceptorClasses(Executable executable, Class<?>... defaultInterceptorClasses);

	/**
	 * Register an {@linkplain javax.interceptor.Interceptor @Interceptor} binding {@link Class type}
	 * whether it adds {@link InterceptorBinding} or not.
	 * <p>
	 *
	 * @param interceptorBindingType {@linkplain javax.interceptor.Interceptor @Interceptor} binding {@link Class type}
	 */
	void registerInterceptorBindingType(Class<? extends Annotation> interceptorBindingType);

	/**
	 * Register an {@linkplain javax.interceptor.Interceptor @Interceptor} binding {@link Class type}
	 * whether it adds {@link InterceptorBinding} or not.
	 *
	 * @param interceptorBindingType An interceptor binding type
	 * @param interceptorBindingDef  An optional list of annotations defining the {@linkplain javax.enterprise.inject.spi.Interceptor interceptor}
	 */
	void registerInterceptorBinding(Class<? extends Annotation> interceptorBindingType, Annotation... interceptorBindingDef);

	/**
	 * Get all registered {@link Class classes} of {@link javax.interceptor.Interceptor @Interceptor}
	 *
	 * @return non-null read-only {@link Set} of {@link Class classes}
	 */
	Set<Class<?>> getInterceptorClasses();

	/**
	 * Get all {@link Class classes} of {@link InterceptorBinding Interceptor bindings}
	 *
	 * @return non-null read-only {@link Set} of {@link Class classes}
	 */
	Set<Class<? extends Annotation>> getInterceptorBindingTypes();

	/**
	 * The given interceptor class is {@link javax.interceptor.Interceptor @Interceptor} or not.
	 *
	 * @param interceptorClass the class of interceptor is abstract or have not a public no-arg constructor
	 */
	boolean isInterceptorClass(Class<?> interceptorClass);

	/**
	 * An interceptor class must not be abstract and must have a public no-arg constructor.
	 *
	 * @param interceptorClass the class of interceptor
	 * @throws NullPointerException  If <code>interceptorClass</code> is <code>null</code>
	 * @throws IllegalStateException If an interceptor class does not annotate @Interceptor or
	 *                               is abstract or have not a public no-arg constructor
	 */
	void validateInterceptorClass(Class<?> interceptorClass) throws NullPointerException, IllegalStateException;

	default <T> T unwrap(Class<T> type) {
		return InterceptorUtils.unwrap(type);
	}

	default boolean isInterceptorBinding(Annotation annotation) {
		return isInterceptorBindingType(annotation.annotationType());
	}

	boolean isInterceptorBindingType(Class<? extends Annotation> annotationType);
}
