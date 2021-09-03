package com.andyadc.codeblocks.common.function;

import com.andyadc.codeblocks.common.lang.ExceptionUtils;

@FunctionalInterface
public interface ThrowableSupplier<T> {

	/**
	 * Executes {@link ThrowableSupplier}
	 *
	 * @param supplier {@link ThrowableSupplier}
	 * @param <T>      the supplied type
	 * @return the result after execution
	 * @throws RuntimeException
	 */
	static <T> T execute(ThrowableSupplier<T> supplier) throws RuntimeException {
		return execute(supplier, RuntimeException.class);
	}

	static <T, E extends Throwable> T execute(ThrowableSupplier<T> supplier, Class<E> errorType) throws E {
		T result;
		try {
			result = supplier.get();
		} catch (Throwable e) {
			throw ExceptionUtils.wrapThrowable(e, errorType);
		}
		return result;
	}

	/**
	 * Applies this function to the given argument.
	 *
	 * @return the supplied result
	 * @throws Throwable if met with any error
	 */
	T get() throws Throwable;
}
