package com.andyadc.codeblocks.common.util;

import java.lang.reflect.Array;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * The utilities class for {@link Array}
 */
public abstract class ArrayUtils extends BaseUtils {

	public static <T> T[] of(T... values) {
		return values;
	}

	public static <T> int length(T... values) {
		return values == null ? 0 : values.length;
	}

	public static <T> boolean isEmpty(T... values) {
		return length(values) == 0;
	}

	public static <T> boolean isNotEmpty(T... values) {
		return !isEmpty(values);
	}

	public static <E> E[] asArray(Enumeration<E> enumeration, Class<?> componentType) {
		List<E> list = Collections.list(enumeration);
		return list.toArray((E[]) Array.newInstance(componentType, 0));
	}
}
