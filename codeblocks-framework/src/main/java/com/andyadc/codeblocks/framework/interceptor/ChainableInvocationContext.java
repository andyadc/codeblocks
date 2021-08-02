package com.andyadc.codeblocks.framework.interceptor;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Chainable {@link InvocationContext}
 */
public class ChainableInvocationContext implements InvocationContext {

	private final InvocationContext delegateContext;

	private final Object[] interceptors; // @Interceptor class instances

	private final int length;

	private final Map<Integer, Method> indexedAroundInvokeMethods;

	private int pos; // position

	public ChainableInvocationContext(InvocationContext delegateContext, Object... interceptors) {
		this.delegateContext = delegateContext;
		this.interceptors = interceptors;
		this.length = interceptors.length;
		this.indexedAroundInvokeMethods = initIndexedAroundInvokeMethods();
		this.pos = 0;
	}

	private Map<Integer, Method> initIndexedAroundInvokeMethods() {
		Map<Integer, Method> indexedMethods = new HashMap<>();
		for (int i = 0; i < length; i++) {
			Object interceptor = interceptors[i];
			Method aroundInvokeMethod = findAroundInvokeMethod(interceptor);
			indexedMethods.put(i, aroundInvokeMethod);
		}
		return indexedMethods;
	}

	private Method getAroundInvokeMethod(int index) {
		return indexedAroundInvokeMethods.get(index);
	}

	private Method findAroundInvokeMethod(Object interceptor) {
		Class interceptorClass = interceptor.getClass();
		return Stream.of(interceptorClass.getMethods()) // all public methods
			.filter(method -> {
				int mods = method.getModifiers();
				if (Modifier.isStatic(mods)) { // non-static
					return false;
				}

				if (method.getParameterCount() != 1) { // only-one argument
					return false;
				}

				if (!InvocationContext.class.isAssignableFrom(method.getParameterTypes()[0])) {
					return false;
				}

				// @AroundInvoke presents on method
				return method.isAnnotationPresent(AroundInvoke.class);
			}).findFirst().get(); // TODO if null

	}

	@Override
	public Object getTarget() {
		return delegateContext.getTarget();
	}

	@Override
	public Object getTimer() {
		return delegateContext.getTimer();
	}

	@Override
	public Method getMethod() {
		return delegateContext.getMethod();
	}

	@Override
	public Object[] getParameters() {
		return delegateContext.getParameters();
	}

	@Override
	public void setParameters(Object[] params) {
		delegateContext.setParameters(params);
	}

	@Override
	public Map<String, Object> getContextData() {
		return delegateContext.getContextData();
	}

	@Override
	public Object proceed() throws Exception {
		if (pos < length) {
			int currentPos = pos++;
			Object interceptor = interceptors[currentPos];
			Method aroundInvokeMethod = getAroundInvokeMethod(currentPos);
			return aroundInvokeMethod.invoke(interceptor, this);
		} else {
			return delegateContext.proceed();
		}
	}
}
