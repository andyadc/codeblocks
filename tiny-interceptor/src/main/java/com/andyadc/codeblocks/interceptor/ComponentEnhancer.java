package com.andyadc.codeblocks.interceptor;

/**
 * Component Enhancer
 * <p>
 * If a component class declares or inherits a class-level interceptor binding,
 * it must not be declared final, or have any non-static, non-private, final
 * methods. If a component has a class-level interceptor binding and is declared
 * final or has a non-static, non-private, final method, the container automatically
 * detects the problem and treats it as a definition error, and causes deployment to
 * fail.
 * <p>
 * If a non-static, non-private method of a component class declares a method-level
 * interceptor binding, neither the method nor the component class may be declared final.
 * If a non-static, non-private, final method of a component has a method-level interceptor
 * binding, the container automatically detects the problem and treats it as a definition
 * error, and causes deployment to fail.
 */
public interface ComponentEnhancer {

	default <T> T enhance(T component) {
		return enhance(component, (Class<? super T>) component.getClass());
	}

	default <T> T enhance(T component, Class<? super T> componentClass) {
		return enhance(component, componentClass, new Object[0]);
	}

	default <T> T enhance(T component, Object... defaultInterceptors) {
		return enhance(component, (Class<? super T>) component.getClass(), defaultInterceptors);
	}

	<T> T enhance(T component, Class<? super T> componentClass, Object... defaultInterceptors);
}
