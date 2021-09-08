package com.andyadc.codeblocks.interceptor.cdi;

import com.andyadc.codeblocks.common.reflect.ClassLoaderUtils;
import com.andyadc.codeblocks.common.reflect.ClassUtils;
import com.andyadc.codeblocks.interceptor.InterceptorBindingAttributeFilter;

import javax.enterprise.util.Nonbinding;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * {@link InterceptorBindingAttributeFilter} for {@link Nonbinding}
 */
public class NonInterceptorBindingAttributeFilter implements InterceptorBindingAttributeFilter {

	private static final String NON_BINDING_ANNOTATION_CLASS_NAME = "javax.enterprise.util.Nonbinding";

	private static final ClassLoader classLoader = ClassLoaderUtils.getClassLoader(NonInterceptorBindingAttributeFilter.class);

	private static final boolean NON_BINDING_ANNOTATION_ABSENT = ClassUtils.isPresent(NON_BINDING_ANNOTATION_CLASS_NAME, classLoader);

	@Override
	public boolean accept(Method attributeMethod) {
		if (NON_BINDING_ANNOTATION_ABSENT) {
			Class<? extends Annotation> nonbindingClass =
				(Class<? extends Annotation>) ClassUtils.resolveClass(NON_BINDING_ANNOTATION_CLASS_NAME, classLoader);
			return !attributeMethod.isAnnotationPresent(nonbindingClass);
		}
		return true;
	}
}
