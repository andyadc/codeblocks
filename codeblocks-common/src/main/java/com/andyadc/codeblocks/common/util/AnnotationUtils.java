package com.andyadc.codeblocks.common.util;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.reflect.AnnotatedElement;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import static com.andyadc.codeblocks.common.function.Streams.filterAll;
import static com.andyadc.codeblocks.common.function.Streams.filterFirst;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;

/**
 * {@link Annotation} Utilities class
 */
public class AnnotationUtils {

	/**
	 * Is the specified type a generic {@link Class type}
	 *
	 * @param annotatedElement the annotated element
	 * @return if <code>annotatedElement</code> is the {@link Class}, return <code>true</code>, or <code>false</code>
	 * @see ElementType#TYPE
	 */
	static boolean isType(AnnotatedElement annotatedElement) {
		return annotatedElement instanceof Class;
	}

	/**
	 * Is the type of specified annotation same to the expected type?
	 *
	 * @param annotation     the specified {@link Annotation}
	 * @param annotationType the expected annotation type
	 * @return if same, return <code>true</code>, or <code>false</code>
	 */
	static boolean isSameType(Annotation annotation, Class<? extends Annotation> annotationType) {
		if (annotation == null || annotationType == null) {
			return false;
		}
		return Objects.equals(annotation.annotationType(), annotationType);
	}

	/**
	 * Find the annotation that is annotated on the specified element may be a meta-annotation
	 *
	 * @param annotatedElement the annotated element
	 * @param annotationType   the type of annotation
	 * @param <A>              the required type of annotation
	 * @return If found, return first matched-type {@link Annotation annotation}, or <code>null</code>
	 */
	public static <A extends Annotation> A findAnnotation(AnnotatedElement annotatedElement, Class<A> annotationType) {
		return (A) filterFirst(getAllDeclaredAnnotations(annotatedElement), a -> isSameType(a, annotationType));
	}

	/**
	 * Get all directly declared annotations of the the annotated element, not including
	 * meta annotations.
	 *
	 * @param annotatedElement    the annotated element
	 * @param annotationsToFilter the annotations to filter
	 * @return non-null read-only {@link List}
	 */
	public static List<Annotation> getAllDeclaredAnnotations(AnnotatedElement annotatedElement,
															 Predicate<Annotation>... annotationsToFilter) {
		if (isType(annotatedElement)) {
			return getAllDeclaredAnnotations(annotatedElement, annotationsToFilter);
		} else {
			return getDeclaredAnnotations(annotatedElement, annotationsToFilter);
		}
	}

	/**
	 * Get annotations that are <em>directly present</em> on this element.
	 * This method ignores inherited annotations.
	 *
	 * @param annotatedElement    the annotated element
	 * @param annotationsToFilter the annotations to filter
	 * @return non-null read-only {@link List}
	 */
	public static List<Annotation> getDeclaredAnnotations(AnnotatedElement annotatedElement,
														  Predicate<Annotation>... annotationsToFilter) {
		if (annotatedElement == null) {
			return emptyList();
		}

		return unmodifiableList(filterAll(asList(annotatedElement.getDeclaredAnnotations()), annotationsToFilter));
	}
}
