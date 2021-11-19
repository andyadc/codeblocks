package com.andyadc.codeblocks.common.reflect;

import com.andyadc.codeblocks.common.function.Predicates;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Set;
import java.util.function.Predicate;

/**
 * The utilities class for {@link Proxy Proxy}
 */
public abstract class ProxyUtils {

	/**
	 * <ul>
	 *     <li>class has a non-private constructor with no parameters</li>
	 *     <li>class is not declared final</li>
	 *     <li>class does not have non-static, final methods with public, protected or default visibility</li>
	 *     <li>class is not primitive type</li>
	 *     <li>class is not array type</li>
	 * </ul>
	 */
	public static boolean isProxyable(Class<?> type) {
		if (ClassUtils.isArray(type)) {
			return false;
		}
		if (ClassUtils.isPrimitive(type)) {
			return false;
		}
		if (ClassUtils.isFinal(type)) {
			return false;
		}
		if (!ConstructorUtils.hasNonPrivateConstructorWithoutParameters(type)) {
			return false;
		}

		Predicate<Method> predicate = Predicates.and(
			MemberUtils.NON_STATIC_METHOD_PREDICATE,
			MemberUtils.FINAL_METHOD_PREDICATE,
			MemberUtils.NON_PRIVATE_METHOD_PREDICATE,
			MethodUtils.OBJECT_METHOD_PREDICATE.negate()
		);

		Set<Method> methods = MethodUtils.getAllDeclaredMethods(type, predicate);
		return methods.isEmpty();
	}
}
