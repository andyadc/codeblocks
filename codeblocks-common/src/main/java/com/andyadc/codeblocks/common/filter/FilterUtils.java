package com.andyadc.codeblocks.common.filter;

import com.andyadc.codeblocks.common.util.CollectionUtils;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public abstract class FilterUtils {

	private FilterUtils() {
	}

	/**
	 * Filter {@link Iterable} object to List
	 *
	 * @param iterable {@link Iterable} object
	 * @param filter   {@link Filter} object
	 * @param <E>      The filtered object type
	 */
	public static <E> List<E> filter(Iterable<E> iterable, Filter<E> filter) {
		return filter(iterable, FilterOperator.AND, filter);
	}

	/**
	 * Filter {@link Iterable} object to List
	 *
	 * @param iterable       {@link Iterable} object
	 * @param filterOperator {@link FilterOperator}
	 * @param filters        {@link Filter} array objects
	 * @param <E>            The filtered object type
	 */
	public static <E> List<E> filter(Iterable<E> iterable, FilterOperator filterOperator, Filter<E>... filters) {
		List<E> list = CollectionUtils.newArrayList(iterable);
		Iterator<E> iterator = list.iterator();
		while (iterator.hasNext()) {
			E element = iterator.next();
			if (!filterOperator.accept(element, filters)) {
				iterator.remove();
			}
		}
		return Collections.unmodifiableList(list);
	}
}
