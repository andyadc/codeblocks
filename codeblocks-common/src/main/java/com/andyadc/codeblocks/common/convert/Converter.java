package com.andyadc.codeblocks.common.convert;

import com.andyadc.codeblocks.common.lang.Prioritized;

import java.util.function.Function;

@FunctionalInterface
public interface Converter<S, T> extends Function<S, T>, Prioritized {

	/**
	 * Convert the source-typed value to the target-typed value
	 *
	 * @param source the source-typed value
	 * @return the target-typed value
	 */
	T convert(S source);

	@Override
	default T apply(S s) {
		return convert(s);
	}
}
