package com.andyadc.codeblocks.interceptor;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.interceptor.InvocationContext;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Chainable {@link InvocationContext}
 */
public class ChainableInvocationContext implements InvocationContext {

	private final InvocationContext delegateContext;

	private final List<Object> interceptors; // @Interceptor class instances

	private final int size;

	private final InterceptorRegistry interceptorRegistry;

	private int pos; // position

	public ChainableInvocationContext(InvocationContext delegateContext, Object... interceptors) {
		this.delegateContext = delegateContext;
		// sort
		this.interceptorRegistry = InterceptorRegistry.getInstance(resolveClassLoader(interceptors));
		this.interceptorRegistry.registerInterceptors(interceptors);
		this.interceptorRegistry.registerDiscoveredInterceptors();
		this.interceptors = resolveInterceptors();
		this.size = this.interceptors.size();
		this.pos = 0;
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
	public Constructor<?> getConstructor() {
		return delegateContext.getConstructor();
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
		if (pos < size) {
			int currentPos = pos++;
			Object interceptor = interceptors.get(currentPos);
			Method interceptionMethod = resolveInterceptionMethod(interceptor);
			return interceptionMethod.invoke(interceptor, this);
		} else {
			return delegateContext.proceed();
		}
	}

	private ClassLoader resolveClassLoader(Object[] interceptors) {
		Object target = interceptors.length > 0 ? interceptors[0] : this;
		return target.getClass().getClassLoader();
	}

	private List<Object> resolveInterceptors() {
		AnnotatedElement annotatedElement = getMethod();
		if (annotatedElement == null) {
			annotatedElement = getConstructor();
		}
		return interceptorRegistry.getInterceptors(annotatedElement);
	}

	private Method resolveInterceptionMethod(Object interceptor) {
		InterceptorInfo interceptorInfo = interceptorRegistry.getInterceptorInfo(interceptor.getClass());

		final Method interceptionMethod;  // nerver null

		if (getTimer() != null) { // If the "Timer" is present
			interceptionMethod = interceptorInfo.getAroundTimeoutMethod();
		} else if (getConstructor() != null) { // If the "Constructor" should be intercepted
			interceptionMethod = interceptorInfo.getAroundConstructMethod();
		} else {
			Method method = getMethod();
			if (method.isAnnotationPresent(PostConstruct.class)) {
				interceptionMethod = interceptorInfo.getPostConstructMethod();
			} else if (method.isAnnotationPresent(PreDestroy.class)) {
				interceptionMethod = interceptorInfo.getPreDestroyMethod();
			} else {
				interceptionMethod = interceptorInfo.getAroundInvokeMethod();
			}
		}

		return interceptionMethod;
	}
}
