package com.andyadc.codeblocks.common.function;

import java.util.function.Predicate;

/**
 * The utilities class for Java {@link Predicate}
 */
public interface Predicates {

	Predicate<?>[] EMPTY_ARRAY = new Predicate[0];

	Predicate<?> ALWAYS_TRUE = e -> true;

	Predicate<?> ALWAYS_FALSE = e -> false;

	/**
	 * {@link Predicate} always return <code>true</code>
	 *
	 * @param <T> the type to test
	 * @return <code>true</code>
	 */
	static <T> Predicate<T> alwaysTrue() {
		return (Predicate<T>) ALWAYS_TRUE;
	}

	/**
	 * {@link Predicate} always return <code>false</code>
	 *
	 * @param <T> the type to test
	 * @return <code>false</code>
	 */
	static <T> Predicate<T> alwaysFalse() {
		return (Predicate<T>) ALWAYS_FALSE;
	}

	/**
	 * a composed predicate that represents a short-circuiting logical AND of {@link Predicate predicates}
	 *
	 * @param predicates {@link Predicate other predicates}
	 * @param <T>        the type to test
	 * @return non-null
	 */
	static <T> Predicate<T> and(Predicate<? super T>... predicates) {
		Predicate<T> andPredicate = alwaysTrue();
		for (Predicate<? super T> p : predicates) {
			andPredicate = andPredicate.and(p);
		}
		return andPredicate;
	}

	/**
	 * a composed predicate that represents a short-circuiting logical OR of {@link Predicate predicates}
	 *
	 * @param predicates {@link Predicate other predicates}
	 * @param <T>        the detected type
	 * @return non-null
	 */
	static <T> Predicate<T> or(Predicate<? super T>... predicates) {
		Predicate<T> orPredicate = alwaysTrue();
		for (Predicate<? super T> p : predicates) {
			orPredicate = orPredicate.or(p);
		}
		return orPredicate;
	}
}
