package com.andyadc.codeblocks.common.function;

import com.andyadc.codeblocks.common.util.ExceptionUtils;

import java.util.function.Consumer;

/**
 * A function interface for {@link Consumer} with {@link Throwable}
 *
 * @param <T> the type to be consumed
 * @see Consumer
 * @see Throwable
 */
@FunctionalInterface
public interface ThrowableConsumer<T> {

	/**
	 * Executes {@link ThrowableConsumer}
	 *
	 * @param t        the input argument
	 * @param consumer {@link ThrowableConsumer}
	 * @throws RuntimeException wrap {@link Exception} to {@link RuntimeException}
	 */
	static <T> void execute(T t, ThrowableConsumer consumer) throws RuntimeException {
		execute(t, consumer, RuntimeException.class);
	}

	/**
	 * Executes {@link ThrowableConsumer}
	 *
	 * @param t        the input argument
	 * @param consumer {@link ThrowableConsumer}
	 * @throws T wrap {@link Throwable} to the specified {@link Throwable} type
	 */
	static <E, T extends Throwable> void execute(E t, ThrowableConsumer consumer, Class<T> throwableType) throws T {
		try {
			consumer.accept(t);
		} catch (Throwable e) {
			throw ExceptionUtils.wrapThrowable(e, throwableType);
		}
	}

	/**
	 * Performs this operation on the given argument.
	 *
	 * @param t the input argument
	 * @throws Throwable if met with error
	 */
	void accept(T t) throws Throwable;
}
