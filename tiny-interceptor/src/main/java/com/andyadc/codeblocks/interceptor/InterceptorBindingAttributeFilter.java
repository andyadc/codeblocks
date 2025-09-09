package com.andyadc.codeblocks.interceptor;

import com.andyadc.codeblocks.common.util.ServiceLoaders;
import jakarta.interceptor.InterceptorBinding;

import java.lang.reflect.Method;
import java.util.function.Predicate;

/**
 * The attribute filter of {@link InterceptorBinding}
 */
public interface InterceptorBindingAttributeFilter extends Predicate<Method> {

	default boolean test(Method attributeMethod) {
		return accept(attributeMethod);
	}

	/**
	 * @param attributeMethod the attribute method declared in the
	 *                        {@link InterceptorBinding interceptor binding}
	 * @return <code>true</code> if attribute method is accepted, <code>false</code> otherwise
	 */
	boolean accept(Method attributeMethod);

	static Predicate<Method>[] filters() {
		return ServiceLoaders.loadAsArray(InterceptorBindingAttributeFilter.class);
	}
}
