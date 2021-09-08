package com.andyadc.codeblocks.interceptor;

import java.lang.annotation.Annotation;
import java.util.Comparator;

public class InterceptorBindingComparator implements Comparator<Annotation> {

	final static Comparator<Annotation> INSTANCE = new InterceptorBindingComparator();

	private InterceptorBindingComparator() {
	}

	@Override
	public int compare(Annotation o1, Annotation o2) {
		return o1.toString().compareTo(o2.toString());
	}
}
