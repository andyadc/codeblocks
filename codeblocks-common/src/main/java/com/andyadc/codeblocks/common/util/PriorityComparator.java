package com.andyadc.codeblocks.common.util;

import javax.annotation.Priority;
import java.util.Comparator;
import java.util.Objects;

/**
 * The {@link Comparator} for the annotation {@link Priority}
 * <p>
 * The less value of {@link Priority}, the more priority
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @see Priority
 * @since 1.0.0
 */
public class PriorityComparator implements Comparator<Object> {

	/**
	 * Singleton instance of {@link PriorityComparator}
	 */
	public static final PriorityComparator INSTANCE = new PriorityComparator();
	private static final Class<Priority> PRIORITY_CLASS = Priority.class;

	public static int compare(Class<?> type1, Class<?> type2) {
		if (Objects.equals(type1, type2)) {
			return 0;
		}

		Priority priority1 = AnnotationUtils.findAnnotation(type1, PRIORITY_CLASS);
		Priority priority2 = AnnotationUtils.findAnnotation(type2, PRIORITY_CLASS);

		if (priority1 != null && priority2 != null) {
			return Integer.compare(priority1.value(), priority2.value());
		} else if (priority1 != null && priority2 == null) {
			return -1;
		} else if (priority1 == null && priority2 != null) {
			return 1;
		}
		// else
		return 0;
	}

	@Override
	public int compare(Object o1, Object o2) {
		return compare(o1.getClass(), o2.getClass());
	}

}
