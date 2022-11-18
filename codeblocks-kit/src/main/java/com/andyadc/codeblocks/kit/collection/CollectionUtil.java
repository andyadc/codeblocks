package com.andyadc.codeblocks.kit.collection;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class CollectionUtil {

	/**
	 * <code>CollectionUtil</code> should not normally be instantiated.
	 */
	private CollectionUtil() {
	}

	/**
	 * 交集
	 */
	public static Collection<?> intersect(Collection<?> coll1, Collection<?> coll2) {
		Map<?, ?> tempMap = coll2.parallelStream().collect(
			Collectors.toMap(Function.identity(), Function.identity(), (oldData, newData) -> newData)
		);
		return coll1.parallelStream().filter(tempMap::containsKey).collect(Collectors.toList());
	}

	/**
	 * coll1中有, coll2中无的
	 */
	public static Collection<?> sub(Collection<?> coll1, Collection<?> coll2) {
		Map<?, ?> tempMap = coll2.parallelStream().collect(
			Collectors.toMap(Function.identity(), Function.identity(), (oldData, newData) -> newData)
		);
		return coll1.parallelStream().filter(str -> !tempMap.containsKey(str)).collect(Collectors.toList());
	}

	/**
	 * Null-safe check if the specified collection is empty.
	 * <p>
	 * Null returns true.
	 *
	 * @param coll the collection to check, may be null
	 * @return true if empty or null
	 */
	public static boolean isEmpty(final Collection<?> coll) {
		return coll == null || coll.isEmpty();
	}

	/**
	 * Null-safe check if the specified collection is not empty.
	 * <p>
	 * Null returns false.
	 *
	 * @param coll the collection to check, may be null
	 * @return true if non-null and non-empty
	 */
	public static boolean isNotEmpty(final Collection<?> coll) {
		return !isEmpty(coll);
	}

	//-----------------------------------------------------------------------

	/**
	 * Reverses the order of the given array.
	 *
	 * @param array the array to reverse
	 */
	public static void reverseArray(final Object[] array) {
		int i = 0;
		int j = array.length - 1;
		Object tmp;

		while (j > i) {
			tmp = array[j];
			array[j] = array[i];
			array[i] = tmp;
			j--;
			i++;
		}
	}
}
