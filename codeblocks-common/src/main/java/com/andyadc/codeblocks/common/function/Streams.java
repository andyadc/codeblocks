package com.andyadc.codeblocks.common.function;

import com.andyadc.codeblocks.common.util.CollectionUtils;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
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

	static <E, L extends List<E>> List<E> filter(L values, Predicate<E> predicate) {
		final L result;
		if (predicate == null) {
			result = values;
		} else {
			result = (L) filterStream(values, predicate).collect(toList());
		}
		return Collections.unmodifiableList(result);
	}

	static <E, S extends Set<E>> Set<E> filter(S values, Predicate<E> predicate) {
		final S result;
		if (predicate == null) {
			result = values;
		} else {
			result = (S) filterStream(values, predicate).collect(Collectors.toSet());
		}
		return Collections.unmodifiableSet(result);
	}

	static <E, Q extends Queue<E>> Queue<E> filter(Q values, Predicate<E> predicate) {
		final Q result;
		if (predicate == null) {
			result = values;
		} else {
			result = (Q) filterStream(values, predicate).collect(LinkedList::new, List::add, List::addAll);
		}
		return CollectionUtils.unmodifiableQueue(result);
	}

	static <T, S extends Iterable<T>> S filter(S values, Predicate<T> predicate) {
		if (CollectionUtils.isSet(values)) {
			return (S) filter((Set) values, predicate);
		} else if (CollectionUtils.isList(values)) {
			return (S) filter((List) values, predicate);
		} else if (CollectionUtils.isQueue(values)) {
			return (S) filter((Queue) values, predicate);
		}
		String message = String.format("The '%s' type can't be supported!", values.getClass().getName());
		throw new UnsupportedOperationException(message);
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

	static <T, R> List<R> map(List<T> values, Function<T, R> mapper) {
		return stream(values)
			.map(mapper)
			.collect(Collectors.toList());
	}

	static <T, R> Set<R> map(Set<T> values, Function<T, R> mapper) {
		return stream(values)
			.map(mapper)
			.collect(Collectors.toSet());
	}
}


