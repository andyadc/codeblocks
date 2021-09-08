package com.andyadc.test;

import com.andyadc.codeblocks.interceptor.InterceptorBindingAttributeFilter;

import java.lang.reflect.Method;

public class AlwaysFalseInterceptorBindingAttributeFilter implements InterceptorBindingAttributeFilter {

	@Override
	public boolean accept(Method attributeMethod) {
		return false;
	}
}
