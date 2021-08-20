package com.andyadc.codeblocks.common.reflect;

import com.andyadc.codeblocks.common.function.Streams;
import com.andyadc.codeblocks.common.lang.StringUtils;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import static com.andyadc.codeblocks.common.reflect.ClassUtils.getAllInheritedTypes;

/**
 * The utilities class for Java Refection {@link Method}
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
public class MethodUtils {

	/**
	 * Get all {@link Method methods} of the declared class
	 *
	 * @param declaringClass        the declared class
	 * @param includeInheritedTypes include the inherited types, e,g. super classes or interfaces
	 * @param publicOnly            only public method
	 * @param methodsToFilter       (optional) the methods to be filtered
	 * @return non-null read-only {@link List}
	 */
	static List<Method> getMethods(Class<?> declaringClass, boolean includeInheritedTypes, boolean publicOnly,
								   Predicate<Method>... methodsToFilter) {

		if (declaringClass == null || declaringClass.isPrimitive()) {
			return Collections.emptyList();
		}

		// All declared classes
		List<Class<?>> declaredClasses = new LinkedList<>();
		// Add the top declaring class
		declaredClasses.add(declaringClass);
		// If the super classes are resolved, all them into declaredClasses
		if (includeInheritedTypes) {
			declaredClasses.addAll(getAllInheritedTypes(declaringClass));
		}

		// All methods
		List<Method> allMethods = new LinkedList<>();
		for (Class<?> classToSearch : declaredClasses) {
			Method[] methods = publicOnly ? classToSearch.getMethods() : classToSearch.getDeclaredMethods();
			// Add the declared methods or public methods
			for (Method method : methods) {
				allMethods.add(method);
			}
		}

		return Collections.unmodifiableList(Streams.filterAll(allMethods, methodsToFilter));
	}

	/**
	 * Get all declared {@link Method methods} of the declared class, excluding the inherited methods
	 *
	 * @param declaringClass  the declared class
	 * @param methodsToFilter (optional) the methods to be filtered
	 * @return non-null read-only {@link List}
	 * @see #getMethods(Class, boolean, boolean, Predicate[])
	 */
	static List<Method> getDeclaredMethods(Class<?> declaringClass, Predicate<Method>... methodsToFilter) {
		return getMethods(declaringClass, false, false, methodsToFilter);
	}

	/**
	 * Get all public {@link Method methods} of the declared class, including the inherited methods.
	 *
	 * @param declaringClass  the declared class
	 * @param methodsToFilter (optional) the methods to be filtered
	 * @return non-null read-only {@link List}
	 * @see #getMethods(Class, boolean, boolean, Predicate[])
	 */
	static List<Method> getMethods(Class<?> declaringClass, Predicate<Method>... methodsToFilter) {
		return getMethods(declaringClass, false, true, methodsToFilter);
	}

	/**
	 * Get all declared {@link Method methods} of the declared class, including the inherited methods.
	 *
	 * @param declaringClass  the declared class
	 * @param methodsToFilter (optional) the methods to be filtered
	 * @return non-null read-only {@link List}
	 * @see #getMethods(Class, boolean, boolean, Predicate[])
	 */
	static List<Method> getAllDeclaredMethods(Class<?> declaringClass, Predicate<Method>... methodsToFilter) {
		return getMethods(declaringClass, true, false, methodsToFilter);
	}

	/**
	 * Get all public {@link Method methods} of the declared class, including the inherited methods.
	 *
	 * @param declaringClass  the declared class
	 * @param methodsToFilter (optional) the methods to be filtered
	 * @return non-null read-only {@link List}
	 * @see #getMethods(Class, boolean, boolean, Predicate[])
	 */
	static List<Method> getAllMethods(Class<?> declaringClass, Predicate<Method>... methodsToFilter) {
		return getMethods(declaringClass, true, true, methodsToFilter);
	}

	/**
	 * Find the {@link Method} by the the specified type and method name without the parameter types
	 *
	 * @param type       the target type
	 * @param methodName the specified method name
	 * @return if not found, return <code>null</code>
	 */
	static Method findMethod(Class type, String methodName) {
		return findMethod(type, methodName, ClassUtils.EMPTY_CLASS_ARRAY);
	}

	/**
	 * Find the {@link Method} by the the specified type, method name and parameter types
	 *
	 * @param type           the target type
	 * @param methodName     the method name
	 * @param parameterTypes the parameter types
	 * @return if not found, return <code>null</code>
	 */
	static Method findMethod(Class type, String methodName, Class<?>... parameterTypes) {
		Method method = null;
		try {
			if (type != null && StringUtils.isNotEmpty(methodName)) {
				method = type.getDeclaredMethod(methodName, parameterTypes);
			}
		} catch (NoSuchMethodException e) {
		}
		return method;
	}

	/**
	 * Invoke the target object and method
	 *
	 * @param object          the target object
	 * @param methodName      the method name
	 * @param parameterValues the method parameters
	 * @param <T>             the return type
	 * @return the target method's execution result
	 */
	public static <T> T invokeMethod(Object object, String methodName, Object... parameterValues) {
		Class[] parameterTypes = ClassUtils.resolveTypes(parameterValues);
		return invokeMethod(object, methodName, parameterTypes, parameterValues);
	}

	/**
	 * Invoke the target object and method
	 *
	 * @param object          the target object
	 * @param methodName      the method name
	 * @param parameterTypes  the types of parameters
	 * @param parameterValues the method parameters
	 * @param <T>             the return type
	 * @return the target method's execution result
	 */
	public static <T> T invokeMethod(Object object, String methodName, Class[] parameterTypes, Object[] parameterValues) {
		Class type = object.getClass();
		Method method = findMethod(type, methodName, parameterTypes);
		T value = null;

		if (method == null) {
			throw new IllegalStateException(String.format("cannot find method %s,class: %s", methodName, type.getName()));
		}

		try {
			enableAccessible(method);
			value = (T) method.invoke(object, parameterValues);
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}

		return value;
	}

	/**
	 * Tests whether one method, as a member of a given type,
	 * overrides another method.
	 *
	 * @param overrider  the first method, possible overrider
	 * @param overridden the second method, possibly being overridden
	 * @return {@code true} if and only if the first method overrides
	 * the second
	 * @jls 8.4.8 Inheritance, Overriding, and Hiding
	 * @jls 9.4.1 Inheritance and Overriding
	 * @see Elements#overrides(ExecutableElement, ExecutableElement, TypeElement)
	 */
	public static boolean overrides(Method overrider, Method overridden) {
		if (overrider == null || overridden == null) {
			return false;
		}

		// equality comparison: If two methods are same
		if (Objects.equals(overrider, overridden)) {
			return false;
		}

		// Modifiers comparison: Any method must be non-static method
		if (MemberUtils.isStatic(overrider) || MemberUtils.isStatic(overridden)) { //
			return false;
		}

		// Modifiers comparison: the accessibility of any method must not be private
		if (MemberUtils.isPrivate(overrider) || MemberUtils.isPrivate(overridden)) {
			return false;
		}

		// Inheritance comparison: The declaring class of overrider must be inherit from the overridden's
		if (!overridden.getDeclaringClass().isAssignableFrom(overrider.getDeclaringClass())) {
			return false;
		}

		// Method comparison: must not be "default" method
		if (overrider.isDefault()) {
			return false;
		}

		// Method comparison: The method name must be equal
		if (!Objects.equals(overrider.getName(), overridden.getName())) {
			return false;
		}

		// Method comparison: The count of method parameters must be equal
		if (!Objects.equals(overrider.getParameterCount(), overridden.getParameterCount())) {
			return false;
		}

		// Method comparison: Any parameter type of overrider must equal the overridden's
		for (int i = 0; i < overrider.getParameterCount(); i++) {
			if (!Objects.equals(overridden.getParameterTypes()[i], overrider.getParameterTypes()[i])) {
				return false;
			}
		}

		// Method comparison: The return type of overrider must be inherit from the overridden's
		return overridden.getReturnType().isAssignableFrom(overrider.getReturnType());

		// Throwable comparison: "throws" Throwable list will be ignored, trust the compiler verify
	}

	/**
	 * Find the nearest overridden {@link Method method} from the inherited class
	 *
	 * @param overrider the overrider {@link Method method}
	 * @return if found, the overrider <code>method</code>, or <code>null</code>
	 */
	public static Method findNearestOverriddenMethod(Method overrider) {
		Class<?> declaringClass = overrider.getDeclaringClass();
		Method overriddenMethod = null;
		for (Class<?> inheritedType : getAllInheritedTypes(declaringClass)) {
			overriddenMethod = findOverriddenMethod(overrider, inheritedType);
			if (overriddenMethod != null) {
				break;
			}
		}
		return overriddenMethod;
	}

	/**
	 * Find the overridden {@link Method method} from the declaring class
	 *
	 * @param overrider      the overrider {@link Method method}
	 * @param declaringClass the class that is declaring the overridden {@link Method method}
	 * @return if found, the overrider <code>method</code>, or <code>null</code>
	 */
	public static Method findOverriddenMethod(Method overrider, Class<?> declaringClass) {
		List<Method> matchedMethods = getAllMethods(declaringClass, method -> overrides(overrider, method));
		return matchedMethods.isEmpty() ? null : matchedMethods.get(0);
	}

	public static void enableAccessible(Method method) {
		if (!MemberUtils.isPublic(method) || !method.isAccessible()) {
			method.setAccessible(true);
		}
	}
}
