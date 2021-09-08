package com.andyadc.codeblocks.common.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Objects;
import java.util.function.Consumer;

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
		return asArray(Collections.list(enumeration), componentType);
	}

	public static <E> E[] asArray(Iterable<E> elements, Class<?> componentType) {
		return asArray(CollectionUtils.newArrayList(elements), componentType);
	}

	public static <E> E[] asArray(Collection<E> collection, Class<?> componentType) {
		return collection.toArray((E[]) Array.newInstance(componentType, 0));
	}

	public static <T> void iterate(T[] values, Consumer<T> consumer) {
		Objects.requireNonNull(values, "The argument must not be null!");
		for (T value : values) {
			consumer.accept(value);
		}
	}
}
