package com.andyadc.codeblocks.common.util;

import com.andyadc.codeblocks.common.lang.AnnotationUtils;
import jakarta.annotation.Priority;

import java.util.Comparator;
import java.util.Objects;

/**
 * The {@link Comparator} for the annotation {@link Priority}
 * <p>
 * The less value of {@link Priority}, the more priority
 *
 * @see Priority
 */
public class PriorityComparator implements Comparator<Object> {

	/**
	 * Singleton instance of {@link PriorityComparator}
	 */
	public static final PriorityComparator INSTANCE = new PriorityComparator();
	private static final Class<Priority> PRIORITY_CLASS = Priority.class;
	private static final int UNDEFINED_VALUE = -1;

	public static int compare(Class<?> type1, Class<?> type2) {
		if (Objects.equals(type1, type2)) {
			return 0;
		}

		Priority priority1 = AnnotationUtils.findAnnotation(type1, PRIORITY_CLASS);
		Priority priority2 = AnnotationUtils.findAnnotation(type2, PRIORITY_CLASS);

		int priorityValue1 = getValue(priority1);
		int priorityValue2 = getValue(priority2);
		return Integer.compare(priorityValue1, priorityValue2);
	}

	private static Class<?> resolveClass(Object object) {
		return object instanceof Class ? (Class<?>) object : object.getClass();
	}

	private static int getValue(Priority priority) {
		int value = priority == null ? UNDEFINED_VALUE : priority.value();
		return value < 0 ? UNDEFINED_VALUE : value;
	}

	@Override
	public int compare(Object o1, Object o2) {
		return compare(resolveClass(o1), resolveClass(o2));
	}
}
