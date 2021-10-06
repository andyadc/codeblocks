package com.andyadc.test;

import com.andyadc.codeblocks.interceptor.AnnotatedInterceptor;
import com.andyadc.codeblocks.interceptor.util.InterceptorUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Priority;
import javax.interceptor.AroundConstruct;
import javax.interceptor.AroundInvoke;
import javax.interceptor.AroundTimeout;
import javax.interceptor.Interceptor;
import javax.interceptor.InterceptorBinding;
import javax.interceptor.InvocationContext;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.andyadc.codeblocks.interceptor.util.InterceptorUtils.*;
import static org.junit.jupiter.api.Assertions.*;


/**
 * {@link InterceptorUtils} Test
 */
public class InterceptorUtilsTest {

	@Test
	public void testConstants() {
		assertEquals(javax.interceptor.Interceptor.class, INTERCEPTOR_ANNOTATION_TYPE);
		assertEquals(InterceptorBinding.class, INTERCEPTOR_BINDING_ANNOTATION_TYPE);
		assertEquals(AroundInvoke.class, AROUND_INVOKE_ANNOTATION_TYPE);
		assertEquals(AroundTimeout.class, AROUND_TIMEOUT_ANNOTATION_TYPE);
		assertEquals(AroundConstruct.class, AROUND_CONSTRUCT_ANNOTATION_TYPE);
		assertEquals(PostConstruct.class, POST_CONSTRUCT_ANNOTATION_TYPE);
		assertEquals(PreDestroy.class, PRE_DESTROY_ANNOTATION_TYPE);
	}

	@Test
	public void testIsInterceptorClass() {
		isInterceptorClass(ExternalInterceptor.class);
	}

	@Test
	public void testSortInterceptors() {
		List<Class<?>> interceptorClasses = new LinkedList<>(Arrays.asList(A.class, B.class));
		interceptorClasses = sortInterceptors(interceptorClasses);
		assertEquals(B.class, interceptorClasses.get(0));
		assertEquals(A.class, interceptorClasses.get(1));
	}

	@Test
	public void testUnwrap() {
		testOnError(() -> unwrap(A.class), Exception.class);
		Assertions.assertNotNull(unwrap(String.class));
	}

	@Test
	public void testValidateInterceptorClass() {
		InterceptorUtils.validateInterceptorClass(ExternalInterceptor.class);
		testOnError(() -> InterceptorUtils.validateInterceptorClass(String.class), IllegalStateException.class);
		testOnError(() -> InterceptorUtils.validateInterceptorClass(A.class), IllegalStateException.class);
		testOnError(() -> InterceptorUtils.validateInterceptorClass(B.class), IllegalStateException.class);
	}

	protected void testOnError(Runnable runnable, Class<? extends Throwable> errorClass) {
		Throwable throwable = null;
		try {
			runnable.run();
		} catch (Throwable e) {
			throwable = e;
		}
		assertTrue(errorClass.isAssignableFrom(throwable.getClass()));
	}

	@Test
	public void testIsAnnotatedInterceptorBinding() {
		assertTrue(InterceptorUtils.isAnnotatedInterceptorBinding(Logging.class));
		assertTrue(InterceptorUtils.isAnnotatedInterceptorBinding(Monitored.class));
		assertFalse(InterceptorUtils.isAnnotatedInterceptorBinding(Override.class));
	}

	@Test
	public void testIsAroundInvokeMethod() throws Throwable {
		Method method = AnnotatedInterceptor.class.getMethod("intercept", InvocationContext.class);
		assertTrue(InterceptorUtils.isAroundInvokeMethod(method));
	}

	@Test
	public void testIsAroundTimeoutMethod() throws Throwable {
		Method method = AnnotatedInterceptor.class.getMethod("interceptTimeout", InvocationContext.class);
		assertTrue(InterceptorUtils.isAroundTimeoutMethod(method));
	}

	@Test
	public void testIsAroundConstructMethod() throws Throwable {
		Method method = AnnotatedInterceptor.class.getMethod("interceptConstruct", InvocationContext.class);
		assertTrue(InterceptorUtils.isAroundConstructMethod(method));
	}

	@Test
	public void testIsPostConstructMethod() throws Throwable {
		Method method = AnnotatedInterceptor.class.getMethod("interceptPostConstruct", InvocationContext.class);
		assertTrue(InterceptorUtils.isPostConstructMethod(method));
	}

	@Test
	public void testIsPreDestroyMethod() throws Throwable {
		Method method = AnnotatedInterceptor.class.getMethod("interceptPreDestroy", InvocationContext.class);
		assertTrue(InterceptorUtils.isPreDestroyMethod(method));
	}

	@Priority(2)
	@Interceptor
	abstract class A {
	}

	@Priority(1)
	@Interceptor
	final class B {
	}
}
