package com.andyadc.codeblocks.interceptor;

import com.andyadc.codeblocks.common.util.CollectionUtils;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The composite {@link InterceptorBindingInfo} instance
 */
public class InterceptorBindings implements Iterable<InterceptorBindingInfo> {

	private final Set<InterceptorBindingInfo> interceptorBindings;

	private final Set<Class<? extends Annotation>> interceptorBindingTypes;

	public InterceptorBindings(Collection<Annotation> interceptorBindings) {
		this(CollectionUtils.asSet(interceptorBindings));
	}

	public InterceptorBindings(Set<Annotation> interceptorBindings) {
		this.interceptorBindings = asInterceptorBindings(interceptorBindings);
		this.interceptorBindingTypes = asInterceptorBindingTypes(interceptorBindings);
	}

	public Set<InterceptorBindingInfo> getInterceptorBindings() {
		return interceptorBindings;
	}

	public Set<Class<? extends Annotation>> getInterceptorBindingTypes() {
		return interceptorBindingTypes;
	}

	@Override
	public Iterator<InterceptorBindingInfo> iterator() {
		return interceptorBindings.iterator();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		InterceptorBindings that = (InterceptorBindings) o;
		return Objects.equals(interceptorBindings, that.interceptorBindings);
	}

	@Override
	public int hashCode() {
		return Objects.hash(interceptorBindings);
	}

	private Set<InterceptorBindingInfo> asInterceptorBindings(Collection<Annotation> interceptorBindings) {
		return CollectionUtils.asSet(sortedStream(interceptorBindings)
			.map(InterceptorBindingInfo::new)
			.collect(Collectors.toList()));
	}

	private Set<Class<? extends Annotation>> asInterceptorBindingTypes(Collection<Annotation> interceptorBindings) {
		return CollectionUtils.asSet(sortedStream(interceptorBindings)
			.map(Annotation::annotationType)
			.collect(Collectors.toList()));
	}

	private Stream<Annotation> sortedStream(Collection<Annotation> interceptorBindings) {
		return interceptorBindings.stream().sorted(InterceptorBindingComparator.INSTANCE);
	}
}
