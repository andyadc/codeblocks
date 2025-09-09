package com.andyadc.codeblocks.interceptor;

import com.andyadc.codeblocks.common.lang.AnnotationUtils;
import com.andyadc.codeblocks.interceptor.util.InterceptorUtils;
import jakarta.interceptor.InterceptorBinding;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Objects;

/**
 * The Metadata Info Class for {@link InterceptorBinding}
 */
public class InterceptorBindingInfo {

	private final Annotation declaredAnnotation;

	private final Class<? extends Annotation> declaredAnnotationType;

	/**
	 * If <code>true</code>, the declared annotation does not annotate {@link InterceptorBinding}
	 */
	private final boolean synthetic;

	private final Map<String, Object> attributes;

	public InterceptorBindingInfo(Annotation declaredAnnotation) {
		this.declaredAnnotation = declaredAnnotation;
		this.declaredAnnotationType = declaredAnnotation.annotationType();
		this.synthetic = !InterceptorUtils.isAnnotatedInterceptorBinding(declaredAnnotationType);
		this.attributes = AnnotationUtils.getAttributesMap(declaredAnnotation, InterceptorBindingAttributeFilter.filters());
	}

	/**
	 * New instance of {@link InterceptorBindingInfo}
	 *
	 * @param interceptorBinding the instance of {@linkplain InterceptorBinding interceptor binding}
	 * @return non-null
	 */
	public static InterceptorBindingInfo valueOf(Annotation interceptorBinding) {
		return new InterceptorBindingInfo(interceptorBinding);
	}

	public Class<? extends Annotation> getDeclaredAnnotationType() {
		return declaredAnnotationType;
	}

	public boolean isSynthetic() {
		return synthetic;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		InterceptorBindingInfo that = (InterceptorBindingInfo) o;
		return synthetic == that.synthetic
			&& Objects.equals(declaredAnnotationType, that.declaredAnnotationType)
			&& Objects.equals(attributes, that.attributes);
	}

	@Override
	public int hashCode() {
		return Objects.hash(declaredAnnotationType, synthetic, attributes);
	}

	public boolean equals(Annotation declaredAnnotation) {
		if (declaredAnnotation == null) {
			return false;
		}
		return this.equals(valueOf(declaredAnnotation));
	}

	public Annotation getDeclaredAnnotation() {
		return declaredAnnotation;
	}
}
