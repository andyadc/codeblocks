package com.andyadc.codeblocks.common.filter;

/**
 * {@link Filter} interface
 *
 * @param <T> the type of Filtered object
 * @version 1.0.0
 * @see Filter
 * @since 1.0.0
 */
@FunctionalInterface
public interface Filter<T> {
	/**
	 * Does accept filtered object?
	 *
	 * @param filteredObject filtered object
	 * @return
	 */
	boolean accept(T filteredObject);
}
