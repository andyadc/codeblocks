package com.andyadc.codeblocks.interceptor;

import com.andyadc.codeblocks.common.function.Predicates;
import com.andyadc.codeblocks.common.util.ServiceLoaders;

import javax.interceptor.InterceptorBinding;
import java.lang.reflect.Method;
import java.util.function.Predicate;

/**
 * The attribute filter of {@link InterceptorBinding}
 */
public interface InterceptorBindingAttributeFilter extends Predicate<Method> {

	Predicate<Method> FILTERS = filters();

	static Predicate<Method> filters() {
		return Predicates.or(ServiceLoaders.loadAsArray(InterceptorBindingAttributeFilter.class));
	}

	default boolean test(Method attributeMethod) {
		return accept(attributeMethod);
	}

	/**
	 * @param attributeMethod the attribute method declared in the
	 *                        {@link InterceptorBinding interceptor binding}
	 * @return <code>true</code> if attribute method is accepted, <code>false</code> otherwise
	 */
	boolean accept(Method attributeMethod);
}
