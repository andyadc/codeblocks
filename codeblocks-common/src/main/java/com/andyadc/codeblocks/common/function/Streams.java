package com.andyadc.codeblocks.common.function;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.andyadc.codeblocks.common.function.Predicates.and;
import static com.andyadc.codeblocks.common.function.Predicates.or;
import static java.util.stream.Collectors.toList;

/**
 * The utilities class for {@link Stream}
 *
 * @since 1.0.0
 */
public interface Streams {

	static <T> Stream<T> stream(Iterable<T> iterable) {
		return StreamSupport.stream(iterable.spliterator(), false);
	}

	static <T, S extends Iterable<T>> Stream<T> filterStream(S values, Predicate<T> predicate) {
		return stream(values).filter(predicate);
	}

	static <T, S extends Iterable<T>> List<T> filterList(S values, Predicate<T> predicate) {
		return filterStream(values, predicate).collect(toList());
	}

	static <T, S extends Iterable<T>> Set<T> filterSet(S values, Predicate<T> predicate) {
		// new Set with insertion order
		return filterStream(values, predicate).collect(LinkedHashSet::new, Set::add, Set::addAll);
	}

	static <T, S extends Iterable<T>> S filter(S values, Predicate<T> predicate) {
		final boolean isSet = Set.class.isAssignableFrom(values.getClass());
		return (S) (isSet ? filterSet(values, predicate) : filterList(values, predicate));
	}

	static <T, S extends Iterable<T>> S filterAll(S values, Predicate<T>... predicates) {
		return filter(values, and(predicates));
	}

	static <T, S extends Iterable<T>> S filterAny(S values, Predicate<T>... predicates) {
		return filter(values, or(predicates));
	}

	static <T> T filterFirst(Iterable<T> values, Predicate<T>... predicates) {
		return stream(values)
			.filter(and(predicates))
			.findFirst()
			.orElse(null);
	}
}


