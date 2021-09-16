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
 */
public interface Streams {

	static <T> Stream<T> stream(Iterable<T> iterable) {
		return StreamSupport.stream(iterable.spliterator(), false);
	}

	static <T, I extends Iterable<T>> Stream<T> filterStream(I values, Predicate<? super T> predicate) {
		return filterStream(values, predicate, Predicates.EMPTY_ARRAY);
	}

	static <T, I extends Iterable<T>> Stream<T> filterStream(I values, Predicate<? super T>... predicates) {
		return filterStream(values, Predicates.alwaysTrue(), predicates);
	}

	static <T, I extends Iterable<T>> Stream<T> filterStream(I values, Predicate<? super T> predicate,
															 Predicate<? super T>... otherPredicates) {
		return stream(values).filter(and(predicate, otherPredicates));
	}

	static <E, L extends List<E>> List<E> filter(L values, Predicate<? super E> predicate) {
		return filter(values, predicate, Predicates.EMPTY_ARRAY);
	}

	static <E, L extends List<E>> List<E> filter(L values, Predicate<? super E>... predicates) {
		return filter(values, Predicates.alwaysTrue(), predicates);
	}

	static <E, L extends List<E>> List<E> filter(L values, Predicate<? super E> predicate, Predicate<? super E>... otherPredicates) {
		final L result;
		if (predicate == null) {
			result = values;
		} else {
			result = (L) filterStream(values, predicate, otherPredicates).collect(toList());
		}
		return Collections.unmodifiableList(result);
	}

	static <E, S extends Set<E>> Set<E> filter(S values, Predicate<? super E> predicate) {
		return filter(values, predicate, Predicates.EMPTY_ARRAY);
	}

	static <E, S extends Set<E>> Set<E> filter(S values, Predicate<? super E>... predicates) {
		return filter(values, Predicates.alwaysTrue(), predicates);
	}

	static <E, S extends Set<E>> Set<E> filter(S values, Predicate<? super E> predicate,
											   Predicate<? super E>... otherPredicates) {
		final S result;
		if (predicate == null) {
			result = values;
		} else {
			result = (S) filterStream(values, predicate, otherPredicates).collect(Collectors.toSet());
		}
		return Collections.unmodifiableSet(result);
	}

	static <E, Q extends Queue<E>> Queue<E> filter(Q values, Predicate<? super E> predicate) {
		return filter(values, predicate, Predicates.EMPTY_ARRAY);
	}

	static <E, Q extends Queue<E>> Queue<E> filter(Q values, Predicate<? super E>... predicates) {
		return filter(values, Predicates.alwaysTrue(), predicates);
	}

	static <E, Q extends Queue<E>> Queue<E> filter(Q values, Predicate<? super E> predicate,
												   Predicate<? super E>... otherPredicates) {
		final Q result;
		if (predicate == null) {
			result = values;
		} else {
			result = (Q) filterStream(values, predicate, otherPredicates)
				.collect(LinkedList::new, List::add, List::addAll);
		}
		return CollectionUtils.unmodifiableQueue(result);
	}

	static <T, S extends Iterable<T>> S filter(S values, Predicate<? super T> predicate) {
		return (S) filter(values, predicate, Predicates.EMPTY_ARRAY);
	}

	static <T, S extends Iterable<T>> S filter(S values, Predicate<? super T>... predicates) {
		return filter(values, Predicates.alwaysTrue(), predicates);
	}

	static <T, S extends Iterable<T>> S filter(S values,
											   Predicate<? super T> predicate,
											   Predicate<? super T>... otherPredicates) {
		if (CollectionUtils.isSet(values)) {
			return (S) filter((Set) values, predicate, otherPredicates);
		} else if (CollectionUtils.isList(values)) {
			return (S) filter((List) values, predicate, otherPredicates);
		} else if (CollectionUtils.isQueue(values)) {
			return (S) filter((Queue) values, predicate, otherPredicates);
		}
		String message = String.format("The '%s' type can't be supported!", values.getClass().getName());
		throw new UnsupportedOperationException(message);
	}

	static <T, S extends Iterable<T>> S filterAny(S values, Predicate<? super T>... predicates) {
		return filterAny(values, Predicates.alwaysTrue(), predicates);
	}

	static <T, S extends Iterable<T>> S filterAny(S values, Predicate<? super T> predicate,
												  Predicate<? super T>... otherPredicates) {
		return filter(values, or(predicate, otherPredicates));
	}

	static <T> T filterFirst(Iterable<T> values, Predicate<? super T> predicate) {
		return (T) filterFirst(values, predicate, Predicates.EMPTY_ARRAY);
	}

	static <T> T filterFirst(Iterable<T> values, Predicate<? super T>... predicates) {
		return filterFirst(values, Predicates.alwaysTrue(), predicates);
	}

	static <T> T filterFirst(Iterable<T> values, Predicate<? super T> predicate,
							 Predicate<? super T>... otherPredicates) {
		return stream(values)
			.filter(and(predicate, otherPredicates))
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


