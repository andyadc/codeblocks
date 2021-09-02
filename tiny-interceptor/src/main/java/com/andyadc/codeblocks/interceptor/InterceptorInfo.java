package com.andyadc.codeblocks.interceptor;

import com.andyadc.codeblocks.common.lang.AnnotationUtils;
import com.andyadc.codeblocks.common.reflect.MethodUtils;
import com.andyadc.codeblocks.common.util.CollectionUtils;
import com.andyadc.codeblocks.interceptor.util.InterceptorUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.interceptor.AroundConstruct;
import javax.interceptor.AroundInvoke;
import javax.interceptor.AroundTimeout;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Interceptor Info Metadata class
 */
public class InterceptorInfo {

	private final Class<?> interceptorClass;

	private final Method aroundInvokeMethod;

	private final Method aroundTimeoutMethod;

	private final Method aroundConstructMethod;

	private final Method postConstructMethod;

	private final Method preDestroyMethod;

	private final Set<Annotation> interceptorBindings;

	private final InterceptorRegistry interceptorRegistry;

	public InterceptorInfo(Class<?> interceptorClass) {
		this.interceptorClass = interceptorClass;
		Map<Class<? extends Annotation>, Method> interceptionMethods = resolveInterceptionMethods();
		this.aroundInvokeMethod = interceptionMethods.remove(AroundInvoke.class);
		this.aroundTimeoutMethod = interceptionMethods.remove(AroundTimeout.class);
		this.aroundConstructMethod = interceptionMethods.remove(AroundConstruct.class);
		this.postConstructMethod = interceptionMethods.remove(PostConstruct.class);
		this.preDestroyMethod = interceptionMethods.remove(PreDestroy.class);
		this.interceptorBindings = resolveInterceptorBindings();
		this.interceptorRegistry = InterceptorRegistry.getInstance(interceptorClass.getClassLoader());
	}

	private Map<Class<? extends Annotation>, Method> resolveInterceptionMethods() throws IllegalStateException {
		Set<Method> methods = MethodUtils.getAllDeclaredMethods(interceptorClass);
		Map<Class<? extends Annotation>, Method> interceptionMethods = new HashMap<>();

		for (Method method : methods) {
			resolveInterceptionMethod(method, AroundInvoke.class, InterceptorUtils::isAroundInvokeMethod, interceptionMethods);
			resolveInterceptionMethod(method, AroundTimeout.class, InterceptorUtils::isAroundTimeoutMethod, interceptionMethods);
			resolveInterceptionMethod(method, AroundConstruct.class, InterceptorUtils::isAroundConstructMethod, interceptionMethods);
			resolveInterceptionMethod(method, PostConstruct.class, InterceptorUtils::isPostConstructMethod, interceptionMethods);
			resolveInterceptionMethod(method, PreDestroy.class, InterceptorUtils::isPreDestroyMethod, interceptionMethods);
		}

		return interceptionMethods;
	}

	private void resolveInterceptionMethod(Method method, Class<? extends Annotation> annotationType,
										   Predicate<Method> isInterceptionMethod,
										   Map<Class<? extends Annotation>, Method> interceptionMethods) {
		if (isInterceptionMethod.test(method)) {
			if (interceptionMethods.putIfAbsent(annotationType, method) != null) {
				throw interceptionMethodDefinitionException(annotationType);
			}
		}
	}

	private IllegalStateException interceptionMethodDefinitionException(Class<? extends Annotation> annotationType) {
		throw new IllegalStateException(String.format("There is only one @%s method is declared in the interceptor class[%s]",
			annotationType.getName(), interceptorClass.getName()));
	}

	private Set<Annotation> resolveInterceptorBindings() {
		return CollectionUtils.ofSet(AnnotationUtils.getAllDeclaredAnnotations(interceptorClass, interceptorRegistry::isInterceptorBinding));
	}

	public Class<?> getInterceptorClass() {
		return interceptorClass;
	}

	public Method getAroundInvokeMethod() {
		return aroundInvokeMethod;
	}

	public Method getAroundTimeoutMethod() {
		return aroundTimeoutMethod;
	}

	public Method getAroundConstructMethod() {
		return aroundConstructMethod;
	}

	public Method getPostConstructMethod() {
		return postConstructMethod;
	}

	public Method getPreDestroyMethod() {
		return preDestroyMethod;
	}

	public Set<Annotation> getInterceptorBindings() {
		return interceptorBindings;
	}

	public InterceptorRegistry getInterceptorRegistry() {
		return interceptorRegistry;
	}
}
