package com.andyadc.codeblocks.interceptor;

import jakarta.interceptor.InvocationContext;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * {@link Constructor} {@link InvocationContext}
 */
public class ReflectiveConstructorInvocationContext implements InvocationContext {

	private final Constructor<?> constructor;
	private final Map<String, Object> contextData;
	private Object[] parameters;

	public ReflectiveConstructorInvocationContext(Constructor<?> constructor, Object... parameters) {
		Objects.requireNonNull(constructor, "The argument 'constructor' must not be null");
		Objects.requireNonNull(parameters, "The arguments 'parameters' must not be null");
		this.constructor = constructor;
		this.setParameters(parameters);
		this.contextData = new HashMap<>();
	}

	@Override
	public final Object getTarget() {
		return null;
	}

	@Override
	public final Object getTimer() {
		return null;
	}

	@Override
	public final Method getMethod() {
		return null;
	}

	@Override
	public final Constructor<?> getConstructor() {
		return constructor;
	}

	@Override
	public final Object[] getParameters() {
		return parameters;
	}

	@Override
	public final void setParameters(Object[] params) {
		this.parameters = params != null ? params : new Object[0];
	}

	@Override
	public final Map<String, Object> getContextData() {
		return contextData;
	}

	@Override
	public Object proceed() throws Exception {
		return constructor.newInstance(parameters);
	}
}
